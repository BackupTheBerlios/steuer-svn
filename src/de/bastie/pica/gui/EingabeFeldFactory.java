package de.bastie.pica.gui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import de.bastie.pica.bo.*;

public class EingabeFeldFactory {

  /**
   * createEingabefeld
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
    /** @todo Validator hinzufügen */
    /** @todo Minimale Größe bestimmen */
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
   * Erzeugt ein Dokument für die Eingabe, welches bereits einige fehlerhafte
   * Eingaben abfängt:
   * <ul><li>Prüfung auf das spezielle Format</li>
   * <li>Prüfung ob höchstens die maximale Anzahl der Zeichen eingegeben wurde</li></ul>
   * <br>Formate:</br>
   * <ul><li>A = alphanumerisch</li>
   * <li>D = Datum, wird gefolgt von dem Format (in englisch Buchstaben codiert z.B. Ddd.MM.jjjj)</li>
   * <li>F = numerische Dezimalzahl, wird gefolgt von der Anzahl der maximalen Zeichen vor dem Punkt + '.' + Anzahl der maximalen Zeichen nach dem Punkt</li>
   * <li>G = numerische Ganzzahl</li>
   * <li>g = numerische Ganzzahl (nur positiv und 0)</li>
   * <li>H = wie F, jedoch nur positive Werte und 0 bzw. 0.00 (Null) zulässig</li>
   * <li>h = wie F, jedoch nur negative Werte und 0 bzw. 0.00(Null) zulässig</li>
   * <li>I = wie F, jedoch nur positive Werte zulässig</li>
   * <li>i = wie F, jedoch nur negative Werte zulässig</li>
   * <li>M = Formatierter String im MaskFormatter Format, wird gefolgt von der Codierung</li>
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
