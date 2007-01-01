/**
 * Finanzamt
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.bo;

import java.io.*;
import java.util.*;

import de.bastie.pica.*;

/**
 * Repr�sentiert das Finanzamt
 *
 * @author � 2006 Bastie - Sebastian Ritter
 * @version 1.0
 */
public final class Finanzamt {
  /**
   * Die Bundesfinanzamtsnummer
   */
  private int bufa;
  /**
   * Der Namew des Finanzamtes
   */
  private String name;
  /**
   * Information, ob f�r dieses Finanzamt eine ELSTER �bermittlung zul�ssig ist
   */
  private boolean elsterUebermittlungZulaessig = false;
  /**
   * Information, ob f�r dieses Finanzamt nur Test-ELSTER �bermittlungen zul�ssig sind
   */
  private boolean testUebermittlungZulaessig = false;
  /**
   * Information, ob dieses Finanzamt k�nftig entf�llt (wichtig f�r Lohnsteuerbescheinigungen)
   */
  private boolean kuenftigEntfallend = false;
  /**
   * Information, ob f�r dieses Finanzamt UStVA, LStA und Dauerfristverl�ngerungen per ELSTER �bermittlung zul�ssig ist.
   */
  private boolean voranmeldungUebermittlungZulaessig = false;
  /**
   * Information, ob f�r diese Finanzamt ESt per ELSTER �bermittlung zul�ssig ist.
   */
  private boolean einkommensteuerZulaessig = false;

  /**
   * Information, ob f�r diese Finanzamt ESt per ELSTER �bermittlung zul�ssig ist.
   * @param b boolean
   */
  protected void setElsterUebermittlungZulaessig(final boolean jaNein) {
    this.elsterUebermittlungZulaessig = jaNein;
  }

  protected void setTestUebermittlungZulaessig(final boolean jaNein) {
    this.testUebermittlungZulaessig = jaNein;
  }

  protected void setKuenftigEntfallend(final boolean jaNein) {
    this.kuenftigEntfallend = jaNein;
  }

  protected void setVoranmeldungUebermittlungZulaessig(final boolean jaNein) {
    this.voranmeldungUebermittlungZulaessig = jaNein;
  }

  protected void setEinkommensteuerZulaessig(final boolean jaNein) {
    this.einkommensteuerZulaessig = jaNein;
  }

  public boolean isElsterUebermittlungZulaessig() {
    return this.elsterUebermittlungZulaessig;
  }

  public boolean isTestUebermittlungZulaessig() {
    return this.testUebermittlungZulaessig;
  }

  public boolean isKuenftigEntfallend() {
    return this.kuenftigEntfallend;
  }

  public boolean isVoranmeldungUebermittlungZulaessig() {
    return this.voranmeldungUebermittlungZulaessig;
  }

  public boolean isEinkommensteuerZulaessig() {
    return this.einkommensteuerZulaessig;
  }


  protected void setName(final String faName) {
    this.name = faName;
  }

  public String getName() {
    return this.name;
  }

  protected void setBufa(final int bufa) {
    this.bufa = bufa;
  }

  public int getBufa() {
    return this.bufa;
  }

  /**
   * Liste der Finanz�mter
   */
  private static ArrayList<Finanzamt> finanzaemter;
  public static Finanzamt getFinanzamt(final long steuernummerMitBUFA) {
    Finanzamt fa = null;
    if (finanzaemter == null) {
      try {
        load();
      }
      catch (final IOException shit) {
        throw new SteuerException(900000000, "Problem beim Laden der Finanz�mter.");
      }
    }
    final int bufa = Integer.parseInt(Long.toString(steuernummerMitBUFA).substring(0,4));
    for (Finanzamt finanzamt : finanzaemter) {
      if (bufa == finanzamt.getBufa()) {
        fa = finanzamt;
        break;
      }
    }
    return fa;
  }

  /**
   * Die zul�ssigen Bundesfinanzamtsnummern
   */
  private static int[] bufas;

  /**
   * Liefert alle aktuell g�ltigen bundeseinheitlichen Finanzamtsnummern
   * @return int
   */
  public synchronized static int[] getAktuelleBUFAs() {
    if (bufas == null) {
      try {
        if (finanzaemter == null) {
          finanzaemter = Finanzamt.load();
        }
        synchronized (finanzaemter) {
          bufas = new int[finanzaemter.size()];
          for (int i = 0; i < finanzaemter.size(); i++) {
            bufas[i] = finanzaemter.get(i).getBufa();
          }
        }
      }
      catch (final Exception shit) {
        throw new SteuerException(900000000, "Problem beim Laden der Finanz�mter", shit);
      }
    }
    return bufas;
  }

  /**
   * Parse Information
   */
  private final static char ELSTER_TEST_AMT = 'T',
                            ELSTER_PRODUKTION_AMT = 'E',
                            INFORMATION_POSITIV = 'J',
                            INFORMATON_NEGATIV = 'N';
  public static ArrayList<Finanzamt> load() throws IOException {
    if (finanzaemter == null) {
      finanzaemter = new ArrayList<Finanzamt>();
      final Properties p = new Properties();
      p.load(Finanzamt.class.getResourceAsStream("Finanzaemter.properties"));
      for (Object nr : p.keySet()) {
        final Finanzamt fa = new Finanzamt();
        fa.setBufa(Integer.parseInt((String) nr));
        final String faDaten = p.getProperty((String) nr);
        final Scanner scan = new Scanner(faDaten).useDelimiter("\\s*,\\s*");
        fa.setName(scan.next().trim());
        switch (scan.next().charAt(0)) { // ELSTER zul�ssig
          case ELSTER_PRODUKTION_AMT:
            fa.setElsterUebermittlungZulaessig(true);
            fa.setTestUebermittlungZulaessig(false);
            break;
          case ELSTER_TEST_AMT:
            fa.setElsterUebermittlungZulaessig(true);
            fa.setTestUebermittlungZulaessig(true);
            break;
          default:
            fa.setElsterUebermittlungZulaessig(false);
            fa.setTestUebermittlungZulaessig(false);
        }
        switch (scan.next().charAt(0)) { // Voranmeldungen und Dauerfritverl�ngerung zul�ssig?
          case INFORMATION_POSITIV:
            fa.setVoranmeldungUebermittlungZulaessig(true);
            break;
          case INFORMATON_NEGATIV:
          default:
            fa.setVoranmeldungUebermittlungZulaessig(false);
            break;
        }
        switch (scan.next().charAt(0)) { // Finanzamt wegfallend?
          case INFORMATION_POSITIV:
            fa.setKuenftigEntfallend(true);
            break;
          case INFORMATON_NEGATIV:
          default:
            fa.setKuenftigEntfallend(false);
            break;
        }
        switch (scan.next().charAt(0)) { // Einkommensteuer zul�ssig?
          case INFORMATION_POSITIV:
            fa.setEinkommensteuerZulaessig(true);
            break;
          case INFORMATON_NEGATIV:
          default:
            fa.setEinkommensteuerZulaessig(false);
            break;
        }
        scan.close();

        finanzaemter.add(fa);
      }
    }
    return finanzaemter;
  }

  /** @todo Geimeindedaten, Anschriften etc. GEMFA Daten aufnehmen */
}
