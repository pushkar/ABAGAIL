package util;


public class TestUtil {

    public static void assertEquals(Object item, Object item2){
        boolean isEqual = item.equals(item2);
        handleFailureOrSuccess(isEqual, "The first item does not match the expected value");
    }

    public static void assertNotEquals(Object item, Object item2){
        boolean isEqual = item.equals(item2);
        handleFailureOrSuccess(isEqual, "The first item matches the expected value");
    }

    public static void assertTrue(boolean expression){
        handleFailureOrSuccess(expression, "The provided expression was not true");
    }

    public static void assertFalse(boolean expression){
        handleFailureOrSuccess(!expression, "The provided expression was not false");
    }

    private static void handleFailureOrSuccess(boolean expression, String message){
        if (!expression){
            throw new RuntimeException(message);
        }
    }
}
