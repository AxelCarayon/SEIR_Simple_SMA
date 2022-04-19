package view;

import sma.agents.Agent;
import sma.environment.Environment;
import utils.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GraphicEnvironment extends Canvas implements Environment {

    public final static int RADIUS = 10;

    public final static int CHUNK_SIZE = 2*RADIUS;

    private Agent[] agents;

    private List<Agent>[][] chunks;

    private int windowWidth;
    private int windowHeight;

    public GraphicEnvironment(int width,int height,Agent[] agents) {
        this.windowWidth = width;
        this.windowHeight = height;
        this.agents = agents;
        setSize(windowWidth,windowHeight);
        setVisible(true);
    }

    public void initiateChunks() {
        chunks = new ArrayList[(windowWidth/CHUNK_SIZE)][(windowHeight/CHUNK_SIZE)];
        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < chunks[i].length; j++) {
                chunks[i][j] = new ArrayList<>();
            }
        }
        for (Agent agent : agents) {
            int x = agent.getPosition().x/CHUNK_SIZE;
            int y = agent.getPosition().y/CHUNK_SIZE;
            chunks[x][y].add(agent);
        }
    }

    public void notifyNewPosition(Point oldPosition, Point newPosition, Agent agent) {
        if (oldPosition.x/CHUNK_SIZE != newPosition.x/CHUNK_SIZE || oldPosition.y/CHUNK_SIZE != newPosition.y/CHUNK_SIZE) {
            chunks[oldPosition.x/CHUNK_SIZE][oldPosition.y/CHUNK_SIZE].remove(agent);
            chunks[newPosition.x/CHUNK_SIZE][newPosition.y/CHUNK_SIZE].add(agent);
        }
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
                drawCenteredCircle(g,agent.getPosition().x,agent.getPosition().y,RADIUS);
            }
        }
    }

    @Override
    public List<Agent> getNeighbors(Point p) {
        var neighbors = new ArrayList<Agent>();

        for (int i = 0; i < MAX_CHUNK; i++) {
            neighbors.addAll(getChunkNeighbors(i,p));
        }
        return neighbors;
    }

    private List<Agent> getChunkNeighbors(int relativeTo, Point p) {
        var x = p.x/CHUNK_SIZE;
        var y = p.y/CHUNK_SIZE;
        switch (relativeTo) {
            case LEFT -> x-=1;
            case RIGHT -> x+=1;
            case UP -> y-=1;
            case DOWN -> y+=1;
            case CENTER -> {}
            case UP_LEFT -> {x-=1;y-=1;}
            case UP_RIGHT -> {x+=1;y-=1;}
            case DOWN_LEFT -> {x-=1;y+=1;}
            case DOWN_RIGHT -> {x+=1;y+=1;}
            default -> throw new IllegalStateException("Unexpected value: " + relativeTo);
        };

        var neighbors = new ArrayList<Agent>();
        try{
            for (Agent agent : chunks[x][y]) {
                if (detectCollision(p, agent.getPosition())) {
                    neighbors.add(agent);
                }
            }
        }catch (Exception e) {
            return neighbors;
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
        return distanceSquared < (2*RADIUS) * (2*RADIUS);
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
