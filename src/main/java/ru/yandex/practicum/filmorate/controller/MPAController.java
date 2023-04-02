package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;

@RestController
@RequestMapping(value = "/mpa")
@RequiredArgsConstructor
public class MPAController {

    private final MPAService mpaService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<MPA> getRatings() {
        return mpaService.getAllMPA();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MPA getRatingById(@PathVariable int id) {
        return mpaService.getMPA(id);
    }
}
