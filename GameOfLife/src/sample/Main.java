package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;
import java.util.Arrays;
import java.util.Map;

public class Main extends Application {
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static double WIDTH = screenSize.getWidth();
    public static double HEIGHT = screenSize.getHeight();

    public static Stage mainStage;
    public static GridPane gridPane;
    public static FlowPane flowPane;
    public static Scene scn1;
    public static Life life;
    public static Button[][] btnCell;
    public static boolean stop = true;
    public static double myHeight = HEIGHT/5;
    public static double myWidth = WIDTH/5;
    public static AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            try {
                refresh();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void start(Stage primaryStage) throws Exception{
        initStage(primaryStage);
        initAll();
        //
        finalInit();
    }
    public static void initStage(Stage in){
        mainStage=in;
    }
    public static void initLife(){
        life = new Life(40,40);
    }
    public static void initGrid(){
        gridPane = new GridPane();
        gridPane.setMaxSize(myHeight/10,myHeight/10);
        gridPane.setPadding(new Insets(10,10,10,10));
        gridPane.setHgap(5);gridPane.setVgap(5);
        gridPane.setAlignment(Pos.CENTER);
    }
    public static void initCell(){
        btnCell = new Button[life.getH()][life.getW()];
        for (int i = 0; i < btnCell.length; i++){
            for (int j = 0; j < btnCell[i].length; j++){
                btnCell[i][j] = new Button();
                btnCell[i][j].setMaxSize(10,10);
                btnCell[i][j].setMinSize(10,10);
                btnCell[i][j].setStyle("-fx-background-color: #ffffff");
                int finalI = i;
                int finalJ = j;
                btnCell[i][j].setOnMouseClicked(e->{
                    life.revertCell(new int[]{finalI, finalJ});
                    refreshBoard();
                    System.out.println(finalI + " " + finalJ);
                });
                gridPane.add(btnCell[i][j],j,i);
            }
        }
    }
    public static void refresh() throws InterruptedException {
        Thread.sleep(200);
        step();
    }
    public static void refreshBoard(){
        boolean[][] lifeCell = life.getCell();
        for (int i = 0; i < lifeCell.length; i++){
            for (int j = 0; j < lifeCell[i].length; j++){
                if (lifeCell[i][j])btnCell[i][j].setStyle("-fx-background-color: #000000");
                else btnCell[i][j].setStyle("-fx-background-color: #ffffff");
            }
        }
    }
    public static void step(){
        life.refresh();
        refreshBoard();
    }
    public static void initAll(){
        initLife();
        initGrid();
        initMenu();
        initCell();
        initScene();
        refreshBoard();
    }
    public static void initMenu(){
        TextField text = new TextField("Survive/Repro input");
        text.setOnMouseClicked(e->text.clear());
        Button reset = new Button("Reset");
        Button stopBtn = new Button("Start");
        reset.setOnMouseClicked(e->{
            initLife();
            timer.stop();stop=true;stopBtn.setText("Start");
            text.setOnMouseExited(k->text.setText("Survive/Repro input"));
            refreshBoard();
        });
        Text rule = new Text();
        stopBtn.setOnMouseClicked(e->{
            if (stop){timer.start();stop=false;stopBtn.setText("Stop");}
            else {timer.stop();stop=true;stopBtn.setText("Resume");}
        });
        Button step = new Button("Step");
        step.setOnMouseClicked(e->{
            step();
        });
        Button survive = new Button("Set survive");
        survive.setOnMouseClicked(e->{
            String in = text.getText();
            int length = in.length()>8?8:in.length();
            int[] out = new int[length];
            for (int i = 0; i < out.length && i < 8; i++){
                out[i] = Integer.valueOf(in.charAt(i))-'0';
            }
            System.out.println(Arrays.toString(out));
            life.setSurvive(out);
            rule.setText(life.getConditionToText());
        });
        Button repro = new Button("Set reproduction");
        repro.setOnMouseClicked(e->{
            String in = text.getText();
            int length = in.length()>8?8:in.length();
            int[] out = new int[length];
            for (int i = 0; i < out.length && i < 8; i++){
                out[i] = Integer.valueOf(in.charAt(i))-'0';
            }
            //System.out.println(Arrays.toString(out));
            life.setRepro(out);
            rule.setText(life.getConditionToText());
        });
        rule.setText(life.getConditionToText());
        Text handbook = new Text();
        handbook.setText(" Survive/Repro input = any number from 1-8 without space, e.g. 234. Does not handle error input.");
        flowPane = new FlowPane();
        flowPane.setPadding(new Insets(10,10,10,10));
        flowPane.getChildren().addAll(reset,stopBtn,step,text,survive,repro,rule,handbook);
    }
    public static void initScene(){
        BorderPane bdp = new BorderPane();
        bdp.setCenter(gridPane);
        bdp.setTop(flowPane);
        scn1 = new Scene(bdp,myWidth,myHeight);
    }
    public static void finalInit(){
        mainStage.setTitle("Hello World");
        mainStage.setScene(scn1);
        mainStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
