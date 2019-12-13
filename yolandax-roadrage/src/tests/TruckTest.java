/*
 * TCSS 305 - Road Rage
 */

package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Direction;
import model.Light;
import model.Terrain;
import model.Truck;
import org.junit.Test;

/**
 * The Test for Truck.
 * @author Yolanda Xu
 * @version 25 Oct 2019
 */
public class TruckTest {
    
    /**
     * The number of times to repeat a test to have a high probability that all
     * random possibilities have been explored.
     */
    private static final int TRIES_FOR_RANDOMNESS = 50;

    /**
     * Test method for {@link Truck#canPass(Terrain, Light)}.
     */
    @Test
    public void testCanPass() {
        final List<Terrain> validTerrain = new ArrayList<>();
        validTerrain.add(Terrain.STREET);
        validTerrain.add(Terrain.LIGHT);
        validTerrain.add(Terrain.CROSSWALK);
                
        final Truck truck = new Truck(0, 0, Direction.NORTH);
        // test each terrain type as a destination
        for (final Terrain destinationTerrain : Terrain.values()) {
            // try the test under each light condition
            for (final Light currentLightCondition : Light.values()) {
                if (destinationTerrain == Terrain.STREET) {
                
                    // Trucks can pass STREET under any light condition
                    assertTrue("Trucks can pass STREET"
                               + ", with light " + currentLightCondition,
                               truck.canPass(destinationTerrain, currentLightCondition));
                } else if (destinationTerrain == Terrain.LIGHT) {
                
                    // Trucks can pass LIGHT under any light condition
                    assertTrue("Trucks can pass LIGHT"
                               + ", with light " + currentLightCondition,
                               truck.canPass(destinationTerrain, currentLightCondition));
                } else if (destinationTerrain == Terrain.CROSSWALK) {
                           // Trucks can pass CROSSWALK
                           // if the light is YELLOW or GREEN but not RED

                    if (currentLightCondition == Light.RED) {
                        assertFalse("Truck should NOT be able to pass " + destinationTerrain
                            + ", with light " + currentLightCondition,
                            truck.canPass(destinationTerrain,
                                          currentLightCondition));
                    } else { // light is yellow or red
                        assertTrue("Truck should be able to pass " + destinationTerrain
                            + ", with light " + currentLightCondition,
                            truck.canPass(destinationTerrain,
                                          currentLightCondition));
                    }
                } else if (!validTerrain.contains(destinationTerrain)) {
 
                    assertFalse("Truck should NOT be able to pass " + destinationTerrain
                        + ", with light " + currentLightCondition,
                        truck.canPass(destinationTerrain, currentLightCondition));
                }
            } 
        }
    }

    /**
     * Test Truck constructor.
     */
    @Test
    public void testTruck() {
        final Truck t = new Truck(10, 11, Direction.NORTH);
        
        assertEquals("x coordinate not initialized correctly!", 10, t.getX());
        assertEquals("y coordinate not initialized correctly!", 11, t.getY());
        assertEquals("direction not initialized correctly!",
                     Direction.NORTH, t.getDirection());
        assertEquals("death time not initialized correctly!", -1, t.getDeathTime());
        assertTrue("isAlive() fails initially!", t.isAlive());
    }

    /**
     * Test randomness for choosing direction.
     */
    @Test
    public void testChooseDirectionSurroundedByStreet() {
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.STREET);
        neighbors.put(Direction.NORTH, Terrain.STREET);
        neighbors.put(Direction.EAST, Terrain.STREET);
        neighbors.put(Direction.SOUTH, Terrain.STREET);
        
        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;
        
        final Truck truck = new Truck(0, 0, Direction.NORTH);
        
        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = truck.chooseDirection(neighbors);
            
