package it.unibo.jetpackjoyride.core.statistical.impl;

import java.io.IOException;

import it.unibo.jetpackjoyride.core.statistical.api.GameStatsController;
import it.unibo.jetpackjoyride.core.statistical.api.GameStatsModel;
import it.unibo.jetpackjoyride.core.statistical.api.GameStatsView;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * A class implementing the GameStatsController interface.
 * @author yukai.zhou@studio.unibo.it
 */
public final class GameStatsHandler implements GameStatsController {

    private GameStatsModel model;
    private GameStatsView view;

    /**
     * Constructs a new GameStatsHandler.
     */
    public GameStatsHandler() {
        System.out.println("GAME STATS CREATED");
        loadDateFromFile();
        this.view =  new GameStatsViewImpl();
    }

    @Override
    public void updateModel() {
        model.addDistance();
    }
    @Override
    public void updateView() {
        view.updateDataView(model);
    }
    @Override
    public Text getText() {
        return view.getText();
    } 
    @Override
    public ImageView getImageView() {
        return view.getImageView();
    }
    @Override
    public GameStatsModel getGameStatsModel() {
        return this.model;
    }

     /**
     * A method use to load the last statistical.
     */
    private void loadDateFromFile() {
        try {
            this.model = GameStatsIO.readFromFile("gameStats.data"); 
            System.out.println("Game stats loaded successfully.");
            System.out.println(this.model.getTotCoins());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load game stats: " + e.getMessage());
            this.model = new GameStats();
        }
    }
}
