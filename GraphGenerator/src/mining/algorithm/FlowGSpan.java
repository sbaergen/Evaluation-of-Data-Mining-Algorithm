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
 */package mining.algorithm;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;
import java.util.Vector;

import mining.data.DataSet;
import mining.data.ExecutionFlowGraph;
import mining.data.PatternEdge;
import mining.data.PatternGraph;
import mining.data.PatternVertex;

/**
 * The FlowGSpan class represents the FlowGSpan algorithm itself, using
 * other classes (SubGraphMiner and MinerState) to do parts of the 
 * algorithmic work.
 * @author cgomes
 *
 */
public class FlowGSpan implements Runnable{	
	/**
	 * Dataset to be mined. A collection of Execution Flow Graphs (EFGs).
	 * Includes only those EFGs from profiled programs that are hotter than 
	 * MIN_METHOD_HOTNESS.
	 */
	DataSet graphDB; 
	
	/**
	 * Threshold to compare a pattern's support value against. If the 
	 * support value of a pattern is higher than minSupport, it is considered
	 * frequent.
	 */
	double minSupport; //threshold applied to tick count and edge frequency info
	
	/**
	 * Maximum number of nodes that a pattern can have.
	 */
	double maxNodes; 
	
	/**
	 * Frequency support (Sf) of pattern being currently analyzed.
	 */
	double currFreqSupport; 
	/**
	 * Weight support (Sw) of pattern being currently analyzed.
	 */
	double currWeightSupport;
	/**
	 * Number of nodes of pattern currently analyzed.
	 */
	double currNumNodes;
	/**
	 * Number of matched found of a pattern in the dataset.
	 */
	int numMatches;
	
	/**
	 * Total node weight, considering all EFGs in program (NOT dataset).
	 */
	double totalWeight;
	/**
	 * Total edge frequency considering all EFGs in program (NOT dataset).
	 */
	double totalFreq;
	
	/**
	 * Set of frequent patterns to be output; essentially sgMap.Keys().
	 */
	Vector<String> resultSet; 
	/**
	 * For each size of pattern (in number of edges), the number of patterns
	 * of that size.
	 */
	Vector<Integer> resultSizes;
	
	/**
	 * Map between pattern ID and instructions that generated it.
	 */
	LinkedHashMap<Integer, Vector<String>> instructionMap; 
	
	
	/**
	 * All attributes that actually exist in the dataset.
	 */
	Vector<Integer> existingAttrs; 
	
	/**
	 * Map between size of patterns (in number of edges) and all the distinct
	 * attributes contained at the patterns of that size found to be frequent.
	 */
	LinkedHashSet<PatternEdge> freqEdges; //FGSpan-edgecomb
	LinkedHashSet<Integer> freqAttrs;
	/**
	 * Holds how many attributes have already been inserted into the current pattern
	 * being analyzed. It is compared against MAX_ATTRIBUTES_TOTAL.
	 */
	int currAttrNum;
	
	Vector<PatternGraph> patternsToProcess;
	
	Vector<PatternGraph> childSet;
	
	LinkedHashSet<Integer> childFreqAttrs;
	LinkedHashSet<PatternEdge> childFreqEdges; //FGSpan-edgecomb

	int startIndex;
	
	int endIndex;
	
	Vector<Integer> attributesToLookFor;

	int count = 0;

