/*
 * TCSS 305 - Road Rage
 */

package model;

import java.util.Map;
import java.util.Random;

/**
 * The Human.
 * @author Yolanda Xu
 * @version 25 Oct 2019
 */
public final class Human extends AbstractVehicle implements Vehicle {
    
    /**
     * The number of updates between Human's death and when it should revive.
     */
    private static final int MY_DEATH_ROUNDS = 45;
    
    /**
     * Boolean for if the terrain is a crosswalk.
     */
    private boolean myCrosswalk;
    
    /**
     * Constructor for a Human.
     * @param theX X location on the map
     * @param theY Y location on the map
     * @param theDir direction the Human is facing
     */
    public Human(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, MY_DEATH_ROUNDS);
    }    

    /**
     * Humans can walk on grass or crosswalks.
     * @return true if the Human can pass
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        final boolean pass;
        if (theTerrain == Terrain.GRASS) {
            pass = true;
        } else { 
            pass = (theTerrain == Terrain.CROSSWALK)
                            && (theLight == Light.YELLOW || theLight == Light.RED);
        }
        return pass;
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
        if (canPass(theNeighbors.get(dir1), Light.YELLOW)) {
            dir = dir1;
        } else {
            switch (randomDir) {
                case 0: 
                    if (canPass(theNeighbors.get(dir2), Light.YELLOW)) {
                        dir = dir2;
                    } else {
                        dir = dir3;
                    }
                    break;
                case 1: 
                    if (canPass(theNeighbors.get(dir3), Light.YELLOW)) {
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
     * Helper method to decide if the neighboring direction is a crosswalk.
     * @param theNeighbors Map of neighboring directions
     * @param dir1 Direction 1
     * @param dir2 Direction 2
     * @param dir3 Direction 3
     * @return the direction
     */
    private Direction helpCrosswalk(final Map<Direction, Terrain> theNeighbors, 
                                    final Direction dir1, final Direction dir2, 
                                    final Direction dir3) {
        Direction dir = getDirection();
        if (theNeighbors.get(dir1) == Terrain.CROSSWALK) {
            dir = dir1;
            myCrosswalk = true;
        } else if (theNeighbors.get(dir2) == Terrain.CROSSWALK) {
            dir = dir2;
            myCrosswalk = true;
        } else if (theNeighbors.get(dir3) == Terrain.CROSSWALK) {
            dir = dir3;
            myCrosswalk = true;
        } else {
            myCrosswalk = false;
        }
        return dir;
    }

    /**
     * Crosswalks preferred. Then Random direction. Otherwise, reverse.
     * @return the direction
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction dir = helpCrosswalk(theNeighbors, getDirection(), getDirection().left(), 
                                  getDirection().right());
        final boolean cantPass = !canPass(theNeighbors.get(dir), Light.YELLOW) 
                        && !canPass(theNeighbors.get(dir.left()), Light.YELLOW)
                        && !canPass(theNeighbors.get(dir.right()), Light.YELLOW);
        if (myCrosswalk) {
            dir = helpCrosswalk(theNeighbors, dir, dir.left(), dir.right());
        } else {
            if (cantPass) {
                dir = dir.reverse();
            } else {
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
            }
        }
        return dir;
    }
}    
