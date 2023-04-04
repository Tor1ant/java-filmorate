package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPADbStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class MPAService {

    private final MPADbStorage mpaDbStorage;

    public List<MPA> getAllMPA() {
        List<MPA> allMPA = mpaDbStorage.getAllMPA();
        log.info("получен список MPA");
        return allMPA;
    }

    public MPA getMPA(int id) {
        MPA mpa = mpaDbStorage.getMPAById(id);
        log.info("получен MPA фильма " + mpa.getName());
        return mpa;
    }
}
