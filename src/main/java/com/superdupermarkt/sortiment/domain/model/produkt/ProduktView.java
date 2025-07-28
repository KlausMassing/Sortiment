package com.superdupermarkt.sortiment.domain.model.produkt;

import java.util.UUID;

import com.superdupermarkt.sortiment.domain.model.value.Preis;

/**
 * Ist das View-Model für ein Produkt.
 *
 * @param id          ID des Produkts.
 * @param bezeichnung Beschreibung des Produkts.
 * @param preis       Der tagesaktuelle Preis des Produkts.
 * @param qualitaet   Die tagesaktuelle Qualität des Produkts.
 * @param verfallen   Verfallen oder der tagesaktuelle Qualitätsitungsstatus des
 *                    Produkts ist zu gering.
 *
 *                    <p>
 *                    Bietet eine Methode {@link #verfallenText()} für eine
 *                    lesbare Darstellung des Verfallsstatus.
 *                    </p>
 */
public record ProduktView(
                UUID id,
                String bezeichnung,
                Preis preis,
                Integer qualitaet, Boolean verfallen) {

        public String verfallenText() {
                return verfallen ? "Ja" : "Nein";
        }
}
