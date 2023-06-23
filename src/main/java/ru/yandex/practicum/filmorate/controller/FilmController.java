package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.error.ObjectExistsException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.filmservice.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
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
        try{
            filmService.putLike(id, userId);
        }catch (ObjectExistsException e){
            return;
        }
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deletedLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping(path = "/films/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10")@Positive Integer count,
                                  @RequestParam(required = false, name = "genreId") Long genreId,
                                  @RequestParam(required = false, name = "year") Integer year) {
        return filmService.getTopFilms(count, genreId, year);
    }

    @GetMapping(path = "/films/common")
    public List<Film> getMutualTopFilms(@RequestParam int userId, @RequestParam int friendId) {
        return filmService.getMutualTopFilms(userId, friendId);
    }

    @DeleteMapping(path = "/films/{filmId}")
    public Film deleteFilm(@PathVariable long filmId) {
        return filmService.deleteFilm(filmId);
    }

    @GetMapping(path = "/films/director/{directorId}")
    public List<Film> getFilmsByDirector(@PathVariable long directorId,
                                         @RequestParam(defaultValue = "without") String sortBy) {
        return filmService.getFilmsByDirector(directorId, sortBy);
    }

    @GetMapping(path = "/films/search")
    public List<Film> search(@RequestParam String query, @RequestParam String by) {
        return filmService.search(query, by);
    }
}