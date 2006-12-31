/**
 * Umsatzsteuervoranmeldung
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.bo.ustva;

import java.util.HashMap;

import de.bastie.pica.bo.IDatenlieferant;
import de.bastie.pica.bo.ISteuerart;

public interface Umsatzsteuervoranmeldung extends ISteuerart {
  public IDatenlieferant getDatenLieferant();
  public final String ART = "UStVA";
  public String toNutzdatenXMLFragment ();
  /**
   * Liefert die Schnittstellenversion
   * @return String
   */
  public String getSchnittstellenVersion ();
  /**
   * Prüft die Daten der Umsatzsteuervoranmeldung
   */
  public void plausiblisiere ();

  /**
   * Liefert den Wert zur ID zurück
   * @param id String
   * @return String
   */
  public String getWert (final String id);
  /**
   * Liefert alle Schlüssel-Wert Paare
   * @return HashMap
   */
  public HashMap getSchluesselWertPaare ();

  public void setWert (final String id, final String wert);

  public final String SCHLUESSEL_Jahr ="Jahr";
  public final String SCHLUESSEL_Zeitraum = "Zeitraum";
  public final String SCHLUESSEL_Steuernummer = "Steuernummer";
  public final String SCHLUESSEL_Kz09 = "Kz09";
  public final String SCHLUESSEL_Kz10 = "Kz10";
  public final String SCHLUESSEL_Kz22 = "Kz22";
  public final String SCHLUESSEL_Kz26 = "Kz26";
  public final String SCHLUESSEL_Kz29 = "Kz29";
  public final String SCHLUESSEL_Kz35 = "Kz35";
  public final String SCHLUESSEL_Kz36 = "Kz36";
  public final String SCHLUESSEL_Kz39 = "Kz39";
  public final String SCHLUESSEL_Kz41 = "Kz41";
  public final String SCHLUESSEL_Kz42 = "Kz42";
  public final String SCHLUESSEL_Kz43 = "Kz43";
  public final String SCHLUESSEL_Kz44 = "Kz44";
  public final String SCHLUESSEL_Kz45 = "Kz45";
  public final String SCHLUESSEL_Kz48 = "Kz48";
  public final String SCHLUESSEL_Kz49 = "Kz49";
  public final String SCHLUESSEL_Kz52 = "Kz52";
  public final String SCHLUESSEL_Kz53 = "Kz53";
  public final String SCHLUESSEL_Kz59 = "Kz59";
  public final String SCHLUESSEL_Kz60 = "Kz60";
  public final String SCHLUESSEL_Kz61 = "Kz61";
  public final String SCHLUESSEL_Kz62 = "Kz62";
  public final String SCHLUESSEL_Kz63 = "Kz63";
  public final String SCHLUESSEL_Kz64 = "Kz64";
  public final String SCHLUESSEL_Kz65 = "Kz65";
  public final String SCHLUESSEL_Kz66 = "Kz66";
  public final String SCHLUESSEL_Kz67 = "Kz67";
  public final String SCHLUESSEL_Kz69 = "Kz69";
  public final String SCHLUESSEL_Kz73 = "Kz73";
  public final String SCHLUESSEL_Kz74 = "Kz74";
  public final String SCHLUESSEL_Kz76 = "Kz76";
  public final String SCHLUESSEL_Kz77 = "Kz77";
  public final String SCHLUESSEL_Kz80 = "Kz80";
  public final String SCHLUESSEL_Kz81 = "Kz81";
  public final String SCHLUESSEL_Kz83 = "Kz83";
  public final String SCHLUESSEL_Kz84 = "Kz84";
  public final String SCHLUESSEL_Kz85 = "Kz85";
  public final String SCHLUESSEL_Kz86 = "Kz86";
  public final String SCHLUESSEL_Kz89 = "Kz89";
  public final String SCHLUESSEL_Kz91 = "Kz91";
  public final String SCHLUESSEL_Kz93 = "Kz93";
  public final String SCHLUESSEL_Kz94 = "Kz94";
  public final String SCHLUESSEL_Kz95 = "Kz95";
  public final String SCHLUESSEL_Kz96 = "Kz96";
  public final String SCHLUESSEL_Kz98 = "Kz98";

  public void setDatenLieferant(IDatenlieferant datenLieferant);

}
