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

    @PutMapping("/films/{id}/like/{userId}")
    public void putLike(@PathVariable long id, @PathVariable long userId) {
        filmService.putLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deletedLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping(path = "/films/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping(path = "/films/common")
    public List<Film> getMutualTopFilms(@RequestParam int userId, @RequestParam int friendId) {
        return filmService.getMutualTopFilms(userId, friendId);
    }
  
    @DeleteMapping(path = "/films/{filmId}")
    public Film deleteFilm(@PathVariable long filmId) {
        return filmService.deleteFilm(filmId);
    }
}