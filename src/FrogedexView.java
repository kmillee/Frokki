import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FrogedexView extends JPanel {
    private JPanel frogListPanel;
    private JPanel frogInfoPanel;
    private Frogedex frogedex;
    private JPanel selectedFrogCard;

    public FrogedexView() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        Utils.setFixedSize(this, 858, 430);
    }
    public void installUI(Frogedex frogedex) {
        this.frogedex = frogedex;
        List<Frog> frogs = frogedex.getFrogs();

        // Content setup
        Frog frog = frogs.isEmpty() ? null : frogs.getFirst();
        frogInfoPanel = getFrogInfoPanel(frog);

        frogListPanel = getFrogListPanel(frogs);

        JPanel separationPanel = new JPanel();
        separationPanel.setBackground(UIManager.getColor("Component.borderColor"));
        Utils.setFixedSize(separationPanel,5, 430);

        this.add(frogInfoPanel);
        this.add(separationPanel);
        this.add(frogListPanel);
    }

    public void updateFrogInfo(Frog frog){
        remove(frogInfoPanel);
        frogInfoPanel = getFrogInfoPanel(frog);
        add(frogInfoPanel, 0);
        revalidate();
        repaint();
    }

    private JPanel getFrogInfoPanel(Frog frog){
        JPanel frogInfoPanel =  new JPanel();
        frogInfoPanel.setLayout(new BoxLayout(frogInfoPanel, BoxLayout.Y_AXIS));

        if(frog == null) {
            Utils.setFixedSize(frogInfoPanel, 322, 430);
            return frogInfoPanel;
        }
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
        acquisitionLabel.setBorder(new EmptyBorder(0,0,0,23));

        JLabel frogImageLabel = new JLabel(frogImage);

        JProgressBar progressBar = new JProgressBar(0, Constants.EXPERIENCE_THRESHOLD);
        progressBar.setValue(frog.getExperience());
        progressBar.putClientProperty(FlatClientProperties.STYLE, "arc: 20; horizontalSize: 170,10;");

        // -- Button
        JButton summonButton = new JButton("Summon");
        summonButton.setFont(fontsmall);
        summonButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        summonButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frog.setActive(true);
            }
        });

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

        // Acquisition date panel + summon button
        JPanel acquisitionPanel = new JPanel();
        acquisitionPanel.setLayout(new BoxLayout(acquisitionPanel, BoxLayout.X_AXIS));
        acquisitionPanel.setBorder(new EmptyBorder(0, 20,10,0));

        acquisitionPanel.add(summonButton);
        acquisitionPanel.add(Box.createHorizontalGlue());
        acquisitionPanel.add(acquisitionLabel);

        // --- Put everything in the frogInfoPanel
        JPanel frogInfo = new JPanel();
        Border innerPadding = new EmptyBorder(0,58,0,58);
        frogInfo.setBorder(innerPadding);

        frogInfo.setLayout(new BoxLayout(frogInfo, BoxLayout.Y_AXIS));
        frogInfo.add(speciesLabel);
        frogInfo.add(frogImagePanel);
        frogInfo.add(nameLabel);
        frogInfo.add(levelPanel);


        frogInfoPanel.add(frogInfo);
        frogInfoPanel.add(Box.createVerticalGlue());
        frogInfoPanel.add(acquisitionPanel);

        return frogInfoPanel;
    }
    /**
     * Creates a frog card panel that is used to display a frog's picture and name
     * @return
     */
    private JPanel getFrogCardPanel(Frog frog) {
        // Load and resize image
        ImageIcon frogImage = frog.getImage();
        frogImage = Utils.resizeImageIcon(frogImage, 82, 82);
        JLabel frogImageLabel = new JLabel(frogImage);
        frogImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create label for frog's name
        JLabel frogNameLabel = new JLabel(frog.getName());
        frogNameLabel.setFont(Utils.loadFont("Gaegu" + File.separator + "Gaegu-Regular.ttf", 14));
        frogNameLabel.setHorizontalAlignment(JLabel.CENTER);
        frogNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        frogNameLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Panel for inner frog card
        JPanel frogCard = new JPanel();
        frogCard.setLayout(new BoxLayout(frogCard, BoxLayout.Y_AXIS));

        // following line from https://github.com/JFormDesigner/FlatLaf/issues/367
        // Set border based on frog selected
        if(frog.equals(frogedex.getSelectedFrog())){
            frogCard.putClientProperty(FlatClientProperties.STYLE,
                    "[light]background: tint(@background,50%);" +
                    "[dark]background: shade(@background,15%);" +
                    "[light]border: 16,16,16,16,#ADD8E6,,20;" +
                    "[dark]border: 16,16,16,16,#5F9EA0,,20;"
            );
        }
        else {
            frogCard.putClientProperty(FlatClientProperties.STYLE,
                    "[light]background: tint(@background,50%);" +
                            "[dark]background: shade(@background,15%);" +
                            "[light]border: 16,16,16,16,shade(@background,10%),,20;" +
                            "[dark]border: 16,16,16,16,tint(@background,10%),,20;"
            );
        }

        // Add image and name
        frogCard.add(frogImageLabel);
        frogCard.add(frogNameLabel);
        Utils.setFixedSize(frogCard, 108, 131);

        frogCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(selectedFrogCard != null){
                    selectedFrogCard.putClientProperty(FlatClientProperties.STYLE,
                            "[light]background: tint(@background,50%);" +
                            "[dark]background: shade(@background,15%);" +
                            "[light]border: 16,16,16,16,shade(@background,10%),,20;" +
                            "[dark]border: 16,16,16,16,tint(@background,10%),,20;"
                    );
                }
                selectedFrogCard = frogCard;
                selectedFrogCard.putClientProperty(FlatClientProperties.STYLE,
                        "[light]background: tint(@background,50%);" +
                        "[dark]background: shade(@background,15%);" +
                        "[light]border: 16,16,16,16,#ADD8E6,,20;" +
                        "[dark]border: 16,16,16,16,#5F9EA0,,20;"
                );

                // Notify controller about modification in selection
                frogedex.selectFrog(frog);
            }
        });

        frogCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return frogCard;
    }

    private JPanel getFrogListPanel(java.util.List<Frog> frogs) {
        JPanel cardPanel = new JPanel(new CardLayout());
        Utils.setFixedSize(cardPanel, 528, 430);
        int frogsPerPage = 6;
        int totalPages = (int) Math.ceil((double) frogs.size() / frogsPerPage);

        for(int pageIndex = 0 ; pageIndex < totalPages ; pageIndex++) {
            JPanel page = getFrogListPagePanel(frogs, pageIndex,  frogsPerPage);
            cardPanel.add(page, "Page " + pageIndex);
        }

        // Setup navigation layout
        Font buttonFont = Utils.loadFont("Gaegu" + File.separator + "Gaegu-Bold.ttf", 20);
        JButton prevButton = new JButton("<");
        prevButton.setFont(buttonFont);
        prevButton.setMargin(new Insets(5, 0, 0, 0));
        JButton nextButton = new JButton(">");
        nextButton.setFont(buttonFont);
        nextButton.setMargin(new Insets(5, 0, 0, 0));

        prevButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setOpaque(false);
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(prevButton);
        navigationPanel.add(Box.createHorizontalStrut(5));
        navigationPanel.add(nextButton);
        navigationPanel.add(Box.createHorizontalGlue());

        // Make navigation functional
        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
        final int[] currentPage = {0}; // track selected page index -> final int[] because it is necessary in lambda expressions


        // Method to update enabled state of a button
        Runnable updateButtonStates = () -> {
            prevButton.setEnabled(currentPage[0]>0);
            nextButton.setEnabled(currentPage[0]<totalPages-1);
        };

        prevButton.addActionListener(e -> {
            if(currentPage[0] > 0){
                currentPage[0]--;
                cardLayout.show(cardPanel, "Page " + currentPage[0]);
                updateButtonStates.run();
            }
        });

        nextButton.addActionListener(e -> {
            if(currentPage[0] < totalPages - 1){
                currentPage[0]++;
                cardLayout.show(cardPanel, "Page " + currentPage[0]);
                updateButtonStates.run();
            }
        });

        updateButtonStates.run();

        // Encapsulate list and navigation in a single panel
        JPanel frogListPanel = new JPanel();
        frogListPanel.setLayout(new BorderLayout());
        frogListPanel.setOpaque(false);
        frogListPanel.add(cardPanel,  BorderLayout.CENTER);
        frogListPanel.add(navigationPanel, BorderLayout.SOUTH);

        return frogListPanel;
    }

    /**
     * Creates a subpage for the list of all frogs
     * @param frogs list of frogs
     * @param pageIndex index of the page to display
     * @param frogsPerPage number of frogs to display in a page
     * @return list of frogs page
     */
    private JPanel getFrogListPagePanel(List<Frog> frogs, int pageIndex, int frogsPerPage) {
        JPanel page = new JPanel();
        Utils.setFixedSize(page, 368, 285);
        JPanel cardList = new JPanel(new GridLayout(2, 3, 22,22));
        cardList.setOpaque(false);

        // Add up to 6 frogs to the current page
        for(int i = pageIndex * frogsPerPage; i < Math.min((pageIndex+1) * frogsPerPage, frogs.size()); i++){
            Frog frog =  frogs.get(i);
            JPanel frogCard = getFrogCardPanel(frog);
            cardList.add(frogCard);
        }
        // Fills up empty slots
        while(cardList.getComponentCount() < frogsPerPage){
            cardList.add(new JPanel());
        }

        page.add(cardList);
        return page;
    }
}
