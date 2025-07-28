package com.superdupermarkt.sortiment.infrastructure.persistence.inmemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.superdupermarkt.sortiment.domain.model.kalkulation.Kalkulation;
import com.superdupermarkt.sortiment.domain.repository.KalkulationRepository;

@Repository
public class InMemoryKalkulationRepository implements KalkulationRepository {

    private final Map<String, Kalkulation> store = new HashMap<>();

    @Override
    public Optional<Kalkulation> findByTyp(String typ) {
        return Optional.ofNullable(store.get(typ));
    }

    @Override
    public void save(String typ, Kalkulation kalkulation) {
        store.put(typ, kalkulation);
    }

    public void deleteByTyp(String typ) {
        store.remove(typ);
    }

    @Override
    public List<Kalkulation> findAll() {
        return new ArrayList<>(store.values());
    }

}
