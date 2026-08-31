package org.example.utility;

import org.example.exceptions.InputTimeoutException;
import org.example.exceptions.InvalidNumberInputException;

import java.math.BigDecimal;
import java.util.Scanner;

public class UtilityExceptions {
    public static int safeIntInput(Scanner sc, String message)
            throws InvalidNumberInputException {
        System.out.print(message);
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidNumberInputException("Unos mora biti broj!");
        }
    }

    public static BigDecimal safeBigDecimalInput(Scanner sc, String message)
            throws InvalidNumberInputException {
        System.out.print(message);
        try {
            return new BigDecimal(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new InvalidNumberInputException("Unos mora biti decimalni broj!");
        }
    }

    public static String waitForInput(Scanner sc) {
        long start = System.currentTimeMillis();
        while (!sc.hasNextLine()) {
            if (System.currentTimeMillis() - start > 15000) { // 15 sekundi timeout
                throw new InputTimeoutException("Vrijeme za unos je isteklo!");
            }
        }
        return sc.nextLine();
    }
}
