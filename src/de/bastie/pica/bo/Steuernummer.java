package de.bastie.pica.bo;

import java.util.HashMap;
import de.bastie.pica.SteuerException;
import de.bastie.pica.bo.ustva.Umsatzsteuervoranmeldung;
import de.bastie.pica.bo.ustva.Dauerfristverlaengerung;
import de.bastie.pica.bo.lsta.Lohnsteueranmeldung;

public final class Steuernummer {

  private long steuernummerMitBUFA;

  /**
   * Setzt eine neue Steuernummer und führt eine Prüfung dieser durch.
   *
   * @param stnrMitBufa long
   * @return boolean Prüfziffer OK
   * @throws IllegalSteuernummerException
   */
  public boolean setSteuernummerMitBUFA (final long stnrMitBufa) throws IllegalSteuernummerException {
    boolean pruefzifferOK = this.pruefeSteuernummer (stnrMitBufa);
    this.steuernummerMitBUFA = stnrMitBufa;
    return pruefzifferOK;
  }

  public long getSteuernummerMitBUFA () {
    return this.steuernummerMitBUFA;
  }

  public Steuernummer () {
  }

  /**
   * Prüft die aktuelle Steuernummer und liefert die Information, ob die
   * Prüfziffer OK ist.
   * @return HashMap
   */
  public boolean pruefeSteuernummer () {
    return this.pruefeSteuernummer(this.getSteuernummerMitBUFA());
  }

  public boolean isSteuernummerFuerSteuerartZulaessig (final ISteuerart art) {
    return this.isSteuernummerFuerSteuerartZulaessig(this.getSteuernummerMitBUFA(), art);
  }

  /**
   * @todo Klasse Finanzamt nutzen
   * @param steuernummerMitBUFA long
   * @param art Steuerart
   * @return boolean
   */
  public boolean isSteuernummerFuerSteuerartZulaessig (final long steuernummerMitBUFA, final ISteuerart art) {
    boolean zulaessig = true;
    try {
      this.pruefeSteuernummer(steuernummerMitBUFA);
      switch (this.getBUFA ()) {
        case 9101 : //Augsburg Stadt - AN
        case 9181 : //München 1 - AN
        case 9182 : //München 2 - AN
        case 9183 : //München 3 - AN
        case 9184 : //München 4 - AN
        case 9185 : //München 5 - AN
          if (art instanceof Umsatzsteuervoranmeldung ||
              art instanceof Lohnsteueranmeldung      ||
              art instanceof Dauerfristverlaengerung)   {
            zulaessig = false;
          }
          break;
      }
    }
    catch (final IllegalSteuernummerException unzulaessig) {
      zulaessig = false;
    }
    return zulaessig;
  }

  public int getBUFA () {
    return this.getBUFA (this.getSteuernummerMitBUFA());
  }
  public int getBUFA (final long steuernummerMitBUFA) {
    return Integer.parseInt(Long.toString(steuernummerMitBUFA).substring(0,3));
  }

