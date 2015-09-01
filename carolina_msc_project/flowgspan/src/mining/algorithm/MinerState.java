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
 * The MinerState class represents a step in the mining process, and holds the
 * current mapping between nodes in the sub-graph to be matched and nodes (if any)
 * in the bigger graph that is being mined for the sub-graph.
 * @author cgomes
 *
 */
public class MinerState {
	/**
	 * How many nodes from subGraph have been matches thus far.
	 */
	int currMatchLength;
	/**
	 * Mapping between nodes in subGraph and nodes of wholeGraph (that belong to an
	 * instance of subGraph in wholeGraph).
	 */
	LinkedHashMap<Integer, Long> sgCore;
	/**
	 * Mapping between nodes in wholeGraph (that belong to an instance of subGraph in 
	 * wholeGraph) and nodes in subGraph.
	 */
	LinkedHashMap<Long, Integer> wgCore;
	
	/**
	 * Sub-graph to be mined for.
	 */
	PatternGraph subGraph;
	/**
	 * Graph to be looked at for subGraph instances.
	 */
	ExecutionFlowGraph wholeGraph;
	
	/**
	 * Size of subGraph (in number of nodes).
	 */
	int sgSize;
	/**
	 * Size of wholeGraph (in number of nodes).
	 */
	int wgSize;
	
	/**
	 * Total frequency (addition of edge frequencies) of pattern instance found.
	 */
	double totalFreq;
	/**
	 * Minimum edge frequency of pattern instance found.
	 */
	double minFreq;
	/**
	 * Maximum edge frequency of pattern instance found.
	 */
	double maxFreq;
          
	 /**
	  * Constructor called when a subGraph is to be mined for in a particular wholeGraph
	  * for the first time.
	  * @param subGraph Sub-graph to be mined for.
	  * @param wholeGraph Graph where mining is to happen.
	  */
	 public MinerState(PatternGraph subGraph, ExecutionFlowGraph wholeGraph) {
		 this.subGraph = subGraph;
		 this.wholeGraph = wholeGraph;
		 
		 sgSize = subGraph.getVertexSet().size();
		 wgSize = wholeGraph.getVertexSet().size();
		 
		 currMatchLength = 0;
		 
		 sgCore = new LinkedHashMap<Integer, Long>();
		 wgCore = new LinkedHashMap<Long, Integer>();
		 
		 totalFreq = 0f;
		 minFreq = Double.MAX_VALUE;
		 maxFreq = 0f;
	 }
	 
	 @SuppressWarnings("unchecked")
	 /**
	 * Constructor for derived mining state (there will be an addition of a subGraph-wholeGraph 
	 * node correspondence)
	 */
	 public MinerState(MinerState otherState) {
		 subGraph = otherState.getSubGraph();
		 wholeGraph = otherState.getWholeGraph();
		 sgSize = otherState.getSubGraphSize();
		 wgSize = otherState.getWholeGraphSize();
		 currMatchLength = otherState.getCurrMatchLength();

		 sgCore = (LinkedHashMap<Integer, Long>) otherState.getSubGraphCore().clone();
		 wgCore = (LinkedHashMap<Long, Integer>) otherState.getWholeGraphCore().clone();
		    
		 totalFreq = otherState.getTotalFreq();
		 minFreq = otherState.getMinFreq();
		 maxFreq = otherState.getMaxFreq();
	 }

	@SuppressWarnings("unchecked")
	/**
	 * Constructor for initial mining of a subgraph, but based on previous mining state.
	 */
	public MinerState(MinerState prevState, PatternGraph newSubGraph, ExecutionFlowGraph prevWholeGraph, 
			LinkedHashMap<Integer, Integer> oldNewIdCorrespondence) {
		 subGraph = newSubGraph;
		 wholeGraph = prevWholeGraph;
		 sgSize = newSubGraph.getVertexSet().size();
		 wgSize = prevState.getWholeGraphSize();
		 currMatchLength = prevState.getCurrMatchLength();

		 if(oldNewIdCorrespondence == null) {
			 sgCore = (LinkedHashMap<Integer, Long>)prevState.getSubGraphCore().clone();
			 wgCore = (LinkedHashMap<Long, Integer>)prevState.getWholeGraphCore().clone();
		 }
		 else {
			 LinkedHashMap<Integer, Long> prevSgCore = prevState.getSubGraphCore();
			 sgCore = new LinkedHashMap<Integer, Long>();
			 for(Integer key : prevSgCore.keySet()) {
				 int newKey = oldNewIdCorrespondence.get(key);
				 sgCore.put(newKey, prevSgCore.get(key));
			 }
			 
			 LinkedHashMap<Long, Integer> prevWgCore = prevState.getWholeGraphCore();
			 wgCore = new LinkedHashMap<Long, Integer>();
			 for(Long key : prevWgCore.keySet()) {
				 int newValue = oldNewIdCorrespondence.get(prevWgCore.get(key));
				 wgCore.put(key, newValue);
			 }
		 }
		 
		 totalFreq = prevState.getTotalFreq();
		 minFreq = prevState.getMinFreq();
		 maxFreq = prevState.getMaxFreq();
	 }

	/**
	 * Returns subGraph.
	 * @return Sub-graph.
	 */
	 public PatternGraph getSubGraph() {
		 return subGraph;
	 }
	 /**
	  * Returns graph we are looking at.
	  * @return wholeGraph.
	  */
	 public ExecutionFlowGraph getWholeGraph() {
		 return wholeGraph;
	 }
	 
