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
 */package mining.data;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import com.sun.javafx.geom.Edge;
import mining.algorithm.MinerState;

public class PatternGraph implements Cloneable{
	/**
	 * Set of all edges contained in the graph. The edges are easily
	 * identified by a pair of IDs, which are the IDs of the two nodes
	 * the Edge connects.
	 */
    LinkedHashMap<Pair<Integer, Integer>, PatternEdge> edgeSet;
    /**
     * Set of nodes contained in the graph. They are identified by
     * their unique ID.
     */
	LinkedHashMap<Integer, PatternVertex> vertexSet;
    Vector<Integer> vertices;
	
	/**
	 * Graph's entry node.
	 */
	PatternVertex entryVertex;
	
	/**
	 * Graph's exit node (if any).
	 */
	PatternVertex exitVertex;
	
	/**
	 * Set of IDs of the EFGs where this Graph appears, when mined. 
	 */
	Vector<Integer> GS;
	
	/**
	 * Mapping between EFGs this Graph appears at, and its occurrences in each EFG.
	 */
	LinkedHashMap<Integer, Vector<MinerState>> minerStateMap;
	
	/**
	 * Number of instances found of Graph in dataset of EFGs. Note that this
	 * assumes Graph represents a sub-graph pattern.
	 */
	int numInstances;
	
	/**
	 * String representation of Graph.
	 */
	String keyStr;
	
	/**
	 * Instances of this sub-graph pattern (if the Graph instance really represents
	 * a pattern).
	 */
	//Vector<String> mappings;
	
	/**
	 * Indicates if Graph instance is:
	 * 0 - a node
	 * 1 - a sub-path
	 * 2 - a sub-graph
	 */
	int patternType;
	
	/**
	 * Gap allowed for this particular pattern (if it's indeed a pattern).
	 */
	int allowedGap;
	
	/**
	 * Graph constructor. Just variable initialization.
	 */
	public PatternGraph() {
		edgeSet = new LinkedHashMap<Pair<Integer,Integer>, PatternEdge>();
		//vertexSet = new LinkedHashMap<Integer, PatternVertex>();
        vertices = new Vector<Integer>();
		GS = new Vector<Integer>();
	
		entryVertex = null;
		exitVertex = null;
	
		minerStateMap = new LinkedHashMap<Integer, Vector<MinerState>>();
		
		numInstances = 0;
		
		patternType = -1;
		allowedGap = 0;
	}

	/**
	 * Sets the number of instances of Graph found in dataset.
	 * @param instances Number of Graph instances found.
	 */
	public void setNumInstances(int instances) {
		numInstances = instances;
	}
	
	/**
	 * Sets if pattern is a node, a sub-path or a sub-graph.
	 * If the pattern is already known to be a sub-graph, it cannot
	 * be changed to be anything else so there is no need for reassignment.
	 * @param type Type of pattern
	 */
	public void setPatternType(int type) {
		if(patternType != 2) {
			patternType = type;
		}
	}
	
	/**
	 * Returns type of pattern.
	 * @return Pattern type (0 for node, 1 for sub-path, 2 for sub-graph.
	 */
	public int getPatternType() {
		return patternType;
	}
	
	/**
	 * Inserts an edge in the Graph. Note that this function works under the 
	 * supposition that edges being added had their from-node and to-node already 
	 * created and added to them.
	 * @param e Edge to be inserted in Graph.
	 */
	public void insertEdge(PatternEdge e) {
		int fromId = e.getFromVertex().getId();
		int toId = e.getToVertex().getId();
		edgeSet.put(new Pair<Integer, Integer>(fromId, toId), e);
		e.getFromVertex().setOutEdge(e);
		e.getToVertex().setIncEdge(e);
	}
	
	/**
	 * Inserts node in Graph.
	 * @param v Node to be inserted.
	 */
	public void insertVertex(int index) {
		//vertexSet.put(v.getId(), v);
        if (!vertices.contains(index))
            vertices.add(index);
	}
	
