package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Director {

    private long id;

    @EqualsAndHashCode.Exclude
    @NotBlank(message = "Name cannot be empty")
    private String name;
}