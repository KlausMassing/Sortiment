package com.superdupermarkt.sortiment.domain.model.kalkulation;

import java.time.LocalDate;

import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;

public class KalkulationKaese extends Kalkulation {

    private final Integer MINIMUM_QUALITAET = 30;

    public KalkulationKaese() {
        super("Käse");
    }

    @Override
    public Integer berechneQualitaet(Produkt product, LocalDate aktuellesDatum) {
        Integer qualitaet = 0;
        if (product.getVerfallsdatum().isAfter(aktuellesDatum)) {

            Long tageZumVerfallsdatum = berechneTageZumVerfall(product, aktuellesDatum);
            qualitaet = tageZumVerfallsdatum.intValue();

        }
        return qualitaet;
    }

    @Override
    public Boolean isVerfallen(Produkt product, LocalDate aktuellesDatum) {

        return berechneQualitaet(product, aktuellesDatum) < MINIMUM_QUALITAET;
    }

}
