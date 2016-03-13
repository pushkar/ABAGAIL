import csv
from datetime import datetime, timedelta
import json
import sys
from math import ceil

from dist import *
from opt import *
from opt.example import *
from opt.ga import *
from shared import *
from func.nn.backprop import *

from utils import argmax_index

training_file = './data/credit-g/credit-g-70.csv'
testing_file = './data/credit-g/credit-g-30.csv'
trains = None
tests = None
dataset = None

log_dir = './logs/'
plot_dir = './data/plot/'

input_layer = 23
hidden_layer = 20
output_layer = 1

"""time in seconds"""
tune_time = 20
training_time = 120

oa_names = ['RHC', 'SA', 'GA']
log = {
    'find_parameters': {},
    'train_time': {}
    }

comb_parameters={}

"""store the index into the combination array"""
best_parameters={ 'SA':-1, 'GA':-1, }
best_error={ 'SA':-1, 'GA':-1, }

"""
the plan for training:
    1. finding the best combination of parameters
    1.1 SA: temperature from 10^3 to 10^5, decay from 0.5 to 0.95
    1.2 GA: population from 100 to 200 at step of 20, num mate is always half of
            population, mutate is 2, 4, 6, 8, 10% of population
    1.3 rhc: nothing to configure
    1.4 evaluation metric: fix training 1000 iterations and fix the one with
    best CV error for use later
    2. evaluate performance over iterations, but plot against time
    2.1 SA, GA, rhc, backprop
"""

"""generate the list of combination of parameters"""
def generate_comb_parameters():
    def comb_sa():
        result = []
        for t in (1000, 10000, 100000, 1000000, 10000000, 100000000):
            for cooling in (0.5, 0.6, 0.7, 0.8, 0.9, 0.95):
                result.append({'t':t, 'cooling':cooling})
        return result

    def comb_ga():
        result = []
        for population in (100, 120, 140, 160, 180, 200):
            for mutate in (0.02, 0.04, 0.06, 0.08, 0.1):
                result.append({
                    'population':population,
                    'mate':population/2,
                    'mutate':int(ceil(mutate*population))})
        return result
    comb_parameters['SA']=comb_sa()
    comb_parameters['GA']=comb_ga()


"""load the parameters and create the algorithm"""
def load_oa(oa_name, param, nnop):
    def load_sa(param, nnop):
        t = param['t']
        cooling = param['cooling']
        return SimulatedAnnealing(t, cooling, nnop)

    def load_ga(param, nnop):
        population=param['population']
        mate=param['mate']
        mutate=param['mutate']
        return StandardGeneticAlgorithm(population, mate, mutate, nnop)

    def load_rhc(nnop):
        return RandomizedHillClimbing(nnop)

    if oa_name == 'SA':
        return load_sa(param, nnop)
    elif oa_name == 'GA':
        return load_ga(param, nnop)
    else:
        return load_rhc(nnop)

"""find the best parameters for the networks and save in best_parameters
dictionary"""
def find_parameters(trains, tests, dataset):
    """the network factory"""
    factory = BackPropagationNetworkFactory()
    """the measure"""
    measure = SumOfSquaresError()
    for oa_name in comb_parameters:
        """reserve space in log"""
        log['find_parameters'][oa_name]=[x for x in comb_parameters[oa_name]]

        """training all the combinations"""
        for idx, param in enumerate(comb_parameters[oa_name]):
            network = factory.createClassificationNetwork([input_layer,
                hidden_layer, output_layer])
            nnop = NeuralNetworkOptimizationProblem(dataset, network, measure)
            oa = load_oa(oa_name, param, nnop)
            """train the oa"""
            training_log = train(oa, network, measure, oa_name, trains, tests,
                training_time=tune_time, trainer_returns_fitness=True)
            training_log['parameters'] = param
            """average over the last 20 iterations"""
            last_errors=[x['test_error'] for x in training_log['training'][-20:]]
            test_error = sum(last_errors)/len(last_errors)
            """book keeping to find the best configuration"""
            if best_error[oa_name] < 0 or best_error[oa_name] > test_error:
                best_error[oa_name] = test_error
                best_parameters[oa_name] = idx
            """put the log into the global log object"""
            log['find_parameters'][oa_name][idx]=training_log
        print "the best parameter for {} is {}".format(oa_name, comb_parameters[oa_name][best_parameters[oa_name]])

"""find train/val vs iterations"""
def train_time(trains, tests, dataset):
    factory = BackPropagationNetworkFactory()
    """the measure"""
    measure = SumOfSquaresError()
    for oa_name in oa_names:
        network = factory.createClassificationNetwork([input_layer,
            hidden_layer, output_layer])
        nnop = NeuralNetworkOptimizationProblem(dataset, network, measure)
        if oa_name in comb_parameters:
            oa = load_oa(oa_name, comb_parameters[oa_name][best_parameters[oa_name]],
                nnop)
        else:
            oa = load_oa(oa_name, None, nnop)
        training_log = train(oa, network, measure, oa_name, trains, tests,
            training_time = training_time, trainer_returns_fitness=True)
        if oa_name in comb_parameters:
            training_log['parameters'] = comb_parameters[oa_name][best_parameters[oa_name]]
        """just remember the log"""
        log['train_time'][oa_name]=training_log

