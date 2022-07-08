package sma;

import agents.Agent;
import agents.RandomWalkingAgent3P;
import agents.seirs.*;
import agents.states.InfectedSEIRSState;
import behaviors.Randomized;
import environment.SEIRSEnvironment;
import environment.WrappingChunkedSEIRSEnvironment;
import models.Parameters;
import environment.ChunkedSEIRSEnvironment;
import scheduler.DeterministScheduler;
import scheduler.FairAsynchronousScheduler;
import scheduler.FairSynchronousScheduler;
import scheduler.Scheduler;
import utils.StatsRecorder;
import utils.YamlReader;
import view.DisplaySquaredEnvironment;
import view.FrameBuilder;
import view.StatisticsCanvas;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"InfiniteLoopStatement", "ResultOfMethodCallIgnored"})
public class SEIRS_SMA extends Randomized implements SMA{

    private final Parameters parameters;
    private final Agent[] agents;
    private SEIRSEnvironment environment;
    private Scheduler scheduler;
    private StatisticsCanvas statisticsCanvas;
    private DisplaySquaredEnvironment display;
    private final FrameBuilder fb = new FrameBuilder();

    private HashMap<String,Integer> stats;

    public SEIRS_SMA(Parameters params) {
        super(params.seed());
        parameters = YamlReader.getParams();
        r.setSeed(parameters.seed());
        if (parameters.threePhased()) {
            agents = new ThreePhasedSEIRSAgent[parameters.population()];
        }else {
            agents = new CyclicSEIRSAgent[parameters.population()];
        }
        initEnvironment();
        initPopulation();
        infectPatientZero();
        initScheduler();
        if (parameters.graphicalMode()) {
            initGraphics();
        }
    }

    private void initGraphics() {
        statisticsCanvas = new StatisticsCanvas(300,parameters.size());
        display = new DisplaySquaredEnvironment(environment,(SEIRSAgent[]) agents);

        fb.setSimulationCanvas(display);
        fb.setStatsCanvas(statisticsCanvas);

        fb.buildWindow();
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
        if (parameters.graphicalMode()) {
            updateGraphics();
        }
        if (parameters.timeBetweenCycles() > 0) {
            try {
                Thread.sleep(parameters.timeBetweenCycles());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initPopulation() {
        for (int i = 0; i<parameters.population();i++) {
            Point position = new Point(r.nextInt(parameters.size()),r.nextInt(parameters.size()));
            Agent agent;
            if (parameters.threePhased()){
                if (parameters.infectionStacks()) {
                    agent = new RandomWalkingAgent3P(position,(parameters.seed()+i),environment);
                } else {
                    agent = new FairInfectionRWAgent3P(position,(parameters.seed()+i),environment);
                }
            } else {
                if (parameters.infectionStacks()) {
                    agent = new RandomWalkingAgentCyclic(position,(parameters.seed()+i),environment);
                } else {
                    agent = new FairInfectionRWAgentCyclic(position,(parameters.seed()+i),environment);
                }
            }
            agents[i] = agent;
        }
    }

    private void infectPatientZero() {
        for (int i=0 ; i< parameters.nbOfPatientZero(); i++) {
            int nextInt = (r.nextInt(parameters.population()));
            SEIRSAgent agent = (SEIRSAgent)agents[nextInt];
            while (agent.getState() instanceof InfectedSEIRSState) {
                agent = (SEIRSAgent)agents[(r.nextInt(parameters.population()))];
            }
            agent.changeState(new InfectedSEIRSState(agent));
        }
    }

    private void initScheduler() {
        if (parameters.playRecord()) {
            if (parameters.recordExperiment()) {
                throw new IllegalStateException("You cannot record and play an experiment at the same time. " +
                        "Please check the parameters.yaml file.");
            }
            scheduler = new DeterministScheduler("src/main/resources/executionOrder.csv");
        } else {
            if (parameters.synchronousMode()) {
                scheduler = new FairSynchronousScheduler(parameters.seed());
            } else {
                scheduler = new FairAsynchronousScheduler();
            }
        }
        scheduler.init(agents);
    }

    private void initEnvironment() {
        if (parameters.wrappingWorld()) {
            environment = new WrappingChunkedSEIRSEnvironment(parameters.size(),(SEIRSAgent[]) agents);
        } else {
            environment = new ChunkedSEIRSEnvironment(parameters.size(),(SEIRSAgent[]) agents);
        }
    }

    private void recordExperiment() {
        try{
            File file = new File("src/main/resources/executionOrder.csv");
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            List<String> executionOrder= environment.getExecutionOrder();
            for (int i = 0; i < executionOrder.size()-1; i++) {
                bw.write(executionOrder.get(i)+",");
            }
            bw.write(executionOrder.get(executionOrder.size()-1));
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        calculateDensity();
        Instant startTime = Instant.now();
        System.out.println("Starting simulation at : "+ Date.from(startTime));
        if (parameters.nbOfCycles() <0) {
            while (true) {
                doNextCycle();
            }
        } else {
            int cpt = 0;
            while (cpt < parameters.nbOfCycles()) {
                doNextCycle();
                cpt++;
            }

            if (parameters.recordExperiment()) {
                recordExperiment();
            }

            Instant endTime = Instant.now();
            System.out.println("Simulation done !");
            Duration duration = Duration.between(startTime,endTime);
            System.out.println("Elapsed time : " + duration.toHoursPart() + " hours, " + duration.toMinutesPart() + " minutes, " + duration.toSecondsPart() + "seconds.");
            System.exit(0);
        }
    }

    private void calculateDensity() {
        int pixels = parameters.size()*parameters.size();
        int agents = parameters.population();
        System.out.println("Population density is : " + (float)agents/pixels + " agents per pixel.");
    }

    public static void main(String[] args) {
        SMA sma = new SEIRS_SMA(YamlReader.getParams());
        sma.run();
    }
}
