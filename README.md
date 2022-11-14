## CS351
# DISEASE SIMULATION BY RAJU NAYAK AND MANJIL PRADHAN

## Disease Simulation
This program is a visual simulation of disease spreading among the population based on the configuration file provided 
by the user. 

## Design
The Simulation GUI displays the simulation of disease being spread among the agents. The diseaseSimulation.Manager class is responsible 
for managing the simulation by constantly tracking the agent's health state, and
behavior of agents. It also needs to track the distance between agents to determine the neighbors of each of the agent. 
The diseaseSimulation.Agent class has its own thread, so each agent runs on it own thread, which means it continuously wandering within the given
boundary, and transitioning its health state in response to messages received.
The other classes are helper classes for representing agent's parameter, and messages that are transmitted to each agent
so that they can update their health state. 

## Configuration File: How to set up
The user must follow the guidelines provided below for the program to understand the parameter.Though there is no
requirement of the order on which parameter should be provided, but the user must provide the name of the parameter. The
user must include following parameters in their configuration file:
1. random 
2. InitialSick 
3. ExposureDistance 
4. Incubation 
5. Sick 
6. Recover
7. Dimensions
8. Wander

## diseaseSimulation.State
There are 5 different health state of the agent. Most of the agent starts from vulnerable state, but when they come in close
contact with the sick agent, they transform to incubating, and then to sick. The agent might get immune or sick depending on 
the recovery probability of the agent. 

## How to run the Simulation
The user starts with uploading the configuration file in .txt format that has all required parameters. After uploading the file, user has to submit the file, by 
clicking the submit button, and click on start to start the simulation. The simulation stops when there are no possibility 
of transition. If the user want to start the simulation again, they have to click on restart button, and restart the simulation.

## diseaseSimulation.Message Log
The message log box on the right side of the window displays all the change in agent's state. When there is a change in 
the state of any one of the agent, the message log will track the change, and display it to the user.

## KNOWN ISSUES
1. This program can only run when the user provided configuration files. Without configuration file, it will display 
error. 
2. If the user sets the parameter with more than two decimal places, it might cause the value in text box to display 0.
3. The graph does not shrink to fit the plot area, it continues plotting outside of plot area which can't be seen.
