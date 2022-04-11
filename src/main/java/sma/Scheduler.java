package sma;

import models.Parameters;
import utils.YamlReader;
import view.GraphicEnvironment;

import javax.swing.Timer;
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class Scheduler{

    private Parameters parameters;
    private Agent[] agents;
    private GraphicEnvironment gEnvironment;
    private Random r;
    private Stack<Integer> executionOrder;


    public Scheduler() {
        parameters = YamlReader.getParams();
        agents = new Agent[parameters.getPopulation()];
        r = new Random(parameters.getSeed());
        executionOrder = new Stack<>();
    }

    private void populateEnvironment() {
        for (int i = 0; i<parameters.getPopulation();i++) {
            Point position = new Point(r.nextInt(parameters.getSize()+1),r.nextInt(parameters.getSize()+1));
            Agent agent = new Agent(position,gEnvironment,parameters.getSeed()+i,i);
            agents[i] = agent;
        }
    }

    public void init() {
        gEnvironment = new GraphicEnvironment(agents);
        populateEnvironment();
        infectPatientZero();
    }

    private void infectPatientZero() {
        for (int i=0 ; i< parameters.getNbOfPatientZero(); i++) {
            agents[(r.nextInt(parameters.getPopulation()))].setState(Agent.State.INFECTED);
        }
    }

    private void generateExecutionOrder() {
        for (int i = 0; i < parameters.getPopulation(); i++) {
            executionOrder.add(i);
        }
        Collections.shuffle(executionOrder,r);
    }

    private void wakeAgents() {
        while (!executionOrder.isEmpty()) {
            agents[(executionOrder.pop())].wakeUp();
            gEnvironment.repaint();
        }
    }

    public void run() throws InterruptedException, IOException {
        while (true) {
            generateExecutionOrder();
            wakeAgents();
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Scheduler scheduler = new Scheduler();
        scheduler.init();
        scheduler.run();
    }
}
