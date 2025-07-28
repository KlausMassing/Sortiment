package com.superdupermarkt.sortiment.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.superdupermarkt.sortiment.domain.model.kalkulation.Kalkulation;
import com.superdupermarkt.sortiment.domain.model.kalkulation.KalkulationKaese;
import com.superdupermarkt.sortiment.domain.model.kalkulation.KalkulationWein;
import com.superdupermarkt.sortiment.domain.model.kalkulation.KalulationVariabel;
import com.superdupermarkt.sortiment.domain.repository.KalkulationRepository;

public class KalkulationServiceTest {

    private KalkulationRepository kalkulationRepository;
    private KalkulationService kalkulationService;

    // Simple in-memory mock for KalkulationRepository
    private static class MockKalkulationRepository implements KalkulationRepository {
        private final Map<String, Kalkulation> store = new HashMap<>();

        @Override
        public void save(String typ, Kalkulation kalkulation) {
            store.put(typ, kalkulation);
        }

        @Override
        public Optional<Kalkulation> findByTyp(String typ) {
            return Optional.ofNullable(store.get(typ));
        }

        @Override
        public List<Kalkulation> findAll() {
            return new ArrayList<>(store.values());
        }
    }

    @BeforeEach
    public void setUp() {
        kalkulationRepository = new MockKalkulationRepository();
        kalkulationService = new KalkulationService(kalkulationRepository);
    }

    @Test
    public void testInitKalkulation_savesKaeseAndWein() {
        kalkulationService.initKalkulation();

        Optional<Kalkulation> kaese = kalkulationRepository.findByTyp("Käse");
        Optional<Kalkulation> wein = kalkulationRepository.findByTyp("Wein");

        assertTrue(kaese.isPresent());
        assertTrue(wein.isPresent());
        assertTrue(kaese.get() instanceof KalkulationKaese);
        assertTrue(wein.get() instanceof KalkulationWein);
    }

    @Test
    public void testErstelleKalkulation_savesKalkulation() {
        Kalkulation kalkulation = new KalkulationKaese();
        kalkulationService.erstelleKalkulation(kalkulation);

        Optional<Kalkulation> result = kalkulationRepository.findByTyp(kalkulation.getTyp());
        assertTrue(result.isPresent());
        assertEquals(kalkulation, result.get());
    }

    @Test
    public void testErstelleKalkulation_nullKalkulation_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            kalkulationService.erstelleKalkulation(null);
        });
    }

    @Test
    public void testErstelleKalkulation_nullTyp_throwsException() {

        assertThrows(IllegalArgumentException.class, () -> {
            Kalkulation kalkulation = new Kalkulation("") {
                @Override
                public String getTyp() {
                    return null;
                }
            };
            kalkulationService.erstelleKalkulation(kalkulation);
        });

    }

    @Test
    public void testErstelleVariableKalkulation_savesVariableKalkulation() {
        String typ = "TestTyp";
        String preisFunktion = "preisF";
        String qualitaetFunktion = "qualF";
        String verfallenFunktion = "verfallF";

        kalkulationService.erstelleVariableKalkulation(typ, preisFunktion, qualitaetFunktion, verfallenFunktion);

        Optional<Kalkulation> result = kalkulationRepository.findByTyp(typ);
        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof KalulationVariabel);

        KalulationVariabel variable = (KalulationVariabel) result.get();
        assertEquals(typ, variable.getTyp());
        assertEquals(preisFunktion, variable.getPreisFunktion());
        assertEquals(qualitaetFunktion, variable.getQualitaetFunktion());
        assertEquals(verfallenFunktion, variable.getVerfallenFunktion());
    }

    @Test
    public void testFindeKalkulation_returnsCorrectKalkulation() {
        Kalkulation kalkulation = new KalkulationKaese();
        kalkulationRepository.save(kalkulation.getTyp(), kalkulation);

        Optional<Kalkulation> found = kalkulationService.findeKalkulation(kalkulation.getTyp());
        assertTrue(found.isPresent());
        assertEquals(kalkulation, found.get());
    }

    @Test
    public void testFindeKalkulation_notFound_returnsEmpty() {
        Optional<Kalkulation> found = kalkulationService.findeKalkulation("NichtVorhanden");
        assertFalse(found.isPresent());
    }

    @Test
    public void testFindeAlleKalkulationen_returnsAll() {
        Kalkulation k1 = new KalkulationKaese();
        Kalkulation k2 = new KalkulationWein();
        kalkulationRepository.save(k1.getTyp(), k1);
        kalkulationRepository.save(k2.getTyp(), k2);

        List<Kalkulation> all = kalkulationService.findeAlleKalkulationen();
        assertEquals(2, all.size());
        assertTrue(all.contains(k1));
        assertTrue(all.contains(k2));
    }
}