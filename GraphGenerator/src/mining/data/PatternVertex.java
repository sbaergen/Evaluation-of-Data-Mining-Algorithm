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

import java.util.BitSet;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Vector;

import mining.algorithm.FlowGSpanController;

public class PatternVertex {
	/**
	 * Bitset that represents which attributes exist in node.
	 * If i-th element of the bitset is 1, then i-th attribute exists in this node.
	 */
	BitSet attribute;
	/**
	 * Mapping between attribute index in bitset, which identifies it, and its weight.
	 */
	LinkedHashMap<Integer,Double> attrWeight;
	/**
	 * Node weight, i.e. number of cycles the instruction represented by this node takes
	 * to complete.
	 */
	double weight; 
	/**
	 * Total number of attributes allowed in a Vertex. It is necessary to define this 
	 * constant because BitSet requires a constant when its size is defined, and cannot 
	 * be defined without a size argument.
	 */
	public static int NUM_ATTR = 32; 
	/**
	 * Mask used when considering some but not all of the node attributes, in any
	 * comparison with another node.
	 */
	BitSet mask;
	
	/**
	 * Set of edges coming into the node.
	 */
	Vector<PatternEdge> incoming;
	/**
	 * Set of edges going out of the node.
	 */
	Vector<PatternEdge> outgoing;
	
	/**
	 * Collection of instruction this Vertex is mapped to.
	 */
	Vector<String> instructions; 
	/**
	 * Collection of addresses of the instructions this Vertex is mapped to.
	 */
	Vector<String> addresses;
	
	/**
	 * Number of forward edges going out of the node.
	 */
	int forwardEdgeCount;
	
	/**
	 * If Vertex represents an instruction (be it assembly-level instruction or bytecode)
	 * in the EFG, the ID means the instruction address. If Vertex represents a sub-graph
	 * pattern node, the ID means the depth-first-like order of the Vertex in the pattern.
	 */
	int id;
	
	/**
	 * Whether this Vertex has been visited by an external procedure. May have multiple
	 * uses, but right now is used only for the depth-first-like traversal that reassigns
	 * IDs to nodes of a sub-graph pattern when new edges are added to it.
	 */
	boolean visited;
	
	/**
	 * Vertex constructor.
	 * @param weight Vertex weight.
	 * @param id Vertex ID.
	 */
	public PatternVertex(double weight, int id) {
		this.weight = weight;
		this.id = id;
		
		attribute = new BitSet();
		attribute.set(0, NUM_ATTR - 1, false);
		attrWeight = new LinkedHashMap<Integer, Double>();
		incoming = new Vector<PatternEdge>();
		outgoing = new Vector<PatternEdge>();
		
		//Mas is initialized in such a way as to make all attributes valid.
		mask = new BitSet();
		mask.set(0, NUM_ATTR - 1, true); 
	
		instructions = null;
		addresses = null;
	
		forwardEdgeCount = 0;
	
		visited = false;
	}
	
	/**
	 * Adds an instruction to the list of instructions that this node corresponds to.
	 * Note that in this case it is implied that the node is part of a sub-graph pattern.
	 * @param instruction Instruction to be added.
	 */
	public void addInstruction(String instruction) {
		if(instructions == null) {
			instructions = new Vector<String>();
		}
		instructions.add(instruction);
	}
	/**
	 * Returns list of instructions that this node corresponds to.
	 * @return List of instructions the node corresponds to.
	 */
	public Vector<String> getInstructions() {
		return instructions;
	}
	
	/**
	 * Converts into a string representation of the node, but instead of showing its attributes,
	 * shows the instruction it represents. Uses an instanceIdx argument to indicate which of
	 * the node instances we want.
	 * @param instanceIdx Instance whose string representation we want.
	 * @return String representation of node, showing the instruction it corresponds to.
	 */
	public String toMapping(int instanceIdx) {
		if(instructions != null && instanceIdx < instructions.size()) {
			String str = Long.toHexString(this.id)+ ": ";
			str += "(" + addresses.get(instanceIdx) + " => " + instructions.get(instanceIdx)+ ")";
			return str;
		}
		return "";	
	}

