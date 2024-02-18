package it.unibo.jetpackjoyride.core.handler.obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import it.unibo.jetpackjoyride.core.entities.entity.api.EntityGenerator;
import it.unibo.jetpackjoyride.core.entities.entity.impl.EntityGeneratorImpl;
import it.unibo.jetpackjoyride.core.entities.obstacle.api.Obstacle;
import it.unibo.jetpackjoyride.core.entities.obstacle.api.Obstacle.ObstacleType;
import it.unibo.jetpackjoyride.core.entities.powerup.api.PowerUp.PowerUpType;
import it.unibo.jetpackjoyride.core.handler.generic.GenericController;
import it.unibo.jetpackjoyride.core.hitbox.api.Hitbox;
import it.unibo.jetpackjoyride.core.hitbox.impl.HitboxImpl;
import it.unibo.jetpackjoyride.core.movement.Movement;
import it.unibo.jetpackjoyride.core.movement.Movement.MovementChangers;
import it.unibo.jetpackjoyride.core.movement.MovementImpl;
import it.unibo.jetpackjoyride.utilities.GameInfo;
import it.unibo.jetpackjoyride.utilities.Pair;
import javafx.scene.image.Image;


public final class ObstacleSpawner {
    private final EntityGenerator entityGenerator;
    private Image[] images;
    private Integer gracePeriod;

    enum Difficulty{
        VERYEASY(1), EASY(2), NOTSOEASY(3), NORMAL(4), NOTSOHARD(5), 
        HARD(6), VERYHARD(7), SWEATING(8), IMPOSSIBLE(9), BARRY(10);

        private final Integer difficulty;

        Difficulty(Integer difficulty){
            this.difficulty = difficulty;
        }

        public static Optional<Difficulty> getDifficulty(Integer difficulty) {
            for(var d : Difficulty.values()){
                if(d.getDifficultyAsInteger().equals(difficulty)) {
                    return Optional.of(d);
                }
            }
            return Optional.empty();
        }

        public Integer getDifficultyAsInteger(){
            return this.difficulty;
        }
    }

    public ObstacleSpawner() {
        this.entityGenerator = new EntityGeneratorImpl();
        this.gracePeriod = 0;
    }



    public List<GenericController<Obstacle,ObstacleView>> generateChunk() {
        if(this.gracePeriod > 0) {
            this.gracePeriod--;
            return List.of();
        }

        //Integer difficulty = GameInfo.moveSpeed.get();
        Integer difficulty = 1;
        Optional<Difficulty> difficultyLevel = Difficulty.getDifficulty(difficulty);
        if(difficultyLevel.isEmpty()) {
            return List.of();
        }

        switch (difficultyLevel.get()) {
            case VERYEASY:
                this.gracePeriod = 5;
                return this.missileChunk();
            case EASY:

                break;
            case NOTSOEASY:

                break;
            case NORMAL:

                break;
            case NOTSOHARD:

                break;
            case HARD:

                break;
            case VERYHARD:

                break;
            case SWEATING:

                break;
            case IMPOSSIBLE:

                break;
            case BARRY:

                break;
            default:
                break;
        }
        return null;

    }

    private List<GenericController<Obstacle,ObstacleView>> missileChunk() {
        final Double screenSizeX = GameInfo.getInstance().getScreenWidth();
        final Double screenSizeY = GameInfo.getInstance().getScreenHeight();

        final List<GenericController<Obstacle,ObstacleView>> obstacleControllers = new ArrayList<>();
        final Random random = new Random();
        int numberOfObstacles = 1;

        for (int i = 0; i < numberOfObstacles; i++) {
            Movement movement = new MovementImpl(new Pair<>(screenSizeX - screenSizeX/20, screenSizeY/6 + random.nextDouble(screenSizeY-screenSizeY/3)),
                            new Pair<>(0.0, 0.0), new Pair<>(0.0, 0.0), new Pair<>(0.0, 0.0), List.of());
                
            obstacleControllers.addAll(this.entityGenerator.generateObstacle(ObstacleType.MISSILE, movement));
        }
        return obstacleControllers;
    }

    /*private List<GenericController<Obstacle,ObstacleView>> randomChunk() {
        final Double screenSizeX = GameInfo.getInstance().getScreenWidth();
        final Double screenSizeY = GameInfo.getInstance().getScreenHeight();
        final Double gameMovingSpeed = Double.valueOf(GameInfo.moveSpeed.get());

        List<GenericController<Obstacle,ObstacleView>> obstacleControllers = new ArrayList<>();
        Random random = new Random();
        int numberOfObstacles = random.nextInt(4) + 1;

        for (int i = 0; i < numberOfObstacles; i++) {
            int typeOfObstacle = random.nextInt(3);
            int typeOfMovement = random.nextInt(4);

            Movement movement;
            Hitbox hitbox;
            ObstacleType obstacleType;
            Image[] actualImages;

            switch (typeOfObstacle) {
                case 0:
                    // MISSILE

                    movement = new MovementImpl(new Pair<>(screenSizeX - screenSizeX/20, screenSizeY/6 + random.nextDouble(screenSizeY-screenSizeY/6)),
                            new Pair<>(0.0, 0.0), new Pair<>(0.0, 0.0), new Pair<>(0.0, 0.0), List.of(
                                    typeOfMovement == 0 ? MovementChangers.GRAVITY
                                            : typeOfMovement == 1 ? MovementChangers.SLOW
                                                    : typeOfMovement == 2 ? MovementChangers.HOMING
                                                            : MovementChangers.SPEEDY,
                                    MovementChangers.BOUNCING, MovementChangers.SPEEDY));
                    hitbox = new HitboxImpl(movement.getCurrentPosition(), new Pair<>(150.0, 50.0));
                    obstacleType = ObstacleType.MISSILE;
                    actualImages = loadImages(0, 19);
                    break;
                case 1:
                    // ZAPPER
                    movement = new MovementImpl(new Pair<>(screenSizeX, random.nextDouble() * screenSizeY),
                            new Pair<>(-gameMovingSpeed, 0.0), new Pair<>(0.0, 0.0),
                            new Pair<>(random.nextDouble() * 180,
                                    random.nextInt(2) == 0 ? 0.0 : random.nextDouble() * 5),
                            List.of());
                    hitbox = new HitboxImpl(movement.getCurrentPosition(), new Pair<>(200.0, 50.0));
                    obstacleType = ObstacleType.ZAPPER;
                    actualImages = loadImages(20, 39);
                    break;
                case 2:
                    // LASER
                    movement = new MovementImpl(new Pair<>(screenSizeX / 2, random.nextDouble() * screenSizeY),
                            new Pair<>(0.0, 0.0), new Pair<>(0.0, 0.0), new Pair<>(0.0, 0.0),
                            List.of(MovementChangers.STATIC));
                    hitbox = new HitboxImpl(movement.getCurrentPosition(), new Pair<>(1150.0, 32.0));
                    obstacleType = ObstacleType.LASER;
                    actualImages = loadImages(40, 55);
                    break;
                default:
                    throw new IllegalStateException();
            }

            //Obstacle model = this.entityGenerator.generateObstacle(obstacleType, movement, hitbox);

            ObstacleView view = new ObstacleView(actualImages);

            GenericController<Obstacle,ObstacleView> obstacle = new GenericController<Obstacle,ObstacleView>(model, view);
            obstacleControllers.add(obstacle);
        }
        return obstacleControllers;
    }*/

}
