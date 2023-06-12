package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.utils.AfterDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private long id;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @Size(max = 200, message = "Description length is more than 200 characters")
    private String description;
    @AfterDate(beforeDate = "1895-12-28", message = "Invalid film realise date")
    @Column(name = "release")
    private LocalDate releaseDate;
    @Positive(message = "Duration of the film is negative")
    private int duration;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "likes",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mpa_id")
    private Mpa mpa;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.DETACH}
    )
    @JoinTable(name = "film_genre",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
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
        genres.add(genre);
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("NAME", name);
        values.put("DESCRIPTION", description);
        values.put("RELEASE", releaseDate);
        values.put("DURATION", duration);
        values.put("MPA_ID", mpa.getId());
        return values;
    }
}