package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Table(name = "FILMS")
@Builder
public class Film {
    private int id;
    private Set<FilmGenre> genres;

    private MPA mpa;

    private final Set<Integer> likesByUserId;
    @NotEmpty
    private final String name;
    @Size(max = 200)
    private final String description;
    @Past
    private final LocalDate releaseDate;
    @Positive
    private final Long duration;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("NAME", name);
        values.put("DESCRIPTION", description);
        values.put("RELEASE_DATE", releaseDate);
        values.put("DURATION", duration);
        values.put("RATING_ID", mpa.getId());
        return values;
    }
}