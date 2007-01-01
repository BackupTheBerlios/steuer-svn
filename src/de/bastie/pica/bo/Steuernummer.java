/**
 * Steuernummer
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.bo;

import de.bastie.pica.*;
import de.bastie.pica.bo.lsta.*;
import de.bastie.pica.bo.ustva.*;

/**
 * Die Steuernummer
 *
 * @author � 2006, 2007 Bastie - Sebastian Ritter
 * @version 1.0
 */
public final class Steuernummer {

  private static String BUNDESLAND_BREMEN = "Bremen";
  private static String BUNDESLAND_BERLIN = "Berlin";
  private static String BUNDESLAND_SCHLESWIG_HOLSTEIN = "Schleswig Holstein";
  private static String BUNDESLAND_HAMBURG = "Hamburg";
  private static String BUNDESLAND_NIEDERSACHSEN = "Niedersachsen";
  private static String BUNDESLAND_SAARLAND = "Saarland";
  private static String BUNDESLAND_HESSEN = "Hessen";
  private static String BUNDESLAND_RHEINLAND_PFALZ = "Rheinland Pfalz";
  private static String BUNDESLAND_BADEN_WUERTTEMBERG = "Baden W�rttemberg";
  private static String BUNDESLAND_BRANDENBURG = "Brandenburg";
  private static String BUNDESLAND_SACHSEN_ANHALT = "Sachsen Anhalt";
  private static String BUNDESLAND_SACHSEN = "Sachsen";
  private static String BUNDESLAND_MECKLENBURG_VORPOMMERN = "Mecklenburg Vorpommern";
  private static String BUNDESLAND_THUERINGEN = "Th�ringen";
  private static String BUNDESLAND_NORDRHEIN_WESTPFALEN = "Nordrhein Westpfahlen";
  private static String BUNDESLAND_BAYERN = "Bayern";

  /**
   * Die Steuernummer
   */
  private long steuernummerMitBUFA;
  /**
   * Finanzamt der Steuernummer
   */
  private Finanzamt finanzamt;

