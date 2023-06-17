package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "genres")
public class Genre implements Comparable<Genre> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private long id;

    @Column(name = "genre_name")
    @EqualsAndHashCode.Exclude
    private String name;

    @ManyToMany(mappedBy = "genres")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Film> films = new HashSet<>();

    @Override
    public int compareTo(Genre o) {
        if (o == null) {
            return -1;
        }

        long id = this.getId();
        long comrId = o.getId();

        return Math.toIntExact(id - comrId);
    }
}