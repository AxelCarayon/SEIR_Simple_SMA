package view;

import sma.agents.RandomWalkingAgent;
import sma.agents.states.*;
import sma.environment.Environment;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GraphicEnvironment extends JPanel implements Environment {

    public final static int RADIUS = 10;

    public final static int CHUNK_SIZE = 2*RADIUS;

    private RandomWalkingAgent[] agents;

    private List<RandomWalkingAgent>[][] chunks;

    private int windowWidth;
    private int windowHeight;

    public GraphicEnvironment(int width, int height, RandomWalkingAgent[] agents) {
        this.setDoubleBuffered(true);
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
        for (RandomWalkingAgent agent : agents) {
            int x = agent.getPosition().x/CHUNK_SIZE;
            int y = agent.getPosition().y/CHUNK_SIZE;
            chunks[x][y].add(agent);
        }
    }

    public void notifyNewPosition(Point oldPosition, Point newPosition, RandomWalkingAgent agent) {
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
    public List<RandomWalkingAgent> getNeighbors(Point p) {
        var neighbors = new ArrayList<RandomWalkingAgent>();

        for (int i = 0; i < MAX_CHUNK; i++) {
            neighbors.addAll(getChunkNeighbors(i,p));
        }
        return neighbors;
    }

    private List<RandomWalkingAgent> getChunkNeighbors(int relativeTo, Point p) {
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

        var neighbors = new ArrayList<RandomWalkingAgent>();
        try{
            for (RandomWalkingAgent agent : chunks[x][y]) {
                if (detectCollision(p, agent.getPosition())) {
                    neighbors.add(agent);
                }
            }
        }catch (Exception e) {
            return neighbors;
        }
        return neighbors;
    }

    public HashMap<String,Integer> getAgentStatus() {

        var map = new HashMap<String,Integer>();
        map.put(State.EXPOSED,0);
        map.put(State.INFECTED,0);
        map.put(State.RECOVERED,0);
        map.put(State.SUCEPTIBLE,0);

        for (RandomWalkingAgent agent : agents) {
            String state = agent.getState().toString();
            map.put(state,map.get(state)+1);
        }
        return map;
    }

    private Boolean detectCollision(Point pos1, Point pos2) {
        double xDif = pos1.x - pos2.x;
        double yDif = pos1.y - pos2.y;
        double distanceSquared = xDif * xDif + yDif * yDif;
        return distanceSquared < (2*RADIUS) * (2*RADIUS);
    }

    private void colorAgent(Graphics g, RandomWalkingAgent a) {
        var state = a.getState();
        switch (state.toString()) {
            case State.SUCEPTIBLE-> g.setColor(Color.GRAY);
            case State.EXPOSED -> g.setColor(Color.YELLOW);
            case State.INFECTED -> g.setColor(Color.RED);
            case State.RECOVERED -> g.setColor(Color.GREEN);
        }
    }
}
