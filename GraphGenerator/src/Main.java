import java.io.*;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Vector;

import com.sun.tools.hat.internal.util.VectorSorter;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.*;

import data.EFG;
import data.Node;

public class Main {
	final String FILENAME = "input.txt";
	static Vector<EFG> graphs;
	public static void main (String args[]) {
        Vector<String> values = manualParse(args[0]);
        for (int i = 0; i < Integer.parseInt(values.get(0)); i++) {
            EFG efg = new EFG();
            graphs.add(efg);
        }

	}




    public static Vector<String> manualParse(String filename) {
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
