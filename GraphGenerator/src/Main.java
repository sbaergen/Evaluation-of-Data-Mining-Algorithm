import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import mining.algorithm.ReturnInfo;
//import org.antlr.v4.runtime.misc.NotNull;
//import org.antlr.v4.runtime.*;

import data.EFG;
import data.Node;

public class Main {
    final static String INPUT = "input.txt";
    final static String CONFIG = "config.txt";
    final static String COUNTERS = "counters.txt";
    final static String OUTPUT = "output.txt";
    final static String RESULT = "result.txt";
    final static String CSV = "data.csv";
    static Vector<EFG> graphs;
    static Vector<String> values;
    static Vector<EFG> patternBank;
    static int position = 12;

    public static void main(String args[]) {
        graphs = new Vector<EFG>();
        Main m = new Main();
        values = m.manualParse(args[0]);
        if (args.length == 2)
            patternBank = m.createPatterns(args[1]);
        else if (args.length == 1){
            int size = values.size();
            values.set(size-1, 0+"");
            values.set(size-2, 0+"");
            values.set(size-3, 0+"");
        }
        else {
            System.out.println("Usage: Main Parameters [PatternsFileName(optional)]");
            System.exit(10);
        }
        System.out.println("Creating Graph");
        m.createGraph(values, graphs, false);
        m.createResultFile(0, null, 0, args.length);
        int numEdges = m.createGraphFile(INPUT, graphs);
        m.createConfigFile();
        graphs = null;
        patternBank = null;
        String arguments[] = {CONFIG, COUNTERS, OUTPUT, INPUT};
        System.out.println(values);
        System.out.println("Starting AFGMiner");
        long startTime = System.currentTimeMillis();
        ReturnInfo info = mining.manager.MinerManager.main(arguments);
        long endTime = System.currentTimeMillis();
        System.out.println("Compiling Results");
        m.createResultFile(endTime-startTime, info, numEdges, args.length);
    }


    public Vector<EFG> createPatterns(String filename){
        File file = new File(filename);
        Vector<EFG> patterns = new Vector<EFG>();
        /*if (file.exists()){
            System.out.println("Reading Patterns");
            try {
                Node node;
                int numNodes;
                int numAttr;
                int numEdges;
                int weight;
                String[] attr;
                String[] edge;
                BufferedReader br = new BufferedReader(new FileReader(filename));
                int numEFG = Integer.parseInt(br.readLine().trim());
                for (int i = 0; i < numEFG; i++){
                    numNodes = Integer.parseInt(br.readLine().trim());
                    EFG efg = new EFG(numNodes);
                    for (int j = 0; j < numNodes; j++) {
                        br.readLine();
                        weight = Integer.parseInt(br.readLine().trim());
                        node = new Node(weight, Integer.parseInt(values.get(8)), numNodes);
                        numAttr = Integer.parseInt(br.readLine().trim());
                        for (int k = 0; k < numAttr; k++) {
                            attr = br.readLine().split(" ");
                            node.addAttribute(Integer.parseInt(attr[0]), Double.parseDouble(attr[1]));
                        }
                        efg.addNode(j, node);
                    }
                    numEdges = Integer.parseInt(br.readLine().trim());
                    for (int l = 0; l < numEdges; l++){
                        edge = br.readLine().split(" ");
                        efg.getNodes().get(Integer.parseInt(edge[0])).addEdge(Integer.parseInt(edge[1]), Double.parseDouble(edge[2]));
                    }
                    patterns.add(efg);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return patterns;
        }*/
        System.out.println("Creating Patterns");
        Random rd = new Random();
        Vector<String> patternValues = new Vector<String>();
        for (String s: values)
            patternValues.add(s);
        int size = patternValues.size();
        int numEFG = Integer.parseInt(patternValues.get(size-3));
        patternValues.set(6,  numEFG + "");
        int numNodes = Integer.parseInt(patternValues.get(size - 2));
        if (numNodes < numEFG)
            numNodes = numEFG;
        patternValues.set(7, numNodes +"");
        createGraph(patternValues, patterns, true);
        createGraphFile(filename, patterns);
        position = 12;
        return patterns;
    }

