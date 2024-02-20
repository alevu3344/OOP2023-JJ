package it.unibo.jetpackjoyride.menu.menus;

import it.unibo.jetpackjoyride.core.GameLoop;
import it.unibo.jetpackjoyride.menu.buttoncommand.ButtonFactory;
import it.unibo.jetpackjoyride.menu.buttoncommand.api.Command;
import it.unibo.jetpackjoyride.menu.buttoncommand.impl.StartCommand;
import it.unibo.jetpackjoyride.menu.buttoncommand.impl.PauseCommand;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Class representing the  Pause menu, extend from the GameMenu.
 * @author yukai.zhou@studio.unibo.it
 */
public final class PauseMenu extends GameMenu {
    private static final int PAUSE_BUTTON_SIZE = 50;
    private static final int RESTART_WIDTH = 220;
    private static final int RESTART_HEIGHT = 120;
    private static final int SPACE1 = 20;
    private static final int SPACE = 70;

    private final VBox buttonsVBox;
    private final GameLoop gameLoop;
    private Button pauseButton;


    /**
     * Constructs a new PauseMenu.
     * And it call back the constructor of the superclass
     * @param primaryStage the primary stage
     * @param gameLoop     the game loop
     */
    public PauseMenu(final Stage primaryStage, final GameLoop gameLoop) {
        super(primaryStage, null);
        this.gameLoop = gameLoop;
        buttonsVBox = new VBox();
        initializeGameMenu();
    }

    @Override
    protected void initializeGameMenu() {
        buttonsVBox.setPrefWidth(gameInfo.getScreenWidth());
        buttonsVBox.setPrefHeight(gameInfo.getScreenHeight());
        buttonsVBox.setAlignment(Pos.CENTER);
        buttonsVBox.setSpacing(SPACE1);
        buttonsVBox.setStyle("-fx-background-color: rgb(0, 0, 0);");
        Command continueCommand = new StartCommand(gameLoop, this);
        Button restartButton = ButtonFactory.createButton("back", 
        e -> { 
            continueCommand.execute(); 
            this.buttonsVBox.setVisible(false); 
        }, RESTART_WIDTH, RESTART_HEIGHT);

        Command pauseCommand = new PauseCommand(gameLoop, this);
        pauseButton = ButtonFactory.createButton("Pause", e -> pauseCommand.execute(), PAUSE_BUTTON_SIZE, PAUSE_BUTTON_SIZE);
        pauseButton.setLayoutX(gameInfo.getScreenWidth() - SPACE);
        pauseButton.setLayoutY(0);

        buttonsVBox.getChildren().addAll(restartButton);
        buttonsVBox.setVisible(false);
    }

   /**
     * Sets the pause button on Game Screen.
     * @param root The main root of the GameLoop class
     */
    public void setPauseButton(final Pane root) {
          root.getChildren().add(this.pauseButton);
    }

    /**
    * Adjusts the horizontal position of the pause button based on a new value.
    * This is typically used to reposition the pause button in response to changes in the game window size.
    * @param newValue The new x-coordinate value for the pause button's layout.
    */
    public void setPauseButtonSize(final double newValue) {
        this.pauseButton.setLayoutX(newValue - pauseButton.getWidth());
    }

    /**
     * Sets the ButtonVBox that containg back button on Game Screen.
     * @param root The main root of the GameLoop class
     */
    public void setButtonVBox(final Pane root) {
         root.getChildren().add(buttonsVBox);
    }

    /**
    * Sets the preferred width of the VBox containing the menu buttons.
    * This can be used to adjust the width of the buttons container in response to changes in the game window size.
    * @param newValue The new preferred width for the buttons VBox.
    */
    public void setButtonVBoxSizeX(final double newValue) {
        this.buttonsVBox.setPrefWidth(newValue);
    }

    /**
    * Sets the preferred height of the VBox containing the menu buttons.
    * This can be used to adjust the height of the buttons container in response to changes in the game window size.
    * @param newValue The new preferred height for the buttons VBox.
    */
    public void setButtonVBoxSizeY(final double newValue) {
        this.buttonsVBox.setPrefHeight(newValue);
    }

    /**
     * Sets the visibility of the menu.
     *
     * @param isVisible whether the menu is visible
     */
    public void setVisible(final boolean isVisible) {
        this.buttonsVBox.setVisible(isVisible);
    }
}
