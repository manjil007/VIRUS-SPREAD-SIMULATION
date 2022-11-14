package diseaseSimulation;

/**
 * This class is responsible for encapsulating all the
 * parameters of an individual agent.
 * @author Manjil Pradhan
 */
public class Parameter {
    private int incubationTime, sicknessTime; //reanimateTime, starvationTime
    private int infectionDistance;
    private double recoveryProb; //reanimateProbability
    private int maxX, maxY;
    private int initialSick, initialImmune;
    private int speed;
    private boolean move; //zombies

    /**
     * Constructor for diseaseSimulation.Parameter class.
     */
    public Parameter(){
        this.incubationTime = 5;
        this.sicknessTime = 10;
        this.recoveryProb = 0.95;
        this.speed = 100;
        this.infectionDistance = 50;
        this.maxX = 500;
        this.maxY = 500;
        this.move = false;

        //if zombies include zombies parameter
    }


    /**
     * returns the incubation time
     * @return incubation time
     */
    public int getIncubationTime() {
        return incubationTime;
    }

    /**
     * sets the incubation time
     * @param incubationTime
     */
    public void setIncubationTime(int incubationTime) {
        this.incubationTime = incubationTime;
    }

    /**
     * returns the sickness time.
     * @return sickness time
     */
    public int getSicknessTime() {
        return sicknessTime;
    }

    /**
     * sets the sickness time
     * @param sicknessTime
     */
    public void setSicknessTime(int sicknessTime) {
        this.sicknessTime = sicknessTime;
    }

    /**
     * returns the infection distance.
     * @return infection distance
     */
    public int getInfectionDistance() {
        return infectionDistance;
    }

    /**
     * sets the infection distance
     * @param infectionDistance
     */
    public void setInfectionDistance(int infectionDistance) {
        this.infectionDistance = infectionDistance;
    }

    /**
     * returns the recovery probability of an agent.
     * @return recovery probability
     */
    public double getRecoveryProb() {
        return recoveryProb;
    }

    /**
     * sets the recovery probability of an agent
     * @param recoveryProb
     */
    public void setRecoveryProb(double recoveryProb) {
        this.recoveryProb = recoveryProb;
    }

    /**
     * returns of max width of simulation
     * @return
     */
    public int getMaxX() {
        return maxX;
    }

    /**
     * sets the maximum width of simulation
     * @param maxX
     */
    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    /**
     * @return maximum height of simulation
     */
    public int getMaxY() {
        return maxY;
    }

    /**
     * @param maxY maximum width of simulation
     */
    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    /**
     * @return number of sick agents initialized
     */
    public int getInitialSick() {
        return initialSick;
    }

    /**
     * @param initialSick numnber of sick agents
     */
    public void setInitialSick(int initialSick) {
        this.initialSick = initialSick;
    }

    /**
     * @return number of immune agents
     */
    public int getInitialImmune() {
        return initialImmune;
    }

    /**
     * @param initialImmune number of immune agents
     */
    public void setInitialImmune(int initialImmune) {
        this.initialImmune = initialImmune;
    }

    /**
     * @return returns the rate of movement
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @param speed rate of movement.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * @return is stable or moving.
     */
    public boolean isMoving() {
        return move;
    }

    /**
     * @param move boolean value of move
     */
    public void setMove(boolean move) {
        this.move = move;
    }
}