            if (d == Direction.WEST) {
                seenWest = true;
            } else if (d == Direction.NORTH) {
                seenNorth = true;
            } else if (d == Direction.EAST) {
                seenEast = true;
            } else if (d == Direction.SOUTH) { // this should NOT be chosen
                seenSouth = true;
            }
        }
 
        assertTrue("chooseDirection() fails to select randomly "
                   + "among all possible valid choices!",
                   seenWest && seenNorth && seenEast);
            
        assertFalse("chooseDirection() reversed direction when not necessary!",
                    seenSouth);
    }
    
    /**
     * Test randomness for 2 possible directions.
     */
    @Test
    public void testChooseDirectionWithTwoPossible() {
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.STREET);
        neighbors.put(Direction.NORTH, Terrain.STREET);
        neighbors.put(Direction.EAST, Terrain.TRAIL);   // this should NOT be chosen
        neighbors.put(Direction.SOUTH, Terrain.STREET);
        
        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;
        
        final Truck truck = new Truck(0, 0, Direction.NORTH);
        
        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = truck.chooseDirection(neighbors);
            
            if (d == Direction.WEST) {
                seenWest = true;
            } else if (d == Direction.NORTH) {
                seenNorth = true;
            } else if (d == Direction.EAST) { // this should NOT be chosen
                seenEast = true;
            } else if (d == Direction.SOUTH) { // this should NOT be chosen
                seenSouth = true;
            }
        }
 
        assertTrue("chooseDirection() fails to select randomly "
                   + "among all possible valid choices!",
                   seenWest && seenNorth);
        assertFalse("chooseDirection() chose invalid direction!",
                    seenEast);
        assertFalse("chooseDirection() reversed direction when not necessary!",
                    seenSouth);
    }
    
    /**
     * Test randomness for 1 possible direction.
     */
    @Test
    public void testChooseDirectionWithOnePossible() {
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.STREET);
        neighbors.put(Direction.NORTH, Terrain.TRAIL);  // this should NOT be chosen
        neighbors.put(Direction.EAST, Terrain.TRAIL);   // this should NOT be chosen
        neighbors.put(Direction.SOUTH, Terrain.STREET); // this should NOT be chosen
        
        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;
        
        final Truck truck = new Truck(0, 0, Direction.NORTH);
        
        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = truck.chooseDirection(neighbors);
            
            if (d == Direction.WEST) {
                seenWest = true;
            } else if (d == Direction.NORTH) {  // this should NOT be chosen
                seenNorth = true;
            } else if (d == Direction.EAST) {   // this should NOT be chosen
                seenEast = true;
            } else if (d == Direction.SOUTH) {  // this should NOT be chosen
                seenSouth = true;
            }
        }
 
        assertTrue("chooseDirection() fails to select randomly "
                   + "among all possible valid choices!",
                   seenWest);
        assertFalse("chooseDirection() chose invalid direction!",
                    seenNorth && seenEast);
        assertFalse("chooseDirection() reversed direction when not necessary!",
                    seenSouth);
    }
    
    /**
     * Test randomness for 1 possible direction.
     */
    @Test
    public void testChooseDirectionWithOnePossible2() {
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.TRAIL);  // this should NOT be chosen
        neighbors.put(Direction.NORTH, Terrain.STREET);  
        neighbors.put(Direction.EAST, Terrain.TRAIL);   // this should NOT be chosen
        neighbors.put(Direction.SOUTH, Terrain.STREET); // this should NOT be chosen
        
        boolean seenWest = false;
        boolean seenNorth = false;
        boolean seenEast = false;
        boolean seenSouth = false;
        
        final Truck truck = new Truck(0, 0, Direction.NORTH);
        
        for (int count = 0; count < TRIES_FOR_RANDOMNESS; count++) {
            final Direction d = truck.chooseDirection(neighbors);
            
            if (d == Direction.WEST) {          // this should NOT be chosen
                seenWest = true;
            } else if (d == Direction.NORTH) {  
                seenNorth = true;
            } else if (d == Direction.EAST) {   // this should NOT be chosen
                seenEast = true;
            } else if (d == Direction.SOUTH) {  // this should NOT be chosen
                seenSouth = true;
            }
        }
 
        assertTrue("chooseDirection() fails to select randomly "
                   + "among all possible valid choices!",
                   seenNorth);
        assertFalse("chooseDirection() chose invalid direction!",
                    seenWest && seenEast);
        assertFalse("chooseDirection() reversed direction when not necessary!",
                    seenSouth);
    }
    
    /**
     * Test method for {@link Truck#chooseDirection(java.util.Map)}.
     */
    @Test
    public void testChooseDirectionOnStreetMustReverse() {
        
        for (final Terrain t : Terrain.values()) {
            if (t != Terrain.STREET && t != Terrain.CROSSWALK && t != Terrain.LIGHT) {
                
                final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
                neighbors.put(Direction.WEST, t);
                neighbors.put(Direction.NORTH, t);
                neighbors.put(Direction.EAST, t);
                neighbors.put(Direction.SOUTH, Terrain.STREET);
                
                final Truck truck = new Truck(0, 0, Direction.NORTH);
                
                // must reverse and go SOUTH
                assertEquals("chooseDirection() failed "
                                + "when reverse was the only valid choice!",
                             Direction.SOUTH, truck.chooseDirection(neighbors));
            }
                
        }
    }
}
