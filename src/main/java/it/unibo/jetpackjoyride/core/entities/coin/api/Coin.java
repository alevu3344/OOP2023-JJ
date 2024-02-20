package it.unibo.jetpackjoyride.core.entities.coin.api;

import java.util.List;

import it.unibo.jetpackjoyride.core.hitbox.api.Hitbox;
import it.unibo.jetpackjoyride.utilities.Pair;

/**
 * Interface of the coin controller.
 * @author yukai.zhou@studio.unibo.it
 */
public interface Coin {

/**
 * Updates the model of the coin.
 */
    void updateModel();

/**
 * Renders the coin.
 */
    void render();

/**
 * Get the model of the coin.
 *
 * @return the model of the coin
 */
    List<Pair<Double, Double>> getModelData();

/**
 * Set the position of the coin.
 *
 * @param position the position to set for the coin
 */
    void setPosition(Pair<Double, Double> position);

/**
 * Set the collected state of the coin.
 *
 * @param isCollected true if the coin is collected, false otherwise
 */
    void setCollectedState(boolean isCollected);

     /**
     * Get hitbox of the coin model.
     * 
     * @return The hitbox of the coin model.
     */
    Hitbox geHitbox();

    /**
     * Check if the coin is collected.
     * 
     * @return True if the coin is collected, false otherwise.
     */
    boolean isCollected();
}
