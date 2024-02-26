package it.unibo.jetpackjoyride.core.entities.entity.impl;

import java.util.ArrayList;
import java.util.List;

import it.unibo.jetpackjoyride.core.entities.barry.api.Barry;
import it.unibo.jetpackjoyride.core.entities.barry.impl.BarryImpl;
import it.unibo.jetpackjoyride.core.entities.entity.api.EntityModelGenerator;
import it.unibo.jetpackjoyride.core.entities.obstacle.api.Obstacle;
import it.unibo.jetpackjoyride.core.entities.obstacle.api.Obstacle.ObstacleType;
import it.unibo.jetpackjoyride.core.entities.obstacle.impl.Laser;
import it.unibo.jetpackjoyride.core.entities.obstacle.impl.Missile;
import it.unibo.jetpackjoyride.core.entities.obstacle.impl.Zapper;
import it.unibo.jetpackjoyride.core.entities.pickups.api.PickUp;
import it.unibo.jetpackjoyride.core.entities.pickups.api.PickUp.PickUpType;
import it.unibo.jetpackjoyride.core.entities.pickups.impl.ShieldPickUp;
import it.unibo.jetpackjoyride.core.entities.pickups.impl.VehiclePickUp;
import it.unibo.jetpackjoyride.core.entities.powerup.api.PowerUp;
import it.unibo.jetpackjoyride.core.entities.powerup.api.PowerUp.PowerUpType;
import it.unibo.jetpackjoyride.core.entities.powerup.impl.DukeFishron;
import it.unibo.jetpackjoyride.core.entities.powerup.impl.LilStomper;
import it.unibo.jetpackjoyride.core.entities.powerup.impl.MrCuddlesGenerator;
import it.unibo.jetpackjoyride.core.entities.powerup.impl.ProfitBird;
import it.unibo.jetpackjoyride.core.hitbox.api.Hitbox;
import it.unibo.jetpackjoyride.core.hitbox.impl.HitboxImpl;
import it.unibo.jetpackjoyride.core.movement.Movement;
import it.unibo.jetpackjoyride.utilities.MovementChangers;
import it.unibo.jetpackjoyride.utilities.Pair;
import it.unibo.jetpackjoyride.utilities.exceptions.NotImplementedObjectException;

/**
 * The {@link EntityModelGeneratorImpl} class is a factory of all entities.
 * This class provides methods for generating the entities obstacles, powerups
 * and pickups.
 * For powerups and pickups, only the type is needed, since their movement is
 * fixed.
 * Obstacles have to be generated by supplying a Movement class.
 * The entity is generated by combining the two classes HitboxImpl and Movement.
 * The Hitbox class is also fixed for every type of entity and doesn't need to
 * be provided.
 * 
 * @author gabriel.stira@studio.unibo.it
 */
public final class EntityModelGeneratorImpl implements EntityModelGenerator {
    /*
     * Define the dimensions of the hitbox of the obstacles.
     */
    private static final Pair<Double, Double> MISSILE_HITBOX_DIMENSIONS = new Pair<>(40.0, 15.0);
    private static final Pair<Double, Double> ZAPPER_HITBOX_DIMENSIONS = new Pair<>(160.0, 30.0);
    private static final Pair<Double, Double> LASER_HITBOX_DIMENSIONS = new Pair<>(980.0, 24.0);

    /*
     * Define the coordinates where the powerups will be generated.
     */
    private static final Pair<Double, Double> LILSTOMPER_SPAWNING_COORDINATES = new Pair<>(200.0, 360.0);
    private static final Pair<Double, Double> MRCUDDLE_SPAWNING_COORDINATES = new Pair<>(200.0, 0.0);
    private static final Pair<Double, Double> PROFITBIRD_SPAWNING_COORDINATES = new Pair<>(200.0, 360.0);
    private static final Pair<Double, Double> DUKEFISHRON_SPAWNING_COORDINATES = new Pair<>(200.0, 360.0);

    /*
     * Define the dimensions of the hitbox of the powerups.
     */
    private static final Pair<Double, Double> LILSTOMPER_HITBOX_DIMENSIONS = new Pair<>(160.0, 140.0);
    private static final Pair<Double, Double> MRCUDDLE_HITBOX_DIMENSIONS = new Pair<>(120.0, 70.0);
    private static final Pair<Double, Double> PROFITBIRD_HITBOX_DIMENSIONS = new Pair<>(120.0, 70.0);
    private static final Pair<Double, Double> DUKEFISHRON_HITBOX_DIMENSIONS = new Pair<>(150.0, 100.0);

    /**
     * Define the initial position of Barry.
     */
    private static final Pair<Double, Double> BARRY_STARTING_POS = new Pair<>(200.0, 630.0);

    /**
     * Define the dimensions of barry's hitbox.
     */
    private static final Pair<Double, Double> BARRY_HITBOX_DIMENSIONS = new Pair<>(75.0, 100.0);

