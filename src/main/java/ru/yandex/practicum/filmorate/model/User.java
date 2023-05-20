package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Email(message = "Does not match the email")
    private String email;
    @NotBlank(message = "Login cannot be empty")
    private String login;
    private String name;
    @PastOrPresent(message = "Birthday is longer than the current date")
    private LocalDate birthday;

    @OneToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> friendsList = new HashSet<>();

    public void addFriend(User friend) {
        friendsList.add(friend);
    }

    public void removeFriend(User friend) {
        friendsList.remove(friend);
    }
}