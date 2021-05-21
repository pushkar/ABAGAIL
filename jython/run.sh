#!/bin/bash
# edit the classpath to to the location of your ABAGAIL jar file
#

export CLASSPATH=ABAGAIL.jar:$CLASSPATH
mkdir -p data/plot logs image

# four peaks
echo "four peaks"
jython fourpeaks.py

# count ones
echo "count ones"
jython countones.py

# continuous peaks
echo "continuous peaks"
jython continuouspeaks.py

# knapsack
echo "Running knapsack"
jython knapsack.py

# iris test
echo "Running iris test"
jython iris_test.py
mv *.csv data

# traveling salesman
echo "Running traveling salesman test"
jython travelingsalesman.py

# graphs
echo "Creating Sample Graphs"
python plot_data.py
python fourpeaks_plot.py