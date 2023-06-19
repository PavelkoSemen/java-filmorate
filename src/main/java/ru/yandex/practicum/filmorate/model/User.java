package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private long id;
    @Email(message = "Does not match the email")
    private String email;
    @NotBlank(message = "Login cannot be empty")
    private String login;
    private String name;
    @PastOrPresent(message = "Birthday is longer than the current date")
    private LocalDate birthday;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<User> friendsList = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Film> films = new HashSet<>();

    public User(long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriend(User friend) {
        friendsList.add(friend);
    }

    public void removeFriend(User friend) {
        friendsList.remove(friend);
    }
}