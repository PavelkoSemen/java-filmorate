package ru.yandex.practicum.filmorate.service.filmservice.utils;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AfterDateValidator implements ConstraintValidator<AfterDate, LocalDate> {
    private String beforeDate;

    private String dateFormat;

    @Override
    public void initialize(AfterDate constraintAnnotation) {
        beforeDate = constraintAnnotation.beforeDate();
        dateFormat = constraintAnnotation.dateFormat();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate before = LocalDate.parse(beforeDate, DateTimeFormatter.ofPattern(dateFormat));

        if (localDate == null) {
            return false;
        }
        return localDate.isAfter(before);
    }
}