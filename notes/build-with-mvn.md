# Building with Maven

This project has been configured to be built with Maven.


## Environment

Be sure to download and install:
  
  * [Apache Maven][1] (version 3.3.9 or higher)
  * [Java JDK 1.8][2] (build 73 or higher)
  

## Compiling the Project (and running Unit Tests)

From the top level directory (the one with `pom.xml` in it), type:

  * `mvn clean install`
 
This will compile the project, run all the unit tests, and create jar files
(one for production classes, one for test classes), placing them in the
target directory:

 * `target/abagail-1.0-SNAPSHOT.jar`
 * `target/abagail-1.0-SNAPSHOT-tests.jar`

If the build is successful, it means all the unit tests passed.


## Running the Code

If you are using an IDE (e.g. Eclipse, IntelliJ) you should be able to
invoke your code from there.

Alternatively, you could run it from the command line, for which a handy
script has been provided:

 * `./runexp.sh MyExperiment arg1 arg2`
 
The script assumes that your main class has been defined in the 
`exp` (experiments) package.
 
As an example, (after building), try the following:

 * `./runexp.sh SampleExperiment "foo bar" baz 12 3`


## Generating Javadocs

From the top level directory (the one with `pom.xml` in it), type:

  * `mvn javadoc:javadoc`
  
This will generate the Javadocs for the project. The index page for the
documentation will be:

  * `target/site/apidocs/index.html`
  
Typically (on MacOS, for example) you can load this straight into your
browser with: 

  * `open target/site/apidocs/index.html`


## Create Some Experiments

We suggest you create your main classes in the `exp` package. You might want
to create sub packages for your other classes. For example:

```
A
B
C
```

Have fun building ABAGAIL Experiments!
 
[1]: https://maven.apache.org/download.cgi
[2]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html