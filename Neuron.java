/*
 * Neuron.java
 *
 * Created on 17 January 2008, 23:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package psoann;

/**
 *
 * @author Chris
 */
public class Neuron {
    
    double output;
    double[] inputs;
    double[] weights;
    
    /** Creates a new instance of Neuron */
    public Neuron(int numberOfInputs) {
        inputs = new double[numberOfInputs+1];
        weights = new double[numberOfInputs+1];
        inputs[numberOfInputs] = -1;
    }
    
    public void setWeights(double[] newWeights) {
        for (int i=0; i<inputs.length - 1; i++) {
            weights[i] = newWeights[i];
        }
    }
    
    public void setInputs(double[] newInputs) {
        for (int i=0; i<inputs.length-1; i++) {
            inputs[i] = newInputs[i];
        }
    }
    
    public void calculateOutput() {
        output = 0.0;
        for (int i=0; i<inputs.length; i++) {
            output += (inputs[i]*weights[i]);
        }
        output = (1.0 / (1.0 + Math.exp(-output)));
    }
    
    public void fireOrNoFire() {
        output = 0.0;
        for (int i=0; i<inputs.length; i++) {
            output += (inputs[i]*weights[i]);
        }
        output = (1.0 / (1.0 + Math.exp(-output)));
        if (output < 0.5) output = 0;
        else output = 1;
    }
    
}
