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
import java.util.Vector;

import mining.data.EFGEdge;
import mining.data.EFGVertex;
import mining.data.ExecutionFlowGraph;
import mining.data.PatternGraph;
import mining.data.PatternVertex;


/**
 * The SubgraphMiner class controls the process of finding all matches of a sub-graph in
 * a bigger graph. 
 * @author cgomes
 *
 */
public class SubgraphMiner {
	/**
	 * Sub-graph to be mined for.
	 */
	PatternGraph subGraph;
	/**
	 * Graph where the sub-graph is to be looked for.
	 */
	ExecutionFlowGraph wholeGraph;
	
	/**
	 * Number of matches of subGraph found in wholeGraph.
	 */
	int numMatches;
	/**
	 * Weight support of subGraph in wholeGraph.
	 */
	double weightSupport;
	/**
	 * Frequency support of subGraph in wholeGraph.
	 */
	double freqSupport;
	
	/**
	 * If true, mappings between subGraph's nodes and edges and corresponding 
	 * nodes and edges of its matches in wholeGraph are recorded, in order to 
	 * be used as starting search points for future child patterns of subGraph.
	 * If false, mappings are not recorded, so search for future child patterns
	 * of subGraph, in this particular wholeGraph, always must check all nodes of
	 * wholeGraph.
	 */
	boolean saveState;
	
	/**
	 * Constructor for SubgraphMiner, just initializes variables.
	 * @param subGraph Sub-graph to be mined for.
	 * @param wholeGraph Graph to be looked at for subGraph instances.
	 * @param saveState Whether to record areas where subGraph is found in wholeGraph.
	 */
	public SubgraphMiner(PatternGraph subGraph, ExecutionFlowGraph wholeGraph, boolean saveState) {
		this.subGraph = subGraph;
		this.wholeGraph = wholeGraph;
		this.numMatches = 0;
		weightSupport = 0;
		freqSupport = 0;
		this.saveState = saveState;
	}
	/**
	 * Returns number of matches of subGraph in wholeGraph.
	 * @return Number of matches.
	 */
	public int getNumMatches() {
		return numMatches;
	}
	
	/**
	 * Returns weight support of subGraph in wholeGraph.
	 * @return Weight support.
	 */
	public double getWeightSupport() {
		return weightSupport;
	}
	/**
	 * Returns frequency support of subGraph in wholeGraph.
	 * @return Frequency support.
	 */
	public double getFreqSupport() {
		return freqSupport;
	}
	
	/**
	 * Starts mining process.
	 * @param prevStates Locations in wholeGraph where the parent pattern of subGraph was found.
	  @param typeOfAddition If 0, a new {edge, node} pair is being added; otherwise, a back-edge is being added.
	 * @param pivotVertexId Node from the parent pattern to which the new edge is to be connected from (from-node).
	 * @param targetVertexId If typeOfAddition is 0, the new node added to the pattern. If typeOfAddition is 1, the
	 * @param oldNewIdCorrespondence Map between node IDs before and after addition of new edge.
	 * @return Vector of locations where subGraph is found in wholeGraph, if we opt to record them. Otherwise, an empty vector.
	 */
	public Vector<MinerState> run(Vector<MinerState> prevStates, int typeOfAddition, 
					int pivotVertexId, int targetVertexId, 
					LinkedHashMap<Integer, Integer> oldNewIdCorrespondence) {
		Vector<MinerState> endStates = new Vector<MinerState>();
		//If there are previous MinerStates available (i.e, this mining run is a pattern expansion
		//of a previously found pattern)...

		if(prevStates != null) {
			//System.out.println("Starter states:\n");
			for(int i = 0; i < prevStates.size(); ++i) {
				MinerState newStartState = new MinerState(prevStates.get(i), subGraph, wholeGraph, oldNewIdCorrespondence);
				
				//If typeofAddition is 1, a forward or backward edge has been added to the parent 
				//pattern in order to form the child pattern, and the edge is between 2 nodes 
				//that already exist in the parent pattern.
				if(typeOfAddition == 1) {
					matchEdgeOnly(newStartState, endStates, pivotVertexId, targetVertexId);
				}
				//If typeOfAddition is 0, a forward edge and a new node have been added to the
				//parent pattern, in order to form the child pattern.
				else {
					match(newStartState, endStates, pivotVertexId, targetVertexId);
				}
				//System.out.println("State " + i + ":" + prevStates.get(i).getWholeGraphCore().toString() + "\n");
			}
		}
		//If there are no previous MinerStates available (i.e. no records of locations in
		//wholeGraph where the parent pattern of subGraph was found), the matching process
		//occurs by checking all nodes in wholeGraph and all nodes in subGraph for correspondence.
		else {
			MinerState startState = new MinerState(subGraph, wholeGraph);
			match(startState, endStates, pivotVertexId, targetVertexId);
		}
		
		//Facilitating garbage collection...
		for(MinerState s : endStates) {
			s.setWholeGraph(null);
			s.setSubGraph(null);
		}
		return endStates;
	}

