package diseaseSimulation;

/**
 * This class is responsible for transmitting the message between
 * agents.
 * @author Manjil Pradhan
 */
public class Message {
    private State state = State.VULNERABLE;
    private MessageType messageType;

    /**
     * Constructor class of the diseaseSimulation.Message class.
     * @param type message type
     */
    public Message(MessageType type){
        this.messageType = type;
    }

    public Message(MessageType type, State state){
        this.messageType = type;
        this.state = state;
    }

    /**
     * returns the state of the agents
     * @return diseaseSimulation.State
     */
    public State getState(){
        return state;
    }

    /**
     * returns the message type of the agent.
     * @return diseaseSimulation.MessageType
     */
    public MessageType getMessageType(){
        return messageType;
    }
}
