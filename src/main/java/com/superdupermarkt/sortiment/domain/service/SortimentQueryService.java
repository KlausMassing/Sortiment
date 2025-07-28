package com.superdupermarkt.sortiment.domain.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.superdupermarkt.sortiment.domain.model.produkt.ProduktView;
import com.superdupermarkt.sortiment.domain.repository.ProduktRepository;

@Service
public class SortimentQueryService {
    private final ProduktRepository repository;

    public SortimentQueryService(ProduktRepository repository) {
        this.repository = repository;

    }

    public List<ProduktView> evaluiereProdukte(LocalDate heute) {
        return repository.findAll().stream()
                .map(produkt -> produkt.toProduktView(heute))
                .toList();
    }

}
