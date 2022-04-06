package sma;

import java.awt.Point;
import java.util.Random;

public class Agent implements IAgent{

    private Point position;
    private Random r;
    private Environment environment;

    private State state;

    public Agent(Point position, Environment environment,int seed) {
        this.position = position;
        this.environment = environment;
        this.state = State.SUSCEPTIBLE;
        this.r = new Random(seed);
    }

    @Override
    public void move() {
        int move = r.nextInt(4);

        Point newPosition = switch (move) {
            case Environment.LEFT -> new Point(position.x-1,position.y);
            case Environment.RIGHT -> new Point(position.x+1,position.y);
            case Environment.UP -> new Point(position.x,position.y-1);
            case Environment.DOWN -> new Point(position.x,position.y+1);
            default -> throw new IllegalStateException("Unexpected value: " + move);
        };

        if (environment.isCaseEmpty(newPosition)) {
            environment.emptyCase(position);
            environment.fillCase(this,newPosition);
            position = newPosition;
        }
    }

    @Override
    public void contact() {
        //TODO
    }

    @Override
    public void getStatus() {
        //TODO
    }

    @Override
    public String toString() {
        return "A";
    }
}
