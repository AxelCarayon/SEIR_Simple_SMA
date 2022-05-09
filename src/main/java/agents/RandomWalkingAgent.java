package agents;

import agents.states.SEIRSState;
import agents.states.SuceptibleSEIRSState;
import behaviors.Randomized;
import environment.SEIRSEnvironment;
import utils.YamlReader;

import java.awt.Point;
import java.util.List;

public class RandomWalkingAgent extends Randomized implements SEIRSAgent {

    protected Point position;
    protected final SEIRSEnvironment environment;
    protected SEIRSState state;

    private List<Point> authorizedPositions;
    private Point nextPosition;
    private final String id;

    public RandomWalkingAgent(Point position, long seed, SEIRSEnvironment environment) {
        super(seed);
        this.id = String.valueOf(seed);
        this.position = position;
        this.state = new SuceptibleSEIRSState(this);
        this.environment = environment;
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
            if (roll <= YamlReader.getParams().infectionRate()*10000) {
                isExposed = true;
            }
        }
        return isExposed;
    }

    @Override
    public boolean isInfected() {
        boolean isSick = false;
        int roll = r.nextInt(10000)+1;
        if (roll <= YamlReader.getParams().incubationRate()*10000) {
            isSick = true;
        }
        return isSick;
    }

    @Override
    public boolean isRecovered() {
        boolean isHealed = false;
        int roll = r.nextInt(10000)+1;
        if (roll <= YamlReader.getParams().recoveryRate()*10000) {
            isHealed = true;
        }
        return isHealed;
    }

    @Override
    public boolean hasLostImmunity() {
        boolean hasLostImmunity = false;
        int roll = r.nextInt(10000)+1;
        if (roll <= YamlReader.getParams().looseImmunityRate()*10000) {
            hasLostImmunity = true;
        }
        return hasLostImmunity;
    }

    @Override
    public SEIRSState getState() { return this.state; }

    @Override
    public Point getPosition() { return position; }

    @Override
    public String getId() {
        return this.id;
    }

}
