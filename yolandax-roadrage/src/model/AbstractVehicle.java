/*
 * TCSS 305 - Road Rage
 */

package model;

import java.util.Locale;

/**
 * The Abstract Vehicle parent class.
 * @author Yolanda Xu
 * @version 25 Oct 2019
 */
public abstract class AbstractVehicle implements Vehicle {
    
    /** Boolean for if the vehicle is alive. */
    private boolean myAlive;
    
    /** X location. */
    private int myX;
    
    /** Y location. */
    private int myY;
    
    /** The direction. */
    private Direction myDir;
    
    /** Original X location. */
    private final int myOriginalX;
    
    /** Original Y location. */
    private final int myOriginalY;
    
    /** Original direction. */
    private final Direction myOriginalDir;
    
    /** Number of pokes. */
    private int myPokes;
    
    /** The number of updates between Vehicle's death and when it should revive. */
    private final int myDeathTime;
    
    /**
     * Constructor for a Vehicle.
     * @param theX X location on the map
     * @param theY Y location on the map
     * @param theDir direction the Vehicle is facing
     * @param theDeathTime the death time
     */
    protected AbstractVehicle(final int theX, final int theY, final Direction theDir, 
                              final int theDeathTime) {
        myAlive = true;
        myX = theX;
        myY = theY;
        myDir = theDir;
        myOriginalX = theX;
        myOriginalY = theY;
        myOriginalDir = theDir;
        myPokes = 0;
        myDeathTime = theDeathTime;
    }

    /**
     * All Vehicles can't pass through walls.
     * @return true if the vehicle can pass
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        return !(theTerrain == Terrain.WALL);
    }

    /**
     * A command that notifies this vehicle that it has collided with another vehicle.
     */
    @Override
    public void collide(final Vehicle theOther) {
        if (this.isAlive() && theOther.isAlive()) {
            if (myPokes <= myDeathTime && myDeathTime > theOther.getDeathTime()) {
                myAlive = false;
            }
        } else  if (myPokes == myDeathTime) {
            myAlive = true;
        }
    }

    /**
     * Returns The number of updates between Vehicle's death and when it should revive.
     * @return the death time
     */
    @Override
    public int getDeathTime() {
        return myDeathTime;
    }

    /**
     * File name for alive/dead vehicle.
     * @return the file name
     */
    @Override
    public String getImageFileName() {
        String name = getClass().getSimpleName().toLowerCase(Locale.ENGLISH);
        final StringBuilder sb = new StringBuilder(name);
        if (isAlive()) {
            sb.append(".gif");
        } else {
            sb.append("_dead.gif");
        }
        name = sb.toString();
        return name;
    }

    /**
     * Returns the current direction.
     * @return the direction
     */
    @Override
    public Direction getDirection() {
        return myDir;
    }

    /**
     * Returns the X location.
     * @return x
     */
    @Override
    public int getX() {
        return myX;
    }

    /**
     * Returns the Y location.
     * @return y
     */
    @Override
    public int getY() {
        return myY;
    }

    /**
     * Returns the alive status.
     * @return true if the vehicle is alive
     */
    @Override
    public boolean isAlive() {
        return myAlive;
    }

    /**
     * Command called by GUI once for each time the city animates one turn.
     */
    @Override
    public void poke() {
        if (!myAlive && myPokes >= 0 && myPokes < getDeathTime()) {
            myPokes++;  
        } else {
            myPokes = 0;
            myAlive = true;
            setDirection(Direction.random());
        }
    }

    /**
     * Reset the GUI.
     */
    @Override
    public void reset() {
        myAlive = true;
        myPokes = 0;
        setX(myOriginalX);
        setY(myOriginalY);
        setDirection(myOriginalDir);
    }

    /**
     * Set the direction.
     */
    @Override
    public void setDirection(final Direction theDir) {
        myDir = theDir;
    }

    /**
     * Set the X location.
     */
    @Override
    public void setX(final int theX) {
        myX = theX;
    }

    /**
     * Set the Y location.
     */
    @Override
    public void setY(final int theY) {
        myY = theY;
    }

    /**
     * Override toString().
     * @return toString
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() +  " [myAlive= " + myAlive 
                        + ", myPokes= " + myPokes + ", myDir = " + myDir + "]";
    }

}
