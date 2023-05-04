package ru.yandex.practicum.filmorate.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ValidFilmDateValidator implements ConstraintValidator<ValidFilmDate, LocalDate> {
    private final LocalDate firstFilmDate = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(ValidFilmDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null) {
            return false;
        }
        return localDate.isAfter(firstFilmDate);
    }
}