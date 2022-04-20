package view;

import utils.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FrameBuilder {

    public final static int LEFT = 0;
    public final static int RIGHT = 1;
    public final static int TOP = 2;
    public final static int BOTTOM = 3;

    private int windowWidth = 0;
    private int windowHeight = 0;

    private List<Pair<JComponent,Integer>> components;

    public FrameBuilder() {
        resetWindow();
    }

    public void resetWindow() {
        components = new ArrayList<>();
    }


    public void addComponent(JComponent c,int position) {
        var pair = new Pair<>(c,position);
        System.out.println(c.getWidth());
        System.out.println(c.getHeight());
        if (windowWidth < c.getWidth()) {
            windowWidth = c.getWidth();
        }
        if (windowHeight < c.getHeight()) {
            windowHeight = c.getHeight();
        }
        components.add(pair);
    }

    public JFrame buildWindow() {
        JFrame frame = new JFrame();
        frame.setLayout(new java.awt.GridLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(new Dimension(1000,500));

        for (Pair<JComponent,Integer> pair : components ) {
            switch (pair.getSecond()) {
                case TOP -> frame.add(pair.getFirst(),BorderLayout.NORTH);
                case BOTTOM -> frame.add(pair.getFirst(),BorderLayout.SOUTH);
                case LEFT -> frame.add(pair.getFirst(),BorderLayout.WEST);
                case RIGHT -> frame.add(pair.getFirst(),BorderLayout.EAST);
                default -> throw new IllegalStateException("Wrong position value");
            }
        }
        //frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        return frame;
    }
}
