package com.superdupermarkt.sortiment.application.usecase;

import org.springframework.stereotype.Component;

import com.superdupermarkt.sortiment.domain.service.KalkulationService;

@Component
public class ErstelleKalkulationUseCase {
    private final KalkulationService kalkulationService;

    public ErstelleKalkulationUseCase(KalkulationService kalkulationService) {
        this.kalkulationService = kalkulationService;
    }

    public void erstelleVariableKalkulation(String typ, String preisFunktion, String qualitaetFunktion,
            String verfallenFunktion) {
        kalkulationService.erstelleVariableKalkulation(typ, preisFunktion, qualitaetFunktion, verfallenFunktion);
    }
}
