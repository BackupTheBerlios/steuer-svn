package de.bastie.pica.ref.gui.mask;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import de.bastie.pica.*;
import de.bastie.pica.bo.*;

public class UStVAMask {

  private HashMap<String, JComponent> eingabefeld;
  private final ISteuerart art;
  private final int jahr;
  private Eingabefeldplan kennzahlenplan = new Eingabefeldplan();
  public UStVAMask (final ISteuerart art, final int jahr) {
    this.jahr = jahr;
    this.art = art;
  }

  public String getTitel () {
    return "Umsatzsteuervoranmeldung";
  }
  public String getKurzTitel () {
    return "UStVA";
  }

  public String [] getEingabeKomponentenTitel () {
    return new String[] {
        "Allgemeine Angaben",
        "Besteuerungsgrundlagen",
        "Weitere Angaben"
    };
  }
  private JComponent [] eingabeKomponenten;
  public JComponent [] getEingabeKomponenten () {
    if (this.eingabeKomponenten == null) {
      eingabeKomponenten = new JComponent[] {
                          this.getAllgemeineAngabenPanel(),
                          this.getBesteuerungsgrundlagenPanel(),
                          this.getWeitereAngabenPanel()
      };
    }
    return this.eingabeKomponenten;
  }

  public JPanel getWeitereAngabenPanel () throws SteuerException {
    JPanel pane = new JPanel(new GridBagLayout(), true);
    try {
      eingabefeld = kennzahlenplan.createDatenlieferantJComponent(this.art.getDatenlieferant());
      // Die Oberflächenelemente definieren
      /**@todo Ausgliedern, Anbinden ans Datenmodel... */
      JComponent[][] bno = new JComponent[][] {
         {new JLabel("Verrechnung des Erstattungsbetrags erwünscht / Erstattungsbetrag ist abgetreten"), new JCheckBox("", false), null},
         {new JLabel("Einzugsermächtigung wird für diesen Voranmeldungszeitraum widerrufen"), new JCheckBox("", false), null},
         {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)},
         {new JLabel("Datenlieferer"), null, null}, {new JLabel("Name"), eingabefeld.get("Name"), null},
         {new JLabel("Strasse"), eingabefeld.get("Strasse"), null},
         {new JLabel("PLZ / Ort"), eingabefeld.get("PLZ"), eingabefeld.get("Ort")},
         {new JLabel("Telefon / eMail"), eingabefeld.get("Telefon"), eingabefeld.get("EMail")},
         {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)},
         {new JLabel("Mitwirkender"), null, null},
         {new JLabel("Name"), new JFormattedTextField("Mitwirkendername"), null},
         {new JLabel("Beruf"), new JFormattedTextField("Mitwirkenderberuf"), null},
         {new JLabel("Telefon"), new JFormattedTextField("TelefonVorwahl"), new JFormattedTextField("TelefonApparat")},
      };