	/**
	 * Returns node that has a certain ID.
	 * @param vertexId ID of node to be returned.
	 * @return Returned node.
	 */
	public PatternVertex getVertex(int vertexId) {

        if (entryVertex.getId() == vertexId)
		    return entryVertex;
        if (exitVertex.getId() == vertexId)
            return exitVertex;
        for (PatternEdge e: edgeSet.values()) {
            if (e.getFromVertex().getId() == vertexId)
                return e.getFromVertex();
            if (e.getToVertex().getId() == vertexId)
                return e.getToVertex();

        }
        return null;
	}
	
	/**
	 * Returns edge set.
	 * @return Edge set of this Graph.
	 */
	public LinkedHashMap<Pair<Integer, Integer>, PatternEdge> getEdgeSet() {
		return edgeSet;
	}
	
	/**
	 * Returns node set.
	 * @return Node set of this Graph.
	 */

	public LinkedHashMap<Integer, PatternVertex> getVertexSet() {
        //System.out.println(vertexSet + " ORIGINAL");
        //System.out.println(createVertexSet() + " COPY");
		return vertexSet;
        //return createVertexSet();
	}
	
	/**
	 * Sets exit node.
	 * @param v Exit node for this Graph.
	 */
	public void setExitVertex(PatternVertex v) {
		exitVertex = v;
	}
	
	/**
	 * Returns exit node.
	 * @return Exit node.
	 */
	public PatternVertex getExitVertex() {
		return exitVertex;
	} 
	
	/**
	 * Sets entry node of this Graph.
	 * @param v Entry node.
	 */
	public void setEntryVertex(PatternVertex v) {
		entryVertex = v;
	}
	
	/**
	 * Returns entry node.
	 * @return Entry node.
	 */
	public PatternVertex getEntryVertex() {
		return entryVertex;
	}
	
	/**
	 * Adds an EFG ID to the GS set.
	 * @param i EFG ID.
	 */
	public void addToGS(int i) {
		//Lazy initialization of GS.
		if(GS == null) {
			GS = new Vector<Integer>();
		}
		GS.add(i);
	}
	/**
	 * Returns GS of this Graph.
	 * @return GS.
	 */
	public Vector<Integer> getGS() {
		return GS;
	}
	
	/**
	 * Clones a Graph. The copy is deep, which means that ALL elements of Graph,
	 * including edges and nodes, are cloned as well (but not attribute info).
	 * @return Cloned Graph.
	 */
	public PatternGraph clone() {
		PatternGraph cloned = new PatternGraph();
		/*LinkedHashMap<Integer, PatternVertex> vertexSet = new LinkedHashMap<Integer, PatternVertex>();
		for(PatternVertex origV : vertexSet.values()) {
>>>>>>> patternGraph
			PatternVertex v = new PatternVertex(origV.getWeight(), origV.getId());
			v.setAttributes(origV.getAttributes());
			v.setAttrWeights(origV.getAttrWeights());
			if(origV.equals(entryVertex)) {
    			cloned.setEntryVertex(v);
    		}
    		else if(origV.equals(exitVertex)) {
    			cloned.setExitVertex(v);
    		}
			cloned.insertVertex(v);
		}*/
		for(PatternEdge origE : edgeSet.values()) {
            PatternVertex enter = new PatternVertex(entryVertex.getWeight(), entryVertex.getId());
            PatternVertex exit = new PatternVertex(exitVertex.getWeight(), exitVertex.getId());
            enter.setAttributes(entryVertex.getAttributes());
            exit.setAttributes(exitVertex.getAttributes());
            cloned.setEntryVertex(enter);
            cloned.setExitVertex(exit);
			int fromId = origE.getFromVertex().getId();
			int toId = origE.getToVertex().getId();
			PatternVertex fromV = vertexSet.get(fromId);
			PatternVertex toV = vertexSet.get(toId);
			PatternEdge e = new PatternEdge(fromV, toV, origE.getFrequency());
			cloned.insertEdge(e);
		}
	
		return cloned;
	}

