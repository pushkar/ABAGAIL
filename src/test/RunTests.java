package test;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Author: Derek Miller dmiller303@gatech.edu
 */
public class RunTests {

    public static void main(String args[]) throws Exception{
        System.out.println(handleTests());
    }

    private static String handleTests() throws Exception{
        String exceptionHolder = "";

        List<Class> classes = getClasses("test.unit");
        int passCount = 0;
        int failCount = 0;

        for (Class clazz : classes){
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods){
                if (method.getName().startsWith("test")){
                    try {
                        method.invoke(clazz.newInstance());
                        passCount++;
                    } catch (Exception e){
                        failCount++;
                        exceptionHolder += "\n Class: " + clazz.getName() + " method: " + method.getName() + " message: " + e.getCause().getMessage() + "\n";
                    }

                }
            }
        }
        String finalMessage = "Total Tests Ran: " + (passCount + failCount) + " Tests Passed: " + passCount + " Tests Failed: " + failCount + "\n \n";
        return finalMessage + exceptionHolder;
    }

    private static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException, Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) throw new ClassNotFoundException("Class isn't found");
        //replaces the packagePath with slashes
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException, Exception {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                //adds class, substring removes .class
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
