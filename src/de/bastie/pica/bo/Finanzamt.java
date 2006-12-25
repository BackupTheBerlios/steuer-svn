package de.bastie.pica.bo;

import java.io.*;
import java.util.*;
import de.bastie.pica.SteuerException;

public final class Finanzamt {
  private int bufa;
  private String name;
  private boolean elsterUebermittlungZulaessig = false;
  private boolean testUebermittlungZulaessig = false;
  private boolean kuenftigEntfallend = false;
  private boolean voranmeldungUebermittlungZulaessig = false;
  private boolean einkommensteuerZulaessig = false;

  protected void setElsterUebermittlungZulaessig (final boolean b) {
    this.elsterUebermittlungZulaessig = b;
  }
  protected void setTestUebermittlungZulaessig (final boolean b) {
    this.testUebermittlungZulaessig = b;
  }
  protected void setKuenftigEntfallend (final boolean b) {
    this.kuenftigEntfallend = b;
  }
  protected void setVoranmeldungUebermittlungZulaessig (final boolean b) {
    this.voranmeldungUebermittlungZulaessig = b;
  }
  protected void setEinkommensteuerZulaessig (final boolean b) {
    this.einkommensteuerZulaessig = b;
  }
  public boolean isElsterUebermittlungZulaessig () {
    return this.elsterUebermittlungZulaessig;
  }
  public boolean isTestUebermittlungZulaessig () {
    return this.testUebermittlungZulaessig;
  }
  public boolean isKuenftigEntfallend () {
    return this.kuenftigEntfallend;
  }
  public boolean isVoranmeldungUebermittlungZulaessig () {
    return this.voranmeldungUebermittlungZulaessig;
  }
  public boolean isEinkommensteuerZulaessig () {
    return this.einkommensteuerZulaessig;
  }


  protected void setName (final String faName) {
    this.name = faName;
  }
  public String getName () {
    return this.name;
  }

  protected void setBufa (final int bufa) {
    this.bufa = bufa;
  }
  public int getBufa() {
    return this.bufa;
  }



  private final static char ELSTER_TEST_AMT = 'T',
                            ELSTER_PRODUKTION_AMT = 'E',
                            INFORMATION_POSITIV = 'J',
                            INFORMATON_NEGATIV = 'N';
  public static ArrayList<Finanzamt> load () throws IOException {
    final ArrayList<Finanzamt> finanzaemter = new ArrayList<Finanzamt> ();
    /** @todo static auflösen */
    final Properties p = new Properties();
    p.load (Finanzamt.class.getResourceAsStream("Finanzaemter.properties"));
    for (Object nr : p.keySet()) {
      final Finanzamt fa = new Finanzamt();
      fa.setBufa(Integer.parseInt((String)nr));
      final String faDaten = p.getProperty((String)nr);
      final Scanner scan = new Scanner(faDaten).useDelimiter("\\s*,\\s*");
      fa.setName(scan.next().trim());
      switch (scan.next().charAt(0)) { // ELSTER zulässig
      case ELSTER_PRODUKTION_AMT :
        fa.setElsterUebermittlungZulaessig(true);
        fa.setTestUebermittlungZulaessig(false);
        break;
      case ELSTER_TEST_AMT :
        fa.setElsterUebermittlungZulaessig(true);
        fa.setTestUebermittlungZulaessig(true);
        break;
      default:
        fa.setElsterUebermittlungZulaessig(false);
        fa.setTestUebermittlungZulaessig(false);
      }
      switch (scan.next().charAt(0)) { // Voranmeldungen und Dauerfritverlängerung zulässig?
      case INFORMATION_POSITIV :
        fa.setVoranmeldungUebermittlungZulaessig(true);
        break;
      case INFORMATON_NEGATIV :
      default :
        fa.setVoranmeldungUebermittlungZulaessig(false);
        break;
      }
      switch (scan.next().charAt(0)) { // Finanzamt wegfallend?
      case INFORMATION_POSITIV :
        fa.setKuenftigEntfallend(true);
        break;
      case INFORMATON_NEGATIV :
      default :
        fa.setKuenftigEntfallend(false);
        break;
      }
      switch (scan.next().charAt(0)) { // Einkommensteuer zulässig?
      case INFORMATION_POSITIV :
        fa.setEinkommensteuerZulaessig(true);
        break;
      case INFORMATON_NEGATIV :
      default :
        fa.setEinkommensteuerZulaessig(false);
        break;
      }
      scan.close();

      finanzaemter.add(fa);
    }
    return finanzaemter;
  }

  /** @todo Geimeindedaten, Anschriften etc. GEMFA Daten aufnehmen */
}
