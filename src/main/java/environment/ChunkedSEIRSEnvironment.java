package environment;

import agents.Agent2D;
import agents.SEIRSAgent;
import agents.states.InfectedSEIRSState;
import agents.states.SEIRSState;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public class ChunkedSEIRSEnvironment implements SEIRSEnvironment {

    public final static int RADIUS = 10;
    public final static int CHUNK_SIZE = 2*RADIUS;

    public final int size;
    private final SEIRSAgent[] agents;
    private List<SEIRSAgent>[][] chunks;

    public ChunkedSEIRSEnvironment(int size, SEIRSAgent[] agents) {
        this.agents = agents;
        this.size = size;
    }

    private void initiateChunks() {
        chunks = new ArrayList[(size/CHUNK_SIZE)][(size/CHUNK_SIZE)];
        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < chunks[i].length; j++) {
                chunks[i][j] = new ArrayList<>();
            }
        }
        for (SEIRSAgent agent : agents) {
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

    private List<SEIRSAgent> getInfectedChunkNeighbors(int relativeTo, Point p) {
        Point newPosition = getRelativePoint(relativeTo,p);
        Point chunk = new Point(newPosition.x/CHUNK_SIZE,newPosition.y/CHUNK_SIZE);
        List<SEIRSAgent> neighbors = new ArrayList<>();
        try{
            for (SEIRSAgent agent : chunks[chunk.x][chunk.y]) {
                if (detectCollision(p, agent.getPosition())) {
                    if (agent.getState() instanceof InfectedSEIRSState) {
                        neighbors.add(agent);
                    }
                }
            }
        }catch (Exception e) {
            return neighbors;
        }
        return neighbors;
    }

    @Override
    public void notifyNewPosition(Point oldPosition, Point newPosition, Agent2D agent) {
        if (chunks == null) {
            initiateChunks();
        }
        if (oldPosition.x/CHUNK_SIZE != newPosition.x/CHUNK_SIZE || oldPosition.y/CHUNK_SIZE != newPosition.y/CHUNK_SIZE) {
            chunks[oldPosition.x/CHUNK_SIZE][oldPosition.y/CHUNK_SIZE].remove((SEIRSAgent) agent);
            chunks[newPosition.x/CHUNK_SIZE][newPosition.y/CHUNK_SIZE].add((SEIRSAgent) agent);
        }
    }

    @Override
    public List<SEIRSAgent> getInfectedNeighbors(Point p) {
        if (chunks == null) {
            initiateChunks();
        }
        var neighbors = new ArrayList<SEIRSAgent>();

        for (int i = 0; i < MAX_CHUNK; i++) {
            neighbors.addAll(getInfectedChunkNeighbors(i,p));
        }
        return neighbors;
    }

    @Override
    public HashMap<String,Integer> getAgentsStatus() {
        if (chunks == null) {
            initiateChunks();
        }
        var map = new HashMap<String,Integer>();
        map.put(SEIRSState.EXPOSED,0);
        map.put(SEIRSState.INFECTED,0);
        map.put(SEIRSState.RECOVERED,0);
        map.put(SEIRSState.SUCEPTIBLE,0);

        for (SEIRSAgent SEIRSAgent : agents) {
            String state = SEIRSAgent.getState().toString();
            map.put(state,map.get(state)+1);
        }
        return map;
    }

    @Override
    public int getSize() {
        return size;
    }
}
