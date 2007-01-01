/**
 * EingabeFeldFactory
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.gui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import de.bastie.pica.bo.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class EingabeFeldFactory {

  /**
   * Erzeugt ein neues Eingabefeld
   *
   * @param id String
   * @param format char
   * @param min int
   * @param max int
   * @param eingabe String
   * @param art Steuerart
   * @return V
   */
  public static JComponent createEingabefeld (final IDatencontainer art, final String id, String format, int min, int max,String eingabe) {
    /** @todo Validator hinzuf�gen */
    /** @todo Minimale Gr��e bestimmen */
    JTextField eingabefeld = new JTextField();
    eingabefeld.setDocument (createDocument(format,min,max));
    eingabefeld.getDocument().addDocumentListener(new DocumentListener () {
      public void insertUpdate(DocumentEvent e) {
        handleNewValue(e);
      }
      public void removeUpdate(DocumentEvent e) {
        handleNewValue(e);
      }
      public void changedUpdate(DocumentEvent e) {
        handleNewValue(e);
      }

      private void handleNewValue (DocumentEvent e) {
        try {
          art.setWert(id, e.getDocument().getText(0, e.getDocument().getLength()));
        }
        catch (BadLocationException shit) {shit.printStackTrace();}
      }
    });
    return eingabefeld;
  }

  /**
   * Erzeugt ein neues Eingabefeld
   *
   * @param id String
   * @param format char
   * @param min int
   * @param max int
   * @param eingabe String
   * @param art Steuerart
   * @param eingabeWerteListe Liste der zul�ssigen Eingabewerte
   * @param pflichtFeld Information, ob es sich um ein Pflichtfeld handelt - abh�ngig hiervon ist der Wert auch l�schbar
   * @return V
   */
  public static JComponent createEingabefeld (final IDatencontainer art, final String id, String format, int min, int max,String eingabe, final ArrayList<String> eingabeWerteListe, final boolean pflichtFeld) {
    JComponent eingabefeld = null;
    if (eingabeWerteListe == null ) {
      eingabefeld = createEingabefeld(art,id,format,min,max,eingabe);
    }
    else {
      if (eingabeWerteListe.size() == 1) {
        final JCheckBox eingabeComponent = new JCheckBox(new String(),false);
        eingabeComponent.setBorder(BorderFactory.createEmptyBorder());
        eingabeComponent.addActionListener(new ActionListener () {
          public void actionPerformed(final ActionEvent event) {
            if (eingabeComponent.isSelected()) {
              art.setWert(id,eingabeWerteListe.get(0));
            }
            else {
              art.removeWert (id);
            }
          }
        });
        eingabefeld = eingabeComponent;
      }
      else {
        final String nichtGesetzt = "--- nicht gesetzt ---";
        if (!pflichtFeld) {
          eingabeWerteListe.add(0,nichtGesetzt);
        }
        final String [] werte = new String[eingabeWerteListe.size()];
        eingabeWerteListe.toArray(werte);
        final JComboBox auswahlComponent = new JComboBox(werte);
        auswahlComponent.addActionListener(new ActionListener () {
          public void actionPerformed(final ActionEvent event) {
            if (nichtGesetzt.equals(auswahlComponent.getSelectedItem())){
              art.removeWert(id);
            }
            else {
              art.setWert(id, auswahlComponent.getSelectedItem().toString());
            }
          }
        });
        eingabefeld = auswahlComponent;
        if (pflichtFeld) {
          art.setWert(id, auswahlComponent.getSelectedItem().toString());
        }
      }
    }
    return eingabefeld;
  }

  /**
   * Erzeugt ein Dokument f�r die Eingabe, welches bereits einige fehlerhafte
   * Eingaben abf�ngt:
   * <ul><li>Pr�fung auf das spezielle Format</li>
   * <li>Pr�fung ob h�chstens die maximale Anzahl der Zeichen eingegeben wurde</li></ul>
   * <br>Formate:</br>
   * <ul><li>A = alphanumerisch</li>
   * <li>D = Datum, wird gefolgt von dem Format (in englisch Buchstaben codiert z.B. Ddd.MM.jjjj)</li>
   * <li>F = numerische Dezimalzahl, wird gefolgt von der Anzahl der maximalen Zeichen vor dem Punkt + '.' + Anzahl der maximalen Zeichen nach dem Punkt</li>
   * <li>G = numerische Ganzzahl</li>
   * <li>g = numerische Ganzzahl (nur positiv und 0)</li>
   * <li>H = wie F, jedoch nur positive Werte und 0 bzw. 0.00 (Null) zul�ssig</li>
   * <li>h = wie F, jedoch nur negative Werte und 0 bzw. 0.00(Null) zul�ssig</li>
   * <li>I = wie F, jedoch nur positive Werte zul�ssig</li>
   * <li>i = wie F, jedoch nur negative Werte zul�ssig</li>
   * <li>M = Formatierter String im MaskFormatter Format, wird gefolgt von der Codierung</li>
   * <li>L = Auswahllister</li>
   * </ul>
   * @param eingabeformat String
   * @param minimaleAnzahl int
   * @param maximaleAnzahl int
   * @return Document
   */
  protected static Document createDocument (final String eingabeformat, final int minimaleAnzahl, final int maximaleAnzahl) {
    /** @todo weitere Formate aufnehmen */
    switch (eingabeformat.charAt(0)) {
    case 'G' : return new GanzzahlenBegrenztesPlainDocument(maximaleAnzahl);
    case 'A' : return new ZeichenBegrenztesPlainDocument(maximaleAnzahl);
    default  : return new PlainDocument();
    }
  }
}
