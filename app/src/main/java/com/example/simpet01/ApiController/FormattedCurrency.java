package com.example.simpet01.ApiController;

import java.text.NumberFormat;
import java.util.Locale;

public class FormattedCurrency {

    public static String formatCurrency(int amount) {
        // Create a Locale for Indonesian
        Locale indonesia = new Locale("id", "ID");

        // Create a NumberFormat for the Indonesian currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(indonesia);

        // Format the integer as currency without decimal places
        String formattedCurrency = currencyFormat.format(amount);

        // Remove the trailing ",00" by applying a custom pattern
        if (formattedCurrency.endsWith(",00")) {
            formattedCurrency = formattedCurrency.substring(0, formattedCurrency.length() - 3);
        }

        return formattedCurrency;
    }

}
