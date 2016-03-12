#!/bin/bash
# edit the classpath to to the location of your ABAGAIL jar file
#
export CLASSPATH=./lib/ABAGAIL.jar:$CLASSPATH
mkdir -p data/plot logs image
LOGFILE=credit-g.log.json
TRIALNAME=$1
echo running $TRIALNAME

jython harproblem.py $LOGFILE
python plot.py $LOGFILE 

mkdir logs/$TRIALNAME
mv credit-g* logs/$TRIALNAME
## four peaks
#echo "four peaks"
#jython fourpeaks.py
#
## count ones
#echo "count ones"
#jython countones.py
#
## continuous peaks
#echo "continuous peaks"
#jython continuouspeaks.py
#
## knapsack
#echo "Running knapsack"
#jython knapsack.py