	/**
	 * Returns addresses of instructions this node is mapped to.
	 * @return List of instruction addresses this node is mapped to.
	 */
	public Vector<String> getAddresses() {
		return addresses;
	}
	/**
	 * Adds instruction addresses to the list of addresses this node is mapped to.
	 * @param address Address to add.
	 */
	public void addAddress(String address) {
		if(addresses == null) {
			addresses = new Vector<String>();
		}
		addresses.add(address);
	}

	/**
	 * Returns whether a node has been visited.
	 * @return True if visited, false otherwise.
	 */
	public boolean visited() {
		return visited;
	}

	/**
	 * Sets if a node has been visited.
	 * @param b Whether node has been visited.
	 */
	public void setVisited(boolean b) {
		visited = b;
	}

	/**
	 * Returns all child nodes of this node that are linked to it by forward edges.
	 * @return List of child nodes linked to this node by forward edges.
	 */
	public Vector<PatternVertex> getForwardChildren() {
		Vector<PatternVertex> children = new Vector<PatternVertex>();
		// Queue up this node's children
		for(int i = 0; i < outgoing.size(); ++i) {
			if(outgoing.get(i).isBackEdge() == false) {
				children.add(outgoing.get(i).getToVertex());
			}
		}
		return children;
	}

	public int getAttributeCount() {
		return attrWeight.keySet().size();
	}
	
	/**
	 * Returns attributes that exist in Vertex, along with corresponding weights.
	 * @return Mapping of attributes and weights.
	 */
	@SuppressWarnings("unchecked")
	public LinkedHashMap<Integer, Double> getAttrWeights() {
		return (LinkedHashMap<Integer, Double>) attrWeight.clone();
	}

	/**
	 * Sets attribute weight for a given attribute in Vertex.
	 * @param index Attribute identifier.
	 * @param weight Weight of attribute.
	 */
	public void setAttribute(int index, double weight) {
		//if(weight > hotness) {
			//System.out.println("Attribute weight greater than vertex hotness!");
			//return;
		//}
		attribute.set(index);
		double previousWeight = 0;
		
		if(attrWeight.get(index) != null) {
			previousWeight = attrWeight.get(index);
		}
		attrWeight.put(index, previousWeight + weight);
	}

	/**
	 * Returns node ID.
	 * @return Node ID.
	 */
	public int getId() {
		return id;
	}	

	/**
	 * Returns BitSet of attributes.
	 * @return BitSet of attributes.
	 */
	public BitSet getAttributes() {
		return attribute;
	}

	/**
	 * Sets the attributes that exist in the Vertex.
	 * @param attributes Set of existing attributes.
	 */
	public void setAttributes(BitSet attributes) {
		this.attribute = attributes;
	}

	/**
	 * Sets the attributes that exist in Vertex, along with corresponding weights.
	 * @param attributeWeights Mapping of attributes and respective weights.
	 */
	public void setAttrWeights(LinkedHashMap<Integer, Double> attributeWeights) {
		this.attrWeight = attributeWeights;
	}

	/**
	 * Returns child nodes of current node.
	 * @return Set of child nodes.
	 */
	public Collection<PatternVertex> getChildren() {
		Vector<PatternVertex> children = new Vector<PatternVertex>();
		// Queue up this node's children
		for(int i = 0; i < outgoing.size(); ++i) {
			children.add(outgoing.get(i).getToVertex());
		}
		return children;
	}

	/**
	 * Returns whether an attribute given by identifier exists in Vertex.
	 * @param index Attribute identifier.
	 * @return True if attribute exists in Vertex, false otherwise.
	 */
	public boolean getAttribute(int index) {
		return attribute.get(index);
	}

	/**
	 * Returns forward outgoing edge number.
	 * @return Number of forward outgoing edges.
	 */
	public int getForwardOutEdgeCount() {
		return forwardEdgeCount;
	}
	/**
	 * Returns number of incoming edges.
	 * @return Number of incoming edges.
	 */
	public int getInEdgeCount() {
		return incoming.size();
	}
	
