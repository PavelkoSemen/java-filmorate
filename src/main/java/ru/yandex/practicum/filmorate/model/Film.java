package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.utils.ValidFilmDate;

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