package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.eventenum.EventOperation;
import ru.yandex.practicum.filmorate.model.eventenum.EventType;

import java.time.LocalDateTime;

@Data
@Builder
public class Event {
    private long eventId;
    private long entityId;
    private long timestamp;
    private EventOperation operation;
    private EventType eventType;
    private long userId;

}