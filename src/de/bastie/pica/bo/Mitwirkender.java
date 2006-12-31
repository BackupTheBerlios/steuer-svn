/**
 * Mitwirkender
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.bo;

import java.util.HashMap;

/**
 * Repräsentation des Mitwirkenden bei einer Steuererklärung z.B. UStVA
 * @author © 2006 Bastie - Sebastian Ritter
 * @version 1.0
 */
public class Mitwirkender implements IDatencontainer {

  private HashMap<String,String> idWert = new HashMap<String,String> ();

  public String getDatenArt() {
    return "Mitwirkender";
  }

  public String getName () {
    return this.idWert.get("MitwirkenderName");
  }
  public void setName (final String name) {
    this.idWert.put("MitwirkenderName", name);
  }

  public String getBeruf () {
    return this.idWert.get("MitwirkenderBeruf");
  }
  public void setBeruf (final String berufsbezeichnung) {
    this.idWert.put("MitwirkenderBeruf", berufsbezeichnung);
  }

  public String getVorwahl () {
    return this.idWert.get("MitwirkenderVorwahl");
  }
  public void setVorwahl (final String vorwahl) {
    this.idWert.put("MitwirkenderVorwahl", vorwahl);
  }

  public String getRufnummer () {
    return this.idWert.get("MitwirkenderRufnummer");
  }
  public void setRufnummer (final String rufnummer) {
    this.idWert.put("MitwirkenderRufnummer", rufnummer);
  }

  public String getWert(String id) {
    return this.idWert.get (id);
  }

  public void setWert(String id, String wert) {
    this.idWert.put(id,wert);
  }

  public void removeWert(String id) {
    this.idWert.remove(id);
  }
}
