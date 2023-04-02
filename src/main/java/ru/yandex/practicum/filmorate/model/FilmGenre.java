package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Builder
@Data
public class FilmGenre {

    private int id;

    private String name;

    @JsonCreator
    public FilmGenre(@JsonProperty("id") int id) {
        this.id = id;
    }
}
