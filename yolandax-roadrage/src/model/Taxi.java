/*
 * TCSS 305 - Road Rage
 */

package model;

/**
 * The Taxi.
 * @author Yolanda Xu
 * @version 25 Oct 2019
 */
public final class Taxi extends Car implements Vehicle {
    
    /**
     * The number of updates between Taxi's death and when it should revive.
     */
    private static final int MY_DEATH_ROUNDS = 15;
    
    /**
     * Number of clock cycles needed in order for Taxi to go.
     */
    private static final int CLOCK_CYCLE = 3;
    
    /**
     * Counter for each clock cycle.
     */
    private static int counter;
    
    /**
     * Constructor for a Taxi.
     * @param theX X location on the map
     * @param theY Y location on the map
     * @param theDir direction the Taxi is facing
     */
    public Taxi(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, MY_DEATH_ROUNDS);
    }    

    /**
     * If a crosswalk light is immediately ahead of the taxi and the crosswalk 
     * light is red, the Taxi stays still and does not move for 3 clock cycles 
     * or until the crosswalk light turns 
     * green, whichever occurs first.
     * @return true if the taxi can pass
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean pass = false;
        if (theTerrain == Terrain.CROSSWALK && (theLight == Light.RED)) {
            counter++;
        }
        if (theTerrain == Terrain.CROSSWALK && (theLight == Light.YELLOW)) {
            pass = true;
        } else if (counter == CLOCK_CYCLE) {
            counter = 0;    //reset counter
            pass = true;
        }
        return pass || super.canPass(theTerrain, theLight);
    }
}
