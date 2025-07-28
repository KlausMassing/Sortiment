package com.superdupermarkt.sortiment.domain.util;

import org.springframework.stereotype.Component;

/**
 * Factory für die Erzeugung von Parser für verschiedene Typen von
 * Inhaltsobjekten.
 * Bedient das ServiceLocator Pattern, um die Abhängigkeiten zu kapseln und die
 * Erzeugung von Parsern zu vereinfachen.
 */
@Component
public interface ParserFactory {
    ProduktParser getParser(ContentTyp contentType);
}
