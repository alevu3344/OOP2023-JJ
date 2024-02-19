package it.unibo.jetpackjoyride.core.movement;

import it.unibo.jetpackjoyride.utilities.Pair;

import java.util.*;

public interface Movement {

    enum MovementChangers {
        DIAGONALUP, // ySpeed is initially set the same as xSpeed
        DIAGONALDOWN, // ySpeed is initially set the same as -xSpeed
        BOUNCING, // Once the upper or lower bound of the screen is hit, the y speed is inverted
        GRAVITY, // y speed is accelerated downwards
        INVERSEGRAVITY, // y speed is accelerated upwards
        BOUNDS, // x and y will only vary between specified limits
    }

    Pair<Double, Double> getCurrentPosition();

    Pair<Double, Double> getSpeed();

    Pair<Double, Double> getAcceleration();

    Pair<Double, Double> getRotation();

    List<MovementChangers> getMovementChangers();

    void setCurrentPosition(Pair<Double, Double> currPos);

    void setAcceleration(Pair<Double, Double> newAcceleration);

    void setSpeed(Pair<Double, Double> newSpeed);

    void setRotation(Pair<Double, Double> newRotationAngle);

    void setMovementChangers(List<MovementChangers> listOfChangers);

    void update();
}
