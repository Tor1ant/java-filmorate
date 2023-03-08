package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@SpringBootTest
public class UserControllerTest {
    private RestTemplate restTemplate;
    private ConfigurableApplicationContext ctx;
    private HttpHeaders headers;
    private final String url = "http://localhost:8080/users";

    @BeforeEach
    void runApp() {
        ctx = SpringApplication.run(FilmorateApplication.class);
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @AfterEach
    void stopServer() {
        ctx.close();
    }

    @DisplayName("Проверка создания пользователя")
    @Test
    public void shouldTrueWhenCreateUser() throws JsonProcessingException {
        User user = new User("312Gora@mail.ru", "LoginTest", LocalDate.of(1998,
                1, 26));
        user.setName("Frog265");
        HttpEntity<User> httpEntity = new HttpEntity<>(user, headers);
        String result = restTemplate.postForObject(url, httpEntity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        User userForTest = objectMapper.readValue(result, User.class);
        user.setId(1);
        Assertions.assertEquals(user, userForTest);
    }

    @DisplayName("Создание пользователя без полей")
    @Test
    public void should400BAD_REQUESTWhenPostEmptyUser() {
        HttpEntity<User> request = new HttpEntity<>(null, headers);
        HttpClientErrorException httpClientErrorException = Assertions.assertThrows(HttpClientErrorException.class,
                () -> restTemplate.postForObject(
                        url, request, String.class));
        Assertions.assertEquals("400 BAD_REQUEST", httpClientErrorException.getStatusCode().toString());
    }

    @DisplayName("Создание пользователя с неправильной почтой")
    @Test
    public void should400BAD_REQUESTWhenPostUserWithWrongEmail() {
        User user = new User("312GoraMail.ru", "LoginTest", LocalDate.of(1998,
                1, 26));
        user.setName("Frog265");
        HttpEntity<User> httpEntity = new HttpEntity<>(user, headers);
        HttpClientErrorException httpClientErrorException = Assertions.assertThrows(HttpClientErrorException.class,
                () -> restTemplate.postForObject(
                        url, httpEntity, String.class));
        Assertions.assertEquals("400 BAD_REQUEST", httpClientErrorException.getStatusCode().toString());
    }

    @DisplayName("Создание пользователя с пробелом в логине")
    @Test
    public void should400BAD_REQUESTWhenPostUserWithWrongLogin() {
        User user = new User("312Gora@mail.ru", "Login Test", LocalDate.of(1998,
                1, 26));

        user.setName("Frog265");
        HttpEntity<User> httpEntity = new HttpEntity<>(user, headers);
        HttpClientErrorException httpClientErrorException = Assertions.assertThrows(HttpClientErrorException.class,
                () -> restTemplate.postForObject(
                        url, httpEntity, String.class));
        Assertions.assertEquals("400 BAD_REQUEST", httpClientErrorException.getStatusCode().toString());
    }

    @DisplayName("Создание пользователя с пустым логином")
    @Test
    public void should400BAD_REQUESTWhenPostUserWithEmptyLogin() {
        User user = new User("312Gora@mail.ru", null, LocalDate.of(1998,
                1, 26));
        user.setName("Frog265");
        HttpEntity<User> httpEntity = new HttpEntity<>(user, headers);
        HttpClientErrorException httpClientErrorException = Assertions.assertThrows(HttpClientErrorException.class,
                () -> restTemplate.postForObject(
                        url, httpEntity, String.class));
        Assertions.assertEquals("400 BAD_REQUEST", httpClientErrorException.getStatusCode().toString());
    }

    @DisplayName("Создание пользователя с пустым именем")
    @Test
    public void shouldTrueWhenPostUserWithEmptyName() {
        User user = new User("312Gora@mail.ru", "Takanashi265", LocalDate.of(1998,
                1, 26));
        HttpEntity<User> httpEntity = new HttpEntity<>(user, headers);
        restTemplate.postForObject(url, httpEntity, String.class);
        ParameterizedTypeReference<List<User>> listOfUsersType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET, null, listOfUsersType);
        User userFromServer = Objects.requireNonNull(responseEntity.getBody()).get(0);
        Assertions.assertEquals("Takanashi265", userFromServer.getName());
    }

    @DisplayName("Создание пользователя с датой рождения в будущем")
    @Test
    public void should400BAD_REQUESTWhenPostUserWithWrongBirthDate() {
        User user = new User("312Gora@mail.ru", "Takanashi265", LocalDate.of(30092,
                1, 26));
        user.setName("Frog265");
        HttpEntity<User> httpEntity = new HttpEntity<>(user, headers);
        HttpClientErrorException httpClientErrorException = Assertions.assertThrows(HttpClientErrorException.class,
                () -> restTemplate.postForObject(
                        url, httpEntity, String.class));
        Assertions.assertEquals("400 BAD_REQUEST", httpClientErrorException.getStatusCode().toString());
    }

    @DisplayName("Обновление существующего пользователя")
    @Test
    public void shouldTrueNewNameWhenPutUser() {
        User user = new User("312Gora@mail.ru", "LoginTest", LocalDate.of(1998,
                1, 26));
        user.setName("Frog265");
        User user2 = new User("Test@mail.ru", "ОбновленныйПользователь", LocalDate.of(1998,
                1, 26));
        user2.setId(1);
        HttpEntity<User> httpEntity = new HttpEntity<>(user, headers);
        HttpEntity<User> httpEntity2 = new HttpEntity<>(user2);
        restTemplate.postForObject(url, httpEntity, String.class);
        restTemplate.exchange(url, HttpMethod.PUT, httpEntity2, User.class);
        ParameterizedTypeReference<List<User>> listOfUsersType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET, null, listOfUsersType);
        User userFromServer = Objects.requireNonNull(responseEntity.getBody()).get(0);
        Assertions.assertEquals("ОбновленныйПользователь", userFromServer.getName());
    }

    @DisplayName("Обновление пользователя с несуществующим id")
    @Test
    public void should404NOT_FOUNDWhenPutUserWithWrongId() {
        User user = new User("312Gora@mail.ru", "LoginTest", LocalDate.of(1998,
                1, 26));
        user.setName("Frog265");
        User user2 = new User("Test@mail.ru", "ОбновленныйПользователь", LocalDate.of(1998,
                1, 26));
        user2.setId(2);
        HttpEntity<User> httpEntity = new HttpEntity<>(user, headers);
        HttpEntity<User> httpEntity2 = new HttpEntity<>(user2);
        restTemplate.postForObject(url, httpEntity, String.class);
        HttpClientErrorException httpClientErrorException = Assertions.assertThrows(HttpClientErrorException.class,
                () -> restTemplate.exchange(url, HttpMethod.PUT, httpEntity2, User.class));
        Assertions.assertEquals("404 NOT_FOUND", httpClientErrorException.getStatusCode().toString());
    }
}