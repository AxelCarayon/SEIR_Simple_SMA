package sma;

import models.Parameters;
import sma.agents.RandomWalkingAgent;
import sma.agents.states.InfectedState;
import sma.environment.SquaredChunksEnvironment;
import sma.scheduler.FairAsynchronousScheduler;
import sma.scheduler.FairSynchronousScheduler;
import sma.scheduler.Scheduler;
import utils.StatsRecorder;
import utils.YamlReader;
import view.DisplaySquaredEnvironment;
import view.FrameBuilder;
import view.StatisticsCanvas;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class SMA {

    private Parameters parameters;
    private Random r;
    private RandomWalkingAgent[] agents;
    private SquaredChunksEnvironment environment;
    private Scheduler scheduler;
    private StatisticsCanvas statisticsCanvas;
    private DisplaySquaredEnvironment display;

    private HashMap<String,Integer> stats;

    private FrameBuilder frameBuilder;

    public SMA() {
        parameters = YamlReader.getParams();
        r = new Random(parameters.getSeed());
        agents = new RandomWalkingAgent[parameters.getPopulation()];

        statisticsCanvas = new StatisticsCanvas(500,500);
        frameBuilder = new FrameBuilder();
    }


    private void populateEnvironment() {
        for (int i = 0; i<parameters.getPopulation();i++) {
            Point position = new Point(r.nextInt(parameters.getSize()),r.nextInt(parameters.getSize()));
            RandomWalkingAgent agent = new RandomWalkingAgent(position,parameters.getSeed()+i,environment);
            agents[i] = agent;
        }
    }

    private void infectPatientZero() {
        for (int i=0 ; i< parameters.getNbOfPatientZero(); i++) {
            var agent = agents[(r.nextInt(parameters.getPopulation()))];
            agent.changeState(new InfectedState(agent));
        }
    }

    private void initScheduler() {
        if (parameters.isSynchronousMode()) {
            scheduler = new FairSynchronousScheduler(agents, parameters.getSeed());
        } else {
            scheduler = new FairAsynchronousScheduler(agents);
        }
    }

    public void init() {
        environment = new SquaredChunksEnvironment(parameters.getSize(),agents);
        populateEnvironment();
        environment.initiateChunks();
        infectPatientZero();
        display = new DisplaySquaredEnvironment(environment,agents);

        frameBuilder.addComponent(display,FrameBuilder.TOP);
        frameBuilder.addComponent(statisticsCanvas,FrameBuilder.RIGHT);
        frameBuilder.buildWindow();

        initScheduler();
        statisticsCanvas.updateValues(environment.getAgentStatus());
        statisticsCanvas.repaint();
    }

    private void updateGraphics(){
        display.repaint();
        statisticsCanvas.updateValues(stats);
        statisticsCanvas.repaint();
    }

    private void doNextCycle() throws IOException, InterruptedException {
        scheduler.nextCycle();
        stats = environment.getAgentStatus();
        StatsRecorder.writeToCSV(stats,"output.csv");
        updateGraphics();
        if (parameters.getTimeBetweenCycles() > 0) {
            Thread.sleep(parameters.getTimeBetweenCycles());
        }
    }

    public void run() throws IOException, InterruptedException {

        if (parameters.getNbOfCycles() <0) {
            while (true) {
                doNextCycle();
            }
        } else {
            int cpt = 0;
            while (cpt < parameters.getNbOfCycles()) {
                doNextCycle();
                cpt++;
            }
        }
        System.out.println("Simulation done !");
        System.exit(0);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        SMA sma = new SMA();
        sma.init();
        sma.run();
    }

}
