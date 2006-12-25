package de.bastie.pica.bo;

/**
 * Der Datencontainer dient zum Sammeln der Informationen, welche z.B. über
 * eine Eingabemaske erzeugt werden.
 * @author unbekannt
 * @version 1.0
 */
public interface IDatencontainer {
    /**
   * Liefert die Information zur Datenart zurück.
   * @return String
   */
  public String getDatenArt ();

  /**
   * Liefert einen Eingabewert bezeichnet durch die ID (Kennzahl/Feldkennung/...).
   * @param id String
   * @return String
   */
  public String getWert(final String id);

  /**
   * Setzt einen Eingabewert bezeichnet durch die ID (Kennzahl/Feldkennung/...).
   * @param id String
   * @param wert String
   */
  public void setWert (final String id, final String wert);


}
