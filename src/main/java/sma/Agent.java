package sma;

import utils.YamlReader;

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
    private Environment environment;

    private State state;
    private Boolean exposedThisCycle;
    private Boolean infectedThisCycle;

    public Agent(Point position, Environment environment,int seed) {
        this.position = position;
        this.environment = environment;
        this.state = State.SUSCEPTIBLE;
        this.r = new Random(seed);
    }

    private void move() {
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
        if (state == State.SUSCEPTIBLE) {
            contact();
        }
        move();
        if (state == State.EXPOSED && !exposedThisCycle) {
            incubate();
        }
        if (state == State.INFECTED && !infectedThisCycle) {
            recover();
        }
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
