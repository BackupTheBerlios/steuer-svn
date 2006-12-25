package de.bastie.pica.bo;

import java.util.HashMap;
/**
 *
 * @author unbekannt
 * @version 1.0
 */
public class DatenlieferantImpl implements IDatenlieferant {
  private String NAME = "Name", STRASSE = "Strasse", ORT ="Ort", PLZ ="plz", TELEFON = "Telefon", EMAIL ="EMail";
  public String getName() {
    return this.getWert(NAME);
  }
  public void setName (final String name) {
    this.setWert(NAME,name);
  }

  public String getStrasse() {
    return this.getWert(STRASSE);
  }
  public void setStrasse (final String strasse) {
    this.setWert(STRASSE,strasse);
  }

  public String getPLZ() {
    return this.getWert(PLZ);
  }
  public void setPLZ (final String postleitzahl) {
    this.setWert(PLZ,postleitzahl);
  }

  public String getOrt() {
    return this.getWert(ORT);
  }
  public void setOrt (final String ort) {
    this.setWert(ORT,ort);
  }

  public String getTelefon() {
    return this.getWert(TELEFON);
  }
  public void setTelefon (final String telefonnummer) {
    this.setWert(TELEFON,telefonnummer);
  }

  public String getEMail() {
    return this.getWert(EMAIL);
  }
  public void setEMail (final String eMail) {
    this.setWert(EMAIL,eMail);
  }

  private HashMap<String,String> wert = new HashMap<String,String> ();

  public String getDatenArt() {
    return "Datenlieferer";
  }

  public String getWert(String id) {
    return this.wert.get(id);
  }

  public void setWert(final String id, final String wert) {
    this.wert.put(id,wert);
  }
}
