package data;

import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Random;
public class Node {
	/**
	 * BitSet representing the attributes the node contains.
	 * The node has attribute i if index i in attributes is set to 1.
	 */
	private BitSet attributes;
	
	/**
	 * BitSet representing the edges that exist from the node.
	 * The node has an edge leading to node i if index i in edges is set to 1.
	 */
	private BitSet edges;
	
	/**
	 * LinkedHashMap mapping the index of attributes in the BitSet with the attributes' weights.
	 */
	private LinkedHashMap<Integer, Double> attrWeight;
	
	/**
	 * LinkedHashMap mapping the index of edges in the BitSet with the edges' weights.
	 */
	private LinkedHashMap<Integer, Double> edgeWeight;
	
	/**
	 * The weight of the node.
	 */
	private int weight;
	
	public Node(int weight, int attrNumber, int nodeNumber){
		this.weight = weight;
		this.attributes = new BitSet(attrNumber);
		this.edges = new BitSet(nodeNumber);
		this.attrWeight = new LinkedHashMap<Integer, Double>();
		this.edgeWeight = new LinkedHashMap<Integer, Double>();

	}
	

	public void addAttribute (int attrIndex, double weightAttr){
		attributes.set(attrIndex);
		attrWeight.put(attrIndex, weightAttr);
	}
	

	public void addEdge (int edgeIndex, double weightEdge){
		edges.set(edgeIndex);
		edgeWeight.put(edgeIndex, weightEdge);
	}

	public BitSet getAttributes() {
		return attributes;
	}

	public void setAttributes(BitSet attributes) {
		this.attributes = attributes;
	}

	public BitSet getEdges() {
		return edges;
	}

	public void setEdges(BitSet edges) {
		this.edges = edges;
	}

	public LinkedHashMap<Integer, Double> getAttrWeight() {
		return attrWeight;
	}

	public void setAttrWeight(LinkedHashMap<Integer, Double> attrWeight) {
		this.attrWeight = attrWeight;
	}

	public LinkedHashMap<Integer, Double> getEdgeWeight() {
		return edgeWeight;
	}

	public void setEdgeWeight(LinkedHashMap<Integer, Double> edgeWeight) {
		this.edgeWeight = edgeWeight;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	
}
