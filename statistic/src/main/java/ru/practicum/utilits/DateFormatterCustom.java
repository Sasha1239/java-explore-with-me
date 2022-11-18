package ru.practicum.utilits;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateFormatterCustom {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String dateToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(dateTimeFormatter);
    }

    public LocalDateTime stringToDate(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        String[] lines = dateTime.split(" ");
        return LocalDateTime.of(LocalDate.parse(lines[0]), LocalTime.parse(lines[1]));
    }
}
