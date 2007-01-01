/**
 * UStVAMask
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.ref.gui.mask;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import de.bastie.pica.*;
import de.bastie.pica.bo.*;
import de.bastie.pica.gui.*;
import de.bastie.pica.ref.gui.*;

/**
 * Oberflächenerzeugung für die Umsatzsteuervoranmeldung
 *
 * @author © 2006, 2007 Bastie - Sebastian Ritter
 * @version 1.0
 */
public class UStVAMask {

  private HashMap<String, JComponent> eingabefeld;
  private final ISteuerart art;
  private final int jahr;
  private JComponent [] eingabeKomponenten;
  private Eingabefeldplan kennzahlenplan = new Eingabefeldplan();

  public UStVAMask (final ISteuerart art, final int jahr) {
    this.jahr = jahr;
    this.art = art;
    try{
      eingabefeld = kennzahlenplan.createDatenlieferantJComponent(this.art.getDatenlieferant());
      eingabefeld.putAll(kennzahlenplan.createMitwirkenderJComponent(this.art.getMitwirkender()));
      eingabefeld.putAll(kennzahlenplan.createJComponents(this.art, Integer.toString(jahr), true));
      eingabefeld.putAll(kennzahlenplan.createUnternehmerJComponent(this.art.getUnternehmer()));
    }
    catch (Exception shit) {
      throw new SteuerException (900000000,"Problem beim Laden der Eingabefelder", shit);
    }
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

    // Die Oberflächenelemente definieren
    /**@todo Ausgliedern */
    JComponent[][] bno = new JComponent[][] {
       {new JLabel("Verrechnung des Erstattungsbetrags erwünscht / Erstattungsbetrag ist abgetreten"), eingabefeld.get("Kz29"), null},
       {new JLabel("Einzugsermächtigung wird für diesen Voranmeldungszeitraum widerrufen"), eingabefeld.get("Kz26"), null},
       {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)},
       {new JLabel("Datenlieferer"), null, null}, {new JLabel("Name"), eingabefeld.get("Name"), null},
       {new JLabel("Strasse"), eingabefeld.get("Strasse"), null},
       {new JLabel("PLZ / Ort"), eingabefeld.get("PLZ"), eingabefeld.get("Ort")},
       {new JLabel("Telefon / eMail"), eingabefeld.get("Telefon"), eingabefeld.get("EMail")},
       {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)},
       {new JLabel("Mitwirkender"), null, null},
       {new JLabel("Name"), eingabefeld.get("MitwirkenderName"), null},
       {new JLabel("Beruf"), eingabefeld.get("MitwirkenderBeruf"), null},
       {new JLabel("Telefon"), eingabefeld.get("MitwirkenderVorwahl"), eingabefeld.get("MitwirkenderRufnummer")},
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

