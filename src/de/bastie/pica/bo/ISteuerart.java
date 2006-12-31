/**
 * ISteuerart
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.bo;

/**
 * Die Steuerart
 *
 * @author Bastie - Sebastian Ritter
 * @version 1.0
 */
public interface ISteuerart extends IDatencontainer {

  /**
   * Die Steuernummer für die Steuerart
   * @param stnrElsterFormat long
   */
  void setSteuernummer (final long stnrElsterFormat);
  /**
   * Die Steuernummer für die Steuerart
   * @return long
   */
  long getSteuernummer ();

  /**
   * Der Datenlieferant für die Steuerart
   * @return IDatenlieferant
   */
  public IDatenlieferant getDatenlieferant ();

  /**
   * Der Mitwirkende bei der Erstellung der Steuer-"erklärung"
   * @return IDatencontainer
   */
  public Mitwirkender getMitwirkender ();

  /**
   * Gibt die Steuerart als XML String zurück
   * @return String
   */
  public String toXML ();

  /**
   * Der Unternehmer, für den die Steuererklärung erstellt wird
   * @todo Auslagerung aus ISteuerart später notwendig z.B. für ESt
   * @return Unternehmer
   */
  public Unternehmer getUnternehmer();
}
