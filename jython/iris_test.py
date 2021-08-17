"""
 * Iris nn example using network builder
 *
 * https://archive.ics.uci.edu/ml/datasets/Iris
 *
 * @author John Mansfield
 * @version 1.1
"""

from __future__ import with_statement
import os
from func.nn import OptNetworkBuilder, BackpropNetworkBuilder
from shared import DataSet, Instance
from shared.filt import LabelSplitFilter, DiscreteToBinaryFilter, RandomOrderFilter, TestTrainSplitFilter
from shared.normalizer import StandardMeanAndVariance
from shared.reader import CSVDataSetReader, DataSetReader
from java.io import File

def initialize_data(input_data):
    #import data
    dsr = CSVDataSetReader((File(input_data)).getAbsolutePath())
    ds = dsr.read();
    
    #split last attribute for label
    lsf = LabelSplitFilter()
    lsf.filter(ds)

    #encode label as one-hot array and get outputLayerSize
    dbf = DiscreteToBinaryFilter()
    dbf.filter(ds.getLabelDataSet())
    output_layer_size=dbf.getNewAttributeCount()
    
    #test-train split
    percentTrain=75;
    randomOrderFilter = RandomOrderFilter();
    randomOrderFilter.filter(ds);
    testTrainSplit = TestTrainSplitFilter(percentTrain);
    testTrainSplit.filter(ds);
    train=testTrainSplit.getTrainingSet();
    test=testTrainSplit.getTestingSet();
    
    #standardize data
    smv = StandardMeanAndVariance();
    smv.fit(train);
    smv.transform(train);
    smv.transform(test);

    return train, test, output_layer_size

def run_networks(train, test, output_layer_size):
        
    #build and run backprop network using builder
    network = BackpropNetworkBuilder()\
      .withLayers([25,10,output_layer_size])\
      .withDataSet(train, test)\
      .withIterations(5000)\
      .train()
      
    #build and run RHC opt network using builder 
    network = OptNetworkBuilder()\
        .withLayers([25,10,output_layer_size])\
        .withDataSet(train, test)\
        .withRHC()\
        .withIterations(1000)\
        .train()
    
    #build and run SA opt network using builder 
    network = OptNetworkBuilder()\
        .withLayers([25,10,output_layer_size])\
        .withDataSet(train, test)\
        .withSA(15000, .95)\
        .withIterations(1000)\
        .train()
        
    #build and run GA opt network using builder 
    network = OptNetworkBuilder()\
        .withLayers([25,10,output_layer_size])\
        .withDataSet(train, test)\
        .withGA(300, 150, 50)\
        .withIterations(100)\
        .train()

def main():
    
    input_data="iris.txt"
    train, test, output_layer_size = initialize_data(input_data)
    run_networks(train, test, output_layer_size)

if __name__ == "__main__":
    main()