      // Die Oberflächenelemente ausrichten
      int x = 0;
      int y = 0;
      GridBagConstraints gbc = new GridBagConstraints(x, y, 1, 1, 1d, 0d, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 5), 0, 0);
      for (JComponent[] zeile : bno) {
        for (JComponent elem : zeile) {
          gbc = new GridBagConstraints(x, y, 1, 1, 1d, 0d, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 5), 0, 0);
          if (elem != null) {
            if (elem instanceof JSeparator) { // Separatoren verbinden, aber Abstand links und rechts behalten
              gbc.insets = new Insets(0, x == 0 ? gbc.insets.left : 0, 0, x == zeile.length - 1 ? gbc.insets.right : 0);
            }
            pane.add(elem, gbc);
          }
          x++;
        }
        x = 0;
        y++;
      }
      gbc.weighty = 1d;
      gbc.fill = gbc.VERTICAL;
      pane.add(new JLabel(), gbc);
    }
    catch (final Exception shit) {
      throw new SteuerException (900000000,"Problem beim Laden der Eingabefelder", shit);
    }
    finally {
      return pane;
    }
  }

  public JPanel getAllgemeineAngabenPanel () {
    JPanel pane = new JPanel(new GridBagLayout (),true);
    // Die Oberflächenelemente definieren
    /**@todo Ausgliedern, Anbinden ans Datenmodel... */
    JComponent [] [] bno = new JComponent [] [] {
      {new JLabel("Steuernummer"), new JFormattedTextField("Steuernummer"), null},
      {new JLabel("Finanzamt"), new JFormattedTextField("Finanzamt"), null},
      {new JSeparator(JSeparator.HORIZONTAL),new JSeparator(JSeparator.HORIZONTAL),new JSeparator(JSeparator.HORIZONTAL)},
      {new JLabel("Unternehmer"), null, null},
      {new JLabel("Name"), new JFormattedTextField("Unternehmername"), null},
      {new JLabel("Strasse"), new JFormattedTextField("Unternehmerstrasse"), null},
      {new JLabel("PLZ / Ort"), new JFormattedTextField("PLZ"), new JFormattedTextField("Ort")},
      {new JLabel("Telefon / eMail"), new JFormattedTextField("Telefon"), new JFormattedTextField("eMail")},
      {new JSeparator(JSeparator.HORIZONTAL),new JSeparator(JSeparator.HORIZONTAL),new JSeparator(JSeparator.HORIZONTAL)},
      {new JLabel("Voranmeldungszeitraum"), new JComboBox(new String []{"Zeitraum"}), null},
      {new JLabel("Berichtigte Anmeldung"), new JCheckBox("",false), null},
      {new JLabel("Belege werden nachgereicht"), new JCheckBox("",false), null},
    };

    // Die Oberflächenelemente ausrichten
    int x = 0;
    int y = 0;
    GridBagConstraints gbc = new GridBagConstraints(x,y,1,1,1d,0d,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2,5,3,5),0,0);
    for (JComponent [] zeile : bno) {
      for (JComponent elem : zeile) {
        gbc = new GridBagConstraints(x,y,1,1,1d,0d,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2,5,3,5),0,0);
        if (elem != null) {
          if (elem instanceof JSeparator) { // Separatoren verbinden, aber Abstand links und rechts behalten
            gbc.insets = new Insets(0,x == 0 ? gbc.insets.left:0,0,x==zeile.length-1 ? gbc.insets.right:0);
          }
          pane.add (elem, gbc);
        }
        x++;
      }
      x = 0;
      y++;
    }
    gbc.weighty = 1d;
    gbc.fill = gbc.VERTICAL;
    pane.add(new JLabel (),gbc);

    return pane;
  }

  public JPanel getBesteuerungsgrundlagenPanel () throws SteuerException {
    JPanel pane = new JPanel (new GridBagLayout (), true);
    try {
      eingabefeld = kennzahlenplan.createJComponents(art, Integer.toString(jahr), true);
      // Die Oberflächenelemente definieren
      /**@todo Ausgliedern, Anbinden ans Datenmodel... */
      JComponent[][] bno = new JComponent[][] {
                           {new JLabel("Lieferungen und sonstige Leistungen"), new JLabel("Bemessungsgrundlage"), new JLabel("Steuer")}, {new JLabel("(einschließlich unentgeldlicher Wertabgaben"), new JLabel("ohne Umsatzsteuer"), null}, {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)}, {new JLabel("Steuerfreie Umsätze mit Vorsteuerabzug"), new JLabel("in vollen Euro"), null}, {new JLabel("Innergemeinschaftliche Lieferungen (§4,1b UStG) an Abnehmer mit UStID"), eingabefeld.get("Kz41"), null}, {new JLabel("Innergemeinschaftliche Lieferungen von Fahrzeugen an Abnehmer ohne UStID"), eingabefeld.get("Kz44"), null}, {new JLabel("Innergemeinschaftliche Lieferungen von neuen Fahrzeugen außerhalb des Unternehmens (§2a UStG)"), // new JFormattedTextField("Kz41"),null},
                           eingabefeld.get("Kz49"), null}, {new JLabel("Weitere steuerfreie Umsätze mit Vorsteuerabzug"), eingabefeld.get("Kz43"), null}, {new JLabel("Steuerfrei Umsätze ohne Vorsteuerabzug §4,8-28 UStG"), eingabefeld.get("Kz48"), null}, {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)}, {new JLabel("Steuerflichtige Umsätze"), new JLabel("in vollen Euro"), new JLabel("in Euro und Cent")}, {new JLabel(" - zum Steuersatz von 19%"), eingabefeld.get("Kz81"), new JFormattedTextField("--steuer--")}, {new JLabel(" - zum Steuersatz von 7%"), eingabefeld.get("Kz86"), new JFormattedTextField("--steuer--")}, {new JLabel(" - die anderen Steuersätzen unterliegen"), eingabefeld.get("Kz35"), eingabefeld.get("Kz36")},
                           {new JSeparator(JSeparator.HORIZONTAL), null, null}, {new JLabel("Umsätze land- und forstwirtschaftlicher Betriebe nach §24 UStG"), null, null}, {new JLabel("Lieferungen in das übrige Gemeinschaftsgebiet an Abnehmer mit UStID"), eingabefeld.get("Kz77"), null}, {new JLabel("Umsätze für die eine Steuer nach §24 UStG zu entrichten ist"), eingabefeld.get("Kz76"), eingabefeld.get("Kz80")}, {new JSeparator(JSeparator.HORIZONTAL), null, null}, {new JLabel("Innergemeinschaftliche Erwerbe"), null, null}, {new JLabel("Steuerfreie innergemeinschaftliche Erwerbe nach §4b UStG"), eingabefeld.get("Kz91"), null}, {new JLabel("Steuerflichtige innergemeinschaftliche Erwerbe"), null, null}, {new JLabel(" - zum Steuersatz von 19%"), eingabefeld.get("Kz89"),
                           new JFormattedTextField("--steuer--")}, {new JLabel(" - zum Steuersatz von 7%"), eingabefeld.get("Kz93"), new JFormattedTextField("--steuer--")}, {new JLabel(" - die anderen Steuersätzen unterliegen"), eingabefeld.get("Kz95"), eingabefeld.get("Kz98")}, {new JLabel(" - neuer Fahrzeuge von Lieferen ohne UStID zum allgemeinen Steuersatz"), eingabefeld.get("Kz94"), eingabefeld.get("Kz96")}, {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)}, {new JLabel("Ergänzende Angaben zu Umsätzen"), null, null}, {new JLabel("Lieferungen des ersten Abnehmers bei innergemeinschaftlichen Dreiecksgeschäften (§25b(2) UStG)"), eingabefeld.get("Kz42"), null},
                           {new JLabel("Steuerpfl. Umsätze i.S.d. §13(1),1 S.1-5 UStG für die der Leistungsempfänger die Steuer schuldet"), eingabefeld.get("Kz60"), null}, {new JLabel("Nicht steuerbare Umsätze (Leistungsort nicht im Inland)"), eingabefeld.get("Kz45"), null}, {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)}, {new JLabel("Umsätze, für die als Leistungsempfänger die Steuer nach §13b(2) UStG geschuldet werden"), null, null}, {new JLabel("Leistungen eines im Ausland ansässigen Unternehmers (§13b(1),S.1 Nr.1+5 UStG)"), eingabefeld.get("Kz52"), eingabefeld.get("Kz53")}, {new JLabel("Lieferungen sicherungsübereigneter Gegenstände und Umsätze die unter das GrEStG fallen (§13b(1),S1 Nr. 1+2 UStG)"),
                           eingabefeld.get("Kz73"), eingabefeld.get("Kz74")}, {new JLabel("Bauleistungen eines im Ansatz im Inland ansässigen Unternehmers (§13b(1),S.1 Nr.4 UStG)"), eingabefeld.get("Kz84"), eingabefeld.get("Kz85")}, {new JLabel("Steuer infolge Wechsel der Besteuerungsform sowie Nachsteuer auf versteuerte Anzahlungen wegen Steuersatzerhöhung"), null, eingabefeld.get("Kz65")}, {new JLabel("Umsatzsteuer"), null, new JFormattedTextField("--steuer--")}, {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)}, {new JLabel("Abziehbare Vorsteuerbeträge"), null, null}, {new JLabel(" - nach §15(1),S.1 Nr.1 UStG, §15(1),S.1 Nr.5 UStG, §25(5) UStG"), null, eingabefeld.get("Kz66")},
                           {new JLabel(" - aus innergemeinschaftlichen Erwerb von Gegenständen §15(1),S.1 Nr.3 UStG"), null, eingabefeld.get("Kz61")}, {new JLabel("Entrichtete Einfuhrumsatzsteuer §15(1),S.1 Nr.2 UStG"), null, eingabefeld.get("Kz62")}, {new JLabel(" - aus Leistungen i.S.d. §13b(1) UStG (§15(1),S.1 Nr.4 UStG)"), null, eingabefeld.get("Kz67")}, {new JLabel(" - die nach allgemeinen Durchschnittsätzen ermittelt sind §§23, 23a UStG"), null, eingabefeld.get("Kz63")}, {new JLabel("Berichtigung des Vorsteuerabzugs nach §15a UStG"), null, eingabefeld.get("Kz64")}, {new JLabel("Vorsteuerabzug nach §15(4a) UStG"), null, eingabefeld.get("Kz59")}, {null, null, new JFormattedTextField("--summe--")}, {new JSeparator(JSeparator.HORIZONTAL), null, null}, {new JLabel("Andere Steuerbeträge"), null,
                           eingabefeld.get("Kz69")}, {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)}, {new JLabel("Umsatzsteuer-Vorauszahlungen / Überschuss"), null, new JFormattedTextField("--summe--")}, {new JLabel("Anrechnung der Sondervorauszahlung für Dauerfristverlängerung"), null, eingabefeld.get("Kz39")}, {null, null, new JSeparator()}, {new JLabel("Verbleibene Umsatzsteuer-Vorauszahlung / Überschuss"), null, eingabefeld.get("Kz83")}, {null, null, new JSeparator()}, {null, null, new JSeparator()},
      };

      // Die Oberflächenelemente ausrichten
      int x = 0;
      int y = 0;
      GridBagConstraints gbc = new GridBagConstraints(x, y, 1, 1, 1d, 0d, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 5), 0, 0);
      for (JComponent[] zeile : bno) {
        for (JComponent elem : zeile) {
          gbc = new GridBagConstraints(x, y, 1, 1, 1d, 0d, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 5), 0, 0);
          if (elem != null) {
            if (elem instanceof JSeparator) { // Separatoren verbinden, aber Abstand links und rechts behalten
              gbc.insets = new Insets(0, x == 0 ? gbc.insets.left : 0, 0, x == zeile.length - 1 ? gbc.insets.right : 0);
            }
            pane.add(elem, gbc);
          }
          x++;
        }
        x = 0;
        y++;
      }
      gbc.weighty = 1d;
      gbc.fill = gbc.VERTICAL;
      pane.add(new JLabel(), gbc);

    }
    catch (final Exception shit) {
      throw new SteuerException (900000000,"Problem beim Laden der Eingabefelder", shit);
    }
    finally {
      return pane;
    }
  }


  public String toString() {
    return this.getKurzTitel();
  }


  public JComponent getComponentByTitle (final String title) {
    int index = -1;
    for (int i = 0; i < this.getEingabeKomponentenTitel().length; i++) {
      if (title.equals(this.getEingabeKomponentenTitel()[i])) {
        index = i;
        i = this.getEingabeKomponentenTitel().length+1;
      }
    }
    return (index == -1) ? new JComponent(){} : this.getEingabeKomponenten()[index];
  }
}
