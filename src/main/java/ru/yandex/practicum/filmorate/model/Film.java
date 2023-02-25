package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Film {
    private int id;
    @NotEmpty
    private final String name;
    @Size(max = 200)
    private final String description;

    private final LocalDate releaseDate;
    @Positive
    private final Long duration;

    @JsonCreator
    public Film() {
        this.name = null;
        this.description = null;
        this.releaseDate = null;
        this.duration = null;
    }
}