	/**
	 * Returns string representation of Graph.
	 * @return String representation of Graph.
	 */
	public String toString() {
		keyStr = "";
		LinkedHashMap<Integer, PatternVertex> vertexSeta = createVertexSet();
		if(edgeSet.size() > 0) {
			List<Pair<Integer, Integer>> keyList = new Vector<Pair<Integer, Integer>>();
			keyList.addAll(edgeSet.keySet());
			PairComparator<Integer, Integer> pairComp = new PairComparator<Integer, Integer>();
			Collections.sort(keyList, pairComp);
			
			for(Pair<Integer, Integer> pair : keyList) {
				PatternEdge e = edgeSet.get(pair);
				keyStr += e.toString() + "\n";
			}
		}
		else {
			keyStr += "( ";
            LinkedHashMap<Integer, PatternVertex> vertexSet = createVertexSet();

            for(PatternVertex v : vertexSet.values()) {
				keyStr += "[ " + Long.toHexString(v.getId()) + ": " + v.toCode() + " ] ";
			}
			keyStr += ")\n";
		}
		return keyStr;
	}
	
	/**
	 * Finds entry and exist nodes of a Graph, in case they exist.
	 */
	public void findEntryExitVertices() {

        LinkedHashMap<Integer, PatternVertex> vertexSet = createVertexSet();
		for(PatternVertex v : vertexSet.values()) {
			if(v.getInEdgeCount() == 0) {
				setEntryVertex(v);
				break;
			}
		}
		//for(PatternVertex v : vertexSet.values()) {
        for(PatternEdge e : edgeSet.values()) {
            PatternVertex v = e.getToVertex();
			if(v.getOutEdgeCount() == 0) {
				setExitVertex(v);
				break;
			}
		}
	}
	
	/**
	 * Finds the edge between 2 nodes with given IDs, but only if there are no dummy nodes 
	 * in-between them.
	 * @param fromVertexId The ID of from-node.
	 * @param toVertexId The ID of to-node.
	 * @return Edge that goes from from-node to to-node, or null if edge not found.
	 */
	public PatternEdge hasEdge(int fromVertexId, int toVertexId) {
		for(PatternEdge e : edgeSet.values()) {
			if(e.getFromVertex().getId() == fromVertexId && e.getToVertex().getId() == toVertexId) {
				return e;
			}
		}
		
		return null;
	}
	
	/**
	 * Changes the IDs of nodes in the Graph, assigning IDs in depth-first-like order.
	 * This is done only for Graphs that represent sub-graph patterns and only after they
	 * receive a new {edge, node} pair or new edge. Graphs that represent EFG never have
	 * their node IDs changed, because such IDs are actually instruction addresses and, thus,
	 * globally unique.
	 * @param oldNewIdCorrespondence Mapping between node IDs before ID reassignment, and node 
	 * IDs after reassignment. This mapping is created by this method. 
	 */
	public void lexSort(LinkedHashMap<Integer, Integer> oldNewIdCorrespondence) {
		int id = 0;
        LinkedHashMap<Integer, PatternVertex> vertexSet = createVertexSet();

        Stack<PatternVertex> vertexStack = new Stack<PatternVertex>();
		vertexStack.push(entryVertex);
		LinkedHashMap<Integer, PatternVertex> newVertexSet = new LinkedHashMap<Integer, PatternVertex>();
		
		while(vertexStack.empty() == false) {
			PatternVertex v = vertexStack.peek();
		
			if(v.visited() == false) {
				oldNewIdCorrespondence.put(v.getId(), id);
				v.setId(id);
				v.setVisited(true);
				newVertexSet.put(v.getId(), v);
				id += 1;
			}
		
			Vector<PatternVertex> vChildren = v.getForwardChildren();
			boolean canPop = true;
			for(PatternVertex child : vChildren) {
				if(child.visited() == false) {
					canPop = false;
					vertexStack.push(child);
					break;
				}
			}
			
			if(canPop == true) {
				vertexStack.pop();
			}
		}
		
		vertexSet.clear();
		vertexSet.putAll(newVertexSet);
		System.out.println(vertexSet);
		LinkedHashMap<Pair<Integer, Integer>, PatternEdge> newEdgeSet = new LinkedHashMap<Pair<Integer, Integer>, PatternEdge>();
		System.out.println(oldNewIdCorrespondence);
        System.out.println(edgeSet);
		for(Pair<Integer, Integer> origPair : edgeSet.keySet()) {
			int firstId = origPair.getFirst();
			int secondId = origPair.getSecond();
			int newFirstId = oldNewIdCorrespondence.get(firstId);
            int newSecondId = oldNewIdCorrespondence.get(secondId);
			Pair<Integer, Integer> newPair = new Pair<Integer, Integer>(newFirstId, newSecondId);
			newEdgeSet.put(newPair, edgeSet.get(origPair));
		}
		edgeSet.clear();
		edgeSet.putAll(newEdgeSet);
	}
	
