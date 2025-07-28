package com.superdupermarkt.sortiment.domain.util;

import java.io.File;
import java.util.List;

import com.superdupermarkt.sortiment.domain.model.produkt.Produkt;

public interface ProduktParser {
    List<Produkt> parse(File file);
}
