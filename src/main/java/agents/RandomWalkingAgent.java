package agents;

import agents.states.SEIRSState;
import agents.states.SuceptibleSEIRSState;
import environment.ChunkedSEIRSEnvironment;
import environment.SEIRSEnvironment;
import utils.YamlReader;

import java.awt.Point;
import java.util.Random;

public class RandomWalkingAgent implements SEIRSAgent {

    private Point position;
    private final Random r;
    private final SEIRSEnvironment environment;
    private SEIRSState state;

    public RandomWalkingAgent(Point position, int seed, SEIRSEnvironment environment) {
        this.position = position;
        this.state = new SuceptibleSEIRSState(this);
        this.environment = environment;
        this.r = new Random(seed);
    }

    private void move() {
        state.onMovement();
        int move = r.nextInt(4);
        Point newPosition = switch (move) {
            case SEIRSEnvironment.LEFT -> new Point(position.x- ChunkedSEIRSEnvironment.RADIUS,position.y);
            case SEIRSEnvironment.RIGHT -> new Point(position.x+ ChunkedSEIRSEnvironment.RADIUS,position.y);
            case SEIRSEnvironment.UP -> new Point(position.x,position.y- ChunkedSEIRSEnvironment.RADIUS);
            case SEIRSEnvironment.DOWN -> new Point(position.x,position.y+ ChunkedSEIRSEnvironment.RADIUS);
            default -> throw new IllegalStateException("Unexpected value: " + move);
        };
        if (newPosition.x <= environment.getSize()-1 && newPosition.x >= 0 && newPosition.y <= environment.getSize()-1 && newPosition.y >=0 ) {
            environment.notifyNewPosition(position,newPosition ,this);
            position = newPosition;
        }
    }

    @Override
    public void wakeUp() {
        move();
    }


    @Override
    public void changeState(SEIRSState SEIRSState) { this.state = SEIRSState; }

    @Override
    public boolean isExposed() {
        boolean isExposed = false;
        for (int i = 0 ; i<environment.getInfectedNeighbors(position).size() ; i++) {
            int roll = r.nextInt(10000)+1;
            if (roll <= YamlReader.getParams().getInfectionRate()*10000) {
                isExposed = true;
            }
        }
        return isExposed;
    }

    @Override
    public boolean isInfected() {
        boolean isSick = false;
        int roll = r.nextInt(10000)+1;
        if (roll <= YamlReader.getParams().getIncubationRate()*10000) {
            isSick = true;
        }
        return isSick;
    }

    @Override
    public boolean isRecovered() {
        boolean isHealed = false;
        int roll = r.nextInt(10000)+1;
        if (roll <= YamlReader.getParams().getRecoveryRate()*10000) {
            isHealed = true;
        }
        return isHealed;
    }

    @Override
    public boolean hasLostImmunity() {
        boolean hasLostImmunity = false;
        int roll = r.nextInt(10000)+1;
        if (roll <= YamlReader.getParams().getLooseImmunityRate()*10000) {
            hasLostImmunity = true;
        }
        return hasLostImmunity;
    }

    @Override
    public SEIRSState getState() { return this.state; }

    @Override
    public Point getPosition() { return position; }

}
