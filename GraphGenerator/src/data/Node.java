package data;

import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Random;
public class Node {

	/**
	 * BitSet representing the attributes the node contains.
	 * The node has attribute i if index i in attributes is set to 1.
	 */
	BitSet attributes;
	
	/**
	 * BitSet representing the edges that exist from the node.
	 * The node has an edge leading to node i if index i in edges is set to 1.
	 */
	BitSet edges;
	
	/**
	 * LinkedHashMap mapping the index of attributes in the BitSet with the attributes' weights.
	 */
	LinkedHashMap<Integer, Double> attrWeight;
	
	/**
	 * LinkedHashMap mapping the index of edges in the BitSet with the edges' weights.
	 */
	LinkedHashMap<Integer, Double> edgeWeight;
	
	/**
	 * The weight of the node.
	 */
	int weight;
	
	public Node(int weight, int attrNumber, int nodeNumber){
		this.weight = weight;
		this.attributes = new BitSet(attrNumber);
		this.edges = new BitSet(nodeNumber);
		this.attrWeight = new LinkedHashMap<Integer, Double>();
		this.edgeWeight = new LinkedHashMap<Integer, Double>();

	}
	
	/**
	 * Adds an attribute to the attributes BitSet by setting the specified index to 1
	 * @param attrIndex
	 */
	public void addAttribute (int attrIndex){
		attributes.set(attrIndex);
		//TODO: Weights
	}
	
	/**
	 * Adds an edge to the edges Bitset by setting the specified index to 1
	 * @param edgeIndex
	 */
	public void addEdge (int edgeIndex){
		edges.set(edgeIndex);
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
	public double getExponential (int rate){
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
	
	
}
