package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class User {
    private int id;
    @Getter
    private Map<Integer, Boolean> friends;
    @NotBlank(message = "email адрес не может быть пустым")
    @Email(message = "некорректный email адрес")
    private final String email;
    @NotBlank()
    @Pattern(regexp = "\\S*", message = "логин не может содержать пробелы или быть пустым")
    private final String login;
    @Setter
    private String name;
    @Past
    private final LocalDate birthday;


    @JsonCreator
    public User() {
        this.email = null;
        this.login = null;
        this.name = null;
        this.birthday = null;
        this.friends = new HashMap<>();
    }
}