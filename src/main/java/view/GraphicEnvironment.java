package view;

import sma.Agent;
import sma.IEnvironment;
import utils.YamlReader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphicEnvironment extends JFrame implements IEnvironment {

    public final static int RADIUS = 10;
    public final static String WINDOW_NAME = "SMA-SEIR";

    public Agent[] agents;

    private int y_offset;
    private int x_offset;

    private int windowWidth;
    private int windowHeight;

    public GraphicEnvironment(int width,int height,Agent[] agents) {
        this.windowWidth = width;
        this.windowHeight = height;
        this.agents = agents;
        setSize(windowWidth,windowHeight);
        setName(WINDOW_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        y_offset = getInsets().top;
        x_offset = getInsets().left;
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < agents.length; i++) {
            var agent = agents[i];
            if (agent != null) {
                colorAgent(g,agent);
                g.fillOval(agent.getPosition().x+x_offset,agent.getPosition().y+y_offset, RADIUS, RADIUS);
            }
        }
    }

    @Override
    public List<Agent> getNeighbors(Point p) {
        var neighbors = new ArrayList<Agent>();
        for (Agent agent : agents) {
            if (detectCollision(p, agent.getPosition())) {
                neighbors.add(agent);
            }
        }
        return neighbors;
    }

    private Boolean detectCollision(Point pos1, Point pos2) {
        double xDif = pos1.x - pos2.x;
        double yDif = pos1.y - pos2.y;
        double distanceSquared = xDif * xDif + yDif * yDif;
        return distanceSquared < (2* RADIUS) * (2* RADIUS);
    }

    private void colorAgent(Graphics g,Agent a) {
        switch (a.getState()) {
            case SUSCEPTIBLE -> g.setColor(Color.GRAY);
            case EXPOSED -> g.setColor(Color.YELLOW);
            case INFECTED -> g.setColor(Color.RED);
            case RECOVERED -> g.setColor(Color.GREEN);
        }
    }
}
