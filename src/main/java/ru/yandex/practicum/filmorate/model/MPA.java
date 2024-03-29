package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Data
public class MPA {

    private final int id;

    private String name;

    @JsonCreator
    public MPA(@JsonProperty("id") int id) {
        this.id = id;
    }
}
