package de.bastie.pica.bo;

public interface Steuerart {

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

  /**
   * Gibt die Steuerart als XML String zurück
   * @return String
   */
  public String toXML ();
}
