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
	
	/**
	 * Adds an attribute to the attributes BitSet by setting the specified index to 1
	 * using a uniform distribution
	 * @param attrIndex
	 */
	public void addAttribute (int attrIndex, int min, int max){
		double weightAttr = getUniform(min, max);
		attributes.set(attrIndex);
		attrWeight.put(attrIndex, weightAttr);
	}
	
	/**
	 * Adds an attribute to the attributes BitSet by setting the specified index to 1
	 * using an exponential distribution
	 * @param attrIndex
	 * @param distribution
	 * @param rate
	 */
	public void addAttribute (int attrIndex, double rate){
		double weightAttr = getExponential(rate);
		attributes.set(attrIndex);
		attrWeight.put(attrIndex, weightAttr);
	}
	
	/**
	 * Adds an attribute to the attributes BitSet by setting the specified index to 1
	 * using a gaussian distribution
	 * @param attrIndex
	 * @param distribution
	 * @param height
	 * @param center
	 * @param width
	 */
	public void addAttribute (int attrIndex, double height, int center, int width){
		double weightAttr = getGaussian(height, center, width);
		attributes.set(attrIndex);
		attrWeight.put(attrIndex, weightAttr);
	}
	
	/**
	 * Adds an attribute to the attributes BitSet by setting the specified index to 1
	 * using a poisson distribution
	 * @param attrIndex
	 * @param distribution
	 * @param mean
	 */
	public void addAttribute (int attrIndex, int mean){
		double weightAttr = getPoisson(mean);
		attributes.set(attrIndex);
		attrWeight.put(attrIndex, weightAttr);
	}
	
	/**
	 * Adds an edge to the edges BitSet by setting the specified index to 1
	 * using a uniform distribution. The index will correspond to the destination
	 * node's index
	 * @param attrIndex
	 */
	public void addEdge (int edgeIndex, int min, int max){
		double weightEdge = getUniform(min, max);
		attributes.set(edgeIndex);
		attrWeight.put(edgeIndex, weightEdge);
	}
	
	/**
	 * Adds an edge to the edges BitSet by setting the specified index to 1
	 * using an exponential distribution. The index will correspond to the destination
	 * node's index
	 * @param attrIndex
	 */
	public void addEdge (int edgeIndex, double rate){
		double weightEdge = getExponential(rate);
		attributes.set(edgeIndex);
		attrWeight.put(edgeIndex, weightEdge);
	}
	
	/**
	 * Adds an edge to the edges BitSet by setting the specified index to 1
	 * using a gaussian distribution. The index will correspond to the destination
	 * node's index
	 * @param attrIndex
	 */
	public void addEdge (int edgeIndex, double height, int center, int width){
		double weightEdge = getGaussian(height, center, width);
		attributes.set(edgeIndex);
		attrWeight.put(edgeIndex, weightEdge);
	}
	
	/**
	 * Adds an edge to the edges BitSet by setting the specified index to 1
	 * using a poisson distribution. The index will correspond to the destination
	 * node's index
	 * @param attrIndex
	 */
	public void addEdge (int edgeIndex, int mean){
		double weightEdge = getPoisson(mean);
		attributes.set(edgeIndex);
		attrWeight.put(edgeIndex, weightEdge);
	}
	
	
	/**
	 * Gets a random number on a Poisson distribution
	 * @param mean
	 * @return
	 */
	public double getPoisson (double mean){
		Random rnd = new Random();
		int number = rnd.nextInt();
		return Math.pow(Math.E, mean)*Math.pow(mean, number)/factorial(number);
	}
	
	/**
	 * Gets a random number on an exponential distribution
	 * @param rate
	 * @return
	 */
	public double getExponential (double rate){
		Random rnd = new Random();
		int number = rnd.nextInt();
		return rate*Math.pow(Math.E, rate*-1*number);
	}
	
	/**
	 * Gets a random number on a Gaussian distribution
	 * @param height
	 * @param center
	 * @param width
	 * @return
	 */
	public double getGaussian (double height, int center, int width){
		Random rnd = new Random();
		int number = rnd.nextInt();
		return Math.pow(Math.E, -1*Math.pow(number-center, 2)/(2*Math.pow(width, 2)));
	}
	
	/**
	 * Gets a random number on a Uniform distribution
	 * @param min
	 * @param max
	 * @return
	 */
	public double getUniform (int min, int max){
		Random rnd = new Random();
		return rnd.nextInt(max-min+1) + min;
	}
	
	/**
	 * Returns the factorial of a given number
	 * @param number
	 * @return
	 */
	public int factorial (int number){
		if (number==1)
			return number;
		return number*(number-1);
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
