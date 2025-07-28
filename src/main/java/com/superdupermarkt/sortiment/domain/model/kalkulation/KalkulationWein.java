package com.superdupermarkt.sortiment.domain.model.kalkulation;

import java.time.LocalDate;

import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;
import com.superdupermarkt.sortiment.domain.model.value.Preis;

public class KalkulationWein extends Kalkulation {

    public KalkulationWein() {
        super("Wein");
    }

    @Override
    public Preis berechnePreis(Produkt product, LocalDate aktuellesDatum) {

        return product.getBasisPreis();
    }

    @Override
    public Integer berechneQualitaet(Produkt product, LocalDate aktuellesDatum) {

        Integer qualitaet = 0;

        Long alter = aktuellesDatum.toEpochDay() - product.getEinraeumDatum().toEpochDay();

        int tage = alter.intValue();
        int multiplikator = tage / 10; // integer division to get the number of 10-day periods

        qualitaet += multiplikator;

        if (qualitaet > 50)
            qualitaet = 50;
        return qualitaet;
    }
}
