import agents.RandomWalkingAgent;
import agents.SEIRSAgent;
import agents.states.InfectedSEIRSState;
import environment.SEIRSEnvironment;
import models.Parameters;
import scheduler.FairAsynchronousScheduler;
import scheduler.FairSynchronousScheduler;
import scheduler.Scheduler;
import sma.SEIRS_SMA;
import sma.SMA;
import utils.YamlReader;

import java.awt.*;
import java.util.Random;

public class RunExperiment {

    public static void main(String[] args) {
        SMA sma = new SEIRS_SMA();
        Parameters parameters = YamlReader.getParams();
        Random r = new Random(parameters.getSeed());

        SEIRSAgent[] agents = new RandomWalkingAgent[parameters.getPopulation()];
        Scheduler scheduler;
        SEIRSEnvironment environment = new SEIRSEnvironment(parameters.getSize(),agents);

        //Populate agents
        for (int i = 0; i<parameters.getPopulation();i++) {
            Point position = new Point(r.nextInt(parameters.getSize()),r.nextInt(parameters.getSize()));
            RandomWalkingAgent agent = new RandomWalkingAgent(position,parameters.getSeed()+i,environment);
            agents[i] = agent;
        }

        //Infect agents
        for (int i=0 ; i< parameters.getNbOfPatientZero(); i++) {
            SEIRSAgent agent = agents[(r.nextInt(parameters.getPopulation()))];
            while (agent.getState() instanceof InfectedSEIRSState) {
                agent = agents[(r.nextInt(parameters.getPopulation()))];
            }
            agent.changeState(new InfectedSEIRSState(agent));
        }

        //create scheduler
        if (parameters.isSynchronousMode()) {
            scheduler = new FairSynchronousScheduler(parameters.getSeed());
        } else {
            scheduler = new FairAsynchronousScheduler();
        }

        sma.init(environment,scheduler,agents);
        sma.run();
    }

}
