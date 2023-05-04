package ru.yandex.practicum.filmorate.model;


import lombok.Data;
import ru.yandex.practicum.filmorate.utils.ValidFilmDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @Size(max = 200, message = "Description length is more than 200 characters")
    private String description;
    @ValidFilmDate
    @Column(name = "release_date")
    private LocalDate releaseDate;
    @Positive(message = "Duration of the film is negative")
    private int duration;
}