package com.superdupermarkt.sortiment.application.gateway.rest;

import java.time.LocalDate;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superdupermarkt.sortiment.SortimentApplication;
import com.superdupermarkt.sortiment.application.usecase.BereinigeSortimentUseCase;
import com.superdupermarkt.sortiment.application.usecase.CreateSortimentUseCase;
import com.superdupermarkt.sortiment.application.usecase.EvaluiereSortimentUseCase;
import com.superdupermarkt.sortiment.domain.model.produkt.ProduktView;

@RestController
@RequestMapping("/api/sortiment")
@Profile(SortimentApplication.PROFIL_REST)
public class SortimentController {

    private final CreateSortimentUseCase createSortimentUseCase;
    private final EvaluiereSortimentUseCase evaluiereSortimentUseCase;
    private final BereinigeSortimentUseCase bereinigeSortimentUseCase;

    public SortimentController(CreateSortimentUseCase createSortimentUseCase,
            EvaluiereSortimentUseCase evaluiereSortimentUseCase, BereinigeSortimentUseCase bereinigeSortimentUseCase) {
        this.createSortimentUseCase = createSortimentUseCase;
        this.evaluiereSortimentUseCase = evaluiereSortimentUseCase;
        this.bereinigeSortimentUseCase = bereinigeSortimentUseCase;
    }

    @GetMapping
    public List<ProduktView> evaluiereSortiment() {
        return evaluiereSortimentUseCase.evaluiereProdukte(LocalDate.now());
    }

    @PutMapping("/import/{filename}")
    public void importProducts(@PathVariable String filename) {
        createSortimentUseCase.erstelleSortimentAusDatei(filename);
    }

    @PutMapping("/bereinigen/{datum}")
    public void bereinigeSortiment(@PathVariable String datum) {
        LocalDate date = LocalDate.parse(datum);
        bereinigeSortimentUseCase.entferneVerfalleneProdukte(date);
    }
}