package de.bastie.pica.bo;

/**
 * Die Steuerart
 *
 * @author Bastie - Sebastian Ritter
 * @version 1.0
 */
public interface ISteuerart extends IDatencontainer {

  /**
   * Der Datenlieferant f�r die Steuerart
   * @return IDatenlieferant
   */
  public IDatenlieferant getDatenlieferant ();

  /**
   * Gibt die Steuerart als XML String zur�ck
   * @return String
   */
  public String toXML ();
}
