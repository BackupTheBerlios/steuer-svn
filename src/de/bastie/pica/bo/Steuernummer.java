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
 * @version 1.0 Stand: 23. November 2006
 */
public final class Steuernummer {
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_BREMEN = "Bremen"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_BERLIN = "Berlin"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_SCHLESWIG_HOLSTEIN = "Schleswig Holstein"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_HAMBURG = "Hamburg"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_NIEDERSACHSEN = "Niedersachsen"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_SAARLAND = "Saarland"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_HESSEN = "Hessen"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_RHEINLAND_PFALZ = "Rheinland Pfalz"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_BADEN_WUERTTEMBERG = "Baden W�rttemberg"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_BRANDENBURG = "Brandenburg"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_SACHSEN_ANHALT = "Sachsen Anhalt"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_SACHSEN = "Sachsen"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_MECKLENBURG_VORPOMMERN = "Mecklenburg Vorpommern"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_THUERINGEN = "Th�ringen"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_NORDRHEIN_WESTFALEN = "Nordrhein-Westfahlen"; // $NON-NLS-1$
  /**
   * Bundesland
   */
  private final static String BUNDESLAND_BAYERN = "Bayern"; // $NON-NLS-1$

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
  /**
   * Die Steuernummer mit Bundeseinheitlicher Finanzamtsnummer
   * @return long
   */
  public long getSteuernummerMitBUFA () {
    return this.steuernummerMitBUFA;
  }

  /**
   * Pr�ft die aktuelle Steuernummer und liefert die Information, ob die
   * Pr�fziffer OK ist.
   * @return HashMap
   */
  public boolean pruefeSteuernummer () {
    return this.pruefeSteuernummer(this.getSteuernummerMitBUFA());
  }

  /**
   * Pr�ft, ob f�r diese Steuernummer die angeforderte Steuerart zul�ssig ist.
   * @param art ISteuerart
   * @return boolean
   */
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
  /**
   * Die Bundeseinheitliche Finanzamtsnummer
   * @return int
   */
  public int getBUFA () {
    return this.getBUFA (this.getSteuernummerMitBUFA());
  }
  /**
   * Die Bundeseinheitliche Finanzamtsnummer
   * @param steuernummerMitBUFA long
   * @return int
   */
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
  private boolean pruefeSteuernummer (final long steuernummerMitBUFA) throws IllegalSteuernummerException {
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
    return pruefziffer;
  }
  /**
   * Das zur Steuernummer zugeh�rige Bundesland
   * @return String
   */
  public String getBundeslandZurSteuernummer () {
    return this.getBundeslandZurSteuernummer(this.getSteuernummerMitBUFA());
  }
  /**
   * Das zur Steuernummer zugeh�rige Bundesland
   * @param steuernummerMitBUFA long
   * @return String
   */
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
    case 53 : return BUNDESLAND_NORDRHEIN_WESTFALEN;
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
    final StringBuilder formatted = new StringBuilder(this.toLandesFormatSteuernummer());
    final String bundesland = this.getBundeslandZurSteuernummer();
    if (BUNDESLAND_BAYERN.equals(bundesland) ||
        BUNDESLAND_BRANDENBURG.equals(bundesland) ||
        BUNDESLAND_MECKLENBURG_VORPOMMERN.equals(bundesland) ||
        BUNDESLAND_SAARLAND.equals(bundesland) ||
        BUNDESLAND_SACHSEN.equals(bundesland) ||
        BUNDESLAND_SACHSEN_ANHALT.equals(bundesland) ||
        BUNDESLAND_THUERINGEN.equals(bundesland)) {
      formatted.insert(3, '/');
      formatted.insert(7, '/');
    }
    else if (BUNDESLAND_BERLIN.equals(bundesland) ||
             BUNDESLAND_HAMBURG.equals(bundesland) ||
             BUNDESLAND_RHEINLAND_PFALZ.equals(bundesland) ||
             BUNDESLAND_NIEDERSACHSEN.equals(bundesland)) {
      formatted.insert(2, '/');
      formatted.insert(6, '/');
    }
    else if (BUNDESLAND_BREMEN.equals(bundesland) ||
             BUNDESLAND_SCHLESWIG_HOLSTEIN.equals(bundesland)) {
      formatted.insert(2, ' ');
      formatted.insert(6, ' ');
    }
    else if (BUNDESLAND_BADEN_WUERTTEMBERG.equals(bundesland)) {
      formatted.insert(5, '/');
    }
    else if (BUNDESLAND_NORDRHEIN_WESTFALEN.equals(bundesland)) {
      formatted.insert(3, '/');
      formatted.insert(8, '/');
    }
    else { // Hessen
      formatted.insert(3, ' ');
      formatted.insert(7, ' ');
    }

