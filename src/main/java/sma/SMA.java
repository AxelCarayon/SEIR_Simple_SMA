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
import java.sql.SQLOutput;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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
        initScheduler();
        if (parameters.isGraphicalMode()) {
            initGraphics();
        }
    }

    private void initGraphics() {
        statisticsCanvas = new StatisticsCanvas(500,500);
        display = new DisplaySquaredEnvironment(environment,agents);
        frameBuilder = new FrameBuilder();

        frameBuilder.addComponent(display,FrameBuilder.TOP);
        frameBuilder.addComponent(statisticsCanvas,FrameBuilder.RIGHT);
        frameBuilder.buildWindow();
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
        StatsRecorder.writeToCSV(stats,"src/main/resources/output.csv");
        if (parameters.isGraphicalMode()) {
            updateGraphics();
        }
        if (parameters.getTimeBetweenCycles() > 0) {
            Thread.sleep(parameters.getTimeBetweenCycles());
        }
    }

    public void run() throws IOException, InterruptedException {
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

    public static void main(String[] args) throws InterruptedException, IOException {
        SMA sma = new SMA();
        sma.init();
        sma.run();
    }

}
