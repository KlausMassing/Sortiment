package com.superdupermarkt.sortiment.domain.model.kalkulation;

import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;
import com.superdupermarkt.sortiment.domain.model.value.Preis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KalulationVariabelTest {

    private Produkt produktMock;
    private Preis preisMock;
    private LocalDate aktuellesDatum;
    private LocalDate verfallsDatum;

    // Simple JS functions for testing
    private final String preisFunktion = "(function berechnePreis(basisPreis, qualitaet, tageZumVerfallsdatum) { return basisPreis * qualitaet * 0.1 + tageZumVerfallsdatum; })";
    private final String qualitaetFunktion = "(function berechneQualitaet(tageZumVerfallsdatum) { return Math.max(0, 100 - tageZumVerfallsdatum); })";
    private final String verfallenFunktion = "(function isVerfallen(qualitaet, tageZumVerfallsdatum) { return qualitaet <= 0 || tageZumVerfallsdatum < 0; })";
    private final String typ = "Test";

    @BeforeEach
    void setUp() {
        produktMock = mock(Produkt.class);
        preisMock = mock(Preis.class);

        aktuellesDatum = LocalDate.of(2025, 6, 1);
        verfallsDatum = LocalDate.of(2025, 6, 10);

        when(produktMock.getBasisPreis()).thenReturn(preisMock);
        when(preisMock.preis()).thenReturn(BigDecimal.valueOf(10));
        when(produktMock.getVerfallsdatum()).thenReturn(verfallsDatum);
    }

    @Test
    void testBerechnePreis() {
        KalulationVariabel kalkulation = new KalulationVariabel(typ, preisFunktion, qualitaetFunktion,
                verfallenFunktion);

        Preis preis = kalkulation.berechnePreis(produktMock, aktuellesDatum);

        // qualitaet = 100 - 9 = 91, tageZumVerfallsdatum = 9
        // preis = 10 * 91 * 0.1 + 9 = 91 + 9 = 100
        assertNotNull(preis);
        assertEquals(BigDecimal.valueOf(100), preis.preis());
    }

    @Test
    void testBerechneQualitaet() {
        KalulationVariabel kalkulation = new KalulationVariabel(typ, preisFunktion, qualitaetFunktion,
                verfallenFunktion);

        Integer qualitaet = kalkulation.berechneQualitaet(produktMock, aktuellesDatum);

        // qualitaet = 100 - 9 = 91
        assertEquals(91, qualitaet);
    }

    @Test
    void testIsVerfallenFalse() {
        KalulationVariabel kalkulation = new KalulationVariabel(typ, preisFunktion, qualitaetFunktion,
                verfallenFunktion);

        Boolean verfallen = kalkulation.isVerfallen(produktMock, aktuellesDatum);

        // qualitaet = 91, tageZumVerfallsdatum = 9 -> not expired
        assertFalse(verfallen);
    }

    @Test
    void testIsVerfallenTrueWhenQualitaetZero() {
        KalulationVariabel kalkulation = new KalulationVariabel(typ, preisFunktion, qualitaetFunktion,
                verfallenFunktion);

        // aktuellesDatum == verfallsDatum + 1 => verfallen
        Boolean verfallen = kalkulation.isVerfallen(produktMock, verfallsDatum.plusDays(1));

        assertTrue(verfallen);
    }

    @Test
    void testIsVerfallenTrueWhenTageZumVerfallsdatumNegative() {
        KalulationVariabel kalkulation = new KalulationVariabel(typ, preisFunktion, qualitaetFunktion,
                verfallenFunktion);

        // aktuellesDatum after verfallsDatum
        Boolean verfallen = kalkulation.isVerfallen(produktMock, aktuellesDatum.plusDays(20));

        assertTrue(verfallen);
    }
}