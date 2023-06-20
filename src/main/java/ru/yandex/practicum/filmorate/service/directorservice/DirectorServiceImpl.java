package ru.yandex.practicum.filmorate.service.directorservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorRepository;
import ru.yandex.practicum.filmorate.error.UnknownFilmException;
import ru.yandex.practicum.filmorate.model.Director;

@Slf4j
@Service
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository directorRepository;

    @Autowired
    public DirectorServiceImpl(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public Director addFilmDirector(long filmId, Director director) {
        log.info("Добавление режиссера к фильму с id: {}", filmId);
        return directorRepository.addFilmDirector(filmId, director)
                .orElseThrow(() -> new UnknownFilmException("Фильм не найден: " + filmId));
    }
}
