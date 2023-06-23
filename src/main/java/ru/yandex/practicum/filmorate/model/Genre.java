package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {

    private long id;

    @EqualsAndHashCode.Exclude
    private String name;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Film> films = new HashSet<>();
}