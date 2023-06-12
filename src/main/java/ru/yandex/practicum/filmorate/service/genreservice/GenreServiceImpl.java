package ru.yandex.practicum.filmorate.service.genreservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreRepository;
import ru.yandex.practicum.filmorate.error.UnknownGenreException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre getGenre(long id) {
        log.info("Получение жанра по id: {}", id);
        return genreRepository.get(id).orElseThrow(() -> new UnknownGenreException("Жанр не найден: " + id));
    }

    @Override
    public List<Genre> getAllGenres() {
        log.info("Получение жанров");
        return genreRepository.getAll();
    }
}