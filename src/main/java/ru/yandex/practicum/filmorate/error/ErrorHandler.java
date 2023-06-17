package ru.yandex.practicum.filmorate.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler(SaveUserException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage saveUserException(SaveUserException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(SaveFilmException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage saveFilmException(SaveFilmException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(UnknownUserException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage unknownUserException(UnknownUserException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(UnknownFilmException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage unknownFilmException(UnknownFilmException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(UnknownGenreException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage unknownGenreException(UnknownGenreException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(UnknownMpaException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage unknownMpaException(UnknownMpaException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage methodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }
}