package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetUsers() {
        Optional<Map<Integer, User>> optionalUsers = Optional.ofNullable(userStorage.getUsers());

        assertThat(optionalUsers)
                .isPresent()
                .hasValueSatisfying(userMap ->
                        assertThat(userMap.get(1)).
                                hasFieldOrPropertyWithValue("name", "Totoshka"));
    }

    @Test
    public void testCreateUser() {
        User user = User.builder()
                .name("est adipisicing")
                .login("doloreUpdate")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(1976, 9, 20))
                .build();

        Optional<ResponseEntity<User>> responseEntity = Optional.ofNullable(userStorage.createUser(user));

        assertThat(responseEntity)
                .isPresent()
                .hasValueSatisfying(httpStatus ->
                        assertThat(responseEntity.get().getBody()).isEqualTo(user));
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
        userStorage.updateUser(user);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(1));

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
        userStorage.deleteUser(user);
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
                .login("doloreUpdate")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(1976, 9, 20))
                .build();
        userStorage.createUser(user);
        userStorage.addFriend(1, 4);
        Optional<List<User>> userFriends = Optional.ofNullable(userStorage.getUserFriends(1));
        assertThat(userFriends)
                .isPresent()
                .hasValueSatisfying(friendList ->
                        assertThat(friendList.size()).isEqualTo(3)
                );
    }

    @Test
    public void testDeleteFriend() {
        userStorage.deleteFriend(1, 3);
        Optional<List<User>> userFriends = Optional.ofNullable(userStorage.getUserFriends(1));
        assertThat(userFriends)
                .isPresent()
                .hasValueSatisfying(friendList ->
                        assertThat(friendList.size()).isEqualTo(2)
                );
    }

    @Test
    public void testGetCommonFriends() {
        Optional<List<User>> commonFriends = Optional.ofNullable(userStorage.getCommonFriends(1, 2));
        assertThat(commonFriends)
                .isPresent()
                .hasValueSatisfying(friendList ->
                        assertThat(friendList.get(0).getName()).isEqualTo("Suleman")
                );
    }
}
