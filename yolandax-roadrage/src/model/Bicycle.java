/*
 * TCSS 305 - Road Rage
 */

package model;

import java.util.Map;

/**
 * The Bicycle.
 * @author Yolanda Xu
 * @version 25 Oct 2019
 */
public final class Bicycle extends AbstractVehicle implements Vehicle {
    
    /**
     * The number of updates between Bicycle's death and when it should revive.
     */
    private static final int MY_DEATH_ROUNDS = 35;
    
    /**
     * Boolean for if the terrain is a trail.
     */
    private boolean myTrail;
    
    /**
     * Constructor for a Bicycle.
     * @param theX X location on the map
     * @param theY Y location on the map
     * @param theDir direction the Bicycle is facing
     */
    public Bicycle(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, MY_DEATH_ROUNDS);
    }    

    /**
     * Bicycles can travel on streets and through lights and crosswalk lights, and trails.
     * @return true if the bicycle can pass
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        final boolean pass;
        if ((theTerrain == Terrain.LIGHT)
                        && (theLight == Light.RED || theLight == Light.YELLOW)) {
            pass = false;
        } else if ((theTerrain == Terrain.CROSSWALK)
                        && (theLight == Light.RED || theLight == Light.YELLOW)) {
            pass = false;
        } else {
            pass = theTerrain == Terrain.STREET || theTerrain == Terrain.LIGHT
                            || theTerrain == Terrain.CROSSWALK || theTerrain == Terrain.TRAIL;
        } 
        return pass;
    }
    
    /**
     * Helper method to choose a direction to the trail.
     * @param theNeighbors Map of neighboring directions
     * @param dir1 Direction 1
     * @param dir2 Direction 2
     * @param dir3 Direction 3
     * @return the direction
     */
    private Direction helpTrail(final Map<Direction, Terrain> theNeighbors, final Direction 
                                dir1, final Direction dir2, final Direction dir3) {
        Direction dir = getDirection();
        if (theNeighbors.get(dir1) == Terrain.TRAIL) {
            dir = dir1;
            myTrail = true;
        } else if (theNeighbors.get(dir2) == Terrain.TRAIL) {
            dir = dir2;
            myTrail = true;
        } else if (theNeighbors.get(dir3) == Terrain.TRAIL) {
            dir = dir3;
            myTrail = true;
        } else {
            myTrail = false;
        }
        return dir;
    }

    /**
     * Trail preferred. Then straight, then left, then right. Otherwise, reverse.
     * @return the direction
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction dir = helpTrail(theNeighbors, getDirection(), getDirection().left(), 
                                  getDirection().right());
        if (myTrail) {
            dir = helpTrail(theNeighbors, dir, dir.left(), dir.right());
        } else {
            if (canPass(theNeighbors.get(getDirection()), Light.GREEN)) {
                dir = getDirection();
            } else if (canPass(theNeighbors.get(dir.left()), Light.GREEN)) {
                dir = dir.left();
            } else if (canPass(theNeighbors.get(dir.right()), Light.GREEN)) {
                dir = dir.right();
            } else {
                dir = dir.reverse();
            }
        }
        return dir;
    }
}
