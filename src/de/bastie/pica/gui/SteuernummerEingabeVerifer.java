/**
 * SteuernummerEingabeVerifer
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

import de.bastie.pica.bo.*;

/**
 * Steuernummernprüfung bei der Eingabe
 *
 * @author © 2006, 2007 Bastie - Sebastian Ritter
 * @version 1.0
 */
public class SteuernummerEingabeVerifer extends InputVerifier {
  private Border defaultBorder;
  public boolean verify (JComponent input) {
    boolean inputOK = false;
    try {
      if (input instanceof JTextComponent) {
        Steuernummer stnr = new Steuernummer();
        inputOK = stnr.setSteuernummerMitBUFA(Long.parseLong(((JTextComponent) input).getText()));
      }
    }
    catch (NumberFormatException keineSteuernummer){}
    return inputOK;
  }

  public boolean shouldYieldFocus(JComponent input) {
    if (this.defaultBorder == null) {
      this.defaultBorder = input.getBorder();
    }
    if (!this.verify(input)) {
      input.setBorder(BorderFactory.createLineBorder(Color.RED,2));
    }
    else {
      input.setBorder(this.defaultBorder);
    }
    return true;
  }

}
