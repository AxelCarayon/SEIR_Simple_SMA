package view;

import sma.agents.Agent;
import sma.agents.states.*;
import sma.environment.SquaredChunksEnvironment;

import javax.swing.*;
import java.awt.*;

public class DisplaySquaredEnvironment extends JPanel {


    private int windowWidth;
    private int windowHeight;

    private Agent[] agents;

    private SquaredChunksEnvironment environment;

    public DisplaySquaredEnvironment(SquaredChunksEnvironment environment, Agent[] agents) {
        this.environment = environment;
        this.setDoubleBuffered(true);
        this.windowWidth = environment.size;
        this.windowHeight = environment.size;
        this.agents = agents;
        setSize(windowWidth,windowHeight);
        setVisible(true);
    }

    private void drawCenteredCircle(Graphics g, int x, int y, int r) {
        x = x-(r/2);
        y = y-(r/2);
        g.fillOval(x,y,r,r);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < agents.length; i++) {
            var agent = agents[i];
            if (agent != null) {
                colorAgent(g,agent);
                drawCenteredCircle(g,agent.getPosition().x,agent.getPosition().y,environment.RADIUS);
            }
        }
    }

    private void colorAgent(Graphics g, Agent a) {
        var state = a.getState();
        switch (state.toString()) {
            case State.SUCEPTIBLE-> g.setColor(Color.GRAY);
            case State.EXPOSED -> g.setColor(Color.YELLOW);
            case State.INFECTED -> g.setColor(Color.RED);
            case State.RECOVERED -> g.setColor(Color.GREEN);
        }
    }
}
