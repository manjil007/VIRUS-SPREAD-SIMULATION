package diseaseSimulation;

/**
 *This class encapsulates all the formats and configuration of the
 * agents. The parameters related to the diseaseSimulation.Agent class are configured
 * through FormatAgent class.
 * @author Manjil Pradhan
 */
public class FormatAgents {
    private Formats formatType;
    private int columns, rows, numberOfAgents;
    private int initialSick = 1;
    private int initialImmune = 0;

    /**
     * Constructor for diseaseSimulation.FormatAgents
     */
    public FormatAgents(){
        formatType = Formats.GRID;
    }

    /**
     * This class sets the format type.
     * @param format formatType
     */
    public void setFormatType(String format){
        if (format.equals("grid")){
            formatType = Formats.GRID;
        }else if (format.equals("randomGrid")){
            formatType = Formats.RANDOMGRID;
        }else if (format.equals("random")){
            formatType = Formats.RANDOM;
        }else{
            formatType = Formats.GRID;
        }
    }

    /**
     *sets the height.
     * @param columns height
     */
    public void setColumns(int columns){
        this.columns = columns;
    }

    /**
     * sets the width.
     * @param rows width
     */
    public void setRows(int rows){
        this.rows = rows;
    }

    /**
     * sets number of agents that will be initialized
     * @param numberOfAgents
     */
    public void setNumberOfAgents(int numberOfAgents){
        this.numberOfAgents = numberOfAgents;
    }

    /**
     * returns the height of the grid.
     * @return height
     */
    public int getColumns() {
        return columns;
    }

    /**
     * returns the width of the grid.
     * @return width
     */
    public int getRows() {
        return rows;
    }

    /**
     * returns the number of agents that was set or initialized
     * @return number of agents.
     */
    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    /**
     * returns the number of sick agents that was initialized
     * @return number of sick agents.
     */
    public int getInitialSick() {
        return initialSick;
    }

    /**
     * returns the number of immune agents that was initialized
     * @return number of immune agents.
     */
    public int getInitialImmune() {
        return initialImmune;
    }

    /**
     * returns the formatType of grid.
     * @return
     */
    public Formats getFormatType(){
        return this.formatType;
    }
}
