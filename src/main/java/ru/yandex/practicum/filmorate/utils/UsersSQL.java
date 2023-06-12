package ru.yandex.practicum.filmorate.utils;

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
    public static final String updateUser = "UPDATE users SET\n" +
            "email = ?, login = ?, name = ?, birthday = ?\n" +
            "WHERE user_id = ?";
    public static final String deleteUser = "DELETE FROM users WHERE user_id = ?";

    public static final String insertIntoFriends = "INSERT INTO friends(user_id, friend_id)\n" +
            "VALUES (?, ?)";
    public static final String deleteFriends = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

}