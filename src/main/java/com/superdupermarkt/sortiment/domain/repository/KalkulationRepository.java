package com.superdupermarkt.sortiment.domain.repository;

import java.util.List;
import java.util.Optional;

import com.superdupermarkt.sortiment.domain.model.kalkulation.Kalkulation;

public interface KalkulationRepository {

    Optional<Kalkulation> findByTyp(String typ);

    void save(String string, Kalkulation kalkulation);

    List<Kalkulation> findAll();
}
