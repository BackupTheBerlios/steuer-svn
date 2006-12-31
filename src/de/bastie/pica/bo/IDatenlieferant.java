/**
 * IDatenlieferant
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.bo;

/**
 * Der Datenlieferant (auch Datenübermitter oder -sender genannt) ist derjenige
 * der die Daten übermittelt. Diese Person kann insbesondere von dem
 * Steuerpflichten (Mandanten) etc. abweichen. Zudem weicht meist der
 * Hersteller der Software (Producer) vom Datenlieferanten ab.
 *
 * @todo Prüfen, ob allgemein genug für DatenLieferant bei Nicht Anmeldesteuern
 * @author Bastie - Sebastian Ritter
 * @version 1.0
 */
public interface IDatenlieferant extends IDatencontainer {
  /**
   * Der Name des Datenlieferantes
   * @param name String
   */
  public void setName (final String name);
  /**
   * Der Name des Datenlieferantes
   * @return String
   */
  public String getName ();
  /**
   * Die Strasse der Adresse des Datenlieferantes
   * @param strasse String
   */
  public void setStrasse (final String strasse);
  /**
   * Die Strasse der Adresse des Datenlieferantes
   * @return String
   */
  public String getStrasse ();
  /**
   * Die PLZ der Adresse des Datenlieferantes
   * @param plz String
   */
  public void setPLZ (final String plz);
  /**
   * Die PLZ der Adresse des Datenlieferantes
   * @return String
   */
  public String getPLZ ();
  /**
   * Der Ort der Adresse des Datenlieferantes
   * @param ort String
   */
  public void setOrt (final String ort);
  /**
   * Der Ort der Adresse des Datenlieferantes
   * @return String
   */
  public String getOrt ();
  /**
   * Die Telefonnummer des Datenlieferantes
   * @param telefon String
   */
  public void setTelefon (final String telefon);
  /**
   * Die Telefonnummer des Datenlieferantes
   * @return String
   */
  public String getTelefon ();
  /**
   * Die eMail Adresse des Datenlieferantes
   * @param eMail String
   */
  public void setEMail (final String eMail);
  /**
   * Die eMail Adresse des Datenlieferantes
   * @return String
   */
  public String getEMail ();


}
