package ru.yandex.practicum.filmorate.dao.jdbctemplateimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.EventRepository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.eventenum.EventOperation;
import ru.yandex.practicum.filmorate.model.eventenum.EventType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.utils.sqlscript.EventSQL.*;

@Repository
@Slf4j
public class JdbcEventRepository implements EventRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcEventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Event> save(Event event) {
        log.info("Сохранение события {}", event);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(insertIntoEvent, new String[]{"EVENT_ID"});
            stmt.setLong(1, event.getTimestamp());
            stmt.setString(2, event.getEventType().name());
            stmt.setString(3, event.getOperation().name());
            stmt.setLong(4, event.getEntityId());
            stmt.setLong(5, event.getUserId());
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() == null) {
            log.error("Событие {} не удалось сохранить", event);
            return Optional.empty();
        }
        event.setEventId(keyHolder.getKey().longValue());

        log.info("Событие {} сохранено", event);
        return Optional.of(event);
    }

    @Override
    public Optional<Event> get(long id) {
        log.info("Возвращаем событие с id: {}", id);
        return jdbcTemplate.query(getEventById, this::mapRow, id).stream().findAny();
    }

    @Override
    public List<Event> getEventFeed(long userId) {
        log.info("Получение ленты событий");
        return jdbcTemplate.query(getEventsByUserId, this::mapRow, userId, userId);
    }

    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        Event event = Event.builder().build();
        event.setEventId(rs.getLong("EVENT_ID"));
        event.setTimestamp(rs.getLong("EVENT_TIME"));
        event.setEventType(EventType.valueOf(rs.getString("EVENT_TYPE")));
        event.setOperation(EventOperation.valueOf(rs.getString("OPERATION")));
        event.setEntityId(rs.getLong("ENTITY_ID"));
        event.setUserId(rs.getLong("USER_ID"));
        return event;
    }
}