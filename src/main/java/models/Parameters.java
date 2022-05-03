package models;

@SuppressWarnings("unused")
public record Parameters(
        long seed,
        int population,
        int size,
        int nbOfPatientZero,
        float infectionRate,
        float incubationRate,
        float recoveryRate,
        float looseImmunityRate,
        int nbOfCycles,
        int timeBetweenCycles,
        boolean synchronousMode,
        boolean graphicalMode,
        boolean infectionStacks,
        boolean wrappingWorld) {
}
