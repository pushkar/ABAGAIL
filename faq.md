# Frequently Asked Questions

### Table of contents

#### Getting Started
* [What is ABAGAIL?](#user-content-what-is-abagail)
* [How to get started with Abagail?](#user-content-how-to-get-started-with-abagail)
* [How is ABAGAIL project organized?](#user-content-how-is-abagail-project-organized)
* [What are the different ways can I work with ABAGAIL?](#user-content-what-are-the-different-ways-to-work-with-abagail)
* [How to use ABAGAIL with Eclipse?](#user-content-how-to-use-abagail-with-eclipse)
* [Is there a tutorial or documentation for ABAGAIL?](#user-content-is-there-a-tutorial-or-documentation-for-abagail)

#### Usage Questions
* [Can you generate graphs out of ABAGAIL library?](#user-content-can-you-generate-graphs-out-of-abagail-library)
* [How does ABAGAIL output results?](#user-content-how-does-abagail-output-results)
* [Does Abagail have any form of built in cross validation?](#user-content-does-abagail-have-any-form-of-built-in-cross-validation)
* [How can I find out the actual number of iterations performed in Abagail?](#how-can-i-find-out-the-actual-number-of-iterations-performed-in-abagail)
* [Could someone help explain the training_iterations in ABAGAIL? When I increase it, I get better accuracy.](#could-someone-help-explain-the-training_iterations-in-abagail-when-i-increase-it-i-get-better-accuracy)
* [My neural network classifier is performing very poorly, how to fix it?](#user-content-my-neural-network-classifier-is-performing-very-poorly-how-to-fix-it)
* [How does the neural network get its weights updated?](#user-content-how-does-the-neural-network-get-its-weights-updated)
* [How to change number of neurons in a hidden layer?](#user-content-how-to-change-number-of-neurons-in-a-hidden-layer)
* [I am trying to understand what the count ones optimization function is doing, but I am not sure...](#i-am-trying-to-understand-what-the-count-ones-optimization-function-is-doing-but-i-am-not-sure)
* [TravelingSalesmanRouteEvaluationFunction - why the 1/total distance?](#user-content-travelingsalesmanrouteevaluationfunction---why-the-1total-distance)
* [How do I get multi-label classification working in AbaloneTest.java?](#user-content-how-do-i-get-multi-label-classification-working-in-abalonetestjava)
* [My dataset has categorical features, can i use ABAGAIL?](#user-content-my-dataset-has-categorical-features-can-i-use-abagail)

#### Other questions
* [What are some characteristics of problems simulated annealing works well on?](#user-content-what-are-some-characteristics-of-problems-simulated-annealing-works-well-on)
* [Getting build error: /home/unazi/ABAGAIL/build.xml:40: Unexpected attribute "additionalparam", how do I fix this?](#user-content-getting-ant-build-error-homeunaziabagailbuildxml40-unexpected-attribute-additionalparam-how-do-i-fix-this)

***
## What is ABAGAIL?

ABAGAIL is an acronym of Absolute Best Andrew Guillory Artificial Intelligence Library.

from `src > abagail.html`: 
> This library is the result of close to a year of research and class work in artificial intelligence (AI). It contains a number of interconnected Java packages that implement machine learning and artificial intelligence algorithms. These are artificial intelligence algorithms implemented for the kind of people that like to implement algorithms themselves.

[Back to top](#user-content-table-of-contents)

***

## How to get started with ABAGAIL?

Start by download the source code from github
You can use git to clone the repository:
```
git clone https://github.com/pushkar/ABAGAIL.git
```
or download the .zip file from [github](https://github.com/pushkar/ABAGAIL)

After downloading the source code, decide how you want to work with the library, the most common options are listed [here](#user-content-what-are-the-different-ways-can-i-work-with-abagail). 

<strong>VERY BASIC INSTRUCTIONS FOR COMMAND LINE</strong>
 
1. Install Java 8 SDK from here [http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. Install Ant [http://ant.apache.org/](http://ant.apache.org/)
3. Clone or download source files from Git
4. Go with command line to where the build.xml file is and run:  ant
   (note: the ant executable should be in your path somehow if you installed ant correctly.. so will java and javac)
5. Now run your scripts

[Back to top](#user-content-table-of-contents)

***

## How is ABAGAIL project organized?
At a high-level, the project structure is organized as follows:

`dist`
* Contains classes to generate distributions

`func`
* Contains superviced learning algorithms
* Contains clustering algorithms

`opt`
* Contains random search algorithms
* Contains optimization test functions

`rl`
* Contains reinforcement learning algorithms

`shared`
* Contains filters
* Contains IO and test running functions

`util`
* Contains utility methods and data structures

[Back to top](#user-content-table-of-contents)

***
## What are the different ways to work with ABAGAIL?
 There are several options:
* use terminal or command line interface to compile and execute Java code
* use Java-compatible IDE with ABAGAIL as a library &nbsp; examples: [Eclipse](#user-content-how-to-use-abagail-with-eclipse)
* use Scala console with ABAGAIL as a library
* use Jython

[Back to top](#user-content-table-of-contents)

***

## How to use ABAGAIL with Eclipse?

1. fork `https://github.com/pushkar/ABAGAIL.git`
 
2. From the terminal clone your forked repo to a directory of your choice.
 
3.  Create an Eclipse project, let's say it's "AbagailProject"
 
4.  With the following steps you will convert the directory where you copied Abagail into an Eclipse Project
 
  a. Go to File->New->Project.
 
  b. On the menu double click on Java and select "Java Project from Existing Ant Build file".  Click Next.

  c.  Browse to the Abagail folder and select the xml file.  Click Finish.
 
5. Now you need to include this new Eclipse project to the build folder of your project.

  a. Right click on "AbagailProject" and select Properties.

  b. On the left pane of the window select "Java Build Path."

  c.  Select the "Projects" tab.

  d.  Click Add...

  e.  Select the abagail project you just created (note that when you create the Eclipse project, the name of the project is "project" by default)
 
6.  To use any of the Abagail classes just use import. For instance, if you want to use the class ArffDataSetReader, you need to add the following import statement: `import shared.reader.ArffDataSetReader;`


[Back to top](#user-content-table-of-contents)

***

## Is there a tutorial or documentation for ABAGAIL?

At this time the documentation is very limited. ABAGAIL comes with some documentation for you to look through when you build the project as well as a few examples. Users are actively encouraged to contribute to project documentation.

[Back to top](#user-content-table-of-contents)

***
## Can you generate graphs out of ABAGAIL library?

Currently no. There are several external libraries and software you can use to visualize results:
* Microsoft Excel
* R
* matplotlib
* JFreeChart 
* matlab
* mathematica
* ...etc.

[Back to top](#user-content-table-of-contents)

***

## How does ABAGAIL output results?
The test functions output results to command line. Saving data to disk requires extending the source code.

Here is a simple extension methods for writing output to file, created by Yeshwant Dattatreya.
```
    public void write_output_to_file(String output_dir, String file_path, String result, Boolean final_result) {
        // This function will have to be modified depending on the format of your file name. 
        // Else the splits won't work.
        try {
            if (final_result) {
                String full_path = output_dir + "/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) +
                        "/" + "final_result.csv";
                String[] params = file_path.split("_");
                String line = "";
                switch (params.length) {
                    case 9:
                        line = params[0] + ",none," + params[6] + "," + params[8].split("\\.")[0];
                        break;
                    case 10:
                        line = params[0] + "," + params[3] + "," + params[7] + "," + params[9].split("\\.")[0];
                        break;
                    case 11:
                        line = params[0] + "," + params[3] + "_" + params[4] + "," + params[8] + ","
                                + params[10].split("\\.")[0];
                        break;
                }
                PrintWriter pwtr = new PrintWriter(new BufferedWriter(new FileWriter(full_path, true)));
                synchronized (pwtr) {
                    pwtr.println(line + result);
                    pwtr.close();
                }
            } else {
                String full_path = output_dir + "/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) +
                        "/" + file_path;
                Path p = Paths.get(full_path);
                Files.createDirectories(p.getParent());
                Files.write(p, result.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

[Back to top](#user-content-table-of-contents)

***

## Does Abagail have any form of built in cross validation?

Currently no. You will need to extend the source code to add cross-validation.

[Back to top](#user-content-table-of-contents)

***

## How can I find out the actual number of iterations performed in ABAGAIL?
When you work with random search optimization problems, the number of iterations in the example problems is fixed. 

For example:
```
FixedIterationTrainer fit = new FixedIterationTrainer(algorithm, 200000);`
```

will train the algorithm 200,000 times. 

Alternatively, you can use `ConvergenceTrainer` which will train until convergence, or up to set number of maximum iterations, whichever occurs earlier:
```
ConvergenceTrainer trainer = new ConvergenceTrainer(algorithm);`
```

[Back to top](#user-content-table-of-contents)

***

## Could someone help explain the training_iterations in ABAGAIL? When I increase it, I get better accuracy.

In short, the more you train, the more accurate your weights become. This leads to better accuracy

[Back to top](#user-content-table-of-contents)

***

## My neural network classifier is performing very poorly, how to fix it?

Make sure your dataset values are normalized.  See [this article](https://en.wikipedia.org/wiki/Feature_scaling) for more details.

[Back to top](#user-content-table-of-contents)

***

## How does the neural network get its weights updated?

> Q:I was looking through the AbaloneTest.java code, and notice that each of the randomized algorithm has a train function that, in the case of SA for e.g., picks a neighbor and its fitness value (or sticks with the current point). However, I wasn't able to figure out how the NN gets updated with the new weights? I don't see anything where the train the SA code passes anything back to the concrete class of HillClimbingProblem.

Under ABAGAIL/src/func/nn/backprop/:  BackPropagationNetworkFactory.java

```
private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();

/*********/

// returns instance of BackPropagationNetwork which has updateWeights method.
networks[i] = factory.createClassificationNetwork(new int[] {inputLayer, hiddenLayer, outputLayer});

/*********/

networks[i].setWeights(optimalInstance.getData());
```

Networks is an instance of BackPropagationNetwork.java, which has a method `updateWeights()`, which in turn extends class `FeedForwardNetwork.java` > `LayeredNetwork.java` > `NeuralNetwork.java`. The last one has `setWeights()` method.


[Back to top](#user-content-table-of-contents)

***
## How to change number of neurons in a hidden layer?

> Q: I'm trying to use ABAGAIL to train a neural network with randomized hill climbing. It looks like the number of nodes in a hidden layer is assigned to be the same as the number of nodes in the input layer. How do I change the number of neurons in a hidden layer?

Look at AbaloneTest.java for an example. It shows you how to set up the layers using an int array. This file is under `opt > test`.

[Back to top](#user-content-table-of-contents)


***

## I am trying to understand what the count ones optimization function is doing, but I am not sure...

> Q: I am trying to understand what the count ones optimization function is doing. From the test class in ABAGAIL, I see that an array is created and filled up with all int=2. I think i understand the concept of counting all of the 1s in the vector, but I do not see how the array of all 2s turns into an array of 1s and 0s?

If you dig through the classes that uses the ranges variable, it will become clear. The 2 specifies how many different values are possible at any point in the vector (0 and 1). 

[Back to top](#user-content-table-of-contents)

***
## TravelingSalesmanRouteEvaluationFunction - why the 1/total distance?

The object of the Traveling Salesman problem is to minimize the distance in a route. However, the optimization algorithms maximize their fitness functions. So it returns the inverse of the distance, which gets larger as the distance gets smaller.
 
Returning the normal distance would result in the algorithms finding the longest routes, which isn't what we're interested in.

[Back to top](#user-content-table-of-contents)

***
## How do I get multi-label classification working in AbaloneTest.java?

This is from the method `initializeInstances`. 

You'll need to change whatever your classes are let's say ("cat" -> 0, "dog" -> 1, "other" ->2). This encoding will then correspond to the output layer. I.e. "cat" -> 0 will correspond to the 0th output layer in the array.
 
```
        Instance[] instances = new Instance[attributes.length];

        for(int i = 0; i < instances.length; i++) {
            instances[i] = new Instance(attributes[i][0]);
            
            // Read the digit 0-9 from the attribute array that was read from the csv
            int c = (int) attributes[i][1][0];

            // Create a double array of length 10, all values are initialized to 0
            double[] classes = new double[nClasses];

            // Set the i'th index to 1.0
            classes[c] = 1.0; 
            instances[i].setLabel(new Instance(classes));
        }
```

[Back to top](#user-content-table-of-contents)

***
## My dataset has categorical features, can I use ABAGAIL?

Convert the categorical features to binary, e.g., if you have Country as a variable, then each value of country will have its own column with 1 for the country that's present, and 0 for values that isn't.

[Back to top](#user-content-table-of-contents)

***
## Getting ant build error: /home/unazi/ABAGAIL/build.xml:40: Unexpected attribute "additionalparam", how do I fix this?

Make sure you are using a version where this issue has been fixed. See: [https://github.com/pushkar/ABAGAIL/pull/31](https://github.com/pushkar/ABAGAIL/pull/31)

[Back to top](#user-content-table-of-contents)


***
##What are some characteristics of problems simulated annealing works well on?
Generally, simulated annealing works well if there are multiple minimum and you need to find the global minimum because SA will (hopefully) break out of the local minimum. 

in the ABAGAIL library look for sample test problems `under opt > example` directory

[Back to top](#user-content-table-of-contents)

***