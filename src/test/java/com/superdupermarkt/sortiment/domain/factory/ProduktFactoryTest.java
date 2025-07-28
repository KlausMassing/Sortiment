package com.superdupermarkt.sortiment.domain.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.superdupermarkt.sortiment.domain.model.kalkulation.Kalkulation;
import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;
import com.superdupermarkt.sortiment.domain.model.value.Preis;
import com.superdupermarkt.sortiment.domain.service.KalkulationService;

class ProduktFactoryTest {

    private KalkulationService kalkulationService;
    private ProduktFactory produktFactory;

    @BeforeEach
    void setUp() {
        kalkulationService = mock(KalkulationService.class);
        produktFactory = new ProduktFactory(kalkulationService);
    }

    @Test
    void testErstellePreis_valid() {
        Optional<Preis> preis = produktFactory.erstellePreis("12.34");
        assertTrue(preis.isPresent());
        assertEquals(new BigDecimal("12.34"), preis.get().preis());
    }

    @Test
    void testErstellePreis_invalid() {
        Optional<Preis> preis = produktFactory.erstellePreis("abc");
        assertFalse(preis.isPresent());
    }

    @Test
    void testErstelleQualitaet_valid() {
        Optional<Integer> qualitaet = produktFactory.erstelleQualitaet("5");
        assertTrue(qualitaet.isPresent());
        assertEquals(5, qualitaet.get());
    }

    @Test
    void testErstelleQualitaet_invalid() {
        Optional<Integer> qualitaet = produktFactory.erstelleQualitaet("abc");
        assertFalse(qualitaet.isPresent());
    }

    @Test
    void testErstelleDatum_valid() {
        Optional<LocalDate> datum = produktFactory.erstelleDatum("01.01.2024");
        assertTrue(datum.isPresent());
        assertEquals(LocalDate.of(2024, 1, 1), datum.get());
    }

    @Test
    void testErstelleDatum_invalid() {
        Optional<LocalDate> datum = produktFactory.erstelleDatum("invalid");
        assertFalse(datum.isPresent());
    }

    @Test
    void testErstelleProduktAusCsv_valid() {
        Kalkulation kalkulation = mock(Kalkulation.class);
        when(kalkulationService.findeKalkulation("Standard")).thenReturn(Optional.of(kalkulation));
        String csv = "Standard,Testprodukt,10.99,01.01.2025";
        Optional<Produkt> produkt = produktFactory.erstelleProduktAusCsv(csv);
        assertTrue(produkt.isPresent());
        assertEquals("Testprodukt", produkt.get().getBezeichnung());
        assertEquals(new BigDecimal("10.99"), produkt.get().getBasisPreis().preis());
        assertEquals(LocalDate.of(2025, 1, 1), produkt.get().getVerfallsdatum());
    }

    @Test
    void testErstelleProduktAusCsv_invalid() {
        String csv = "zu,kurz";
        Optional<Produkt> produkt = produktFactory.erstelleProduktAusCsv(csv);
        assertFalse(produkt.isPresent());
    }

    @Test
    void testErstelleProduktAusJson_valid() throws Exception {
        Kalkulation kalkulation = mock(Kalkulation.class);
        when(kalkulationService.findeKalkulation("Standard")).thenReturn(Optional.of(kalkulation));
        String json = """
                {
                  "id": "123e4567-e89b-12d3-a456-426614174000",
                  "produkttyp": "Standard",
                  "bezeichnung": "Testprodukt",
                  "basispreis": "9.99",
                  "verfallsdatum": "31.12.2024",
                  "einraeumdatum": "01.01.2024"
                }
                """;
        Optional<Produkt> produkt = produktFactory.erstelleProduktAusJson(json);
        assertTrue(produkt.isPresent());
        assertEquals("Testprodukt", produkt.get().getBezeichnung());
        assertEquals(new BigDecimal("9.99"), produkt.get().getBasisPreis().preis());
        assertEquals(LocalDate.of(2024, 12, 31), produkt.get().getVerfallsdatum());
        assertEquals(LocalDate.of(2024, 1, 1), produkt.get().getEinraeumDatum());
    }

    @Test
    void testErstelleProduktAusJson_invalid() {
        String json = "{ invalid json }";
        Optional<Produkt> produkt = produktFactory.erstelleProduktAusJson(json);
        assertFalse(produkt.isPresent());
    }

    @Test
    void testErstelleProduktListeAusJson_valid() throws Exception {
        Kalkulation kalkulation = mock(Kalkulation.class);
        when(kalkulationService.findeKalkulation("Standard")).thenReturn(Optional.of(kalkulation));
        String jsonArray = """
                [
                  {
                    "id": "123e4567-e89b-12d3-a456-426614174000",
                    "produkttyp": "Standard",
                    "bezeichnung": "Produkt1",
                    "basispreis": "5.00",
                    "verfallsdatum": "01.01.2025",
                    "einraeumdatum": "01.01.2024"
                  },
                  {
                    "id": "123e4567-e89b-12d3-a456-426614174001",
                    "produkttyp": "Standard",
                    "bezeichnung": "Produkt2",
                    "basispreis": "10.00",
                    "verfallsdatum": "02.01.2025",
                    "einraeumdatum": "02.01.2024"
                  }
                ]
                """;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonArray.getBytes());
        List<Produkt> produkte = produktFactory.erstelleProduktListeAusJson(inputStream);
        assertEquals(2, produkte.size());
        assertEquals("Produkt1", produkte.get(0).getBezeichnung());
        assertEquals("Produkt2", produkte.get(1).getBezeichnung());
    }

    @Test
    void testErstelleProduktListeAusJson_invalid() {
        String jsonArray = "[ invalid json ]";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonArray.getBytes());
        List<Produkt> produkte = produktFactory.erstelleProduktListeAusJson(inputStream);
        assertTrue(produkte.isEmpty());
    }
}