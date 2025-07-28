package com.superdupermarkt.sortiment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SortimentApplication {

	public static final String PROFIL_REST = "rest"; // startet die REST Endpunkte, sonst wird der
														// SortimentConsoleRunner gestartet.
	public static final String PROFIL_SQL = "sql"; // benutzt H2 für die Datenbank und JPA für die Persistence

	public static void main(String[] args) {

		// System.setProperty("spring.profiles.active", PROFIL_SQL +","+PROFIL_REST);
		System.setProperty("spring.profiles.active", PROFIL_SQL);

		// Unterdrückt die Warnung, die GraalJS erzeugt
		System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");

		SpringApplication.run(SortimentApplication.class, args);
	}

}
