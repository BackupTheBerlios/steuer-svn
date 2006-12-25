package de.bastie.pica.ref.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.Action;
import javax.swing.event.*;
import javax.swing.text.html.*;
import javax.swing.tree.*;

import de.bastie.pica.bo.*;
import de.bastie.pica.bo.ustva.*;
import de.bastie.pica.ref.gui.mask.*;

public class PicaFrame extends JFrame {

  private JLabel statusZeile = new JLabel("Pica Pica © 2006 Bastie - Sebastian Ritter");
  private JTree steuerarten = new JTree();
  private JTree hilfe = new JTree();
  private JMenuBar menuBar = new JMenuBar();
  private JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
  private JPanel rechts;
  private JPanel bearbeitung = new JPanel(new BorderLayout());
  private ISteuerart aktuelleSteuerart;
  private JTextPane hilfeText;
  private JSplitPane vertikalLinks;

  private IDatenlieferant boDatenlieferant;
  private UStVAMask masken;

  private DefaultTreeModel steuerartenTreeModel, hilfeTreeModel;

  public PicaFrame() {
    super("Pica Pica");

    this.boDatenlieferant = new DatenlieferantImpl();
    this.masken = new UStVAMask(aktuelleSteuerart, 2007);


    JPanel contentPane = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1d, 0d, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
    contentPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 2, 0));
    this.setContentPane(contentPane);


    this.menuBar.add(new JMenu("Datei"));
    this.menuBar.add(new JMenu("Einstellungen"));
    JComponent fillMenu = new JComponent(){};
    this.menuBar.add(fillMenu);
    JMenu about = new JMenu("Über");
    about.add(new AbstractAction ("Info") {
      public void actionPerformed(ActionEvent e) {
        DialogFactory.showAboutDialog (PicaFrame.this);
      }
    });
    this.menuBar.add (about);
    this.setJMenuBar(this.menuBar);


    this.toolBar.setFloatable(false);
    this.toolBar.setRollover(true);
    AbstractAction hallo = new AbstractAction("Senden", new ImageIcon(this.getClass().getResource("../res/senden.png"))) {
      public void actionPerformed(ActionEvent e) {
        try {
          StringBuilder xml = new StringBuilder();
          xml.append (aktuelleSteuerart.toXML());
          System.err.println(new Exception().getStackTrace()[0].getClassName() + "\n" + aktuelleSteuerart.toXML());

          final String ENCODING = "ISO-8859-15";
          byte[] xml_ISO_8859_15 = xml.toString().getBytes(ENCODING);
          JOptionPane.showMessageDialog(PicaFrame.this,"Datenversendung erfolgreich.\n"+new String (xml_ISO_8859_15),"Datenübermittlung erfolgreich", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception ex) {
          JOptionPane.showMessageDialog(PicaFrame.this,"Datenversendung gescheitert.\n<small>"+ex.getMessage()+"</small>","Fehler bei der Datenübermittlung", JOptionPane.ERROR_MESSAGE);
        }
      }
    };
    hallo.putValue(Action.SHORT_DESCRIPTION, hallo.getValue(Action.NAME));
    this.toolBar.add(hallo);
    this.add(toolBar, gbc);


    JSeparator toolBarSeparator = new JSeparator(JSeparator.HORIZONTAL);
    toolBarSeparator.setMinimumSize(new Dimension(0, 8));
    gbc.gridy++;
    this.add(toolBarSeparator, gbc);

    JPanel splitStatus = new JPanel(new BorderLayout());
    splitStatus.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    gbc.gridy++;
    gbc.weighty = 1d;
    gbc.fill = GridBagConstraints.BOTH;
    this.add(splitStatus, gbc);

    JPanel links = new JPanel(new BorderLayout());
    rechts = new JPanel(new BorderLayout());
    rechts.add(this.bearbeitung, BorderLayout.CENTER);
    JSplitPane horizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, links, rechts);
    horizontal.setDividerLocation(180);
    horizontal.setDividerSize(8);
    splitStatus.add(horizontal, BorderLayout.CENTER);
    JScrollPane steuerartenAuswahl = new JScrollPane(steuerarten);
    steuerartenAuswahl.setBorder(BorderFactory.createEmptyBorder());
    JScrollPane hilfeAuswahl = new JScrollPane(hilfe);
    hilfeAuswahl.setBorder(BorderFactory.createEmptyBorder());
    vertikalLinks = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, steuerartenAuswahl, hilfeAuswahl);
    vertikalLinks.setBorder(BorderFactory.createEmptyBorder());
    vertikalLinks.setDividerSize(8);
    vertikalLinks.setDividerLocation(333);

    links.add(vertikalLinks, BorderLayout.CENTER);


    this.statusZeile.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
    splitStatus.add(this.statusZeile, BorderLayout.SOUTH);

    contentPane.setFocusable(true);
    contentPane.requestFocusInWindow();


    this.initTree();
  }


  protected void initTree() {
    // Steuerarten
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Steuerarten", true);
    this.steuerartenTreeModel = new DefaultTreeModel(root);

    final DefaultMutableTreeNode ustva = new DefaultMutableTreeNode(masken, true);
    root.add(ustva);

    this.steuerarten.setModel(this.steuerartenTreeModel);
    this.steuerarten.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(final TreeSelectionEvent e) {
        DefaultMutableTreeNode tn = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
        if (tn == ustva) {
          for (String name : masken.getEingabeKomponentenTitel()) {
            tn.add(new DefaultMutableTreeNode(name, false));
          }
          aktuelleSteuerart = new UmsatzsteuervoranmeldungImpl();
          DatenlieferantImpl datenlieferant = new DatenlieferantImpl();
          ((UmsatzsteuervoranmeldungImpl) aktuelleSteuerart).setDatenLieferant(datenlieferant);
          masken = new UStVAMask(aktuelleSteuerart, 2007);
        }
        // Die einzelnen Blätter
        else if (tn.isLeaf()) {
          TreeNode parent = tn;
          do {
            parent = parent.getParent();
          }
          while (tn.getClass() != ustva.getClass()); /** @todo Abhängigkeit von UStVA auflösen */
           JComponent eingabe = masken.getComponentByTitle(tn.getUserObject().toString());
          JScrollPane eingabeMaske = new JScrollPane(eingabe);
          eingabeMaske.setBorder(BorderFactory.createEmptyBorder());
          bearbeitung.removeAll();
          bearbeitung.add(eingabeMaske, BorderLayout.CENTER);
          bearbeitung.updateUI();
        }
      }
    });

    // Hilfe
    DefaultMutableTreeNode rootHilfe = new DefaultMutableTreeNode("Hilfe", true);
    this.hilfeTreeModel = new DefaultTreeModel(rootHilfe);

    this.hilfe.setModel(this.hilfeTreeModel);
    this.hilfe.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(final TreeSelectionEvent e) {
        JPanel oben = new JPanel(new BorderLayout());
        oben.add(bearbeitung, BorderLayout.CENTER);
        hilfeText = new JTextPane();
        hilfeText.setEditorKit(new HTMLEditorKit());
        hilfeText.setText("<body bgcolor='white'><h1>Pica Pica</h1><p>Copyright &copy;2006 Bastie - Sebastian Ritter</p></body>");
        JScrollPane jspHelp = new JScrollPane(hilfeText);
        jspHelp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, oben, jspHelp);
        jsp.setDividerLocation(vertikalLinks.getDividerLocation());
        jsp.setDividerSize(vertikalLinks.getDividerSize());
        rechts.removeAll();
        rechts.add(jsp, BorderLayout.CENTER);
        rechts.updateUI();
      }
    });
  }

}
