package com.superdupermarkt.sortiment.domain.model.produkt;

import java.time.LocalDate;
import java.util.UUID;

import com.superdupermarkt.sortiment.domain.model.kalkulation.Kalkulation;
import com.superdupermarkt.sortiment.domain.model.value.Preis;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Die Klasse {@code Produkt} ist eine Aggreagate (und Domain Entity) und
 * repräsentiert ein Produkt im Sortiment.
 * Sie enthält Informationen wie die eindeutige ID, Bezeichnung, Basispreis,
 * Verfallsdatum, Einräumdatum sowie eine Kalkulation zur Berechnung von
 * Qualität und Preis.
 *
 * <p>
 * Wichtige Methoden:
 * <ul>
 * <li>{@link #aktuelleQualitaet(LocalDate)} – Berechnet die aktuelle Qualität
 * des Produkts für ein gegebenes Datum.</li>
 * <li>{@link #aktuellerPreis(LocalDate)} – Berechnet den aktuellen Preis des
 * Produkts für ein gegebenes Datum.</li>
 * <li>{@link #isVerfallen(LocalDate)} – Prüft, ob das Produkt zum gegebenen
 * Datum verfallen ist.</li>
 * <li>{@link #toProduktView(LocalDate)} – Erstellt eine Präsentationsansicht
 * des Produkts für ein gegebenes Datum.</li>
 * </ul>
 *
 * <p>
 * Instanzen dieser Klasse sind unveränderlich (immutable).
 *
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Produkt {

    final UUID id;
    final String bezeichnung;
    final Preis basisPreis;
    final LocalDate verfallsDatum;
    final LocalDate einraeumDatum;
    final Kalkulation kalkulation;

    public Integer aktuelleQualitaet(LocalDate aktuellesDatum) {

        return kalkulation.berechneQualitaet(this, aktuellesDatum);
    }

    public Preis aktuellerPreis(LocalDate aktuellesDatum) {

        return kalkulation.berechnePreis(this, aktuellesDatum);
    }

    public Preis getBasisPreis() {

        return basisPreis;
    }

    public String getBezeichnung() {

        return bezeichnung;
    }

    public LocalDate getVerfallsdatum() {

        return verfallsDatum;
    }

    public boolean isVerfallen(LocalDate aktuellesDatum) {

        return kalkulation.isVerfallen(this, aktuellesDatum);
    }

    public ProduktView toProduktView(LocalDate aktuellesDatum) {
        return new ProduktView(
                id,
                bezeichnung,
                aktuellerPreis(aktuellesDatum),
                aktuelleQualitaet(aktuellesDatum),
                isVerfallen(aktuellesDatum));
    }
}
