package agents.states;

import agents.seirs.SEIRSAgent;

public class SuceptibleSEIRSState extends SEIRSState {

    public SuceptibleSEIRSState(SEIRSAgent agent) {
        super(agent);
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

    public static void main(String[] args) {
        float i = 4.12f;
        int i2 = (int) i;
        System.out.println((float)i2);
    }
}
