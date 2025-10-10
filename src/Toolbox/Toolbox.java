package Toolbox;

import javax.swing.*;
import java.awt.*;

public class Toolbox {
    public enum Tool {
        NONE,
        NET,
        GRAB,
        SCISSORS,
        BELL
    }

    private Tool currentTool = Tool.NONE;
    private ToolboxModel toolboxModel;
    private ToolboxView toolboxView;
    private JFrame frame;

    public Toolbox(){
        this.toolboxModel = new ToolboxModel();
        this.toolboxView = new ToolboxView();

        setUpFrame();
        setUpTools();
    }

    public void setUpFrame(){
        frame = new JFrame("Toolbox");
        frame.setPreferredSize(new Dimension(400,200));
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);

        frame.pack();
    }


    public void setUpTools(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.pink);

        // Add tools button
        JButton netButton = new JButton("Net");
        netButton.addActionListener(e -> {
            setCurrentTool(Tool.NET);
        });
        panel.add(netButton);

        JButton scissorsButton = new JButton("Scissors");
        scissorsButton.addActionListener(e -> {
            setCurrentTool(Tool.SCISSORS);
        });        panel.add(scissorsButton);

        JButton grabButton = new JButton("Grab");
        grabButton.addActionListener(e -> {
            setCurrentTool(Tool.GRAB);
        });
        panel.add(grabButton);

        JButton bellButton = new JButton("Bell");
        bellButton.addActionListener(e -> {
            setCurrentTool(Tool.BELL);
        });        panel.add(bellButton);


        frame.add(panel, BorderLayout.CENTER);

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
    }

    public JFrame getFrame(){
        return frame;
    }

    public ToolboxModel getToolboxModel() {
        return toolboxModel;
    }

    public ToolboxView getToolboxView() {
        return toolboxView;
    }


}
