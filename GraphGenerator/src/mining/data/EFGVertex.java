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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;

import mining.algorithm.FlowGSpanController;

/**
 * The Vertex class represents a node in the Graph class, i.e. either a node in
 * an Execution Flow Graph (EFG) or a node in a sub-graph pattern.
 * @author cgomes
 *
 */
public class EFGVertex {
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
	Vector<EFGEdge> incoming;
	/**
	 * Set of edges going out of the node.
	 */
	Vector<EFGEdge> outgoing;
	
	/**
	 * Number of forward edges going out of the node.
	 */
	int forwardEdgeCount;
	
	// We will occasionally need dummy nodes to put in BBs with no 
	// instructions 
	/**
	 * Whether a Vertex is a dummy node. A dummy node represents a whole basic 
	 * block without any profile information (i.e. no weight information).
	 */
	//boolean isDummy = false;
	
	/**
	 * If Vertex represents an instruction (be it assembly-level instruction or bytecode)
	 * in the EFG, the ID means the instruction address. If Vertex represents a sub-graph
	 * pattern node, the ID means the depth-first-like order of the Vertex in the pattern.
	 */
	int id;
	
	/**
	 * ID of the EFG this Vertex comes from, if the Vertex represents an EFG node. 
	 */
	int symId;
	
	/**
	 * If this Vertex represents a bytecode, the bytecode ID will be stored here.
	 */
	//String bytecodeId = "";
	
	/**
	 * Attribute weight of the attribute with minimum weight in Vertex.
	 */
	double minAttrWeight;
	/**
	 * Attribute weight of the attribute with maximum weight in Vertex.
	 */
	double maxAttrWeight;
	
	boolean canBeDiscarded;
	
	//int BBN;
	
	/**
	 * Collection of instruction this Vertex is mapped to.
	 */
	//Vector<String> instructions;
	/**
	 * Collection of addresses of the instructions this Vertex is mapped to.
	 */
	//Vector<String> addresses;
	
	/**
	 * Vertex constructor.
	 * @param weight Vertex weight.
	 * @param id Vertex ID.
	 */
	public EFGVertex(double weight, int id) {
		this.weight = weight;
		this.id = id;
		//isDummy = false;
		
		attribute = new BitSet();
		attribute.set(0, NUM_ATTR - 1, false);
		attrWeight = new LinkedHashMap<Integer, Double>();
		incoming = new Vector<EFGEdge>();
		outgoing = new Vector<EFGEdge>();
		
		//Mask is initialized in such a way as to make all attributes valid.
		mask = new BitSet();
		mask.set(0, NUM_ATTR - 1, true); 
		
		minAttrWeight = Double.MAX_VALUE;
		maxAttrWeight = 0;
	
		//instructions = null;
		//addresses = null;
	
		forwardEdgeCount = 0;
		
		canBeDiscarded = true;
	}
	
	/**
	 * Vertex constructor if Vertex represents a bytecode.
	 * @param weight Vertex weight.
	 * @param id Vertex ID.
	 * @param bytecodeId Bytecode ID represented by Vertex.
	 */
	public EFGVertex(double weight, int id, String bytecodeId) {
		this(weight, id);
		//this.bytecodeId = bytecodeId;
	}
	
	/**
	 * Vertex constructor if Vertex represents a dummy node.
	 * @param weight Weight of dummy node.
	 * @param id ID of dummy node.
	 * @param dummy Whether node is really dummy or not.
	 */
	public EFGVertex(double weight, int id, boolean dummy) {
		this(weight, id);
		//isDummy = dummy;
	}
	//public void setBBN(int bbn) {
		//BBN = bbn;
	//}
	/**
	 * Returns bytecode ID.
	 * @return Bytecode ID in string representation.
	 */
	/*public String getBytecodeId() {
		return bytecodeId;}*/
	/**
	 * Returns whether Vertex is a dummy node.
	 * @return True if node is dummy, false otherwise.
	 */
	/*public boolean isDummy() {
		return isDummy;
	}*/
	