  /**
   * Prüft die übergebene Steuernummer und liefert zurück, ob die Prüfziffer
   * zulässig ist. In Fehlerfällen wird eine IllegalSteuernummerException
   * geworfen.
   * @param steuernummerMitBUFA long
   * @return boolean ist die Prüfziffer OK
   * @throws IllegalSteuernummerException
   */
  protected boolean pruefeSteuernummer (final long steuernummerMitBUFA) throws IllegalSteuernummerException {
    boolean problem = false;

    // Anzahl der Stellen prüfen
    if (steuernummerMitBUFA < 100000000000l){
      throw new IllegalSteuernummerException(IllegalSteuernummerException.FEHLERCODE_STEUERNUMMER_MIT_ZU_WENIG_STELLEN,"Die Steuernummer hat zu wenige Stellen.");
    }
    if (steuernummerMitBUFA < 999999999999l){
      throw new IllegalSteuernummerException(IllegalSteuernummerException.FEHLERCODE_STEUERNUMMER_MIT_ZU_VIELEN_STELLEN,"Die Steuernummer hat zu viele Stellen.");
    }

    // Zulässige BUFA prüfen
    problem = true;
    for (int bufa : this.getAktuelleBUFAs()) {
      if (steuernummerMitBUFA < ((bufa +1) * 100000000) &&
          steuernummerMitBUFA >= (bufa * 100000000)) {
        problem = false;
        break;
      }
    }
    if (problem) throw new IllegalSteuernummerException(IllegalSteuernummerException.FEHLERCODE_UNGUELTIGE_BUFA,"Unzulässige Bundeseinheitliche Finanzamtsnummer.");

    // Steuerbezirk prüfen
    final String steuerbezirkString = Long.toString(steuernummerMitBUFA).substring(4,7);
    final int steuerbezirk = Integer.parseInt(steuerbezirkString);
    if (steuerbezirk == 0) {
      throw new IllegalSteuernummerException(IllegalSteuernummerException.FEHLERCODE_UNGUELTIGER_STEUERBEZIRK_0,"Der Steuerbezirk "+steuerbezirkString+" ist ungültig.");
    }
    else if (steuerbezirk < 100){
      final String bundesland = this.getBundeslandZurSteuernummer(steuernummerMitBUFA);
      if ("Bayern".equals(bundesland)      ||
          "Saarland".equals(bundesland)    ||
          "Thüringen".equals(bundesland)   ||
          "Sachsen".equals(bundesland)     ||
          "Brandenburg".equals(bundesland) ||
          "Sachsen-Anhalt".equals(bundesland) ||
          "Mecklenburg-Vorpommern".equals(bundesland)) {
        throw new IllegalSteuernummerException(IllegalSteuernummerException.FEHLERCODE_UNGUELTIGER_STEUERBEZIRK_KLEINER_100,"Der Steuerbezirk "+steuerbezirkString+" ist in diesem Bundesland ungültig.");
      }
    }

    // Steuernummer prüfen
    final String bundesland = this.getBundeslandZurSteuernummer(steuernummerMitBUFA);
    if ("Bayern".equals(bundesland) && "99999999".equals(Long.toString (steuernummerMitBUFA).substring(4))) {
      throw new IllegalSteuernummerException(IllegalSteuernummerException.FEHLERCODE_UNGUELTIGER_STEUERBEZIRK_KLEINER_100,"Die Steuernummer "+steuernummerMitBUFA+" ist in diesem Bundesland ungültig.");
    }

    // Prüfziffer prüfen
    final int pruefziffer = this.berechnePruefziffer(steuernummerMitBUFA);

    return pruefziffer == Integer.parseInt(Long.toString(steuernummerMitBUFA).substring(11))
                       && pruefziffer < 10;

  }

  public int berechnePruefziffer (final long steuernummerMitBUFA) {
    /** @todo Not yet implemented */
    throw new Error();
  }

  public String getBundeslandZurSteuernummer () {
    return this.getBundeslandZurSteuernummer(this.getSteuernummerMitBUFA());
  }
  public String getBundeslandZurSteuernummer (final long steuernummerMitBUFA) {
    /** @todo Not yet implemented */
    throw new Error();
  }

  public String toFormatLandesFormatSteuernummer () {
    /** @todo Not yet implemented */
    throw new Error();
  }
  public String toLandesFormatSteuernummer () {
    /** @todo Not yet implemented */
    throw new Error();
  }
  public String toElsterFormatSteuernummer () {
    return Long.toString(this.getSteuernummerMitBUFA()).substring(0,4)+'0'+Long.toString(this.getSteuernummerMitBUFA()).substring(4);
  }
  public String toSteuernummerMitBUFA () {
    return Long.toString (this.getSteuernummerMitBUFA());
  }
  /**
   * Liefert alle aktuell gültigen bundeseinheitlichen Finanzamtsnummern
   * @return int
   */
  public int[] getAktuelleBUFAs () {
    /** @todo Not yet implemented */
    throw new Error();
  }

