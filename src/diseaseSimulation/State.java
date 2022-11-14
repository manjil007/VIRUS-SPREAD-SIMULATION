package diseaseSimulation;

import javafx.scene.paint.Color;

/**
 * Different possible health state of an agent.
 * @author Manjil Pradhan
 */
public enum State {
    VULNERABLE {
        public Color getColor(){return Color.BLUE;}
    },
    INCUBATING {
        public Color getColor(){return Color.ORANGE;}
    },
    SICK {
        public Color getColor(){return Color.RED;}
    },
    IMMUNE {
        public Color getColor(){return Color.GREEN;}
    },
    DEAD {
        public Color getColor(){return Color.BLACK;}
    };

    public abstract Color getColor();
}
