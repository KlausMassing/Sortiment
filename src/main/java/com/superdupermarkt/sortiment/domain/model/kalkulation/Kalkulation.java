package com.superdupermarkt.sortiment.domain.model.kalkulation;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;
import com.superdupermarkt.sortiment.domain.model.value.Preis;

public class Kalkulation {

    private final String typ;

    public Kalkulation(String typ) {
        this.typ = typ;

    }

    public String getTyp() {
        return typ;
    }

    private final BigDecimal faktor = new BigDecimal("0.1");

    public Preis berechnePreis(Produkt product, LocalDate aktuellesDatum) {

        BigDecimal basisPreis = product.getBasisPreis().preis();
        Integer qualitaet = berechneQualitaet(product, aktuellesDatum);
        if (qualitaet > 0) {
            basisPreis = basisPreis.multiply(BigDecimal.valueOf(qualitaet).multiply(faktor));
        }
        return new Preis(basisPreis);
    }

    public Integer berechneQualitaet(Produkt product, LocalDate aktuellesDatum) {
        return 0;
    }

    public Boolean isVerfallen(Produkt product, LocalDate aktuellesDatum) {
        return berechneQualitaet(product, aktuellesDatum) < 0;
    }

    protected Long berechneTageZumVerfall(Produkt product, LocalDate aktuellesDatum) {
        return product.getVerfallsdatum().toEpochDay() - aktuellesDatum.toEpochDay();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
