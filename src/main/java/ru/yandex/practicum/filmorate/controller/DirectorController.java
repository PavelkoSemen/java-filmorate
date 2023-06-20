package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.directorservice.DirectorService;

import javax.validation.Valid;

@RestController
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @PostMapping(path = "/{filmId}")
    public Director addFilmDirector(@PathVariable long filmId, @RequestBody @Valid Director director) {
        return directorService.addFilmDirector(filmId, director);
    }
}
