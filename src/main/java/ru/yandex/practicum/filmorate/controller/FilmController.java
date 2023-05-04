package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.filmservice.FilmService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping(path = "/films/{filmId}")
    public Film getFilm(@PathVariable("filmId") Long id) {
        return filmService.getFilm(id);
    }

    @PostMapping(path = "/films")
    public Film addFilm(@RequestBody @Valid Film film, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(this.getErrorsMessage(errors));
        }
        return filmService.createFilm(film);
    }

    @PutMapping(path = "/films")
    public Film updateFilm(@RequestBody @Valid Film film, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(this.getErrorsMessage(errors));
        }
        return filmService.updateFilm(film);
    }

    @GetMapping(path = "/films")
    public List<Film> getFilms() {
        return filmService.getAllFilms();
    }

    private String getErrorsMessage(Errors errors) {
        return errors.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
    }
}