    /**
     * Creates the Graph using the user defined values.
     */
    public void createGraph(Vector<String> params, Vector<EFG> data, boolean isPattern) {
        int nodePosition;
        int numEFG = Integer.parseInt(params.get(6));
        int numAttr = Integer.parseInt(params.get(8));
        double attrProb = Double.parseDouble(params.get(9));
        double edgeProb = Double.parseDouble(params.get(10));
        addEFG(numEFG, data, params, isPattern); // Create EFGs with number of nodes,
        nodePosition = position;
        int count = 0;
        for (EFG e: data){
            position = nodePosition;
            int size = e.getSize();
            int index = 0;
            for (int i = 0; i < size; i++){
                if (!isPattern && patternBank != null && i < size-1 && i > 0){
                    if (getBernoulli(Double.parseDouble(values.get(values.size() - 1)))){
                        int tempPosition = position;
                        int offset = 5;
                        int valuesSize = values.size();
                        while (true){
                            String value = values.get(valuesSize-offset);
                            if (value.equals("G") || value.equals("g") || value.equals("u") || value.equals("U") || value.equals("E") || value.equals("e")){
                                position = valuesSize-offset;
                                break;
                            }
                            offset++;
                        }
                        double patternSelect = patternSelection();
                        double interval = 1.0/patternBank.size();
                        double sum = 0;
                        int patternIndex = -1;
                        while (sum < patternSelect){
                            patternIndex++;
                            sum+=interval;
                        }
                        EFG pat = patternBank.get(patternIndex);
                        position = tempPosition;
                        LinkedHashMap<Integer, Node> pattern = pat.getNodes();
                        int patSize = pat.getSize();
                        values.set(7, Integer.parseInt(values.get(7))+patSize-1+"");
                        e.setSize(e.getSize()+patSize - 1);
                        for (int j = 0; j < patSize; j++){
                            Node node = pattern.get(j);
                            Node newNode = (Node) node.deepClone(node);
                            e.addNode(index, newNode);
                            index++;
                        }
                    } else {
                        int weight = (int)getDistributedWeight();
                        Node node = new Node(weight + 1, numAttr, size);
                        addAttributes(node, attrProb, numAttr);
                        e.addNode(index, node);
                        index++;
                        if (i < size-1)
                            position = nodePosition;
                    }
                } else {
                    int weight = (int) getDistributedWeight();
                    Node node = new Node(weight + 1, numAttr, size);
                    addAttributes(node, attrProb, numAttr);
                    e.addNode(index, node);
                    index++;
                    if (i < size - 1)
                        position = nodePosition;
                }
            }
            /*size = e.getSize();
            for (int i = 0; i < size; i++){
                BitSet newBitSet = new BitSet(size);
                e.getNodes().get(i).setEdges(newBitSet);
            }*/
            addEdges(e, edgeProb);
        }
    }

    /**
     * Adds all EFGs to the graphs Vector
     * @param numEFG The number of EFGs to add
     */
    public void addEFG(int numEFG, Vector<EFG> data, Vector<String> params, boolean isPattern){
        int[] sizes = getEFGSizes(numEFG, params, isPattern);
        for (int i = 0; i < numEFG; i++){
            int efgSize = sizes[i];
            if (efgSize != 1)
                efgSize+=2;
            EFG efg = new EFG(efgSize);
            data.add(efg);
        }
    }

