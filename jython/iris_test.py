"""
 * Iris nn example using network builder
 *
 * https://archive.ics.uci.edu/ml/datasets/Iris
 *
 * @author John Mansfield
 * @version 1.0
"""

from __future__ import with_statement
import os
from func.nn import OptNetworkBuilder, BackpropNetworkBuilder
from shared import DataSet, Instance
from shared.filt import LabelSplitFilter, DiscreteToBinaryFilter
from shared.reader import CSVDataSetReader, DataSetReader
from java.io import File

def initialize_data():
    
    #import data
    dsr = CSVDataSetReader((File("iris.txt")).getAbsolutePath())
    ds = dsr.read();
    
    #split last attribute for label
    lsf = LabelSplitFilter()
    lsf.filter(ds)

    #convert label to binary for nn classification and get outputLayerSize
    dbf = DiscreteToBinaryFilter()
    dbf.filter(ds.getLabelDataSet())
    output_layer_size=dbf.getNewAttributeCount()

    return output_layer_size, ds

def main():
    
    output_layer_size, set = initialize_data()
    percent_train=75
    
    #create backprop network using builder
    network = BackpropNetworkBuilder()\
      .withLayers([25,10,output_layer_size])\
      .withDataSet(set, percent_train)\
      .withIterations(5000)\
      .train()
        
    #create opt network using builder 
    #can also use withRHC() or withGA(popSize, toMate, toMutate)
#    network = OptNetworkBuilder()\
#        .withLayers([25,10,output_layer_size])\
#        .withDataSet(set, percent_train)\
#        .withSA(15000, .95)\
#        .withIterations(1000)\
#        .train()
 
if __name__ == "__main__":
    main()