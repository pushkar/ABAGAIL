:: edit the classpath to the location of your ABAGAIL jar file
set CLASSPATH=ABAGAIL.jar
mkdir -p data\plot logs image

:: four peaks
echo "four peaks"
jython fourpeaks.py

:: count ones
echo "count ones"
jython countones.py

:: continuous peaks
echo "continuous peaks"
jython continuouspeaks.py

:: knapsack
echo "Running knapsack"
jython knapsack.py

:: abalone test
echo "Running abalone test"
jython abalone_test.py

:: TSP
echo "Running TSP test"
jython travelingsalesman.py

:: graphs
echo "Creating Sample Graphs"
python plot_data.py
python fourpeaks_plot.py
