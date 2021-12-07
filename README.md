ABAGAIL
=======

[![Build Status](https://travis-ci.org/pushkar/ABAGAIL.svg?branch=master)](https://travis-ci.org/pushkar/ABAGAIL)

The library contains a number of interconnected Java packages that implement machine learning and artificial intelligence algorithms. These are artificial intelligence algorithms implemented for the kind of people that like to implement algorithms themselves.

Usage
------
*For discrete optimization problems see java examples [/src/opt/test](https://github.com/pushkar/ABAGAIL/tree/master/src/opt/test) or jython versions [/jython](https://github.com/pushkar/ABAGAIL/tree/master/jython)   
*For jython | csv | python and grid search examples see [/jython](https://github.com/pushkar/ABAGAIL/blob/master/jython)   
*Also see [Wiki](https://github.com/pushkar/ABAGAIL/wiki), [FAQ](https://github.com/pushkar/ABAGAIL/blob/master/faq.md) 

Here is a simple example of how to import data and build a neural network using the iris data set (taken from [IrisTest.java](https://github.com/pushkar/ABAGAIL/blob/master/src/opt/test/IrisTest.java)).  Train and test error will be exported in csv format to the current working directory.   
```
//import data
DataSetReader dsr = new CSVDataSetReader((new File("src/opt/test/iris.txt")).getAbsolutePath());
DataSet ds = dsr.read();

//split last attribute for label
LabelSplitFilter lsf = new LabelSplitFilter();
lsf.filter(ds);

//encode label as one-hot array and get outputLayerSize
DiscreteToBinaryFilter dbf = new DiscreteToBinaryFilter();
dbf.filter(ds.getLabelDataSet());
outputLayerSize=dbf.getNewAttributeCount();

//test-train split
int percentTrain=75;
RandomOrderFilter randomOrderFilter = new RandomOrderFilter();
randomOrderFilter.filter(ds);
TestTrainSplitFilter testTrainSplit = new TestTrainSplitFilter(percentTrain);
testTrainSplit.filter(ds);
train=testTrainSplit.getTrainingSet();
test=testTrainSplit.getTestingSet();

//standardize data
StandardMeanAndVariance smv = new StandardMeanAndVariance();
smv.fit(train);
smv.transform(train);
smv.transform(test);

//create backprop network using builder
BackPropagationNetwork network = new BackpropNetworkBuilder()
  .withLayers(new int[] {25,10,outputLayerSize})
  .withDataSet(train, test)
  .withIterations(5000)
  .train();
  
//create opt network using builder
FeedForwardNetwork optNetwork = new OptNetworkBuilder()
  .withLayers(new int[] {25,10,outputLayerSize})
  .withDataSet(train, test)
  .withSA(100000, .975)
  .withIterations(1000)
  .train();

```


Contributing
------------

1. Fork it.
2. Create a branch (`git checkout -b my_branch`)
3. Commit your changes (`git commit -am "Awesome feature"`)
4. Push to the branch (`git push origin my_branch`)
5. Open a [Pull Request][1]
6. Enjoy a refreshing Diet Coke and wait 

Features
========

### Hidden Markov Models

* Baum-Welch reestimation algorithm, scaled forward-backward algorithm, Viterbi algorithm
* Support for Input-Output Hidden Markov Models
* Write your own output or transition probability distribution or use the provided distributions, including neural network based conditional probability distributions
* Neural Networks

### Feed-forward backpropagation neural networks of arbitrary topology
* Configurable error functions with sum of squares, weighted sum of squares
* Multiple activation functions with logistic sigmoid, linear, tanh, and soft max
* Choose your weight update rule with standard update rule, standard update rule with momentum, Quickprop, RPROP
* Online and batch training
* Support Vector Machines

### Fast training with the sequential minimal optimization algorithm
* Support for linear, polynomial, tanh, radial basis function kernels
* Decision Trees

### Information gain or GINI index split criteria
* Binary or all attribute value splitting
* Chi-square signifigance test pruning with configurable confidence levels
* Boosted decision stumps with AdaBoost
* K Nearest Neighbors

### Fast kd-tree implementation for instance based algorithms of all kinds
* KNN Classifier with weighted or non-weighted classification, customizable distance function
* Linear Algebra Algorithms

### Basic matrix and vector math, a variety of matrix decompositions based on the standard algorithms
* Solve square systems, upper triangular systems, lower triangular systems, least squares
* Singular Value Decomposition, QR Decomposition, LU Decomposition, Schur Decomposition, Symmetric Eigenvalue Decomposition, Cholesky Factorization
* Make your own matrix decomposition with the easy to use Householder Reflection and Givens Rotation classes
* Optimization Algorithms

### Randomized hill climbing, simulated annealing, genetic algorithms, and discrete dependency tree MIMIC
* Make your own crossover functions, mutation functions, neighbor functions, probability distributions, or use the provided ones.
* Optimize the weights of neural networks and solve travelling salesman problems
* Graph Algorithms

### Kruskals MST and DFS
* Clustering Algorithms

### EM with gaussian mixtures, K-means
* Data Preprocessing

### PCA, ICA, LDA, Randomized Projections
* Convert from continuous to discrete, discrete to binary
* Reinforcement Learning

### Value and policy iteration for Markov decision processes

[1]: https://help.github.com/articles/using-pull-requests