    /*
     * Define more movement characteristics of some powerups.
     */
    private static final Pair<Double, Double> DUKEFISHRON_BASE_SPEED = new Pair<>(0.0, 10.0);
    private static final Double DUKE_ROTATION_ANGLE = 20.0;

    /*
     * Define the coordinates where the pickups will be generated.
     */
    private static final Pair<Double, Double> VEHICLE_PICKUP_SPAWNING_COORDINATES = new Pair<>(1350.0, 360.0);
    private static final Pair<Double, Double> SHIELD_PICKUP_SPAWNING_COORDINATES = new Pair<>(1350.0, 360.0);

    /*
     * Define the dimensions of the hitbox of the pickups.
     */
    private static final Pair<Double, Double> VEHICLE_PICKUP_HITBOX_DIMENSIONS = new Pair<>(80.0, 80.0);
    private static final Pair<Double, Double> SHIELD_PICKUP_HITBOX_DIMENSIONS = new Pair<>(50.0, 50.0);

    /*
     * Define more movement characteristics of some pickups.
     */
    private static final Pair<Double, Double> VEHICLE_PICKUP_BASE_SPEED = new Pair<>(-3.0, 0.0);
    private static final Pair<Double, Double> SHIELD_PICKUP_BASE_SPEED = new Pair<>(-5.0, 0.0);

    @Override
    public Obstacle generateObstacle(final ObstacleType obstacleType, final Movement obstacleMovement) {
        Hitbox obstacleHitbox;
        Obstacle obstacleModel;
        final Movement newMovement = new Movement.Builder()
                .setPosition(obstacleMovement.getPosition())
                .setSpeed(obstacleMovement.getSpeed())
                .setAcceleration(obstacleMovement.getAcceleration())
                .setRotation(obstacleMovement.getRotation())
                .setMovementChangers(obstacleMovement.getMovementChangers())
                .build();
        try {
            switch (obstacleType) {
                case MISSILE: // Canon obstacle existing in the original game
                    obstacleHitbox = new HitboxImpl(newMovement.getPosition(), MISSILE_HITBOX_DIMENSIONS,
                            newMovement.getRotation().get1());
                    obstacleModel = new Missile(newMovement, obstacleHitbox);
                    break;
                case ZAPPER: // Canon obstacle existing in the original game
                    obstacleHitbox = new HitboxImpl(newMovement.getPosition(), ZAPPER_HITBOX_DIMENSIONS,
                            newMovement.getRotation().get1());
                    obstacleModel = new Zapper(newMovement, obstacleHitbox);
                    break;
                case LASER: // Canon obstacle existing in the original game
                    obstacleHitbox = new HitboxImpl(newMovement.getPosition(), LASER_HITBOX_DIMENSIONS,
                            newMovement.getRotation().get1());
                    obstacleModel = new Laser(newMovement, obstacleHitbox);
                    break;
                default:
                    throw new NotImplementedObjectException(
                            "EntityModelGenerator could not generate the obstacle. A missile will be generated instead.");
            }
        } catch (NotImplementedObjectException e) {
            obstacleHitbox = new HitboxImpl(newMovement.getPosition(), MISSILE_HITBOX_DIMENSIONS, 0.0);
            obstacleModel = new Missile(newMovement, obstacleHitbox);
        }

        return obstacleModel;
    }

