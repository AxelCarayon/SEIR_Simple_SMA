package sma.environment;

import sma.agents.Agent;
import sma.agents.states.State;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SquaredChunksEnvironment implements Environment {

    public final static int RADIUS = 10;
    public final static int CHUNK_SIZE = 2*RADIUS;

    public int size;
    private Agent[] agents;
    private List<Agent>[][] chunks;

    public SquaredChunksEnvironment(int size, Agent[] agents) {
        this.agents = agents;
        this.size = size;
    }

    public void initiateChunks() {
        chunks = new ArrayList[(size/CHUNK_SIZE)][(size/CHUNK_SIZE)];
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
    private Boolean detectCollision(Point pos1, Point pos2) {
        double xDif = pos1.x - pos2.x;
        double yDif = pos1.y - pos2.y;
        double distanceSquared = xDif * xDif + yDif * yDif;
        return distanceSquared < (2*RADIUS) * (2*RADIUS);
    }

    private Point getRelativePoint(int relativeTo, Point p) {
        return switch (relativeTo) {
            case LEFT -> new Point(p.x-1,p.y);
            case RIGHT -> new Point(p.x+1,p.y);
            case UP -> new Point(p.x,p.y-1);
            case DOWN -> new Point(p.x,p.y+1);
            case CENTER -> p;
            case UP_LEFT -> new Point(p.x-1,p.y-1);
            case UP_RIGHT -> new Point(p.x+1,p.y-1);
            case DOWN_LEFT -> new Point(p.x-1,p.y+1);
            case DOWN_RIGHT -> new Point(p.x+1,p.y+1);
            default -> throw new IllegalStateException("Unexpected value: " + relativeTo);
        };
    }

    private List<Agent> getChunkNeighbors(int relativeTo, Point p) {
        Point newPosition = getRelativePoint(relativeTo,p);
        Point chunk = new Point(newPosition.x/CHUNK_SIZE,newPosition.y/CHUNK_SIZE);
        var neighbors = new ArrayList<Agent>();
        try{
            for (Agent agent : chunks[chunk.x][chunk.y]) {
                if (detectCollision(p, agent.getPosition())) {
                    neighbors.add(agent);
                }
            }
        }catch (Exception e) {
            return neighbors;
        }
        return neighbors;
    }

    @Override
    public List<Agent> getNeighbors(Point position) {
        if (chunks == null) {
            throw new IllegalStateException("Chunks aren't initialized, you should use the initiateMethod() first.");
        }
        var neighbors = new ArrayList<Agent>();

        for (int i = 0; i < MAX_CHUNK; i++) {
            neighbors.addAll(getChunkNeighbors(i,position));
        }
        return neighbors;
    }

    @Override
    public void notifyNewPosition(Point oldPosition, Point newPosition, Agent agent) {
        if (chunks == null) {
            throw new IllegalStateException("Chunks aren't initialized, you should use the initiateMethod() first.");
        }
        if (oldPosition.x/CHUNK_SIZE != newPosition.x/CHUNK_SIZE || oldPosition.y/CHUNK_SIZE != newPosition.y/CHUNK_SIZE) {
            chunks[oldPosition.x/CHUNK_SIZE][oldPosition.y/CHUNK_SIZE].remove(agent);
            chunks[newPosition.x/CHUNK_SIZE][newPosition.y/CHUNK_SIZE].add(agent);
        }
    }

    @Override
    public HashMap<String,Integer> getAgentStatus() {

        var map = new HashMap<String,Integer>();
        map.put(State.EXPOSED,0);
        map.put(State.INFECTED,0);
        map.put(State.RECOVERED,0);
        map.put(State.SUCEPTIBLE,0);

        for (Agent agent : agents) {
            String state = agent.getState().toString();
            map.put(state,map.get(state)+1);
        }
        return map;
    }
}
