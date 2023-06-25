package ru.yandex.practicum.filmorate.service.directorservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorRepository;
import ru.yandex.practicum.filmorate.error.EntityNotFoundException;
import ru.yandex.practicum.filmorate.error.EntitySaveException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Slf4j
@Service
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository directorRepository;

    @Autowired
    public DirectorServiceImpl(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public Director getDirector(long id) {
        log.info("Получение режиссера с с id: {}", id);
        return directorRepository.findDirectorById(id)
                .orElseThrow(() -> new EntityNotFoundException("Режиссер не найден: " + id));
    }

    public Director createDirector(Director director) {
        log.info("Создание режиссера {}", director);
        return directorRepository.createDirector(director)
                .orElseThrow(() -> new EntitySaveException("Режиссер не сохранен: " + director));
    }

    public Director updateDirector(Director director) {
        log.info("Обновление режиссера с id: {}", director.getId());
        return directorRepository.updateDirector(director)
                .orElseThrow(() -> new EntityNotFoundException("Режиссер не найден: " + director.getId()));
    }

    public void deleteDirector(long id) {
        log.info("Удаление режиссера с id: {}", id);
        directorRepository.deleteDirector(id);
    }

    public List<Director> getAllDirectors() {
        log.info("Получение всех режиссеров");
        return directorRepository.findAll();
    }
}