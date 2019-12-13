/*
 * TCSS 305 - Road Rage
 */

package model;

import java.util.Map;

/**
 * The Car.
 * @author Yolanda Xu
 * @version 25 Oct 2019
 */
public class Car extends AbstractVehicle implements Vehicle {
    
    /**
     * The number of updates between Car's death and when it should revive.
     */
    private static final int MY_DEATH_ROUNDS = 15;
    
    /**
     * Constructor for a Car.
     * @param theX X location on the map
     * @param theY Y location on the map
     * @param theDir direction the Car is facing
     */
    public Car(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, MY_DEATH_ROUNDS);
    }   
    
    /**
     * Constructor for a Car.
     * @param theX X location on the map
     * @param theY Y location on the map
     * @param theDir direction the Car is facing
     * @param theDeathTime the death time
     */
    public Car(final int theX, final int theY, final Direction theDir, 
                final int theDeathTime) {
        super(theX, theY, theDir, theDeathTime);
    }

    /**
     * Cars can only travel on streets and through lights and crosswalks.
     * @return true if the car can pass
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        final boolean pass;
        if (theTerrain == Terrain.LIGHT && theLight == Light.RED) {
            pass = false;
        } else if (theTerrain == Terrain.CROSSWALK 
                        && (theLight == Light.YELLOW || theLight == Light.RED)) {
            pass = false;
        } else {
            pass = theTerrain == Terrain.STREET || theTerrain == Terrain.LIGHT
                        || theTerrain == Terrain.CROSSWALK;
        }
        return pass;
    }

    /**
     * Cars prefer straight, then left, then right. Otherwise, reverse.
     * @return the direction
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction dir = getDirection();
        if (canPass(theNeighbors.get(dir), Light.GREEN)) {
            dir = getDirection();
        } else if (canPass(theNeighbors.get(dir.left()), Light.GREEN)) {
            dir = dir.left();
        } else if (canPass(theNeighbors.get(dir.right()), Light.GREEN)) {
            dir = dir.right();
        } else {
            dir = dir.reverse();
        }
        return dir;
    }
}
