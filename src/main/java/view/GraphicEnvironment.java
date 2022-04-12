package view;

import sma.Agent;
import sma.IEnvironment;
import utils.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphicEnvironment extends Canvas implements IEnvironment {

    public final static int RADIUS = 10;

    private Agent[] agents;

    private int windowWidth;
    private int windowHeight;

    public GraphicEnvironment(int width,int height,Agent[] agents) {
        this.windowWidth = width;
        this.windowHeight = height;
        this.agents = agents;
        setSize(windowWidth,windowHeight);
        setVisible(true);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < agents.length; i++) {
            var agent = agents[i];
            if (agent != null) {
                colorAgent(g,agent);
                g.fillOval(agent.getPosition().x,agent.getPosition().y, RADIUS, RADIUS);
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

    public HashMap<Agent.State,Pair<Integer,Color>> getAgentStatus() {

        Pair<Integer,Color> susceptible = new Pair<>(0,Color.GRAY);
        Pair<Integer,Color> exposed = new Pair<>(0,Color.YELLOW);
        Pair<Integer,Color> infected = new Pair<>(0,Color.RED);
        Pair<Integer,Color> recovered = new Pair<>(0,Color.green);

        for (Agent agent : agents) {
            switch (agent.getState()) {
                case SUSCEPTIBLE -> susceptible.setFirst(susceptible.getFirst()+1);
                case EXPOSED -> exposed.setFirst(exposed.getFirst()+1);
                case INFECTED -> infected.setFirst(infected.getFirst()+1);
                case RECOVERED -> recovered.setFirst(recovered.getFirst()+1);
            }
        }
        var result = new HashMap<Agent.State,Pair<Integer,Color>>();
        result.put(Agent.State.SUSCEPTIBLE,susceptible);
        result.put(Agent.State.EXPOSED,exposed);
        result.put(Agent.State.INFECTED,infected);
        result.put(Agent.State.RECOVERED,recovered);
        return result;
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
