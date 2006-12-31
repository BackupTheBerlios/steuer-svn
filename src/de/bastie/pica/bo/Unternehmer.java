/**
 * Unternehmer
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.bo;

import java.util.*;

/**
 * Repräsentation des Unternehmers
 *
 * @author © 2006 Bastie - Sebastian Ritter
 * @version 1.0
 */
public final class Unternehmer implements IDatencontainer {

  /**
   * Datenhaltung
   */
  private HashMap<String,String> idWert = new HashMap<String,String> ();

  /**
   * Die Datenart
   * @return String
   */
  public String getDatenArt() {
    return "Unternehmer"; //$NON-NLS-1$
  }

  /**
   * Name des Unternehmers
   * @param name String
   */
  public void setName (final String name) {
    this.setWert("UnternehmerName", name); //$NON-NLS-1$
  }
  /**
   * Name des Unternehmers
   * @return String
   */
  public String getName () {
    return this.getWert("UnternehmerName"); //$NON-NLS-1$
  }
  /**
   * Strassenname und Hausnummer des Unternehmer(sitzes)
   * @param strasse String
   */
  public void setStrasse (final String strasse) {
    this.setWert("UnternehmerStrasse",strasse);//$NON-NLS-1$
  }
  /**
   * Strassenname und Hausnummer des Unternehmer(sitzes)
   * @return String
   */
  public String getStrasse () {
    return this.getWert("UnternehmerStrasse");//$NON-NLS-1$
  }
  /**
   * PLZ des Unternehmer(sitzes)
   * @param plz String
   */
  public void setPLZ (final String plz) {
    this.setWert("UnternehmerPLZ",plz);//$NON-NLS-1$
  }
  /**
   * PLZ des Unternehmer(sitzes)
   * @return String
   */
  public String getPLZ () {
    return this.getWert("UnternehmerPLZ");//$NON-NLS-1$
  }
  /**
   * Ort des Unternehmer(sitzes)
   * @param ort String
   */
  public void setOrt (final String ort) {
    this.setWert("UnternehmerOrt", ort);//$NON-NLS-1$
  }
  /**
   * Ort des Unternehmer(sitzes)
   * @return String
   */
  public String getOrt () {
    return this.getWert("UnternehmerOrt");//$NON-NLS-1$
  }
  /**
   * Telefonnummer des Unternehmers
   * @param telefonnummer String
   */
  public void setTelefon (final String telefonnummer) {
    this.setWert("UnternehmerTelefon", telefonnummer);//$NON-NLS-1$
  }
  /**
   * Telefonnummer des Unternehmers
   * @return String
   */
  public String getTelefon () {
    return this.getWert("UnternehmerTelefon");//$NON-NLS-1$
  }
  /**
   * eMail Adresse des Unternehmers
   * @param eMailAdresse String
   */
  public void setEMail (final String eMailAdresse) {
    this.setWert("UnternehmerEMail",eMailAdresse);//$NON-NLS-1$
  }
  /**
   * eMail Adresse des Unternehmers
   * @return String
   */
  public String getEMail () {
    return this.getWert("UnternehmerEMail");//$NON-NLS-1$
  }

  /**
   * Gespeicherter Wert anhand der ID
   * @param id String
   * @return String
   */
  public String getWert(String id) {
    return this.idWert.get(id);
  }

  /**
   * Speichert den Wert anhand der ID
   * @param id String
   * @param wert String
   */
  public void setWert(String id, String wert) {
    this.idWert.put(id, wert);
  }

  public void removeWert(String id) {
    this.idWert.remove(id);
  }
}
