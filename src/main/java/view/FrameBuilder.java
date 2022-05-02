package view;

import javax.swing.*;
import java.awt.*;

public class FrameBuilder {

    private JPanel simulation;
    private JPanel stats;

    public FrameBuilder() {
        resetWindow();
    }

    public void resetWindow() {
        simulation = null;
        stats = null;
    }

    public void setSimulationCanvas(JPanel canvas) {
        this.simulation = canvas;
    }

    public void setStatsCanvas(JPanel canvas) {
        this.stats = canvas;
    }

    public void buildWindow() {

        if (simulation == null || stats == null) {
            throw new IllegalStateException("Cannot create windows : one of the canvas is not initialized.");
        }

        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

        if (simulation.getWidth()+stats.getWidth() > screenDimension.getWidth() ||
                simulation.getHeight() > screenDimension.getHeight()) {
            System.err.println("WARNING : screen size is smaller than the simulation window. The simulation will " +
                    "still work but won't display properly.");
        }

        JFrame frame = new JFrame();
        JSplitPane panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, simulation, stats);
        panel.setDividerLocation(simulation.getWidth());
        panel.setDividerSize(0);
        frame.setSize(simulation.getWidth()+stats.getWidth(),simulation.getHeight());
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.setVisible(true);
    }
}
