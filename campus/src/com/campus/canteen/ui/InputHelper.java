package com.campus.canteen.ui;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Shared console input helper to safely read numbers and text without crashing
 * on invalid user input (e.g. letters instead of numbers) or leftover newlines.
 */
public class InputHelper {

    private static Scanner scanner = new Scanner(System.in);

    private InputHelper() {
        // Utility class - no instances needed
    }

    /**
     * Reads a whole line from the console.
     * Throws IllegalStateException if the input stream is closed (EOF / Ctrl+Z).
     */
    private static String readLine(String prompt) {
        System.out.print(prompt);

        try {
            if (!scanner.hasNextLine()) {
                throw new IllegalStateException("Input stream closed. Exiting application.");
            }
            return scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            throw new IllegalStateException("Input stream closed. Exiting application.", e);
        }
    }

    /**
     * Keeps prompting until the user enters a valid integer.
     */
    public static int readInt(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid whole number.");
            }
        }
    }

    /**
     * Keeps prompting until the user enters a valid long integer.
     */
    public static long readLong(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    /**
     * Keeps prompting until the user enters a valid finite decimal number.
     */
    public static double readDouble(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                double value = Double.parseDouble(input);
                if (!Double.isFinite(value)) {
                    System.out.println("Invalid input! Please enter a finite number.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid decimal number.");
            }
        }
    }

    /**
     * Keeps prompting until the user enters non-empty text.
     */
    public static String readNonEmptyString(String prompt) {
        while (true) {
            String input = readLine(prompt);
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    /**
     * Closes the shared Scanner when the application exits.
     */
    public static void close() {
        scanner.close();
    }
}
