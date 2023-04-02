package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.FilmGenreController;
import ru.yandex.practicum.filmorate.controller.MPAController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmoRateApplicationTests {
    private final FilmController filmController;
    private final UserController userController;
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;
    private final MPAController mpaController;
    private final FilmGenreController filmGenreController;

    @Test
    @Order(1)
    public void testCreateUser() {
        User user = User.builder()
                .name("est adipisicing")
                .login("testCreateUser")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(1976, 9, 20))
                .build();
        Optional<ResponseEntity<User>> responseEntity = Optional.ofNullable(userController.postUser(user));

        assertThat(responseEntity)
                .isPresent()
                .hasValueSatisfying(httpStatus ->
                        assertThat(Objects.requireNonNull(responseEntity.get().getBody()).getName()).isEqualTo(user
                                .getName()));
    }

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = Optional.ofNullable(userController.getUser(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetUsers() {
        Optional<Collection<User>> optionalUsers = Optional.ofNullable(userController.getUsers());
        List<User> users = new ArrayList<>(optionalUsers.get());
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("name", "Totoshka");
    }

    @Test
    public void testUpdateUser() {
        User user = User.builder()
                .name("est adipisicing")
                .login("doloreUpdate")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(1976, 9, 20))
                .build();
        user.setId(1);
        userController.putUser(user);
        Optional<User> userOptional = Optional.ofNullable(userController.getUser(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(updatedUser ->
                        assertThat(updatedUser).hasFieldOrPropertyWithValue("name", "est adipisicing")
                );
    }

    @Test
    public void testDeleteUser() {
        User user = User.builder()
                .name("est adipisicing")
                .login("doloreUpdate")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(1976, 9, 20))
                .build();
        user.setId(1);
        userController.deleteUser(user);
        Optional<Map<Integer, User>> optionalUsers = Optional.ofNullable(userStorage.getUsers());
        assertThat(optionalUsers)
                .isPresent()
                .hasValueSatisfying(updatedUser ->
                        assertThat(updatedUser.containsKey(1)).isFalse()
                );
    }

    @Test
    public void testAddFriendToUserAndGetHisFriends() {
        User user = User.builder()
                .name("est adipisicing")
                .login("CreatingUser")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(1976, 9, 20))
                .build();
        userController.postUser(user);
        userController.addFriend(1, 4);
        Optional<List<User>> userFriends = Optional.ofNullable(userStorage.getUserFriends(1));
        assertThat(userFriends)
                .isPresent()
                .hasValueSatisfying(friendList ->
                        assertThat(friendList.size()).isEqualTo(3)
                );
    }

    @Test
    public void getFriends() {
        List<User> friends = userController.getFriends(1);
        assertThat(friends.size()).isEqualTo(2);
    }

    @Test
    public void testDeleteFriend() {
        userController.deleteFriend(1, 3);
        Optional<List<User>> userFriends = Optional.ofNullable(userStorage.getUserFriends(1));
        assertThat(userFriends)
                .isPresent()
                .hasValueSatisfying(friendList ->
                        assertThat(friendList.size()).isEqualTo(1)
                );
    }

    @Test
    public void testGetCommonFriends() {
        Optional<List<User>> commonFriends = Optional.ofNullable(userController.getCommonFriends(1, 2));
        assertThat(commonFriends)
                .isPresent()
                .hasValueSatisfying(friendList ->
                        assertThat(friendList.get(0).getName()).isEqualTo("Suleman")
                );
    }

    @Test
    public void testCreateFilm() {
        Film film = Film.builder()
                .mpa(new MPA(1))
                .name("TestFilm")
                .description("Test Description")
                .releaseDate(LocalDate.of(2023, 4, 2))
                .duration(150L)
                .build();

        Optional<ResponseEntity<Film>> responseEntity = Optional.ofNullable(filmController.postFilm(film));
        assertThat(responseEntity)
                .isPresent()
                .hasValueSatisfying(httpStatus ->
                        assertThat(Objects.requireNonNull(responseEntity.get().getBody()).getId()).isEqualTo(7));
    }

    @Test
    public void testUpdateFilm() {
        Film film = Film.builder()
                .mpa(new MPA(1))
                .name("TestFilm")
                .description("Test Description")
                .releaseDate(LocalDate.of(2023, 4, 2))
                .duration(150L)
                .id(1)
                .build();
        Optional<ResponseEntity<Film>> responseEntity = Optional.ofNullable(filmController.putFilm(film));
        assertThat(responseEntity)
                .isPresent()
                .hasValueSatisfying(httpStatus ->
                        assertThat(Objects.requireNonNull(responseEntity.get().getBody()).getName()).isEqualTo(film
                                .getName()));
    }

    @Test
    public void testUpdateFilmWithGenres() {
        Film film = Film.builder()
                .mpa(new MPA(1, "G"))
                .name("TestFilm")
                .description("Test Description")
                .releaseDate(LocalDate.of(2023, 4, 2))
                .duration(150L)
                .id(1)
                .genres(new ArrayList<>(List.of(new FilmGenre(1, "Комедия"))))
                .build();
        Optional<ResponseEntity<Film>> responseEntity = Optional.ofNullable(filmController.putFilm(film));
        assertThat(responseEntity)
                .isPresent()
                .hasValueSatisfying(httpStatus ->
                        assertThat(Objects.requireNonNull(responseEntity.get().getBody())).isEqualTo(film));
    }

    @Test
    public void testDeleteFilm() {
        filmController.deleteFilm(1);
        Optional<Map<Integer, Film>> films = Optional.ofNullable(filmDbStorage.getFilms());
        assertThat(films)
                .isPresent()
                .hasValueSatisfying(httpStatus ->
                        assertThat((films.get().size())).isEqualTo(5));
    }

    @Test
    public void testGetFilms() {
        Collection<Film> films = filmController.getFilms();
        assertThat(films.size()).isEqualTo(6);
    }

    @Test
    public void testGetFilm() {
        Film film = filmController.getFilm(1);
        assertThat(film.getName()).isEqualTo("Только представь!");
    }

    @Test
    public void testAddLikeToFilm() {
        int countLines = filmController.addLikeToFilm(5, 2);
        assertThat(countLines)
                .isEqualTo(1);
    }

    @Test
    public void testMostLikedFilms() {
        List<Film> mostLikedFilms = filmController.getMostLikedFilms(2);
        assertThat(mostLikedFilms.size()).isEqualTo(2);
    }

    @Test
    public void getFilmGenres() {
        List<FilmGenre> filmGenres = filmGenreController.getGenres();

        assertThat(filmGenres.size())
                .isEqualTo(6);

        assertThat(filmGenres.get(0))
                .extracting(FilmGenre::getId, FilmGenre::getName)
                .containsExactly(1, "Комедия");

        assertThat(filmGenres.get(1))
                .extracting(FilmGenre::getId, FilmGenre::getName)
                .containsExactly(2, "Драма");

        assertThat(filmGenres.get(2))
                .extracting(FilmGenre::getId, FilmGenre::getName)
                .containsExactly(3, "Мультфильм");

    }

    @Test
    public void testGetGenreById() {
        FilmGenre filmGenre = filmGenreController.getGenreById(1);
        assertThat(filmGenre.getName()).isEqualTo("Комедия");
    }

    @Test
    public void testGetRatings() {
        List<MPA> ratings = mpaController.getRatings();

        assertThat(ratings.size())
                .isEqualTo(5);

        assertThat(ratings.get(0))
                .extracting(MPA::getId, MPA::getName)
                .containsExactly(1, "G");

        assertThat(ratings.get(1))
                .extracting(MPA::getId, MPA::getName)
                .containsExactly(2, "PG");

        assertThat(ratings.get(2))
                .extracting(MPA::getId, MPA::getName)
                .containsExactly(3, "PG-13");
    }

    @Test
    public void testGetRatingById() {
        MPA mpa = mpaController.getRatingById(1);
        assertThat(mpa.getName()).isEqualTo("G");
    }
}
