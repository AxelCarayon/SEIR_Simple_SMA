package view;

import agents.seirs.SEIRSAgent;
import agents.states.SEIRSState;
import environment.ChunkedSEIRSEnvironment;
import environment.SEIRSEnvironment;

import javax.swing.*;
import java.awt.*;

public class DisplaySquaredEnvironment extends JPanel {

    private final SEIRSAgent[] CyclicSEIRSAgents;

    public DisplaySquaredEnvironment(SEIRSEnvironment environment, SEIRSAgent[] CyclicSEIRSAgents) {
        this.setDoubleBuffered(true);
        this.CyclicSEIRSAgents = CyclicSEIRSAgents;
        setSize(environment.getSize(),environment.getSize());
        setVisible(true);
    }

    private void drawCenteredCircle(Graphics g, int x, int y) {
        x = x-(ChunkedSEIRSEnvironment.RADIUS /2);
        y = y-(ChunkedSEIRSEnvironment.RADIUS /2);
        g.fillOval(x,y, ChunkedSEIRSEnvironment.RADIUS, ChunkedSEIRSEnvironment.RADIUS);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (SEIRSAgent agent : CyclicSEIRSAgents) {
            if (agent != null) {
                colorAgent(g, agent);
                drawCenteredCircle(g, agent.getPosition().x, agent.getPosition().y);
            }
        }
    }

    private void colorAgent(Graphics g, SEIRSAgent a) {
        var state = a.getState();
        switch (state.toString()) {
            case SEIRSState.SUCEPTIBLE-> g.setColor(Color.GRAY);
            case SEIRSState.EXPOSED -> g.setColor(Color.YELLOW);
            case SEIRSState.INFECTED -> g.setColor(Color.RED);
            case SEIRSState.RECOVERED -> g.setColor(Color.GREEN);
        }
    }
}