	/**
	 * Sets node ID.
	 * @param vertexId Node ID.
	 */
	public void setId(int vertexId) {
		this.id = vertexId;
	}
	
	/**
	 * Returns node ID.
	 * @return Node ID.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets Vertex weight.
	 * @param weight Vertex weight.
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	/**
	 * Returns Vertex weight.
	 * @return Vertex weight.
	 */
	public double getWeight() {
		return weight;
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
	 * Sets an attribute to have the same weight as Vertex.
	 * @param index Attribute identifier.
	 */
	public void setAttribute(int index) {
		attribute.set(index);
		attrWeight.put(index, this.weight);
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
	 * Returns BitSet of attributes.
	 * @return BitSet of attributes.
	 */
	public BitSet getAttributes() {
		return attribute;
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
	 * Returns whether an attribute given by identifier exists in Vertex.
	 * @param index Attribute identifier.
	 * @return True if attribute exists in Vertex, false otherwise.
	 */
	public boolean getAttribute(int index) {
		return attribute.get(index);
	}
	
	/**
	 * Returns weight of given attribute identifier, if attribute exists in Vertex.
	 * @param attr Attribute identifier.
	 * @return Attribute weight if attribute is found, infinity otherwise.
	 */
	public double getAttributeWeight(int attr) {
		if(attrWeight.containsKey(attr)) {
			return attrWeight.get(attr);
		}
		return Double.MAX_VALUE;
	}
	
	/**
	 * Returns number of attributes in Vertex.
	 * @return Number of attributes in Vertex.
	 */
	public int getNumAttrs() {
		return attribute.cardinality();
	}
	
	/**
	 * Sets a mask over the attribute BitSet.
	 * @param mask Mask.
	 */
	public void setMask(BitSet mask) {
		this.mask = mask;
	}
	
	/**
	 * Returns the mask currently in effect over the attribute BitSet.
	 * @return Mask.
	 */
	public BitSet getMask() {
		return this.mask;
	}
	
	/**
	 * Adds an incoming edge to Vertex.
	 * @param e Incoming edge.
	 */
	public void setIncEdge(EFGEdge e) {
		incoming.add(e);
	}
	
	/**
	 * Adds an outgoing edge to Vertex.
	 * @param e Outgoing edge.
	 */
	public void setOutEdge(EFGEdge e) {
		if(e.isBackEdge() == false) {
			++forwardEdgeCount;
		}
		outgoing.add(e);
	}
	
	/**
	 * Returns edges coming into Vertex.
	 * @return Edges coming into Vertex.
	 */
	public Vector<EFGEdge> getIncEdges() {
		return incoming;
	}
	
	/**
	 * Returns edges going out of Vertex.
	 * @return Edges going out of Vertex.
	 */
	public Vector<EFGEdge> getOutEdges() {
		return outgoing;
	}
	
	/**
	 * Returns whether a node is equal to another one. The current node is equal to another one if 
	 * the second one has a set of attributes that is equal to the attributes of the current one.
	 * @param v Node to be compared against this node.
	 * @param useThisMask Whether the current one is using its mask when considering the attribute set.
	 * @param useVMask Whether the other one is using its mask when considering its attribute set.
	 * @return Whether the two nodes are equal.
	 */
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
	 * Returns if the current node is considered to come before, in a sorted order, than the
	 * node passed as argument. A node comes before another if the representation as integer  
	 * value of its attributes is lower than the integer representation of the attributes of 
	 * the other node.
	 * @param v Node that the current node is to be compared against.
	 * @param useThisMask Whether to use the mask over the current node's attribute set.
	 * @param useVMask Whether to use a mask over the other node's attribute set.
	 * @return True if current node comes before node passed as argument, false otherwise.
	 */
	public boolean isLessThan(EFGVertex v, boolean useThisMask, boolean useVMask) {
		BitSet thisBitSet;
		BitSet vBitSet;
		
		if(useThisMask) {
			//DEBUG
			//System.out.println("Original bitset = " + this.attribute.toString());
			//end DEBUG
			thisBitSet = (BitSet) this.attribute.clone();
			//DEBUG
			//System.out.println("This bitset = " + thisBitSet.toString());
			//end DEBUG
			thisBitSet.and(mask);
			//DEBUG
			//System.out.println("This bitset = " + thisBitSet.toString());
			//end DEBUG
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
		
		int thisInt = bitsetToInt(thisBitSet);
		int vInt = bitsetToInt(vBitSet);
		
		if(thisInt < vInt) {
			return true;
		}
		return false;
	}
	
	/**
	 * Helper method to convert a BitSet of attributes into an integer value that represents
	 * it.
	 * @param set Attribute set.
	 * @return Integer that represents attribute set.
	 */
	public int bitsetToInt(BitSet set) {
		int power = 0;
		int result = 0;
		for(int i = 0; i < set.length(); ++i) {
			if(set.get(i) == true) {
				result += (int)Math.pow(2, power);
			}
			++power;
		}

		return result;
	}
	
	/**
	 * Returns sum of existing attribute weights.
	 * @return Sum of attribute weights for this node.
	 */
	public double getAttrWeightSum() {
		float total = 0;
		Iterator<Integer> i = attrWeight.keySet().iterator();
		int idx = 0;
		while(i.hasNext()) {
			idx = i.next();
			total += attrWeight.get(idx);
		}
		return total;
	}
	
	/**
	 * Returns string representation of node.
	 * @return String representation of node.
	 */
	public String toString() {
		String str = String.valueOf(Integer.toHexString(this.id))+ ": ";
		for (int idx = attribute.nextSetBit(0); idx >= 0; idx = attribute.nextSetBit(idx+1)) {
			str += "(" + idx+ ")";
		 }
		
		return str;
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
	 * Returns string representation of attribute set.
	 * @return String representation of attribute set.
	 */
	public String bitsetToString() {
		return attribute.toString();	
	}

	/**
	 * Returns outgoing edge number.
	 * @return Number of outgoing edges.
	 */
	public int getOutEdgeCount() {
		return outgoing.size();
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
	public EFGEdge getOutEdge(int i) {
		return outgoing.get(i);
	}
	/**
	 * Returns an specific incoming edge.
	 * @param i Index of incoming edge.
	 * @return Incoming edge.
	 */
	public EFGEdge getInEdge(int i) {
		return incoming.get(i);
	}

	/**
	 * Returns child nodes of current node.
	 * @return Set of child nodes.
	 */
	public Collection<EFGVertex> getChildren() {
		Vector<EFGVertex> children = new Vector<EFGVertex>();
		// Queue up this node's children
		for(int i = 0; i < outgoing.size(); ++i) {
			children.add(outgoing.get(i).getToVertex());
		}
		return children;
	}

	/**
	 * Returns the frequencies of each one of the outgoing edges for this node.
	 * @return Edge frequencies for each of the outgoing edges.
	 */
	public Vector<Double> getOutEdgesFreq() {
		Vector<Double> freqSet = new Vector<Double>();
		for(int i = 0; i < outgoing.size(); ++i) {
			freqSet.add(outgoing.get(i).getFrequency());
		}
		return freqSet;
	}

	/**
	 * Adds that certain attributes exist in the node.
	 * @param toAdd Set of attributes to be added.
	 */
	public void addAttributes(BitSet toAdd) {
		attribute.or(toAdd);
	}

	/**
	 * Adds attribute weights to the attributes of a node.
	 * @param toAdd Mapping to be added.
	 */
	public void addAttrWeights(LinkedHashMap<Integer, Double> toAdd) {
		Set<Integer> keySet = toAdd.keySet();
		
		for(Integer key : keySet) {
			attrWeight.put(key, attrWeight.get(key) + toAdd.get(key));
		}
	}

	/**
	 * Returns the minimum attribute weight of this node.
	 * @return Minimum attribute weight.
	 */
	public Double getMinAttrWeight() {
		if(minAttrWeight == Double.MAX_VALUE) {
			Collection<Double> weights = this.attrWeight.values();
				for(Double val : weights) {
					minAttrWeight = Math.min(val, minAttrWeight);
				}	
		}
		return minAttrWeight;
	}

	/**
	 * Returns maximum attribute weight of this node.
	 * @return Maximum attribute weight.
	 */
	public double getMaxAttrWeight() {
		if(maxAttrWeight == 0) {
			Collection<Double> weights = this.attrWeight.values();
				for(Double val : weights) {
					maxAttrWeight = Math.max(val, maxAttrWeight);
				}	
		}
		return maxAttrWeight;
	}

	/**
	 * Adds an instruction to the list of instructions that this node corresponds to.
	 * Note that in this case it is implied that the node is part of a sub-graph pattern.
	 * @param instruction Instruction to be added.
	 */
	/*public void addInstruction(String instruction) {
		if(instructions == null) {
			instructions = new Vector<String>();
		}
		instructions.add(instruction);
	}*/
	/**
	 * Returns list of instructions that this node corresponds to.
	 * @return List of instructions the node corresponds to.
	 */
	/*public Vector<String> getInstructions() {
		return instructions;
	}*/
	
	/**
	 * Returns addresses of instructions this node is mapped to.
	 * @return List of instruction addresses this node is mapped to.
	 */
	/*public Vector<String> getAddresses() {
		return addresses;
	}
	/**
	 * Adds instruction addresses to the list of addresses this node is mapped to.
	 * @param address Address to add.
	 */
/*	public void addAddress(String address) {
		if(addresses == null) {
			addresses = new Vector<String>();
		}
		addresses.add(address);
	}
*/
	/**
	 * Returns all child nodes of this node that are linked to it by forward edges.
	 * @return List of child nodes linked to this node by forward edges.
	 */
	public Vector<EFGVertex> getForwardChildren() {
		Vector<EFGVertex> children = new Vector<EFGVertex>();
		// Queue up this node's children
		for(int i = 0; i < outgoing.size(); ++i) {
			if(outgoing.get(i).isBackEdge() == false) {
				children.add(outgoing.get(i).getToVertex());
			}
		}
		return children;
	}

	/**
	 * Sets method ID this node is located at. Note that the implication here is
	 * that this node is part of an EFG, and not a node in a sub-graph pattern.
	 * @param symbol ID of the method this node is located at.
	 */
	public void setSymbolId(int symbol) {
		symId = symbol;
	}

	public int getAttributeCount() {
		return attrWeight.keySet().size();
	}
	
	public void setDiscardable(boolean discardable) {
		canBeDiscarded = discardable;
	}
	
	public void updateDiscardableRoot() {
		if(canBeDiscarded == true) {
			canBeDiscarded = false;
		
			Vector<EFGEdge> edgeQueue = new Vector<EFGEdge>(outgoing);
			
			while(edgeQueue.isEmpty() == false) {
				Vector<EFGEdge> auxQueue = new Vector<EFGEdge>();
				for(EFGEdge e : edgeQueue) {
					if(e.isDiscardable() == true) {
						e.setDiscardable(false);
						EFGVertex toVertex = e.getToVertex();
						toVertex.setDiscardable(false);
						
						auxQueue.addAll(toVertex.getOutEdges());
					}
				}
				edgeQueue.clear();
				edgeQueue = auxQueue;
			}
		}
	}
	
	public boolean isDiscardable() {
		return canBeDiscarded;
	}

	/*public Integer getBBN() {
		return BBN;
	}*/
}
