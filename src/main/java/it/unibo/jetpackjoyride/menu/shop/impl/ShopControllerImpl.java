package it.unibo.jetpackjoyride.menu.shop.impl;

import java.io.IOException;

import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import it.unibo.jetpackjoyride.core.statistical.api.GameStatsController;
import it.unibo.jetpackjoyride.core.statistical.impl.GameStatsIO;
import it.unibo.jetpackjoyride.menu.menus.impl.GameMenuImpl;
import it.unibo.jetpackjoyride.menu.shop.api.BackToMenuObs;
import it.unibo.jetpackjoyride.menu.shop.api.CharacterObs;
import it.unibo.jetpackjoyride.menu.shop.api.ShopController;
import it.unibo.jetpackjoyride.menu.shop.api.ShopItemPurchaseObs;
import javafx.stage.Stage;


/**
 * Controller class for the shop menu.
 * This class manages the interaction between the shop model and view.
 */
public final class ShopControllerImpl implements ShopController {

    private final ShopView view;
 
    private final GameStatsController gameStatsHandler;

    private final GameMenuImpl gameMenu;

   
    private final Set<Items> unlockedItems;
    

    /**
     * Constructs a new ShopControllerImpl instance.
     *
     * @param primaryStage   The primary stage of the application.
     * @param gameMenu       The game menu associated with the shop.
     */
    public ShopControllerImpl(final Stage primaryStage, final GameMenuImpl gameMenu, 
                            final GameStatsController gameStatsController) {

        this.gameMenu = gameMenu;

        this.gameStatsHandler = gameStatsController;


        this.unlockedItems = new HashSet<>(this.gameStatsHandler.getGameStatsModel().getUnlocked());

        

        this.view = new ShopView(this, primaryStage, gameStatsHandler);


        ShopItemPurchaseObs shopItemPurchaseObs = new ShopItemPurchaseObsImpl(this);
        BackToMenuObs backToMenuObs = new BackToMenuObsImpl(this);
        CharacterObs charObs = new CharacterImpl(this);

        // Register observers with ShopView
        this.view.addBuyObs(shopItemPurchaseObs);
        this.view.addBackToMenuObs(backToMenuObs);
        this.view.addCharObs(charObs);
    }

    /**
     * Retrieves the scene of the shop menu.
     * 
     * @return The scene of the shop menu.
     */
    @Override
    public void showTheShop() {
        this.view.setSceneOnStage();
    }

    @Override
    public void buy(final Items item) {

        final var available = this.gameStatsHandler.getGameStatsModel().getTotCoins();

            if (!this.unlockedItems.contains(item)) {
                if (item.getItemCost() > available) {
                    System.out.println("Not enough funds :(\n");
                } else {
                    this.unlockedItems.add(item);
                    this.gameStatsHandler.getGameStatsModel().updateCoins(-item.getItemCost());
                }
            }
        this.view.update();

    }

    @Override
    public int retrieveBalance() {
        return this.gameStatsHandler.getGameStatsModel().getTotCoins();
    }

    @Override
    public void backToMenu() {
        this.save();
        gameMenu.showMenu();
    }

   

    @Override
    public Set<Items> getUnlocked() {

        return Collections.unmodifiableSet(this.unlockedItems);
    }

    @Override
    public void save() {

        this.gameStatsHandler.getGameStatsModel().unlock(this.unlockedItems);

        final String filename = "gameStats.data";

        try {

            GameStatsIO.writeToFile(gameStatsHandler.getGameStatsModel(), filename);
            System.out.println("Game stats saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save game stats: " + e.getMessage());
        }
    }

    @Override
    public void updateView() {
        this.view.update();
    }

    @Override
    public void unlock(Items item) {
        this.unlockedItems.add(item);
    }
}
