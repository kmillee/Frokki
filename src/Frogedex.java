/**
 * Created: 01/10/2025
 * This class is responsible for creating and displaying the "Frogedex", a frame
 * that displays all the frogs a player owns.
 * The left panel displays a frog's information (species, picture, name, experience
 * level, acquisition date). The right part displays a list of all the frogs the
 * player owns.
 */

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatLineBorder;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;

public class Frogedex extends JFrame {
    private PondSimController controller;

    Frogedex(PondSimController controller) {

        this.controller = controller;
        Utils.setFixedSize(this, 858, 430);
        this.setResizable(false);
        this.setTitle("Frogedex");

        Frog frog = new Frog("media/frog_og.png", "Bibi the Frog", "Frog Species", "01/01/2025");

        JPanel frogInfo = getFrogInfoPanel(frog);
        this.add(frogInfo,  BorderLayout.WEST);
        this.setVisible(true);
        pack();
    }


    private JPanel getFrogInfoPanel(Frog frog){
        // --- Get all elements necessary for the frog info panel
        ImageIcon frogImage = frog.getImage();
        frogImage = Utils.resizeImageIcon(frogImage, 166, 166);

        // -- Labels setup & fonts
        Font fontSpecies = Utils.loadFont("Gaegu" + File.separator + "Gaegu-Regular.ttf", 16);
        Font fontFrogNameTitle = Utils.loadFont("Gaegu" + File.separator + "Gaegu-Regular.ttf", 24);
        Font fontsmall = Utils.loadFont("Gaegu" + File.separator + "Gaegu-Regular.ttf", 14);

        // -- Setup labels
        JLabel speciesLabel = new JLabel(frog.getSpecies());
        speciesLabel.setFont(fontSpecies);
        speciesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(frog.getName());
        nameLabel.setFont(fontFrogNameTitle);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(10,0,0,0));

        JLabel levelLabel = new JLabel("Lvl" + frog.getLevel());
        levelLabel.setFont(fontsmall);

        JLabel xpLabel = new JLabel(frog.getExperience() + "/100XP");
        xpLabel.setFont(fontsmall);

        JLabel acquisitionLabel = new JLabel("Captured: " + frog.getAcquisitionDate());
        acquisitionLabel.setFont(fontsmall);
        acquisitionLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel frogImageLabel = new JLabel(frogImage);

        JProgressBar progressBar = new JProgressBar(0, Constants.EXPERIENCE_THRESHOLD);
        progressBar.setValue(frog.getExperience());
        progressBar.putClientProperty(FlatClientProperties.STYLE, "arc: 20; horizontalSize: 170,10;");

        // --- Image subpanel
        JPanel frogImagePanel = new JPanel();
        Utils.setFixedSize(frogImagePanel, 206, 206);

        int padding = 20;
        Color borderColor = UIManager.getColor("Component.borderColor");
        Border outerBorder = new RoundedBorder(20, 4, borderColor);
        Border paddingBorder = new EmptyBorder(padding, padding, padding, padding);

        frogImagePanel.setBorder(new CompoundBorder(outerBorder, paddingBorder));

        frogImagePanel.add(frogImageLabel);


        // --- Level subpanel
        JPanel levelPanel = new JPanel();
        Utils.setFixedSize(levelPanel, 206, 59);

        Border outerBorderLevel = new RoundedBorder(15, 4, borderColor);
        int paddingHorizontal = 15;
        int paddingVertical = 10;
        Border innerPanningBorder = new EmptyBorder(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal);

        levelPanel.setBorder(new CompoundBorder(outerBorderLevel, innerPanningBorder));
        levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS));

        // Level + xp labels subpanel
        JPanel levelXpPanel = new JPanel();
        Utils.setFixedSize(levelXpPanel, 170, 19);
        levelXpPanel.setLayout(new BoxLayout(levelXpPanel, BoxLayout.X_AXIS));
        levelXpPanel.add(levelLabel);
        levelXpPanel.add(Box.createHorizontalGlue());
        levelXpPanel.add(xpLabel);


        levelPanel.add(levelXpPanel);
        levelPanel.add(progressBar);

        // Acquisition date panel
        JPanel acquisitionPanel = new JPanel();
        acquisitionPanel.setLayout(new BoxLayout(acquisitionPanel, BoxLayout.X_AXIS));
        acquisitionPanel.add(Box.createHorizontalGlue());
        acquisitionPanel.add(acquisitionLabel);

        // --- Put everything in the frogInfoPanel
        JPanel frogInfoPanel = new JPanel();
        Border innerPadding = new EmptyBorder(0,58,0,58);
        frogInfoPanel.setBorder(innerPadding);

        frogInfoPanel.setLayout(new BoxLayout(frogInfoPanel, BoxLayout.Y_AXIS));
        frogInfoPanel.add(speciesLabel);
        frogInfoPanel.add(frogImagePanel);
        frogInfoPanel.add(nameLabel);
        frogInfoPanel.add(levelPanel);

        JPanel totalPanel =  new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
        totalPanel.add(frogInfoPanel);
        totalPanel.add(Box.createVerticalGlue());
        totalPanel.add(acquisitionPanel);

        return totalPanel;
    }
}
