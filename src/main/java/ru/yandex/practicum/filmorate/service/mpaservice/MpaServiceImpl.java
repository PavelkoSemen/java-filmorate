package ru.yandex.practicum.filmorate.service.mpaservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaRepository;
import ru.yandex.practicum.filmorate.error.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Service
public class MpaServiceImpl implements MpaService {

    private final MpaRepository mpaRepository;

    @Autowired
    public MpaServiceImpl(MpaRepository mpaRepository) {
        this.mpaRepository = mpaRepository;
    }

    @Override
    public Mpa getMpa(long id) {
        log.info("Получение возрастного рейтинга по id: {}", id);
        return mpaRepository.findMpaById(id).orElseThrow(() -> new EntityNotFoundException("Возрастной рейтинг не найден: " + id));
    }

    @Override
    public List<Mpa> getAllMpa() {
        log.info("Список возрастных рейтингов");
        return mpaRepository.findAll();
    }
}