package sma;

import models.Parameters;
import utils.YamlReader;
import view.FrameBuilder;
import view.GraphicEnvironment;
import view.StatisticsCanvas;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SMA {

    private Parameters parameters;
    private Random r;
    private Agent[] agents;
    private GraphicEnvironment environment;
    private Scheduler scheduler;
    private StatisticsCanvas statisticsCanvas;
    private JFrame window;

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
            Point position = new Point(r.nextInt(parameters.getSize()+1),r.nextInt(parameters.getSize()+1));
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
        infectPatientZero();

        frameBuilder.addComponent(environment,FrameBuilder.TOP);
        frameBuilder.addComponent(statisticsCanvas,FrameBuilder.RIGHT);
        window = frameBuilder.buildWindow();

        scheduler = new Scheduler(agents, parameters.getSeed());
    }

    public void run() throws InterruptedException {
        while (true) {
            scheduler.nextCycle();
            environment.repaint();
            statisticsCanvas.updateValues(environment.getAgentStatus());
            statisticsCanvas.repaint();
            Thread.sleep(100);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SMA sma = new SMA();
        sma.init();
        sma.run();
    }

}
