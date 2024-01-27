package it.unibo.jetpackjoyride.core.movement;

import java.util.ArrayList;
import java.util.List;

import it.unibo.jetpackjoyride.core.movement.MovementGenerator.MovementChangers;
import it.unibo.jetpackjoyride.utilities.Pair;

public abstract class AbstractMovement implements Movement {

    public Pair<Double, Double> startingPosition;
    public Pair<Double, Double> currentPosition;
    public Pair<Double,Double> xyacceleration;
    public Pair<Double,Double> xyspeed;

    private List<MovementChangers> listOfChangers = new ArrayList<>();

    public final static Pair<Double,Double> playerPos = new Pair<>(0.0, 10.0);
    public final static Double TIME = 0.1;

    public AbstractMovement(Pair<Double, Double> startingPosition, Pair<Double, Double> startingSpeed, Pair<Double, Double> startingAcceleration, List<MovementChangers> listOfChangers) {
        this.startingPosition = startingPosition;
        this.currentPosition = startingPosition;
        this.xyspeed = startingSpeed;
        this.xyacceleration = startingAcceleration;
        this.listOfChangers = listOfChangers;
    }

    public Pair<Double, Double> getStartingPosition() {
        return this.startingPosition;
    }

    public Pair<Double, Double> getCurrentPosition() {
        return this.currentPosition;
    }

    @Override
    public void setStartingPosition(Pair<Double, Double> startPos) {
        this.startingPosition = startPos;
    }

    @Override
    public void setCurrentPosition(Pair<Double, Double> currPos) {
        this.currentPosition = currPos;
    }

    public void setAcceleration(Pair<Double, Double> newAcceleration) {
        this.xyacceleration = newAcceleration;
    }

    public Pair<Double, Double> getAcceleration() {
        return this.xyacceleration;
    }

    public void setSpeed(Pair<Double, Double> newSpeed) {
        this.xyspeed = newSpeed;
    }

    public Pair<Double, Double> getSpeed() {
        return this.xyspeed;
    }

    public List<MovementChangers> getMovementChangers() {
        return this.listOfChangers;
    }

    public void setMovementChangers( List<MovementChangers> listOfChangers) {
        this.listOfChangers = listOfChangers;
    }

    public abstract void applyModifiers();

    public abstract void update();
}