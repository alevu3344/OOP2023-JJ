package it.unibo.jetpackjoyride.core.entities.coin.impl;

import it.unibo.jetpackjoyride.core.entities.coin.api.CoinModel;
import it.unibo.jetpackjoyride.core.hitbox.api.Hitbox;
import it.unibo.jetpackjoyride.core.hitbox.impl.HitboxImpl;
import it.unibo.jetpackjoyride.utilities.GameInfo;
import it.unibo.jetpackjoyride.utilities.Pair;

/**
 * Implementation of the CoinModel interface.
 * @author yukai.zhou@studio.unibo.it
 */
public class CoinModelImpl implements CoinModel {
    private static final double COIN_WIDTH = 30;
    private static final double COIN_HEIGHT = 30;

    private Pair<Double,Double> position;
    private HitboxImpl coinHitbox;
    private boolean isCollected;
    
    /**
     * Constructs a CoinModelImpl with the given position and hitbox.
     *
     * @param position the initial position of the coin
     * @param hitbox   the hitbox of the coin
     */
    public CoinModelImpl(Pair<Double,Double> position, HitboxImpl hitbox){
            this.position = position;
            this.coinHitbox = hitbox;
            this.isCollected = false;
    }

    @Override
    public void updateCoinModel(){
        this.position = new Pair<Double,Double>(position.get1()-GameInfo.moveSpeed.get(), position.get2());
        coinHitbox.updateHitbox(position, 0.0);
    }

    @Override
    public boolean isCollected(){
        return this.isCollected;
    }

    @Override
    public void setCollectedState(boolean isCollected){
        this.isCollected = isCollected;
    }
  
    @Override
    public Pair<Double, Double> getPosition() {
        return position;
    }

    @Override
    public void setPosition(Pair<Double,Double> position){
        this.position = position;
        this.coinHitbox.updateHitbox(position, 0.0);
    }
    
    @Override
    public Hitbox geHitbox(){
        return this.coinHitbox;
    }

    @Override
    public Pair<Double, Double> getSize() {
        return new Pair<Double,Double>(COIN_WIDTH,COIN_HEIGHT);
    }

}
