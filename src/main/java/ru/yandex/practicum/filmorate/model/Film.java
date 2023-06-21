package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.utils.AfterDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private long id;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @Size(max = 200, message = "Description length is more than 200 characters")
    private String description;
    @AfterDate(beforeDate = "1895-12-28", message = "Invalid film realise date")
    private LocalDate releaseDate;
    @Positive(message = "Duration of the film is negative")
    private int duration;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<User> users = new HashSet<>();
    private Mpa mpa;
    private Set<Genre> genres = new TreeSet<>();

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addUser(User user) {
        users.add(user);
        user.getFilms().add(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.getFilms().remove(this);
    }

    public void addGenre(Genre genre) {
        if (genre.getId() == 0)
            return;
        genres.add(genre);
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }
}