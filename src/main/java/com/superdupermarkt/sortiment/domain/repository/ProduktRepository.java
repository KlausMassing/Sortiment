package com.superdupermarkt.sortiment.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;

public interface ProduktRepository {
    List<Produkt> findAll();

    Optional<Produkt> findById(UUID id);

    void save(Produkt product);

    void deleteById(UUID id);

    void saveAll(List<Produkt> produkte);
}