  /**
   * Setzt eine neue Steuernummer und f�hrt eine Pr�fung dieser durch.
   *
   * @param stnrMitBufa long
   * @return boolean Pr�fziffer OK
   * @throws IllegalSteuernummerException
   */
  public boolean setSteuernummerMitBUFA (final long stnrMitBufa) throws IllegalSteuernummerException {
    this.finanzamt = Finanzamt.getFinanzamt (stnrMitBufa);
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
   * Pr�ft die aktuelle Steuernummer und liefert die Information, ob die
   * Pr�fziffer OK ist.
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
        /** @todo �ber Klasse Finanzamt zu l�sen */
        case 9101 : //Augsburg Stadt - AN
        case 9181 : //M�nchen 1 - AN
        case 9182 : //M�nchen 2 - AN
        case 9183 : //M�nchen 3 - AN
        case 9184 : //M�nchen 4 - AN
        case 9185 : //M�nchen 5 - AN
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
   * Pr�ft die �bergebene Steuernummer und liefert zur�ck, ob die Pr�fziffer
   * zul�ssig ist. In Fehlerf�llen wird eine IllegalSteuernummerException
   * geworfen.
   * @param steuernummerMitBUFA long
   * @return boolean ist die Pr�fziffer OK
   * @throws IllegalSteuernummerException
   */
  protected boolean pruefeSteuernummer (final long steuernummerMitBUFA) throws IllegalSteuernummerException {
    boolean problem = false;
    final String bundesland = this.getBundeslandZurSteuernummer(steuernummerMitBUFA);

    // Anzahl der Stellen pr�fen
    if (steuernummerMitBUFA < 100000000000l){
      throw new IllegalSteuernummerException(IllegalSteuernummerException.FEHLERCODE_STEUERNUMMER_MIT_ZU_WENIG_STELLEN,"Die Steuernummer hat zu wenige Stellen.");
    }
    if (steuernummerMitBUFA > 999999999999l){
      throw new IllegalSteuernummerException(IllegalSteuernummerException.FEHLERCODE_STEUERNUMMER_MIT_ZU_VIELEN_STELLEN,"Die Steuernummer hat zu viele Stellen.");
    }

    // Zul�ssige BUFA pr�fen
    problem = true;
    final int steuernummerBufa = Integer.parseInt(Long.toString(steuernummerMitBUFA).substring(0,4));
    for (int bufa : this.getAktuelleBUFAs()) {
      if (bufa == steuernummerBufa) {
        problem = false;
        break;
      }
    }
    if (problem) {
      throw new IllegalSteuernummerException(IllegalSteuernummerException.FEHLERCODE_UNGUELTIGE_BUFA, "Unzul�ssige Bundeseinheitliche Finanzamtsnummer [BUFA].");
    }
    // Steuerbezirk pr�fen
    final String steuerbezirkString = Long.toString(steuernummerMitBUFA).substring(4,7);
    final int steuerbezirk = Integer.parseInt(steuerbezirkString);
    if (steuerbezirk == 0) {
      throw new IllegalSteuernummerException(IllegalSteuernummerException.FEHLERCODE_UNGUELTIGER_STEUERBEZIRK_0,"Der Steuerbezirk "+steuerbezirkString+" ist ung�ltig.");
    }
    else if (steuerbezirk < 100){
      if (BUNDESLAND_BAYERN.equals(bundesland)      ||
          BUNDESLAND_SAARLAND.equals(bundesland)    ||
          BUNDESLAND_THUERINGEN.equals(bundesland)   ||
          BUNDESLAND_SACHSEN.equals(bundesland)     ||
          BUNDESLAND_BRANDENBURG.equals(bundesland) ||
          BUNDESLAND_SACHSEN_ANHALT.equals(bundesland) ||
          BUNDESLAND_MECKLENBURG_VORPOMMERN.equals(bundesland)) {
        throw new IllegalSteuernummerException(IllegalSteuernummerException.FEHLERCODE_UNGUELTIGER_STEUERBEZIRK_KLEINER_100,"Der Steuerbezirk "+steuerbezirkString+" ist in diesem Bundesland ung�ltig.");
      }
    }

    // Steuernummer pr�fen
    if (BUNDESLAND_BAYERN.equals(bundesland) && "99999999".equals(Long.toString (steuernummerMitBUFA).substring(4))) {
      throw new IllegalSteuernummerException(IllegalSteuernummerException.FEHLERCODE_UNGUELTIGER_STEUERBEZIRK_KLEINER_100,"Die Steuernummer "+steuernummerMitBUFA+" ist in diesem Bundesland ung�ltig.");
    }

    // Pr�fziffer pr�fen
    final int pruefziffer = this.berechnePruefziffer(steuernummerMitBUFA);

    boolean pruefzifferOK = pruefziffer == Integer.parseInt(Long.toString(steuernummerMitBUFA).substring(11))
                                        && pruefziffer < 10;
    // F�r BY noch das alte Verfahren pr�fen.
    if (!pruefzifferOK && BUNDESLAND_BAYERN.equals(bundesland)) {
      final int pruefzifferBayernAlt = this.berechnePruefzifferNach2erVerfahren(steuernummerMitBUFA);
      pruefzifferOK = pruefzifferBayernAlt == Integer.parseInt(Long.toString(steuernummerMitBUFA).substring(11))
                                           && pruefzifferBayernAlt < 10;
    }
    return pruefzifferOK;
  }

  /**
   * Berechnet die Pr�fziffer einer Steuernummer
   * @param steuernummerMitBUFA long
   * @return int
   */
  public int berechnePruefziffer (final long steuernummerMitBUFA) {
    int pruefziffer = -1;
    final String bundesland = this.getBundeslandZurSteuernummer(steuernummerMitBUFA);
    if (BUNDESLAND_RHEINLAND_PFALZ.equals(bundesland)) {
      pruefziffer = this.berechnePruefzifferNachModifizierten11erVerfahren(steuernummerMitBUFA);
    }
    else if (BUNDESLAND_BADEN_WUERTTEMBERG.equals(bundesland) ||
             BUNDESLAND_HESSEN.equals(bundesland) ||
             BUNDESLAND_SCHLESWIG_HOLSTEIN.equals(bundesland)) { // BY Auslaufmodell
      pruefziffer = this.berechnePruefzifferNach2erVerfahren(steuernummerMitBUFA);
    }
    else { // Restliche Bundesl�nder (inkl. BY neu)
      pruefziffer = this.berechnePruefzifferNach11erVerfahren(steuernummerMitBUFA);
    }
    return pruefziffer; /** @todo Strukturierte Programmierung - 1 Methodeneingang und 1 Methodenausgang */
  }

  public String getBundeslandZurSteuernummer () {
    return this.getBundeslandZurSteuernummer(this.getSteuernummerMitBUFA());
  }
  public String getBundeslandZurSteuernummer (final long steuernummerMitBUFA) {
    //ehemals waren die ersten zwei Zeichen der BUFA quasi der OFD Bezirk
    final int ofdKennung = Integer.parseInt(Long.toString(steuernummerMitBUFA).substring(0,2));
    switch (ofdKennung) {
    case 10 : return BUNDESLAND_SAARLAND;
    case 11 : return BUNDESLAND_BERLIN;
    case 21 : return BUNDESLAND_SCHLESWIG_HOLSTEIN;
    case 22 : return BUNDESLAND_HAMBURG;
    case 23 : return BUNDESLAND_NIEDERSACHSEN;
    case 24 : return BUNDESLAND_BREMEN;
    case 26 : return BUNDESLAND_HESSEN;
    case 27 : return BUNDESLAND_RHEINLAND_PFALZ;
    case 28 : return BUNDESLAND_BADEN_WUERTTEMBERG;
    case 30 : return BUNDESLAND_BRANDENBURG;
    case 31 : return BUNDESLAND_SACHSEN_ANHALT;
    case 32 : return BUNDESLAND_SACHSEN;
    case 40 : return BUNDESLAND_MECKLENBURG_VORPOMMERN;
    case 41 : return BUNDESLAND_THUERINGEN;
    case 51 :
    case 52 :
    case 53 : return BUNDESLAND_NORDRHEIN_WESTPFALEN;
    case 91 :
    case 92 : return BUNDESLAND_BAYERN;
    default : throw new SteuerException (900000000,"Die bundeseinheitliche Finanzamtsnummer ist nicht korrekt.");
    }
  }

  /**
   * Liefert die formatierte Steuernummer im Landesformat mit den zugeh�rigen
   * Querstrichen (/).
   * @return String
   */
  public String toFormatLandesFormatSteuernummer () {
    /** @todo Not yet implemented */
    throw new UnsupportedOperationException(new Exception ().getStackTrace()[0].getMethodName()+" not yet implemented");
  }
  /**
   * Liefert die Steuernummer mit der Anzahl der im l�ndereigenen Format
   * genutzen Zeichen
   * @return String
   */
  public String toLandesFormatSteuernummer () {
    /** @todo Not yet implemented */
    throw new UnsupportedOperationException(new Exception ().getStackTrace()[0].getMethodName()+" not yet implemented");
  }
  public String toElsterFormatSteuernummer () {
    return Long.toString(this.getSteuernummerMitBUFA()).substring(0,4)+'0'+Long.toString(this.getSteuernummerMitBUFA()).substring(4);
  }
  public String toSteuernummerMitBUFA () {
    return Long.toString (this.getSteuernummerMitBUFA());
  }

  /**
   * Liefert alle aktuell g�ltigen bundeseinheitlichen Finanzamtsnummern
   * @return int
   */
  public synchronized int[] getAktuelleBUFAs () {
    return Finanzamt.getAktuelleBUFAs ();
  }

  public String getLandeskennung (final String bundesland) {
    /** @todo Not yet implemented */
    throw new UnsupportedOperationException(new Exception ().getStackTrace()[0].getMethodName()+" not yet implemented");
  }
  public String [] getBundeslaender () {
    /** @todo Not yet implemented */
    throw new UnsupportedOperationException(new Exception ().getStackTrace()[0].getMethodName()+" not yet implemented");
  }


  /**
   * Ermittelt die Pr�fziffer nach dem 2er Verfahren (BY (alt),BW,HE,SH)
   * @param steuernummerMitBUFA long
   * @return int
   * @throws IllegalSteuernummerException
   */
  protected int berechnePruefzifferNach11erVerfahren (final long steuernummerMitBUFA) throws IllegalSteuernummerException {
    int pruefziffer = -1;
    int [] faktoren = null;
    final String bundesland = this.getBundeslandZurSteuernummer(steuernummerMitBUFA);
    // Faktoren ermitteln
    if (BUNDESLAND_BAYERN.equals(bundesland) ||
        BUNDESLAND_BRANDENBURG.equals(bundesland) ||
        BUNDESLAND_MECKLENBURG_VORPOMMERN.equals(bundesland) ||
        BUNDESLAND_SAARLAND.equals(bundesland) ||
        BUNDESLAND_SACHSEN.equals(bundesland) ||
        BUNDESLAND_SACHSEN_ANHALT.equals(bundesland) ||
        BUNDESLAND_THUERINGEN.equals(bundesland)) {
      faktoren = new int [] {0,5,4,3,2,7,6,5,4,3,2};
    }
    else if (BUNDESLAND_BREMEN.equals(bundesland) ||
             BUNDESLAND_HAMBURG.equals(bundesland)) {
      faktoren = new int [] {0,0,4,3,2,7,6,5,4,3,2};
    }
    else if (BUNDESLAND_NIEDERSACHSEN.equals(bundesland)) {
      faktoren = new int [] {0,0,2,9,8,7,6,5,4,3,2};
    }
    else if (BUNDESLAND_BERLIN.equals(bundesland)) {
      final int [] faktorenBERLIN_A = new int [] {0,0,0,0,7,6,5,8,4,3,2};
      final int [] faktorenBERLIN_B = new int [] {0,0,2,9,8,7,6,5,4,3,2};
      final int bufa = this.getBUFA(steuernummerMitBUFA);
      switch (bufa) {
      case 1127 :
      case 1129 :
      case 1130 :
        faktoren = faktorenBERLIN_A;
        break;
      case 1118 :
      case 1131 :
      case 1132 :
      case 1133 :
      case 1134 :
      case 1135 :
      case 1136 :
      case 1137 :
        faktoren = faktorenBERLIN_B;
        break;
      default :
        faktoren = faktorenBERLIN_A;
        // Steuerbezirk pr�fen
        final String steuerbezirkString = Long.toString(steuernummerMitBUFA).substring(4,7);
        final int steuerbezirk = Integer.parseInt(steuerbezirkString);
        if (steuerbezirk > 200 && steuerbezirk < 694) {
          faktoren = faktorenBERLIN_B;
        }
        else if ((steuerbezirk > 0 && steuerbezirk < 30) && bufa == 1116) {
          faktoren = faktorenBERLIN_B;
        }
        else if ((steuerbezirk == 680 || steuerbezirk == 684) && bufa == 1119) {
          faktoren = faktorenBERLIN_B;
        }
      }
    }
    else { //NRW
      faktoren = new int [] {0,3,2,1,7,6,5,4,3,2,1};
    }
    // Produkt
    int [] produkt = new int[faktoren.length];
    final String stnr = Long.toString(steuernummerMitBUFA);
    for (int i = 0; i < produkt.length; i++) {
      produkt [i] = Integer.parseInt(stnr.substring(i,i+1)) * faktoren [i];
    }
    // Summe
    int summe = 0;
    for (int i = 0; i < produkt.length; i++) {
      summe += produkt [i];
    }
    // Pr�fziffer
    if (BUNDESLAND_NORDRHEIN_WESTPFALEN.equals(bundesland)) {
      final int vorhergehendeDurch11TeilbareZahl = (summe % 11 == 0) ? summe - 11 : ((summe / 11) * 11);
      pruefziffer = summe - vorhergehendeDurch11TeilbareZahl;
    }
    else {
      final int naechsteDurch11TeilbareZahl = (summe % 11 == 0) ? summe + 11 : (((summe / 11) + 1) * 11);
      pruefziffer = naechsteDurch11TeilbareZahl - summe;
    }
    return pruefziffer;
  }

  /**
   * Ermittelt die Pr�fziffer nach dem 2er Verfahren (BY (alt),BW,HE,SH)
   * @param steuernummerMitBUFA long
   * @return int
   * @throws IllegalSteuernummerException Fehler bei der Pr�fziffernberechnung
   */
  protected int berechnePruefzifferNach2erVerfahren (final long steuernummerMitBUFA) throws IllegalSteuernummerException {
    int pruefziffer = -1;
    final int summanden [] = new int[] {0,0,9,8,7,6,5,4,3,2,1};
    int [] faktoren = new int[] {0,0,512,256,128,64,32,16,8,4,2};
    if (BUNDESLAND_BAYERN.equals(this.getBundeslandZurSteuernummer(steuernummerMitBUFA))) {
      faktoren = new int[] {0,0,0,0,128,64,32,16,8,4,2}; // In BY werden Stelle 3 und 4 nicht ber�cksichtiigt
    }
    final String stnr = Long.toString(steuernummerMitBUFA);
    int [] summe = new int[summanden.length];
    for (int i = 0; i < summe.length; i++) {
      summe [i] = Integer.parseInt(stnr.substring(i,i+1)) + summanden [i];
    }
    // Summe einstellig (nur letzte Stelle)
    for (int i = 0; i < summe.length; i++) {
      while (summe [i] > 9) {
        summe [i] -= 10;
      }
    }
    // Produkt
    int [] produkt = new int [faktoren.length];
    for (int i = 0; i < produkt.length; i++) {
      produkt [i] = summe [i] * faktoren [i];
    }
    // Quersumme einstellig
    int [] quersummeProdukt = new int [produkt.length];
    for (int i = 0; i < quersummeProdukt.length; i++) {
      String wert = Integer.toString(produkt [i]);
      int quersumme = 0;
      for (int j = 0; j < wert.length(); j++) {
         quersumme += Integer.parseInt(wert.substring(j,j+1));
      }
      while (quersumme > 9) {
        String wertQuersumme = Integer.toString(quersumme);
        int querQuerSumme = 0;
        for (int k = 0; k < wertQuersumme.length(); k++) {
          querQuerSumme += Integer.parseInt(wertQuersumme.substring (k,k+1));
        }
        quersumme = querQuerSumme;
      }
      quersummeProdukt [i] = quersumme;
    }
    // Summe der einstelligen Quersummen
    int summeEinstelligeQuersummen = 0;
    for (int i = 0; i < quersummeProdukt.length; i++) {
      summeEinstelligeQuersummen += quersummeProdukt [i];
    }

    pruefziffer = ((summeEinstelligeQuersummen / 10) +1) *10
                - summeEinstelligeQuersummen;
    if (pruefziffer > 10) { // Korrektur falls summeEinstelligeQuersummen glatt durch 10 teilbar.
      pruefziffer -= 10;
    }

    return pruefziffer;
  }

  /**
   * Ermittelt die Pr�fziffer nach dem modifizierten 11er Verfahren (nur Rheinland-Pfalz)
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
    pruefziffer = ((quersumme / 10) +1) *10
                - quersumme;
    if (pruefziffer > 10) {
      pruefziffer -= 10;
    }
    return pruefziffer;
  }

  /**
   * Sofern eine Steuernummer generell ung�ltig ist wird diese mit der
   * IllegalSteuernummerException zur�ckgewiesen.
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
      272288508658l, // RP g�ltig
      911112747115l, // BY 2er Verfahren g�ltig
      920423066696l, // BY 11er Verfahren (ganzes FA) g�ltig

    };
    Steuernummer stnr = new Steuernummer();
    for (int i = 0; i < TEST_STEUERNUMMERN.length; i++) {
      System.out.println("Pr�fziffer Steuernummer ["+TEST_STEUERNUMMERN[i]+"] = "+stnr.berechnePruefziffer(TEST_STEUERNUMMERN[i]));
    }
  }
}
