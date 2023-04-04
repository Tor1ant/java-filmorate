package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class User {
    private int id;
    @Getter
    private final Map<Integer, Boolean> friends;
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
}