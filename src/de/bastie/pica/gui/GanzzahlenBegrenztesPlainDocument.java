/**
 * GanzzahlenBegrenztesPlainDocument
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.math.BigInteger;

public class GanzzahlenBegrenztesPlainDocument extends ZeichenBegrenztesPlainDocument {

  public GanzzahlenBegrenztesPlainDocument (final int maximaleZeichenAnzahl) {
    super (maximaleZeichenAnzahl);
  }

  public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
    try {
      new BigInteger (str);
      super.insertString (offs,str,a);
    }
    catch (Exception keineZahl) {}
  }


}
