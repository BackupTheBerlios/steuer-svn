/**
 * SteuerException
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica;

/**
 * Basisklasse für geworfene Ausnahmen im Bereich Steuer.
 * Fehlercodes sind grds. 9stellig (3 Stellen Kategorie, 3 Stellen Subsystem, 3 Stellen Fehlernummer)
 * Kategorien:   100=Grundlegener Fehler
 * Subsystem:    000=Basis
 * Fehlernummer: je Fehler.
 * Die einzelnen Fehlernummern werden hierbei bei den konkreten Exceptions definiert.
 * @author unbekannt
 * @version 1.0
 */
public class SteuerException extends RuntimeException {
  /**
   * Eine Ausnahme wird ohne expliziten Fehlercode ausgegeben.
   */
  final static public int FEHLERCODE_OHNE = Integer.MIN_VALUE;
  final private int fehlercode;
  public int getFehlercode () {
    return this.fehlercode;
  }
  public SteuerException(final int fehlercode, final String message) {
    super (message);
    this.fehlercode = fehlercode;
  }

  public SteuerException(final int fehlercode, final String message, final Throwable cause) {
      super(message, cause);
      this.fehlercode = fehlercode;
  }
}
