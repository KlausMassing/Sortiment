package com.superdupermarkt.sortiment.infrastructure.persistence.sql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.superdupermarkt.sortiment.SortimentApplication;
import com.superdupermarkt.sortiment.domain.factory.ProduktFactory;
import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;
import com.superdupermarkt.sortiment.domain.repository.ProduktRepository;
import com.superdupermarkt.sortiment.infrastructure.persistence.sql.entity.ProduktEntity;

@Repository
@Profile(SortimentApplication.PROFIL_SQL)

public class SqlProduktRepository implements ProduktRepository {

    private final ProduktJpaRepository repository;
    private final ProduktFactory produktFactory;

    public SqlProduktRepository(ProduktJpaRepository repository, ProduktFactory produktFactory) {
        this.repository = repository;
        this.produktFactory = produktFactory;
    }

    private Optional<Produkt> entityToProdukt(ProduktEntity produktEntity) {

        Optional<String> json = produktEntity.toJson();
        if (json.isPresent())
            return produktFactory.erstelleProduktAusJson(json.get());

        return Optional.empty();
    }

    @Override
    public List<Produkt> findAll() {

        List<Produkt> produkte = repository.findAll().stream()
                .map((produktEntity) -> {
                    return entityToProdukt(produktEntity);
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return produkte;
    }

    @Override
    public Optional<Produkt> findById(UUID id) {
        Optional<ProduktEntity> p = repository.findById(id);
        if (p.isPresent())
            return entityToProdukt(p.get());

        return Optional.empty();
    }

    @Override
    public void save(Produkt product) {
        ProduktEntity produktEntity = new ProduktEntity(product.getId(), product.getBezeichnung(),
                product.getBasisPreis().preis(), product.getVerfallsdatum(), product.getEinraeumDatum(),
                product.getKalkulation().getTyp());
        produktEntity = repository.save(produktEntity);

    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void saveAll(List<Produkt> produkte) {

        produkte.stream().forEach((produkt) -> {
            save(produkt);
        });
    }

}
