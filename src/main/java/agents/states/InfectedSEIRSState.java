package agents.states;

public class InfectedSEIRSState extends SEIRSState {

    public InfectedSEIRSState(agents.SEIRSAgent SEIRSAgent) {
        super(SEIRSAgent);
    }

    @Override
    public void onMovement() {
        if (agent.isRecovered()) {
            agent.changeState(new RecoveredSEIRSState(agent));
        }
    }

    @Override
    public String toString() {
        return INFECTED;
    }
}