	/**
	 * Returns true if two given nodes are compatible. Being compatible means that
	 * they can be considered a match, when testing if a sub-graph pattern matches a
	 * sub-graph instance in an EFG. A node thisVertex is compatible with a node otherVertex 
	 * if attributes in otherVertex are the same or a superset of attributes in thisVertex.
	 * @param thisVertex Node from the sub-graph pattern.
	 * @param otherVertex Node from the sub-graph instance in EFG.
	 * @return True if the nodes are compatible, false otherwise.
	 */
	public boolean isCompatibleVertex(PatternVertex thisVertex, EFGVertex otherVertex) {
		if(thisVertex.getNumAttrs() <= 0 || otherVertex.getNumAttrs() <= 0) {
			return false;
		}

		synchronized(otherVertex) {
			otherVertex.setMask(thisVertex.getAttributes());
			boolean returnVal = thisVertex.equals(otherVertex, false, true);
			otherVertex.setMask(otherVertex.getAttributes());
			return returnVal;
		}
	}
	
	/**
	 * Sets, for a given EFG that this Graph appears at, the mappings between
	 * nodes in this Graph and nodes in the EFG, i.e. the places where this Graph
	 * occurs in the given EFG.
	 * @param graphIdx ID of EFG.
	 * @param endStates Places where Graph occurs in EFG.
	 */
	public void setMinerState(int graphIdx, Vector<MinerState> endStates) {
		minerStateMap.put(graphIdx, endStates);
	}
	
	/**
	 * Returns, for a given EFG ID, the places in the EFG where Graph occurs. 
	 * @param graphIdx EFG ID.
	 * @return Places where Graph occurs in EFG.
	 */
	public Vector<MinerState> getMinerState(int graphIdx) {
		return minerStateMap.get(graphIdx);
	}
	
	/**
	 * Returns all attribute IDs for all attributes that exist in Graph.
	 * Repetitions included.
	 * @return All attribute IDs in Graph.
	 */
	public Vector<Integer> getAllAttributes() {
		Vector<Integer> allAttrs = new Vector<Integer>();
		
		/*for(PatternVertex v : vertexSet.values()) {
			allAttrs.addAll(v.getAttrWeights().keySet());
		}*/
        for(PatternEdge e : edgeSet.values()) {
            allAttrs.addAll(e.getToVertex().getAttrWeights().keySet());
            allAttrs.addAll(e.getFromVertex().getAttrWeights().keySet());
        }

        return allAttrs;
	}
	
	/**
	 * Returns all distinct attribute IDs that exist in Graph.
	 * @return All distinct attribute IDs.
	 */
	public LinkedHashSet<Integer> getAllDistinctAttributes() {
		LinkedHashSet<Integer> allAttrs = new LinkedHashSet<Integer>();
		
		/*for(PatternVertex v : vertexSet.values()) {
>>>>>>> patternGraph
			allAttrs.addAll(v.getAttrWeights().keySet());
		}*/

        for(PatternEdge e : edgeSet.values()) {
            allAttrs.addAll(e.getToVertex().getAttrWeights().keySet());
            allAttrs.addAll(e.getFromVertex().getAttrWeights().keySet());
        }
		
		return allAttrs;
	}
	
