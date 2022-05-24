# SEIR_Simple_SMA

## About the projet

The project is a simulation made from scratch of a [multi-agent](https://en.wikipedia.org/wiki/Multi-agent_system) based simulation of a [SEIR/SEIRS infection model](https://www.nature.com/articles/s41592-020-0856-2). This code is aimed to be as repeatable and reproducible as possible and is part of a collaborative work for a workshop from the [RED network](https://devs-network.org/jfms-2022/). 

Agents are autonomous, walks randomly in a squared environment.

You can chance the parameters of the simulation in the parameters.yaml file (see the [setting the parameters](#setting-the-parameters) section for more information).


### Built With
[Intelij IDEA](https://www.jetbrains.com/fr-fr/idea/), [Maven](https://maven.apache.org/)

## Getting started

### Prerequisites

Java 17+, Python 3.10

### Installation

1. Clone the repo
```bash
git clone git@github.com:AxelCarayon/SEIR_Simple_SMA.git
```

2. Install dependencies
```bash
mvn clean install
```

3. Run simulation alone
```bash
java -jar out/artifacts/SEIR_Simple_SMA_jar/SEIR_Simple_SMA.jar
```

3. Run from python script and generate stats
```bash
python3 outputToGraph.py
```

## Setting the parameters

On start, the simulation get the parameters from the file [parameters.yaml](https://github.com/AxelCarayon/SEIR_Simple_SMA/blob/main/src/main/resources/parameters.yaml). Below is a list of each parameters and what they means.

```graphicalMode : Boolean```

When true, will display an IHM when running a simulation

```incubationRate : Float [0..1]```

Chances each cycle that an EXPOSED agent become INFECTED.

```infectionRate : Float [0..1]```

Chances each cycle that an SUCEPTIBLE agent become EXPOSED if an INFECTED agent is nearby.

```looseImmunityRate : Float [0..1]```

Chances each cycle that an RECOVERED agent become SUCEPTIBLE again.

```recoveryRate : Float [0..1]```

Chances each cycle that an INFECTED agent become RECOVERED.

```nbOfCycles : Integer```

Determines for how much cycle the simulation will run. 

Setting it to -1 or lower will run the simulation until it's manually stopped.

```nbOfPatientZero : Integer```

Determines how much agents will be already INFECTED when the simulation starts.

```population : Integer```

How much agents will be present in the simuation.

```recordExperiment : Boolean```

If true, the order in wich the agents wake up in the simuation will be recorded.

```playRecord : Boolean```

If true, will use a scheduler that will wake up agents accordingly to the record.

```seed : Integer```

Seed all the random elements in the simulation.

See the [How the random works](#how-the-random-works) section for more details.

```size : Integer```

Set the size in pixels of the square world.

```wrappingWorld : Boolean```

If true, agents will be allowed to wrap to the other side when close to a border.

```synchronousMode : Boolean```

If true, will use a synchronous, seeded scheduler. It is slower but more predictable.

If false, will use a asynchronous scheduler. Faster but results might varies between multiple executions, **even on the same seed**.

```timeBetweenCycles : Boolean```

Delay between each new cycles, in milliseconds. 0 or lower will try make the simulation run as fast as possible.

```infectionStacks : Boolean```

If true, a SUCEPTIBLE agent will roll for infection for **every INFECTED agent nearby**.

If false, a SUCEPTIBLE agent with INFECTED agents nearby will roll **only once, no matter how much infected agents are close.**

## How the random works

All random elements extends the [Randomized.java](https://github.com/AxelCarayon/SEIR_Simple_SMA/blob/main/src/main/java/behaviors/Randomized.java) abstract class.

This class assure that all random elements use the same pseudo-random generator algorithm (SHA1PRNG by SUN).

However, this does not means all agents share the same seed nor that each execution will be the same, **even on the same seed**.

This section will explicit how all the random elements are seeded and interacts and it was designed this way.

### Infecting patient zero

Between multiple runs, given the same seed, the same agent(s) will always be infected to ensure the same starting situation.

### Seeding agents

Each agent will be instanciated with the **same seed and the previous agent, plus one**.
This mean that on a simulation of 2000 agents with the seed 120, all agents will **all have a different seed, between 120 and 2120**.

This choice was made to give all agents the same comportement between multiple runs with the same seed without them sharing the same comportment.

### Waking up the agents

If you use the synchronous scheduler, it is single threaded and the wake up orded will be seeded. Between multiple re-runs given the same seed, this assure that all agents will always wake up in the same order. However, given it's single-threaded nature, it will be slower and less realistic.

If you use the asynchronous scheduler, it's multi-threaded, all agents will wake up each cycle, but you given it's multi-threaded nature, you can't predict wich agent will wake up first, **even on the same seed**.

### Agents behavior

When given multiple possible directions, the agent will choose randomly one of them.

The seed ensure that on multiple executions, given the same seed, the agent will always choose the same direction.

### Changing states

Every cycle, on a changing state, an agent will make a roll a percentage and will change if lower than the percentage in the parameters file.
This assures that on every run with the same seed and under the same condition, the same agent will always be changing state at the same time.

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Licence

Distributed under the [APACHE2 Licence](https://www.apache.org/licenses/LICENSE-2.0)

## Contact
Axel Carayon - axel.carayon@irit.fr
