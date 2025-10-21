package menu;

import main.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * HelpDialogTabs - modal help dialog using JTabbedPane.
 *
 * Usage:
 *   Font titleFont = Utils.loadFont(...);
 *   Font bodyFont  = Utils.loadFont(...);
 *   helpButton.addActionListener(e -> new HelpDialogTabs(menuFrame, titleFont, bodyFont).setVisible(true));
 */
public class HelpDialog extends JDialog {

    public HelpDialog(Frame owner) {
        super(owner, "Help", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(8, 8));

        Font bodyFont = Utils.loadFont("Gaegu" + File.separator + "Gaegu-Regular.ttf", 18);
        Font titleFont = Utils.loadFont("Gaegu" + File.separator + "Gaegu-Bold.ttf", 24);

        // top title
        JLabel titleLabel = new JLabel("Help - Frokki", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setBorder(new EmptyBorder(8, 8, 0, 8));
        add(titleLabel, BorderLayout.NORTH);

        // Build tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Introduction", makeScrollPane(makeHtmlPane(
                "<h2>Welcome to Frokki</h2>" +
                        "<p>Open the Pond to collect frogs, or open the Frogedex to view and manage your collection.</p>" +
                        "<p>Use the tabs to navigate topics.</p>"
                , bodyFont)));

        tabs.addTab("Pond", makeScrollPane(makeHtmlPane(
                "<h2>The Pond</h2>" +
                        "<ul>" +
                        "<li>When you open the pond, reeds, lily pads, frogs, or even a crocodile may appear.</li>" +
                        "<li>Frogs spawn only on <b>available lily pads</b>. Rotten pads must be removed.</li>" +
                        "<li>Use the <b>Hand</b> tool to drag rotten lily pads to the bin.</li>" +
                        "<li>Use the <b>Scissors</b> to cut reeds and free space for new pads.</li>" +
                        "<li>If a crocodile appears, use the <b>Bell</b> to scare it away and bring frogs back.</li>" +
                        "</ul>"
                , bodyFont)));

        tabs.addTab("Frogedex", makeScrollPane(makeHtmlPane(
                "<h2>Frogedex</h2>" +
                        "<ul>" +
                        "<li>View all your collected frogs and select one to see detailed info.</li>" +
                        "<li>Summon a frog to the taskbar by clicking <b>Summon</b> or dragging the frog to the taskbar.</li>" +
                        "<li>Left-click a taskbar frog to make it jump, drag/throw to toss it, or right-click to view its info card.</li>" +
                        "</ul>"
                , bodyFont)));

        tabs.addTab("Controls", makeScrollPane(makeHtmlPane(
                "<h2>Controls & Tools</h2>" +
                        "<ul>" +
                        "<li><b>Hand:</b> Drag items (remove rotten lily pads).</li>" +
                        "<li><b>Scissors:</b> Cut reeds to free space.</li>" +
                        "<li><b>Bell:</b> Scare off crocodiles.</li>" +
                        "<li><b>Taskbar Frog:</b> Left-click jump, right-click info card.</li>" +
                        "</ul>"
                , bodyFont)));

        tabs.addTab("Tips", makeScrollPane(makeHtmlPane(
                "<h2>Tips</h2>" +
                        "<ul>" +
                        "<li>Keep several clean lily pads available for better spawning.</li>" +
                        "<li>Trim reeds regularly so lily pads can spawn.</li>" +
                        "<li>Summon your favourite frogs for quick fun.</li>" +
                        "</ul>"
                , bodyFont)));

        add(tabs, BorderLayout.CENTER);
    }

    private JComponent makeHtmlPane(String htmlFragment, Font bodyFont) {
        String html = "<html><head><style>"
                + "p { margin: 6px 0; } li { margin: 4px 0; }"
                + "body { margin: 8px; }"
                + "</style></head><body>"
                + htmlFragment
                + "</body></html>";

        return getJEditorPane(bodyFont, html);
    }

    private static JEditorPane getJEditorPane(Font bodyFont, String html) {
        JEditorPane pane = new JEditorPane("text/html", html);
        pane.setEditable(false);
        pane.setOpaque(false);
        pane.setBorder(new EmptyBorder(12, 12, 12, 12));

        pane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);

        pane.setFont(bodyFont);
        return pane;
    }

    private JScrollPane makeScrollPane(JComponent content) {
        JScrollPane sp = new JScrollPane(content);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }
}
