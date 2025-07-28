package com.superdupermarkt.sortiment.domain.util;

/**
 * Enum zur Darstellung von Content-Typen.
 * <p>
 * untterstützte Content-Typen sind:
 * <ul>
 * <li>JSON</li>
 * <li>XML</li>
 * <li>CSV</li>
 * </ul>
 * </p>
 *
 * <p>
 * Bietet eine Methode {@link #fromFilename(String)} um den Content-Typ
 * basierend auf der Dateiendung zu ermitteln.
 * </p>
 */
public enum ContentTyp {
    JSON, XML, CSV;

    public static ContentTyp fromFilename(String filename) {
        for (ContentTyp type : ContentTyp.values()) {
            if (filename.toUpperCase().endsWith(type.name())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown file type: " + filename);
    }
}
