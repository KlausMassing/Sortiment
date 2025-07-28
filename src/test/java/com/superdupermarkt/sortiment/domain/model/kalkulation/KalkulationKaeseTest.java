package com.superdupermarkt.sortiment.domain.model.kalkulation;

import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KalkulationKaeseTest {

    private KalkulationKaese kalkulationKaese;
    private Produkt produkt;

    @BeforeEach
    void setUp() {
        kalkulationKaese = new KalkulationKaese();
        produkt = mock(Produkt.class);
    }

    @Test
    void testBerechneQualitaet_BeforeExpiration() {
        LocalDate today = LocalDate.now();
        LocalDate expiration = today.plusDays(40);

        when(produkt.getVerfallsdatum()).thenReturn(expiration);
        when(produkt.getBezeichnung()).thenReturn("Gouda");

        Integer qualitaet = kalkulationKaese.berechneQualitaet(produkt, today);

        assertEquals(40, qualitaet);
    }

    @Test
    void testBerechneQualitaet_OnExpirationDate() {
        LocalDate today = LocalDate.now();

        when(produkt.getVerfallsdatum()).thenReturn(today);
        when(produkt.getBezeichnung()).thenReturn("Edamer");

        Integer qualitaet = kalkulationKaese.berechneQualitaet(produkt, today);

        assertEquals(0, qualitaet);
    }

    @Test
    void testBerechneQualitaet_AfterExpiration() {
        LocalDate today = LocalDate.now();
        LocalDate expiration = today.minusDays(1);

        when(produkt.getVerfallsdatum()).thenReturn(expiration);
        when(produkt.getBezeichnung()).thenReturn("Camembert");

        Integer qualitaet = kalkulationKaese.berechneQualitaet(produkt, today);

        assertEquals(0, qualitaet);
    }

    @Test
    void testIsVerfallen_QualityBelowMinimum() {
        LocalDate today = LocalDate.now();
        LocalDate expiration = today.plusDays(10);

        when(produkt.getVerfallsdatum()).thenReturn(expiration);

        assertTrue(kalkulationKaese.isVerfallen(produkt, today));
    }

    @Test
    void testIsVerfallen_QualityEqualsMinimum() {
        LocalDate today = LocalDate.now();
        LocalDate expiration = today.plusDays(30);

        when(produkt.getVerfallsdatum()).thenReturn(expiration);

        assertFalse(kalkulationKaese.isVerfallen(produkt, today));
    }

    @Test
    void testIsVerfallen_QualityAboveMinimum() {
        LocalDate today = LocalDate.now();
        LocalDate expiration = today.plusDays(50);

        when(produkt.getVerfallsdatum()).thenReturn(expiration);

        assertFalse(kalkulationKaese.isVerfallen(produkt, today));
    }

    @Test
    void testBerechneQualitaet_ExpirationInPast() {
        LocalDate today = LocalDate.now();
        LocalDate expiration = today.minusDays(5);

        when(produkt.getVerfallsdatum()).thenReturn(expiration);
        when(produkt.getBezeichnung()).thenReturn("Emmentaler");

        Integer qualitaet = kalkulationKaese.berechneQualitaet(produkt, today);

        assertEquals(0, qualitaet);
    }

    @Test
    void testBerechneQualitaet_ExpirationTomorrow() {
        LocalDate today = LocalDate.now();
        LocalDate expiration = today.plusDays(1);

        when(produkt.getVerfallsdatum()).thenReturn(expiration);
        when(produkt.getBezeichnung()).thenReturn("Parmesan");

        Integer qualitaet = kalkulationKaese.berechneQualitaet(produkt, today);

        assertEquals(1, qualitaet);
    }

    @Test
    void testIsVerfallen_ExactlyBelowMinimum() {
        LocalDate today = LocalDate.now();
        LocalDate expiration = today.plusDays(29);

        when(produkt.getVerfallsdatum()).thenReturn(expiration);

        assertTrue(kalkulationKaese.isVerfallen(produkt, today));
    }

    @Test
    void testIsVerfallen_ExactlyAtMinimum() {
        LocalDate today = LocalDate.now();
        LocalDate expiration = today.plusDays(30);

        when(produkt.getVerfallsdatum()).thenReturn(expiration);

        assertFalse(kalkulationKaese.isVerfallen(produkt, today));
    }

    @Test
    void testIsVerfallen_WellAboveMinimum() {
        LocalDate today = LocalDate.now();
        LocalDate expiration = today.plusDays(100);

        when(produkt.getVerfallsdatum()).thenReturn(expiration);

        assertFalse(kalkulationKaese.isVerfallen(produkt, today));
    }
}