    /**
     * Calculates the size in number of nodes in each EFG
     * @param numEFG the number of EFGs in the dataset
     * @return int[] that contains an array of integers where each element corresponds
     * to the number of nodes in that EFG
     */
    public int[] getEFGSizes(int numEFG, Vector<String> params, boolean isPattern){
        int numNodes = Integer.parseInt(params.get(7));
        String dist = params.get(11).toUpperCase();;
        String tempdist = "";
        if (isPattern) {
            tempdist = dist;
            dist = "U";
        }
        int sum = 0;
        int index;
        int[] sizes = new int[numEFG];
        //Gaussian
        if (dist.equals("G")) {
            for (int i = 0; i < numEFG; i++) {
                sizes[i] = (int) getGaussianWeight(Double.parseDouble(params.get(12)), numNodes / numEFG, Integer.parseInt(params.get(14)));
                if (sizes[i] <= 0)
                    sizes[i] = 1;
                sum += sizes[i];
            }
            index = 0;
            while (sum != numNodes) {
                if (sum < numNodes) {
                    sizes[numEFG - index - 1]++;
                    sum++;
                } else {
                    if (sizes[index] != 1) {
                        sizes[index]--;
                        sum--;
                    }
                }
                if (index == numEFG - 1)
                    index = 0;
                else
                    index++;
            }
            position += 3;
        }
            //Exponential
        else if (dist.equals("E")) {
            for (int i = 0; i < numEFG; i++) {
                double rate = Double.parseDouble(params.get(12));
                double num = getInverseExponentialCDF(rate, 0.99);
                num /= (double) numEFG;
                num *= (double) (i + 1);
                sizes[i] = (int) (numNodes * getExponentialNode(rate, num));
                if (sizes[i] == 0)
                    sizes[i] = 1;
                sum += sizes[i];
            }
            index = 0;
            while (sum != numNodes) {
                if (sum < numNodes) {
                    sizes[numEFG - index - 1]++;
                    sum++;
                } else {
                    if (sizes[index] != 1) {
                        sizes[index]--;
                        sum--;
                    }
                }
                if (index == numEFG - 1)
                    index = 0;
                else
                    index++;
            }
            position++;
        }
            //Uniform
        else {
            for (int i = 0; i < numEFG; i++) {
                if (numNodes % numEFG < (i + 1))
                    sizes[i] = numNodes / numEFG;
                else
                    sizes[i] = numNodes / numEFG + 1;
            }
            position += 2;
        }
        if (tempdist.equals("G"))
            position++;
        else if (tempdist.equals("E"))
            position--;
        return sizes;
    }

    /**
     * Adds attributes to a node based on an inputed probability via a Bernoulli test
     * @param node The node the attributes are added to
     * @param attrProb The probability used for the bernoulli test
     * @param numAttr The maximum number of attributes possible
     */
    public void addAttributes(Node node, double attrProb, int numAttr){
        int tempPosition = position;
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
        //Ensure every node has at least 1 attribute
        if (node.getAttributes().isEmpty()){
            position = tempPosition;
            Random rd = new Random();
            node.addAttribute(rd.nextInt(numAttr), getDistributedWeight());
        }
    }