	/**
	 * Returns an specific outgoing edge.
	 * @param i Index of the outgoing edge to be returned.
	 * @return Outgoing edge.
	 */
	public PatternEdge getOutEdge(int i) {
		return outgoing.get(i);
	}
	/**
	 * Returns an specific incoming edge.
	 * @param i Index of incoming edge.
	 * @return Incoming edge.
	 */
	public PatternEdge getInEdge(int i) {
		return incoming.get(i);
	}

	/**
	 * Returns whether a node is equal to another one. The current node is equal to another one if 
	 * the second one has a set of attributes that is equal to the attributes of the current one.
	 * @param v Node to be compared against this node.
	 * @param useThisMask Whether the current one is using its mask when considering the attribute set.
	 * @param useVMask Whether the other one is using its mask when considering its attribute set.
	 * @return Whether the two nodes are equal.
	 */
	public boolean equals(PatternVertex v, boolean useThisMask, boolean useVMask) {
		BitSet thisBitSet;
		BitSet vBitSet;
		
		if(useThisMask) {
			thisBitSet = (BitSet) this.attribute.clone();
			thisBitSet.and(mask);
		}
		else {
			thisBitSet = this.attribute;
		}
		
		if(useVMask) {
			vBitSet = (BitSet) v.getAttributes().clone();
			vBitSet.and(v.getMask());
		}
		else {
			vBitSet = v.getAttributes();
		}
		
		if(thisBitSet.equals(vBitSet))
			return true;
		return false;
	}

	private BitSet getMask() {
		return mask;
	}

	public boolean equals(EFGVertex v, boolean useThisMask, boolean useVMask) {
		BitSet thisBitSet;
		BitSet vBitSet;
		
		if(useThisMask) {
			thisBitSet = (BitSet) this.attribute.clone();
			thisBitSet.and(mask);
		}
		else {
			thisBitSet = this.attribute;
		}
		
		if(useVMask) {
			vBitSet = (BitSet) v.getAttributes().clone();
			vBitSet.and(v.getMask());
		}
		else {
			vBitSet = v.getAttributes();
		}
		
		if(thisBitSet.equals(vBitSet))
			return true;
		return false;
	}

	/**
	 * Returns number of attributes in Vertex.
	 * @return Number of attributes in Vertex.
	 */
	public int getNumAttrs() {
		return attribute.cardinality();
	}

	public void setId(int thisId) {
		id = thisId;
	}

	/**
	 * Returns outgoing edge number.
	 * @return Number of outgoing edges.
	 */
	public int getOutEdgeCount() {
		return outgoing.size();
	}
	
	/**
	 * Converts the node into another type of string representation, for the special case
	 * when the node is not linked to any edges.
	 * @return String representation of node.
	 */
	public String toCode() {
		String str = "";//String.valueOf(this.id)+ ": ";
		
		for(Integer idx : attrWeight.keySet()) {
			str += "(" + idx + ")";
		}
		return str;
	}
	
	/**
	 * Returns string representation of node.
	 * @return String representation of node.
	 */
	public String toString() {
		String str = String.valueOf(Long.toHexString(this.id))+ ": ";
		for (int idx = attribute.nextSetBit(0); idx >= 0; idx = attribute.nextSetBit(idx+1)) {
			str += "(" + idx+ ")";
		 }
		
		return str;
	}
	
	/**
	 * Returns Vertex weight.
	 * @return Vertex weight.
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Adds an incoming edge to Vertex.
	 * @param e Incoming edge.
	 */
	public void setIncEdge(PatternEdge e) {
		incoming.add(e);
	}
	
	/**
	 * Adds an outgoing edge to Vertex.
	 * @param e Outgoing edge.
	 */
	public void setOutEdge(PatternEdge e) {
		if(e.isBackEdge() == false) {
			++forwardEdgeCount;
		}
		outgoing.add(e);
	}
}
