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

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
 public  class FilmControllerTest {
    private RestTemplate restTemplate;

    private HttpHeaders headers;
    private ObjectMapper objectMapper;
    private final String url = "http://localhost:8080/films";

    private ConfigurableApplicationContext ctx;

    @BeforeEach
    void runApp() {
        ctx = SpringApplication.run(FilmorateApplication.class);
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void stopServer() {
        ctx.close();
    }

    @DisplayName("Создание фильма со всеми полями.")
    @Test
    public void shouldTrueWhenPostFilmTest() throws JsonProcessingException {
        Film film = new Film("От заката до рассвета", "Спасаясь от полиции после ограбления банка, " +
                "два брата-преступника берут в заложники священника с двумя детьми и бегут в Мексику.",
                LocalDate.of(1996, 1, 19), 108L);
        HttpEntity<Film> request = new HttpEntity<>(film, headers);
        String result = restTemplate.postForObject(url, request, String.class);
        Film film2 = objectMapper.readValue(result, Film.class);
        film.setId(1);
        Assertions.assertEquals(film, film2);
    }

    @DisplayName("Создание пустого фильма.")
    @Test
    public void shouldTrueWhenPostNull() {
        HttpEntity<Film> request = new HttpEntity<>(null, headers);
        Assertions.assertThrows(HttpClientErrorException.class, () -> restTemplate.postForObject(
                url, request, String.class));
    }

    @DisplayName("создание фильма без названия.")
    @Test
    public void shouldTrueWhenPostFilmWithoutTitle() {
        Film film = new Film("", "Спасаясь от полиции после ограбления банка, " +
                "два брата-преступника берут в заложники священника с двумя детьми и бегут в Мексику.",
                LocalDate.of(1996, 1, 19), 108L);
        HttpEntity<Film> request = new HttpEntity<>(film, headers);
        HttpClientErrorException httpClientErrorException = Assertions.assertThrows(HttpClientErrorException.class,
                () -> restTemplate.postForObject(url, request, String.class));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, httpClientErrorException.getStatusCode());
    }

    @DisplayName("создание фильма с описанием в 199 символов и в 201 символов")
    @Test

    public void shouldTrueWhenPostFimWitWrongDescriptionLength() throws JsonProcessingException {
        String chars199 = "a".repeat(199);
        String chars201 = "b".repeat(201);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Film film = new Film("От заката до рассвета", chars199,
                LocalDate.of(1996, 1, 19), 108L);
        HttpEntity<Film> request = new HttpEntity<>(film, headers);
        String result = restTemplate.postForObject(url, request, String.class);
        Film filmForComparison = objectMapper.readValue(result, Film.class);
        film.setId(1);
        Assertions.assertEquals(film, filmForComparison);

        Film film2 = new Film("От заката до рассвета", chars201,
                LocalDate.of(1996, 1, 19), 108L);
        HttpEntity<Film> request2 = new HttpEntity<>(film2, headers);
        HttpClientErrorException httpClientErrorException = Assertions.assertThrows(HttpClientErrorException.class,
                () -> restTemplate.postForObject(
                        url, request2, String.class));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.toString(), httpClientErrorException.getStatusCode().toString());
    }

    @DisplayName("Создание фильма с датой релиза до 28.12.1895")
    @Test
    public void shouldHttpClientErrorExceptionWhenPostFilmWithCreateDateBefore28_12_1895() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Film film = new Film("От заката до рассвета", "Спасаясь от полиции после ограбления банка, " +
                "два брата-преступника берут в заложники священника с двумя детьми и бегут в Мексику.",
                LocalDate.of(1895, 12, 27), 108L);
        HttpEntity<Film> request = new HttpEntity<>(film, headers);
        HttpClientErrorException httpClientErrorException = Assertions.assertThrows(HttpClientErrorException.class,
                () -> restTemplate.postForObject(url, request, String.class));
        Assertions.assertEquals("{\"validationException\":\"Дата создания фильма не может быть раньше " +
                "1895,12,28\"}", httpClientErrorException.
                getResponseBodyAsString());
    }

    @DisplayName("Создание фильма с продолжительностью меньше нуля и равной нулю")
    @Test
    public void shouldHttpClientErrorExceptionWhenPostFilmWithDuration0() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Film film = new Film("От заката до рассвета", "Спасаясь от полиции после ограбления банка, " +
                "два брата-преступника берут в заложники священника с двумя детьми и бегут в Мексику.",
                LocalDate.of(1996, 12, 27), 0L);
        HttpEntity<Film> request = new HttpEntity<>(film, headers);
        HttpClientErrorException httpClientErrorException = Assertions.assertThrows(HttpClientErrorException.class,
                () -> restTemplate.postForObject(url, request, String.class));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.toString(), httpClientErrorException.getStatusCode().toString());
        Film film2 = new Film("От заката до рассвета", "Спасаясь от полиции после ограбления банка, " +
                "два брата-преступника берут в заложники священника с двумя детьми и бегут в Мексику.",
                LocalDate.of(1996, 12, 27), -10L);
        HttpEntity<Film> request2 = new HttpEntity<>(film2, headers);
        HttpClientErrorException httpClientErrorException2 = Assertions.assertThrows(HttpClientErrorException.class,
                () -> restTemplate.postForObject(url, request2, String.class));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.toString(), httpClientErrorException2.getStatusCode().toString());
    }

    @DisplayName("Получение списка фильмов")
    @Test
    public void shouldTrueWhenGetAllFilms() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Film film = new Film("От заката до рассвета", "Спасаясь от полиции после ограбления банка, " +
                "два брата-преступника берут в заложники священника с двумя детьми и бегут в Мексику.",
                LocalDate.of(1996, 12, 27), 108L);
        Film film2 = new Film("Эта замечательная жизнь", "Джордж Бейли, владелец кредитной компании в " +
                "выдуманном американском городке Бедфорд Фоллс",
                LocalDate.of(1946, 12, 20), 120L);
        Film film3 = new Film("Европа", "В 1945 году молодой американец немецкого происхождения Леопольд",
                LocalDate.of(1991, 5, 12), 112L);
        HttpEntity<Film> request = new HttpEntity<>(film, headers);
        HttpEntity<Film> request2 = new HttpEntity<>(film2, headers);
        HttpEntity<Film> request3 = new HttpEntity<>(film3, headers);
        String testFilm1 = restTemplate.postForObject(url, request, String.class);
        String testFilm2 = restTemplate.postForObject(url, request2, String.class);
        String testFilm3 = restTemplate.postForObject(url, request3, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<Film> films = new ArrayList<>();
        films.add(objectMapper.readValue(testFilm1, Film.class));
        films.add(objectMapper.readValue(testFilm2, Film.class));
        films.add(objectMapper.readValue(testFilm3, Film.class));
        ParameterizedTypeReference<List<Film>> typeReference = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<Film>> filmsFromServer = restTemplate.exchange(url,
                HttpMethod.GET, null, typeReference);
        Assertions.assertEquals(films, filmsFromServer.getBody());
    }

    @DisplayName("Обновление существующего фильма")
    @Test
    public void shouldTrueWhenPutFilm() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Film film = new Film("От заката до рассвета", "Спасаясь от полиции после ограбления банка, " +
                "два брата-преступника берут в заложники священника с двумя детьми и бегут в Мексику.",
                LocalDate.of(1996, 1, 19), 108L);
        HttpEntity<Film> request = new HttpEntity<>(film, headers);
        restTemplate.postForObject(url, request, String.class);
        Film film2 = new Film("Эта замечательная жизнь", "Джордж Бейли, владелец кредитной компании в " +
                "выдуманном американском городке Бедфорд Фоллс",
                LocalDate.of(1946, 12, 20), 120L);
        film2.setId(1);
        HttpEntity<Film> request2 = new HttpEntity<>(film2, headers);
        ParameterizedTypeReference<Film> typeReference = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Film> filmFromServer = restTemplate.exchange(url, HttpMethod.PUT,
                request2, typeReference);
        Assertions.assertEquals(film2, filmFromServer.getBody());
    }

    @DisplayName("Обновление несуществующего фильма")
    @Test
    public void shouldTrueWhenPutNotExistingFilm() {
        Film film = new Film("Эта замечательная жизнь", "Джордж Бейли, владелец кредитной компании в " +
                "выдуманном американском городке Бедфорд Фоллс",
                LocalDate.of(1946, 12, 20), 120L);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Film> request2 = new HttpEntity<>(film, headers);
        ParameterizedTypeReference<Film> typeReference = new ParameterizedTypeReference<>() {
        };
        HttpClientErrorException httpClientErrorException = Assertions.assertThrows(HttpClientErrorException.class,
                () -> restTemplate.exchange("http://localhost:8080/films", HttpMethod.PUT,
                        request2, typeReference));
        Assertions.assertEquals(HttpStatus.NOT_FOUND.toString(), httpClientErrorException.getStatusCode().toString());
    }
}