    @Override
    public List<PowerUp> generatePowerUp(final PowerUpType powerUpType) {
        Movement powerUpMovement;
        Hitbox powerUpHitbox;
        final List<PowerUp> powerUpModels = new ArrayList<>();
        try {
            switch (powerUpType) {
                case LILSTOMPER: // Canon powerup existing in the original game
                    powerUpMovement = new Movement.Builder().setPosition(LILSTOMPER_SPAWNING_COORDINATES)
                            .setMovementChangers(List.of(MovementChangers.GRAVITY, MovementChangers.BOUNDS)).build();
                    powerUpHitbox = new HitboxImpl(powerUpMovement.getPosition(), LILSTOMPER_HITBOX_DIMENSIONS,
                            powerUpMovement.getRotation().get1());
                    powerUpModels.add(new LilStomper(powerUpMovement, powerUpHitbox));
                    break;
                case MRCUDDLES: // Canon powerup existing in the original game
                    powerUpMovement = new Movement.Builder().setPosition(MRCUDDLE_SPAWNING_COORDINATES)
                            .setMovementChangers(List.of(MovementChangers.INVERSEGRAVITY, MovementChangers.BOUNDS))
                            .build();
                    powerUpHitbox = new HitboxImpl(powerUpMovement.getPosition(), MRCUDDLE_HITBOX_DIMENSIONS,
                            powerUpMovement.getRotation().get1());
                    powerUpModels.addAll(new MrCuddlesGenerator(powerUpMovement, powerUpHitbox).generateMrCuddle());
                    break;
                case PROFITBIRD: // Canon powerup existing in the original game
                    powerUpMovement = new Movement.Builder().setPosition(PROFITBIRD_SPAWNING_COORDINATES)
                            .setMovementChangers(List.of(MovementChangers.GRAVITY, MovementChangers.BOUNDS)).build();
                    powerUpHitbox = new HitboxImpl(powerUpMovement.getPosition(), PROFITBIRD_HITBOX_DIMENSIONS,
                            powerUpMovement.getRotation().get1());
                    powerUpModels.add(new ProfitBird(powerUpMovement, powerUpHitbox));
                    break;
                case DUKEFISHRON: // Non canon powerup. An easter egg for Terraria players ;)
                    powerUpMovement = new Movement.Builder().setPosition(DUKEFISHRON_SPAWNING_COORDINATES)
                            .setSpeed(DUKEFISHRON_BASE_SPEED).setRotation(DUKE_ROTATION_ANGLE, 0.0)
                            .setMovementChangers(List.of(MovementChangers.BOUNCING)).build();
                    powerUpHitbox = new HitboxImpl(powerUpMovement.getPosition(), DUKEFISHRON_HITBOX_DIMENSIONS,
                            powerUpMovement.getRotation().get1());
                    powerUpModels.add(new DukeFishron(powerUpMovement, powerUpHitbox));
                    break;
                default:
                    throw new NotImplementedObjectException(
                            "EntityModelGenerator could not generate the obstacle." 
                          + "A lilStomper powerup will be spawned instead.");
            }
        } catch (NotImplementedObjectException e) {
            powerUpMovement = new Movement.Builder().setPosition(LILSTOMPER_SPAWNING_COORDINATES)
                    .setMovementChangers(List.of(MovementChangers.GRAVITY, MovementChangers.BOUNDS)).build();
            powerUpHitbox = new HitboxImpl(powerUpMovement.getPosition(), LILSTOMPER_HITBOX_DIMENSIONS, 0.0);
            powerUpModels.add(new LilStomper(powerUpMovement, powerUpHitbox));
        }

        return powerUpModels;
    }

    @Override
    public PickUp generatePickUp(final PickUpType pickUpType) {
        Movement pickUpMovement;
        Hitbox pickUpHitbox;
        PickUp pickUpModel;

        try {
            switch (pickUpType) {
                case VEHICLE: // Canon pickup existing in the original game
                    pickUpMovement = new Movement.Builder().setPosition(VEHICLE_PICKUP_SPAWNING_COORDINATES)
                            .setSpeed(VEHICLE_PICKUP_BASE_SPEED).setMovementChangers(List.of(MovementChangers.GRAVITY))
                            .build();
                    pickUpHitbox = new HitboxImpl(pickUpMovement.getPosition(), VEHICLE_PICKUP_HITBOX_DIMENSIONS,
                            pickUpMovement.getRotation().get1());
                    pickUpModel = new VehiclePickUp(pickUpMovement, pickUpHitbox);
                    break;
                case SHIELD: // Canon pickup existing in the original game
                    pickUpMovement = new Movement.Builder().setPosition(SHIELD_PICKUP_SPAWNING_COORDINATES)
                            .setSpeed(SHIELD_PICKUP_BASE_SPEED).build();
                    pickUpHitbox = new HitboxImpl(pickUpMovement.getPosition(), SHIELD_PICKUP_HITBOX_DIMENSIONS,
                            pickUpMovement.getRotation().get1());
                    pickUpModel = new ShieldPickUp(pickUpMovement, pickUpHitbox);
                    break;
                default:
                    throw new NotImplementedObjectException(
                            "EntityModelGenerator could not generate the pickup. A vehicle pickups will be generated instead.");
            }
        } catch (NotImplementedObjectException e) {
            pickUpMovement = new Movement.Builder().setPosition(VEHICLE_PICKUP_SPAWNING_COORDINATES)
                    .setSpeed(VEHICLE_PICKUP_BASE_SPEED).setMovementChangers(List.of(MovementChangers.GRAVITY)).build();
            pickUpHitbox = new HitboxImpl(pickUpMovement.getPosition(), VEHICLE_PICKUP_HITBOX_DIMENSIONS, 0.0);
            pickUpModel = new VehiclePickUp(pickUpMovement, pickUpHitbox);
        }

        return pickUpModel;
    }

    @Override
    public Barry generateBarry() {
        final Movement barryMovement = new Movement.Builder().setPosition(BARRY_STARTING_POS)
                .setMovementChangers(List.of(MovementChangers.GRAVITY, MovementChangers.BOUNDS)).build();
        final Hitbox barryHitbox = new HitboxImpl(barryMovement.getPosition(),
                BARRY_HITBOX_DIMENSIONS,
                0.0);
        return new BarryImpl(barryMovement, barryHitbox);
    }
}