	 /**
	  * Returns length (in number of nodes) of current match.
	  * @return Length of current match.
	  */
	 int getCurrMatchLength() {
		 return currMatchLength;
	 }
	 
	 /**
	  * Returns size (in number of nodes) of sub-graph.
	  * @return Number of nodes of subGraph.
	  */
	 public int getSubGraphSize() {
		 return sgSize;
	 }
	 
	 /**
	  * Returns size (in number of nodes) of wholeGraph.
	  * @return Number of nodes of wholeGraph.
	  */
	 public int getWholeGraphSize() {
		 return wgSize;
	 }
	
	 /**
	  * Returns sgCore mapping.
	  * @return Mapping of nodes in subGraph and instance nodes in wholeGraph.
	  */
	 public LinkedHashMap<Integer, Long> getSubGraphCore() {
		 return sgCore;
	 }
	 
	 /** 
	  * Returns wgCore mapping. 
	  * @return Mapping of instance nodes in wholeGraph and nodes in subGraph.
	  */
	 public LinkedHashMap<Long, Integer> getWholeGraphCore() {
		 return wgCore;
	 }
	 
	 /**
	  * Returns total frequency of instance of subGraph found in wholeGraph.
	  * @return Total frequency of pattern instance (sum of all edge frequencies).
	  */
	 public double getTotalFreq() {
		 return totalFreq;
	 }
	 
	 /**
	  * Returns minimum edge frequency of pattern instance found in wholeGraph.
	  * @return Minimum edge frequency of pattern instance.
	  */
	 public double getMinFreq() {
		 return minFreq;
	 }
	 
	 /**
	  * Returns maximum edge frequency of pattern instance found in wholeGraph.
	  * @return Maximum edge frequency of pattern instance.
	  */
	 public double getMaxFreq() {
		 return maxFreq;
	 }
	 
	 /**
	  * Returns true if the match we have found already covers the whole sub-graph we are mining
	  * for; false otherwise.
	  * @return Whether a complete match of subGraph has been found.
	  */
	 boolean isGoal() {
		 //System.out.println("currMatchLength, sgSize: " + currMatchLength + " " + sgSize);
		 return currMatchLength == sgSize;
	 }
	 
	 /**
	  * Splits sgCore mapping, placing subGraph's nodes in sgCoreSet and
	  * corresponding wholeGraph's nodes in wgCoreSet.
	  * @param sgCoreSet Where subGraph's nodes are stored.
	  * @param wgCoreSet Where corresponding wholeGraph's nodes are stored.
	  */
	 void getCoreSet(Vector<Integer> sgCoreSet, Vector<Long> wgCoreSet) {
		 for (int i : sgCore.keySet()) {
			 sgCoreSet.add(i);
			 wgCoreSet.add(sgCore.get(i));
		 }
	 }
	 
	 /**
	  * Returns whether a node from subGraph and a node from wholeGraph are
	  * feasible as a match.
	  * @param sgVertexId The ID of node from subGraph.
	  * @param wgVertexId The ID of node from wholeGraph.
	  * @return
	  */
	 boolean isFeasiblePair(int sgVertexId, long wgVertexId) {
		 PatternVertex sgVertex = subGraph.getVertex(sgVertexId);
		 EFGVertex wgVertex = wholeGraph.getVertex(wgVertexId);
		 
		 return subGraph.isCompatibleVertex(sgVertex, wgVertex);
	 }
	 
	 /**
	  * Adds a {subGraph node, wholeGraph node} pair to the sgCore and wgCore 
	  * mappings. Also calculates pattern instance frequency support values thus far.
	  * @param sgParentVertexId The parent node of the new subGraph node that is to be paired up.
	  * @param sgChildVertexId The new subGraph node that is to be paired up.
	  * @param wgChildVertexId The wholeGraph node to be paired up with the subGraph node.
	  */
	 void addPair(int sgParentVertexId, int sgChildVertexId, long wgChildVertexId) {
		 sgCore.put(sgChildVertexId, wgChildVertexId);
		 wgCore.put(wgChildVertexId, sgChildVertexId);
		 
		 long wgParentVertexId = sgCore.get(sgParentVertexId);
		 double freq = 0;
		 
		 //Comment if skipping dummy nodes when mining.
		 EFGEdge e = wholeGraph.hasEdge(wgParentVertexId, wgChildVertexId);
		 if(e != null) {
			 freq = e.getFrequency();
		 }
		 
		 //Uncomment if skipping dummy nodes when mining.
		 //if(sgParentVertexId != sgChildVertexId) {
		//	 freq = wholeGraph.findEdge(wgParentVertexId, wgChildVertexId);
		 //}
		 
		 if(freq > 0) {
			 totalFreq += freq;
			 minFreq = Math.min(minFreq, freq);
			 maxFreq = Math.max(maxFreq, freq);
		 }
		 ++currMatchLength;
	 }
	 /**
	  * Sets the graph to be mined in order to find the sub-graph.
	  * @param graph Graph to be mined.
	  */
	 public void setWholeGraph(ExecutionFlowGraph graph) {
		wholeGraph = graph;
	}

	 /**
	  * Sets the sub-graph to be mined for.
	  * @param graph Sub-graph to be mined for.
	  */
	public void setSubGraph(PatternGraph graph) {
		subGraph = graph;
	}
}