	/**
	 * Handles case of edge-only addition, to parent pattern of subGraph, in order to generate subGraph.
	 * The idea is to check if the edge that goes from the pivot node (whose ID is pivotVertexId) to the 
	 * target node (whose ID is targetVertexId) in subGraph also exists in the instance s of the parent pattern
	 * of subGraph. That is, we check if the node in s that corresponds to the pivot node in subGraph has an edge 
	 * coming out of it that goes to the node in s that corresponds to the target node in subGraph. It is important
	 * to remember that the nodes in s referred to here are the ones in wholeGraph.
	 * @param s Mapping between nodes in subGraph's parent pattern and nodes in wholeGraph.
	 * @param endStates Set of mappings between subGraph and wholeGraph. If we find a mapping, we store it here.
	 * @param pivotVertexId ID of subGraph's pivot node.
	 * @param targetVertexId ID of subGraph's target node.
	 */
	private void matchEdgeOnly(MinerState s, Vector<MinerState> endStates, 
			long pivotVertexId, long targetVertexId) {
		LinkedHashMap<Integer, Long> sgCore = s.getSubGraphCore();
		
		long toVertexId = sgCore.get(targetVertexId);
		long fromVertexId = sgCore.get(pivotVertexId);
		
		//Uncomment if skipping dummy nodes when looking for pattern instances.
		//double freq = wholeGraph.findEdge(fromVertexId, toVertexId);
	
		//Comment if skipping dummy nodes when looking for pattern instances.
		EFGEdge e = wholeGraph.hasEdge(fromVertexId, toVertexId);
		double freq = 0;
		if(e != null) {
			freq = e.getFrequency();
		}
		
		//Should work for both dummy node cases...
		if(freq > 0) {
			Vector<Integer> sgCoreSet = new Vector<Integer>();
			Vector<Long> wgCoreSet = new Vector<Long>();
			s.getCoreSet(sgCoreSet, wgCoreSet);
			
			++numMatches;
			weightSupport += calcWeightSupport(wgCoreSet, sgCoreSet);
			if(sgCoreSet.size() > 1) {
				freqSupport += Math.min(s.getMinFreq(), freq);
			}
		}
	     
	    return;
	}

	/**
	 * Calculates weight support for a pattern instance of subGraph found in wholeGraph.
	 * @param wgCoreSet Nodes in wholeGraph that correspond to those in subGraph.
	 * @param sgCoreSet Nodes in subGraph.
	 * @return Weight support of pattern instance.
	 */
	private double calcWeightSupport(Vector<Long> wgCoreSet, Vector<Integer> sgCoreSet) {
		/**
		 * Weight support of an instance is the minimum attribute weight found in this 
		 * instance.
		 */
		double minWeight = Double.MAX_VALUE;
		for(int i = 0; i < wgCoreSet.size(); ++i) {
			EFGVertex wholeV = wholeGraph.getVertex(wgCoreSet.get(i));
			PatternVertex subV = subGraph.getVertex(sgCoreSet.get(i));

			for(int attr: subV.getAttrWeights().keySet()) {
				double weight = wholeV.getAttributeWeight(attr);
				minWeight = Math.min(minWeight, weight);

			}
		}
		return minWeight;
	}
	
	/**
	 * Handles matching process for a subGraph that was generated by the addition of
	 * a new {edge, node} pair to its parent pattern.
	 * The idea is to use the parent pattern's mappings to find the node 'v' in wholeGraph that
	 * corresponds to the pivot node in subGraph (from which we are connecting the new edge).
	 * Then, we get all the child nodes of 'v'and compare them to the target node of subGraph,
	 * which is a newly created node. For each node 'q' that we find to correspond to the target
	 * node, we calculate the support value of the instance that includes 'q' by calling the 
	 * handleGoalFound method. 
	 * @param s Mappings between nodes in subGraph's parent pattern and wholeGraph nodes.
	 * @param endStates Set of mappings between subGraph and wholeGraph. If we find a mapping, we store it here.
	 * @param pivotVertexId ID of subGraph's pivot node.
	 * @param targetVertexId ID of subGraph's target node.
	 */
	void match(MinerState s, Vector<MinerState> endStates, int pivotVertexId, int targetVertexId) {
		Vector<Long> candNodes = findCandidateNodes(s, pivotVertexId);
	    int i = 0;
	    while (i < candNodes.size()) {
	    	long candVertexId = candNodes.get(i);
	    	if (s.isFeasiblePair(targetVertexId, candVertexId) == true) { 
	    		MinerState sDerived = new MinerState(s);
	            sDerived.addPair(pivotVertexId, targetVertexId, candVertexId);
	            if(sDerived.isGoal()) {
	            	handleGoalFound(sDerived, endStates);
	            }
	        }
	    	++i;
	    }
	    
	    return;
	  }

