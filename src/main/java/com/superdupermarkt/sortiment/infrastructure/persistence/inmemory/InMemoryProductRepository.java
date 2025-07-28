package com.superdupermarkt.sortiment.infrastructure.persistence.inmemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.superdupermarkt.sortiment.SortimentApplication;
import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;
import com.superdupermarkt.sortiment.domain.repository.ProduktRepository;

@Repository
@Profile(InMemoryProductRepository.NOT_PROFIL_SQL)
public class InMemoryProductRepository implements ProduktRepository {

    public static final String NOT_PROFIL_SQL = "!" + SortimentApplication.PROFIL_SQL;

    private final Map<UUID, Produkt> store = new HashMap<>();

    @Override
    public List<Produkt> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Produkt> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void save(Produkt product) {
        store.put(product.getId(), product);
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }

    @Override
    public void saveAll(List<Produkt> produkte) {
        produkte.forEach(this::save);
    }

}
