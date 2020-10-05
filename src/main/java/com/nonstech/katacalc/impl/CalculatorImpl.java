package com.nonstech.katacalc.impl;

import com.nonstech.katacalc.Calculator;
import com.nonstech.katacalc.exceptions.BadInputException;
import com.nonstech.katacalc.exceptions.NegativeNumberException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorImpl implements Calculator {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\[(.+?)\\]");
    private static final String FIND_DELIMITER_IN_INPUT_PATTERN = "//.*?\n";

    private static final String DEFAULT_DELIMITER = ",";

    public int add(String input) {
        if (input.isEmpty()) {
            return 0;
        }
        if (checkIfInputHasBadEnd(input)) {
            throw new BadInputException();
        }
        String delimiters = getDelimitersFromInput(input);
        input = input.replaceFirst(FIND_DELIMITER_IN_INPUT_PATTERN, "");
        String[] numbers = input.split(delimiters + "|\n");
        int result = 0;
        List<String> negatives = new ArrayList<>();
        for (String number : numbers) {
            if (checkIfElementIsNotNumeric(number)) {
                throw new BadInputException();
            }
            int numberFromText = Integer.parseInt(number);
            if (numberFromText < 0) {
                negatives.add(number);
            } else if (numberFromText < 1000) {
                result += numberFromText;
            }
        }
        if (negatives.isEmpty()) {
            return result;
        }
        String message = "Negatives not allowed! Negatives: " + String.join(" ", negatives);
        throw new NegativeNumberException(message);
    }

    private String getDelimitersFromInput(String input) {
        String delimiters = DEFAULT_DELIMITER;
        String firstLine = input.split("\n")[0];
        if (isDelimiter(firstLine)) {
            final List<String> delimitersFromPlaceholders = getDelimitersFromPlaceholders(firstLine);
            if (delimitersFromPlaceholders.isEmpty()) {
                delimiters = firstLine.substring(2);
            } else {
                delimiters = String.join("|", delimitersFromPlaceholders);
            }
        }
        return delimiters;
    }

    private List<String> getDelimitersFromPlaceholders(String textWithPotentialPlaceholders) {
        final Matcher matcher = PLACEHOLDER_PATTERN.matcher(textWithPotentialPlaceholders);
        final List<String> delimitersList = new LinkedList<>();
        while (matcher.find()) {
            delimitersList.add(matcher.group(1));
        }
        return delimitersList;
    }

    private boolean isDelimiter(String element) {
        if (element == null) {
            return false;
        }
        return checkIfElementIsNotNumeric(element) && element.startsWith("//");
    }

    private boolean checkIfInputHasBadEnd(String input) {
        return checkIfElementIsNotNumeric(input.substring(input.length() - 1));
    }

    private boolean checkIfElementIsNotNumeric(String element) {
        return !NUMBER_PATTERN.matcher(element).matches();
    }

}
