package agents.states;

public class ExposedSEIRSState extends SEIRSState {

    public ExposedSEIRSState(agents.SEIRSAgent SEIRSAgent) {
        super(SEIRSAgent);
    }

    @Override
    public void onMovement() {
        if (agent.isInfected()) {
            agent.changeState(new InfectedSEIRSState(agent));
        }
    }

    @Override
    public String toString() {
        return EXPOSED;
    }
}
