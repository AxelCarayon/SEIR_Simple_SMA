package sma;

import agents.Agent;
import environment.Environment;
import models.Parameters;
import agents.RandomWalkingAgent;
import environment.SEIRSEnvironment;
import scheduler.Scheduler;
import utils.StatsRecorder;
import utils.YamlReader;
import view.DisplaySquaredEnvironment;
import view.FrameBuilder;
import view.StatisticsCanvas;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

@SuppressWarnings("InfiniteLoopStatement")
public class SEIRS_SMA implements SMA{

    private final Parameters parameters;
    private RandomWalkingAgent[] agents;
    private SEIRSEnvironment environment;
    private Scheduler scheduler;
    private StatisticsCanvas statisticsCanvas;
    private DisplaySquaredEnvironment display;

    private HashMap<String,Integer> stats;

    public SEIRS_SMA() {
        parameters = YamlReader.getParams();
        agents = new RandomWalkingAgent[parameters.getPopulation()];
    }

    private void initGraphics() {
        statisticsCanvas = new StatisticsCanvas(500,500);
        display = new DisplaySquaredEnvironment(environment,agents);
        FrameBuilder frameBuilder = new FrameBuilder();

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

    private void doNextCycle(){
        scheduler.doNextCycle();
        stats = environment.getAgentStatus();
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

    @Override
    public void init(Environment environment, Scheduler scheduler, Agent[] agents) {
        this.agents = (RandomWalkingAgent[]) agents;
        this.scheduler = scheduler;
        scheduler.init(agents);
        this.environment = (SEIRSEnvironment)environment;
        this.environment.initiateChunks();
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
}
