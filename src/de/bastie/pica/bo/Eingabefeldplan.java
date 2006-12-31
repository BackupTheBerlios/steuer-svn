/**
 * Eingabefeldplan
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.bo;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.xml.parsers.*;

import de.bastie.pica.bo.ustva.*;
import de.bastie.pica.gui.*;
import org.w3c.dom.*;
import org.xml.sax.*;



/**
 * Eingabefelder können inhaltlich auf bestimmte Werte eingeschränkt werden.
 * Mit Hilfe des Eingabefeldplanes ist es möglich eine Plausibilisierung
 * der Eingaben hiergegen durchzuführen.
 *
 * @author Bastie - Sebastian Ritter
 * @version 1.0
 */
public class Eingabefeldplan {

    /**
     * XML Dokument
     */
    private Document doc;

  /**
   * Erzeugt einen neuen Eingabefeldplan, indem die externe Definition geladen wird.
   * @param art die Steuerart
   * @param zeitraum der zugehörige Zeitraum
   * @param reload Neu laden
   * @return die Eingabefelder
   * @throws ParserConfigurationException XML Parser Fehler
   * @throws SAXException XML Datei inkonsistent
   * @throws IOException Fehler beim Einlesen der Beschreibung
   */
  public HashMap<String, JComponent> createJComponents(final ISteuerart art, final String zeitraum, final boolean reload) throws ParserConfigurationException, SAXException, IOException {
    if (reload) {
      this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.getClass().getResourceAsStream("../Eingabefeldplan.xml")); //$NON-NLS-1$
    }
    return this.createJComponents(art,zeitraum);
  }

  /**
   * Wertet den geladenen Eingabefeldplan aus, erzeugt die zugehörigen
   * Eingabefelder und sichert diese anhand der ID im Eingabefeldplan
   * @param art Steuerart
   * @param zeitraum String
   * @return HashMap
   */
  public HashMap<String, JComponent> createJComponents(final IDatencontainer art, final String zeitraum) {
    HashMap<String, JComponent> eingabefelder = new HashMap<String, JComponent>();
    if (art != null) {
      NodeList eingabeplan = this.doc.getElementsByTagName("Eingabeplan"); //$NON-NLS-1$
      for (int i = 0; i < eingabeplan.getLength(); i++) {
        Node plan = eingabeplan.item(i);

        if (art.getDatenArt().equals(plan.getAttributes().getNamedItem("DatenArt").getTextContent()) && zeitraum.equals(plan.getAttributes().getNamedItem("Zeitraum").getTextContent())) {  //$NON-NLS-1$  //$NON-NLS-2$
          NodeList felderListe = plan.getChildNodes();
          for (int j = 0; j < felderListe.getLength(); j++) {
            Node feld = felderListe.item(j);
            //<Feld id="Name" kurztext="Name" format="A" min="1" max="45" eingabe="pflicht" />
            if ("Feld".equals(feld.getNodeName())) { //$NON-NLS-1$
              final String id = feld.getAttributes().getNamedItem("id").getTextContent(); //$NON-NLS-1$
              final String format = feld.getAttributes().getNamedItem("format").getTextContent();  //$NON-NLS-1$
              final int min = Integer.parseInt(((feld.getAttributes().getNamedItem("min") != null) ? feld.getAttributes().getNamedItem("min").getTextContent() : "0"));  //$NON-NLS-1$  //$NON-NLS-2$
              final int max = Integer.parseInt(((feld.getAttributes().getNamedItem("max") != null) ? feld.getAttributes().getNamedItem("max").getTextContent() : "0"));  //$NON-NLS-1$  //$NON-NLS-2$
              final String eingabe = (feld.getAttributes().getNamedItem("eingabe") != null) ? feld.getAttributes().getNamedItem("eingabe").getTextContent() : "optional";  //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$

              if (feld.getChildNodes().getLength() > 0) {
                Node wert = null;
                for (int k = 0; k < feld.getChildNodes().getLength(); k++) {
                  if ("Wert".equals(feld.getChildNodes().item(k).getNodeName())) {
                    wert = feld.getChildNodes().item(k);
                  }
                }
                if (wert != null) {
                  ArrayList<String> zulaessigeWerte = null;
                  if (wert.getAttributes().getNamedItem("typ") != null) {
                    final String werteTyp = wert.getAttributes().getNamedItem("typ").getTextContent();
                    if ("Liste".equals(werteTyp)) {
                      zulaessigeWerte = new ArrayList<String>();
                      final String delim = wert.getAttributes().getNamedItem("separator").getTextContent();
                      StringTokenizer tokenizer = new StringTokenizer(wert.getTextContent(), delim, false);
                      while (tokenizer.hasMoreElements()) {
                        zulaessigeWerte.add(tokenizer.nextToken());
                      }
                      if (feld.getAttributes().getNamedItem("eingabe") != null &&
                          "pflicht".equals(feld.getAttributes().getNamedItem("eingabe").getTextContent())) {
                        eingabefelder.put(id, EingabeFeldFactory.createEingabefeld(art, id, format, min, max, eingabe, zulaessigeWerte, true));
                      }
                      else {
                        eingabefelder.put(id, EingabeFeldFactory.createEingabefeld(art, id, format, min, max, eingabe, zulaessigeWerte, false));
                      }
                    }
                    else {
                      if (feld.getAttributes().getNamedItem("eingabe") != null &&
                          "pflicht".equals(feld.getAttributes().getNamedItem("eingabe").getTextContent())) {
                        eingabefelder.put(id, EingabeFeldFactory.createEingabefeld(art, id, format, min, max, eingabe, zulaessigeWerte, true));
                      }
                      else {
                        eingabefelder.put(id, EingabeFeldFactory.createEingabefeld(art, id, format, min, max, eingabe, zulaessigeWerte, false));
                      }
                    }
                  }
                  else {
                    eingabefelder.put(id, EingabeFeldFactory.createEingabefeld(art, id, format, min, max, eingabe));
                  }
                }
              }
              else {
                eingabefelder.put(id, EingabeFeldFactory.createEingabefeld(art, id, format, min, max, eingabe));
              }
            }
          }
        }
      }
    }
    return eingabefelder;
  }

  /**
   * Erzeugt die Eingabefelder für die Bearbeitung eines Datenlieferanten
   * @param lieferant Datenlieferant
   * @return HashMap
   */
  public HashMap<String, JComponent> createDatenlieferantJComponent (final IDatenlieferant lieferant) {
    HashMap<String, JComponent> eingabefelder = new HashMap<String, JComponent>();
    try {
      this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.getClass().getResourceAsStream("../Datenlieferant.xml")); //$NON-NLS-1$
      eingabefelder = this.createJComponents(lieferant,"alle");
    }
    catch (final Exception shit) {
      shit.printStackTrace();
    }
    return eingabefelder;
  }
  /**
   * Erzeugt die Eingabefelder für die Bearbeitung eines Mitwirkenden
   * @param mitwirkender Mitwirkender
   * @return HashMap
   */
  public HashMap<String, JComponent> createMitwirkenderJComponent (final Mitwirkender mitwirkender) {
    HashMap<String, JComponent> eingabefelder = new HashMap<String, JComponent>();
    try {
      this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.getClass().getResourceAsStream("./Mitwirkender.xml")); //$NON-NLS-1$
      eingabefelder = this.createJComponents(mitwirkender,"alle");
    }
    catch (final Exception shit) {
      shit.printStackTrace();
    }
    return eingabefelder;
  }

  /**
   * Erzeugt die Eingabekomponenten für den Unternehmer
   *
   * @param unternehmer Unternehmer
   * @return HashMap
   */
  public HashMap<String,JComponent> createUnternehmerJComponent (final Unternehmer unternehmer) {
    HashMap<String, JComponent> eingabefelder = new HashMap<String, JComponent>();
    try {
      this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.getClass().getResourceAsStream("./Unternehmer.xml")); //$NON-NLS-1$
      eingabefelder = this.createJComponents(unternehmer,"alle");//$NON-NLS-1$
    }
    catch (final Exception shit) {
      shit.printStackTrace();
    }
    return eingabefelder;
  }
















  /**
   * @todo REMOVE
   * @param args String[]
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Eingabefeldplan efp = new Eingabefeldplan();
    final UmsatzsteuervoranmeldungImpl art = new UmsatzsteuervoranmeldungImpl();
    art.setDatenLieferant(new DatenlieferantImpl());
    HashMap<String, JComponent> comp = efp.createJComponents(art, "2007");
    JFrame fenster = new JFrame("Test");  //$NON-NLS-1$
    JPanel eingabefelder = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1d, 0d, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
    for (String id : comp.keySet()) {
      gbc.gridx = 0;
      eingabefelder.add(new JLabel(id), gbc);
      gbc.gridx++;
      eingabefelder.add(comp.get(id), gbc);
      gbc.gridy++;
    }
    fenster.add(new JScrollPane(eingabefelder), BorderLayout.CENTER);
    fenster.setSize(600, 400);
    fenster.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    JMenuBar menuBar = new JMenuBar();
    JMenu auswertung = new JMenu("Auswertung");  //$NON-NLS-1$
    menuBar.add(auswertung);
    auswertung.add(new AbstractAction("Eingabewerte ausgeben") {  //$NON-NLS-1$
      public void actionPerformed(final ActionEvent event) {
        System.err.println(art.toXML());
      }
    });
    fenster.setJMenuBar(menuBar);
    fenster.setVisible(true);
  }

}
