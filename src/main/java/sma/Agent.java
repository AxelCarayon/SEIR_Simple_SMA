package sma;

import utils.YamlReader;
import view.GraphicEnvironment;

import java.awt.Point;
import java.util.Random;

public class Agent {

    public enum State {
        SUSCEPTIBLE,
        EXPOSED,
        INFECTED,
        RECOVERED
    }

    private Point position;
    private Random r;
    private GraphicEnvironment environment;

    private State state;
    private Boolean exposedThisCycle;
    private Boolean infectedThisCycle;

    public Agent(Point position, int seed, GraphicEnvironment environment) {
        this.position = position;
        this.state = State.SUSCEPTIBLE;
        this.environment = environment;
        this.r = new Random(seed);
    }

    private void move() {
        int move = r.nextInt(4);

        Point newPosition = switch (move) {
            case IEnvironment.LEFT -> new Point(position.x-environment.RADIUS,position.y);
            case IEnvironment.RIGHT -> new Point(position.x+environment.RADIUS,position.y);
            case IEnvironment.UP -> new Point(position.x,position.y-environment.RADIUS);
            case IEnvironment.DOWN -> new Point(position.x,position.y+environment.RADIUS);
            default -> throw new IllegalStateException("Unexpected value: " + move);
        };
        if (newPosition.x <= environment.getWidth() && newPosition.x >= 0 && newPosition.y <= environment.getHeight() && newPosition.y >=0 ) {
            position = newPosition;
        }
    }

    private void contact() {
        for (Agent neighbor: environment.getNeighbors(position)) {
            if (neighbor.getState() == State.INFECTED) {
                int roll = r.nextInt(100);
                if (roll <= YamlReader.getParams().getInfectionChance()*100) {
                    state = State.EXPOSED;
                    exposedThisCycle = true;
                }
            }
        }
    }

    private void incubate() {
        int roll = r.nextInt(100);
        if (roll <= YamlReader.getParams().getIncubationRate()*100) {
            state = State.INFECTED;
            infectedThisCycle = true;
        }
    }

    private void recover() {
        int roll = r.nextInt(100);
        if (roll <= YamlReader.getParams().getRecoveryRate()*100) {
            state = State.RECOVERED;
        }
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void wakeUp() {
        exposedThisCycle = false;
        infectedThisCycle = false;
        move();
        if (state == State.SUSCEPTIBLE) {
            contact();
        }
        if (state == State.EXPOSED && !exposedThisCycle) {
            incubate();
        }
        if (state == State.INFECTED && !infectedThisCycle) {
            recover();
        }
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return switch (state) {
            case SUSCEPTIBLE -> "S";
            case EXPOSED -> "E";
            case INFECTED -> "I";
            case RECOVERED -> "R";
        };
    }
}
