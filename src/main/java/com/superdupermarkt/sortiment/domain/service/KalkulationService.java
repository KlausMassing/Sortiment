package com.superdupermarkt.sortiment.domain.service;

import java.util.List;
import java.util.Optional;

import com.superdupermarkt.sortiment.domain.model.kalkulation.Kalkulation;
import com.superdupermarkt.sortiment.domain.model.kalkulation.KalkulationKaese;
import com.superdupermarkt.sortiment.domain.model.kalkulation.KalkulationWein;
import com.superdupermarkt.sortiment.domain.model.kalkulation.KalulationVariabel;
import com.superdupermarkt.sortiment.domain.repository.KalkulationRepository;

public class KalkulationService {

    private final KalkulationRepository kalkulationRepository;

    public KalkulationService(KalkulationRepository kalkulationRepository) {
        this.kalkulationRepository = kalkulationRepository;
    }

    /**
     * Initiert alle Kalkulationen.
     */
    public void initKalkulation() {

        kalkulationRepository.save("Käse", new KalkulationKaese());
        kalkulationRepository.save("Wein", new KalkulationWein());
    }

    public void erstelleKalkulation(Kalkulation kalkulation) {
        if (kalkulation == null || kalkulation.getTyp() == null) {
            throw new IllegalArgumentException("Kalkulation und Typ dürfen nicht null sein.");
        }
        kalkulationRepository.save(kalkulation.getTyp(), kalkulation);
    }

    /**
     * Erstellt eine variable Kalkulation mit benutzerdefinierten
     * Funktionsparametern.
     *
     * @param typ               Der Typ der Kalkulation.
     * @param preisFunktion     String-Repräsentation einer Preisfunktion (z.B. als
     *                          mathematischer Ausdruck oder Funktionsname).
     * @param qualitaetFunktion String-Repräsentation einer Qualitätsfunktion (z.B.
     *                          als mathematischer Ausdruck oder Funktionsname).
     * @param verfallenFunktion String-Repräsentation einer Verfallsfunktion (z.B.
     *                          als mathematischer Ausdruck oder Funktionsname).
     */
    public void erstelleVariableKalkulation(String typ, String preisFunktion, String qualitaetFunktion,
            String verfallenFunktion) {

        kalkulationRepository.save(typ,
                new KalulationVariabel(typ, preisFunktion, qualitaetFunktion, verfallenFunktion));
    }

    public Optional<Kalkulation> findeKalkulation(String typ) {
        return kalkulationRepository.findByTyp(typ);
    }

    public List<Kalkulation> findeAlleKalkulationen() {
        return kalkulationRepository.findAll();
    }
}
