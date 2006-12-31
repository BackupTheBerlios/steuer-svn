/**
 * UmsatzsteuervoranmeldungImpl
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.bo.ustva;

import java.text.*;
import java.util.*;

import de.bastie.pica.bo.*;

/**
 * Implementierung einer Umsatzsteuervoranmeldung
 *
 * @author © 2006 Bastie - Sebastian Ritter
 * @version 1.0
 */
public class UmsatzsteuervoranmeldungImpl implements Umsatzsteuervoranmeldung {

  private long stnrElsterFormat;
  private HashMap werte = new HashMap();
  private IDatenlieferant datenLieferant;
  private Mitwirkender mitwirkender;
  private Unternehmer unternehmer;

  /**
   * Konstruktor, welcher eine neue Umsatzsteuervoranmeldung erzeugt inkl.
   * der notwendigen verbundenen Teile wie Mitwirkender, Datenlieferer und
   * Unternehmer
   */
  public UmsatzsteuervoranmeldungImpl () {
    this.mitwirkender = new Mitwirkender ();
    this.unternehmer = new Unternehmer ();
    this.setDatenLieferant(new DatenlieferantImpl());
  }

  /**
   * Erzeugung eines entsprechendes XML Dokumentes
   * @return String
   */
  public String toNutzdatenXMLFragment() {
    this.plausiblisiere(); // Erst prüfen, ob Fehler beim Datenaufbau da sind
    StringBuilder xml = new StringBuilder();
    xml.append("<Nutzdaten>");// $NON-NLS-1$
    xml.append("<Anmeldungssteuern art='").append(ART).append("' version='").append(this.getSchnittstellenVersion()).append("'>");// $NON-NLS-1$// $NON-NLS-2$// $NON-NLS-3$
    xml.append("<DatenLieferant>");// $NON-NLS-1$
    xml.append("<Name>").append(this.getDatenLieferant().getName()).append("</Name>");// $NON-NLS-1$// $NON-NLS-2$
    xml.append("<Strasse>").append(this.getDatenLieferant().getStrasse()).append("</Strasse>");// $NON-NLS-1$// $NON-NLS-2$
    xml.append("<PLZ>").append(this.getDatenLieferant().getPLZ()).append("</PLZ>");// $NON-NLS-1$// $NON-NLS-2$
    xml.append("<Ort>").append(this.getDatenLieferant().getOrt()).append("</Ort>");// $NON-NLS-1$// $NON-NLS-2$
    xml.append((this.getDatenLieferant().getTelefon() != null && this.getDatenLieferant().getTelefon().trim().length() != 0) ? "<Telefon>"+this.getDatenLieferant().getTelefon()+"</Telefon>" : "");// $NON-NLS-1$// $NON-NLS-2$
    xml.append((this.getDatenLieferant().getEMail() != null && this.getDatenLieferant().getEMail().trim().length() != 0) ? "<Email>"+this.getDatenLieferant().getEMail()+"</Email>" : "");// $NON-NLS-1$// $NON-NLS-2$
    xml.append("</DatenLieferant>");// $NON-NLS-1$
    xml.append("<Erstellungsdatum>");// $NON-NLS-1$
    xml.append(new SimpleDateFormat ("yyyyMMdd").format(new Date ()));// $NON-NLS-1$
    xml.append("<Steuerfall>");// $NON-NLS-1$
    xml.append("<Umsatzsteuervoranmeldung>");// $NON-NLS-1$
    for (Object key : this.werte.keySet()) {
      xml.append('<').append(key).append('>').append(this.werte.get(key)).append("</").append(key).append('>');// $NON-NLS-1$
    }
    xml.append("</Umsatzsteuervoranmeldung>");// $NON-NLS-1$
    xml.append("</Steuerfall>");// $NON-NLS-1$
    xml.append("</Erstellungsdatum>");// $NON-NLS-1$
    xml.append("</Anmeldungssteuern>");// $NON-NLS-1$
    xml.append("</Nutzdaten>");// $NON-NLS-1$
    return xml.toString();
  }

  /**
   * Version der XML Struktur
   * @return String
   */
  public String getSchnittstellenVersion() {
    return "200701";// $NON-NLS-1$
  }
  /**
   * Der Datenlieferant
   * @return IDatenlieferant
   */
  public IDatenlieferant getDatenLieferant() {
    return this.datenLieferant;
  }

  /**
   * @deprecated
   * @return HashMap
   */
  public HashMap getSchluesselWertPaare() {
    return this.werte;
  }
  /**
   * Liefert einen einzelnen Wert zurück
   * @param id String
   * @return String
   */
  public String getWert(final String id) {
    return (String)this.werte.get(id);
  }
  /**
   * Setzt einen einzelnen Wert
   * @param id String
   * @param wert String
   */
  public void setWert (final String id, final String wert) {
    this.werte.put(id, wert);
  }
  /**
   * Der Datenlieferant
   * @param datenLieferant IDatenlieferant
   */
  public void setDatenLieferant(final IDatenlieferant datenLieferant) {
    this.datenLieferant = datenLieferant;
  }
  /**
   * Die Datenart UStVA
   * @return String
   */
  public String getDatenArt() {
    return "UStVA";// $NON-NLS-1$
  }
  /**
   * Der XML String
   * @return String
   */
  public String toXML () {
    /** @todo den Rest des XML Dokumentes ausgeben */
    return this.toNutzdatenXMLFragment();
  }
  /**
   * Der Datenlieferant
   * @return IDatenlieferant
   */
  public IDatenlieferant getDatenlieferant() {
    return this.datenLieferant;
  }
  /**
   * Der Mitwirkende
   * @return Mitwirkender
   */
  public Mitwirkender getMitwirkender() {
    return this.mitwirkender;
  }
  /**
   * Der Unternehmer
   * @return Unternehmer
   */
  public Unternehmer getUnternehmer() {
    return this.unternehmer;
  }
  /**
   * Plausibilitätsprüfungen
   */
  public void plausiblisiere() {
  }

  /**
   * Wert entfernen
   * @param id String
   */
  public void removeWert(String id) {
    this.werte.remove(id);
  }

  /**
   * Die Steuernummer der UStVA
   * @param stnrElsterFormat long
   */
  public void setSteuernummer(final long stnrElsterFormat) {
    this.stnrElsterFormat = stnrElsterFormat;
  }

  /**
   * Die Steuernummer der UStVA
   * @return long
   */
  public long getSteuernummer() {
    return this.stnrElsterFormat;
  }
}
