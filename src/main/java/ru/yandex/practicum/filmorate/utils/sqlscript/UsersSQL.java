package ru.yandex.practicum.filmorate.utils.sqlscript;

public final class UsersSQL {
    private UsersSQL() {
    }

    public static final String getAllUsers = "SELECT * FROM users";
    public static final String getUserById = "SELECT * FROM users WHERE user_id = ?";
    public static final String getMutualFriends = "SELECT u.*\n" +
            "FROM friends fi\n" +
            "         INNER JOIN friends fo\n" +
            "                    ON fi.friend_id = fo.friend_id\n" +
            "                           AND fi.user_id = ?\n" +
            "                           AND fo.user_id = ?\n" +
            "         INNER JOIN users u\n" +
            "                    ON u.user_id = fo.friend_id";
    public static final String getFriends = "SELECT u.*\n" +
            "FROM users u\n" +
            "         INNER JOIN (SELECT * FROM friends where user_id = ?) f\n" +
            "                    ON u.user_id = f.friend_id";

    public static final String insertIntoUser = "INSERT INTO users(email, login, name, birthday)\n" +
            "VALUES (?, ?, ?, ?)";
    public static final String updateUser = "UPDATE users SET\n" +
            "email = ?, login = ?, name = ?, birthday = ?\n" +
            "WHERE user_id = ?";
    public static final String deleteUser = "DELETE FROM users WHERE user_id = ?";

    public static final String insertIntoFriends = "INSERT INTO friends(user_id, friend_id)\n" +
            "VALUES (?, ?)";
    public static final String deleteFriends = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    public static final String queryFilmsRecommendations = "SELECT  f.* , m.* , g.* , d.*\n" +
            "FROM\n" +
            "(" +
            "SELECT *\n" +
            "FROM FILMS films \n" +
            "INNER JOIN (\n" +
            "SELECT likedFilmId\n" +
            "FROM(\n" +
            "  SELECT likes.FILM_ID AS likedFilmId \n" +
            "  FROM LIKES likes\n" +
            "      INNER JOIN (\n" +
            "        SELECT peopleLikes.user_id AS userWithSameLikes\n" +
            "        FROM LIKES AS userLikes, LIKES AS peopleLikes \n" +
            "        WHERE userLikes.film_id = peopleLikes.film_id \n" +
            "        AND userLikes.USER_ID <> peopleLikes.USER_ID AND userLikes.USER_ID = ? \n" +
            "        GROUP BY peopleLikes.user_id  \n" +
            "        ORDER BY count(*) DESC limit 10) \n" +
            "        usersWithSameLikes ON usersWithSameLikes.userWithSameLikes = likes.USER_ID\n" +
            "        GROUP BY likedFilmId\n" +
            "  ) AS sameLikesFromOtherUsers\n" +
            "  LEFT JOIN(\n" +
            "    SELECT FILM_ID AS userId\n" +
            "    FROM LIKES\n" +
            "  WHERE USER_ID = ? \n" +
            ")\n" +
            "onlyUserLikes ON onlyUserLikes.userId = sameLikesFromOtherUsers.likedFilmId\n" +
            "WHERE onlyUserLikes.userId IS NULL  \n" +
            ")\n" +
            "ON likedFilmId = films.FILM_ID ) f \n" +
            FilmsSQL.joinFilmAttribute;
}