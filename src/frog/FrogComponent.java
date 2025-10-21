package frog;

import UI.RoundedBorder;
import com.formdev.flatlaf.FlatClientProperties;
import frog.states.IdleState;
import main.Constants;
import main.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FrogComponent extends JComponent implements MouseListener, MouseMotionListener {
    private final Frog frog;
    private FrogState currentState;
    private int anchorX;
    private int anchorY;
    private int direction = 1; // Use to get the direction of the frog, supposed to be either 1 or -1
    Timer experienceTimer;
    private final PhysicsBody physicsBody;
    private boolean displayFrogInfo = false;

    // -- layout related
    private JPanel frogInfoPanel;
    private JLabel levelLabel;
    private JLabel xpLabel;
    private JProgressBar progressBar;

    // -- Listener
    private List<ChangeListener> listeners = new ArrayList<>(); // Mainly used to communicate to FrogBar that frog info panel should be updated

    public FrogComponent(Frog frog) {
        this.frog = frog;
        this.physicsBody = new PhysicsBody();
        frogInfoPanel = createFrogInfoPanel();

        setSize(Constants.TASKBAR_FROG_SIZE,Constants.TASKBAR_FROG_SIZE);
        setOpaque(false);

        addMouseListener(this);
        addMouseMotionListener(this);

        setState(new IdleState(this));

        physicsBody.addChangeListener(e -> {
            if(!physicsBody.isActive()){
                // Ground was reached, we transition back to idle state
                if(!(currentState instanceof IdleState)){
                    setState(new IdleState(this));
                }
            }

            Container parent =  getParent();
            if(parent != null){
                anchorX = physicsBody.getX();
                anchorY = parent.getHeight() - physicsBody.getY() - getHeight();
            }
            repaint();
        });

        experienceTimer = new Timer(5000, e -> {
            frog.increaseExperience();
        });
        experienceTimer.setRepeats(true);
        experienceTimer.start();

        frog.addChangeListeners(e -> {
            updateFrogInfoPanel();
            repaint();
        });


    }

    // ---- Setters & getters
    public JPanel getFrogInfoPanel() {
        return frogInfoPanel;
    }

    public void setFrogInfoPanelVisible(boolean visible) {
        frogInfoPanel.setVisible(visible);
    }
    public void setState(FrogState state) {
        if(currentState != null) currentState.exitState();
        this.currentState = state;
        currentState.enterState();
    }

    public Frog getFrog(){
        return frog;
    }

    public void setAnchor(int x, int y){
        anchorX = x;
        anchorY = y;
    }
    public PhysicsBody getPhysicsBody(){
        return this.physicsBody;
    }

    /**
     * Updates the location of the frog component based on its anchor point.
     * There is clamping to ensure the frog does not go out of bounds of the parent container.
     * @param parent The parent container of the frog component. Must not be null.
     */
    public void updateLocationFromAnchor(Container parent){
        int topLeftX = anchorX;
        int topLeftY = parent.getHeight() - anchorY - getHeight();

        topLeftX = Math.max(0, Math.min(topLeftX, parent.getWidth() - getWidth()));
        topLeftY = Math.max(0, Math.min(topLeftY, parent.getHeight() - getHeight()));
        setLocation(topLeftX, topLeftY);
    }

    /**
     * Sets the bottom-left anchor point of the frog component relative to its parent container.
     * @param x The x-coordinate of the bottom-left anchor point.
     * @param yFromBottom The y-coordinate of the bottom-left anchor point measured from
     * the bottom of the parent container. If the value is 0, the frog is at the bottom of
     * the parent container.
     */
    public void setBottomLeftAnchor(int x, int yFromBottom){
        this.anchorX = x;
        this.anchorY = yFromBottom;
        Container parent = getParent();
        if(parent != null){
            updateLocationFromAnchor(parent);
        }
    }

    public void setDirection(int direction){
        this.direction = direction;
    }

    private void setDisplayFrogInfo(boolean displayFrogInfo){
        this.displayFrogInfo = displayFrogInfo;
        notifyChangeListener(); // Notifies FrogBar
    }

    public void invertDisplayFrogInfo(){
        setDisplayFrogInfo(!displayFrogInfo);
    }

    public boolean getDisplayFrogInfo(){
        return displayFrogInfo;
    }

    // --- Layout

    public JPanel createFrogInfoPanel() {
        Frog frog = getFrog();
        JPanel displayFrogInfo = new JPanel();
        displayFrogInfo.setLayout(new BoxLayout(displayFrogInfo, BoxLayout.Y_AXIS));

        Color borderColor = UIManager.getColor("Component.borderColor");
        Color backgroundColor = UIManager.getColor("Panel.background");
        Border outerBorder = new RoundedBorder(20, 4, borderColor, backgroundColor);
        Border paddingBorder = new EmptyBorder(10, 20, 10, 8);
        displayFrogInfo.setBorder(new CompoundBorder(outerBorder, paddingBorder));

        Font fontFrogNameTitle = Utils.loadFont("Gaegu" + File.separator + "Gaegu-Regular.ttf", 20);
        Font fontsmall = Utils.loadFont("Gaegu" + File.separator + "Gaegu-Regular.ttf", 14);

        // --- Buttons
        JButton unsummonButton = new JButton("Unsummon");
        unsummonButton.setFont(fontsmall);
        unsummonButton.putClientProperty(FlatClientProperties.STYLE,
                "arc: 20;" +
                        "background: #f6685e;" +
                        "disabledBackground: #f6685e;" +
                        "focusedBackground: #f6685e;");

        unsummonButton.addActionListener(e -> frog.setActive(false));
        unsummonButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton closeButton = new JButton("X");
        closeButton.setFont(fontsmall);
        closeButton.putClientProperty(FlatClientProperties.STYLE,
                "arc: 20;" +
                        "background: #f6685e;" +
                        "disabledBackground: #f6685e;" +
                        "focusedBackground: #f6685e;");
        closeButton.addActionListener(e -> invertDisplayFrogInfo());
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // --- Labels
        JLabel frogName =  new JLabel(frog.getName());
        frogName.setFont(fontFrogNameTitle);

        levelLabel = new JLabel("Lvl" + frog.getLevel());
        levelLabel.setFont(fontsmall);

        xpLabel = new JLabel(frog.getExperience() + "/100XP");
        xpLabel.setFont(fontsmall);

        progressBar = new JProgressBar(0, Constants.EXPERIENCE_THRESHOLD);
        progressBar.setValue(frog.getExperience());
        progressBar.putClientProperty(FlatClientProperties.STYLE, "arc: 20; horizontalSize: 125,10;");

        // --- Level subpanel
        JPanel levelPanel = new JPanel();
        Utils.setFixedSize(levelPanel, 155, 59);


        Border outerBorderLevel = new RoundedBorder(15, 4, borderColor);
        int paddingHorizontal = 15;
        int paddingVertical = 10;
        Border innerPanningBorder = new EmptyBorder(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal);

        levelPanel.setBorder(new CompoundBorder(outerBorderLevel, innerPanningBorder));
        levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS));

        // Level + xp labels subpanel
        JPanel levelXpPanel = new JPanel();
        Utils.setFixedSize(levelXpPanel, 125, 19);
        levelXpPanel.setLayout(new BoxLayout(levelXpPanel, BoxLayout.X_AXIS));
        levelXpPanel.add(levelLabel);
        levelXpPanel.add(Box.createHorizontalGlue());
        levelXpPanel.add(xpLabel);

        levelPanel.add(levelXpPanel);
        levelPanel.add(progressBar);


        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBorder(new  EmptyBorder(0, 0, 10, 0));
        topPanel.add(frogName);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(closeButton);

        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        levelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        unsummonButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        displayFrogInfo.add(topPanel);
        displayFrogInfo.add(levelPanel);
        displayFrogInfo.add(Box.createVerticalGlue());
        displayFrogInfo.add(unsummonButton);


        int width = Math.max(levelXpPanel.getPreferredSize().width + 20, frogName.getPreferredSize().width + closeButton.getPreferredSize().width);
        displayFrogInfo.setSize(width + 60, 156);
        displayFrogInfo.setOpaque(false);
        return displayFrogInfo;
    }

    private void updateFrogInfoPanel(){
        if(levelLabel != null) levelLabel.setText("Lvl" + frog.getLevel());
        if(xpLabel != null) xpLabel.setText(frog.getExperience() + "/100XP");
        if(progressBar != null) progressBar.setValue(frog.getExperience());
    }


    // --- Mouse events
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        getFrog().getSound().play();
        currentState.mousePressed(e);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        currentState.mouseReleased(e);
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {
        currentState.mouseDragged(e);
    }
    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the frog
        ImageIcon frogImage = currentState.getCurrentImage();
        Dimension frameSize = currentState.getCurrentSize();

        setSize(frameSize);
        Container parent = getParent();
        if (parent != null) {
            updateLocationFromAnchor(parent);
        }

        if (direction > 0) {
            // Draw the image as is
            g2d.drawImage(frogImage.getImage(), 0, 0, getWidth(), getHeight(), null);
        } else {
            // Flip the image horizontally and adjust its size
            g2d.drawImage(frogImage.getImage(), getWidth(), 0, 0, getHeight(), 0, 0, frogImage.getIconWidth(), frogImage.getIconHeight(), null);
        }

        g2d.dispose();
    }

    public void stopTimer(){
        experienceTimer.stop();
    }

    public void addChangeListener(ChangeListener changeListener){
        listeners.add(changeListener);
    }

    private void notifyChangeListener(){
        for(ChangeListener listener : listeners){
            listener.stateChanged(new ChangeEvent(this));
        }
    }
}