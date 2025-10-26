package UI;

import main.Constants;
import main.Utils;
import pond.PondController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Toolbox represents a floating tool panel for interacting with the pond.
 * <p>
 * Responsibilities:
 * - Provides different tools for interacting with pond objects:
 *   (NET, GRAB, GRAB_WHILE, SCISSORS, BELL).
 * - Tracks the currently selected tool.
 * - Updates the cursor icon when tools are selected.
 * - Handles grabbing and placing tiles.
 * - Provides access to the "bin" area for drag-and-drop operations.
 */
public class Toolbox implements MouseListener{

    /** Enum representing the available tools */
    public enum Tool {
        NONE,           // No tool selected
        NET,            // Catch frogs
        GRAB,           // Pick up tiles
        GRAB_WHILE,     // Actively dragging a grabbed tile
        SCISSORS,       // Cut reeds
        BELL            // Scare crocodiles
    }

    private Tool currentTool = Tool.NET;
    private JFrame frame;
    public JButton grabButton;
    public PondController controller;

    public Toolbox(PondController controller) {
        this.controller = controller;
        setUpFrame();
        setUpTools();
        installUI();
    }

    private void installUI(){
        frame.addMouseListener(this);
        frame.requestFocus();
    }
    public void setUpFrame(){
        frame = new JFrame("Toolbox");
        frame.setPreferredSize(new Dimension(250,100));
        frame.setLocationRelativeTo(null);

        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setFocusable(true);
        frame.requestFocus();
        frame.pack();
    }


    public void setUpTools(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.pink);

        // NET tool button
        JButton netButton = addButton(Constants.NET_IMG, Tool.NET);
        netButton.setToolTipText("Net: click to catch frogs!");
        panel.add(netButton);

        // SCISSORS tool button
        JButton scissorsButton = addButton(Constants.SCISSORS_IMG, Tool.SCISSORS);
        scissorsButton.setToolTipText("Scissors: click to cut off reeds.");
        panel.add(scissorsButton);

        // GRAB tool button
        grabButton = addButton(Constants.GRAB_BEFORE_IMG, Tool.GRAB);
        grabButton.setToolTipText("Grab: drag and drop rotten lily pads into the bin.");
        panel.add(grabButton);

        // BELL tool button
        JButton bellButton = addButton(Constants.BELL_IMG, Tool.BELL);
        bellButton.setToolTipText("Bell: shake it next to crocodiles to scare them away!");
        panel.add(bellButton);

        frame.add(panel, BorderLayout.CENTER);
    }

    public JButton addButton(ImageIcon icon, Tool tool){
        JButton newButton = new JButton();
        newButton.setPreferredSize(new Dimension(50,50));
        newButton.setIcon(Utils.resizeKeepingRatio(icon, 45,45));

        newButton.addActionListener(e -> {
            setCurrentTool(tool);
            Utils.setCustomCursor(getToolIcon(tool), frame.getContentPane());
            if (controller != null) {
                controller.updateToolButtonIcon(tool);
            }
        });
        return newButton;
    }


    // ---- UTILITIES ----
    public void show(){
        frame.setVisible(true);
    }

    public Tool getCurrentTool(){
        return currentTool;
    }

    /**
     * Sets the current tool and updates the grabButton icon if necessary.
     * @param currentTool Tool to activate
     */
    public void setCurrentTool(Tool currentTool){
        this.currentTool = currentTool;
        if (currentTool == Tool.GRAB_WHILE){
            // Show bin icon when actively grabbing
            grabButton.setIcon(Utils.resizeKeepingRatio(Constants.BIN_IMG, 45,45));

        }
        else{
            grabButton.setIcon(Utils.resizeKeepingRatio(Constants.GRAB_BEFORE_IMG, 45,45));
        }
    }

    public JFrame getFrame(){
        return frame;
    }

    public ImageIcon getToolIcon(Tool tool){
        return switch (tool) {
            case Tool.BELL -> Constants.BELL_IMG;
            case Tool.SCISSORS -> Constants.SCISSORS_IMG;
            case Tool.GRAB -> Constants.GRAB_BEFORE_IMG;
            case Tool.GRAB_WHILE ->  Constants.GRAB_WHILE_IMG;
            case Tool.NET -> Constants.NET_IMG;
            default -> null;
        };
    }

    /**
     * Returns a rectangle representing the bin area for dropped tiles.
     * Used to detect if a grabbed tile was dropped into the bin.
     */
    public Rectangle getBinRectangle(){
        Point binPos = grabButton.getLocationOnScreen();
        return new Rectangle(binPos.x, binPos.y  - grabButton.getHeight() , grabButton.getWidth(), grabButton.getHeight());
    }

    // ---- LISTENERS ----
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        if (getCurrentTool() != Tool.NONE) {
            Utils.setCustomCursor(getToolIcon(getCurrentTool()), frame.getContentPane());
        }
    }

    @Override public void mouseExited(MouseEvent e) {}



}
