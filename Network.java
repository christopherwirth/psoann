/*
 * Network.java
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
public class Network {
    
    ArrayList<ArrayList<Neuron>> net;
    double[] networkInputs;
    
    /** Creates a new instance of Network */
    public Network(int numberOfInputs, ArrayList<Integer> topology) {
        net = new ArrayList();
        // set up first layer of neurons, taking set inputs
        net.add(new ArrayList());
        for (int i=0; i<topology.get(0); i++) net.get(0).add(new Neuron(numberOfInputs));
        // set up remaining layers, taking inputs from all nodes in layer below
        for (int i=1; i<topology.size(); i++) {
            net.add(new ArrayList());
            for (int j=0; j<topology.get(i); j++) {
                net.get(i).add(new Neuron(topology.get(i-1)));
            }
        }
    }
    
    public void setNetworkInputs(double[] inputs) {
        networkInputs = new double[inputs.length];
        for (int i=0; i<inputs.length; i++) {
            networkInputs[i] = inputs[i];
        }
    }
    
    public double[] getOutputsOfLayer(int layerIndex) {
        double[] outputs = new double[net.get(layerIndex).size()];
        for (int i=0; i<outputs.length; i++) {
            net.get(layerIndex).get(i).calculateOutput();
            //net.get(layerIndex).get(i).fireOrNoFire();
            outputs[i] = net.get(layerIndex).get(i).output;
        }
        return outputs;
    }
    
    public void forwardPass() {
        // for all neurons in first layer, set inputs as inputs to the network
        for (int j=0; j<net.get(0).size(); j++) net.get(0).get(j).setInputs(networkInputs);
        // for all other layers
        for (int i=1; i<net.size(); i++) {
            double[] outputsOfLayerBelow = getOutputsOfLayer(i-1);
            for (int j=0; j<net.get(i).size(); j++) net.get(i).get(j).setInputs(outputsOfLayerBelow);
        }
        for (int i=0; i<net.get(net.size()-1).size(); i++) {
            net.get(net.size()-1).get(i).calculateOutput();
            //net.get(net.size()-1).get(i).fireOrNoFire();
            //System.out.print(net.get(net.size()-1).get(i).output+"     ");
        }
        //System.out.println();
    }
    
    public double evaluateFitness(int targetOutputIndex) {
        double fitness = 0.0;
        // adds total of all outputs
        //System.out.println(targetOutputIndex);
        for (int i=0; i<net.get(net.size()-1).size(); i++) {
            fitness += net.get(net.size()-1).get(i).output;
            //System.out.print(i+": "+net.get(net.size()-1).get(i).output+"   ");
        }
        // sets fitness to (value for target output) / (sum of values of all outputs)
        fitness = net.get(net.size()-1).get(targetOutputIndex).output / fitness;
        //System.out.println();
        //System.out.println(fitness);
        //System.out.println();
        return fitness;
    }
    
    public double evaluateFitness(boolean targetIsOne) {
        if (targetIsOne) return net.get(net.size()-1).get(0).output;
        else return (1 - net.get(net.size()-1).get(0).output);
    }
    
}
