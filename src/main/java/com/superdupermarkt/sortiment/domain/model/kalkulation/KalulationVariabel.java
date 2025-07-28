package com.superdupermarkt.sortiment.domain.model.kalkulation;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;
import com.superdupermarkt.sortiment.domain.model.value.Preis;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false)
@Getter
public class KalulationVariabel extends Kalkulation {
    private static final Logger logger = LoggerFactory.getLogger(KalulationVariabel.class);

    private static final String GRAALJS = "js";

    private final String preisFunktion;
    private final String qualitaetFunktion;
    private final String verfallenFunktion;

    public KalulationVariabel(String typ, String preisFunktion, String qualitaetFunktion, String verfallenFunktion) {
        super(typ);
        this.preisFunktion = preisFunktion;
        this.qualitaetFunktion = qualitaetFunktion;
        this.verfallenFunktion = verfallenFunktion;
    }

    @Override
    public Preis berechnePreis(Produkt product, LocalDate aktuellesDatum) {
        BigDecimal basisPreis = product.getBasisPreis().preis();
        Integer qualitaet = berechneQualitaet(product, aktuellesDatum);
        Long tageZumVerfallsdatum = berechneTageZumVerfall(product, aktuellesDatum);
        double preis = 0.0;
        try (Context context = Context.create()) {
            Value value = context.eval(GRAALJS, preisFunktion);
            value = value.execute(basisPreis.doubleValue(), qualitaet, tageZumVerfallsdatum);

            preis = value.asDouble();
        } catch (PolyglotException e) {
            logError("Fehler bei der Ausführung der Preisfunktion: " + e.getMessage());
        }

        return new Preis(new BigDecimal(preis));
    }

    @Override
    public Integer berechneQualitaet(Produkt product, LocalDate aktuellesDatum) {
        Long tageZumVerfallsdatum = berechneTageZumVerfall(product, aktuellesDatum);

        Integer qualitaet = 0;

        try (Context context = Context.create()) {
            Value value = context.eval(GRAALJS, qualitaetFunktion);
            value = value.execute(tageZumVerfallsdatum);
            qualitaet = value.asInt();
        } catch (PolyglotException e) {
            logError("Fehler bei der Ausführung der Qualitaetfunktion: " + e.getMessage());
        }
        return qualitaet;
    }

    @Override
    public Boolean isVerfallen(Produkt product, LocalDate aktuellesDatum) {
        Integer qualitaet = berechneQualitaet(product, aktuellesDatum);
        Long tageZumVerfallsdatum = berechneTageZumVerfall(product, aktuellesDatum);

        Boolean isVerfallen = false;
        try (Context context = Context.create()) {
            Value value = context.eval(GRAALJS, verfallenFunktion);
            value = value.execute(qualitaet, tageZumVerfallsdatum);
            isVerfallen = value.asBoolean();
        } catch (PolyglotException e) {
            logError("Fehler bei der Ausführung der Verfallsfunktion: " + e.getMessage());
        }
        return isVerfallen;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + getTyp() + "]";
    }

    private void logError(String message) {
        logger.error(message);
    }
}
