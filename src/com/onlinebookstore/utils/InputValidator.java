package com.onlinebookstore.utils;

public class InputValidator {
    public static boolean validateString(String input) {
        return input != null && !input.trim().isEmpty();
    }
}