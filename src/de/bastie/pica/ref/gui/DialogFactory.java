/**
 * DialogFactory
 * @licence MPL, GPL, LGPL
 */
package de.bastie.pica.ref.gui;

import java.awt.*;
import java.lang.management.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.html.*;

public class DialogFactory {
  /**
   * showAboutDialog
   *
   * @param picaFrame PicaFrame
   */
  public static void showAboutDialog (final JFrame frame) {
    JDialog dialog = new JDialog(frame);
    dialog.setTitle("Über - "+frame.getTitle());
    dialog.setModal(true);

    OperatingSystemMXBean mbOS = ManagementFactory.getOperatingSystemMXBean();
    MemoryMXBean mbMemory = ManagementFactory.getMemoryMXBean();
    RuntimeMXBean mbRuntime = ManagementFactory.getRuntimeMXBean();

    JTextPane ueber = new JTextPane();
    ueber.setEditorKit(new HTMLEditorKit());
    StringBuilder text = new StringBuilder();
    text.append("<body>")
        .append("<h1>System Informationen</h1>")
        .append("<h2>Betriebssystem</h2>")
        .append("<table border='0'>")
        .append("<tr width='30%'><td><strong>Name</strong></td><td>").append(mbOS.getName()).append("</td></tr>")
        .append("<tr><td><strong>Version</strong></td><td>").append(mbOS.getVersion()).append("</td></tr>")
        .append("<tr><td><strong>Architektur</strong></td><td>").append(mbOS.getArch()).append("</td></tr>")
        .append("<tr><td><strong>Prozessoren</strong></td><td>").append(mbOS.getAvailableProcessors()).append("</td></tr>")
        .append("</table>")
        .append("<h2>Speicher</h2>")
        .append("<table border='0'>")
        .append("<tr width='30%'><td><strong>System Speicher</strong></td><td>").append(mbMemory.getNonHeapMemoryUsage()).append("</td></tr>")
        .append("<tr><td><strong>Anwendungsspeicher</strong></td><td>").append(mbMemory.getHeapMemoryUsage()).append("</td></tr>")
        .append("</table>")
        .append("<h2>Laufzeitumgebung</h2>")
        .append("<table border='0'>")
        .append("<tr width='30%'><td><strong>Boot Suchpfad</strong></td><td>").append(mbRuntime.getBootClassPath()).append("</td></tr>")
        .append("<tr><td><strong>Anwendungssuchpfad</strong></td><td>").append(mbRuntime.getClassPath()).append("</td></tr>")
        .append("<tr><td><strong>DLL Suchpfad</strong></td><td>").append(mbRuntime.getLibraryPath()).append("</td></tr>")
        .append("<tr><td><strong>Anwendungsstart</strong></td><td>").append(new Date (mbRuntime.getStartTime())).append("</td></tr>")
        .append("</table>")
        .append("</body>");
    ueber.setText(text.toString());
    dialog.add(new JScrollPane(ueber), BorderLayout.CENTER);
    dialog.setSize(400, 300);
    dialog.setVisible(true);
  }

  /**
   * showMessageDialog
   *
   * @param pane JPanel
   * @param string String
   * @param string1 String
   * @param i int
   */
  public static void showMessageDialog(Component parentComponent, Object message, String title, int messageTyp) {
    JOptionPane pane = new JOptionPane(message, messageTyp);
    JDialog dialog = pane.createDialog(parentComponent, title);
    dialog.setVisible(true);
  }
}
