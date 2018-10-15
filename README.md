ABAGAIL
=======

[![Build Status](https://travis-ci.org/pushkar/ABAGAIL.svg?branch=master)](https://travis-ci.org/pushkar/ABAGAIL)

The library contains a number of interconnected Java packages that implement machine learning and artificial intelligence algorithms. These are artificial intelligence algorithms implemented for the kind of people that like to implement algorithms themselves.

Usage
------

* See [FAQ](https://github.com/pushkar/ABAGAIL/blob/master/faq.md)
* See [Wiki](https://github.com/pushkar/ABAGAIL/wiki)

### Changes
1. Converted the project to `gradle` from `ant`
2. added logback to support logging messages to file and console
3. created test classes with more wrapper iterations of underlying classes.

### Compile:
```bash
./gradlew clean build
```
Note: This will run all the tests and generate the results under output.
Depending on the machine, it may take long time, since in some tests, 100 or even 1000 iterations are run. Run times may be around 40 minute or so.

This creates a jar file : build/libs/abagail-1.0-SNAPSHOT.jar

### Build without test
```bash
./gradlew clean build -x test
```

### Output:
All the generated output is under "output".

### Tests Construction
There are 4 classes to execute various aspects of this homework.
They are under src/test/java/example
1. `EyeStateTestTest`
   perform neural network by replacing backpropagation with each of the four optimizers.

2. `TwoColorComparisonTester`

3. `TSPComparisonTest`

4. `FourPeaksComparisonTest`

### Execution:

These classes can be executed by opening in an IDE And running each class individually.
Or, we can execute them via gradle as,

```bash
./gradlew  -Dtest.single=*EyeStateTestTest test
./gradlew  -Dtest.single=*FourPeaksComparisonTest test
./gradlew  -Dtest.single=*TSPComparisonTest test
./gradlew  -Dtest.single=*TwoColorComparisonTester test
```
Notice the asterisk before the name of the class to wildcard the package name, since it is unconventional.

Issues
-------

See [Issues page](https://github.com/pushkar/ABAGAIL/issues?state=open).

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
