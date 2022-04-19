package models;

public class Parameters {

    private int seed;
    private int population;
    private int size;
    private int nbOfPatientZero;
    private float infectionChance;
    private float incubationRate;
    private float recoveryRate;
    private int nbOfCycles;
    private boolean synchronousMode;

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

    public float getInfectionChance() { return infectionChance; }

    public void setInfectionChance(float infectionChance) {this.infectionChance = infectionChance;}

    public float getIncubationRate() {return incubationRate;}

    public void setIncubationRate(float incubationRate) {this.incubationRate = incubationRate;}

    public float getRecoveryRate() {return recoveryRate;}

    public void setRecoveryRate(float recoveryRate) {this.recoveryRate = recoveryRate;}

    public int getNbOfCycles() {return nbOfCycles;}

    public void setNbOfCycles(int nbOfCycles) {this.nbOfCycles = nbOfCycles;}

    public boolean isSynchronousMode() {return synchronousMode;}

    public void setSynchronousMode(boolean synchronousMode) {this.synchronousMode = synchronousMode;}
}
