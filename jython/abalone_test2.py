"""
Single run version using network builder. 

Implementation of backprop, randomized hill climbing, simulated annealing, and genetic algorithm to
find optimal weights to a neural network that is classifying abalone as having either fewer
or more than 15 rings.  

Based on AbaloneTest.java by Hannah Lau

Run the bat or shell file first to add the ABAGAIL jar to your classpath then run:  
jython abalone_test2.py > out.txt
"""
from __future__ import with_statement
import os
import csv
import time
from func.nn.backprop import BackPropagationNetwork, ADAM
from func.nn.activation import RELU
from func.nn import OptNetworkBuilder, BackpropNetworkBuilder
from shared import SumOfSquaresError, DataSet, Instance
from opt.example import NeuralNetworkOptimizationProblem

INPUT_FILE = os.path.join("abalone.txt")
INPUT_LAYER = 7
HIDDEN_LAYER = 5
OUTPUT_LAYER = 1
TRAINING_ITERATIONS = 1000

def initialize_instances():
    """Read the abalone.txt CSV data into a list of instances."""
    instances = []
    # Read in the abalone.txt CSV file
    with open(INPUT_FILE, "r") as abalone:
        reader = csv.reader(abalone)
        for row in reader:
            instance = Instance([float(value) for value in row[:-1]])
            instance.setLabel(Instance(0 if float(row[-1]) < 15 else 1))
            instances.append(instance)
    return instances

def main():
    """Run algorithms on the abalone dataset."""
    instances = initialize_instances()
    INPUT_LAYER = 7
    HIDDEN_LAYER = 5   
    OUTPUT_LAYER = 1   #OUTPUT_LAYER must match dataset (e.g., 1 binary, >1 multiclass) 
    results = ""
    start = time.time()
    correct = 0
    incorrect = 0
    
    #BACKPROP NETWORK
#    network = BackpropNetworkBuilder()\
#      .withLayers([INPUT_LAYER,HIDDEN_LAYER,OUTPUT_LAYER])\
#      .withDataSet(DataSet(instances))\
#      .withIterations(TRAINING_ITERATIONS)\
#      .withUpdateRule(ADAM())\
#      .withActivationFunction(RELU())\
#      .withErrorMeasure(SumOfSquaresError())\
#      .train()
        
    #OPT NETWORK - can also use withRHC() or withGA(popSize, toMate, toMutate)
    network = OptNetworkBuilder()\
        .withLayers([INPUT_LAYER,HIDDEN_LAYER,OUTPUT_LAYER])\
        .withDataSet(DataSet(instances))\
        .withSA(15000, .95)\
        .withIterations(TRAINING_ITERATIONS)\
        .train()
    
    end = time.time()
    training_time = end - start
    start = time.time()
    for instance in instances:
        network.setInputValues(instance.getData())
        network.run()
        actual = instance.getLabel().getContinuous()
        predicted = network.getOutputValues().get(0)
        if abs(predicted - actual) < 0.5:
            correct += 1
        else:
            incorrect += 1
    
    end = time.time()
    testing_time = end - start  
    results += "\nResults: \nCorrectly classified %d instances." % (correct)
    results += "\nIncorrectly classified %d instances.\nPercent correctly classified: %0.03f%%" % (incorrect, float(correct)/(correct+incorrect)*100.0)
    results += "\nTraining time: %0.03f seconds" % (training_time,)
    results += "\nTesting time: %0.03f seconds\n" % (testing_time,) 
    print(results)

if __name__ == "__main__":
    main()