    /**
     * Adds edges too a given EFG. 2 Bernoulli tests are done between each set of 2 nodes
     * within an EFG (one forward, one backward).
     * @param efg The EFG where edges are added
     * @param edgeProb The probability used for the bernoulli test
     */
    public void addEdges(EFG efg, double edgeProb){
        boolean limit = false;
        int edgePosition = position;
        boolean insertEdge;
        boolean validSink = false;
        double weight;
        int numNodes = efg.getSize();
        BitSet sourceCheck = new BitSet(numNodes);
        Node source = efg.getNodes().get(0);
        for (int i = 1; i < numNodes-1; i++) {
            Node node = efg.getNodes().get(i);
            int count = 0;
            for (int j = i; j < numNodes - 1; j++) {
                insertEdge = getBernoulli(edgeProb);
                if (insertEdge && (count < 2 || !limit)) {
                    count++;
                    weight = getDistributedWeight();
                    node.addEdge(j, weight);
                    if (i!=j)
                        sourceCheck.set(j);
                }
                position = edgePosition;
                if (i != j) {
                    insertEdge = getBernoulli(edgeProb);
                    if (insertEdge && (count < 2 || !limit)) {
                            count++;
                            weight = getDistributedWeight();
                            Node reverseNode = efg.getNodes().get(j);
                            reverseNode.addEdge(i, weight);
                            sourceCheck.set(i);
                        }
                    }
                    position = edgePosition;
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
            position = edgePosition;
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
            efg.getNodes().get(numNodes-2).addEdge(numNodes-1, weight);
        }

    }

    /**
     * Gets a weight randomly determined on a given distribution.
     * @return The weight as a double
     */
    public double getDistributedWeight(){
        double weight = 0;
        String dist = values.get(position).toUpperCase();
            //Uniform
            if (dist.equals("U")) {
                int min = Integer.parseInt(values.get(position + 1));
                int max = Integer.parseInt(values.get(position + 2));
                weight = getUniformWeight(min, max);
                position += 3;
            }
            //Exponential
            else if (dist.equals("E")) {
                double rate = Double.parseDouble(values.get(position + 1));
                weight = getExponentialWeight(rate);
                position += 2;
            }
            //Gaussian
            else if (dist.equals("G")) {
                double height = Double.parseDouble(values.get(position + 1));
                int center = Integer.parseInt(values.get(position + 2));
                int width = Integer.parseInt(values.get(position + 3));
                weight = getGaussianWeight(height, center, width);
                position += 4;
            }
            //Poisson
            else {
                double mean = Double.parseDouble(values.get(position + 1));
                weight = getPoissonNumber(mean);
                position += 2;
            }
        return weight;
    }

    /**
     * Performs a bernoulli test with a given probability
     * @param prob A double between 0 and 1 to use for the bernoulli test
     * @return The result of the bernoulli test as a boolean
     */
    public boolean getBernoulli (double prob){
        Random rnd = new Random();
        if (rnd.nextDouble() < prob)
            return true;
        return false;
    }

    /**
     * Parses a file and puts the parameters into a String vector
     * @param filename The name of the file to be parsed
     * @return String vector with parameters
     */
    public Vector<String> manualParse(String filename){
            Vector<String> values = new Vector<String>();
            try {
                BufferedReader stream = new BufferedReader(new FileReader(filename));
                String arguments = "";
                String input;
                while ((input = stream.readLine()) != null) {
                    input = input + " ";
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

    public double patternSelection(){
        String dist = values.get(position).toUpperCase();
        Random rd = new Random();
        if (dist == "G"){
            double height = Double.parseDouble(values.get(position+1));
            int centre = Integer.parseInt(values.get(position+2));
            int width = Integer.parseInt(values.get(position+3));
            double root = Math.sqrt(Math.abs(2*Math.pow(width,2)*Math.log(.01/height)));
            int max = (int)(root+centre);
            int min = (int)(-1*root + centre);
            int num = rd.nextInt(max-min)+min;
            return height*Math.exp(-1*Math.pow(num-centre, 2)/(2*width*width));
        } else if (dist == "E") {
            double rate = Double.parseDouble(values.get(position+1));
            int max = (int)(-1*Math.log(.01/rate)/rate);
            int min = (int)(-1*Math.log(1/rate)/rate);
            int num = rd.nextInt(max-min)+min;
            return rate*Math.exp(-1*num*rate);
        } else {
            return rd.nextDouble();
        }
    }
    /**
     * Returns random number on uniform distribution
     * @param min The minimum number
     * @param max The maximum number
     * @return Random number on Uniform distribution
     */
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


    public double getGaussianWeight (double height, int center, int width) {
        Random rnd = new Random();
        double number = rnd.nextDouble();
        double negative = rnd.nextDouble();
        if (number >= height)
            height = number + .1;
        if (negative < 0.5) {
            double value = -Math.sqrt(-2 * Math.pow(width, 2) * Math.log(number / height)) + center;
            if (value > 0)
                return value;
        }
        return Math.sqrt(-2 * Math.pow(width, 2) * Math.log(number / height)) + center;
    }

    /**
     * Creates the graph file to be inputted into AFGMiner
     * @return the total Edges in the dataset
     */
	public int createGraphFile(String file, Vector<EFG> data){
		int size = data.size();
        int totalEdges = 0;
		//http://stackoverflow.com/questions/2885173/java-how-to-create-a-file-and-write-to-a-file 25/05/2015 for PrintWriter lines
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(size+"\n");
			// For each EFG
			for (EFG currentEFG: data){
                int numEdges = 0;
                String edgeString = "";
                LinkedHashMap<Integer, Node> currentNodes = currentEFG.getNodes();
				int numNodes = currentNodes.size();
				writer.write(numNodes + "\n");
				
				// For each node in EFG for node information
				for (int j = 0; j < numNodes; j++){
					writer.write(j +"\n");
					Node currentNode = currentNodes.get(j);
					writer.write(currentNode.getWeight()+"\n");
					BitSet attributes = currentNode.getAttributes();
					int numAttr = attributes.cardinality();
					writer.write(numAttr+"\n");
					LinkedHashMap<Integer, Double> currentAttributes = currentNode.getAttrWeight();
					
					// For each attribute in Node
					int attrIndex = attributes.nextSetBit(0);
					while (attrIndex != -1){
						double attrWeight = currentAttributes.get(attrIndex);
						writer.write(attrIndex + " " + attrWeight+"\n");
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
                totalEdges+=numEdges;
				writer.write(numEdges+"\n");
                if (numEdges > 0)
				    writer.write(edgeString+"\n");
            }
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return totalEdges;
	}

    /**
     * Creates the configuration file for AFGMiner
     */
    public void createConfigFile(){
        try {
            PrintWriter writer = new PrintWriter(CONFIG, "UTF-8");
            String header = "localhost" + '\n' + "EFG_ZSys" + '\n' + 50000 + '\n' + "username" + '\n' +
                    "DT120524" + '\n' + 0 + '\n' + values.get(0) + '\n' + 0 + '\n' + values.get(1) + '\n' +
                    values.get(2) + '\n' + values.get(3) + '\n' + 0 + '\n' + 0 + '\n' + values.get(4) +
                    '\n' + values.get(5);
            writer.println(header);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the result file to display the results obtained from AFGMiner including Machine characteristics.
     * @param time Time taken to run AFGMiner
     * @param info All info from AFGMiner
     * @param numEdges Number of Edges in Dataset
     */
    public void createResultFile(long time, ReturnInfo info, int numEdges, int length){
        try {
            BufferedWriter writer;
            String processor;
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("sysctl -n machdep.cpu.brand_string");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            processor = br.readLine();
            if (info != null) {
                writer = new BufferedWriter(new FileWriter(RESULT));
                //System.getProperties().list(System.out);
                writer.write("--------MACHINE INFORMATION--------\n\n");
                writer.write("Processor: " + processor);
                writer.write("\nJava.vm.version: " + System.getProperties().getProperty("java.vm.version"));
                writer.write("\nJava.runtime.version: " + System.getProperties().getProperty("java.runtime.version"));
                writer.write("\nJava.class.version: " + System.getProperties().getProperty("java.class.version"));
                writer.write("\nSun.management.compiler: " + System.getProperties().getProperty("sun.management.compiler"));
                writer.write("\nJava.vm.specification.version: " + System.getProperties().getProperty("java.vm.specification.version"));
                writer.write("\nOS Name: " + System.getProperties().getProperty("os.name"));
                writer.write("\nOS Version: " + System.getProperties().getProperty("os.ver" +
                        "sion"));
                writer.write("\nOS Arch: " + System.getProperties().getProperty("os.arch"));
                writer.write("\nAvailable Processors: " + Runtime.getRuntime().availableProcessors());
                writer.write("\n\n--------TEST RESULTS--------\n");
                writer.write("\nTotal Time: " + time + "ms");
                writer.write("\nTotal Edges: " + numEdges);
                writer.write("\nTotal Subgraphs Tested: " + info.getCount());
                writer.write("\nNumber of Hot Subgraphs: " + info.getNumHotSubgraphs());
                LinkedHashMap<Integer, Integer> patternsPerEdge = info.getNumPatternsPerNumEdges();
                int size = patternsPerEdge.size();
                for (int i = 0; i < size; i++) {
                    int numGraphs = patternsPerEdge.get(i);
                    writer.write("\nNumber of Hot " + i + "-Edge Subgraphs: " + numGraphs);
                }
                writer.close();
            }
            String header = "";
            String data = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()) + ",";
            File f = new File(CSV);
            if (f.isFile())
                writer = new BufferedWriter(new FileWriter(CSV, true));
            else {
                writer = new BufferedWriter(new FileWriter(CSV));
                writer.write("--------MACHINE INFORMATION--------\n\n");
                writer.write("Processor: " + processor);
                writer.write("\nJava.vm.version: " + System.getProperties().getProperty("java.vm.version"));
                writer.write("\nJava.runtime.version: " + System.getProperties().getProperty("java.runtime.version"));
                writer.write("\nJava.class.version: " + System.getProperties().getProperty("java.class.version"));
                writer.write("\nSun.management.compiler: " + System.getProperties().getProperty("sun.management.compiler"));
                writer.write("\nJava.vm.specification.version: " + System.getProperties().getProperty("java.vm.specification.version"));
                writer.write("\nOS Name: " + System.getProperties().getProperty("os.name"));
                writer.write("\nOS Version: " + System.getProperties().getProperty("os.version"));
                writer.write("\nOS Arch: " + System.getProperties().getProperty("os.arch"));
                writer.write("\nAvailable Processors: " + Runtime.getRuntime().availableProcessors());

                writer.write("\n\n--------TEST RESULTS--------\n");
                header = " DATE ,MAXATTR, MAXFOWARD, MAXBACKWARD, GAP, MINSUPPORT, MAXNODES, EFGS, NODES, NUMATTR, ATTRBERN," +
                        " EDGEBERN, NODES/EFG, P1, P2, P3, EDGE WEIGHT, P1, P2, P3, NODE WEIGHT, P1, P2, P3, " +
                        "ATTR WEIGHT, P1, P2, P3, PATNODE/EFG, P1, P2, P3, PATEFG, PATNODE, PATPROB, TIME(ms), NUMEDGES, TOTALSUBGRAPHS," +
                        " HOTSUBGRAPHS, 0-EDGE, 1-EDGE, 2-EDGE," +
                        " 3-EDGE, 4-EDGE, 5-EDGE\n" ;
                writer.write(header);
            }
            int numValues = values.size();
            int count = 0;
            for (int i = 0; i < numValues; i++) {
                String current = values.get(i).toUpperCase();
                if (count == 4 && length == 1)
                    data += " -, -, -, -,";
                if (current.equals("E")) {
                    data += current + "," + values.get(i + 1) + ", -, -,";
                    i++;
                    count++;
                }
                else if (current.equals("U")) {
                    data += current + "," + values.get(i + 1) + "," + values.get(i + 2) + ", -,";
                    i += 2;
                    count++;
                }
                else if (current.equals("G")) {
                    data += values.get(i) + ",";
                    count++;
                }
                else
                    data += values.get(i) + ",";
            }
            if (info == null)
                data += "ERROR";
            else {
                data += time + "," + numEdges + "," + info.getCount() + "," + info.getNumHotSubgraphs().toString();
                LinkedHashMap<Integer, Integer> patternsPerEdge = info.getNumPatternsPerNumEdges();
                int size = patternsPerEdge.size();
                for (int i = 0; i < size; i++) {
                    int numGraphs = patternsPerEdge.get(i);
                    data += "," + numGraphs;
                }
            }
            writer.write(data+'\n');
            writer.close();

            System.out.println();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
