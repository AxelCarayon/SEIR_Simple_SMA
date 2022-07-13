# ODD description

## Purpose and patterns

This SMA aims at simulating the spreading of a disease, following the SEIRS model.
The main goal is focused on reproductibility and aim to display what is essential to design and share so our work and results can be reproducted and re-used by others persons.

## Entities, states variables and scales

### Scales

The simulation is executed on a squared environment mesured with an abstract unit size. Size is configurable and there is no minimum or maximum.
In the (optionnal) graphical interpreation, each unit is represented by one pixel.
The simulation step is also an abstract unit and does not represent any real life unit.
The environment can be parametrable to have bounds, wich means that they can't go above the edges. If deactivated, agents wrap to the other side when they go pass a border.

### Entities

There is only one kind of entity in the environment, the **SEIRSAgent**.
Agents move on a random pattern. They will ask the environment wich they are allowed to go (up, down, left or right) and will choose one in random.
There is no collision, multiple agents can be on the same position.

The infestion spead at the individual scale. If an infected agent overlap it's position with another agent, he can spread his disease on him.

Each cycle exposed agents have a chance to become infected.
Each cycle infected agents have a chance to become recovered.
Each cycle recovered agents have a chance to become susceptible.

## Design concepts

### Basic principles

As far as the epidemiological dynamics is concerned, we rely on much scientific evidence that
the disease could be represented by a SEIRS model with an infectious state that can be
presymptomatic or symptomatic, with a certain degree of survivability of the virus
in the environment and the possibility of people being infected by it.

The individual agents’ behavior is very simple and roam aimlessly in random in the environment.

### Emergence

The main emergent (or at least complex to predict) results are the evolution of the number of
infected cases given the different parameters.
In most cases, the 4 differents populations stabilize but when given weak enough infection rate and long enough loosing immunity rate, you can obtain waves.

### Objectives

### Interaction

Agent in the *susceptible* state can be infected if they are in contact with an agent in the *infected* state.

Each susceptible agent in this situation will roll a pseudo-random number between 0 and 1 and if it's bigger than the given number in the parameters, they will switch to the exposed state.

### Observation

A graphical interface can be used if asked in the parameters file.
It will show a window that display the simulation at each step and the total of each population.
Note that it can slow down the speed of the simulation and if the environment is too big for the screen resolution, it won't fit properly.

## Initialization

In order to keep the model as generic as possible, many parameters and initial values are stored in independent external yaml file :

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
