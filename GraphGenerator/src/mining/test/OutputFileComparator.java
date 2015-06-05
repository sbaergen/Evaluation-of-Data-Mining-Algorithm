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
 */package mining.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

public class OutputFileComparator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Vector<Integer> patternsPerGen = new Vector<Integer>();
		int[] totalPatternsFlowGSP  = new int[1];
		List<String> patternsFlowGSP = readFlowGSPFile(args[0], patternsPerGen, totalPatternsFlowGSP);
		Vector<Integer> patternsPerNumEdges = new Vector<Integer>();
		Vector<Integer> patternsPerNumNodes = new Vector<Integer>();
		HashMap<Integer, Integer> patternsPerNumAttributes = new HashMap<Integer, Integer>();
		int[] totalPatternsFlowGSpan = new int[1];
		HashMap<Integer, Vector<String>> patternsFlowGSpan = readFlowGSpanFile(args[1], patternsPerNumEdges, 
						patternsPerNumNodes, patternsPerNumAttributes, totalPatternsFlowGSpan);
		
		double numMatches = calcCoverage(patternsFlowGSP, patternsFlowGSpan, patternsPerGen, patternsPerNumAttributes);
	
		double coverage = numMatches/totalPatternsFlowGSP[0];
		double invCoverage = numMatches/totalPatternsFlowGSpan[0];
		
		System.out.println("[FlowGSP Statistics]:\n");
		System.out.println("Number of Generations: " + patternsPerGen.size() + "\nNumber of Patterns per Pattern Size:\n");
		for(int i = 0; i < patternsPerGen.size(); ++i) {
			System.out.println("\t\t"+"("+(i+1)+")"+" " + patternsPerGen.get(i) +"\n");
		}
		System.out.println("Total Number of Patterns: " + totalPatternsFlowGSP[0]);
		
		System.out.println("\n\n[FlowGSpan Statistics]:\n");
		System.out.println("Number of Generations: " + patternsPerNumAttributes.keySet().size() + "\nNumber of Patterns per Pattern Size:\n");
		for(Integer attr : patternsPerNumAttributes.keySet()) {
			System.out.println("\t\t"+"("+attr+")"+" " + patternsPerNumAttributes.get(attr) +"\n");
		}
		System.out.println("Max Number of Edges : " + patternsPerNumEdges.size() + "\nNumber of Patterns per Added Edge:\n");
		for(int i = 0; i < patternsPerNumEdges.size(); ++i) {
			System.out.println("\t\t"+"("+(i)+")"+" " + patternsPerNumEdges.get(i) +"\n");
		}
		System.out.println("Max Number of Nodes: " + patternsPerNumNodes.size() + "\nNumber of Patterns per Added Node:\n");
		for(int i = 0; i < patternsPerNumNodes.size(); ++i) {
			System.out.println("\t\t"+"("+(i+1)+")"+" " + patternsPerNumNodes.get(i) +"\n");
		}
		System.out.println("Total Number of Patterns: " + totalPatternsFlowGSpan[0]);
		
		System.out.println("\n\nCoverage: " + coverage + "\nInverse Coverage: " + invCoverage);
	}

	private static double calcCoverage(List<String> patternsFlowGSP,
			HashMap<Integer, Vector<String>> patternsFlowGSpan,
			Vector<Integer> patternsPerGen,
			HashMap<Integer, Integer> patternsPerPatternSize) {
		
		List<String> convertedPatterns = convertToFlowGSpanFormat(patternsFlowGSP);
		return match(convertedPatterns, patternsFlowGSpan);
	}

	private static List<String> convertToFlowGSpanFormat(
			List<String> patternsFlowGSP) {
		List<String> convertedList = new Vector<String>();
		
		for(String str : patternsFlowGSP) {
			String attribute = "";
			String output = "";
			boolean newEdge = true;
			boolean newAttribute = false;
			int numVertex = 0;
			int vertexId = 0;
			
			for(int i = 0; i < str.length(); ++i) { 
				char c = str.charAt(i);
				
				if(c == '[') {
					if(newEdge == true) {
						output += "( ";
					}
					output += "[ " + vertexId + ": ";
					attribute = "(";
					++numVertex;
					newAttribute = true;
					newEdge = false;
				}
				else if(c == ',') {
					attribute += ")";
					output += attribute;
					attribute = "(";
					newAttribute = true;
					newEdge = false;
				}
				else if(c == ']') {
					if(numVertex%2 == 0) {
						attribute += ")";
						output += attribute+" ] )";
						newEdge = true;
					}
					else {
						attribute += ")";
						output += attribute +" ]";
						if(str.charAt(i+1) == '>') {
							output += " )";
						}
						else {
							output += ", ";
						}
						++vertexId;
						newEdge = false;
					}
				}
				else if(newAttribute == true && c != ' ' && c != '<' && c != '>') {
					attribute += c;
				}
			}
			convertedList.add(output);
		}
		System.out.println("\n\n\n" + convertedList.toString() + "\n\n");
		return convertedList;
	}

	private static int match(List<String> flowgsp, HashMap<Integer, Vector<String>> flowgspan) {
		int numMatches = 0;
		for(int i = 0; i < flowgsp.size(); ++i) {
			boolean matched = false;
			String strGSP = flowgsp.get(i);
			for(Integer key : flowgspan.keySet()) {
				for(String strGSpan : flowgspan.get(key)) {
					if(strGSP.equalsIgnoreCase(strGSpan)) {
						//System.out.println("FGSP: " + strGSP);
						//System.out.println("FGSpan: " + strGSpan);
						//System.out.println("MATCH");
						matched = true;
						++numMatches;
					}
				}
			}
			if(matched == false) {
				System.out.println("NOT MATCHED = " + strGSP);
			}
		}
		return numMatches;
	}

	private static HashMap<Integer, Vector<String>> readFlowGSpanFile(String fileName,
			Vector<Integer> patternsPerNumEdges,
			Vector<Integer> patternsPerNumNodes,
			HashMap<Integer, Integer> patternsPerNumAttributes, int[] totalPatterns) {
		/*
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date today = new Date();
		ProgramManager.writeOutputToFile("Results for Data Mining on " + dateFormat.format(today));
		ProgramManager.writeOutputToFile("\r\n\tSupport Threshold:\t\t" + minSupport);
		ProgramManager.writeOutputToFile("\r\n\tMax Number of Nodes (Wt):\t\t" + maxWt);
		ProgramManager.writeOutputToFile("\r\n\tMax Number of Attributes per Node:\t\t" + GSpan.MAX_ATTRIBUTES_PER_VERTEX);
		ProgramManager.writeOutputToFile("\r\n\tMax Number of Edge Additions:\t\t" + GSpan.MAX_NEW_EDGE_ADDITIONS);
		ProgramManager.writeOutputToFile("\r\n\tDataset Size (in number of graphs):\t\t" + datasetSize + "\n\n");

		 //outputs results according to edge size
		int maxGraphSize = 0;
		for(int i = 0; i < resultSet.size(); ++i) {
			maxGraphSize = Math.max(resultSet.get(i).getEdgeSet().size(), maxGraphSize);
		}
		
		for(int i = 0; i <= maxGraphSize; ++i) {
			ProgramManager.writeOutputToFile("\n\nGraphs of size (in number of edges) i: ");
			for(int j = 0; j < resultSet.size(); ++j) {
				Graph g = resultSet.get(j);
				if(g.getEdgeSet().size() == i) {
					ProgramManager.writeOutputToFile("\n\n" + g.toString() + sgMap.get(g.toCode()).toString());
				}
			}
		}
		 */
		HashMap<Integer, Vector<String>> patterns = new HashMap<Integer, Vector<String>>();
		
		try {
			BufferedReader reader = 
				new BufferedReader(new FileReader(new File(fileName)));
			int linesToJump = 0;
			String line = "";
			//Jumps header part.
			while(linesToJump < 2) {
				line = reader.readLine();
				++linesToJump;
			}
			
			//"Max Number of Nodes(...)
			line = reader.readLine();
			String[] lineParts;
			lineParts = line.split(":\t\t");
			int maxNodes = Integer.valueOf(lineParts[1]);
			++linesToJump;
			
			while(linesToJump < 8) {
				line = reader.readLine();
				++linesToJump;
			}
			
			//Sets number of patterns for each pattern size in terms of nodes as 0,
			//because we want to use each position as a counter.
			for(int i = 0; i < maxNodes; ++i) {
				patternsPerNumNodes.add(0);
			}
			
			int numEdges = 0;
			//"Graphs of size(...)", starting with 0 edges
			line = reader.readLine();
			
			while(line != null) {
				//Reached part with sub-graph to instruction mapping. So can stop reading.
				if(line.contains("==") == true) {
					break;
				}
				else {
					lineParts = line.split(": ");
				}
				
				int numPatterns = Integer.valueOf(lineParts[1]);
				totalPatterns[0] += numPatterns;
				
				patternsPerNumEdges.add(numPatterns);
				
				int patternLine = 0;
				while(patternLine < numPatterns) {
					int edgeLine = 0;
					
					if(numEdges == 0) {
						//"\n"
						line = reader.readLine();
						//"Subgraph <subgraph_ID>:"
						line = reader.readLine();
						
						//Start of the pattern.
						line = reader.readLine();
						
						patternsPerNumNodes.set(0, patternsPerNumNodes.get(0) + 1);
						
						if(patternsPerNumAttributes.get(1) == null) {
							patternsPerNumAttributes.put(1, 1);
						}
						else {
							patternsPerNumAttributes.put(1, patternsPerNumAttributes.get(1) + 1);
						}
						
						Vector<String> patternVec;
						if((patternVec = patterns.get(1)) == null) {
							patternVec = new Vector<String>();
						}
						patternVec.add(line);
						patterns.put(1, patternVec);
					}
					else {
						//"\n"
						line = reader.readLine();
						//"Subgraph <subgraph_ID>:"
						line = reader.readLine();
						
						String pattern = "";
						HashSet<Integer> distinctIds = new HashSet<Integer>();
						Vector<String> attributes = new Vector<String>();
						while(edgeLine < numEdges) {
							//To fill 'patterns' list
							line = reader.readLine();
							pattern = pattern.concat(line);
							++edgeLine;
							
							//To fill 'patternsPerNumNodes' list
							String[] nodeRelatedParts = line.split("\\[ ");
							String[] nodeRelatedParts2 = nodeRelatedParts[1].split(": ");
							String[] nodeRelatedParts3 = nodeRelatedParts[2].split(": ");
							
							int fromVertexId = Integer.valueOf(nodeRelatedParts2[0]);
							int toVertexId = Integer.valueOf(nodeRelatedParts3[0]);
							
							distinctIds.add(fromVertexId);
							distinctIds.add(toVertexId);
							
							//To fill 'patternsPerNumAttributes' list
							String attribute = "";
							boolean canAdd = false;
							
							for(int i = 0; i < line.length(); ++i) {
								if(line.charAt(i) == '(') {
									canAdd = true;
									attribute = "";
								}
								if(line.charAt(i) == ')') {
									if(canAdd == true) {
										attributes.add(attribute +")");
										canAdd = false;
									}
								}
								if(canAdd == true) {
									attribute += line.charAt(i);
								}
							}
						
						}
						
						//To fill 'patternsPerNumNodes' list, incrementing the number
						//of patterns that have this given number of nodes.
						int idx = distinctIds.size();
						patternsPerNumNodes.set(idx - 1, patternsPerNumNodes.get(idx-1) + 1);
						
						//To fill 'patternsPerNmAttributes' list, incrementing the number
						//of patterns that have this given number of attributes.
						int patternSize = attributes.size();
						
						if(patternsPerNumAttributes.get(patternSize) == null) {
							patternsPerNumAttributes.put(patternSize, 1);
						}
						else {
							patternsPerNumAttributes.put(patternSize, patternsPerNumAttributes.get(patternSize) + 1);
						}
						
						Vector<String> patternVec;
						if((patternVec = patterns.get(patternSize)) == null) {
							patternVec = new Vector<String>();
						}
						patternVec.add(pattern);
						patterns.put(patternSize, patternVec);
					}
					//Line with threshold values.
					line = reader.readLine();
					++patternLine;
				}
					
				++numEdges;
				//"\n"
				line = reader.readLine();
				//"Graphs of size(...)"
				line = reader.readLine();
			}
			
			reader.close();
		} catch (IOException e) {
			System.err.println("Unable to read FlowGSpan output file");
			System.exit(1);
		}
		
		return patterns;
	}

	/**
	 * Read data from the specified file
	 * 
	 * @param fileName The file to read from
	 * @return An array of each line of the file, unparsed
	 */
	private static List<String> readFlowGSPFile(String fileName, 
			Vector<Integer> patternsPerGen, int[] totalPatterns) {
		/*DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date today = new Date();
		writer.write("Results for Data Mining on " + dateFormat.format(today));
		writer.write("\r\n\tSupport Threshold:\t\t" + support);
		writer.write("\r\n\tMax Gap Size:\t\t" + gap);
		writer.write("\r\n\tSliding Window Size:\t\t" + window);
		writer.write("\r\n\r\n\tNumber of Generations:\t\t" + gens);
		
		writer.write("\r\n\r\nFrequent Sequences:\r\n");
		int totalSize = 0;
		for(int i = 0; i < data.length; i++) {
			List<Sequence> gen = data[i];
			totalSize += gen.size();
			writer.write("\r\nGeneration " + i + ": "+gen.size() + "elements\r\n");
			for(int j = 0; gen != null && j < gen.size(); j++) {
				Sequence current = gen.get(j);
				writer.write(current.toString(false) + " (" +
						current.getFreqSupport() + ") (" +
						current.getTimeSupport() + ")\r\n");
			}
		}
		
		writer.write("\nNumber of patterns found: "+totalSize);
		*/
		List<String> patterns = new ArrayList<String>();
		
		try {
			BufferedReader reader = 
				new BufferedReader(new FileReader(new File(fileName)));
			int linesToJump = 0;
			String line = "";
			while(linesToJump < 5) {
				line = reader.readLine();
				++linesToJump;
			}
			line = reader.readLine();
			String[] lineParts = line.split(":\t\t");
			int numGen = Integer.valueOf(lineParts[1]);
			++linesToJump;
			
			while(linesToJump < 9) {
				line = reader.readLine();
				++linesToJump;
			}
			
			int gen = 0;
			while(gen < numGen) {
				//line "Generation (...)"
				line = reader.readLine();
				lineParts = line.split(": ");
				
				//Number of generations actually produced was less than numGen.
				if(line.equalsIgnoreCase("") == true) {
					break;
				}
	
				int numPatterns = Integer.valueOf(lineParts[1]);
				totalPatterns[0] += numPatterns;
				
				patternsPerGen.add(numPatterns);
				linesToJump = 0;
				
				while(linesToJump < numPatterns) {
					line = reader.readLine();
					lineParts = line.split(" \\(");
					String pattern = lineParts[0];
					patterns.add(pattern);
					++linesToJump;
				}
				
				//"\n"
				line = reader.readLine();
				++gen;
			}
			
			reader.close();
		} catch (IOException e) {
			System.err.println("Unable to read FlowGSP output file");
			System.exit(1);
		}
		return patterns;
	}
}
