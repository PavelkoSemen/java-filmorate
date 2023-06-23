package ru.yandex.practicum.filmorate.utils.sqlscript;

public final class EventSQL {
    private EventSQL() {
    }

    public static final String insertIntoEvent = "INSERT INTO events(event_time, event_type" +
            ", operation, entity_id, user_id) \n" +
            "VALUES (?, ?, ?, ?, ?);";

    public static final String getEventById = "SELECT * FROM events e WHERE e.event_id = ?";
    public static final String getEventsByUserId = "SELECT e.*\n" +
            "FROM events e\n" +
            "WHERE e.user_id in (SELECT friend_id FROM friends f WHERE f.user_id = ?)\n" +
            "   OR e.user_id = ?";
}