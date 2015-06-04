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
    final String INPUT = "input.txt";
    final String CONFIG = "config.txt";
    static Vector<EFG> graphs;
    static Vector<String> values;
    static int position = 12;

    public static void main(String args[]) {
        graphs = new Vector<>();
        Main m = new Main();
        values = m.manualParse(args[0]);
        m.createGraph();
        m.createConfigFile();
    }


    public void createGraph() {
        int nodePosition;
        int numEFG = Integer.parseInt(values.get(6));
        int numAttr = Integer.parseInt(values.get(8));
        double attrProb = Double.parseDouble(values.get(9));
        double edgeProb = Double.parseDouble(values.get(10));
        addEFG(numEFG); // Create EFGs with number of nodes,
        nodePosition = position;
        for (EFG e: graphs){
            position = nodePosition;
            int size = e.getSize();
            for (int i = 0; i < size; i++){
                double weight = getDistributedWeight();
                Node node = new Node(weight, numAttr, size);
                addAttributes(node, attrProb, numAttr);
                e.addNode(i, node);
                if (i < size-1)
                    position = nodePosition;
            }
            addEdges(e, edgeProb);
        }
        createGraphFile();
    }

    public void addEFG(int numEFG){
        int[] sizes = getEFGSizes(numEFG);
        for (int i = 0; i < numEFG; i++){
            int efgSize = sizes[i];
            if (efgSize != 1)
                efgSize+=2;
            EFG efg = new EFG(efgSize);
            graphs.add(efg);
        }
    }
