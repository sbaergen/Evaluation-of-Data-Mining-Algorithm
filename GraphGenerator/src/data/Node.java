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


    /**
     * Uses D. Knuth's algorithm for generating Poisson-distributed random variables
     * http://en.wikipedia.org/wiki/Poisson_distribution#Generating_Poisson-distributed_random_variables 27/05/2015
     * @param mean
     * @return
     */
    public int getPoisson (double mean) {
        Random rnd = new Random();
        double L = Math.exp(-mean);
        int k = 0;
        int p = 1;
        do {
            k++;
            p*=rnd.nextDouble();
        } while (p > L);
        return k-1;

    }
    /**
     * Gets a random number on an exponential distribution based on
     * the inversion method.
     * @param rate
     * @return
     */
    public double getExponential (double rate){
        Random rnd = new Random();
        int number = rnd.nextInt();
        return Math.log(1-number)/-rate;
    }
	/**
	 * Gets a random number on a Gaussian distribution. Two random numbers are used:
	 * number is used to plug into the inverse Gaussian function, where negative is
	 * used to determine to use the negative or positive square root via a Bernoulli
	 * test.
	 * @param height
	 * @param center
	 * @param width
	 * @return random Gaussian number
	 */
	public double getGaussian (double height, int center, int width){
		Random rnd = new Random();
		double number = rnd.nextDouble();
		double negative = rnd.nextDouble();
		if (negative < 0.5)
			return -Math.sqrt(-2*Math.pow(width, 2)*Math.log(number/height))+center;
		return Math.sqrt(-2*Math.pow(width, 2)*Math.log(number/height))+center;
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
