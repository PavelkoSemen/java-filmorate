package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Email
    private String email;
    @NotBlank(message = "Login cannot be empty")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
}