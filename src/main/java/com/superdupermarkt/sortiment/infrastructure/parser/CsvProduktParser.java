package com.superdupermarkt.sortiment.infrastructure.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.superdupermarkt.sortiment.domain.factory.ProduktFactory;
import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;
import com.superdupermarkt.sortiment.domain.util.ProduktParser;

@Component("CSV")
public class CsvProduktParser implements ProduktParser {
    private static final Logger logger = LoggerFactory.getLogger(CsvProduktParser.class);
    private final ProduktFactory produktFactory;

    public CsvProduktParser(ProduktFactory produktFactory) {
        this.produktFactory = produktFactory;
    }

    @Override
    public List<Produkt> parse(File file) {

        List<Produkt> parsedData = new ArrayList<>();
        try (BufferedReader bufferedReader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                Optional<Produkt> p = produktFactory.erstelleProduktAusCsv(line);
                if (p.isPresent()) {
                    parsedData.add(p.get());
                } else {
                    logError("Ungültige CSV Zeile: " + line);
                }
            }

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
