package ru.yandex.practicum.filmorate.error;

public class RepositoryException extends RuntimeException {
    public RepositoryException() {
    }

    public RepositoryException(Throwable cause) {
        super(cause);
    }

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}