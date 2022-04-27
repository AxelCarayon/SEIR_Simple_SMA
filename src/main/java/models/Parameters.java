package models;

@SuppressWarnings("unused")
public class Parameters {

    private int seed;
    private int population;
    private int size;
    private int nbOfPatientZero;
    private float infectionRate;
    private float incubationRate;
    private float recoveryRate;
    private float looseImmunityRate;
    private int nbOfCycles;
    private int timeBetweenCycles;
    private boolean synchronousMode;
    private boolean graphicalMode;
    private boolean infectionStacks;

    public Parameters() {
    }

    public int getNbOfPatientZero() {return nbOfPatientZero;}

    public void setNbOfPatientZero(int nbOfPatientZero) {this.nbOfPatientZero = nbOfPatientZero;}

    public int getSeed() {return seed;}

    public void setSeed(int seed) {this.seed = seed;}

    public int getPopulation() {return population;}

    public void setPopulation(int population) {this.population = population;}

    public int getSize() {return size;}

    public void setSize(int size) {this.size = size;}

    public float getInfectionRate() { return infectionRate; }

    public void setInfectionRate(float infectionRate) {this.infectionRate = infectionRate;}

    public float getIncubationRate() {return incubationRate;}

    public void setIncubationRate(float incubationRate) {this.incubationRate = incubationRate;}

    public float getRecoveryRate() {return recoveryRate;}

    public void setRecoveryRate(float recoveryRate) {this.recoveryRate = recoveryRate;}

    public float getLooseImmunityRate() { return looseImmunityRate; }

    public void setLooseImmunityRate(float looseImmunityRate) { this.looseImmunityRate = looseImmunityRate; }

    public int getNbOfCycles() {return nbOfCycles;}

    public void setNbOfCycles(int nbOfCycles) {this.nbOfCycles = nbOfCycles;}

    public boolean isSynchronousMode() {return synchronousMode;}

    public void setSynchronousMode(boolean synchronousMode) {this.synchronousMode = synchronousMode;}

    public int getTimeBetweenCycles() { return timeBetweenCycles; }

    public void setTimeBetweenCycles(int timeBetweenCycles) { this.timeBetweenCycles = timeBetweenCycles; }

    public boolean isGraphicalMode() { return graphicalMode; }

    public void setGraphicalMode(boolean graphicalMode) { this.graphicalMode = graphicalMode; }

    public boolean isInfectionStacks() { return infectionStacks; }

    public void setInfectionStacks(boolean infectionStacks) { this.infectionStacks = infectionStacks; }
}
