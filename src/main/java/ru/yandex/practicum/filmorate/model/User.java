package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class User {
    private int id;
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
    }
}