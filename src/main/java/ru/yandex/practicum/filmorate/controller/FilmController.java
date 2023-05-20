package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.filmservice.FilmService;

import javax.validation.Valid;
import java.util.List;

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
    public Film addFilm(@RequestBody @Valid Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping(path = "/films")
    public Film updateFilm(@RequestBody @Valid Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping(path = "/films")
    public List<Film> getFilms() {
        return filmService.getAllFilms();
    }
}