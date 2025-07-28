# Sortiment

Dieses Projekt implementiert ein Sortiment-Modul unter Anwendung moderner Software-Architekturprinzipien. Im Mittelpunkt stehen Domain-Driven Design (DDD) und bewährte Entwurfsmuster, um eine robuste, flexible und wartbare Lösung zu schaffen.
Es wurde für dieses Beispiel bewust darauf verzichtet die Anwendung in drei Module aufzuteilen. Vorteil wäre, das nur das JAR für Applikationslogik in fremde Systeme eingebnden werden muss, um auf die Funnktionaität der Anwendung zuzugreifen.

## Spring Boot

Es handelt sich um eine Spring Boot Anwendung mit zwei Profilen:

- **`rest`**
- **`h2`**

Das Profil `rest` enthält die Applikationslogik und die Endpoints, die über eine REST-API zur Verfügung stehen.
Das Profil `h2` ist ein H2-Datenbank-Modul, das die Datenbank für die Anwendung initialisiert und verwendet.

Ohne das Profil `rest` wird die Anwendung einen Testlauf auf der Log-Console ausführen.

## Verwendete Patterns und deren Vorteile

- **Repository Pattern**  
   Das Repository Pattern abstrahiert den Datenzugriff und trennt die Domänenlogik von der Persistenzschicht. Dadurch kann die Geschäftslogik unabhängig von der verwendeten Datenbank oder Infrastruktur entwickelt und getestet werden. Vorteile:

  - Erleichtert das Testen durch Mocking von Repositories
  - Ermöglicht Austausch der Persistenztechnologie ohne Anpassung der Domäne

- **Factory Pattern**  
   Das Factory Pattern wird eingesetzt, um komplexe Domänenobjekte zu erzeugen und dabei sicherzustellen, dass alle Invarianten und Regeln eingehalten werden. Vorteile:

  - Zentralisiert die Objekt-Erstellung und hält die Domänenlogik sauber
  - Verhindert inkonsistente Zustände von Objekten

- **Aggregate Root**  
   Aggregate Roots kontrollieren den Zugriff auf zusammengehörige Entitäten und gewährleisten Konsistenz innerhalb eines Aggregats. Vorteile:

  - Schutz der Integrität von Aggregaten
  - Klare Schnittstellen für Änderungen an zusammengehörigen Objekten

- **Value Object**  
   Value Objects repräsentieren Werte ohne eigene Identität und sind unveränderlich. Vorteile:

  - Vereinfachen die Modellierung von Konzepten wie Maßeinheiten oder Geldbeträgen
  - Erhöhen die Sicherheit und Nachvollziehbarkeit durch Unveränderlichkeit

- **Service Pattern**  
   Services kapseln domänenspezifische Geschäftslogik, die nicht direkt zu einer Entität oder einem Value Object gehört. Vorteile:

  - Trennung von Verantwortlichkeiten
  - Förderung der Wiederverwendbarkeit von Logik

- **CQRS**  
  Das CQRS (Command Query Responsibility Segregation) Pattern trennt die Anforderungen an die Domäne in zwei Teile: Anforderungen zum Ändern (Commands) und Abfragen (Queries). Dadurch wird die Anforderungsebene von der Geschäftslogik getrennt, was die Anforderungen leichter zu testen und zu implementieren macht. Vorteile:
  - Verbessert die Testbarkeit und die Wiederverwendbarkeit von Logik
  - Verbessert die Komplexität der Anforderungsabwicklung

## DDD-Ansatz

Das Projekt folgt konsequent dem Domain-Driven Design Ansatz:

- **Domänenmodell**  
   Die zentrale Geschäftslogik ist in klar abgegrenzten Domänenklassen modelliert. Dadurch bleibt die Fachlichkeit im Mittelpunkt und technische Details werden ausgeblendet.

- **Bounded Context**  
   Das Sortiment-Modul bildet einen eigenen Bounded Context mit klar definierten Schnittstellen zu anderen Modulen. So werden Überschneidungen und Missverständnisse zwischen verschiedenen Fachbereichen vermieden.

- **Ubiquitous Language**  
   Die im Code verwendeten Begriffe spiegeln die Sprache der Fachdomäne wider. Dies erleichtert die Kommunikation zwischen Entwicklern und Fachexperten und reduziert Missverständnisse.

- **Trennung von Domäne und Infrastruktur**  
   Die Domänenschicht ist strikt unabhängig von technischen Details wie Datenbanken, Frameworks oder externen Systemen. Dadurch bleibt die Geschäftslogik portabel und leichter testbar.

## Ziel

Das Ziel dieses Projekts ist eine wartbare, erweiterbare und fachlich korrekte Implementierung des Sortiments. Die Architektur ermöglicht es, zukünftige Anforderungen flexibel und effizient umzusetzen, ohne die Integrität der Domäne zu gefährden. Durch den Einsatz von DDD und bewährten Patterns wird eine nachhaltige und qualitativ hochwertige Softwarebasis geschaffen.
