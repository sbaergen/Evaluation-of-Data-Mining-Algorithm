import java.io.*;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Vector;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.*;

import data.EFG;
import data.Node;

public class Main {
    final String FILENAME = "input.txt";
    static Vector<EFG> graphs;
    static Vector<String> values;
    static int position = 5;

    public static void main(String args[]) {

        Main m = new Main();
        values = m.manualParse(args[0]);
        //m.createGraph();

        int numEFG = 3;
        int numNode = 10;
        double rate = 1;
        double max = m.getInverseExponentialCDF(rate, .99);
        double div = max/(double)numEFG;
        for (int i = 0; i < numEFG; i++){
            double out = div*(i+1);
            double full = m.getExponentialNode(rate, out);
            double bottom = div*i;
            full-=m.getExponentialNode(rate, bottom);
            System.out.println((int)(full*numNode));
        }

    }



    public void createGraph() {
        int numEFG = Integer.parseInt(values.get(0));
        int numAttr = Integer.parseInt(values.get(2));
        double attrProb = Double.parseDouble(values.get(3));
        double edgeProb = Double.parseDouble(values.get(4));
        addEFG(numEFG); // Create EFGs with number of nodes,
        for (EFG e: graphs){
            int size = e.getSize();
            for (int i = 0; i < size; i++){
                double weight = getDistributedWeight();
                Node node = new Node(weight, numAttr, size);
                addAttributes(node, attrProb, numAttr);
                e.addNode(i, node);
            }
            addEdges(e, edgeProb);
        }
        createFile();
    }

    public void addEFG(int numEFG){
        for (int i = 0; i < numEFG; i++){
            int efgSize = getEFGSize(i, numEFG);
            EFG efg = new EFG(efgSize);
            graphs.add(efg);
        }
    }

    //TODO: Generate EFG size
    public int getEFGSize(int index, int numEFG){
        int numNodes = Integer.parseInt(values.get(2));
        String dist = values.get(5);
        switch(dist){
            case ("U"):
                if (numNodes%numEFG < (index+1))
                    return numNodes/numEFG;
                return numNodes/numEFG + 1;
            case ("E"):
                double rate = Double.parseDouble(values.get(6));
                double num = getInverseExponentialCDF(rate, 0.99);
                num/=(double)numEFG;
                num*=(double)(index+1);
                return (int)(numNodes*getExponentialNode(rate, num));
            case ("G"):
            case ("P"):


        }
        return 0;
    }

    public void addAttributes(Node node, double attrProb, int numAttr){
        boolean insertAttr;
        for (int i = 0; i < numAttr; i++){
            insertAttr = getBernoulli(attrProb);
            if (insertAttr){
                double weight = getDistributedWeight();
                node.addAttribute(i, weight);
            }
        }
    }

    public void addEdges(EFG efg, double edgeProb){
        boolean insertEdge;
        double weight = 0;
        int numNodes = efg.getSize();
        for (int i = 0; i < numNodes; i++){
            for (int j = i; j < numNodes; j++){
                insertEdge = getBernoulli(edgeProb);
                if (insertEdge){
                    weight = getDistributedWeight();
                    Node node = efg.getNodes().get(i);
                    node.addEdge(j, weight);
                }
                if (i!=j) {
                    insertEdge = getBernoulli(edgeProb);
                    if (insertEdge) {
                        weight = getDistributedWeight();
                        Node node = efg.getNodes().get(i);
                        node.addEdge(i, weight);
                    }
                }
            }
        }
    }

    public double getDistributedWeight(){
        double weight = 0;
        String dist = values.get(position).toUpperCase();
        switch (dist) {
            case ("U"):
                int min = Integer.parseInt(values.get(position + 1));
                int max = Integer.parseInt(values.get(position + 2));
                weight = getUniformWeight(min, max);
                break;
            case ("E"):
                double rate = Double.parseDouble(values.get(position + 1));
                weight = getExponentialWeight(rate);
                break;
            case ("G"):
                double height = Double.parseDouble(values.get(position + 1));
                int center = Integer.parseInt(values.get(position + 2));
                int width = Integer.parseInt(values.get(position + 3));
                weight = getGaussianWeight(height, center, width);
                break;
            case ("P"):
                double mean = Double.parseDouble(values.get(position + 1));
                weight = getPoissonWeight(mean);
                break;
        }
        return weight;
    }

    public boolean getBernoulli (double prob){
        Random rnd = new Random();
        if (rnd.nextDouble() < prob)
            return true;
        return false;
    }

