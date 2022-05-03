package agents;

import agents.states.SEIRSState;
import agents.states.SuceptibleSEIRSState;
import environment.SEIRSEnvironment;
import utils.YamlReader;

import java.awt.Point;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class RandomWalkingAgent implements SEIRSAgent {

    protected Point position;
    protected Random r;
    protected final SEIRSEnvironment environment;
    protected SEIRSState state;

    private List<Point> authorizedPositions;
    private Point nextPosition;
    private int seed;

    public RandomWalkingAgent(Point position, int seed, SEIRSEnvironment environment) {
        this.seed = seed;
        this.position = position;
        this.state = new SuceptibleSEIRSState(this);
        this.environment = environment;
        try{
            r = SecureRandom.getInstance("SHA1PRNG", "SUN");
        }catch (Exception e) {
            System.err.println(e);
        }
        r.setSeed(seed);
    }

    private void move() {
        state.onMovement();
        environment.notifyNewPosition(nextPosition,this);
        position = nextPosition;
    }

    private void perceiveAuthorizedPositions() {
        authorizedPositions =  environment.perceiveAuthorizedPositions(this);
    }

    private void decideNextMove() {
        int next = r.nextInt(authorizedPositions.size());
        nextPosition = authorizedPositions.get(next);
    }

    @Override
    public void wakeUp() {
        perceiveAuthorizedPositions();
        if (!authorizedPositions.isEmpty()) {
            decideNextMove();
            move();
        }
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
