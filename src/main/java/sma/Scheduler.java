package sma;

import models.Parameters;
import utils.YamlReader;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Scheduler{

    Parameters parameters;
    List<Agent> agents;
    Environment environment;
    Random r;
    Stack<Integer> executionOrder;

    public Scheduler() throws IOException {
        parameters = YamlReader.readFile("parameters.yaml");
        agents = new ArrayList<>();
        r = new Random(parameters.getSeed());
        executionOrder = new Stack<>();
    }

    private void populateEnvironment() {
        for (int i = 0; i<parameters.getPopulation();i++) {
            Point position = new Point(r.nextInt(parameters.getSize()+1),r.nextInt(parameters.getSize()+1));
            while(!environment.isCaseEmpty(position)) {
                position = new Point(r.nextInt(parameters.getSize()+1),r.nextInt(parameters.getSize()+1));
            }
            Agent agent = new Agent(position,environment,parameters.getSeed()+i);
            agents.add(agent);
            environment.fillCase(agent,position);
        }
    }

    public void init() {
        environment = new Environment(parameters.getSize());
        populateEnvironment();
    }

    private void generateExecutionOrder() {
        for (int i = 0; i < parameters.getPopulation(); i++) {
            executionOrder.add(i);
        }
        Collections.shuffle(executionOrder,r);
    }

    private void wakeAgents() {
        while (!executionOrder.isEmpty()) {
            agents.get(executionOrder.pop()).move();
        }
    }

    public void run() throws InterruptedException {
        while (true) {
            generateExecutionOrder();
            wakeAgents();
            System.out.println(environment);
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Scheduler scheduler = new Scheduler();
        scheduler.init();
        scheduler.run();
    }
}
