/**
 * This class represents the agents, and encapsulates the behaviors
 * and states of agents such as states, moving randomly, etc.
 * @author Manjil Pradhan
 */

package diseaseSimulation;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class Agent implements Runnable{
    static int agentNumber = 0;
    private State healthState;
    private Point2D location;
    private final int incubatingTime, sickTime;
    private final int rate, num;
    private final double recoveryProb; //reanimateProb
    private ArrayList<Agent> neighbors;
    private long lastWander, infectedAgents = 0, lastSeek = 0;
    private final Random random;
    private final String name;
    private final LinkedBlockingDeque<Message> message;
    private final Object healthLock, lockLocation, neighborLock;
    public final int maxX, maxY;
    public final boolean moveRandomly;

    /**
     * Constructor for diseaseSimulation.Agent.
     * @param location initial Point2D locations of the agent
     * @param parameters all the agents parameters such as incubation period,
     *                   sickness period, recovery probability, etc.
     */
    public Agent(Point2D location, Parameter parameters){
        healthState = State.VULNERABLE;
        this.location = location;
        incubatingTime = parameters.getIncubationTime();
        sickTime = parameters.getSicknessTime();
        rate = parameters.getSpeed();
        recoveryProb = parameters.getRecoveryProb();
        moveRandomly = parameters.isMoving();

        neighbors = new ArrayList<>();
        this.random = new Random();
        lastWander = System.currentTimeMillis();
        message = new LinkedBlockingDeque<>();
        agentNumber += 1;
        num = agentNumber;
        name = "diseaseSimulation.Agent" + agentNumber;

        healthLock = new Object();
        lockLocation = new Object();
        neighborLock = new Object();

        maxX = parameters.getMaxX();
        maxY = parameters.getMaxY();
    }

    /**
     * returns Point2D location of the agent.
     * @return location
     */
    public Point2D getLocation(){
        synchronized (lockLocation){
            return location;
        }
    }

    /**
     * returns health state of an agent.
     * @return health state
     */
    public State getHealthState(){
        synchronized (healthLock){
            return healthState;
        }
    }

    /**
     * sets the list of neighbor of the agents.
     * @param neighbors
     */
    public void setNeighbors(ArrayList<Agent> neighbors){
        synchronized (neighborLock){
            this.neighbors = neighbors;
        }
    }

    /**
     * sets the health state of the agent.
     * @param state health state
     */
    public void setHealthState(State state){
        synchronized (healthLock){
            this.healthState = state;
        }
    }

    /**
     * sets the message for the agent.
     * @param msg
     */
    public void transmitMessage(Message msg) {
        try{
            message.put(msg);
        }catch(InterruptedException e){}
    }

    /**
     * This class is responsible for transmitting the disease
     * by sending message to its neighbors.
     */
    public void transmitDisease(){
        synchronized (neighborLock){
            for (Agent a : neighbors){
                if (a.getHealthState() == State.VULNERABLE){
                    a.transmitMessage(new Message(MessageType.MOVING, State.INCUBATING));
                }
            }
        }
    }

    /**
     * This function makes the agent move around randomly, if they
     * move out of given boundary, it brings back within the given boundary.
     * @param now current time
     * @param rate rate of movement
     */
    private void moveRandomly(long now, int rate){
        double timeLapse = (now - lastWander) / 1000.0;
        double direction = 2 * Math.PI * random.nextDouble();

        double posX = rate * timeLapse * Math.cos(direction);
        double posY = rate * timeLapse * Math.sin(direction);

        synchronized (lockLocation) {
            posX += this.location.getX();
            posY += this.location.getY();

            while (posX > maxX){
                posX -= maxX;
            }

            while (posX < 0){
                posX += maxX;
            }

            while (posY > maxY){
                posY -= maxY;
            }

            while (posY < 0){
                posY += maxY;
            }
            this.location = new Point2D(posX, posY);
        }
        lastWander = now;
    }

    /**
     * This class begins the agent's life, moving the agents randomly,
     * receiving and transmitting the messages, and infecting neighbors.
     * The thread of the agents end when the health state changes to Dead.
     */
    public void run(){
        Message msg;
        int timeLapse;

        while (this.getHealthState() != State.DEAD){
            if (moveRandomly && getHealthState() != State.DEAD){
                moveRandomly(System.currentTimeMillis(), rate);
            }
            if (getHealthState() == State.SICK){
                transmitDisease();
            }

            try{
                msg = message.poll(100, TimeUnit.MILLISECONDS);
            }catch (InterruptedException e){continue;}

            if (msg == null){
                continue;
            }

            if (msg.getMessageType() == MessageType.MOVING) {
                if (msg.getState() == State.INCUBATING) {
                    if (getHealthState() == State.VULNERABLE) {
                        new Thread(new UpdateState(this, State.SICK,
                                incubatingTime * 1000)).start();
                        setHealthState(msg.getState());
                    }
                }else if (msg.getState() == State.SICK){
                    if (getHealthState() == State.VULNERABLE ||
                            getHealthState() == State.INCUBATING){
                        if (random.nextDouble() < recoveryProb){
                            new Thread(new UpdateState(this, State.IMMUNE,
                                    sickTime * 1000)).start();
                        }else{
                            new Thread(new UpdateState(this, State.DEAD,
                                    sickTime * 1000)).start();
                        }
                        setHealthState(msg.getState());
                    }
                }else if (msg.getState() == State.DEAD){
                    setHealthState(State.DEAD);
                }else if (msg.getState() == State.IMMUNE){
                    if (getHealthState() == State.SICK){
                        if (random.nextDouble() < recoveryProb){
                            new Thread(new UpdateState(this, State.VULNERABLE,
                                    sickTime * 1000)).start();
                            setHealthState(State.VULNERABLE);
                        }else{
                            setHealthState(msg.getState());
                        }
                    }

                }
            }else if (msg.getMessageType() == MessageType.STOP){ // when msg.getMessageType == STOP
                return;
            }
        }
    }

    /**
     * This class checks if the agents is moving or stable.
     * @return
     */
    public boolean isMoving(){
        return message.isEmpty();
    }

    /**
     * Helped function to start the thread of each agent.
     */
    public void start(){
        new Thread(this).start();
    }
}


