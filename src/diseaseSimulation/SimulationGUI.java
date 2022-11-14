package diseaseSimulation;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


/**
 * This is entry point of the program. It creates GUI to run the simulation
 * based on the configuration parameters.
 * @author Raju Nayak
 */

public class SimulationGUI extends Application {
    private static int distance;
    private static int incubation;
    private static int sickTime;
    private static double recovery;
    private static int initialSick;
    private static int height;
    private static int width;
    private static int row;
    private static int col;
    private static int num;
    private static int begImmuneNo;
    private static long begTimer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Disease Simulation");

        BorderPane border = new BorderPane();
        border.setPadding(new Insets(5,5,5,5));
        border.setBackground(Background.fill(Color.POWDERBLUE));
        Scene scene = new Scene(border, 1200, 700);
        Font guiFont = Font.font("Times New Roman", FontWeight.BOLD, 16);

        // Create simulation area
        Canvas canvas = new Canvas(200,200);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Options for the drop-down menu
        ObservableList<Integer> initSickNum = FXCollections.observableArrayList();
        ObservableList<Integer> expDistanceVal = FXCollections.observableArrayList();
        ObservableList<Integer> incubationVal = FXCollections.observableArrayList();
        ObservableList<Integer> sicknessTimeVal = FXCollections.observableArrayList();
        ObservableList<Integer> initImmOpts = FXCollections.observableArrayList();

        for (int i = 0; i <= 100; i++){
            if (i <= 10){
                initSickNum.add(i);
                initImmOpts.add(i);
            }
            if (i <= 50) {
                expDistanceVal.add(i);
                incubationVal.add(i);
            }
            if (i <= 30) {
                sicknessTimeVal.add(i);
            }
        }

        ObservableList<Double> recoveryProb = FXCollections.observableArrayList(
                0.0, 0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09,
                0.1, 0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19, 0.2,
                0.21, 0.22, 0.23, 0.24, 0.25, 0.26, 0.27, 0.28, 0.29, 0.3, 0.31,
                0.32, 0.33, 0.34, 0.35, 0.36, 0.37, 0.38, 0.39, 0.4, 0.41, 0.42,
                0.43, 0.44, 0.45, 0.46, 0.47, 0.48, 0.49, 0.5, 0.51, 0.52, 0.53,
                0.54, 0.55, 0.56, 0.57, 0.58, 0.59, 0.6, 0.61, 0.62, 0.63, 0.64,
                0.65, 0.66, 0.67, 0.68, 0.69, 0.7, 0.71, 0.72, 0.73, 0.74, 0.75,
                0.76, 0.77, 0.78, 0.79, 0.8, 0.81, 0.82, 0.83, 0.84, 0.85, 0.86,
                0.87, 0.88, 0.89, 0.9, 0.91, 0.92, 0.93, 0.94, 0.95, 0.95, 0.96,
                0.97, 0.98, 0.99, 1.0
        );

        // Boxes to hold options for different configuration
        ComboBox<Integer> initSickBox = new ComboBox<>(initSickNum);
        initSickBox.setValue(1);
        initSickBox.setMaxHeight(20);
        initSickBox.setMaxWidth(150);
        ComboBox<Integer> expDistanceBox = new ComboBox<>(expDistanceVal);
        expDistanceBox.setValue(20);
        expDistanceBox.setMaxHeight(20);
        expDistanceBox.setMaxWidth(150);
        ComboBox<Integer> incubationBox = new ComboBox<>(incubationVal);
        incubationBox.setValue(5);
        incubationBox.setMaxHeight(20);
        incubationBox.setMaxWidth(150);
        ComboBox<Integer> sickTimeBox = new ComboBox<>(sicknessTimeVal);
        sickTimeBox.setValue(10);
        sickTimeBox.setMaxHeight(20);
        sickTimeBox.setMaxWidth(150);
        ComboBox<Double> recoveryBox = new ComboBox<>(recoveryProb);
        recoveryBox.setValue(0.95);
        recoveryBox.setMaxHeight(20);
        recoveryBox.setMaxWidth(150);
        ComboBox<Integer> initImmuneBox = new ComboBox<>(initImmOpts);
        initImmuneBox.setValue(0);
        initImmuneBox.setMaxHeight(20);
        initImmuneBox.setMaxWidth(150);