	public LinkedHashSet<PatternEdge> getAllDistinctEdges() {
		LinkedHashSet<PatternEdge> allEdges = new LinkedHashSet<PatternEdge>();
		
		for(PatternEdge e : edgeSet.values()) {
			allEdges.add(e);
		}
		
		return allEdges;
	}
	/**
	 * Returns, in string representation, all instances of Graph, showing the instructions
	 * that the nodes in Graph represent, instead of the attributes.
	 * @return All string representations of Graph instances.
	 */
	/*public Vector<String> getInstructionMappings() {
		
		if(mappings != null) {
			return mappings;
		}
		
		mappings = new Vector<String>();
		List<Pair<Integer, Integer>> keyList = new Vector<Pair<Integer, Integer>>();
		
		if(edgeSet.size() > 0) {
			keyList.addAll(edgeSet.keySet());
			PairComparator<Integer, Integer> pairComp = new PairComparator<Integer, Integer>();
			Collections.sort(keyList, pairComp);
			
			for(int i = 0; i < numInstances; ++i) {
				String str = "";
				for(Pair<Integer, Integer> pair : keyList) {
					PatternEdge e = edgeSet.get(pair);
					str += e.toMapping(i) + "\n";
				}
				mappings.add(str);
			}
		}
		
		else {
			for(int i = 0; i < numInstances; ++i) {
				String str = "";
				str += "( ";
				for(PatternVertex v : vertexSet.values()) {
					str += "[ " + v.toMapping(i) + " ] ";
				}
				str += ")\n";
				mappings.add(str);
			}
		}
		return mappings;
	}*/

	/**
	 * Sets string representation of Graph.
	 * @param str String representation of Graph.
	 */
	public void setKeyString(String str) {
		keyStr = str;
	}
	
	/**
	 * Returns string representation of Graph.
	 * @return String representation of Graph.
	 */
	public String getKeyString() {
		if(keyStr == null) {
			return toString();
		}
		return keyStr;
	}

	/**
	 * Returns number of instances found of this pattern.
	 * @return Number of instances.
	 */
	public int getNumInstances() {
		return numInstances;
	}

	/**
	 * Returns allowed gap, i.e. when searching for this particular
	 * pattern, how many nodes can be skipped when a matching node
	 * isn't found.
	 * @return Allowed gap.
	 */
	public int getAllowedGap() {
		return allowedGap;
	}

	/**
	 * Sets allowed gap.
	 * @param gap Gap to be allowed for this pattern.
	 */
	public void setAllowedGap(int gap) {
		allowedGap = gap;
	}

	/**
	 * Checks if pattern contains any of the attributes in the input
	 * parameter.
	 * @param attributesToLookFor Attributes to verify.
	 * @return True if any of the attributes in the list is present in the 
	 * pattern, false otherwise.
	 */
	public boolean containsAnyAttributes(Vector<Integer> attributesToLookFor) {
		if(attributesToLookFor.isEmpty() == true) {
			return false;
		}
		//for(PatternVertex v : vertexSet.values()) {
        for(PatternEdge e : edgeSet.values()){
            PatternVertex v = e.getFromVertex();
			for(Integer attr : attributesToLookFor) {
				if(v.getAttribute(attr)) {
					return true;
				}
                v = e.getToVertex();
                if(v.getAttribute(attr))
                    return true;
			}
		}
		return false;
	}

    public LinkedHashMap<Integer, PatternVertex> createVertexSet () {

        LinkedHashMap<Integer, PatternVertex> vertexSeta = new LinkedHashMap<>();
        vertexSeta.put(entryVertex.getId(), entryVertex);
        vertexSeta.put(exitVertex.getId(), exitVertex);

        for (PatternEdge e: edgeSet.values()) {
            PatternVertex from = new PatternVertex(e.getFromVertex().getWeight(), e.getFromVertex().getId());
            from.setAttributes(e.getFromVertex().getAttributes());
            from.setAttrWeights(e.getFromVertex().getAttrWeights());
            vertexSeta.put(from.getId(), from);
            PatternVertex to = new PatternVertex(e.getToVertex().getWeight(), e.getToVertex().getId());
            to.setAttributes(e.getToVertex().getAttributes());
            to.setAttrWeights(e.getToVertex().getAttrWeights());
            vertexSeta.put(to.getId(), to);
        }
        return vertexSeta;
    }
}
