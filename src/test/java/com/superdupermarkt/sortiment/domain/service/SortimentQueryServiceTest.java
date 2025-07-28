package com.superdupermarkt.sortiment.domain.service;

import com.superdupermarkt.sortiment.domain.model.produkt.ProduktView;
import com.superdupermarkt.sortiment.domain.repository.ProduktRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SortimentQueryServiceTest {

    private ProduktRepository repository;
    private SortimentQueryService service;

    @BeforeEach
    void setUp() {
        repository = mock(ProduktRepository.class);
        service = new SortimentQueryService(repository);
    }

    @Test
    void evaluiereProdukte_shouldReturnProduktViews() {
        // Arrange
        var heute = LocalDate.now();

        var produkt1 = mock(com.superdupermarkt.sortiment.domain.model.produkt.Produkt.class);
        var produkt2 = mock(com.superdupermarkt.sortiment.domain.model.produkt.Produkt.class);

        var view1 = mock(ProduktView.class);
        var view2 = mock(ProduktView.class);

        when(repository.findAll()).thenReturn(Arrays.asList(produkt1, produkt2));
        when(produkt1.toProduktView(heute)).thenReturn(view1);
        when(produkt2.toProduktView(heute)).thenReturn(view2);

        // Act
        List<ProduktView> result = service.evaluiereProdukte(heute);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(view1));
        assertTrue(result.contains(view2));
        verify(repository).findAll();
        verify(produkt1).toProduktView(heute);
        verify(produkt2).toProduktView(heute);
    }

    @Test
    void evaluiereProdukte_shouldReturnEmptyListWhenNoProducts() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of());

        // Act
        List<ProduktView> result = service.evaluiereProdukte(LocalDate.now());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findAll();
    }
}