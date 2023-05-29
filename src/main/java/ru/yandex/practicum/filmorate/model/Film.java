package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.utils.AfterDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @Size(max = 200, message = "Description length is more than 200 characters")
    private String description;
    @AfterDate(beforeDate = "1895-12-28", message = "Invalid film realise date")
    @Column(name = "release_date")
    private LocalDate releaseDate;
    @Positive(message = "Duration of the film is negative")
    private int duration;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinTable(name = "likes",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<User> users = new HashSet<>();


    public Film(long id, String name, String description, LocalDate releaseDate, int duration) {
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
}