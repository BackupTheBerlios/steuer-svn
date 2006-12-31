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
   * Die Steuernummer f�r die Steuerart
   * @param stnrElsterFormat long
   */
  void setSteuernummer (final long stnrElsterFormat);
  /**
   * Die Steuernummer f�r die Steuerart
   * @return long
   */
  long getSteuernummer ();

  /**
   * Der Datenlieferant f�r die Steuerart
   * @return IDatenlieferant
   */
  public IDatenlieferant getDatenlieferant ();

  /**
   * Der Mitwirkende bei der Erstellung der Steuer-"erkl�rung"
   * @return IDatencontainer
   */
  public Mitwirkender getMitwirkender ();

  /**
   * Gibt die Steuerart als XML String zur�ck
   * @return String
   */
  public String toXML ();

  /**
   * Der Unternehmer, f�r den die Steuererkl�rung erstellt wird
   * @todo Auslagerung aus ISteuerart sp�ter notwendig z.B. f�r ESt
   * @return Unternehmer
   */
  public Unternehmer getUnternehmer();
}