"""train the neuralnetwork"""
def train_nn(trains, tests, dataset):
    """
    1. initialize the neurwlnetwork
    2. train
    3. evaluate the network
    """
    factory = BackPropagationNetworkFactory()
    network = factory.createClassificationNetwork([input_layer, hidden_layer,
        output_layer])
    measure = SumOfSquaresError()
    trainer = BatchBackPropagationTrainer(dataset, network, measure,
        RPROPUpdateRule())

    training_log=train(trainer, network, measure, 'NN', trains, tests,
        training_time=training_time)
    log['train_time']['NN']=training_log
"""
1. open the file
2. return training instances and test instances
"""
def initialize_instances():
    files = (training_file, testing_file)
    def process_one_file(csvfile):
        instances = []
        with open(csvfile, 'r') as f:
            cr = csv.reader(f, delimiter=',')
            for row in cr:
                """
                last one is label,
                others are data
                """
                instance = Instance([float(x) for x in row[:-1]])
                """read the class"""
                cls = int(row[-1])
                """convert class to label"""
                #label = [0.0,0.0]
                #label[cls-1]=1.0
                """binary classification"""
                label = cls - 1
                instance.setLabel(Instance(label))
                instances.append(instance)
        return instances

    return tuple([process_one_file(csvfile) for csvfile in files])

"""
train the oa for number of iterations
return a log dictionary whose scheme is specified below
"""
def train(trainer, network, measure, oa_name, trains, tests, training_iterations=None,
        training_time=None, trainer_returns_fitness=False):
    """
    scheme for log:
    log = {
        'training':[
            0:{
                'time',
                'training_error',
                'test_error',
            },
            ...
        ],
        'result':{
            'correct_classifications'
            'incorrect_classifications'
        }
    }
    """
    log={'training':[]}
    print 'training ', oa_name
    """
    Train the given optimization algorithm
    1.
    """
    """record start time"""
    start_time = datetime.today()
    """train the algorithm"""
    def train_one_iteration():
        """calculate training error"""
        fitness = trainer.train()
        """
        the returned fitness is not normalized by NeuralNetworkEvaluationFunction
        but the error returned from BatchBackPropagationTrainer is
        """
        training_error = 1 / fitness / len(trains) if trainer_returns_fitness else fitness
        #print training_error
        """calculate test error"""
        correct, incorrect, test_error = evaluate_network(network, tests, measure)

        delta = datetime.today() - start_time
        delta = delta.days * 3600 * 24 + delta.seconds + delta.microseconds / 1000000.0
        log['training'].append({
            'time': delta,
            'training_error': training_error,
            'correct': correct,
            'incorrect': incorrect,
            'test_error': test_error,
            })
    if training_iterations:
        for i in xrange(training_iterations):
            train_one_iteration()
    elif training_time:
        while True:
            delta = datetime.today() - start_time
            delta = delta.days * 3600 * 24 + delta.seconds + delta.microseconds / 1000000.0
            train_one_iteration()
            if delta > training_time:
                break
    else:
        raise "must specify at least one of training_iterations/training_time"

    """
    find the best performance point
    """
    best_error = -1
    for record in log['training']:
        if best_error == -1 or best_error > record['test_error']:
            log['result']=record
            best_error = record['test_error']
    print "result of {}: {}".format(oa_name, log['result'])
    return log

"""
Given a network and list of instances, the ouput of the network is evaluated and
a decimal error value is returned
"""
def evaluate_network(network, instances, measure):
    correct = 0
    incorrect = 0
    test_error = 0.0
    for instance in instances:
        network.setInputValues(instance.getData())
        network.run()
        output_label = Instance(network.getOutputValues())
        test_error += measure.value(output_label, instance)

        """multiclass"""
        #actual = instance.getLabel().getData().argMax()
        #predicted = network.getOutputValues().argMax()
        #print "instanceing actual: {} {} predicted: {} {}".format(actual, instance.getLabel().getData(), predicted, network.getOutputValues())

        #raw_input()
        """binaryclass"""
        """use a trick to extract the value"""
        actual = instance.getLabel().getData().sum()
        predicted = 0.0 if network.getOutputValues().sum() < 0.5 else 1.0

        if predicted == actual:
            correct += 1
        else:
            incorrect += 1
    test_error /= len(instances)
    return (correct, incorrect, test_error)

def main():
    """
    1. generate training and test sets
    2. initialize neuralnetwork optimization problems
    3. initialize optimization algorithms
    4. train the algorithms
    """
    """get the instances"""
    trains, tests = initialize_instances()
    dataset = DataSet(trains)
    """generate the comb parameters"""
    generate_comb_parameters()
    """find the best configuration of networks"""
    find_parameters(trains, tests, dataset)
    """find relationship between train/val with the best configurations"""
    train_time(trains, tests, dataset)
    """train the backprop net"""
    train_nn(trains, tests, dataset)

    json_file = sys.argv[1]
    with open(json_file, 'w') as f:
        f.write(json.dumps(log))

if __name__ == "__main__":
    main()
