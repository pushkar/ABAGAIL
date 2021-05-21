"""
* Grid search example
*
* @author John Mansfield
* @version 1.0
"""

from __future__ import with_statement
import os
import itertools
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

def grid_search(set, output_layer_size, percent_train, temp_range, decay_range):
    for i in itertools.product(temp_range, decay_range):
        print("running SA opt network with temp, decay:", i)
        #build and run SA opt network using builder 
        network = OptNetworkBuilder()\
            .withLayers([25,10,output_layer_size])\
            .withDataSet(set, percent_train)\
            .withSA(i[0], i[1])\
            .withIterations(1000)\
            .train()
        name=str(i[0])+"_"+str(i[1])+'.csv'
        os.rename('SA-NNOut.csv', name) 
        
def main():
    input_data="iris.txt"
    set, output_layer_size = initialize_data(input_data)
    percent_train=75 #reserve 25 percent for test data
    temp_range=[1000, 10000, 100000]
    decay_range=[.4, .7, .9]
    grid_search(set, output_layer_size, percent_train, temp_range, decay_range)

if __name__ == "__main__":
    main()