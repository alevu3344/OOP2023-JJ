package it.unibo.jetpackjoyride.menu;


import it.unibo.jetpackjoyride.utilities.GameInfo;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.Dimension;
import java.awt.Toolkit;
public abstract class BaseMenu {
    private static final int PORTION = 4;

    private ChangeListener<Number> widthListener;
    private ChangeListener<Number> heightListener;
    
    protected Scene scene;
    protected Stage stage;
    protected StackPane root;
    protected ImageView menuImageView;     
    protected GameInfo gameInfo = GameInfo.getInstance();

    public BaseMenu(final Stage primaryStage) {
         this.stage = primaryStage;
         this.root = new StackPane();
         this.scene = new Scene(root, gameInfo.getScreenWidth(), gameInfo.getScreenHeight());
         setGameStagePosition();
         stageCloseAction();
    }

    public Scene getScene() {
        return this.scene;
    }

    public void removeListener(){
        if(scene != null && widthListener != null){
            scene.widthProperty().removeListener(widthListener);
        }
        if(scene != null && heightListener != null){
            scene.heightProperty().removeListener(heightListener);
        }
    }

    protected void initializeGameMenu() {
    }

    protected void addButtons(Node buttons) {
        this.root.getChildren().add(buttons);
    }

    protected void setMenuImage(Image menuImage){
        if(menuImageView != null){
            this.root.getChildren().remove(menuImageView);
        }
        menuImageView = new ImageView(menuImage);
        menuImageView.setFitWidth(gameInfo.getScreenWidth());
        menuImageView.setFitHeight(gameInfo.getScreenHeight());
        addSizeListener();
        root.getChildren().add(0, menuImageView);
    }

    protected void extendCloseAction(){

    }

    private void stageCloseAction(){
        stage.setOnCloseRequest(event -> {
            extendCloseAction();
            Platform.exit(); 
            System.exit(0);
        });
    }

    private void setGameStagePosition() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final double sw = screenSize.getWidth();
        final double sh = screenSize.getHeight();
        final double mapw = gameInfo.getScreenWidth();
        final double maph = gameInfo.getScreenHeight();

        final double stageX = (sw - mapw) / (PORTION / 2);
        final double stageY = (sh - maph) / PORTION;

        this.stage.setX(stageX);
        this.stage.setY(stageY);
    }

    private void addSizeListener(){
        widthListener = (obs,oldvalue,newVal) ->{
            double ratioX = newVal.doubleValue()/oldvalue.doubleValue();
            menuImageView.setFitWidth(menuImageView.getFitWidth()*ratioX);
        };
        scene.widthProperty().addListener(widthListener);
        heightListener = (obs,oldvalue,newVal) ->{
            double ratioY = newVal.doubleValue()/oldvalue.doubleValue();
            menuImageView.setFitHeight(menuImageView.getFitHeight()*ratioY);
        };
        scene.heightProperty().addListener(heightListener);
    }
}
