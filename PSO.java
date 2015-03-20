/*
 * PSO.java
 *
 * Created on 17 January 2008, 23:14
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
public class PSO {
    
    Network n;
    ArrayList<Double> gBest;
    int numberOfBests = 100;
    ArrayList<Double>[] gBestsEver;
    double[] valueOfBests;
    int[] orderOfBests;
    double valueOfGBest;
    double valueOfGBestEver;
    
    ArrayList<Double>[] gBestsEverSaved;
    double[] valueOfBestsSaved;
    int[] orderOfBestsSaved;
    
    ArrayList<Particle> particles;
    int numberOfParticles = 200;
    ArrayList<Integer> layers;
    int numberOfInputs = 2;
    int numberOfDimensions;
    double fitness = 0;
    int targetOutput;
    int tempInt;
    int numberOfOutputs = 1;
    
    /** Creates a new instance of PSO */
    public PSO() {
        initialiseNetwork();
        initialisePSO();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PSO pso = new PSO();
        pso.gBest = new ArrayList();
        double[] inputs = new double[pso.numberOfInputs];
        pso.gBestsEver = new ArrayList[pso.numberOfBests];
        pso.gBestsEverSaved = new ArrayList[pso.numberOfBests];
        pso.valueOfBests = new double[pso.numberOfBests];
        pso.valueOfBestsSaved = new double[pso.numberOfBests];
        pso.orderOfBests = new int[pso.numberOfBests];
        pso.orderOfBestsSaved = new int[pso.numberOfBests];
        for (int i=0; i<pso.numberOfBests; i++) {
            pso.gBestsEver[i] = new ArrayList();
            pso.gBestsEverSaved[i] = new ArrayList();
            for (int j=0; j<pso.numberOfDimensions; j++) {
                pso.gBestsEver[i].add(0.0);
                pso.gBestsEverSaved[i].add(0.0);
            }
            pso.valueOfBests[i] = 0;
            pso.orderOfBests[i] = i;
            pso.valueOfBestsSaved[i] = 0;
            pso.orderOfBestsSaved[i] = i;
        }
        for (int y=0; y<1; y++) {
            for (int z=0; z<5; z++) {
                
                pso.valueOfGBest = 0;
                pso.gBest.clear();
                for (int i=0; i<pso.numberOfDimensions; i++) {
                    pso.gBest.add(0.0);
                    //pso.gBestEver.add(0.0);
                }
                
                // set inputs
                
                int total;
                int iLimit = 20;
                for (int i=0; i<iLimit; i++) {
                    for (int j=0; j<pso.particles.size(); j++) {
                        pso.updateNetworkWeights(j);
                        pso.fitness = 0.0;
                        
                        for (int k=0; k<1000; k++) {
                            total = 0;
                            //inputs[0] = (int)(Math.random()*31);
                            //inputs[1] = (int)(Math.random()*inputs[0]);
                            //inputs[0] = inputs[0] - inputs[1];
                            for (int x=0; x<pso.numberOfInputs; x++) {
                                inputs[x] = (int)(Math.random()*16);
                                //if (x == 0) total += (inputs[x]*2);
                                //else
                                total += inputs[x];
                            }
                            pso.n.setNetworkInputs(inputs);
                            pso.n.forwardPass();
                            if (pso.numberOfOutputs == 1) {
                                if (total < 16) pso.fitness += pso.n.evaluateFitness(false);
                                else pso.fitness += pso.n.evaluateFitness(true);
                            } else {
                                //if (total < 10) pso.fitness += pso.n.evaluateFitness(0);
                                //else if (total < 20) pso.fitness += pso.n.evaluateFitness(1);
                                //else pso.fitness += pso.n.evaluateFitness(2);
                                int a;
                                for (a=0; a<pso.numberOfOutputs - 1; a++) {
                                    if (total < ((a+1)*10)) {
                                        //System.out.println("total: "+total+"    a: "+a);
                                        pso.fitness += pso.n.evaluateFitness(a);
                                        a = pso.numberOfOutputs - 1;
                                    }
                                }
                                if (a != pso.numberOfOutputs) {
                                    //System.out.println("total: "+total+"     a: else");
                                    pso.fitness += pso.n.evaluateFitness(pso.numberOfOutputs - 1);
                                }
                                //System.out.println("");
                            }
                        }
                        if (pso.fitness > pso.particles.get(j).valueOfPBest) {
                            pso.particles.get(j).valueOfPBest = pso.fitness;
                            for (int k=0; k<pso.numberOfDimensions; k++) pso.particles.get(j).pBest.set(k, pso.particles.get(j).coordinates.get(k));
                            if (pso.fitness > pso.valueOfGBest) {
                                pso.valueOfGBest = pso.fitness;
                                for (int k=0; k<pso.numberOfDimensions; k++) pso.gBest.set(k, pso.particles.get(j).coordinates.get(k));
                                // bestsEver code was here
                                if (pso.fitness > pso.valueOfBests[pso.orderOfBests[pso.numberOfBests - 1]]) {
                                    pso.valueOfBests[pso.orderOfBests[pso.numberOfBests - 1]] = pso.fitness;
                                    for (int k=0; k<pso.numberOfDimensions; k++) pso.gBestsEver[pso.orderOfBests[pso.numberOfBests - 1]].set(k, pso.particles.get(j).coordinates.get(k));
                                    int a = pso.numberOfBests - 1;
                                    while (a > 0) {
                                        if (pso.fitness > pso.valueOfBests[pso.orderOfBests[a-1]]) {
                                            pso.tempInt = pso.orderOfBests[a];
                                            pso.orderOfBests[a] = pso.orderOfBests[a-1];
                                            pso.orderOfBests[a-1] = pso.tempInt;
                                            a--;
                                        } else a = 0;
                                    }
                                }
                            }
                        }
                        
                        
                        pso.particles.get(j).updateVelocity();
                        pso.particles.get(j).updateCoordinates();
                    }
                    
                    if ((i > iLimit - 4) && (pso.calculateAverageFitness(pso.gBest, pso, inputs) <   0.5)) {
                        //System.out.println("i = "+i);
                        pso.resetPSO();
                        i = -1;
                        pso.gBestsEver = pso.gBestsEverSaved.clone();
                        pso.valueOfBests = pso.valueOfBestsSaved.clone();
                        pso.orderOfBests = pso.orderOfBestsSaved.clone();
                    }
                    
                }
                
                pso.gBestsEverSaved = pso.gBestsEver.clone();
                pso.valueOfBestsSaved = pso.valueOfBests.clone();
                pso.orderOfBestsSaved = pso.orderOfBests.clone();
                //System.out.println("value of gBest: "+pso.valueOfGBest );
                System.out.println(((5*y)+z+1)+"... average fitness: "+pso.calculateAverageFitness(pso.gBest, pso, inputs));
                
            }
            pso.resetPSO();
        }
        
        ArrayList<ArrayList> bestOfTheBest = new ArrayList();
        System.out.println("average fitnesses of bests: ");
        for (int i=0; i<pso.numberOfBests; i++) {
            pso.fitness = pso.calculateAverageFitness(pso.gBestsEver[pso.orderOfBests[i]], pso, inputs);
            System.out.println(pso.fitness+"  ");
            if (pso.fitness > 0.75) bestOfTheBest.add(pso.gBestsEver[pso.orderOfBests[i]]);
        }
        pso.numberOfBests = bestOfTheBest.size();
        ArrayList[] bestBests = new ArrayList[pso.numberOfBests];
        for (int i=0; i<pso.numberOfBests; i++) bestBests[i] = bestOfTheBest.get(i);
        int mostBests = pso.numberOfBests;
        for (int i=1; i<mostBests; i++) {
            pso.numberOfBests = i;
            System.out.println("panel success: "+pso.calculatePanelFitness(bestBests, pso, inputs)+" / 100");
            //for (int j=0; j<5; j++) System.out.println("");
        }
        
    }
    
    public double calculateAverageFitness(ArrayList<Double> point, PSO pso, double[] inputs) {
        double averageFitness = 0.0;
        
        pso.particles.get(0).coordinates = point;
        pso.updateNetworkWeights(0);
        int total;
        for (int i=0; i<100; i++) {
            pso.targetOutput = 0;
            total = 0;
            //inputs[0] = (int)(Math.random()*31);
            //inputs[1] = (int)(Math.random()*inputs[0]);
            //inputs[0] = inputs[0] - inputs[1];
            for (int x=0; x<pso.numberOfInputs; x++) {
                inputs[x] = (int)(Math.random()*16);
                //if (x == 0) total += (inputs[x]*2);
                //else
                total += inputs[x];
            }
            pso.n.setNetworkInputs(inputs);
            pso.n.forwardPass();
            //System.out.println("target output: "+pso.targetOutput);
            //System.out.print("fitness of gBest: ");
            if (pso.numberOfOutputs == 1) {
                if (total < 16) pso.fitness = pso.n.evaluateFitness(false);
                else pso.fitness = pso.n.evaluateFitness(true);
            } else {
                //if (total < 10) pso.fitness = pso.n.evaluateFitness(0);
                //else if (total < 20) pso.fitness = pso.n.evaluateFitness(1);
                //else pso.fitness = pso.n.evaluateFitness(2);
                int a=0;
                for (a=0; a<pso.numberOfOutputs - 1; a++) {
                    if (total < ((a+1)*10)) {
                        pso.fitness = pso.n.evaluateFitness(a);
                        a = pso.numberOfOutputs - 1;
                    }
                }
                if (a != pso.numberOfOutputs) pso.fitness = pso.n.evaluateFitness(pso.numberOfOutputs - 1);
            }
            //System.out.println(pso.fitness);
            averageFitness += pso.fitness;
            
        }//
        averageFitness = averageFitness / 100.0;
        
        return averageFitness;
    }
    
    public int calculatePanelFitness(ArrayList<Double>[] panel, PSO pso, double[] inputs) {
        int[] numberOfWrongs = new int[31];
        int[] numberOfRights = new int[31];
        for (int i=0; i<31; i++) {
            numberOfWrongs[i] = 0;
            numberOfRights[i] = 0;
        }
        int score = 0;
        int votes;
        int total;
        for (int i=0; i<100; i++) {
            votes = 0;
            pso.targetOutput = 0;
            total = 0;
            //inputs[0] = (int)(Math.random()*31);
            //inputs[1] = (int)(Math.random()*inputs[0]);
            //inputs[0] = inputs[0] - inputs[1];
            for (int x=0; x<pso.numberOfInputs; x++) {
                inputs[x] = (int)(Math.random()*16);
                //if (x == 0) total += (inputs[x]*2);
                //else
                total += inputs[x];
            }
            pso.n.setNetworkInputs(inputs);
            for (int j=0; j<pso.numberOfBests; j++) {
                pso.particles.get(0).coordinates = panel[j];
                pso.updateNetworkWeights(0);
                pso.n.forwardPass();
                if (pso.numberOfOutputs == 1) {
                    if (total < 16) pso.fitness = pso.n.evaluateFitness(false);
                    else pso.fitness = pso.n.evaluateFitness(true);
                } else {
                    //if (total < 10) pso.fitness = pso.n.evaluateFitness(0);
                    //else if (total < 20) pso.fitness = pso.n.evaluateFitness(1);
                    //else pso.fitness = pso.n.evaluateFitness(2);
                    int a=0;
                    for (a=0; a<pso.numberOfOutputs - 1; a++) {
                        if (total < ((a+1)*10)) {
                            pso.targetOutput = a;
                            a = pso.numberOfOutputs - 1;
                        }
                    }
                    if (a != pso.numberOfOutputs) pso.targetOutput = pso.numberOfOutputs - 1;
                    
                }
                if (pso.fitness > 1.0 || pso.fitness < 0.0) System.out.println("ERROR!");
                if (pso.fitness >= 0.5) {
                    votes++;
                    numberOfRights[total]++;
                } else {
                    numberOfWrongs[total]++;
                }
                
            }
            if ((double)votes > (double)(numberOfBests / 2)) score++;
        }
        /*for (int i=0; i<31; i++) {
            System.out.println(i+": "+numberOfRights[i]+" rights and "+numberOfWrongs[i]+" wrongs");
        }*/
        return score;
    }
    
    public int calculatePanelFitnessMoreThanTwoOutputs(ArrayList<Double>[] panel, PSO pso, double[] inputs) {
        int score = 0;
        int[] votes = new int[pso.numberOfOutputs];
        int total;
        int indexOfHighestFitness = -1;
        double highestFitness;
        int mostVotes;
        for (int i=0; i<100; i++) {
            for (int x=0; x<pso.numberOfOutputs; x++) votes[x] = 0;
            pso.targetOutput = 0;
            total = 0;
            for (int x=0; x<pso.numberOfInputs; x++) {
                inputs[x] = (int)(Math.random()*16);
                //if (x == 0) total += (inputs[x]*2);
                //else
                total += inputs[x];
            }
            pso.n.setNetworkInputs(inputs);
            for (int j=0; j<pso.numberOfBests; j++) {
                pso.particles.get(0).coordinates = panel[j];
                pso.updateNetworkWeights(0);
                pso.n.forwardPass();
                
                int a=0;
                for (a=0; a<pso.numberOfOutputs - 1; a++) {
                    if (total < ((a+1)*10)) {
                        pso.targetOutput = a;
                        a = pso.numberOfOutputs - 1;
                    }
                }
                if (a != pso.numberOfOutputs) pso.targetOutput = pso.numberOfOutputs - 1;
                
                highestFitness = 0.0;
                for (int b=0; b<pso.numberOfOutputs; b++) {
                    pso.fitness = pso.n.evaluateFitness(b);
                    if (pso.fitness > highestFitness) {
                        highestFitness = pso.fitness;
                        indexOfHighestFitness = b;
                    }
                }
                votes[indexOfHighestFitness]++;
                
            }
            mostVotes = 0;
            for (int b=0; b<pso.numberOfOutputs; b++) {
                if (votes[b] > mostVotes) {
                    mostVotes = votes[b];
                    indexOfHighestFitness = b;
                }
            }
            if (indexOfHighestFitness == pso.targetOutput) score++;
        }
        return score;
    }
    
    public void updateNetworkWeights(int particleIndex) {
        // x refers to dimension in PSO
        int x=0;
        double[] weights;
        // loops through all nodes in first hidden layer, setting weights (there is a weight associated with each input)
        for (int j=0; j<layers.get(0); j++) {
            weights = new double[numberOfInputs+1];
            for (int k=0; k<numberOfInputs+1; k++) {
                weights[k] = particles.get(particleIndex).coordinates.get(x);
                x++;
            }
            n.net.get(0).get(j).setWeights(weights);
        }
        // loops through all nodes in each subesquent layer, setting weights (there is a weight associated with each node from the layer below
        for (int i=1; i<layers.size(); i++) {
            for (int j=0; j<layers.get(i); j++) {
                weights = new double[layers.get(i-1)+1];
                for (int k=0; k<layers.get(i-1); k++) {
                    weights[k] = particles.get(particleIndex).coordinates.get(x);
                    x++;
                }
                n.net.get(i).get(j).setWeights(weights);
            }
        }
        
    }
    
    public void initialiseNetwork() {
        layers = new ArrayList();
        //layers.add(10);
        //layers.add(6);
        layers.add(12);
        //layers.add(3);
        layers.add(1);
        //layers.add(1);
        n = new Network(numberOfInputs, layers);
    }
    
    public void initialisePSO() {
        particles = new ArrayList();
        numberOfDimensions = (numberOfInputs+1)*layers.get(0);
        for (int i=1; i<layers.size(); i++) numberOfDimensions += (layers.get(i-1) * layers.get(i));
        for (int i=0; i<numberOfParticles; i++) particles.add(new Particle(this, numberOfDimensions));
    }
    
    public void resetPSO() {
        particles.clear();
        valueOfGBest = 0.0;
        valueOfGBestEver = 0.0;
        for (int i=0; i<numberOfParticles; i++) particles.add(new Particle(this, numberOfDimensions));
    }
    
}