    return pane;
  }

  public JPanel getAllgemeineAngabenPanel () {
    JPanel pane = new JPanel(new GridBagLayout (),true);
    final JPanel parent = pane;

    // Die Oberflächenelemente definieren
    final JFormattedTextField stNrEingabe = new JFormattedTextField();
    final JFormattedTextField finanzamt = new JFormattedTextField();
    try {
      MaskFormatter stnrMask = new MaskFormatter("############");
      stnrMask.setPlaceholderCharacter('_');
      stNrEingabe.setFormatterFactory(new DefaultFormatterFactory (stnrMask));
      stNrEingabe.setInputVerifier(new SteuernummerEingabeVerifer ());
      finanzamt.setEnabled(false);
      stNrEingabe.addFocusListener(new FocusListener () {
        public void focusGained(FocusEvent event) {}
        public void focusLost(FocusEvent event) {
          try {
            Finanzamt fa = Finanzamt.getFinanzamt(Long.parseLong(stNrEingabe.getText().substring(0,4))* 100000000l);
            finanzamt.setText(fa.getName());
          }
          catch (final NumberFormatException keineBufa){}
        }
      });
    }
    catch (Exception ignored){ignored.printStackTrace();}
    /**@todo Ausgliedern, Anbinden ans Datenmodel... */
    JComponent[][] bno = new JComponent[][] {
                         {new JLabel("Steuernummer"), stNrEingabe, null},
                         {new JLabel("Finanzamt"), finanzamt, null},
                         {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)},
                         {new JLabel("Unternehmer"), null, null},
                         {new JLabel("Name"), eingabefeld.get("UnternehmerName"), null},
                         {new JLabel("Strasse"), eingabefeld.get("UnternehmerStrasse"), null},
                         {new JLabel("PLZ / Ort"), eingabefeld.get("UnternehmerPLZ"), eingabefeld.get("UnternehmerOrt")},
                         {new JLabel("Telefon / eMail"), eingabefeld.get("UnternehmerTelefon"), eingabefeld.get("UnternehmerEMail")},
                         {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)},
                         {new JLabel("Voranmeldungszeitraum"), eingabefeld.get("Zeitraum"), null},
                         {new JLabel("Berichtigte Anmeldung"), eingabefeld.get("Kz10"), null},
                         {new JLabel("Belege werden nachgereicht"), eingabefeld.get("Kz22"), null},
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

    return pane;
  }

  public JPanel getBesteuerungsgrundlagenPanel () throws SteuerException {
    JPanel pane = new JPanel (new GridBagLayout (), true);

    /**@todo Ausgliedern */
    // Die Oberflächenelemente definieren
    // Berechnungen
    BerechnungTextField stpflUmsaetze19Prozent = new BerechnungTextField (2);
    stpflUmsaetze19Prozent.setZeigeFehlerAn (false);
    stpflUmsaetze19Prozent.addOperand((JTextComponent)eingabefeld.get("Kz81"),'=');
    stpflUmsaetze19Prozent.addOperand(0.19,'*');

    BerechnungTextField stpflUmsaetze7Prozent = new BerechnungTextField (2);
    stpflUmsaetze7Prozent.setZeigeFehlerAn (false);
    stpflUmsaetze7Prozent.addOperand((JTextComponent)eingabefeld.get("Kz86"),'=');
    stpflUmsaetze7Prozent.addOperand(0.07,'*');

    BerechnungTextField stpflInnerUmsaetze19Prozent = new BerechnungTextField (2);
    stpflInnerUmsaetze19Prozent.setZeigeFehlerAn (false);
    stpflInnerUmsaetze19Prozent.addOperand((JTextComponent)eingabefeld.get("Kz89"),'=');
    stpflInnerUmsaetze19Prozent.addOperand(0.19,'*');

    BerechnungTextField stpflInnerUmsaetze7Prozent = new BerechnungTextField (2);
    stpflInnerUmsaetze7Prozent.setZeigeFehlerAn (false);
    stpflInnerUmsaetze7Prozent.addOperand((JTextComponent)eingabefeld.get("Kz93"),'=');
    stpflInnerUmsaetze7Prozent.addOperand(0.07,'*');

    BerechnungTextField umsatzsteuer = new BerechnungTextField(2);
    umsatzsteuer.setZeigeFehlerAn(false);
    umsatzsteuer.addOperand(stpflUmsaetze19Prozent,'=');
    umsatzsteuer.addOperand(stpflUmsaetze7Prozent,'+');
    umsatzsteuer.addOperand((JTextComponent)eingabefeld.get("Kz36"),'+');
    umsatzsteuer.addOperand((JTextComponent)eingabefeld.get("Kz80"),'+');
    umsatzsteuer.addOperand(stpflInnerUmsaetze19Prozent,'+');
    umsatzsteuer.addOperand(stpflInnerUmsaetze7Prozent,'+');
    umsatzsteuer.addOperand((JTextComponent)eingabefeld.get("Kz98"),'+');
    umsatzsteuer.addOperand((JTextComponent)eingabefeld.get("Kz96"),'+');
    umsatzsteuer.addOperand((JTextComponent)eingabefeld.get("Kz53"),'+');
    umsatzsteuer.addOperand((JTextComponent)eingabefeld.get("Kz74"),'+');
    umsatzsteuer.addOperand((JTextComponent)eingabefeld.get("Kz85"),'+');
    umsatzsteuer.addOperand((JTextComponent)eingabefeld.get("Kz65"),'+');

    BerechnungTextField uStMinusVoSt = new BerechnungTextField (2);
    uStMinusVoSt.setZeigeFehlerAn(false);
    uStMinusVoSt.gleich(umsatzsteuer);
    uStMinusVoSt.minus (eingabefeld.get("Kz66"));
    uStMinusVoSt.minus (eingabefeld.get("Kz61"));
    uStMinusVoSt.minus (eingabefeld.get("Kz62"));
    uStMinusVoSt.minus (eingabefeld.get("Kz67"));
    uStMinusVoSt.minus (eingabefeld.get("Kz63"));
    uStMinusVoSt.minus (eingabefeld.get("Kz64"));
    uStMinusVoSt.minus (eingabefeld.get("Kz59"));

    BerechnungTextField ueberschuss = new BerechnungTextField(2);
    ueberschuss.setZeigeFehlerAn(false);
    ueberschuss.gleich(uStMinusVoSt);
    ueberschuss.plus(eingabefeld.get("Kz69"));

    BerechnungTextField verbleibenerUeberschuss = new BerechnungTextField(2);
    verbleibenerUeberschuss.setZeigeFehlerAn(false);
    verbleibenerUeberschuss.setEditable(true);
    verbleibenerUeberschuss.setFocusable(true);
    verbleibenerUeberschuss.gleich(ueberschuss);
    verbleibenerUeberschuss.minus(eingabefeld.get("Kz39"));
    verbleibenerUeberschuss.getDocument().addDocumentListener(new DocumentListener () {
      public void insertUpdate(DocumentEvent event) {
        this.setWert(event);
      }
      public void removeUpdate(DocumentEvent event) {
        this.setWert(event);
      }
      public void changedUpdate(DocumentEvent event) {
        this.setWert(event);
      }
      public void setWert (DocumentEvent event) {
        try {
          ((AbstractDocument)event.getDocument()).readLock();
          art.setWert("Kz83", event.getDocument().getText(0, event.getDocument().getLength()));
        }
        catch (final BadLocationException shit) {
          shit.printStackTrace();
        }
        finally {
          ((AbstractDocument)event.getDocument()).readUnlock();
        }
      }
    });

    // Maskenbeschreibung
    JComponent[][] bno = new JComponent[][] {
                         {new JLabel("Lieferungen und sonstige Leistungen"), new JLabel("Bemessungsgrundlage"), new JLabel("Steuer")},
                         {new JLabel("(einschließlich unentgeldlicher Wertabgaben"), new JLabel("ohne Umsatzsteuer"), null},
                         {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)},
                         {new JLabel("Steuerfreie Umsätze mit Vorsteuerabzug"), new JLabel("in vollen Euro"), null},
                         {new JLabel("Innergemeinschaftliche Lieferungen (§4,1b UStG) an Abnehmer mit UStID"), eingabefeld.get("Kz41"), null},
                         {new JLabel("Innergemeinschaftliche Lieferungen von Fahrzeugen an Abnehmer ohne UStID"), eingabefeld.get("Kz44"), null},
                         {new JLabel("Innergemeinschaftliche Lieferungen von neuen Fahrzeugen außerhalb des Unternehmens (§2a UStG)"), eingabefeld.get("Kz49"), null},
                         {new JLabel("Weitere steuerfreie Umsätze mit Vorsteuerabzug"), eingabefeld.get("Kz43"), null},
                         {new JLabel("Steuerfrei Umsätze ohne Vorsteuerabzug §4,8-28 UStG"), eingabefeld.get("Kz48"), null},
                         {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)},
                         {new JLabel("Steuerflichtige Umsätze"), new JLabel("in vollen Euro"), new JLabel("in Euro und Cent")},
                         {new JLabel(" - zum Steuersatz von 19%"), eingabefeld.get("Kz81"), stpflUmsaetze19Prozent},
                         {new JLabel(" - zum Steuersatz von 7%"), eingabefeld.get("Kz86"), stpflUmsaetze7Prozent},
                         {new JLabel(" - die anderen Steuersätzen unterliegen"), eingabefeld.get("Kz35"), eingabefeld.get("Kz36")},
                         {new JSeparator(JSeparator.HORIZONTAL), null, null},
                         {new JLabel("Umsätze land- und forstwirtschaftlicher Betriebe nach §24 UStG"), null, null},
                         {new JLabel("Lieferungen in das übrige Gemeinschaftsgebiet an Abnehmer mit UStID"), eingabefeld.get("Kz77"), null},
                         {new JLabel("Umsätze für die eine Steuer nach §24 UStG zu entrichten ist"), eingabefeld.get("Kz76"), eingabefeld.get("Kz80")},
                         {new JSeparator(JSeparator.HORIZONTAL), null, null},
                         {new JLabel("Innergemeinschaftliche Erwerbe"), null, null},
                         {new JLabel("Steuerfreie innergemeinschaftliche Erwerbe nach §4b UStG"), eingabefeld.get("Kz91"), null},
                         {new JLabel("Steuerflichtige innergemeinschaftliche Erwerbe"), null, null},
                         {new JLabel(" - zum Steuersatz von 19%"), eingabefeld.get("Kz89"), stpflInnerUmsaetze19Prozent},
                         {new JLabel(" - zum Steuersatz von 7%"), eingabefeld.get("Kz93"), stpflInnerUmsaetze7Prozent},
                         {new JLabel(" - die anderen Steuersätzen unterliegen"), eingabefeld.get("Kz95"), eingabefeld.get("Kz98")},
                         {new JLabel(" - neuer Fahrzeuge von Lieferen ohne UStID zum allgemeinen Steuersatz"), eingabefeld.get("Kz94"), eingabefeld.get("Kz96")},
                         {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)},
                         {new JLabel("Ergänzende Angaben zu Umsätzen"), null, null},
                         {new JLabel("Lieferungen des ersten Abnehmers bei innergemeinschaftlichen Dreiecksgeschäften (§25b(2) UStG)"), eingabefeld.get("Kz42"), null},
                         {new JLabel("Steuerpfl. Umsätze i.S.d. §13(1),1 S.1-5 UStG für die der Leistungsempfänger die Steuer schuldet"), eingabefeld.get("Kz60"), null},
                         {new JLabel("Nicht steuerbare Umsätze (Leistungsort nicht im Inland)"), eingabefeld.get("Kz45"), null},
                         {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)},
                         {new JLabel("Umsätze, für die als Leistungsempfänger die Steuer nach §13b(2) UStG geschuldet werden"), null, null},
                         {new JLabel("Leistungen eines im Ausland ansässigen Unternehmers (§13b(1),S.1 Nr.1+5 UStG)"), eingabefeld.get("Kz52"), eingabefeld.get("Kz53")},
                         {new JLabel("Lieferungen sicherungsübereigneter Gegenstände und Umsätze die unter das GrEStG fallen (§13b(1),S1 Nr. 1+2 UStG)"), eingabefeld.get("Kz73"), eingabefeld.get("Kz74")},
                         {new JLabel("Bauleistungen eines im Ansatz im Inland ansässigen Unternehmers (§13b(1),S.1 Nr.4 UStG)"), eingabefeld.get("Kz84"), eingabefeld.get("Kz85")},
                         {new JLabel("Steuer infolge Wechsel der Besteuerungsform sowie Nachsteuer auf versteuerte Anzahlungen wegen Steuersatzerhöhung"), null, eingabefeld.get("Kz65")},
                         {new JLabel("Umsatzsteuer"), null, umsatzsteuer},
                         {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)},
                         {new JLabel("Abziehbare Vorsteuerbeträge"), null, null},
                         {new JLabel(" - nach §15(1),S.1 Nr.1 UStG, §15(1),S.1 Nr.5 UStG, §25(5) UStG"), null, eingabefeld.get("Kz66")},
                         {new JLabel(" - aus innergemeinschaftlichen Erwerb von Gegenständen §15(1),S.1 Nr.3 UStG"), null, eingabefeld.get("Kz61")},
                         {new JLabel("Entrichtete Einfuhrumsatzsteuer §15(1),S.1 Nr.2 UStG"), null, eingabefeld.get("Kz62")},
                         {new JLabel(" - aus Leistungen i.S.d. §13b(1) UStG (§15(1),S.1 Nr.4 UStG)"), null, eingabefeld.get("Kz67")},
                         {new JLabel(" - die nach allgemeinen Durchschnittsätzen ermittelt sind §§23, 23a UStG"), null, eingabefeld.get("Kz63")},
                         {new JLabel("Berichtigung des Vorsteuerabzugs nach §15a UStG"), null, eingabefeld.get("Kz64")},
                         {new JLabel("Vorsteuerabzug nach §15(4a) UStG"), null, eingabefeld.get("Kz59")},
                         {null, null, new JSeparator(JSeparator.HORIZONTAL)},
                         {new JLabel("Verbleibender Betrag"), null, uStMinusVoSt},
                         {new JSeparator(JSeparator.HORIZONTAL), null, null},
                         {new JLabel("Andere Steuerbeträge"), null, eingabefeld.get("Kz69")},
                         {new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL), new JSeparator(JSeparator.HORIZONTAL)},
                         {new JLabel("Umsatzsteuer-Vorauszahlungen / Überschuss"), null, ueberschuss},
                         {new JLabel("Anrechnung der Sondervorauszahlung für Dauerfristverlängerung"), null, eingabefeld.get("Kz39")},
                         {null, null, new JSeparator()},
                         {new JLabel("Verbleibene Umsatzsteuer-Vorauszahlung / Überschuss"), null, verbleibenerUeberschuss},
                         {null, null, new JSeparator()},
                         {null, null, new JSeparator()},
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

    return pane;
  }


  public String toString() {
    return this.getKurzTitel(); // Für die Anzeige im Baum
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
