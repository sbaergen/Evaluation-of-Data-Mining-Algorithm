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

import java.util.Collection;

/**
 * The Edge class represents an edge in the Execution Flow Graph (EFG), or the edge
 * of a sub-graph pattern.
 * An edge is identified by the nodes (instances of Vertex class) that it connects,
 * called the from-node and the to-node.
 * @author cgomes
 *
 */
public class EFGEdge {
	/**
	 * Vertex the edge comes from.
	 */
	EFGVertex from; 
	/**
	 * Vertex the edge goes into.
	 */
	EFGVertex to; 
	/**
	 * Whether the edge is a backward-edge. If it is, this may mean 1 of 2 things:
	 * 1. If the edge represents an EFG, it means the from-node represents an instruction with
	 * higher instruction address than the instruction represented by the to-node.
	 * 2. If the edge represents a sub-graph pattern, it means the edge connects two nodes that
	 * already existed in the parent sub-graph pattern of the pattern this edge belongs to.
	 */
	boolean backEdge; 
	/**
	 * Edge frequency.
	 */
	double frequency; 
	/**
	 * Weight of the attribute with minimum weight, among nodes this edge connects.
	 */
	double minAttrWeight; 
	
	boolean canBeDiscarded; 
	
	/**
	 * Edge constructor. Initializes variables and identifies back-edges.
	 * @param from From-node.
	 * @param to To-node.
	 * @param frequency Edge frequency.
	 */
	public EFGEdge(EFGVertex from, EFGVertex to, double frequency) {
		this.from = from;
		this.to = to;
		this.frequency = frequency;
		minAttrWeight = Double.MAX_VALUE;
		
		if(from != null && to != null && (from.getId() >= to.getId())) {
			backEdge = true;
		}
		else {
			this.backEdge = false;
		}
		
		canBeDiscarded = true;
	}

	/**
	 * Whether an edge is a backward-edge.
	 * @return True if edge is a back-edge, false otherwise.
	 */
	public boolean isBackEdge() {
		return backEdge;
	}
	
	/**
	 * Sets edge frequency.
	 * @param freq Frequency value.
	 */
	public void setFrequency(double freq) {
		this.frequency = freq;
	}
	
	public double getFrequency() {
		return frequency;
	}
	
	/**
	 * Returns from-node of edge.
	 * @return From-node.
	 */
	public EFGVertex getFromVertex() {
		return from;
	}
	/**
	 * Returns to-node of edge.
	 * @return To-node.
	 */
	public EFGVertex getToVertex() {
		return to;
	}
	
	/**
	 * Determines whether two edges are equal. They are equal if their from-nodes and to-nodes 
	 * are respectively equal.
	 * @param edgeObj Edge to compare with.
	 * @return True if edges are equal, false otherwise.
	 */
	public boolean equals(Object edgeObj) {
		EFGEdge e = (EFGEdge)edgeObj;
		if(this.from.equals(e.getFromVertex(), false, false) && this.to.equals(e.getToVertex(), false, false))
			return true;
		return false;
	}
	
	/**
	 * Normalizes the frequency of this edge, by dividing by a total frequency value.
	 * Typically the total frequency value is the sum of all edge frequencies over the
	 * EFGs collected from a database.
	 * @param total Total frequency value.
	 */
	public void normalize(double total) {
		frequency /= total;
	}
	
	/**
	 * Converts to string representation of an Edge.
	 * @return String representation of an Edge.
	 */
	public String toString() {
		String str = "( " + "[ " + from.toString() + " ], " + "[ " + to.toString() + " ] )";
		return str;
	}
	
	/**
	 * Returns the minimum attribute weight among the two nodes connected by this Edge.
	 * If the weight has not been computed before, compute it first and then return it.
	 * Note that this method expects attribute weights to never change...and they should
	 * not.
	 * @return Minimum attribute weight.
	 */
	public Double getMinAttrWeight() {
		if(minAttrWeight == Double.MAX_VALUE) {
			Collection<Double> fromWeights = from.getAttrWeights().values();
			Collection<Double> toWeights = to.getAttrWeights().values();
				for(Double fromVal : fromWeights) {
					minAttrWeight = Math.min(fromVal, minAttrWeight);
				}	
				for(Double toVal : toWeights) {
					minAttrWeight = Math.min(toVal, minAttrWeight);
				}
		}
		return minAttrWeight;
	}
	
	public boolean isDiscardable() {
		return canBeDiscarded;
	}

	public void setDiscardable(boolean discardable) {
		canBeDiscarded = discardable;
	}
}
