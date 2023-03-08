package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class Film {
    private int id;
    private final Set<Integer> likesByUserId;
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
        this.likesByUserId = new HashSet<>();
    }
}