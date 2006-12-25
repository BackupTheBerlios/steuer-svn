package de.bastie.pica.ref;

import de.bastie.pica.ref.gui.PicaFrame;
import javax.swing.UIManager;
import javax.swing.ImageIcon;

public class Pica {

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      UIManager.put("Tree.leafIcon", new ImageIcon(Pica.class.getResource("./res/gnome-mime-text-small.png")));
      UIManager.put("Tree.openIcon", new ImageIcon(Pica.class.getResource("./res/gnome-fs-directory-small.png")));
      UIManager.put("Tree.closedIcon", new ImageIcon(Pica.class.getResource("./res/gnome-fs-directory-visiting-small.png")));
      UIManager.put("Tree.expandedIcon", new ImageIcon(Pica.class.getResource("./res/b_minus-small.png")));
      UIManager.put("Tree.collapsedIcon", new ImageIcon(Pica.class.getResource("./res/b_plus-small.png")));
    }
    catch (Exception ignored) {}
    PicaFrame pica = new PicaFrame ();
    pica.setDefaultCloseOperation(pica.EXIT_ON_CLOSE);
    pica.setSize(670,537);
    pica.setVisible(true);
  }
}
