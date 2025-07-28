package com.superdupermarkt.sortiment.domain.factory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.superdupermarkt.sortiment.domain.model.kalkulation.Kalkulation;
import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;
import com.superdupermarkt.sortiment.domain.model.produkt.ProduktJson;
import com.superdupermarkt.sortiment.domain.model.value.Preis;
import com.superdupermarkt.sortiment.domain.service.KalkulationService;

@Component
public class ProduktFactory {

    private static final Logger logger = LoggerFactory.getLogger(ProduktFactory.class);

    public static final DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .appendLiteral('.')
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral('.')
            .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .toFormatter();

    private final KalkulationService kalkulationService;

    public ProduktFactory(KalkulationService kalkulationService) {
        this.kalkulationService = kalkulationService;
    }

    public Optional<Produkt> erstelleProduktAusJson(String json) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            ProduktJson product = mapper.readValue(json, ProduktJson.class);
            return erstelleProdukt(product.getId(), product.getProdukttyp(), product.getBezeichnung(),
                    product.getBasispreis(),
                    product.getVerfallsdatum(), product.getEinraeumdatum());
        } catch (IOException e) {
            logError("Fehler beim Verarbeiten der JSON-Daten" + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Produkt> erstelleProduktListeAusJson(InputStream jsonData) {

        ObjectMapper mapper = new ObjectMapper();

        List<Produkt> produkte = new ArrayList<>();
        List<ProduktJson> products = new ArrayList<>();
        try {
            products = mapper.readValue(jsonData, new TypeReference<List<ProduktJson>>() {
            });
            produkte = products.stream()
                    .map(product -> erstelleProdukt(product.getId(), product.getProdukttyp(), product.getBezeichnung(),
                            product.getBasispreis(),
                            product.getVerfallsdatum(), product.getEinraeumdatum()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

        } catch (IOException e) {
            logError("Fehler beim Verarbeiten der JSON-Daten" + e.getMessage());
        }

        return produkte;
    }

    public Optional<Produkt> erstelleProduktAusCsv(String csv) {

        String[] parts = csv.split(",");

        if (parts.length >= 4) {

            return erstelleProdukt("", parts[0], parts[1], parts[2], parts[3], null);
        }
        return Optional.empty();
    }

    private Optional<Produkt> erstelleProdukt(String id, String kalkulation, String bezeichnung, String preis,
            String verfallsDatum, String einraeumDatum) {

        UUID uuid = erstelleUuid(id);
        Optional<Kalkulation> _kalkulation = kalkulationService.findeKalkulation(kalkulation);

        Optional<Preis> _preis = erstellePreis(preis);
        Optional<LocalDate> _verfallsDatum = erstelleDatum(verfallsDatum);
        Optional<LocalDate> _einraeumDatum = erstelleDatum(einraeumDatum);

        LocalDate einraeumLocalDate = _einraeumDatum.isPresent() ? _einraeumDatum.get() : LocalDate.now();

        if (_kalkulation.isPresent() && _preis.isPresent() && _verfallsDatum.isPresent()) {

            Produkt produkt = new Produkt(uuid, bezeichnung, _preis.get(), _verfallsDatum.get(),
                    einraeumLocalDate, _kalkulation.get());
            return Optional.of(produkt);
        } else {
            if (!_kalkulation.isPresent())
                logError("Invalide Kalkulation");
            if (!_preis.isPresent())
                logError("Invalider Preis");
            if (!_verfallsDatum.isPresent())
                logError("Invalides Verfallsdatum");
        }
        return Optional.empty();

    }

    private UUID erstelleUuid(String id) {
        if (id != null && !id.isEmpty()) {
            try {
                return UUID.fromString(id);
            } catch (IllegalArgumentException e) {
                logError(e.getMessage());
            }
        }
        return UUID.randomUUID();

    }

    private void logError(String message) {
        logger.error(message);
    }

    public Optional<Preis> erstellePreis(String preis) {
        if (preis != null && !preis.isEmpty()) {
            try {
                return Optional.of(new Preis(new BigDecimal(preis.trim())));
            } catch (Exception e) {
                logError("Invalid Preis format: " + preis + " - " + e.getMessage());
            }
        }
        return Optional.empty();
    }

    public Optional<Integer> erstelleQualitaet(String qualitaet) {
        if (qualitaet != null && !qualitaet.isEmpty()) {
            try {
                return Optional.of(Integer.valueOf(qualitaet));
            } catch (Exception e) {
                logError("Invalid Qualität format: " + qualitaet);
            }
        }
        return Optional.empty();
    }

    public Optional<LocalDate> erstelleDatum(String datum) {
        if (datum != null && !datum.isEmpty()) {
            try {
                return Optional.of(LocalDate.parse(datum.trim(), DATE_FORMAT));
            } catch (Exception e) {
                logError("Invalid Verfallsdatum format: " + datum + " - " + e.getMessage());
            }
        }
        return Optional.empty();
    }
}
