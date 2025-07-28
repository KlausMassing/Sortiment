package com.superdupermarkt.sortiment.domain.model.value;

import java.math.BigDecimal;

/**
 * Stellt ein Preis Value im Domain Model dar.
 * <p>
 * Es handelt sich um einen Wertobjekt (Value Object), das einen Preis
 * repräsentiert. Es ist unveränderlich (immutable) und enthält Methoden zur
 * Formatierung.
 * 
 *
 * @param preis the price value as {@link BigDecimal}
 */
public record Preis(BigDecimal preis) {

    public String formatiert() {
        return String.format("%,.2f", preis);
    }

    public String unformatiert() {
        return preis.toPlainString();
    }

    @Override
    public final String toString() {

        return formatiert();
    }
}
