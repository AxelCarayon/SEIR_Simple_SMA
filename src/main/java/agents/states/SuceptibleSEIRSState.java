package agents.states;

public class SuceptibleSEIRSState extends SEIRSState {

    public SuceptibleSEIRSState(agents.SEIRSAgent SEIRSAgent) {
        super(SEIRSAgent);
    }

    @Override
    public void onMovement() {
        if (agent.isExposed()) {
            agent.changeState(new ExposedSEIRSState(agent));
        }
    }

    @Override
    public String toString() {
        return SUCEPTIBLE;
    }
}