        // Labels for the options boxes
        Label begSickLabel = new Label("Initial Sick");
        begSickLabel.setFont(guiFont);
        Label exposeDistanceLabel = new Label("Exposure Distance");
        exposeDistanceLabel.setFont(guiFont);
        Label incubatPeriodLabel = new Label("Incubation Period");
        incubatPeriodLabel.setFont(guiFont);
        Label sickPeriodLabel = new Label("Sickness Time");
        sickPeriodLabel.setFont(guiFont);
        Label recovPeriodLabel = new Label("Recovery Probability");
        recovPeriodLabel.setFont(guiFont);
        Label initialImmuneLabel = new Label("Initial Immune");
        initialImmuneLabel.setFont(guiFont);
        Label configLabel = new Label("Configuration File");
        configLabel.setFont(guiFont);
        TextField configFile = new TextField();
        configFile.setPromptText("Add a configuration file");
        configFile.setMaxHeight(50);
        configFile.setMaxWidth(150);

        // Create buttons and event when button clicked
        Button submitButton = new Button("Submit");
        submitButton.setPrefWidth(100);
        HashMap<Integer, State> timeLapse = new HashMap<>();

        final FormatAgents FM = new FormatAgents();
        final Parameter PARAM = new Parameter();
        final Manager MGR = new Manager();
        final Stage finalStage = new Stage();
        final SimulationPlot simulationPlot = new SimulationPlot(MGR);
        final Pane plotChart = new Pane();

        submitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String configName = configFile.getText();
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new FileReader(configName));
                    String line = reader.readLine();
                    while(line != null) {
                        if(line.contains("randomgrid")) {
                            String[] randims = line.split(" ");
                            row = Integer.parseInt(randims[1]);
                            col = Integer.parseInt(randims[2]);
                            num = Integer.parseInt(randims[3]);
                            FM.setFormatType("randomgrid");
                            FM.setRows(row);
                            FM.setColumns(col);
                            FM.setNumberOfAgents(num);
                        }
                        else if(line.contains("random")) {
                            num = Integer.parseInt(line.split(" ")[1]);
                            FM.setFormatType("random");
                            FM.setNumberOfAgents(num);
                        }
                        else if(line.contains("grid")) {
                            String[] gridims = line.split(" ");
                            row = Integer.parseInt(gridims[1]);
                            col = Integer.parseInt(gridims[2]);
                            num = row * col;
                            FM.setFormatType("grid");
                            FM.setRows(row);
                            FM.setColumns(col);
                            FM.setNumberOfAgents(num);
                        }
                        else if (line.contains("move")) {
                            int speed = Integer.parseInt(
                                    line.split(" ")[1]
                            );
                            PARAM.setSpeed(speed);
                        }
                        else if(line.contains("initialsick")) {
                            String[] initConfig = line.split(" ");
                            initialSick = Integer.parseInt(initConfig[1]);
                            PARAM.setInitialSick(initialSick);
                            initSickBox.setValue(initialSick);
                        }
                        else if(line.contains("dimensions")) {
                            String[] dims = line.split(" ");
                            width = Integer.parseInt(dims[1]);
                            height = Integer.parseInt(dims[2]);

                            MGR.setDimension(width, height);
                            PARAM.setMaxX(width);
                            PARAM.setMaxY(height);
                            canvas.setHeight(height);
                            canvas.setWidth(width);
                        }
                        else if(line.contains("exposuredistance")) {
                            String[] distConfig = line.split(" ");
                            distance = Integer.parseInt(distConfig[1]);
                            PARAM.setInfectionDistance(distance);
                        }
                        else if(line.contains("incubation")) {
                            String[] incubConfig = line.split(" ");
                            incubation = Integer.parseInt(incubConfig[1]);
                            PARAM.setIncubationTime(incubation);
                        }
                        else if(line.contains("sickness")) {
                            String[] sickConfig = line.split(" ");
                            sickTime = Integer.parseInt(sickConfig[1]);
                            PARAM.setSicknessTime(sickTime);
                        }
                        else if(line.contains("recover")) {
                            String[] recovConfig = line.split(" ");
                            recovery = Double.parseDouble(recovConfig[1]);
                            PARAM.setRecoveryProb(recovery);
                        }
                        // Additional required feature
                        else if(line.contains("initialimmune")) {
                            String[] immConfig = line.split(" ");
                            begImmuneNo = Integer.parseInt(immConfig[1]);
                            PARAM.setInitialImmune(begImmuneNo);
                        }
                        line = reader.readLine();
                    }
                    reader.close();

                    finalStage.sizeToScene();

                    incubationBox.setValue(PARAM.getIncubationTime());
                    sickTimeBox.setValue(PARAM.getSicknessTime());
                    recoveryBox.setValue(PARAM.getRecoveryProb());
                    initSickBox.setValue(PARAM.getInitialSick());
                    initImmuneBox.setValue(PARAM.getInitialImmune());

                    expDistanceVal.clear();

                    for (int i = 0; i<(width); i++) {
                        expDistanceVal.add(i+1);
                    }

                    expDistanceBox.setValue(PARAM.getInfectionDistance());

                    int marginX = (int) ((canvas.getWidth() - width) / 2);
                    int marginY = (int) ((canvas.getHeight() - height) / 2);
                    gc.clearRect(0,0,canvas.getWidth(),
                            canvas.getHeight());
                    gc.setFill(Color.LIGHTGRAY);
                    gc.fillRect(marginX,marginY,width + 10,height + 10);
                }
                catch (IOException e) {
                    System.out.println("Error: File not found");
                }
            }
        });

        Button startButton = new Button("Start");
        startButton.setPrefWidth(100);

        // Vertical box for configurations and buttons
        VBox vBox = new VBox(10);
        vBox.setPrefSize(200, 600);
        CornerRadii radii = new CornerRadii(2);
        Insets insets = new Insets(2,2,2,2);
        BackgroundFill fill = new BackgroundFill(Color.SKYBLUE, radii, insets);
        Background backGround = new Background(fill);
        vBox.setBackground(backGround);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().addAll(
                begSickLabel, initSickBox, exposeDistanceLabel, expDistanceBox, incubatPeriodLabel,
                incubationBox, sickPeriodLabel, sickTimeBox, recovPeriodLabel, recoveryBox, initialImmuneLabel,
                initImmuneBox, configLabel, configFile, submitButton, startButton
        );

        // diseaseSimulation.Message box area
        TextArea msgLogBox = new TextArea();
        msgLogBox.setPrefSize(300, 400);
        msgLogBox.setEditable(false);

        Label msgLabel = new Label("Simulation diseaseSimulation.Message");
        msgLabel.setFont(guiFont);
        msgLabel.setAlignment(Pos.TOP_CENTER);
        msgLabel.setPrefSize(300,20);

        Label simPlotLabel = new Label("Agents over time");
        simPlotLabel.setFont(guiFont);
        simPlotLabel.setAlignment(Pos.TOP_CENTER);
        simPlotLabel.setPrefSize(300,20);

        VBox plotBox = new VBox();
        plotBox.setPrefSize(300,300);
        plotBox.setAlignment(Pos.TOP_CENTER);
        plotBox.getChildren().addAll(plotChart);
        plotBox.setBackground(Background.fill(Color.WHITE));
        plotBox.setPadding(new Insets(5,5,5,5));

        // Status area
        VBox stateBox = new VBox();
        stateBox.setPrefSize(300, 700);
        stateBox.setBackground(backGround);
        stateBox.setAlignment(Pos.TOP_CENTER);
        stateBox.getChildren().addAll(msgLabel, msgLogBox, simPlotLabel, plotBox);

        // Create circles and labels for the hBox
        Circle vulnerableCircle = new Circle(8, Color.BLUE);
        Circle incubatingCircle = new Circle(8, Color.ORANGE);
        Circle sickCircle = new Circle(8, Color.RED);
        Circle immuneCircle = new Circle(8, Color.GREEN);
        Circle deadCircle = new Circle(8, Color.BLACK);
        Label vulnerableLabel = new Label("Vulnerable");
        vulnerableLabel.setFont(guiFont);
        Label incubatingLabel = new Label("Incubating");
        incubatingLabel.setFont(guiFont);
        Label sickLabel = new Label("Sick");
        sickLabel.setFont(guiFont);
        Label immuneLabel = new Label("Immune");
        immuneLabel.setFont(guiFont);
        Label deadLabel = new Label("Dead");
        deadLabel.setFont(guiFont);

        // create HBoxes for the hBox
        HBox vulnerableHBox = new HBox();
        HBox incubatingHBox = new HBox();
        HBox sickHBox = new HBox();
        HBox immuneHBox = new HBox();
        HBox deadHBox = new HBox();

        // Add labels and circles to the hBox
        vulnerableHBox.getChildren().addAll(vulnerableLabel, vulnerableCircle);
        incubatingHBox.getChildren().addAll(incubatingLabel, incubatingCircle);
        sickHBox.getChildren().addAll(sickLabel, sickCircle);
        immuneHBox.getChildren().addAll(immuneLabel, immuneCircle);
        deadHBox.getChildren().addAll(deadLabel, deadCircle);

        // Hbox to hold all legends
        HBox hBox = new HBox(10);
        hBox.setMinHeight(20);
        hBox.setMaxHeight(20);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(vulnerableHBox, incubatingHBox, sickHBox, immuneHBox, deadHBox);


        border.setLeft(vBox);
        border.setCenter(canvas);
        border.setRight(stateBox);
        border.setBottom(hBox);
        BorderPane.setMargin(vBox, new Insets(20,10,20,20));
        BorderPane.setMargin(canvas, new Insets(20,0,0,0));
        BorderPane.setMargin(stateBox, new Insets(20,0,20,10));
        BorderPane.setMargin(hBox, new Insets(0,0,20,0));

        startButton.setOnMouseClicked(event -> {
            if (startButton.getText().equals("Start")){
                startButton.setText("Restart");
                MGR.resetTicks();
                begTimer = System.currentTimeMillis();

                PARAM.setRecoveryProb(recoveryBox.getValue());
                PARAM.setIncubationTime(incubationBox.getValue());
                PARAM.setInitialImmune(initImmuneBox.getValue());
                PARAM.setInfectionDistance(expDistanceBox.getValue());
                PARAM.setInitialSick(initSickBox.getValue());
                PARAM.setSicknessTime(sickTimeBox.getValue());

                MGR.makeAgents(FM, PARAM);

                if (initSickBox.getValue() > num){
                    initSickBox.setValue(num);
                }

                if (initImmuneBox.getValue() > num){
                    initImmuneBox.setValue(num);
                }

                for (Agent agent : MGR.getAgents()){
                    timeLapse.put(MGR.getAgents().indexOf(agent) + 1, agent.getHealthState());
                }

                AnimationTimer animation = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        int midX = (int) (canvas.getWidth() - width) / 2;
                        int midY = (int) (canvas.getHeight() - height) / 2;

                        gc.setFill(Color.WHITE);
                        gc.fillRect(midX, midY, width + 10, height);

                        State hState;
                        Point2D cPosition;
                        Color fill1 = null;


                        for (Agent ags : MGR.getAgents()) {
                            hState = ags.getHealthState();
                            cPosition = ags.getLocation();
                            if (hState == State.VULNERABLE) {
                                fill1 = Color.BLUE;
                            } else if (hState == State.INCUBATING) {
                                fill1 = Color.ORANGE;
                            } else if (hState == State.IMMUNE) {
                                fill1 = Color.GREEN;
                            } else if (hState == State.SICK) {
                                fill1 = Color.RED;
                            } else if (hState == State.DEAD) {
                                fill1 = Color.BLACK;
                            }

                            gc.setFill(fill1);
                            gc.fillOval(cPosition.getX() + midX, cPosition.getY() + midY, 8, 8);
                            String msgs = getMessage(timeLapse, hState, MGR, ags);
                            msgLogBox.appendText(msgs);
                            MGR.incTicks();
                        }
                        simulationPlot.updatePlot(MGR.getTicks(),num);
                    }
                };
                animation.start();
                MGR.start();
                plotChart.getChildren().add(simulationPlot);
            }
            else if (startButton.getText().equals("Restart")){
                msgLogBox.clear();
                plotChart.getChildren().clear();
                simulationPlot.resetPlot();
                MGR.resetTicks();
                begTimer = System.currentTimeMillis();

                PARAM.setRecoveryProb(recoveryBox.getValue());
                PARAM.setIncubationTime(incubationBox.getValue());
                PARAM.setInitialImmune(initImmuneBox.getValue());
                PARAM.setInfectionDistance(expDistanceBox.getValue());
                PARAM.setInitialSick(initSickBox.getValue());
                PARAM.setSicknessTime(sickTimeBox.getValue());

                MGR.makeAgents(FM, PARAM);

                if (initSickBox.getValue() > num){
                    initSickBox.setValue(num);
                }

                if (initImmuneBox.getValue() > num){
                    initImmuneBox.setValue(num);
                }

                for (Agent agent : MGR.getAgents()){
                    timeLapse.put(MGR.getAgents().indexOf(agent) + 1, agent.getHealthState());
                }

                AnimationTimer animation = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        int midX = (int) (canvas.getWidth() - width) / 2;
                        int midY = (int) (canvas.getHeight() - height) / 2;

                        gc.setFill(Color.WHITE);
                        gc.fillRect(midX, midY, width + 10, height);

                        State hState;
                        Point2D cPosition;
                        Color fill1 = null;


                        for (Agent ags : MGR.getAgents()) {
                            hState = ags.getHealthState();
                            cPosition = ags.getLocation();
                            if (hState == State.VULNERABLE) {
                                fill1 = Color.BLUE;
                            } else if (hState == State.INCUBATING) {
                                fill1 = Color.ORANGE;
                            } else if (hState == State.IMMUNE) {
                                fill1 = Color.GREEN;
                            } else if (hState == State.SICK) {
                                fill1 = Color.RED;
                            } else if (hState == State.DEAD) {
                                fill1 = Color.BLACK;
                            }

                            gc.setFill(fill1);
                            gc.setFill(fill1);
                            gc.fillOval(cPosition.getX() + midX, cPosition.getY() + midY, 8, 8);
                            String msgs = getMessage(timeLapse, hState, MGR, ags);
                            msgLogBox.appendText(msgs);
                            MGR.incTicks();
                        }
                        simulationPlot.updatePlot(MGR.getTicks(),num);
                    }
                };
                animation.start();
                MGR.start();
                plotChart.getChildren().add(simulationPlot);
            }
        });

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * This method returns the current system time by converting the current system
     * time into seconds.
     * @return currentTime in seconds
     */
    public long getCurrentTime() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - begTimer) / 1000;
    }

    /**
     * This function creates the message for the message log
     * @param m map of events
     * @param hs health state
     * @param am diseaseSimulation.Manager object
     * @param a current diseaseSimulation.Agent
     * @return String message
     */
    public String getMessage(HashMap<Integer, State> m,
                                State hs, Manager am, Agent a) {
        String msg = "";
        long currTime = getCurrentTime();
        int i = am.getAgents().indexOf(a);
        if(hs != State.DEAD) {
            if(m.containsKey(i)) {
                if (hs != m.get(i)) {
                    m.put(i, hs);
                    msg = "Agent " + i + " became " + hs + " at " +
                            currTime + "s\n";
                }
            }
        }
        else {
            if(m.containsKey(i) && hs != m.get(i)) {
                m.put(i, hs);
                msg = "Agent " + i + " died at " + currTime + "s\n";
            }
        }
        return msg;
    }
}
