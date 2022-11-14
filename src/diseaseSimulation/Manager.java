package diseaseSimulation; /**
 * This class is responsible for starting, managing, and stopping the
 * simulation. It repeatedly checks for agent's neighbors, and tracks
 * the state of simulation.
 * @author Manjil Pradhan
 */

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

public class Manager implements Runnable{
    private int width, height;
    private int transmitDistance;
    private final ArrayList<Agent> agents;
    private int ticks;

    /**
     * Constructor
     * @param width width of simulation in pixel size
     * @param height height of simulation in pixel size
     */
    public Manager(int width, int height){
        this.width = width;
        this.height = height;
        this.agents = new ArrayList<>();
    }

    public Manager(){
        this(250,250);
    }

    /**
     * This function returns list of all agents.
     * @return list of all agents
     */
    public ArrayList<Agent> getAgents(){
        return new ArrayList<>(agents);
    }

    /**
     * This function counts current agents on simulation.
     * @return Number of current agent of particular state of simulation
     */
    public EnumMap<State, Integer> getAgentCounts() {
        EnumMap<State, Integer> currentAgent = new EnumMap<>(State.class);
        for (Agent agent : agents) {
            if (!currentAgent.containsKey(agent.getHealthState())) {
                currentAgent.put(agent.getHealthState(), 0);
            }
            currentAgent.put(agent.getHealthState(), 1 + currentAgent.get(agent.getHealthState()));
        }
        return currentAgent;
    }

    /**
     * This function is responsible for instantiating the given number
     * of agents with the given parameters.
     * @param formats
     * @param parameters
     */
    public void makeAgents(FormatAgents formats, Parameter parameters){
        int columns = formats.getColumns();
        int rows = formats.getRows();
        transmitDistance = parameters.getInfectionDistance();

        if (agents.size() >= 1){
            agents.clear();
        }

        switch(formats.getFormatType()){
            case GRID -> makeGrid(columns, rows, parameters);
            case RANDOMGRID -> makeRandomGrid(columns, rows, formats.getNumberOfAgents(), parameters);
            case RANDOM -> makeRandom(formats.getNumberOfAgents(), parameters);
        }

        int i = 0;

        for (i = 0; i < formats.getInitialSick() && i < formats.getNumberOfAgents(); i++){
            Message msg = new Message(MessageType.MOVING, State.SICK);
            agents.get(i).transmitMessage(msg);
        }

        for (; i < formats.getInitialSick() + formats.getInitialImmune() &&
                i < formats.getNumberOfAgents(); i++){
            System.out.println("i : " + i);
            Message msg = new Message(MessageType.MOVING, State.IMMUNE);
            agents.get(i).transmitMessage(msg);
        }
    }

    /**
     * This function makes grid of agents with specified number of
     * rows, and columns.
     * @param columns number of columns
     * @param rows number of rows
     * @param parameters collection of agent's parameter.
     */
    private void makeGrid(int columns, int rows, Parameter parameters){
        int posX, posY;
        parameters.setMove(false);

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                posX = width / (columns + 1) * (j + 1);
                posY = height / (rows + 1) * (i + 1);
                agents.add(new Agent(new Point2D(posX, posY), parameters));
            }
        }
    }

    /**
     * This function makes random grid of agents with specified number of rows
     * and columns. It is a helper function for makeAgents function.
     * @param columns
     * @param rows
     * @param numberOfAgents
     * @param parameters
     */
    private void makeRandomGrid(int columns, int rows, int numberOfAgents, Parameter parameters){
        Random random = new Random();
        int posX, posY;
        ArrayList<Agent> otherAgents = new ArrayList<>();

        parameters.setMove(false);

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                posX = width / (columns + 1) * (j + 1);
                posY = height / (rows + 1) * (i + 1);
                otherAgents.add(new Agent(new Point2D(posX, posY), parameters));
            }
        }

        int index;
        for (int i = 0; i < numberOfAgents; i++){
            index = random.nextInt(otherAgents.size());
            agents.add(otherAgents.get(index));
            otherAgents.remove(index);
        }
    }

    /**
     * It makes the set of number of agents, each of which moves
     * randomly.
     * @param numberOfAgents
     * @param parameters
     */
    private void makeRandom(int numberOfAgents, Parameter parameters){
        Random random = new Random();
        int posX, posY;
        parameters.setMove(true);

        for (int i = 0; i < numberOfAgents; i++){
            posX = (int) (random.nextDouble() * width);
            posY = (int) (random.nextDouble() * height);
            agents.add(new Agent(new Point2D(posX, posY), parameters));
        }
    }

    /**
     * It sets the dimension of the grid.
     * @param width width of grid
     * @param height height of grid
     */
    public void setDimension(int width, int height){
        this.width = width;
        this.height = height;
    }

    /**
     * This function starts the diseaseSimulation.Manager's thread which starts the
     * simulation.
     */
    public void start(){
        new Thread(this).start();
        for (Agent agent : agents){
            agent.start();
        }
    }

    /**
     * This functions checks if the simulation has reached a stable state.
     * It is used to check if the simulation has ended.
     * @return
     */
    public boolean isSteady(){
        State state;

        for (Agent agent : agents){
            state = agent.getHealthState();
            if (!(state == State.DEAD || state == State.IMMUNE)){
                return false;
            }else if (state == State.VULNERABLE && !agent.isMoving()){
                return false;
            }
        }
        return true;
    }

    /**
     * This function checks for neighbors, and sets the neighbors, when
     * some agents come within the infection distance.
     */
    @Override
    public void run() {
        Point2D position;
        ArrayList<Agent> neigh;
        while (!isSteady()){
            for (Agent agent : agents){
                position = agent.getLocation();
                neigh = new ArrayList<>();

                for (Agent a : agents){
                    if (!agent.equals(a) && position.distance(a.getLocation()) < transmitDistance){
                        neigh.add(a);
                    }
                }
                agent.setNeighbors(neigh);
            }
            try{
                Thread.sleep(10);
            }catch (InterruptedException e){}
        }
        for (Agent ag : agents){
            ag.transmitMessage(new Message(MessageType.STOP));
        }
    }

    /**
     * @return ticks
     */
    public int getTicks() {
        return this.ticks;
    }

    /**
     * Increment of ticks
     */
    public void incTicks() {
        this.ticks++;
    }

    /**
     * Reset the simulation step
     */
    public void resetTicks() {
        this.ticks = 0;
    }
}
