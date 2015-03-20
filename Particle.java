/*
 * Particle.java
 *
 * Created on 17 January 2008, 23:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package psoann;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class Particle {
    
    ArrayList<Double> coordinates;
    ArrayList<Double> velocity;
    PSO pso;
    static double momentumRate = 0.9;
    ArrayList<Double> pBest;
    double valueOfPBest = 0;
    int coordLimit = 100;
    double euclidP;
    double euclidG;
    
    /** Creates a new instance of Particle */
    public Particle(PSO pso, int numberOfDimensions) {
        this.pso = pso;
        coordinates = new ArrayList();
        velocity = new ArrayList();
        pBest = new ArrayList();
        for (int i=0; i<numberOfDimensions; i++) {
            // generates random particles with coords between -1 and 1 in every dimension
            coordinates.add((Math.random()*2*coordLimit)-coordLimit);
            // gives each particle a velocity between -mR and +mR in every dimension
            velocity.add((Math.random()*momentumRate*0.8)-(momentumRate*0.4));
            // sets pBest to current position
            pBest.add(coordinates.get(i));
        }
    }
    
    public void updateVelocity() {
        /*euclidP = 0.0;
        euclidG = 0.0;
        for (int i=0; i<(pso.numberOfDimensions-1); i++) {
            euclidP += Math.pow((pBest.get(i)-coordinates.get(i)), 2.0);
            euclidG += Math.pow((pso.gBest.get(i)-coordinates.get(i)), 2.0);
        }
        euclidP = Math.sqrt(euclidP);
        euclidG = Math.sqrt(euclidG);*/
        for (int i=0; i<velocity.size(); i++) {
            velocity.set(i,
                    (
                     (velocity.get(i) +
                      (pBest.get(i) - coordinates.get(i)) +
                      (pso.gBest.get(i) - coordinates.get(i))
                     )
                    * momentumRate)
            );
        }
    }
    
    public void updateCoordinates() {
        for (int i=0; i<coordinates.size(); i++) coordinates.set(i, Math.max(-coordLimit, Math.min(coordLimit, coordinates.get(i)+velocity.get(i))));
        //for (int i=0; i<coordinates.size(); i++) coordinates.set(i, (Math.random()*coordLimit*2)-coordLimit);
    }
        
}