    return formatted.toString();
  }
  /**
   * Liefert die Steuernummer mit der Anzahl der im l�ndereigenen Format
   * genutzen Zeichen
   * @return String
   */
  public String toLandesFormatSteuernummer () {
    final String bundesland = this.getBundeslandZurSteuernummer();
    if (BUNDESLAND_BAYERN.equals(bundesland) ||
        BUNDESLAND_BRANDENBURG.equals(bundesland) ||
        BUNDESLAND_MECKLENBURG_VORPOMMERN.equals(bundesland) ||
        BUNDESLAND_SAARLAND.equals(bundesland) ||
        BUNDESLAND_SACHSEN.equals(bundesland) ||
        BUNDESLAND_SACHSEN_ANHALT.equals(bundesland) ||
        BUNDESLAND_THUERINGEN.equals(bundesland) ||
        BUNDESLAND_NORDRHEIN_WESTFALEN.equals(bundesland)) {
      return this.toSteuernummerMitBUFA().substring(1);
    }
    else if (BUNDESLAND_BERLIN.equals(bundesland) ||
             BUNDESLAND_BREMEN.equals(bundesland) ||
             BUNDESLAND_HAMBURG.equals(bundesland) ||
             BUNDESLAND_RHEINLAND_PFALZ.equals(bundesland) ||
             BUNDESLAND_BADEN_WUERTTEMBERG.equals(bundesland) ||
             BUNDESLAND_NIEDERSACHSEN.equals(bundesland) ||
             BUNDESLAND_SCHLESWIG_HOLSTEIN.equals(bundesland)) {
      return this.toSteuernummerMitBUFA().substring(2);
    }
    else { // Hessen
      return "0"+this.toSteuernummerMitBUFA().substring(2);
    }
  }

  /**
   * R�ckgabe der Steuernummer im 13stelligen ELSTER Format
   * @return String
   */
  public String toElsterFormatSteuernummer () {
    return Long.toString(this.getSteuernummerMitBUFA()).substring(0,4)+'0'+Long.toString(this.getSteuernummerMitBUFA()).substring(4);
  }
  /**
   * R�ckgabe der Steuernummer mit der zugeh�rigen Bundeseinheiten Finanzamtsnummer
   * @return String
   */
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

  /**
   * Liefert die Landeskennung
   * @param bundesland String
   * @return String
   */
  public String getLandeskennung (final String bundesland) {
    if (BUNDESLAND_BAYERN.equals(bundesland)) {
      return "BY"; // $NON-NLS-1$
    }
    if (BUNDESLAND_BRANDENBURG.equals(bundesland)) {
      return "BB"; // $NON-NLS-1$
    }
    if (BUNDESLAND_MECKLENBURG_VORPOMMERN.equals(bundesland)) {
      return "MV"; // $NON-NLS-1$
    }
    if (BUNDESLAND_SAARLAND.equals(bundesland)) {
      return "SL"; // $NON-NLS-1$
    }
    if (BUNDESLAND_SACHSEN.equals(bundesland)) {
      return "SN"; // $NON-NLS-1$
    }
    if (BUNDESLAND_SACHSEN_ANHALT.equals(bundesland)) {
      return "ST"; // $NON-NLS-1$
    }
    if (BUNDESLAND_THUERINGEN.equals(bundesland)) {
      return "TH"; // $NON-NLS-1$
    }
    if (BUNDESLAND_BREMEN.equals(bundesland)) {
      return "HB"; // $NON-NLS-1$
    }
    if (BUNDESLAND_HAMBURG.equals(bundesland)) {
      return "HH"; // $NON-NLS-1$
    }
    if (BUNDESLAND_NIEDERSACHSEN.equals(bundesland)) {
      return "ND"; // $NON-NLS-1$
    }
    if (BUNDESLAND_BERLIN.equals(bundesland)) {
      return "BE"; // $NON-NLS-1$
    }
    if (BUNDESLAND_NORDRHEIN_WESTFALEN.equals(bundesland)) {
      return "NW"; // $NON-NLS-1$
    }
    if (BUNDESLAND_HESSEN.equals(bundesland)) {
      return "HE"; // $NON-NLS-1$
    }
    if (BUNDESLAND_RHEINLAND_PFALZ.equals(bundesland)) {
      return "RP"; // $NON-NLS-1$
    }
    if (BUNDESLAND_SCHLESWIG_HOLSTEIN.equals(bundesland)) {
      return "SH"; // $NON-NLS-1$
    }
    if (BUNDESLAND_BADEN_WUERTTEMBERG.equals(bundesland)) {
      return "BW"; // $NON-NLS-1$
    }
    throw new IllegalArgumentException(bundesland);
  }


  /**
   * Ermittelt die Pr�fziffer nach dem 2er Verfahren (BY (alt),BW,HE,SH)
   * @param steuernummerMitBUFA long
   * @return int
   * @throws IllegalSteuernummerException
   */
  private int berechnePruefzifferNach11erVerfahren (final long steuernummerMitBUFA) throws IllegalSteuernummerException {
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
    if (BUNDESLAND_NORDRHEIN_WESTFALEN.equals(bundesland)) {
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
  private int berechnePruefzifferNach2erVerfahren (final long steuernummerMitBUFA) throws IllegalSteuernummerException {
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
  private int berechnePruefzifferNachModifizierten11erVerfahren (final long steuernummerMitBUFA) {
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
    /** Fehlercode */
    public final static int FEHLERCODE_UNGUELTIGE_BUFA                       = 100000001;
    /** Fehlercode */
    public final static int FEHLERCODE_STEUERNUMMER_MIT_ZU_WENIG_STELLEN     = 100000002;
    /** Fehlercode */
    public final static int FEHLERCODE_STEUERNUMMER_MIT_ZU_VIELEN_STELLEN    = 100000003;
    /** Fehlercode */
    public final static int FEHLERCODE_UNGUELTIGER_STEUERBEZIRK_0            = 100000004;
    /** Fehlercode */
    public final static int FEHLERCODE_UNGUELTIGER_STEUERBEZIRK_KLEINER_100  = 100000005;
    /** Fehlercode */
    public final static int FEHLERCODE_MEHRSTELLIGE_PRUEFZIFFER              = 100000006;
    /** Fehlercode */
    public final static int FEHLERCODE_UNZULAESSIGER_PRUEFZIFFER_ALGORITHMUS = 100000007;

    /**
     * Fehlerhafte Steuernummer
     * @param fehlercode int
     * @param text String
     */
    public IllegalSteuernummerException (final int fehlercode, final String text) {
      super (fehlercode, text);
    }
  }

  /**
   * @todo Testf�lle erzeugen
   * @param args String[]
   */
  public static void main(String[] args) {
    final long [] testSteuernummer = new long[] {
      911112747115l, // BY 2er Verfahren g�ltig
      920423066696l, // BY 11er Verfahren (ganzes FA) g�ltig
      289381508152l, // BW
      918181508155l, // BY (M�nchen)
      923881508157l, // BY (N�rnberg)
      112181508150l, // BE
      304881508155l, // BB
      247581508152l, // HB
      220281508156l, // HH
      261381508153l, // HE
      407981508151l, // MV
      232481508151l, // ND
      513381508159l, // NW 51
      521512312341l, // NW 52
      531481508151l, // NW 53
      272288508658l, // RP
      101081508182l, // SL
      320112312340l, // SN
      310181508154l, // ST
      212981508158l, // MV
      415181508156l, // TH
    };
    Steuernummer stnr = new Steuernummer();
    for (int i = 0; i < testSteuernummer.length; i++) {
      stnr.setSteuernummerMitBUFA(testSteuernummer[i]);
      if (!stnr.pruefeSteuernummer(testSteuernummer[i])) {
        System.err.println(testSteuernummer[i] + " => Fehler bei der Pr�fziffernberechnung "); // $NON-NLS-1$
      }

      System.err.println(stnr.finanzamt.getName() + '\t'+stnr.toElsterFormatSteuernummer()+'\t'+stnr.toFormatLandesFormatSteuernummer());
    }
  }
}
