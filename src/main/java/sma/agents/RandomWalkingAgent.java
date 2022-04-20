package sma.agents;

import sma.agents.states.InfectedState;
import sma.agents.states.State;
import sma.agents.states.SuceptibleState;
import sma.environment.Environment;
import utils.YamlReader;
import view.GraphicEnvironment;

import java.awt.Point;
import java.util.Random;

public class RandomWalkingAgent implements Agent {

    private Point position;
    private Random r;
    private GraphicEnvironment environment;
    private State state;

    public RandomWalkingAgent(Point position, int seed, GraphicEnvironment environment) {
        this.position = position;
        this.state = new SuceptibleState(this);
        this.environment = environment;
        this.r = new Random(seed);
    }

    public void move() {
        state.onMovement();
        
        int move = r.nextInt(4);
        Point newPosition = switch (move) {
            case Environment.LEFT -> new Point(position.x-environment.RADIUS,position.y);
            case Environment.RIGHT -> new Point(position.x+environment.RADIUS,position.y);
            case Environment.UP -> new Point(position.x,position.y-environment.RADIUS);
            case Environment.DOWN -> new Point(position.x,position.y+environment.RADIUS);
            default -> throw new IllegalStateException("Unexpected value: " + move);
        };
        if (newPosition.x <= environment.getWidth()-1 && newPosition.x >= 0 && newPosition.y <= environment.getHeight()-1 && newPosition.y >=0 ) {
            environment.notifyNewPosition(position,newPosition,this);
            position = newPosition;
        }
    }

    @Override
    public void changeState(State state) { this.state = state; }

    @Override
    public boolean isExposed() {
        boolean isExposed = false;
        for (RandomWalkingAgent neighbor: environment.getNeighbors(position)) {
            if (neighbor.getState() instanceof InfectedState) {
                int roll = r.nextInt(100);
                if (roll <= YamlReader.getParams().getInfectionRate()*100) {
                    isExposed = true;
                }
            }
        }
        return isExposed;
    }

    @Override
    public boolean isInfected() {
        boolean isSick = false;
        int roll = r.nextInt(100);
        if (roll <= YamlReader.getParams().getIncubationRate()*100) {
            isSick = true;
        }
        return isSick;
    }

    @Override
    public boolean isRecovered() {
        boolean isHealed = false;
        int roll = r.nextInt(100);
        if (roll <= YamlReader.getParams().getRecoveryRate()*100) {
            isHealed = true;
        }
        return isHealed;
    }

    @Override
    public boolean hasLostImmunity() {
        boolean hasLostImmunity = false;
        int roll = r.nextInt(100);
        if (roll <= YamlReader.getParams().getLooseImmunityRate()*100) {
            hasLostImmunity = true;
        }
        return hasLostImmunity;
    }

    public State getState() { return this.state; }

    public Point getPosition() { return position; }

}
