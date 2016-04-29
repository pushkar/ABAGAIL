# Part 1 - Creating an executable jar file

Dependencies
+ ant
+ class with main function, e.g. AbaloneTest
+ updated build.xml (you can do this)

In order for a command like **java -jar Executable.jar** to work, the build system must compile the main class definition into the jar file.  This isn't as hard as it sounds, and we're going to use AbaloneTest as the example, since most of you may already be used to typing "java -cp ABAGAIL.jar opt.test.AbaloneTest".

The current version of AbaloneTest.java in the repo already has a main function definition.  Search for "public static void main".  It doesn't matter where you put the main function inside your class.  What's important is the format of the function call:

    public static void main(String[] args){
        *whatever code you want main to run
    }

Here's the part that's important, if you know the full source path for your desired main function class, update the build.xml file with that class path:

Here's an example for AbaloneTest
```java
    <jar basedir = "bin/" destfile = "ABAGAIL.jar">
        <manifest>
            <attribute name="Main-Class" value="opt.test.AbaloneTest"/>
        </manifest>
    </jar>
```
Once you've updated the build.xml file, you can rebuild using **ant** at the command line, and your newly created ABAGAIL.jar file will be executable.

_**Note: If you're having issues, it probably wouldn't hurt to start with a fresh git clone of ABAGAIL without any recently compiled class files lurking around.**_

# Part 2 - Passing in arguments
Now that you've updated ABAGAIL.jar to run your main function inside AbaloneTest, let's update AbaloneTest to handle some command line arguments.

_Note: the format of the command must follow the conventions you establish in the main class._

```java
public static void main(String[] args) {
    for (int i = 0; i < args.length; i++) {
        String s = args[i];
        if (s.equalsIgnoreCase("-t")) {
            TARGET_CLASS = args[i + 1];
        } else if (s.equalsIgnoreCase("-iteration_count")) {
            IterationCount = Integer.parseInt(args[i + 1]);
        } else if (s.equalsIgnoreCase("-algo")) {
            algorithm = args[i + 1];
        } else if (s.equalsIgnoreCase("-cooling_alpha")) {
            alpha = Double.parseDouble(args[i + 1]);
        }
    }
    // rest of your main function
}
