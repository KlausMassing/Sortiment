package com.superdupermarkt.sortiment.infrastructure.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.superdupermarkt.sortiment.domain.factory.ProduktFactory;
import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;
import com.superdupermarkt.sortiment.domain.util.ProduktParser;

@Component("JSON")
public class JsonProduktParser implements ProduktParser {
    private static final Logger logger = LoggerFactory.getLogger(JsonProduktParser.class);
    private final ProduktFactory produktFactory;

    public JsonProduktParser(ProduktFactory produktFactory) {
        this.produktFactory = produktFactory;
    }

    @Override
    public List<Produkt> parse(File file) {
        List<Produkt> parsedData = new ArrayList<>();
        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            parsedData = produktFactory.erstelleProduktListeAusJson(inputStream);
        } catch (FileNotFoundException e) {
            logError("Datei nicht gefunden: " + e.getMessage());
        } catch (IOException e1) {
            logError("Datei kann nicht gelesen werden: " + e1.getMessage());
        } catch (RuntimeException e2) {
            logError("Verarbeitung der CSV Datei fehlgeschlagen: " + e2.getMessage());
        }

        return parsedData;
    }

    private void logError(String message) {
        logger.error(message);
    }
}
