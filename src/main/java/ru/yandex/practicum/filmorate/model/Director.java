package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Director implements Comparable<Director> {

    private long id;

    @EqualsAndHashCode.Exclude
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Override
    public int compareTo(Director o) {
        if (o == null) {
            return -1;
        }

        long id = this.getId();
        long comrId = o.getId();

        return Math.toIntExact(id - comrId);
    }
}
