package jnapi.utils;

/**
 * Simple logging util
 *
 * @author Maciej Dragan
 */
public class Log {

    public static void info(String message) {
        System.out.println("I> " + message);
    }

    public static void error(String message) {
        System.out.println("E> " + message);
    }

}
