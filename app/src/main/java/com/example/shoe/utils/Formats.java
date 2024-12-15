package com.example.shoe.utils;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Formats {

    public static String formatCurrency(long amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(amount);
    }
    public static String formatIsoDate(String isoDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return LocalDateTime.parse(isoDate, inputFormatter)
                .format(outputFormatter);
    }

    public static String formatDateFull(String dateString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");

        return LocalDateTime.parse(dateString, inputFormatter)
                .format(outputFormatter);
    }
}
