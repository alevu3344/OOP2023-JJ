package it.unibo.jetpackjoyride.core.handler.entity;

import it.unibo.jetpackjoyride.core.entities.coin.impl.CoinGenerator;
import it.unibo.jetpackjoyride.core.entities.entity.api.Entity.EntityStatus;
import it.unibo.jetpackjoyride.core.entities.pickups.api.PickUp;
import it.unibo.jetpackjoyride.core.entities.pickups.api.PickUp.PickUpType;
import it.unibo.jetpackjoyride.core.entities.pickups.impl.VehiclePickUp;
import it.unibo.jetpackjoyride.core.entities.powerup.api.PowerUp.PowerUpType;
import it.unibo.jetpackjoyride.core.handler.obstacle.ObstacleHandler;
import it.unibo.jetpackjoyride.core.handler.pickup.PickUpHandler;
import it.unibo.jetpackjoyride.core.handler.player.BarryHandler;
import it.unibo.jetpackjoyride.core.handler.powerup.PowerUpHandler;
import it.unibo.jetpackjoyride.core.statistical.api.GameStatsController;
import it.unibo.jetpackjoyride.menu.shop.api.ShopController.Items;

import java.util.*;
import java.util.stream.Collectors;

import javafx.scene.Group;

public class EntityHandler {
    private final static Integer BASEPICKUPSPAWNCHANCE = 100;
    private ObstacleHandler obstacleHandler;
    private PowerUpHandler powerUpHandler;
    private PickUpHandler pickUpHandler;
    private BarryHandler playerHandler;
    private CoinGenerator coinHandler;

    private Set<Items> unlockedPowerUps;

    private boolean isUsingPowerUp;
    private boolean isCanvasAdded;

    public void initialize(final GameStatsController gameStatsHandler) {
        this.obstacleHandler = new ObstacleHandler();
        this.powerUpHandler = new PowerUpHandler();
        this.pickUpHandler = new PickUpHandler();
        this.playerHandler = new BarryHandler();
        this.coinHandler = new CoinGenerator(Optional.of(playerHandler.getModel().getHitbox()), gameStatsHandler.getGameStatsModel());

        this.unlockedPowerUps = gameStatsHandler.getGameStatsModel().getUnlocked();

        this.obstacleHandler.initialize();
        this.isUsingPowerUp = false;
    }

    public boolean update(final Group entityGroup, final boolean isSpaceBarPressed) {

        playerHandler.update(isSpaceBarPressed);
        if(!playerHandler.getModel().isAlive()){
            coinHandler.setPlayerHitbox(Optional.empty());
            return false;
        }
    

        playerHandler.updateView(entityGroup);

        coinHandler.updatPosition();
        coinHandler.renderCoin();

        if (!isCanvasAdded) {
            entityGroup.getChildren().add(coinHandler.getCanvas());
            isCanvasAdded = true;
        }

        if (!this.isUsingPowerUp && this.pickUpHandler.getAllPickUps().isEmpty()) {
            this.spawnVehiclePickUp(this.unlockedPowerUps);
        }

        final var obstacleHit = this.obstacleHandler.update(entityGroup,
                isUsingPowerUp ? Optional.of(this.powerUpHandler.getAllPowerUps().get(0).getEntityModel().getHitbox())
                        : Optional.of(playerHandler.getModel().getHitbox()));
        if (obstacleHit.isPresent()) {
            if (this.isUsingPowerUp) {
                this.powerUpHandler.destroyAllPowerUps();
                this.isUsingPowerUp = false;
                this.playerHandler.getModel().setEntityStatus(EntityStatus.ACTIVE);
                this.coinHandler.setPlayerHitbox(Optional.of(this.playerHandler.getModel().getHitbox()));

            } else {
                this.playerHandler.hit(obstacleHit.get());
            }
        }

        this.powerUpHandler.update(entityGroup, isSpaceBarPressed);

        if (this.pickUpHandler.update(entityGroup, Optional.of(playerHandler.getModel().getHitbox()))) {
            playerHandler.getModel().setEntityStatus(EntityStatus.INACTIVE);
            final PickUp pickUpPickedUp = this.pickUpHandler.getAllPickUps().get(0).getEntityModel();

            switch (pickUpPickedUp.getPickUpType()) {
                case VEHICLE:
                    final VehiclePickUp vehiclePickUp = (VehiclePickUp) pickUpPickedUp;
                    this.spawnPowerUp(vehiclePickUp.getVehicleSpawn());
                    this.isUsingPowerUp = true;
                    this.obstacleHandler.deactivateAllObstacles();
                    this.coinHandler.setPlayerHitbox(
                            Optional.of(this.powerUpHandler.getAllPowerUps().get(0).getEntityModel().getHitbox()));
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private void spawnVehiclePickUp(final Set<Items> unlockedPowerUps) {
        Random rand = new Random();
        if (rand.nextInt(BASEPICKUPSPAWNCHANCE) != 0 || unlockedPowerUps.isEmpty()
                || !unlockedPowerUps.stream().filter(i -> i.getCorresponding().isPresent()).findAny().isPresent()) {
            return;
        }
        final List<PowerUpType> listOfPossibleSpawns = unlockedPowerUps.stream()
                .filter(i -> i.getCorresponding().isPresent()).map(p -> p.getCorresponding().get())
                .collect(Collectors.toList());
        this.pickUpHandler.spawnPickUp(PickUpType.VEHICLE);
        final VehiclePickUp vehiclePickUp = (VehiclePickUp) this.pickUpHandler.getAllPickUps().get(0).getEntityModel();
        vehiclePickUp.setVehicleSpawn(listOfPossibleSpawns.get(rand.nextInt(listOfPossibleSpawns.size())));
    }

    private void spawnPowerUp(final PowerUpType powerUpType) {
        this.powerUpHandler.spawnPowerUp(powerUpType);
    }

    public void stop() {
        this.obstacleHandler.over();
        this.coinHandler.stopGenerate();
    }

    public void start() {
        this.obstacleHandler.start();
        this.coinHandler.startGenerate();
    }

    public void reset() {
        this.obstacleHandler.deactivateAllObstacles();
        this.coinHandler.clean();
    }

}
