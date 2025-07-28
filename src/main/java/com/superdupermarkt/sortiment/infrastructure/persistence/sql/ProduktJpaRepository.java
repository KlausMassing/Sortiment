package com.superdupermarkt.sortiment.infrastructure.persistence.sql;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.superdupermarkt.sortiment.infrastructure.persistence.sql.entity.ProduktEntity;

@Component
public interface ProduktJpaRepository extends JpaRepository<ProduktEntity, UUID> {

}