	/**
	 * Constructor for running datasets loaded from a database.
	 * @param db Dataset (EFGs with hotness higher than MIN_METHOD_HOTNESS).
	 * @param loader Loader class, to load all EFGs of the profiled program (all coming from the database).
	 * @param minSup Minimum support value.
	 * @param maxNodes Maximum number of nodes allowed for patterns.
	 */
	public FlowGSpan(DataSet db, double datasetWeight, double datasetFreq, double minSup, 
			double maxNodes) {
		graphDB = db;
		minSupport = minSup;
		this.maxNodes = maxNodes;
		resultSet = null;
		currFreqSupport = 0f;
		currWeightSupport = 0f;
		currNumNodes = 0f;
		numMatches = 0;
		totalFreq = datasetFreq;
		totalWeight = datasetWeight;
		currAttrNum = 0;
		freqAttrs = new LinkedHashSet<Integer>();
		freqEdges = new LinkedHashSet<PatternEdge>();
	
		resultSet = new Vector<String>();
		resultSizes = new Vector<Integer>();
		instructionMap = new LinkedHashMap<Integer, Vector<String>>();
		
		childSet = new Vector<PatternGraph>();
		childFreqAttrs = new LinkedHashSet<Integer>();
		childFreqEdges = new LinkedHashSet<PatternEdge>(); //FGSpan-edgecomb
		startIndex = endIndex = 0;
		patternsToProcess = null;
		
		attributesToLookFor = new Vector<Integer>();
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("BR_PRED"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("BR_DIR"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("BR_NTCD"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("BR_NTWD"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("BR_TCD"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("BR_TWD"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("BR_TCDWT"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("RR0_DIRMS_SPNHT"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("ISR0_REJECT"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("ISR0_AGI"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("inline1"));
		
	}

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
	 * Constructor for datasets loaded from input text file.
	 * @param db Dataset loaded from text file.
	 * @param minSup Minimum support value.
	 * @param maxNodes Maximum number of nodes allowed for patterns.
	 */
	public FlowGSpan(DataSet db, double minSup, double maxNodes) {
		graphDB = db;
		this.minSupport = minSup;
		this.maxNodes = maxNodes;
		resultSet = null;
		currFreqSupport = 0f;
		currWeightSupport = 0f;
		currNumNodes = 0f;
		numMatches = 0;
		totalFreq = graphDB.getTotalFreq();
		totalWeight = graphDB.getTotalWeight();
		currAttrNum = 0;
		freqAttrs = new LinkedHashSet<Integer>();
		freqEdges = new LinkedHashSet<PatternEdge>();
		
		resultSet = new Vector<String>();
		resultSizes = new Vector<Integer>();
		instructionMap = new LinkedHashMap<Integer, Vector<String>>();
		
		childSet = new Vector<PatternGraph>();
		childFreqAttrs = new LinkedHashSet<Integer>();
		childFreqEdges = new LinkedHashSet<PatternEdge>(); //FGSpan-edgecomb
		startIndex = endIndex = 0;
		patternsToProcess = null;
		
		attributesToLookFor = new Vector<Integer>();
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("BR_PRED"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("BR_DIR"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("BR_NTCD"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("BR_NTWD"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("BR_TCD"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("BR_TWD"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("BR_TCDWT"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("RR0_DIRMS_SPNHT"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("ISR0_REJECT"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("ISR0_AGI"));
		//attributesToLookFor.add(FlowGSpanController.getAttributeIndex("inline1"));
	}
		
	/**
	 * Sets the list of attributes that can be found in the dataset.
	 * @param attrs Existing attributes in dataset.
	 */
	public void setExistingAttrs(Vector<Integer> attrs) {
		existingAttrs = attrs;
	}
	
	public void setStartIndex(int start) {
		startIndex = start;
	}
	
	public void setEndIndex(int end) {
		endIndex = end;
	}
	
	public void setPatternsToProcess(Vector<PatternGraph> patterns) {
		patternsToProcess = patterns;
	}
	
	public Vector<PatternGraph> getPatternsToProcess() {
		return patternsToProcess;
	}
	
	public void setFreqAttrs(LinkedHashSet<Integer> attrs) {
		freqAttrs = attrs;
	}
	
	public LinkedHashSet<Integer> getChildFreqAttrs() {
		return childFreqAttrs;
	}
	
	public LinkedHashSet<PatternEdge> getChildFreqEdges() {
		return childFreqEdges;
	}

	public LinkedHashMap<Integer, Vector<String>> getInstructionMap() {
		return instructionMap;
	}
	
	public Vector<String> getResultSet() {
		return resultSet;
	}
	
	public Vector<Integer> getResultSizes() {
		return resultSizes;
	}
	
	public Vector<PatternGraph> getChildSet() {
		return childSet;
	}
	
	/**
	 * Kick-starts FlowGSpan, by initializing output sets (sgMap, resultSet,
	 * resultSizes and instructionMap); mining for frequent 0-edge sub-graphs and
	 * then mining for sub-graphs of larger size.
	 */
	public void  run() {
		System.out.println("New Run");
		if(patternsToProcess == null) {
			//Mines for 0-edge frequent subgraphs (i.e. frequent nodes).
			findFrequentNodes(childSet, startIndex, endIndex);
			System.out.println("FOUND FREQUENT");
			for(PatternGraph graph: childSet) {
				System.out.println("CHILD");
				String keyStr = graph.getKeyString();

				//Insert patterns into output sets.	
				FlowGSpanController.NUMBER_SUBGRAPHS.incrementAndGet();
				
				resultSet.add(keyStr);
               // System.out.println(resultSet.size() + " 0-edge");
				resultSizes.add(0);
				
				int graphInstances = (int)FlowGSpanController.sgMap.get(keyStr).get(3).floatValue();
				graph.setNumInstances(graphInstances);
				
				instructionMap.put(FlowGSpanController.PATTERN_ID.getAndIncrement(), graph.getInstructionMappings());
				childFreqAttrs.addAll(graph.getAllDistinctAttributes());
			}
			
			//DEBUG
			//System.out.println("Existing attributes: " + GSpan.attributeTable + "\n\n" + GSpan.existingAttrs);
			//System.out.println(childSet.toString());
			//System.out.println("Num of graphs: " + childSet.size());
			//end DEBUG
		}
		else {
			//Starts mining of n-edge subgraphs, with n > 0.
			System.out.println("SUBGRAPH");
			mineSubGraph(patternsToProcess, startIndex, endIndex);
		}
	}

	/**
	 * Mines for frequent 0-edge sub-graphs (nodes).
	 * @return Set of frequent nodes.
	 */
	private void findFrequentNodes(Vector<PatternGraph> nodeSet, int start, int end) {
		Vector<String> attrToPermute = new Vector<String>();
		int count = 0;
		while(start <= end) {

			System.out.println(Runtime.getRuntime().freeMemory());
			int attr = existingAttrs.get(start);
			++start;
			
			//First node in pattern, so id = 0.
			PatternVertex v = new PatternVertex((double)0, 0); 
			v.setAttribute(attr, 0);
			PatternGraph newG = new PatternGraph();
			newG.insertVertex(v);
			newG.setEntryVertex(v);
			newG.setExitVertex(v);
			newG.setPatternType(0);
			
			currWeightSupport = 0;
			currFreqSupport = 0;
			currNumNodes = 0;
			numMatches = 0;
			
			for(int methodId : graphDB.keySet()) {
				ExecutionFlowGraph g = graphDB.get(methodId);
				graphMatch(newG, g, methodId, newG.getMinerState(methodId), true, 0, v.getId(), v.getId(), null);
			}
            count++;
			//System.out.println("COUNT");
			if(getMaxSupport(currWeightSupport/totalWeight, currFreqSupport/totalFreq) > minSupport) {
				//Register supports in graph map.
				Vector<Double> supports = new Vector<Double>();
				
				//if(currWeightSupport/totalWeight >= 1) {
				//	System.err.print("WEIGHT SUPPORT EXCEEDED LIMITS!!!\n");
				//}
				supports.add(currWeightSupport/totalWeight);
				supports.add(currFreqSupport/totalFreq);
				currNumNodes = newG.getVertexSet().size();
				supports.add(currNumNodes);
				supports.add((double)numMatches);
				supports.add((double)newG.getPatternType());
				
				graphDB.addUsefulGraphs(newG.getGS());
				
				FlowGSpanController.sgMap.put(newG.getKeyString(), supports);
				
				nodeSet.add(newG);
				attrToPermute.add(String.valueOf(attr));
				childFreqAttrs.add(attr);
			}
			//DEBUG
			//System.out.println("Attr " + FlowGSpanController.getAttributeName(attr) + " had " + this.numMatches + " matches\n");
			//end DEBUG
		}
		
		if(FlowGSpanController.MAX_ATTRIBUTES_TOTAL > 1) {
			int itemsetNum = 1;
		
			//While not complete...
			do
			{
				System.out.println(Runtime.getRuntime().freeMemory() + " DO");
				//Increase the itemset that is being looked at...
				//System.out.println(itemsetNum + "ITEMSETNUM");
				itemsetNum++;

				//Generate candidate patterns...
				genAttributesToPermute(itemsetNum, childFreqAttrs, attrToPermute);
				
				Vector<String> attrToRemove = new Vector<String>(); 
				//Determine and display frequent itemsets.
				System.out.println(attrToPermute.size());
				for(int i = 0; i < attrToPermute.size(); ++i) {
					PatternVertex v = new PatternVertex((double)0, 0);
					PatternGraph newG = new PatternGraph();
    					//System.out.println(i + "I");
					System.out.println(attrToPermute.get(i));
					for(int j = 0; j < attrToPermute.get(i).length();) {
						String substr;
						int whitespaceIdx = attrToPermute.get(i).indexOf(' ', j);
						//System.out.println(j + "J");						
						if(whitespaceIdx == -1) {
							int strSize = attrToPermute.get(i).length() - j;
							substr = attrToPermute.get(i).substring(j, j + strSize);
							j += strSize;
						}
						else {
							substr = attrToPermute.get(i).substring(j, whitespaceIdx);
							j = whitespaceIdx + 1;
						}
    				
						int val = Integer.valueOf(substr);
						v.setAttribute(val, 0f);
					}
    			
					newG.insertVertex(v);
					newG.setEntryVertex(v);
					newG.setExitVertex(v);
					newG.setPatternType(0);
					
					currWeightSupport = 0;
					currFreqSupport = 0;
					currNumNodes = 0;
					numMatches = 0;
					
					boolean shouldRemove = true;
    			
					for(int methodId : graphDB.keySet()) {
					//	System.out.println("METHODID 22222");
						ExecutionFlowGraph g = graphDB.get(methodId);
						graphMatch(newG, g, methodId, newG.getMinerState(methodId), true, 0, v.getId(), v.getId(), null);
					}
                    count++;
					//System.out.println("COUNT 22222");
					if(getMaxSupport(currWeightSupport/totalWeight, currFreqSupport/totalFreq) > minSupport) {
						//Register supports in graph map
						Vector<Double> supports = new Vector<Double>();

                        //DEBUG
						if(currWeightSupport/totalWeight >= 1) {
							//System.out.println("WEIGHT SUPPORT EXCEEDED LIMITS!!!\n");
						}
						//end DEBUG
						
						supports.add(currWeightSupport/totalWeight);
						supports.add(currFreqSupport/totalFreq);
						currNumNodes = newG.getVertexSet().size();
						supports.add(currNumNodes);
						supports.add((double)numMatches);
						supports.add((double)newG.getPatternType());
						
						graphDB.addUsefulGraphs(newG.getGS());
						
						FlowGSpanController.sgMap.put(newG.getKeyString(), supports);
						
						nodeSet.add(newG);
						shouldRemove = false;
					}
    		
					if(shouldRemove == true) {
						attrToRemove.add(attrToPermute.get(i));
					}
				}
				for(int i = 0; i < attrToRemove.size(); ++i) {
					//System.out.println("REMOVE");
					attrToPermute.remove(attrToRemove.get(i));
				}
				//If there are <=1 frequent items, then it is the end. This prevents reading through the 
				//database again, when there is only one frequent itemset.
			}while(attrToPermute.size() > 1 && itemsetNum < FlowGSpanController.MAX_ATTRIBUTES_TOTAL);
		}
	}

	/**
	 * Gets an initial queue of 0-edge sub-graphs known to be frequent, and
	 * proceeds to, until the queue is empty:
	 * 1. Get the pattern which is head of the queue;
	 * 2. Insert it into output sets;
	 * 3. Expand it if its number of attributes and number of nodes are under limits 
	 * 4. Insert its frequent child patterns into the queue.	 
	 * @param queueToProcess The queue that holds all patterns being analyzed.
	 */
	@SuppressWarnings("unchecked")
	private void mineSubGraph(Vector<PatternGraph> queueToProcess, int start, int end) {
		while(start<= end) {
			//System.out.println(Runtime.getRuntime().freeMemory());
			PatternGraph graph = queueToProcess.get(start);
	
			//DEBUG
			//String keyStr = graph.getKeyString();	
			//System.out.println("Graph: " + keyStr);
			//end DEBUG
			
			currAttrNum = graph.getAllAttributes().size();
			
			if(currAttrNum >= FlowGSpanController.MAX_ATTRIBUTES_TOTAL) {
				++start;
				continue;
			}
			
			Vector<PatternGraph> edgeOnlyChildren = new Vector<PatternGraph>();
			Vector<PatternGraph> children = new Vector<PatternGraph>();

			expandGraph(graph, children, edgeOnlyChildren);

			childSet.addAll(children);
			
			//For performance reasons, child patterns generated by back-edges
			//being added to the parent pattern are not expanded further, so they
			//are not inserted into the queue. Instead, they are simply inserted into
			//the output sets.
			for(PatternGraph child : edgeOnlyChildren) {

				String childKeyStr = child.getKeyString();	
				//another part of the output
				FlowGSpanController.NUMBER_SUBGRAPHS.incrementAndGet();
					
				resultSet.add(childKeyStr);


				resultSizes.add(child.getEdgeSet().size());
				
				int numInstances = (int)FlowGSpanController.sgMap.get(childKeyStr).get(3).floatValue();
				child.setNumInstances(numInstances);
				
				instructionMap.put(FlowGSpanController.PATTERN_ID.getAndIncrement(), child.getInstructionMappings());
			}
				
			for(PatternGraph child: children) {
				System.out.println("Children");
				String childKeyStr = child.getKeyString();
					
				//Insert patterns into output sets.	
				FlowGSpanController.NUMBER_SUBGRAPHS.incrementAndGet();
					
				resultSet.add(childKeyStr);
				int childGraphSize = child.getEdgeSet().size();
				resultSizes.add(childGraphSize);
				child.setNumInstances((int)FlowGSpanController.sgMap.get(childKeyStr).get(3).floatValue());
				instructionMap.put(FlowGSpanController.PATTERN_ID.getAndIncrement(), child.getInstructionMappings());
				
				childFreqEdges.addAll(child.getAllDistinctEdges()); //FGSpan-edgecomb
				childFreqAttrs.addAll(child.getAllDistinctAttributes());
			}
			++start;
		}
	}

	/**
	 * Calls the SubgraphMiner class to find the matches of subGraph in wholeGraph.
	 * @param subGraph Sub-graph to be mined for.
	 * @param wholeGraph Graph in which we hope to find subGraph matches.
	 * @param graphIdx ID of wholeGraph (its index in the set of dataset graphs)
	 * @param prevStates Areas in wholeGraph where the parent of subGraph has been previously found.
	 * @param saveState Whether to save the areas in wholeGraph where subGraph is found.
	 * @param typeOfAddition If 0, a new {edge, node} pair is being added; otherwise, a back-edge is being added.
	 * @param pivotVertexId Node from the parent pattern to which the new edge is to be connected from (from-node).
	 * @param targetVertexId If typeOfAddition is 0, the new node added to the pattern. If typeOfAddition is 1, the
	 * node in the parent pattern to which the new edge is to be connected to (to-node).
	 * @param oldNewIdCorrespondence Map between node IDs before and after addition of new edge.
	 */
	public void graphMatch(PatternGraph subGraph, ExecutionFlowGraph wholeGraph, int graphIdx, Vector<MinerState> prevStates, 
			boolean saveState, int typeOfAddition, int pivotVertexId, int targetVertexId, 
			LinkedHashMap<Integer, Integer> oldNewIdCorrespondence) {
		SubgraphMiner miner = new SubgraphMiner(subGraph, wholeGraph, saveState);
		Vector<MinerState> endStates = miner.run(prevStates, typeOfAddition, pivotVertexId, 
				targetVertexId, oldNewIdCorrespondence);

		int numMatches = miner.getNumMatches();
		if(numMatches > 0) {
			subGraph.addToGS(graphIdx);
			currFreqSupport += miner.getFreqSupport();
			currWeightSupport += miner.getWeightSupport();
			if(endStates.size() > 0) {
				subGraph.setMinerState(graphIdx, endStates);
			}
		}
		this.numMatches += numMatches;
	}
	
	/**
	 * Performs pattern growth process.
	 * @param graph Parent pattern to be grown.
	 * @param edgeOnlyChildren Children grown from parent pattern by the addition of a back-edge.
	 * @return Child patterns frown from parent pattern by the addition of a new {edge, node} pair.
	 */
	private void expandGraph(PatternGraph graph, Vector<PatternGraph> children, Vector<PatternGraph> edgeOnlyChildren) {
		int maxAdditions = 0;
		
		//Handles case where we only want to find sequential patterns.
		if(FlowGSpanController.LIMIT_FWD_EDGE_ADDITIONS == true && FlowGSpanController.MAX_NEW_FWD_EDGE_ADDITIONS == 1) {
			int pivotVertexId = graph.getExitVertex().getId();
			//FGSpan-edgecomb
			if(graph.getEdgeSet().size() > 1) {
				System.out.println("COMBINE EDGES");
				combineEdges(graph, pivotVertexId, freqEdges, children);
			}
			else {
				System.out.println("ATTACH NEW NODE");
				attachNewNode(graph, pivotVertexId, children);
			}
		}
		
		else {
			for(int pivotVertexId : graph.getVertexSet().keySet()) {
				System.out.println("PIVOT VERTEX");
				PatternVertex pivotVertex = graph.getVertex(pivotVertexId);
				if(pivotVertex.getForwardOutEdgeCount() < 2) {
					//FGSpan-edgecomb
					if(graph.getEdgeSet().size() > 1) {
					System.out.println("NON-SEQ COMBINE EDGES");
						combineEdges(graph, pivotVertexId, freqEdges, children);
					}
					else {
					System.out.println("NON-SEQ NEW NODE");
						attachNewNode(graph, pivotVertexId, children);
					}	
					if(FlowGSpanController.LIMIT_FWD_EDGE_ADDITIONS == true) {
						++maxAdditions;
						if(maxAdditions >= FlowGSpanController.MAX_NEW_FWD_EDGE_ADDITIONS) {
							break;
						}
					}
				}
			}
		}
		
		//Handles addition of back-edges.
		/*maxAdditions = 0;
		Vector<Pair<Integer, Integer>> pairsAttempted = new Vector<Pair<Integer, Integer>>();
		pairsAttempted.addAll(graph.getEdgeSet().keySet());
		for(int pivotVertexId : graph.getVertexSet().keySet()) {
			for(int targetVertexId : graph.getVertexSet().keySet()) {
				Pair<Integer, Integer> newPair = new Pair<Integer, Integer>(pivotVertexId, targetVertexId);
				boolean canAdd = true;
				
				for(Pair<Integer, Integer> p : pairsAttempted) {
					if(p.equals(newPair) == true) {
						canAdd = false;
						break;
					}
				}
				
				if(canAdd == false) {
					continue;
				}
				
				pairsAttempted.add(newPair);
				
				attachEdgeOnly(graph, pivotVertexId, targetVertexId, edgeOnlyChildren);
				
				if(FlowGSpanController.LIMIT_BACK_EDGE_ADDITIONS == true) {
					++maxAdditions;
					if(maxAdditions >= FlowGSpanController.MAX_NEW_BACK_EDGE_ADDITIONS) {
						break;
					}
				}
			}
		}*/
	}
	
	private void combineEdges(PatternGraph graph, int pivotVertexId, LinkedHashSet<PatternEdge> edgesToCombine, 
			Vector<PatternGraph> children) {
		PatternVertex pivot = graph.getVertex(pivotVertexId);
		int leftToGen = FlowGSpanController.MAX_ATTRIBUTES_TOTAL - currAttrNum;
		
		for(PatternEdge e : edgesToCombine) {
			int numAttrsToAdd = e.getToVertex().getAttributeCount();
			if(numAttrsToAdd <= leftToGen && pivot.equals(e.getFromVertex(), false, false) == true) {
				PatternGraph childGraph = graph.clone();
				PatternVertex childPivot = childGraph.getVertex(pivotVertexId);
				PatternVertex target = e.getToVertex();
				PatternVertex childTarget = new PatternVertex(0f, childGraph.getVertexSet().size());
				
				childTarget.setAttributes(target.getAttributes());
				childTarget.setAttrWeights(target.getAttrWeights());
				PatternEdge newE = new PatternEdge(childPivot, childTarget, 0f);
				childGraph.insertEdge(newE);
				childGraph.findEntryExitVertices();
				
				boolean isFrequent = enumerate(graph, childGraph, true, 0, pivotVertexId, childTarget.getId());
				if(isFrequent == true) {
					children.add(childGraph);
				}
			}
		}
	}

	/**
	 * Enumerates (finds) all matches of a child pattern in the dataset.
	 * @param parent The parent pattern, that generated the child pattern to be mined for.
	 * @param child The pattern to be mined for.
	 * @param saveState Whether to save the areas where the child pattern is found.
	 * @param typeOfAddition If 0, a new {edge, node} pair is being added; otherwise, a back-edge is being added.
	 * @param pivotVertexId Node from the parent pattern to which the new edge is to be connected from (from-node).
	 * @param targetVertexId If typeOfAddition is 0, the new node added to the pattern. If typeOfAddition is 1, the
	 * node in the parent pattern to which the new edge is to be connected to (to-node).
	 * @return True if child pattern is found to be frequent; false otherwise (i.e. false if pattern has already been
	 * mined previously, or false if it is the first time we have mined it, but it is not frequent.)
	 */
	private boolean enumerate(PatternGraph parent, PatternGraph child, boolean saveState, int typeOfAddition, 
			int pivotVertexId, int targetVertexId) {
		LinkedHashMap<Integer, Integer> oldNewIdCorrespondence = new LinkedHashMap<Integer, Integer>();
		child.lexSort(oldNewIdCorrespondence);
		pivotVertexId = oldNewIdCorrespondence.get(pivotVertexId);
		targetVertexId = oldNewIdCorrespondence.get(targetVertexId);
		
		String keyStr = child.getKeyString();
		
		if(FlowGSpanController.sgMap.containsKey(keyStr)) {
			return false;
		}
	
		currWeightSupport = 0;
		currFreqSupport = 0;
		currNumNodes = 0;
		numMatches = 0;
		
		int sizeGS = parent.getGS().size();
		
		if(child.containsAnyAttributes(attributesToLookFor)) {
			child.setAllowedGap(FlowGSpanController.GAP);
		}
		else {
			child.setAllowedGap(0);
		}
		
		for(int i = 0; i < sizeGS; ++i) {
			int toBeMinedIdx = parent.getGS().get(i);
			ExecutionFlowGraph toBeMined = graphDB.get(toBeMinedIdx);
			graphMatch(child, toBeMined, toBeMinedIdx, parent.getMinerState(toBeMinedIdx), saveState, 
					typeOfAddition, pivotVertexId, targetVertexId, oldNewIdCorrespondence);
		}
		
		boolean canIncludeInOutput = (getMaxSupport(currWeightSupport/totalWeight, currFreqSupport/totalFreq) > minSupport);
		count++;
		/*if(child.getPatternType() == 2) {
			if(currWeightSupport > 0 || currFreqSupport > 0) {
				canIncludeInOutput = true;
			}
		}*/
		if(canIncludeInOutput == true) {
			//if(currWeightSupport/totalWeight >= 1) {
			//	System.err.print("WEIGHT SUPPORT EXCEEDED LIMITS!!!\n");
			//}
		
			//Register supports in graph map
			Vector<Double> supports = new Vector<Double>();
			supports.add(currWeightSupport/totalWeight);
			supports.add(currFreqSupport/totalFreq);
			currNumNodes = child.getVertexSet().size();
			supports.add(currNumNodes);
			supports.add((double)numMatches);
			supports.add((double)child.getPatternType());
			
			graphDB.addUsefulGraphs(child.getGS());
			
			FlowGSpanController.sgMap.put(keyStr, supports);
			
			return true;
		}
		
		return false;
	}

	/**
	 * Attaches new {edge, node} pairs to a pattern, thus generating its child patterns.
	 * @param graph Pattern to which new {edge, node} pairs are added.
	 * @param vertexIdToAttachTo The pivot node's ID.
	 * @param children Where we store generated children that are frequent.
	 */
	@SuppressWarnings("unchecked")
	private void attachNewNode(PatternGraph graph, int vertexIdToAttachTo, Vector<PatternGraph> children) {
		Vector<String> attrToPermute = new Vector<String>();
		int itemsetNum = 0;
		int leftToGen = FlowGSpanController.MAX_ATTRIBUTES_TOTAL - currAttrNum;
		
		PatternVertex fromV = graph.getVertex(vertexIdToAttachTo);
		if(fromV.getChildren().size() > 0) {
        	LinkedHashSet<Integer> branchAttrs = (LinkedHashSet<Integer>) FlowGSpanController.getBranchAttrList().clone();
        	boolean hasBranchAttr = false;
        	
        	for(Integer att : branchAttrs) {
        		if(fromV.getAttribute(att) == true) {
        			hasBranchAttr = true;
        		}
            }
        	
        	if(hasBranchAttr == false) {
        		return;
        	}
		}
    	System.out.println("Entering DO WHILE");
        do
        {
            //Increase the itemset that is being looked at...
            itemsetNum++;
            
            //Generate candidate patterns...
	System.out.println("genAttributesToPermute");
            genAttributesToPermute(itemsetNum, freqAttrs, attrToPermute);
        System.out.println("calcFrequentSubGraphAttrs");
            //And determine and display frequent itemsets.
            calcFrequentSubGraphAttrs(graph, vertexIdToAttachTo, children, itemsetNum, attrToPermute);
        //If there are <=1 frequent items, then it is the end. 
        //This prevents reading through the database again, when there is only one frequent itemset.
	System.out.println(attrToPermute.size() + " " + itemsetNum + " " + leftToGen);
        }while(attrToPermute.size() > 1 && itemsetNum < leftToGen);
	}

	/**
	 * Adds possible back-edges, i.e. edges connecting two nodes that already exist in the parent pattern,
	 * in order to generate its child patterns.
	 * @param graph Pattern to which back-edges are to be added.
	 * @param pivotVertexId Node in the parent pattern from which the back-edge is to be connected (from-node).
	 * @param targetVertexId Node in the parent pattern to which the back-edge is to be connected (to-node).
	 * @param children Place to store generated child patterns that are frequent.
	 */
	private void attachEdgeOnly(PatternGraph graph, int pivotVertexId, int targetVertexId,
			Vector<PatternGraph> children) {
		boolean isFrequent = false;

		PatternGraph childGraph = graph.clone();
		PatternVertex toV = childGraph.getVertex(targetVertexId);
		PatternVertex fromV = childGraph.getVertex(pivotVertexId);
		PatternEdge newE = new PatternEdge(fromV, toV, 0f);
		childGraph.insertEdge(newE);
		childGraph.findEntryExitVertices();

		isFrequent = enumerate(graph, childGraph, false, 1, pivotVertexId, targetVertexId);

		if(isFrequent == true) {
			children.add(childGraph);
		}
	}
	
	/**
	 * Function that composes child sub-graphs by adding, to the parent sub-graph, a node that contains attributes
	 * indicated in attrToPermute. Each child generated contains a new node that has one of the attribute sets (itemsets)
	 * indicated in a position of attrToPermute.
	 * @param graph Parent pattern to be grown.
	 * @param vertexIdToAttachTo Pivot node of parent pattern, to which new nodes are to be attached to.
	 * @param children Place to store generated child patterns that are frequent.
	 * @param itemsetNum The size of the itemset (set of attributes) to be added in newly generated nodes.
	 * @param attrToPermute Set of itemsets to be associated with generated nodes.
	 */
	private void calcFrequentSubGraphAttrs(PatternGraph graph, int vertexIdToAttachTo, Vector<PatternGraph> children, 
			                               int itemsetNum, Vector<String> attrToPermute) {
		boolean isFrequent = false;
		Vector<String> attrToRemove = new Vector<String>();
		
		for(int i = 0; i < attrToPermute.size(); ++i) {
			PatternGraph childGraph = graph.clone();
			PatternVertex newV = new PatternVertex(0f, childGraph.getVertexSet().size());
			System.out.println(i + "I");
			for(int j = 0; j < attrToPermute.get(i).length();) {
				String substr;
				int whitespaceIdx = attrToPermute.get(i).indexOf(' ', j);
				System.out.println(j + "J");
				if(whitespaceIdx == -1) {
					int strSize = attrToPermute.get(i).length() - j;
					substr = attrToPermute.get(i).substring(j, j + strSize);
					j += strSize;
				}
				else {
					substr = attrToPermute.get(i).substring(j, whitespaceIdx);
					j = whitespaceIdx + 1;
				}
				
				int val = Integer.valueOf(substr);
				newV.setAttribute(val, 0f);
			}
			
			childGraph.insertVertex(newV);
			PatternVertex fromV = childGraph.getVertex(vertexIdToAttachTo);
			PatternEdge newE = new PatternEdge(fromV, newV, 0f);
			childGraph.insertEdge(newE);
			System.out.println("findEntryExitVertices");
			childGraph.findEntryExitVertices();
			
			if(fromV.getChildren().size() > 1) {
				childGraph.setPatternType(2);
			}
			else {
				childGraph.setPatternType(1);
			}
			
			//DEBUG
			//System.out.println(childGraph.getKeyString());
			//end DEBUG
			System.out.println("enumerate");
			isFrequent = enumerate(graph, childGraph, true, 0, vertexIdToAttachTo, newV.getId());
			
			if(isFrequent == true) {
				children.add(childGraph);
			}
			else {
				attrToRemove.add(attrToPermute.get(i));
			}
		}
		System.out.println("removing");
		for(int i = 0; i < attrToRemove.size(); ++i) {
			attrToPermute.remove(attrToRemove.get(i));
		}
	}

	/**
	 * Generates attribute sets of size (in number of attributes) itemsetNum, from a list
	 * of possible attributes possibleAttr. Generated attribute sets (also called itemsets)
	 * are stored in attrToPermute.
	 * @param itemsetNum Size of generated itemsets.
	 * @param possibleAttr Attributes that can be combined to generate itemsets.
	 * @param attrToPermute Place where generated itemsets are stored.
	 */
	private void genAttributesToPermute(int itemsetNum, LinkedHashSet<Integer> possibleAttr, Vector<String> attrToPermute) {
		 Vector<String> tempCandidates = new Vector<String>(); //temporary candidate string vector
	     String str1, str2; //strings that will be used for comparisons
	     StringTokenizer st1, st2; //string tokenizers for the two itemsets being compared

	        //if its the first set, candidates are just the numbers
	     if(itemsetNum == 1) {
	    	 for(Integer attr : possibleAttr) {
	        	tempCandidates.add(Integer.toString(attr));
	        }
	     }
	     else if(itemsetNum == 2) { //second itemset is just all combinations of itemset 1 
	    	 //add each itemset from the previous frequent itemsets together
	         for(int i = 0; i < attrToPermute.size(); ++i) {
	        	 st1 = new StringTokenizer(attrToPermute.get(i));
	             str1 = st1.nextToken();
	             for(int j = i + 1; j < attrToPermute.size(); ++j) {
	            	 st2 = new StringTokenizer(attrToPermute.elementAt(j));
	                 str2 = st2.nextToken();
	                 tempCandidates.add(str1 + " " + str2);
	             }
	         }
	     }
	     else {
	         //for each itemset
	         for(int i = 0; i < attrToPermute.size(); ++i) {
	         //compare to the next itemset
	        	 for(int j = i + 1; j < attrToPermute.size(); ++j) {
	        		 //create the strings
	        		 str1 = new String();
	        		 str2 = new String();
	        		 //create the tokenizers
	        		 st1 = new StringTokenizer(attrToPermute.get(i));
	        		 st2 = new StringTokenizer(attrToPermute.get(j));

	        		 //make a string of the first n-2 tokens of the strings
	        		 for(int s = 0; s < itemsetNum - 2; ++s) {
	        			 str1 = str1 + " " + st1.nextToken();
	        			 str2 = str2 + " " + st2.nextToken();
	        		 }

	        		 //if they have the same n-2 tokens, add them together
	        		 if(str2.compareToIgnoreCase(str1)==0) {
	        			 tempCandidates.add((str1 + " " + st1.nextToken() + " " + st2.nextToken()).trim());
	        		 }  
	        	 }
	         }
	     }
	     //clear the old candidates
	     attrToPermute.clear();
	     //set the new ones
	     attrToPermute.addAll(tempCandidates);
	     tempCandidates.clear();
	}
	
	/**
	 * Calculates the support value of a pattern (Sm), by using its
	 * weight support (Sw) and its frequency support (Sf).
	 * Sm = max {Sw, Sf}.
	 * @param weight Weight support.
	 * @param freq Frequency support.
	 * @return Support value of pattern.
	 */
	private double getMaxSupport(double weight, double freq) {
		return Math.max(weight, freq);
	}

	//FGSpan-edgecomb
	public void setFreqEdges(LinkedHashSet<PatternEdge> edges) {
		freqEdges = edges;
	}
}
