package com.superdupermarkt.sortiment.application.usecase;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.superdupermarkt.sortiment.domain.service.SortimentCommandService;

@Component
public class BereinigeSortimentUseCase {

    private final SortimentCommandService commandService;

    public BereinigeSortimentUseCase(SortimentCommandService commandService) {
        this.commandService = commandService;
    }

    public void entferneVerfalleneProdukte(LocalDate datum) {
        commandService.entferneVerfalleneProdukte(datum);
    }
}