  public String getLandeskennung (final String bundesland) {
    /** @todo Not yet implemented */
    throw new Error();
  }
  public String [] getBundeslaender () {
    /** @todo Not yet implemented */
    throw new Error();
  }


  /**
   * Ermittelt die Prüfziffer nach dem 2er Verfahren (BY (alt),BW,HE,SH)
   * @param steuernummerMitBUFA long
   * @return int
   * @throws IllegalSteuernummerException
   */
  protected int berechnePruefzifferNach2erVerfahren (final long steuernummerMitBUFA) throws IllegalSteuernummerException {
    int pruefziffer = -1;
    final int summanden [] = new int[] {0,0,9,8,7,6,5,4,3,2,1};
    int [] faktoren = new int[] {0,0,512,256,128,64,32,16,8,4,2};
    if ("Bayern".equals(this.getBundeslandZurSteuernummer(steuernummerMitBUFA))) {
      faktoren = new int[] {0,0,0,0,128,64,32,16,8,4,2}; // In BY werden Stelle 3 und 4 nicht berücksichtiigt
    }
    else if ("Berlin".equals(this.getBundeslandZurSteuernummer(steuernummerMitBUFA))) {

    }



    return pruefziffer;
  }
  /**
   * Ermittelt die Prüfziffer nach dem modifizierten 11er Verfahren (nur Rheinland-Pfalz)
   * @param steuernummerMitBUFA long
   * @return int
   */
  protected int berechnePruefzifferNachModifizierten11erVerfahren (final long steuernummerMitBUFA) {
    int pruefziffer = -1;

    final int [] faktoren = new int[] {0,0,1,2,1,2,1,2,1,2,1};
    final String stnrMitBUFA = Long.toString(steuernummerMitBUFA);
    int quersumme = 0;
    for (int i = 0; i < faktoren.length; i++) {
      final int stnrZiffer = Integer.parseInt(stnrMitBUFA.substring(i,i+1));
      int produkt = stnrZiffer * faktoren [i];
      if (produkt > 9) {
        quersumme += (produkt - 10 + 1);
      }
      else {
        quersumme += produkt;
      }
    }
    pruefziffer = (((int)(quersumme / 10)) +1) *10
                - quersumme;
    return pruefziffer;
  }

  /**
   * Sofern eine Steuernummer generell ungültig ist wird diese mit der
   * IllegalSteuernummerException zurückgewiesen.
   *
   * @author unbekannt
   * @version 1.0
   */
  public final static class IllegalSteuernummerException extends SteuerException {
    public static int FEHLERCODE_UNGUELTIGE_BUFA                       = 100000001;
    public static int FEHLERCODE_STEUERNUMMER_MIT_ZU_WENIG_STELLEN     = 100000002;
    public static int FEHLERCODE_STEUERNUMMER_MIT_ZU_VIELEN_STELLEN    = 100000003;
    public static int FEHLERCODE_UNGUELTIGER_STEUERBEZIRK_0            = 100000004;
    public static int FEHLERCODE_UNGUELTIGER_STEUERBEZIRK_KLEINER_100  = 100000005;
    public static int FEHLERCODE_MEHRSTELLIGE_PRUEFZIFFER              = 100000006;
    public static int FEHLERCODE_UNZULAESSIGER_PRUEFZIFFER_ALGORITHMUS = 100000007;

    public IllegalSteuernummerException (final int fehlercode, final String text) {
      super (fehlercode, text);
    }
  }

  public static void main(String[] args) {
    final long [] TEST_STEUERNUMMERN = new long[] {
      272288508658l, // RP gültig
      911112747115l, // BY 2er Verfahren gültig
      920423066696l, // BY 11er Verfahren (ganzes FA) gültig

    };
    Steuernummer stnr = new Steuernummer();
    for (int i = 0; i < TEST_STEUERNUMMERN.length; i++) {
      System.out.println("Prüfziffer Steuernummer ["+TEST_STEUERNUMMERN[i]+"] = "+stnr.berechnePruefziffer(TEST_STEUERNUMMERN[i]));
    }
  }
}
