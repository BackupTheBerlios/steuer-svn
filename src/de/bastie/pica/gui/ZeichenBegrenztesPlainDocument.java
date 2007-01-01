/**
 * ZeichenBegrenztesPlainDocument
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.gui;

import javax.swing.text.*;

public class ZeichenBegrenztesPlainDocument extends PlainDocument {
  final private int max;
  public ZeichenBegrenztesPlainDocument (final int maximaleZeichenAnzahl) {
    this.max = maximaleZeichenAnzahl;
  }

  public void insertString (int offs, String str, AttributeSet a) throws BadLocationException {
    if (! (this.getLength()+str.length() > max)) {
      super.insertString(offs,str,a);
    }
  }


}
