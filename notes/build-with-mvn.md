# Building with Maven

This project has been configured to be built with Maven.


## Environment

Be sure to download and install:
  
  * [Apache Maven][1] (version 3.3.9 or higher)
  * [Java JDK 1.8][2] (build 73 or higher)
  
Make sure that the maven executable `mvn` is in your environment `PATH`.


## Compiling the Project (and running Unit Tests)

From the top level directory (the one with `pom.xml` in it), type:

  * `mvn clean install`
 
This will compile the project, run all the unit tests, and create jar files
(one for production classes, one for test classes), placing them in the
target directory:


 * `target/abagail-1.0-SNAPSHOT.jar`
 * `target/abagail-1.0-SNAPSHOT-tests.jar`

If any of the unit tests fail, they will cause the build to fail.

## Running the Code

If you are using an IDE (e.g. Eclipse, or IntelliJ) you should be able to
invoke your code from there.

Alternatively, you could run it from the command line:

 * `java -cp target/abagail-1.0-SNAPSHOT.jar exp/MyExperiment arg1 arg2`
 
As an example, (after building), try the following:

 * `java -cp target/abagail-1.0-SNAPSHOT.jar exp/SampleExperiment`


## Generating Javadocs

From the top level directory (the one with `pom.xml` in it), type:

  * `mvn javadoc:javadoc`
  
This will generate the Javadocs for the project. The index page for the
documentation will be:

  * `target/site/apidocs/index.html`
  
Typically (on MacOS, for example) you can load this straight into your
browser with: 

  * `open target/site/apidocs/index.html`


Have fun playing with the project.
 
[1]: https://maven.apache.org/download.cgi
[2]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html