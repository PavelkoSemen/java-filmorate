package ru.yandex.practicum.filmorate.utils;

public final class FilmsSQL {
    private FilmsSQL() {
    }

    public static final String getAllFilms = "SELECT f.*\n" +
            "     , m.*\n" +
            "     , g.*\n" +
            "     , d.*\n" +
            "FROM films f\n" +
            "         LEFT JOIN mpa m\n" +
            "                   ON f.mpa_id = m.mpa_id\n" +
            "         LEFT JOIN film_genre fg\n" +
            "                   ON f.film_id = fg.film_id\n" +
            "         LEFT JOIN genres g\n" +
            "                   ON g.genre_id = fg.genre_id\n" +
            "         LEFT JOIN film_director fd\n" +
            "                   ON f.film_id = fd.film_id\n" +
            "         LEFT JOIN directors d\n" +
            "                   ON d.director_id = fd.director_id";

    public static final String getFilmById = "SELECT f.*\n" +
            "     , m.*\n" +
            "     , g.*\n" +
            "     , d.*\n" +
            "FROM films f\n" +
            "         LEFT JOIN mpa m\n" +
            "                   ON f.mpa_id = m.mpa_id\n" +
            "         LEFT JOIN film_genre fg\n" +
            "                   ON f.film_id = fg.film_id\n" +
            "         LEFT JOIN genres g\n" +
            "                   ON g.genre_id = fg.genre_id\n" +
            "         LEFT JOIN film_director fd\n" +
            "                   ON f.film_id = fd.film_id\n" +
            "         LEFT JOIN directors d\n" +
            "                   ON d.director_id = fd.director_id\n" +
            " WHERE f.film_id = ?";

    public static final String getTopFilms = "SELECT f.*\n" +
            "     , m.*\n" +
            "     , g.*\n" +
            "     , d.*\n" +
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
            "         LEFT JOIN film_director fd\n" +
            "                   ON f.film_id = fd.film_id\n" +
            "         LEFT JOIN directors d\n" +
            "                   ON d.director_id = fd.director_id\n" +
            "ORDER BY count_likes desc\n" +
            "LIMIT ?";

    public static final String insertIntoFilm = "INSERT INTO films(name, description, release, duration, mpa_id)\n" +
            "VALUES (?, ?, ?, ?, ?)";
    public static final String insertIntoFilmGenre = "INSERT INTO film_genre(film_id, genre_id)\n" +
            "VALUES (?, ?)";
    public static final String deleteFilmGenre = "DELETE FROM film_genre WHERE film_id = ?";
    public static final String insertIntoFilmDirector = "INSERT INTO film_director(film_id, director_id)\n" +
            "VALUES (?, ?)";
    public static final String deleteFilmDirector = "DELETE FROM film_director WHERE film_id = ?";
    public static final String updateFilm = "UPDATE films SET\n" +
            "name = ?, description = ?, release = ?, duration = ?, mpa_id = ?\n" +
            "WHERE film_id = ?";
    public static final String deleteFilm = "DELETE FROM films WHERE film_id = ?";
    public static final String insertIntoLikes = "INSERT INTO likes(film_id, user_id)\n" +
            "VALUES (?, ?)";
    public static final String deleteLikes = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
}