//TODO:
    public int[] getEFGSizes(int numEFG){
        int numNodes = Integer.parseInt(values.get(7));
        String dist = values.get(11);
        int sum = 0;
        int index;
        int[] sizes = new int[numEFG];
        switch (dist) {
            case ("G"):
                for (int i = 0; i < numEFG; i++){
                    sizes[i] = (int) getGaussianWeight(Double.parseDouble(values.get(12)), numNodes/numEFG, Integer.parseInt(values.get(14)));
                    if (sizes[i] <= 0)
                        sizes[i] = 1;
                    sum += sizes[i];
                }
                index = 0;
                while (sum != numNodes){
                    if (sum < numNodes){
                        sizes[numEFG-index-1]++;
                        sum++;
                    } else {
                        if (sizes[index] != 1) {
                            sizes[index]--;
                            sum--;
                        }
                    }
                    if (index == numEFG-1)
                        index = 0;
                    else
                        index++;
                }
                position+=3;
                break;
            case ("P"):
                for (int i = 0; i < numEFG; i++) {
                    sizes[i] = (int) getPoissonNumber(numNodes / numEFG);
                    if (sizes[i] == 0)
                        sizes[i] = 1;
                    sum += sizes[i];
                }
                index = 0;
                while (sum != numNodes){
                    if (sum < numNodes){
                        sizes[numEFG-index-1]++;
                        sum++;
                    } else {
                        if (sizes[index] != 1) {
                            sizes[index]--;
                            sum--;
                        }
                    }
                    if (index == numEFG-1)
                        index = 0;
                    else
                        index++;
                }
                position++;
                break;
            case ("E"):
                for (int i = 0; i < numEFG; i++) {
                    double rate = Double.parseDouble(values.get(6));
                    double num = getInverseExponentialCDF(rate, 0.99);
                    num /= (double) numEFG;
                    num *= (double) (i + 1);
                    sizes[i] = (int) (numNodes * getExponentialNode(rate, num));
                    if (sizes[i] == 0)
                        sizes[i] = 1;
                    sum+=sizes[i];
                }
                index = 0;
                while (sum != numNodes){
                    if (sum < numNodes){
                        sizes[numEFG-index-1]++;
                        sum++;
                    } else {
                        if (sizes[index] != 1) {
                            sizes[index]--;
                            sum--;
                        }
                    }
                    if (index == numEFG-1)
                        index = 0;
                    else
                        index++;
                }
                position++;
                break;
            case ("U"):
                for (int i = 0; i < numEFG; i++) {
                    if (numNodes % numEFG < (i + 1))
                        sizes[i] = numNodes / numEFG;
                    else
                        sizes[i] = numNodes / numEFG + 1;
                }
                position+=2;
                break;
        }
        return sizes;
    }


    public void addAttributes(Node node, double attrProb, int numAttr){
        int tempPosition;
        boolean insertAttr;
        for (int i = 0; i < numAttr; i++){
            insertAttr = getBernoulli(attrProb);
            tempPosition = position;
            double weight = getDistributedWeight();
            if (insertAttr){
                node.addAttribute(i, weight);
            }
            if (i < numAttr-1)
                position = tempPosition;
        }
    }

    public void addEdges(EFG efg, double edgeProb){
        int edgePosition = position;
        boolean insertEdge;
        boolean validSink = false;
        double weight;
        int numNodes = efg.getSize();
        BitSet sourceCheck = new BitSet(numNodes);
        Node node;
        Node source = efg.getNodes().get(0);
        for (int i = 1; i < numNodes-1; i++) {
            node = efg.getNodes().get(i);
            for (int j = i; j < numNodes - 1; j++) {
                insertEdge = getBernoulli(edgeProb);
                if (insertEdge) {
                    weight = getDistributedWeight();
                    node.addEdge(j, weight);
                    if (i!=j)
                        sourceCheck.set(j);
                }
                position = edgePosition;
                if (i != j) {
                    insertEdge = getBernoulli(edgeProb);
                    if (insertEdge) {
                        weight = getDistributedWeight();
                        node = efg.getNodes().get(j);
                        node.addEdge(i, weight);
                        sourceCheck.set(i);
                    }
                    position = edgePosition;
                }
            }
            // Sink Node
            node = efg.getNodes().get(i);
            int card = node.getEdges().cardinality();
            if (card == 0) {
                weight = getDistributedWeight();
                node.addEdge(numNodes - 1, weight);
                validSink = true;
            } else if (card == 1) {
                if (node.getEdges().nextSetBit(0) == i) {
                    weight = getDistributedWeight();
                    node.addEdge(numNodes - 1, weight);
                    validSink = true;
                }
            }

            if (i != numNodes - 2) {
                position = edgePosition;
            }
        }

        // Source Node
        for (int k = 1; k < numNodes-1; k++){
            if (!sourceCheck.get(k)) {
                position = edgePosition;
                weight = getDistributedWeight();
                source.addEdge(k, weight);
            }
        }
        // If source has no outgoing edges add one to first node
        if (source.getEdges().nextSetBit(0) == -1 && numNodes != 1){
            position = edgePosition;
            weight = getDistributedWeight();
            source.addEdge(1, weight);
        }

        // If no edge to sink add one from last node
        if (!validSink && numNodes != 1) {
            position = edgePosition;
            weight = getDistributedWeight();
            efg.getNodes().get(numNodes-1).addEdge(numNodes-2, weight);
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
                position+=3;
                break;
            case ("E"):
                double rate = Double.parseDouble(values.get(position + 1));
                weight = getExponentialWeight(rate);
                position+=2;
                break;
            case ("G"):
                double height = Double.parseDouble(values.get(position + 1));
                int center = Integer.parseInt(values.get(position + 2));
                int width = Integer.parseInt(values.get(position + 3));
                weight = getGaussianWeight(height, center, width);
                position+=4;
                break;
            case ("P"):
                double mean = Double.parseDouble(values.get(position + 1));
                weight = getPoissonNumber(mean);
                position+=2;
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
                String arguments = "";
                String input;
                while ((input = stream.readLine()) != null) {
                    input = input.replaceAll("//.*", " ");
                    arguments += input.replaceAll("\\s+", " ");
                }
                String[] params = arguments.split(" ");
                for (String c : params) {
                    values.add(c);
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

    public double getExponentialPDF (double rate, double x){
        return rate*Math.exp(-rate*x);
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
    public double getPoissonNumber(double mean) {
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
        visitor.visit(tree);
    }


	public void createGraphFile(){
		int size = graphs.size();
		//http://stackoverflow.com/questions/2885173/java-how-to-create-a-file-and-write-to-a-file 25/05/2015 for PrintWriter lines
		try {
			PrintWriter writer = new PrintWriter(INPUT, "UTF-8");
			writer.println(size);
			// For each EFG
			for (EFG currentEFG: graphs){
                int numEdges = 0;
                String edgeString = "";
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
                    if (edgeIndex != -1 && edgeString != "")
                        edgeString += '\n';
					while (edgeIndex != -1){
						double edgeWeight = currentEdges.get(edgeIndex);
						edgeString += j + " " + edgeIndex + " " + edgeWeight;
						edgeIndex = edges.nextSetBit(edgeIndex+1);
                        if (edgeIndex != -1)
                            edgeString += '\n';
					}
					
				}
				writer.println(numEdges);
                if (numEdges > 0)
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

    public void createConfigFile(){
        try {
            PrintWriter writer = new PrintWriter(CONFIG, "UTF-8");
            String header = "localhost" + '\n' + "EFG_ZSys" + '\n' + 50000 + '\n' + "username" + '\n' + "password" +
                    '\n' + "DT120524" + '\n' + 0 + '\n' + values.get(0) + '\n' + 0 + '\n' + values.get(1) + '\n' +
                    values.get(2) + '\n' + values.get(3) + '\n' + 0 + '\n' + 0 + '\n' + 0 + '\n' + values.get(4) +
                    '\n' + values.get(5);
            writer.println(header);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
