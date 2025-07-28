package com.superdupermarkt.sortiment.application.usecase;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.superdupermarkt.sortiment.domain.model.produkt.ProduktView;
import com.superdupermarkt.sortiment.domain.service.SortimentQueryService;

@Component
public class EvaluiereSortimentUseCase {

    private final SortimentQueryService queryService;

    public EvaluiereSortimentUseCase(SortimentQueryService queryService) {
        this.queryService = queryService;
    }

    public List<ProduktView> evaluiereProdukte(LocalDate aktuellesDatum) {
        return queryService.evaluiereProdukte(aktuellesDatum);
    }
}
