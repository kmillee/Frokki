package UI;

import main.Constants;
import main.Utils;
import pond.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Toolbox implements MouseListener, MouseMotionListener {


    public enum Tool {
        NONE,
        NET,
        GRAB,
        GRAB_WHILE, // when actively grabbing smth
        SCISSORS,
        BELL
    }

    private Tool currentTool = Tool.NONE;
    private JFrame frame;

    public JButton grabButton; //image updated during grab
    public Tile grabbedTile;

    public Toolbox(){
        setUpFrame();
        setUpTools();
        installUI();
    }

    private void installUI(){
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        frame.requestFocus();
    }
    public void setUpFrame(){
        frame = new JFrame("UI.Toolbox");
        frame.setPreferredSize(new Dimension(250,100));
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
//        frame.setUndecorated(true);
        frame.setFocusable(true);
        frame.requestFocus();
        frame.pack();
    }


    public void setUpTools(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.pink);

        // Add tools button
        JButton netButton = addButton(Constants.NET_IMG, Tool.NET);
        panel.add(netButton);

        JButton scissorsButton = addButton(Constants.SCISSORS_IMG, Tool.SCISSORS);
        panel.add(scissorsButton);

        grabButton = addButton(Constants.GRAB_BEFORE_IMG, Tool.GRAB);
        panel.add(grabButton);

        JButton bellButton = addButton(Constants.BELL_IMG, Tool.BELL);
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
        });

        return newButton;
    }


    // ---- UTILITIES ----
    public void show(){
        frame.setVisible(true);
    }

    public void close(){
        frame.setVisible(false);
    }

    public Tool getCurrentTool(){
        return currentTool;
    }

    public void setCurrentTool(Tool currentTool){
        this.currentTool = currentTool;
        if (currentTool == Tool.GRAB_WHILE){
            grabButton.setIcon(Constants.BIN_IMG);
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

    public Rectangle getBinRectangle(){
        Point binPos = grabButton.getLocationOnScreen();
        Rectangle binRect = new Rectangle(binPos.x, binPos.y  - grabButton.getHeight() /2, grabButton.getWidth(), grabButton.getHeight());
        return binRect;
    }

    // ---- LISTENERS ----
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (getCurrentTool() != Tool.NONE) {
            Utils.setCustomCursor(getToolIcon(getCurrentTool()), frame.getContentPane());
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        System.out.println("\n###########\nmouseDragged: " + e.getX() + "," + e.getY());

    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        System.out.println("\n###########\nmouseMoved: " + e.getX() + "," + e.getY());


    }


}
