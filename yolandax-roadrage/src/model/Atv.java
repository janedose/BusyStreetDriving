/*
 * TCSS 305 - Road Rage
 */

package model;

import java.util.Map;
import java.util.Random;

/**
 * The ATV.
 * @author Yolanda Xu
 * @version 25 Oct 2019
 */
public final class Atv extends AbstractVehicle implements Vehicle {

    /**
     * The number of updates between ATV's death and when it should revive.
     */
    private static final int MY_DEATH_ROUNDS = 25;
    
    /**
     * Constructor for an ATV.
     * @param theX X location on the map
     * @param theY Y location on the map
     * @param theDir direction the ATV is facing
     */
    public Atv(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, MY_DEATH_ROUNDS);
    }

    /**
     * Helper method to decide a random possible direction.
     * @param theNeighbors Map of neighboring directions
     * @param dir1 Direction 1
     * @param dir2 Direction 2
     * @param dir3 Direction 3
     * @return the direction
     */
    private Direction helpDir(final Map<Direction, Terrain> theNeighbors, final Direction 
                              dir1, final Direction dir2, final Direction dir3) {
        Direction dir = getDirection();
        final Random rn = new Random();
        final int randomDir = rn.nextInt(2);
        if (canPass(theNeighbors.get(dir1), Light.GREEN)) {
            dir = dir1;
        } else {
            switch (randomDir) {
                case 0: 
                    if (canPass(theNeighbors.get(dir2), Light.GREEN)) {
                        dir = dir2;
                    } else {
                        dir = dir3;
                    }
                    break;
                case 1: 
                    if (canPass(theNeighbors.get(dir3), Light.GREEN)) {
                        dir = dir3;
                    } else {
                        dir = dir2;
                    }
                    break;
                default:
                    break;
            }
        }
        return dir;
    }
    
    /**
     * ATVs randomly select to go straight, turn left, or turn right.
     * @return the direction
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction dir = getDirection();
        final Random rn = new Random();
        final int randomDir = rn.nextInt(3);
        switch (randomDir) {
            case 0:
                dir = helpDir(theNeighbors, dir.left(), dir.right(), dir);
                break;
            case 1:
                dir = helpDir(theNeighbors, dir.right(), dir.left(), dir);
                break;
            case 2:
                dir = helpDir(theNeighbors, dir, dir.left(), dir.right());
                break;
            default:
                break;
        }
        return dir;
    }
}
