package de.bastie.pica.bo;

/**
 * Der Datenlieferant (auch Daten�bermitter oder -sender genannt) ist derjenige
 * der die Daten �bermittelt. Diese Person kann insbesondere von dem
 * Steuerpflichten (Mandanten) etc. abweichen. Zudem weicht meist der
 * Hersteller der Software (Producer) vom Datenlieferanten ab.
 *
 * @todo Pr�fen, ob allgemein genug f�r DatenLieferant bei Nicht Anmeldesteuern
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
