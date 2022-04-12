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

    private int frameWidth;
    private int frameHeight;

    private List<Pair<Component,Integer>> components;

    public FrameBuilder() {
        resetWindow();
    }

    public void resetWindow() {
        components = new ArrayList<>();
        frameWidth = 0;
        frameHeight = 0;
    }


    public void addComponent(Component c,int position) {
        var pair = new Pair<>(c,position);
        components.add(pair);
    }

    public JFrame buildWindow() {
        JFrame frame = new JFrame();
        frame.setLayout(new java.awt.GridLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (Pair<Component,Integer> pair : components ) {
            switch (pair.getSecond()) {
                case TOP -> frame.add(pair.getFirst(),BorderLayout.NORTH);
                case BOTTOM -> frame.add(pair.getFirst(),BorderLayout.SOUTH);
                case LEFT -> frame.add(pair.getFirst(),BorderLayout.WEST);
                case RIGHT -> frame.add(pair.getFirst(),BorderLayout.EAST);
                default -> throw new IllegalStateException("Wrong position value");
            }
        }
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        return frame;
    }
}
