package com.superdupermarkt.sortiment.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;
import com.superdupermarkt.sortiment.domain.model.produkt.ProduktView;
import com.superdupermarkt.sortiment.domain.repository.ProduktRepository;
import com.superdupermarkt.sortiment.domain.util.ContentTyp;
import com.superdupermarkt.sortiment.domain.util.ParserFactory;
import com.superdupermarkt.sortiment.domain.util.ProduktParser;

class SortimentCommandServiceTest {

    private ProduktRepository repository;
    private ParserFactory parserFactory;
    private SortimentCommandService service;

    @BeforeEach
    void setUp() {
        repository = mock(ProduktRepository.class);
        parserFactory = mock(ParserFactory.class);
        service = new SortimentCommandService(repository, parserFactory);
    }

    @Test
    void testAddProduct() {
        Produkt produkt = mock(Produkt.class);
        service.addProduct(produkt);
        verify(repository).save(produkt);
    }

    @Test
    void testRemoveProduct() {
        UUID id = UUID.randomUUID();
        service.removeProduct(id);
        verify(repository).deleteById(id);
    }

    @Test
    void testErstelleSortimentAusDatei_success() throws Exception {
        String filename = "produkte.csv";
        File file = mock(File.class);
        ProduktParser parser = mock(ProduktParser.class);
        List<Produkt> produkte = Arrays.asList(mock(Produkt.class), mock(Produkt.class));

        when(parserFactory.getParser(ContentTyp.fromFilename(filename))).thenReturn(parser);
        when(parser.parse(file)).thenReturn(produkte);

        service.erstelleSortimentAusDatei(filename);

    }

    @Test
    void testErstelleSortimentAusDatei_fileNotFound() throws Exception {
        String filename = "notfound.csv";

        service.erstelleSortimentAusDatei(filename);

        // Should not throw, just log error
        verifyNoInteractions(parserFactory);
        verify(repository, never()).saveAll(any());

    }

    @Test
    void testErstelleSortimentAusDatei_runtimeException() throws Exception {
        String filename = "produkte.csv";
        File file = mock(File.class);
        ProduktParser parser = mock(ProduktParser.class);

        when(parserFactory.getParser(ContentTyp.fromFilename(filename))).thenReturn(parser);
        when(parser.parse(file)).thenThrow(new RuntimeException("Parse error"));

        service.erstelleSortimentAusDatei(filename);

        verify(repository, never()).saveAll(any());

    }

    @Test
    void testEntferneVerfalleneProdukte() {
        Produkt produkt1 = mock(Produkt.class);
        Produkt produkt2 = mock(Produkt.class);
        ProduktView view1 = mock(ProduktView.class);
        ProduktView view2 = mock(ProduktView.class);

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        when(repository.findAll()).thenReturn(Arrays.asList(produkt1, produkt2));
        when(produkt1.toProduktView(any())).thenReturn(view1);
        when(produkt2.toProduktView(any())).thenReturn(view2);

        when(view1.verfallen()).thenReturn(true);
        when(view2.verfallen()).thenReturn(false);
        when(view1.id()).thenReturn(id1);

        service.entferneVerfalleneProdukte(LocalDate.now());

        verify(repository).deleteById(id1);
        verify(repository, never()).deleteById(id2);
    }

}