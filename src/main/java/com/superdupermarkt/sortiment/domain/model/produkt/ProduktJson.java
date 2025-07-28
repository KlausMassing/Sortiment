package com.superdupermarkt.sortiment.domain.model.produkt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Datatransfer Object (DTO) for Produkt im Json-Format.
 * 
 *
 * <p>
 * Felder:
 * <ul>
 * <li>bezeichnung - Bezeichnung des Produktes.</li>
 * <li>basispreis - Basispreis des Produktes.</li>
 * <li>verfallsdatum - Verfallsdatum des Produktes.</li>
 * <li>produkttyp - Produkttyp des Produktes zur bestimmung der
 * Kalkulation.</li>
 * </ul>
 * </p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProduktJson {

    String id;
    String produkttyp;
    String bezeichnung;
    String basispreis;
    String verfallsdatum;
    // Optionales Feld, falls das Produkt ein Einräumdatum hat
    String einraeumdatum;

}
