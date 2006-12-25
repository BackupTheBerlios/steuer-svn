package de.bastie.pica.bo;

/**
 * Der Datenlieferant (auch Datenübermitter oder -sender genannt) ist derjenige
 * der die Daten übermittelt. Diese Person kann insbesondere von dem
 * Steuerpflichten (Mandanten) etc. abweichen. Zudem weicht meist der
 * Hersteller der Software (Producer) vom Datenlieferanten ab.
 *
 * @todo Prüfen, ob allgemein genug für DatenLieferant bei Nicht Anmeldesteuern
 * @author unbekannt
 * @version 1.0
 */
public interface DatenLieferant {
  public String getName ();
  public String getStrasse ();
  public String getPLZ ();
  public String getOrt ();
  public String getTelefon ();
  public String getEMail ();

  public String toString ();
}
