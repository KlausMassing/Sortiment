package com.superdupermarkt.sortiment.infrastructure.persistence.sql.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProduktEntity {

    @Id
    private UUID id;
    private String bezeichnung;
    private BigDecimal basispreis;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate verfallsdatum;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate einraeumdatum;
    private String produkttyp;

    public Optional<String> toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return Optional.ofNullable(mapper.writeValueAsString(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
