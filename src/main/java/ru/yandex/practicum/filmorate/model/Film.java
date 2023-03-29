package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@Table(name = "FILMS")
@Builder
public class Film {
    private int id;
    @Getter
    private final Set<FilmGenre> filmGenres;
    @Getter
    @Setter
    @NotEmpty
    private MPA mpa;
    @Getter
    private final Set<Integer> likesByUserId;
    @NotEmpty
    private final String name;
    @Size(max = 200)
    private final String description;

    private final LocalDate releaseDate;
    @Positive
    private final Long duration;

/*    @JsonCreator
    public Film() {
        this.name = null;
        this.description = null;
        this.releaseDate = null;
        this.duration = null;
        this.likesByUserId = new HashSet<>();
        this.filmGenres = new HashSet<>();
        this.mpa = null;
    }*/
}