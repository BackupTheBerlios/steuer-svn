package de.bastie.pica.bo.ustva;

import de.bastie.pica.bo.IDatenlieferant;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class UmsatzsteuervoranmeldungImpl implements Umsatzsteuervoranmeldung {

  private HashMap werte = new HashMap();
  private IDatenlieferant datenLieferant;

  public String toNutzdatenXMLFragment() {
    this.validate(); // Erst prüfen, ob Fehler beim Datenaufbau da sind
    StringBuilder xml = new StringBuilder();
    xml.append("<Nutzdaten>");
    xml.append("<Anmeldungssteuern art='").append(ART).append("' version='").append(this.getVersion()).append("'>");
    xml.append("<DatenLieferant>");
    xml.append("<Name>").append(this.getDatenLieferant().getName()).append("</Name>");
    xml.append("<Strasse>").append(this.getDatenLieferant().getStrasse()).append("</Strasse>");
    xml.append("<PLZ>").append(this.getDatenLieferant().getPLZ()).append("</PLZ>");
    xml.append("<Ort>").append(this.getDatenLieferant().getOrt()).append("</Ort>");
    xml.append((this.getDatenLieferant().getTelefon() != null && this.getDatenLieferant().getTelefon().trim().length() != 0) ? "<Telefon>"+this.getDatenLieferant().getTelefon()+"</Telefon>" : "");
    xml.append((this.getDatenLieferant().getEMail() != null && this.getDatenLieferant().getEMail().trim().length() != 0) ? "<Email>"+this.getDatenLieferant().getEMail()+"</Email>" : "");
    xml.append("</DatenLieferant>");
    xml.append("<Erstellungsdatum>");
    xml.append(new SimpleDateFormat ("yyyyMMdd").format(new Date ()));
    xml.append("<Steuerfall>");
    xml.append("<Umsatzsteuervoranmeldung>");
    for (Object key : this.werte.keySet()) {
      xml.append('<').append(key).append('>').append(this.werte.get(key)).append("</").append(key).append('>');
    }
    xml.append("</Umsatzsteuervoranmeldung>");
    xml.append("</Steuerfall>");
    xml.append("</Erstellungsdatum>");
    xml.append("</Anmeldungssteuern>");
    xml.append("</Nutzdaten>");
    return xml.toString();
  }

  public void validate () {

  }

  public String getVersion() {
    return "200701";
  }

  public IDatenlieferant getDatenLieferant() {
    return this.datenLieferant;
  }

  public HashMap getSchluesselWertPaare() {
    return this.werte;
  }

  public String getWert(final String id) {
    return (String)this.werte.get(id);
  }

  public void setWert (final String id, final String wert) {
    this.werte.put(id, wert);
  }

  public void setDatenLieferant(final IDatenlieferant datenLieferant) {
    this.datenLieferant = datenLieferant;
  }

  public String getDatenArt() {
    return "UStVA";
  }

  public String toXML () {
    /** @todo den Rest des XML Dokumentes ausgeben */
    return this.toNutzdatenXMLFragment();
  }

  public IDatenlieferant getDatenlieferant() {
    return this.datenLieferant;
  }
}
