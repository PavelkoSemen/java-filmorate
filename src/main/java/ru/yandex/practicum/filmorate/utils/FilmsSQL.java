package ru.yandex.practicum.filmorate.utils;

public final class FilmsSQL {
    private FilmsSQL() {
    }

    public static final String GET_ALL_FILMS = "SELECT f.*\n" +
            "     , m.*\n" +
            "     , g.*\n" +
            "FROM films f\n" +
            "         LEFT JOIN mpa m\n" +
            "                   ON f.mpa_id = m.mpa_id\n" +
            "         LEFT JOIN film_genre fg\n" +
            "                   ON f.film_id = fg.film_id\n" +
            "         LEFT JOIN genres g\n" +
            "                   ON g.genre_id = fg.genre_id\n";

    public static final String GET_FILM_BY_ID = "SELECT f.*\n" +
            "     , m.*\n" +
            "     , g.*\n" +
            "FROM films f\n" +
            "         LEFT JOIN mpa m\n" +
            "                   ON f.mpa_id = m.mpa_id\n" +
            "         LEFT JOIN film_genre fg\n" +
            "                   ON f.film_id = fg.film_id\n" +
            "         LEFT JOIN genres g\n" +
            "                   ON g.genre_id = fg.genre_id\n" +
            " WHERE f.film_id = ?";

    public static final String GET_TOP_FILMS = "SELECT f.*\n" +
            "     , m.*\n" +
            "     , g.*\n" +
            "FROM films f\n" +
            "         LEFT JOIN (SELECT film_id\n" +
            "                         , COUNT(user_id) as count_likes\n" +
            "                    FROM likes\n" +
            "                    GROUP BY film_id) cf\n" +
            "                   ON cf.film_id = f.film_id\n" +
            "         LEFT JOIN mpa m\n" +
            "                   ON f.mpa_id = m.mpa_id\n" +
            "         LEFT JOIN film_genre fg\n" +
            "                   ON f.film_id = fg.film_id\n" +
            "         LEFT JOIN genres g\n" +
            "                   ON g.genre_id = fg.genre_id\n" +
            "ORDER BY count_likes desc\n" +
            "LIMIT ?";

    public static final String INSERT_INTO_FILM_GENRE = "INSERT INTO film_genre(film_id, genre_id)\n" +
            "VALUES (?, ?)";
    public static final String DELETE_FILM_GENRE = "DELETE FROM film_genre WHERE film_id = ?";
    public static final String UPDATE_FILM = "UPDATE films SET\n" +
            "name = ?, description = ?, release = ?, duration = ?, mpa_id = ?\n" +
            "WHERE film_id = ?";
    public static final String DELETE_FILM = "DELETE FROM films WHERE film_id = ?";
    public static final String INSERT_INTO_LIKES = "INSERT INTO likes(film_id, user_id)\n" +
            "VALUES (?, ?)";
    public static final String DELETE_LIKES = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
}