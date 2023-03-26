package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;

@AllArgsConstructor
@Getter
@Setter
public class FilmGenre {
    @Id
    @Column(name = "GENRE_ID")
    private  int id;
    @Column(name = "NAME")
    private  String name;
}
