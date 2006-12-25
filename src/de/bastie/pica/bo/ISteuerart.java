package de.bastie.pica.bo;

/**
 * Die Steuerart
 *
 * @author Bastie - Sebastian Ritter
 * @version 1.0
 */
public interface ISteuerart extends IDatencontainer {

  /**
   * Der Datenlieferant für die Steuerart
   * @return IDatenlieferant
   */
  public IDatenlieferant getDatenlieferant ();

  /**
   * Gibt die Steuerart als XML String zurück
   * @return String
   */
  public String toXML ();
}
