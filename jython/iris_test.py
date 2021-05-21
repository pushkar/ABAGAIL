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
from shared.filt import LabelSplitFilter, DiscreteToBinaryFilter
from shared.reader import CSVDataSetReader, DataSetReader
from java.io import File

def initialize_data(input_data):
    #import data
    dsr = CSVDataSetReader((File(input_data)).getAbsolutePath())
    ds = dsr.read();
    
    #split last attribute for label
    lsf = LabelSplitFilter()
    lsf.filter(ds)

    #convert label to binary for nn classification and get outputLayerSize
    dbf = DiscreteToBinaryFilter()
    dbf.filter(ds.getLabelDataSet())
    output_layer_size=dbf.getNewAttributeCount()

    return ds, output_layer_size

def run_networks(set, output_layer_size, percent_train):
        
    #build and run backprop network using builder
    network = BackpropNetworkBuilder()\
      .withLayers([25,10,output_layer_size])\
      .withDataSet(set, percent_train)\
      .withIterations(5000)\
      .train()
      
    #build and run RHC opt network using builder 
    network = OptNetworkBuilder()\
        .withLayers([25,10,output_layer_size])\
        .withDataSet(set, percent_train)\
        .withRHC()\
        .withIterations(1000)\
        .train()
    
    #build and run SA opt network using builder 
    network = OptNetworkBuilder()\
        .withLayers([25,10,output_layer_size])\
        .withDataSet(set, percent_train)\
        .withSA(15000, .95)\
        .withIterations(1000)\
        .train()
        
    #build and run GA opt network using builder 
    network = OptNetworkBuilder()\
        .withLayers([25,10,output_layer_size])\
        .withDataSet(set, percent_train)\
        .withGA(300, 150, 50)\
        .withIterations(100)\
        .train()

def main():
    
    input_data="iris.txt"
    set, output_layer_size = initialize_data(input_data)
    percent_train=75 #reserve 25 percent for test data
    run_networks(set, output_layer_size, percent_train)

if __name__ == "__main__":
    main()