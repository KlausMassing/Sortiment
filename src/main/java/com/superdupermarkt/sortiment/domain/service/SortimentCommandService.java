package com.superdupermarkt.sortiment.domain.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;
import com.superdupermarkt.sortiment.domain.model.produkt.ProduktView;
import com.superdupermarkt.sortiment.domain.repository.ProduktRepository;
import com.superdupermarkt.sortiment.domain.util.ContentTyp;
import com.superdupermarkt.sortiment.domain.util.ParserFactory;
import com.superdupermarkt.sortiment.domain.util.ProduktParser;

@Service
public class SortimentCommandService {
    private static final Logger logger = LoggerFactory.getLogger(SortimentCommandService.class);

    private final ProduktRepository repository;

    private final ParserFactory parserFactory;

    public SortimentCommandService(ProduktRepository repository, ParserFactory parserFactory) {
        this.repository = repository;
        this.parserFactory = parserFactory;
    }

    public void addProduct(Produkt product) {
        repository.save(product);
    }

    public void removeProduct(UUID id) {
        repository.deleteById(id);
    }

    public void erstelleSortimentAusDatei(String filename) {

        try {
            File file = ResourceUtils.getFile("classpath:" + filename);

            ProduktParser parser = parserFactory.getParser(ContentTyp.fromFilename(filename));
            List<Produkt> produkte = parser.parse(file);

            repository.saveAll(produkte);

        } catch (FileNotFoundException e) {
            logger.error("Datei {} nicht gefunden", filename, e);
        } catch (RuntimeException e) {
            logger.error("Laufzeitfehler beim Verarbeiten der Datei {} ", filename, e);
        }

    }

    public void entferneVerfalleneProdukte(LocalDate heute) {
        List<ProduktView> alle = repository.findAll().stream()
                .map(produkt -> produkt.toProduktView(heute))
                .toList();

        alle.stream()
                .filter(ProduktView::verfallen)
                .forEach(produkt -> removeProduct(produkt.id()));
    }

}
