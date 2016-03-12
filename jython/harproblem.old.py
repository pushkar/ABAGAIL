import csv
from datetime import datetime, timedelta
import json
import sys

from dist import *
from opt import *
from opt.example import *
from opt.ga import *
from shared import *
from func.nn.backprop import *

from utils import argmax_index

training_file = './data/credit-g/credit-g-70.csv'
testing_file = './data/credit-g/credit-g-30.csv'

log_dir = './logs/'
plot_dir = './data/plot/'

input_layer = 23
hidden_layer = 20
output_layer = 1

training_iterations = 5000

oa_names = ['RHC', 'SA', 'GA']
log = []

def initialize_instances():
    """
    1. open the file
    2. return instances
    """
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

def train(oa, network, oa_name, trains, tests, measure):
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
        log.append({
            'type': 'training',
            'algorithm': oa_name,
            'time': delta.seconds,
            'iteration': i,
            'training_error': training_error,
            'test_error': test_error,
            })

    """test the final network"""
    correct = 0
    incorrect = 0
    optimal_instance = oa.getOptimal()
    network.setWeights(optimal_instance.getData())
    for test in tests:
        network.setInputValues(test.getData())
        network.run()

        """multiclass"""
        #actual = test.getLabel().getData().argMax()
        #predicted = network.getOutputValues().argMax()
        #print "testing actual: {} {} predicted: {} {}".format(actual, test.getLabel().getData(), predicted, network.getOutputValues())

        #raw_input()
        """binaryclass"""
        """use a trick to extract the value"""
        actual = test.getLabel().getData().sum()
        predicted = 0.0 if network.getOutputValues().sum() < 0.5 else 1.0

        if predicted == actual:
            correct += 1
        else:
            incorrect += 1
    delta = datetime.today() - start_time
    log.append({
        'type': 'result',
        'algorithm': oa_name,
        'correct classifications': correct,
        'incorrect classifications': incorrect
        })
    print "result of {}: correct {} incorrect {}".format(oa_name, correct,
            incorrect)

def main():
    """
    1. generate training and test sets
    2. initialize neuralnetwork optimization problems
    3. initialize optimization algorithms
    4. train the algorithms
    """
    """get the instances"""
    instances, valInstances = initialize_instances()
    dataset = DataSet(instances)
    """create the networks"""
    factory = BackPropagationNetworkFactory()
    networks = map(lambda x: factory.createClassificationNetwork([input_layer,
            hidden_layer, output_layer]), range(3))
    """the measure"""
    measure = SumOfSquaresError()
    """create the nnop"""
    nnop = [NeuralNetworkOptimizationProblem(dataset, network, measure) for
            network in networks]
    """create the oa's"""
    oa =[
            RandomizedHillClimbing(nnop[0]),
            SimulatedAnnealing(1E11, .95, nnop[1]),
            StandardGeneticAlgorithm(200, 100, 10, nnop[2])
        ]

    """names for the optimization algorithms"""
    map(lambda i: train(oa[i], networks[i], oa_names[i], instances, valInstances, measure), range(3))
    json_file = sys.argv[1]
    with open(json_file, 'w') as f:
        f.write(json.dumps(log))

if __name__ == "__main__":
    main()
