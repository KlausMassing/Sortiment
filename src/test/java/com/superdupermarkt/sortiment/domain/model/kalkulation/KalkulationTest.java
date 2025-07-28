package com.superdupermarkt.sortiment.domain.model.kalkulation;

import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;
import com.superdupermarkt.sortiment.domain.model.value.Preis;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KalkulationTest {

    @Test
    void berechnePreis_shouldReturnBasisPreis_whenQualitaetIsZero() {
        Produkt produkt = mock(Produkt.class);
        Preis preis = new Preis(new BigDecimal("5.00"));
        when(produkt.getBasisPreis()).thenReturn(preis);

        Kalkulation kalkulation = new Kalkulation("Test");
        Preis result = kalkulation.berechnePreis(produkt, LocalDate.now());

        assertEquals(preis.preis(), result.preis());
    }

    @Test
    void berechnePreis_shouldMultiplyPreis_whenQualitaetIsGreaterThanZero() {
        Produkt produkt = mock(Produkt.class);
        Preis preis = new Preis(new BigDecimal("10.00"));
        when(produkt.getBasisPreis()).thenReturn(preis);

        Kalkulation kalkulation = new Kalkulation("Test") {
            @Override
            public Integer berechneQualitaet(Produkt product, LocalDate aktuellesDatum) {
                return 2;
            }
        };

        Preis result = kalkulation.berechnePreis(produkt, LocalDate.now());
        // basisPreis * (qualitaet * faktor) = 10 * (2 * 0.1) = 10 * 0.2 = 2.0
        assertEquals(new BigDecimal("2.000"), result.preis());
    }

    @Test
    void berechneQualitaet_shouldReturnZeroByDefault() {
        Produkt produkt = mock(Produkt.class);
        Kalkulation kalkulation = new Kalkulation("Test");
        assertEquals(0, kalkulation.berechneQualitaet(produkt, LocalDate.now()));
    }

    @Test
    void isVerfallen_shouldReturnFalseByDefault() {
        Produkt produkt = mock(Produkt.class);
        Kalkulation kalkulation = new Kalkulation("Test");
        assertFalse(kalkulation.isVerfallen(produkt, LocalDate.now()));
    }
}