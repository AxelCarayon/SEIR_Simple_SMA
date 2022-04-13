package sma;

import models.Parameters;
import utils.DataAdapter;
import utils.Pair;
import utils.StatsRecorder;
import utils.YamlReader;
import view.FrameBuilder;
import view.GraphicEnvironment;
import view.StatisticsCanvas;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class SMA {

    private Parameters parameters;
    private Random r;
    private Agent[] agents;
    private GraphicEnvironment environment;
    private Scheduler scheduler;
    private StatisticsCanvas statisticsCanvas;

    private HashMap<Agent.State, Pair<Integer,Color>> stats;

    private FrameBuilder frameBuilder;

    public SMA() {
        parameters = YamlReader.getParams();
        r = new Random(parameters.getSeed());
        agents = new Agent[parameters.getPopulation()];

        statisticsCanvas = new StatisticsCanvas(500,500);
        frameBuilder = new FrameBuilder();
    }


    private void populateEnvironment() {
        for (int i = 0; i<parameters.getPopulation();i++) {
            Point position = new Point(r.nextInt(parameters.getSize()),r.nextInt(parameters.getSize()));
            Agent agent = new Agent(position,parameters.getSeed()+i,environment);
            agents[i] = agent;
        }
    }

    private void infectPatientZero() {
        for (int i=0 ; i< parameters.getNbOfPatientZero(); i++) {
            agents[(r.nextInt(parameters.getPopulation()))].setState(Agent.State.INFECTED);
        }
    }

    public void init() {
        environment = new GraphicEnvironment(parameters.getSize(),parameters.getSize(),agents);
        populateEnvironment();
        environment.initiateChunks();
        infectPatientZero();

        frameBuilder.addComponent(environment,FrameBuilder.TOP);
        frameBuilder.addComponent(statisticsCanvas,FrameBuilder.RIGHT);
        frameBuilder.buildWindow();

        scheduler = new Scheduler(agents, parameters.getSeed());
        statisticsCanvas.updateValues(environment.getAgentStatus());
        statisticsCanvas.repaint();
    }

    private void updateGraphics(){
        statisticsCanvas.updateValues(stats);
        statisticsCanvas.repaint();
        environment.repaint();
    }

    public void run() throws IOException, InterruptedException {
        while (true) {
            scheduler.nextCycle();
            stats = environment.getAgentStatus();
            updateGraphics();
            StatsRecorder.writeToCSV(DataAdapter.adaptData(stats),"output.csv");
            Thread.sleep(100);
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        SMA sma = new SMA();
        sma.init();
        sma.run();
    }

}