    public Vector<String> manualParse(String filename){
            Vector<String> values = new Vector<String>();
            try {
                BufferedReader stream = new BufferedReader(new FileReader(filename));
                String input;
                while ((input = stream.readLine()) != null) {
                    String[] params = input.split(" ");
                    for (String c : params) {
                        values.add(c);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return values;
        }


    public double getUniformWeight (int min, int max){
        Random rnd = new Random();
        return rnd.nextInt(max-min+1)+min;
    }

    /**
     * Returns a random number on an exponential distribution between 0 and rate
     * @param rate
     * @return
     */
    public double getExponentialNode (double rate, double x){
        return 1-Math.exp(-rate*x);
    }

    public double getInverseExponentialCDF (double rate, double x){
        return -Math.log(1-x)/rate;
    }

    public double getExponentialWeight (double rate){
        Random rnd = new Random();
        double number = rnd.nextDouble();
        return (Math.log(1-number)/-rate) + 1;
    }


    public double getPoissonNode (int mean){
        Random rnd = new Random();
        int number = rnd.nextInt(2*mean);
        return Math.pow(mean, number)/(Math.exp(mean)*factorial(number));
    }

    /**
     * Uses D. Knuth's algorithm for generating Poisson-distributed random variables
     * http://en.wikipedia.org/wiki/Poisson_distribution#Generating_Poisson-distributed_random_variables 27/05/2015
     * @param mean
     * @return
     */
    public double getPoissonWeight (double mean) {
        Random rnd = new Random();
        double L = Math.exp(-mean);
        int k = 0;
        double p = 1;
        do {
            k++;
            p *= rnd.nextDouble();
        } while (p > L);
        return k - 1;
    }

    public int factorial (int number) {
        if (number <= 1)
            return 1;
        return number*factorial(number-1);
    }

    public double getGaussianNode (double height, double center, double width){
        Random rnd = new Random();
        double number = rnd.nextDouble();
        number*=rnd.nextInt(Math.abs((int)(2*width)));
        return height*Math.exp(-Math.pow(number-center,2)/(2*width*width));
    }

    public double getGaussianWeight (double height, int center, int width) {
        Random rnd = new Random();
        double number = rnd.nextDouble();
        double negative = rnd.nextDouble();
        if (negative < 0.5)
            return -Math.sqrt(-2 * Math.pow(width, 2) * Math.log(number / height)) + center;
        return Math.sqrt(-2 * Math.pow(width, 2) * Math.log(number / height)) + center;
    }

    public static void parseText(@NotNull String text){
        InputParserLexer lexer = new InputParserLexer(new ANTLRInputStream(text));
        parse(lexer);
    }

	public static void parseFile(@NotNull String filename) {
        InputParserLexer lexer = null;
        try {
            InputStream stream = new FileInputStream(filename);
            lexer = new InputParserLexer(new ANTLRInputStream(stream));
        } catch (IOException e){
            e.printStackTrace();
        }
        parse(lexer);
    }


    private static void parse(@NotNull InputParserLexer lexer) {
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        InputParserParser parser = new InputParserParser(tokens);
        InputParserParser.ParseContext tree = parser.parse();

        ExtendedVisitor visitor = new ExtendedVisitor();
        System.out.println(tree.toStringTree());
        visitor.visit(tree);
        System.out.println(tree.toString());
    }


	public void createFile (){
		String edgeString = "";
		int numEdges = 0;
		int size = graphs.size();
		//http://stackoverflow.com/questions/2885173/java-how-to-create-a-file-and-write-to-a-file 25/05/2015 for PrintWriter lines
		try {
			PrintWriter writer = new PrintWriter(FILENAME, "UTF-8");
			writer.println(size);
			// For each EFG
			for (EFG currentEFG: graphs){
				LinkedHashMap<Integer, Node> currentNodes = currentEFG.getNodes();
				int numNodes = currentNodes.size();
				writer.println(numNodes);
				
				// For each node in EFG for node information
				for (int j = 0; j < numNodes; j++){
					writer.println(j);
					Node currentNode = currentNodes.get(j);
					writer.println(currentNode.getWeight());
					BitSet attributes = currentNode.getAttributes();
					int numAttr = attributes.cardinality();
					writer.println(numAttr);
					LinkedHashMap<Integer, Double> currentAttributes = currentNode.getAttrWeight();
					
					// For each attribute in Node
					int attrIndex = attributes.nextSetBit(0);
					while (attrIndex != -1){
						double attrWeight = currentAttributes.get(attrIndex);
						writer.println(attrIndex + " " + attrWeight);
						attrIndex = attributes.nextSetBit(attrIndex+1);
					}
					
					//Edge info
					BitSet edges = currentNode.getEdges();
					numEdges += edges.cardinality();
					LinkedHashMap<Integer, Double> currentEdges = currentNode.getEdgeWeight();
					int edgeIndex = edges.nextSetBit(0);
					while (edgeIndex != -1){
						double edgeWeight = currentEdges.get(edgeIndex);
						edgeString += edgeIndex + " " + edgeWeight + "/n";
						edgeIndex = edges.nextSetBit(edgeIndex+1);
					}
					
				}
				writer.println(numEdges);
				writer.println(edgeString);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
