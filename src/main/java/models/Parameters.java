package models;

public class Parameters {

    int seed;
    int population;
    int size;
    int nbOfPatientZero;
    float infectionChance;
    float incubationRate;
    float recoveryRate;
    int nbOfCycles;

    public Parameters() {
    }

    public int getNbOfPatientZero() {return nbOfPatientZero;}

    public void setNbOfPatientZero(int nbOfPatientZero) {this.nbOfPatientZero = nbOfPatientZero;}

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getInfectionChance() { return infectionChance; }

    public void setInfectionChance(float infectionChance) {
        this.infectionChance = infectionChance;
    }

    public float getIncubationRate() {
        return incubationRate;
    }

    public void setIncubationRate(float incubationRate) {
        this.incubationRate = incubationRate;
    }

    public float getRecoveryRate() {
        return recoveryRate;
    }

    public void setRecoveryRate(float recoveryRate) {
        this.recoveryRate = recoveryRate;
    }

    public int getNbOfCycles() {
        return nbOfCycles;
    }

    public void setNbOfCycles(int nbOfCycles) {
        this.nbOfCycles = nbOfCycles;
    }
}
