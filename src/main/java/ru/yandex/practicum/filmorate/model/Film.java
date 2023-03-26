package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@Table(name = "FILMS")
public class Film {
    private int id;
    @Getter
    @ManyToOne
    private Set<FilmGenre> filmGenres;
    @Getter
    private MPARating mpaRating;
    @Getter
    private Set<Integer> likesByUserId;
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
        this.filmGenres = new HashSet<>();
        this.mpaRating = null;
    }
}