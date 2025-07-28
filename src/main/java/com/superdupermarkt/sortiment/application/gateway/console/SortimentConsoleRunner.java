package com.superdupermarkt.sortiment.application.gateway.console;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.superdupermarkt.sortiment.application.usecase.BereinigeSortimentUseCase;
import com.superdupermarkt.sortiment.application.usecase.CreateSortimentUseCase;
import com.superdupermarkt.sortiment.application.usecase.ErstelleKalkulationUseCase;
import com.superdupermarkt.sortiment.application.usecase.EvaluiereSortimentUseCase;
import com.superdupermarkt.sortiment.domain.model.produkt.ProduktView;

@Component
@Profile(SortimentConsoleRunner.NOT_PROFIL_REST)
public class SortimentConsoleRunner
        implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(SortimentConsoleRunner.class);
    public static final String NOT_PROFIL_REST = "!rest";

    private final CreateSortimentUseCase createSortimentUseCase;
    private final EvaluiereSortimentUseCase evaluiereSortimentUseCase;
    private final BereinigeSortimentUseCase bereinigeSortimentUseCase;
    private final ErstelleKalkulationUseCase erstelleKalkulationUseCase;

    public SortimentConsoleRunner(CreateSortimentUseCase createSortimentUseCase,
            EvaluiereSortimentUseCase evaluiereSortimentUseCase, BereinigeSortimentUseCase bereinigeSortimentUseCase,
            ErstelleKalkulationUseCase erstelleKalkulationUseCase) {
        this.createSortimentUseCase = createSortimentUseCase;
        this.evaluiereSortimentUseCase = evaluiereSortimentUseCase;
        this.bereinigeSortimentUseCase = bereinigeSortimentUseCase;
        this.erstelleKalkulationUseCase = erstelleKalkulationUseCase;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 1)
        // Sortiment aus Wein und Käse erstellen
        //

        LOG.info("Sortiment wird geladen");

        createSortimentUseCase.erstelleSortimentAusDatei("static/sortiment/sortiment.csv");

        LOG.info("Sortiment erfolgreich erstellt");

        LocalDate heute = LocalDate.now();
        int plusTage = 1;
        int zaehler = 1;
        List<ProduktView> produkte = null;

        // 2)
        // Sortimentszustand für die nächsten 600 Tagen ausgeben, bis die Qualität von
        // Wein die 50
        // erreicht hat
        //

        while (zaehler <= plusTage)
            do {

                LOG.info("Heute ist der {}, Tag {}", heute, zaehler);
                produkte = evaluiereSortimentUseCase.evaluiereProdukte(heute);
                LOG.info("Anzahl Produkte im Sortiment: {}", produkte.size());

                produkte.forEach(produkt -> {
                    LOG.info("Bezeichnung: {} | Preis: {} | Qualitaet: {} | Verfallen: {}",
                            formatLang(produkt.bezeichnung()),
                            formatKurz(produkt.preis()),
                            formatKurz(produkt.qualitaet()),
                            formatKurz(produkt.verfallenText()));
                });
                bereinigeSortimentUseCase.entferneVerfalleneProdukte(heute);
                heute = heute.plusDays(plusTage);

                LOG.info(" ");
                LOG.info(" ");
                //
                // Wenn der Käse verfallen ist, den Wein schneller älter werden lassen
                //
                if (zaehler > 100) {
                    zaehler += 10;
                    plusTage = 10;
                } else {
                    zaehler++;
                }
            } while (heute.isBefore(LocalDate.now().plusDays(600)));

        // 3)
        // Sortiment um Schokolade erweitern
        //

        LOG.info("Sortiment wird erweitert um Schokolade");

        final String preisFunktion = "(function berechnePreis(basisPreis, qualitaet, tageZumVerfallsdatum) { if(tageZumVerfallsdatum > 5) return basisPreis; return basisPreis/2 ; })";
        final String qualitaetFunktion = "(function berechneQualitaet(tageZumVerfallsdatum) {if(tageZumVerfallsdatum > 5) return 10;  return 0; })";
        final String verfallenFunktion = "(function isVerfallen(qualitaet, tageZumVerfallsdatum) { return  tageZumVerfallsdatum < 0; })";
        final String typ = "Schokolade";

        LOG.info("Anzahl Produkte im Sortiment: {}", produkte.size());

        erstelleKalkulationUseCase.erstelleVariableKalkulation(typ, preisFunktion, qualitaetFunktion,
                verfallenFunktion);

        createSortimentUseCase.erstelleSortimentAusDatei("static/sortiment/schokolade.json");

        heute = LocalDate.now();
        zaehler = 1;
        plusTage = 1;

        // 4)
        // Sortimentszustand für die nächsten 40 Tage bis Schokolade verfallen ist
        //
        do {
            LOG.info("Heute ist der {}, Tag {}", heute, zaehler);
            produkte = evaluiereSortimentUseCase.evaluiereProdukte(heute);
            LOG.info("Anzahl Produkte im Sortiment: {}", produkte.size());

            produkte.forEach(produkt -> {
                LOG.info("Bezeichnung: {} | Preis: {} | Qualitaet: {} | Verfallen: {}",
                        formatLang(produkt.bezeichnung()),
                        formatKurz(produkt.preis()),
                        formatKurz(produkt.qualitaet()),
                        formatKurz(produkt.verfallenText()));
            });
            bereinigeSortimentUseCase.entferneVerfalleneProdukte(heute);

            heute = heute.plusDays(plusTage);

            LOG.info(" ");
            LOG.info(" ");

            zaehler++;

        } while (heute.isBefore(LocalDate.now().plusDays(40)));

        //
        // Ende
        //

    }

    private String formatLang(Object value) {
        return String.format("%-26s", value.toString());
    }

    private String formatKurz(Object value) {
        return String.format("%-10s", value.toString());
    }
}
