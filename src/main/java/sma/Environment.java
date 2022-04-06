package sma;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Environment implements IEnvironment{

    public final static int LEFT = 0;
    public final static int RIGHT = 1;
    public final static int UP = 2;
    public final static int DOWN = 3;

    private Agent[][] world;

    public Environment(int size) {
        world = new Agent[size][size];
    }

    @Override
    public Boolean isCaseEmpty(Point p) {
        try {
            return world[p.x][p.y] == null;
        }catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public void fillCase(Agent a,Point p) {
        world[p.x][p.y] = a;
    }

    @Override
    public void emptyCase(Point p) {
        world[p.x][p.y] = null;
    }

    @Override
    public List<Agent> getNeighbors(Point position) {
        var neighbors = new ArrayList<Agent>();

        for (int i = 0; i < 4; i++) { //i take the value of each possible side (left right up down)
            var agent = getNeighbor(position,i);
            if (agent != null)
                neighbors.add(agent);
        }

        return neighbors;
    }

    private Agent getNeighbor(Point position,int side) {
        Agent agent;
        try{
            agent = switch (side){
                case LEFT -> world[position.x-1][position.y];
                case RIGHT -> world[position.x+1][position.y];
                case UP -> world[position.x][position.y-1];
                case DOWN -> world[position.x][position.y+1];
                default -> throw new IllegalStateException("Unexpected value: " + side);
            };
        } catch(ArrayIndexOutOfBoundsException e) {
            agent = null;
        }
        return agent;
    }

    private String oneLineToString(int line) {
        String output = "";
        for (int i = 0 ; i<world[line].length ; i++) {
            if (world[line][i] == null){
                output += "_ ";
            }else {
                output += world[line][i]+" ";
            }
        }
        return output;
    }


    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i<world.length; i++) {
            output += oneLineToString(i)+"\n";
        }
        return output;
    }
}
