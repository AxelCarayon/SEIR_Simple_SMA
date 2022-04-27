package sma;

import agents.SEIRSAgent;
import agents.states.InfectedSEIRSState;
import environment.SEIRSEnvironment;
import models.Parameters;
import agents.RandomWalkingAgent;
import environment.ChunkedSEIRSEnvironment;
import scheduler.FairAsynchronousScheduler;
import scheduler.FairSynchronousScheduler;
import scheduler.Scheduler;
import utils.StatsRecorder;
import utils.YamlReader;
import view.DisplaySquaredEnvironment;
import view.FrameBuilder;
import view.StatisticsCanvas;

import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

@SuppressWarnings("InfiniteLoopStatement")
public class SEIRS_SMA implements SMA{

    private Parameters parameters;
    private RandomWalkingAgent[] agents;
    private SEIRSEnvironment environment;
    private Scheduler scheduler;
    private StatisticsCanvas statisticsCanvas;
    private DisplaySquaredEnvironment display;
    private Random r;

    private HashMap<String,Integer> stats;

    private void initGraphics() {
        statisticsCanvas = new StatisticsCanvas(500,500);
        display = new DisplaySquaredEnvironment(environment,agents);
        FrameBuilder frameBuilder = new FrameBuilder();

        frameBuilder.addComponent(display,FrameBuilder.TOP);
        frameBuilder.addComponent(statisticsCanvas,FrameBuilder.RIGHT);
        frameBuilder.buildWindow();
        statisticsCanvas.updateValues(environment.getAgentsStatus());
        statisticsCanvas.repaint();
    }

    private void updateGraphics(){
        display.repaint();
        statisticsCanvas.updateValues(stats);
        statisticsCanvas.repaint();
    }

    private void doNextCycle(){
        scheduler.doNextCycle();
        stats = environment.getAgentsStatus();
        try{
            StatsRecorder.writeToCSV(stats,"src/main/resources/output.csv");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        if (parameters.isGraphicalMode()) {
            updateGraphics();
        }
        if (parameters.getTimeBetweenCycles() > 0) {
            try {
                Thread.sleep(parameters.getTimeBetweenCycles());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initPopulation() {
        for (int i = 0; i<parameters.getPopulation();i++) {
            Point position = new Point(r.nextInt(parameters.getSize()),r.nextInt(parameters.getSize()));
            RandomWalkingAgent agent = new RandomWalkingAgent(position,parameters.getSeed()+i,environment);
            agents[i] = agent;
        }
    }

    private void infectPatientZero() {
        for (int i=0 ; i< parameters.getNbOfPatientZero(); i++) {
            SEIRSAgent agent = agents[(r.nextInt(parameters.getPopulation()))];
            while (agent.getState() instanceof InfectedSEIRSState) {
                agent = agents[(r.nextInt(parameters.getPopulation()))];
            }
            agent.changeState(new InfectedSEIRSState(agent));
        }
    }

    private void initScheduler() {
        if (parameters.isSynchronousMode()) {
            scheduler = new FairSynchronousScheduler(parameters.getSeed());
        } else {
            scheduler = new FairAsynchronousScheduler();
        }
        scheduler.init(agents);
    }


    @Override
    public void init() {
        parameters = YamlReader.getParams();
        r = new Random(parameters.getSeed());
        agents = new RandomWalkingAgent[parameters.getPopulation()];
        environment = new ChunkedSEIRSEnvironment(parameters.getSize(),agents);
        initPopulation();
        infectPatientZero();
        initScheduler();
        initGraphics();
    }


    @Override
    public void run() {
        Instant startTime = Instant.now();
        System.out.println("Starting simulation at : "+ Date.from(startTime));
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
            Instant endTime = Instant.now();
            System.out.println("Simulation done !");
            Duration duration = Duration.between(startTime,endTime);
            System.out.println("Elapsed time : " + duration.toHoursPart() + " hours, " + duration.toMinutesPart() + " minutes, " + duration.toSecondsPart() + "seconds.");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SMA sma = new SEIRS_SMA();
        sma.init();
        sma.run();
    }
}
