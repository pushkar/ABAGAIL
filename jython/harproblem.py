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

tune_iterations = 1000
training_iterations = 5000

oa_names = ['RHC', 'SA', 'GA']
log = {
    'find_parameters': {},
    'train_iteration': {}
    }

comb_parameters={'RHC':[0]}

"""store the index into the combination array"""
best_parameters={ 'RHC':-1,'SA':-1, 'GA':-1, }
best_error={ 'RHC':-1,'SA':-1, 'GA':-1, }

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
    for oa_name in oa_names:
        """reserve space in log"""
        log['find_parameters'][oa_name]=[x for x in comb_parameters[oa_name]]

        """training all the combinations"""
        for idx, param in enumerate(comb_parameters[oa_name]):
            network = factory.createClassificationNetwork([input_layer,
                hidden_layer, output_layer])
            nnop = NeuralNetworkOptimizationProblem(dataset, network, measure)
            oa = load_oa(oa_name, param, nnop)
            """train the oa"""
            training_log = train(oa, network, oa_name, trains, tests, measure,
                tune_iterations)
            training_log['parameters'] = param
            test_error=training_log['training'][-1]['test_error']
            """book keeping to find the best configuration"""
            if best_error[oa_name] < 0 or best_error[oa_name] > test_error:
                best_error[oa_name] = test_error
                best_parameters[oa_name] = idx
            """put the log into the global log object"""
            log['find_parameters'][oa_name][idx]=training_log
        print "the best parameter for {} is {}".format(oa_name, comb_parameters[oa_name][best_parameters[oa_name]])

"""find train/val vs iterations"""
def train_iteration(trains, tests, dataset):
    factory = BackPropagationNetworkFactory()
    """the measure"""
    measure = SumOfSquaresError()
    for oa_name in oa_names:
        network = factory.createClassificationNetwork([input_layer,
            hidden_layer, output_layer])
        nnop = NeuralNetworkOptimizationProblem(dataset, network, measure)
        oa = load_oa(oa_name, comb_parameters[oa_name][best_parameters[oa_name]],
            nnop)
        training_log = train(oa, network, oa_name, trains, tests, measure,
            training_iterations)
        """just remember the log"""
        log['train_iteration'][oa_name]=training_log


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
output a string of (time, train_err, test_err) indexed by iteration
"""
def train(oa, network, oa_name, trains, tests, measure, training_iterations):
    """
    scheme for log:
    log = {
        'training':[
            0:{
                'time',
                'training_error',
                'validation_error',
            },
            ...
        ],
        'result':{
            'correct_classifications'
            'incorrect_classifications'
        }
    }
    """
    log={'training':[x for x in xrange(training_iterations)]}
    print 'training ', oa_name
    """
    Train the given optimization algorithm
    1.
    """
    """record start time"""
    start_time = datetime.today()
    """train the algorithm"""
    for i in xrange(training_iterations):
        """calculate training error"""
        fitness = oa.train()
        training_error = 1 / fitness / len(trains)
        """calculate test error"""
        test_error = 0.0
        for test in tests:
            network.setInputValues(test.getData())
            network.run()
            output_label = Instance(network.getOutputValues())
            test_error += measure.value(output_label, test)
        test_error /= len(tests)

        delta = datetime.today() - start_time
        log['training'][i]={
            'time': delta.seconds,
            'training_error': training_error,
            'test_error': test_error,
            }

    """test the final network"""
    optimal_instance = oa.getOptimal()
    network.setWeights(optimal_instance.getData())
    correct, incorrect = evaluate_network(network, tests)
    log['result']={
        'correct classifications': correct,
        'incorrect classifications': incorrect
        }
    print "result of {}: correct {} incorrect {}".format(oa_name, correct,
            incorrect)
    return log

"""
Given a network and list of instances, the ouput of the network is evaluated and
a decimal error value is returned
"""
def evaluate_network(network, instances):
    correct = 0
    incorrect = 0
    for instance in instances:
        network.setInputValues(instance.getData())
        network.run()

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
    return (correct, incorrect)

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
    train_iteration(trains, tests, dataset)

    json_file = sys.argv[1]
    with open(json_file, 'w') as f:
        f.write(json.dumps(log))

if __name__ == "__main__":
    main()
