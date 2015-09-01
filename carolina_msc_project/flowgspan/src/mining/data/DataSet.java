/****************************
 * IBM Confidential
 * Licensed Materials - Property of IBM
 *
 * IBM Rational Developer for Power Systems Software
 * IBM Rational Team Concert for Power Systems Software
 *
 * (C) Copyright IBM Corporation 2010.
 *
 * The source code for this program is not published or otherwise divested of its trade secrets, 
 * irrespective of what has been deposited with the U.S. Copyright Office.
 */package mining.data;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;

/**
 * The DataSet class represents the set of Execution Flow Graphs (EFGs)
 * that have an EFG edge frequency (sum of all edge frequencies in the EFG)that,
 * when divided by the total edge frequency (of all EFGs collected from the database
 * for the program being analyzed), is higher than a minimum defined in the FlowGSpan
 * class.
 * @author cgomes
 *
 */
public class DataSet extends LinkedHashMap<Integer, ExecutionFlowGraph> {
	/**
	 * Total weight value in dataset (sum of node weights of all nodes of all
	 * EFGs in the dataset).
	 */
	double totalWeight;
	/**
	 * Total frequency value in dataset (sum of edge frequencies of all edges
	 * of all EFGs in the dataset).
	 */
	double totalFreq;
	
	/**
	 * True if each EFG instruction is actually a bytecode.
	 */
	boolean usesBytecodes;
	
	/**
	 * List of all distinct useful EFGs in the dataset. Useful 
	 * EFGS are the ones where patterns of the same generation have 
	 * been found, and they become the dataset for the next generation.
	 */
	Set<Integer> usefulGraphs;
	
	/**
	 * Very simple constructor, just initializes data.
	 */
	public DataSet() {
		totalWeight = totalFreq = 0;
		usesBytecodes = false;
		usefulGraphs = Collections.synchronizedSet(new LinkedHashSet<Integer>());
	}
	
	/**
	 * Returns total dataset weight. Lazily calculates the total weight
	 * if it hasn't been calculated before.
	 * @return Total dataset weight.
	 */
	public double getTotalWeight() {
		if(totalWeight > 0) {
			return totalWeight;
		}
		
		for(ExecutionFlowGraph g : this.values()) {
			totalWeight += g.getTotalWeight();
		}
		return totalWeight;
	}
	
	/**
	 * Returns total dataset frequency. Calculates the total frequency lazily
	 * if hasn't been calculated before.
	 * @return Total dataset frequency value.
	 */
	public double getTotalFreq() {
		if(totalFreq > 0) {
			return totalFreq;
		}
		
		for(ExecutionFlowGraph g : this.values()) {
			totalFreq += g.calculateInternalFreq();
		}
		return totalFreq;
	}
	
	/**
	 * Returns the whole dataset (i.e. all EFGs) in string format.
	 * @return The whole dataset in string format.
	 */
	public String toString() {
		String returnStr = "";
		for(int i = 0; i < this.size(); ++i) {
			returnStr += this.get(i).toString();
			returnStr += "\n\n";
		}
		return returnStr;
	}

	/**
	 * Returns whether bytecodes are being used as EFG instructions.
	 * @return True if bytecodes are used as EFG instructions in this dataset,
	 * false otherwise.
	 */
	public boolean usesBytecodes() {
		return usesBytecodes;
	}

	/**
	 * Sets whether the instructions in this dataset's EFGs should be considered
	 * bytecodes.
	 * @param useBytecodes True if instructions should be considered bytecodes, false
	 * otherwise.
	 */
	public void setUsesBytecodes(boolean useBytecodes) {
		this.usesBytecodes = useBytecodes;
	}

	public synchronized void addUsefulGraphs(Vector<Integer> gs) {
		usefulGraphs.addAll(gs);
	}
	
	public synchronized void updateDataset() {
		Vector<Integer> keySet = new Vector<Integer>(this.keySet());
		//DEBUG
		int count = 0;
		//end DEBUG
		for(Integer efgId : keySet) {
			if(usefulGraphs.contains(efgId) == false) {
				this.remove(efgId);
				//DEBUG
				++count;
				//end DEBUG
			}
		}
		usefulGraphs.clear();
		//DEBUG
		System.out.println("EFGs removed from dataset: " + count);
		//end DEBUG
	}
}