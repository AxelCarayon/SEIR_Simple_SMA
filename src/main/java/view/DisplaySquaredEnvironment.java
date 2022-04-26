package view;

import agents.SEIRSAgent;
import agents.states.SEIRSState;
import environment.SEIRSEnvironment;

import javax.swing.*;
import java.awt.*;

public class DisplaySquaredEnvironment extends JPanel {


    private final SEIRSAgent[] SEIRSAgents;

    public DisplaySquaredEnvironment(SEIRSEnvironment environment, SEIRSAgent[] SEIRSAgents) {
        this.setDoubleBuffered(true);
        this.SEIRSAgents = SEIRSAgents;
        setSize(environment.size,environment.size);
        setVisible(true);
    }

    private void drawCenteredCircle(Graphics g, int x, int y) {
        x = x-(SEIRSEnvironment.RADIUS /2);
        y = y-(SEIRSEnvironment.RADIUS /2);
        g.fillOval(x,y, SEIRSEnvironment.RADIUS, SEIRSEnvironment.RADIUS);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (SEIRSAgent SEIRSAgent : SEIRSAgents) {
            if (SEIRSAgent != null) {
                colorAgent(g, SEIRSAgent);
                drawCenteredCircle(g, SEIRSAgent.getPosition().x, SEIRSAgent.getPosition().y);
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
