package com.superdupermarkt.sortiment.application.usecase;

import org.springframework.stereotype.Component;

import com.superdupermarkt.sortiment.domain.service.SortimentCommandService;

@Component
public class CreateSortimentUseCase {

    private final SortimentCommandService commandService;

    public CreateSortimentUseCase(SortimentCommandService commandService) {
        this.commandService = commandService;
    }

    public void erstelleSortimentAusDatei(String filename) {
        commandService.erstelleSortimentAusDatei(filename);
    }

}
