package de.bastie.pica.ref.gui;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public final class BerechnungTextField extends JTextField implements DocumentListener {

  /**
   * Leere Felder werden bei der Berechnung nicht berücksichtigt
   */
  private boolean ignoriereWhitespaceEingabefelder = true;
  /**
   * Anzeige falls nicht berechenbar (z.B. Berechnungsschritt mit
   * Nichtnummerischen Wert)
   */
  private boolean zeigeFehlerAn = true;
  /**
   * Anzahl der anzuzeigenden Nachkommastellen
   */
  private int anzahlDerNachkommastellen = 2;
  /**
   * Die einzelnen Berechnungsschritte
   */
  final private LinkedHashMap<JTextComponent,Character> wertOperator;
  /**
   * Erzeugt ein Darstellungsfeld für Berechnungen, welches anhand der
   * der Operatoren mit den Eingabefeldern das Ergebnis berechnet.
   * @param wertOperator HashMap
   */
  public BerechnungTextField (final LinkedHashMap<JTextComponent,Character> wertOperator) {
    this.wertOperator = wertOperator;
    for (JTextComponent wert : this.wertOperator.keySet()) {
      wert.getDocument().addDocumentListener(this);
    }
    this.setHorizontalAlignment(SwingConstants.RIGHT);
    this.setEditable(false);
    this.setFocusable(false);
  }

  /**
   * Erzeugt ein Darstellungsfeld für Berechnungen, welches anhand der
   * der Operatoren mit den Eingabefeldern das Ergebnis berechnet.
   */
  public BerechnungTextField () {
    this.wertOperator = new LinkedHashMap<JTextComponent,Character>();
    this.setHorizontalAlignment(SwingConstants.RIGHT);
    this.setEditable(false);
    this.setFocusable(false);
  }

  /**
   * Erzeugt ein Darstellungsfeld für Berechnungen, welches anhand der
   * der Operatoren mit den Eingabefeldern das Ergebnis berechnet.
   * @param anzahlDerNachkommastellen Anzahl der darzustellenden Nachkommastellen
   */
  public BerechnungTextField (final int anzahlDerNachkommastellen) {
    this ();
    this.setAnzahlDerNachkommastellen(anzahlDerNachkommastellen);
  }

  /**
   * Fügt einen neuen Berechnunsschritt hinzu
   * @param wert JTextComponent
   * @param operand String
   */
  public void addOperand (final JTextComponent wert, final char operand) {
    wert.getDocument().addDocumentListener(this);
    this.wertOperator.put(wert,operand);
  }
  /**
   * Hinzufügen des Setzen (gleich) Operandes mit dem Inhalt übergebenen JTextComponent
   * @param jTextComponent JComponent
   * @throws java.lang.IllegalArgumentException nicht Auswertbare JComponent übergeben
   */
  public void gleich (final JComponent jTextComponent) throws IllegalArgumentException {
    if (!(jTextComponent instanceof JTextComponent)) {
      throw new IllegalArgumentException ("Instanz einer JTextComponent erwartet");
    }
    this.addOperand((JTextComponent) jTextComponent,'=');
  }
  /**
   * Addition des Operandes mit dem Inhalt übergebenen JTextComponent
   * @param jTextComponent JComponent
   * @throws java.lang.IllegalArgumentException nicht Auswertbare JComponent übergeben
   */
  public void plus (final JComponent jTextComponent) throws IllegalArgumentException {
    if (!(jTextComponent instanceof JTextComponent)) {
      throw new IllegalArgumentException ("Instanz einer JTextComponent erwartet");
    }
    this.addOperand((JTextComponent) jTextComponent,'+');
  }
  /**
   * Subtraktion des Operandes mit dem Inhalt übergebenen JTextComponent
   * @param jTextComponent JComponent
   * @throws java.lang.IllegalArgumentException nicht Auswertbare JComponent übergeben
   */
  public void minus (final JComponent jTextComponent) throws IllegalArgumentException {
    if (!(jTextComponent instanceof JTextComponent)) {
      throw new IllegalArgumentException ("Instanz einer JTextComponent erwartet");
    }
    this.addOperand((JTextComponent) jTextComponent,'-');
  }


  /**
   * Fügt einen neuen konstanten Berechnunsschritt hinzu
   * @param constant Konstante
   * @param operand String
   */
  public void addOperand (final double constant, final char operand) {
    final JTextField wert = new JTextField(Double.toString(constant));
    this.wertOperator.put(wert,operand);
  }

  /**
   * Berechnet den neuen Wert
   * @todo % Rechnung
   * @todo Klammern
   * @todo Punkt vor Strich
   * @todo Modulo
   */
  private void berechneNeuenWert () {
    try {
      double ergebnis = 0d;
      for (JTextComponent wert : this.wertOperator.keySet()) {
        if (this.ignoriereWhitespaceEingabefelder && wert.getText().trim().length() == 0) {
          continue;
        }
        final char operator = this.wertOperator.get(wert);
        final double operand = Double.parseDouble(wert.getDocument().getText(0, wert.getDocument().getLength()).trim());
        switch (operator) {
          case '*':
            ergebnis *= operand;
            break;
          case '/':
            ergebnis /= operand;
            break;
          case '-':
            ergebnis -= operand;
            break;
          case '+':
            ergebnis += operand;
            break;
          case '=':
            ergebnis = operand;
            break;
        }
      }
      String text = Double.toString(ergebnis);
      if (this.anzahlDerNachkommastellen <= 0 && text.indexOf('.') != -1) {
        text = text.substring(0,text.indexOf('.'));
      }
      else {
        // Kommastellen hinzufügen
        if (text.indexOf('.') == -1) {
          text += '.';
        }
        // ggf. mit Nullen hinten auffüllen
        while (text.indexOf('.') + anzahlDerNachkommastellen > text.length() -1) {
          text += '0';
        }
        // Abschneiden zuviel vorhandener Stellen
        if (text.length() - 1 > text.indexOf('.') +anzahlDerNachkommastellen) {
          text = text.substring(0,text.indexOf('.')+anzahlDerNachkommastellen+1);
        }
      }
      this.setText(text);
    }
    catch (final Exception berechnungsProblem) {
      this.setText(this.zeigeFehlerAn ? "#WERT!" : "");
    }
  }

  public void setAnzahlDerNachkommastellen (final int anzahl) {
    this.anzahlDerNachkommastellen = anzahl;
  }

  public void insertUpdate(DocumentEvent event) {
    berechneNeuenWert();
  }
  public void removeUpdate(DocumentEvent event) {
    berechneNeuenWert();
  }
  public void changedUpdate(DocumentEvent event) {
    berechneNeuenWert();
  }

  /**
   * setZeigeFehlerAn
   *
   * @param b boolean
   */
  public void setZeigeFehlerAn(boolean b) {
    this.zeigeFehlerAn = b;
  }
  /**
   * Ermöglicht bei der Berechnung Eingabefelder zu übergehen, in denen nur
   * Whitespaces (z.B. Tabs, Leerzeichen) oder aber gar kein Inhalt vorhanden
   * ist.
   * @param b boolean
   */
  public void setIgnoriereWhitespaceEingabefelder (final boolean b) {
    this.ignoriereWhitespaceEingabefelder = b;
  }
}