	/**
	 * If a match between a candidate node in wholeGraph and subGraph's target node is found,
	 * this method is called to calculate the support value of the found instance of subGraph
	 * and to create the mapping between subGraph and corresponding nodes in wholeGraph.
	 * @param s Mapping of subGraph's parent pattern instance and nodes in wholeGraph.
	 * @param endStates Set of mappings between subGraph and wholeGraph. If we find a 
	 * mapping, we store it here.
	 */
	private void handleGoalFound(MinerState s, Vector<MinerState> endStates) {
		Vector<Integer> sgCoreSet = new Vector<Integer>();
		Vector<Long> wgCoreSet = new Vector<Long>();
		s.getCoreSet(sgCoreSet, wgCoreSet);
		//DEBUG
		//System.out.println("sgCoreSet: ");
		//for(int i = 0; i < sgCoreSet.size(); ++i) {
			//System.out.print(sgCoreSet.get(i));
			//System.out.print("\n");
		//}
		//System.out.println("\n\n\nwgCoreSet: ");
		//for(int i = 0; i < wgCoreSet.size(); ++i) {
		//	System.out.print(wgCoreSet.get(i));
		//	System.out.print("\n");
		//}

		++numMatches;
		weightSupport += calcWeightSupport(wgCoreSet, sgCoreSet);
		if(sgCoreSet.size() > 1) {
			if(s.getMinFreq() < Double.MAX_VALUE) {
				freqSupport += s.getMinFreq();
			}
		}
	
		//Here we create a mapping between pattern instances and 
		//the instructions where they are found.
		for(int i = 0; i < sgCoreSet.size(); ++i) {
			int sgId = sgCoreSet.get(i);
			PatternVertex sgV = subGraph.getVertex(sgId);
			long wgId = wgCoreSet.get(i); 
			EFGVertex wgV = wholeGraph.getVertex(wgId);
			
			if(wgV.getInstructions() != null) {
				for(String instr : wgV.getInstructions()) {
					sgV.addInstruction(instr);
				}
			}
			if(wgV.getAddresses() != null) {
				for(String addr : wgV.getAddresses()) {
					sgV.addAddress(addr);
				}
			}
		}
			
		if(saveState == true) {
			endStates.add(s);
		}
		return;
	}

	/**
	 * If we have a mapping between the pivot node in subGraph and a node in wholeGraph,
	 * we get the children of this node in wholeGraph that are connected to it by forward
	 * edges and return them. If we do not have a mapping, we just return all nodes in
	 * wholeGraph, because they all are candidates for target node.
	 * @param s Mapping between nodes in subGraph and wholeGraph, but may be empty.
	 * @param pivotVertexId Pivot node, used to restrict which nodes are returned as candidates.
	 * @return Candidate nodes in wholeGraph to be compared against subGraph's target node.
	 */
	private Vector<Long> findCandidateNodes(MinerState s, int pivotVertexId) {
		Vector<Long> possibleNodes = new Vector<Long>();
		if(s.getWholeGraphCore().size() == 0) {
			possibleNodes.addAll(s.getWholeGraph().getVertexSet().keySet());
			return possibleNodes;
		}
		
		long wgVertexId = s.getSubGraphCore().get(pivotVertexId);
		EFGVertex currVertex = wholeGraph.getVertex(wgVertexId);
		int remainingGap = subGraph.getAllowedGap();
		Vector<EFGVertex> children = currVertex.getForwardChildren();
		
		while(remainingGap >= 0) {	
			//Comment if skipping dummy nodes when looking for candidate nodes.
			Vector<EFGVertex> nextChildren = new Vector<EFGVertex>();
			for(EFGVertex child : children) {
				//Match between pattern (subGraph) and graph nodes (wholeGraph) is a one-to-one
				//mapping, so there should be no repetitions neither in possibleNodes vector
				//nor in MinerState mappings (wgCore and sgCore).
				boolean isInMapping = s.getWholeGraphCore().keySet().contains(child.getId());
				if(possibleNodes.contains(child.getId()) == false && 
							isInMapping == false) {
					possibleNodes.add(child.getId());
				}
				
				if(remainingGap > 0 && isInMapping == false) {
					nextChildren.addAll(child.getForwardChildren());
				}
			}
			
			children = nextChildren;	
			remainingGap--;
			//Uncomment if skipping dummy nodes when looking for candidate nodes.
			//NOTE: in such a case there is no gap concept implemented. 
			/*LinkedHashSet<Vertex> alreadySeen = new LinkedHashSet<Vertex>();
			
			while(children.isEmpty() == false) {
				Vertex child = children.get(0);
				children.remove(0);
				
				if(alreadySeen.contains(child)) {
					continue;
				}
				
				alreadySeen.add(child);
				
				if(child.isDummy() == true) {
					List<Vertex> candNodes = (List<Vertex>) child.getForwardChildren();
					for(Vertex v : candNodes) {
						if(children.contains(v) == false) {
							children.add(v);
						}
					}
					continue;
				}
				
				//Match between pattern (subGraph) and graph nodes (wholeGraph) is a one-to-one
				//mapping, so there should be no repetitions neither in possibleNodes vector
				//nor in MinerState mappings (wgCore and sgCore).
				if(possibleNodes.contains(child.getId()) == false && 
							(s.getWholeGraphCore().keySet().contains(child.getId()) == false)) {
					possibleNodes.add(child.getId());
				}
			}*/
		}
		
		
		return possibleNodes;
	}
}
