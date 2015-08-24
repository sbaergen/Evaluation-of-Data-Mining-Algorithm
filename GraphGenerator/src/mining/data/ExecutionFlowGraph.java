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
import java.util.List;
import java.util.Vector;

public class ExecutionFlowGraph {
	/**
	 * Set of all edges contained in the graph. The edges are easily
	 * identified by a pair of IDs, which are the IDs of the two nodes
	 * the Edge connects.
	 */
    LinkedHashMap<Pair<Integer, Integer>, EFGEdge> edgeSet;
    /**
     * Set of nodes contained in the graph. They are identified by
     * their unique ID.
     */
	LinkedHashMap<Integer, EFGVertex> vertexSet;
	
	/**
	 * Graph's entry node.
	 */
	EFGVertex entryVertex;
	/**
	 * Graph's exit node (if any).
	 */
	EFGVertex exitVertex;
	
	/**
	 * Number of attributes of the node with maximum number of attributes in Graph.
	 */
	int maxAttrNum;
	
	/**
	 * Sum of all edge frequencies of this Graph.
	 */
	double freq;
	
	/**
	 * Edge that goes into Graph's entry node.
	 */
	EFGEdge entryEdge;
	
	/**
	 * Weight of the node in Graph with minimum weight.
	 */
	double minVertexWeight;
	
	/**
	 * Weight of the node in Graph with maximum weight.
	 */
	double maxVertexWeight;
	
	/**
	 * Frequency of the edge in Graph with minimum frequency.
	 */
	double minEdgeFreq;
	
	/**
	 * Frequency of the edge in Graph with maximum frequency.
	 */
    double maxEdgeFreq;
    
    /**
     * Sum of the weights of all attributes over all nodes in Graph.
     */
	double sumAttrWeights;

	EFGVertex dummy;

	boolean memorize;
	/**
	 * Graph constructor. Just variable initialization.
	 */
	public ExecutionFlowGraph() {
		edgeSet = new LinkedHashMap<Pair<Integer, Integer>, EFGEdge>();
        memorize = true;
        if (memorize)
		    vertexSet = new LinkedHashMap<Integer, EFGVertex>();
		maxAttrNum = 0;
		freq = 0f;
		entryVertex = null;
		exitVertex = null;
		entryEdge = null;
		dummy = new EFGVertex(0, -1);
		minVertexWeight = Double.MAX_VALUE;
		maxVertexWeight = 0;
		minEdgeFreq = Double.MAX_VALUE;
	    maxEdgeFreq = 0;
		sumAttrWeights = 0;
	}
	
	/**
	 * Inserts an edge in the Graph. Note that this function works under the 
	 * supposition that edges being added had their from-node and to-node already 
	 * created and added to them.
	 * @param e Edge to be inserted in Graph.
	 */
	
	public void insertEdge(EFGEdge e) {
		int fromId = e.getFromVertex().getId();
		int toId = e.getToVertex().getId();
		edgeSet.put(new Pair<Integer, Integer>(fromId, toId), e);
		if (toId != -1) {
			e.getFromVertex().setOutEdge(e);
			e.getToVertex().setIncEdge(e);
		}
	}
	
	/**
	 * Inserts node in Graph.
	 * @param v Node to be inserted.
	 */
	public void insertVertex(EFGVertex v) {
        if (memorize)
		    vertexSet.put(v.getId(), v);
        else
		    insertEdge(new EFGEdge(v, dummy, 0));
		if(maxAttrNum < v.getAttrWeights().size()) {
			maxAttrNum = v.getAttrWeights().size(); 
		}
	}
	
	/**
	 * Returns node that has a certain ID.
	 * @param vertexId ID of node to be returned.
	 * @return Returned node.
	 */
	public EFGVertex getVertex(int vertexId) {
        if (memorize)
		    return vertexSet.get(vertexId);
		return createVertexSet().get(vertexId);
	}
	
	/**
	 * Returns edge set.
	 * @return Edge set of this Graph.
	 */
	public LinkedHashMap<Pair<Integer, Integer>, EFGEdge> getEdgeSet() {
		if (memorize)
            return edgeSet;
        return removeDummyEdges();
	}
	
	/**
	 * Returns node set.
	 * @return Node set of this Graph.
	 */
	public LinkedHashMap<Integer, EFGVertex> getVertexSet() {
        if (memorize)
		    return vertexSet;
		return createVertexSet();
	}
	
	/**
	 * Sets exit node.
	 * @param v Exit node for this Graph.
	 */
	public void setExitVertex(EFGVertex v) {
		exitVertex = v;
	}
	
	/**
	 * Returns exit node.
	 * @return Exit node.
	 */
	public EFGVertex getExitVertex() {
		return exitVertex;
	} 
	
	/**
	 * Sets entry node of this Graph.
	 * @param v Entry node.
	 */
	public void setEntryVertex(EFGVertex v) {
		entryVertex = v;
	}
	
	/**
	 * Returns entry node.
	 * @return Entry node.
	 */
	public EFGVertex getEntryVertex() {
		return entryVertex;
	}
	
	/**
	 * Returns maximum number of attributes found in a single node of this Graph.
	 * @return Maximum number of attributes found in a single Graph node.
	 */
	public int getMaxAttrNum() {
		return maxAttrNum;
	}
	
	/**
	 * Sets total frequency value of this Graph.
	 * @param freq Total frequency value.
	 */
	public void setFreq(double freq) {
		this.freq = freq;
	}
	
	/**
	 * Returns total frequency value of this Graph.
	 * @return Total frequency value.
	 */
	public Double getFreq() {
		return freq;
	}

	/**
	 * Returns minimum attribute weight of a node indicated by an ID.
	 * @param idx ID of node.
	 * @return Minimum attribute weight of node.
	 */
	/*public double getMinAttrWeight(int idx) {
		return vertexSet.get(idx).getMinAttrWeight();
	}*/
	
	/**
	 * Sets the entry edge of this Graph. An entry edge is not present in a Graph's edge set,
	 * for it is just a dummy edge to account for the incoming flow to the entry node.
	 * @param dummy Dummy from-node for the entry edge.
	 * @param head Entry node of Graph.
	 * @param freq Frequency of entry edge.
	 */
	public void setEntryEdge(EFGVertex dummy, EFGVertex head, double freq) {
		//sanity check
		if(head == entryVertex) {
			entryEdge = new EFGEdge(dummy, head, freq);
			head.setIncEdge(entryEdge);
		}
	}
		
	/**
	 * Returns string representation of Graph.
	 * @return String representation of Graph.
	 */
	public String toString() {
		String str = "";
        if (!memorize)
		    vertexSet = createVertexSet();

		if(edgeSet.size() > 0) {
			List<Pair<Integer, Integer>> keyList = new Vector<Pair<Integer, Integer>>();
			keyList.addAll(edgeSet.keySet());
			PairComparator<Integer, Integer> pairComp = new PairComparator<Integer, Integer>();
			Collections.sort(keyList, pairComp);
			
			for(Pair<Integer, Integer> pair : keyList) {
				EFGEdge e = edgeSet.get(pair);
				str += e.toString() + "\n";
			}
		}
		else {
			str += "( ";
			for(EFGVertex v : vertexSet.values()) {
				str += "[ " + Integer.toHexString(v.getId()) + ": " + v.toCode() + " ] ";
			}
			str += ")\n";
		}
		return str;
	}
	
	/**
	 * Finds entry and exist nodes of a Graph, in case they exist.
	 */
	public void findEntryExitVertices() {
		if(entryEdge != null) {
			setEntryVertex(entryEdge.getToVertex());
		}
		else {
			for(EFGVertex v : vertexSet.values()) {
				if(v.getInEdgeCount() == 0) {
					setEntryVertex(v);
					break;
				}
			}
		}
		for(EFGVertex v : vertexSet.values()) {
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
	public EFGEdge hasEdge(int fromVertexId, int toVertexId) {
        if (memorize) {
            for (EFGEdge e : edgeSet.values()) {
                if (e.getFromVertex().getId() == fromVertexId && e.getToVertex().getId() == toVertexId) {
                    return e;
                }
            }

            return null;
        } else {
            for (EFGEdge e : removeDummyEdges().values()) {
                if (e.getFromVertex().getId() == fromVertexId && e.getToVertex().getId() == toVertexId) {
                    return e;
                }
            }

            return null;
        }
	}
	
	/**
	 * Function to find whether there is an edge between 2 nodes, even 
	 * if there are dummy nodes in-between them.
	 * @param fromVertexId From-node of edge being looked for.
	 * @param toVertexId To-node of edge being looked for.
	 * @return Frequency of the edge, if it was found, or 0 otherwise.
	 */
	/*public double findEdge(long fromVertexId, long toVertexId) {
		Vertex fromVertex = null;
		
		try {
			fromVertex = vertexSet.get(fromVertexId);
		} catch(ArrayIndexOutOfBoundsException e) {
			return 0;
		}
		
		double freq = 0;
		Vector<Vertex> queue = new Vector<Vertex>();
		queue.addAll(fromVertex.getChildren());
		
		while(queue.isEmpty() == false) {
			Vertex v = queue.get(0);
			queue.remove(0);
			
			if(v.isDummy() == true) {
				List<Vertex> children = v.getForwardChildren();
				
				for(Vertex child : children) {
					if(child.getId() == toVertexId) {
						Edge e = hasEdge(v.getId(), toVertexId);
						if(e != null) {
							double edgeFreq = e.getFrequency();
							freq += edgeFreq;
						}
					}
					if(child.isDummy() == false) {
						children.remove(child);
					}
				}
				queue.addAll(children);
			}
			else if(v.getId() == toVertexId) {
				Edge e = hasEdge(fromVertexId, toVertexId);
				if(e != null) {
					double edgeFreq = e.getFrequency();
					freq += edgeFreq;
				}
			}
		}
		
		return freq;
	}*/
	
	/**
	 * Calculates sum of edge frequencies in Graph.
	 * @return Sum of edge frequencies in Graph.
	 */
	public double calculateInternalFreq() {
		if(freq > 0) {
			return freq;
		}
        if (memorize) {
            for (EFGEdge e : edgeSet.values()) {
                freq += e.getFrequency();
            }
        } else {
            for (EFGEdge e : removeDummyEdges().values()) {
                freq += e.getFrequency();
            }
        }
		
		if(entryEdge != null) {
			freq += entryEdge.getFrequency();
		}
		return freq;
	}
	
	/**
	 * Calculates and returns total weight of Graph (sum of all node weights).
	 * @return Total Graph weight.
	 */
	public double getTotalWeight() {
		double hotness = 0;
        if (!memorize)
		    vertexSet = createVertexSet();

		for(EFGVertex v : vertexSet.values()) {
			hotness += v.getWeight();
		}
		return hotness;
	}

	public void setAllNodesDiscardable() {
        if (!memorize)
            vertexSet = createVertexSet();

		for(EFGVertex v : vertexSet.values()) {
			v.setDiscardable(true);
		}
	}

	public void setNonDiscardableNode(int vertexId) {
        if (!memorize)
            vertexSet = createVertexSet();
		vertexSet.get(vertexId).updateDiscardableRoot();
	}

	public void setAllEdgesDiscardable() {
        if (memorize) {
            for (EFGEdge e : edgeSet.values()) {
                e.setDiscardable(true);
            }
        } else {
            for (EFGEdge e : removeDummyEdges().values()) {
                e.setDiscardable(true);
            }
        }
	}

	public void removeDiscardableEdges() {
		Vector<Pair<Integer, Integer>> allEdgeKeys = new Vector<Pair<Integer,Integer>>(edgeSet.keySet());
		//DEBUG
		int count = 0;
		//end DEBUG
		for(Pair<Integer, Integer> keyPair : allEdgeKeys) {
			EFGEdge e = edgeSet.get(keyPair);
			
			if(e.isDiscardable() == true) {
				edgeSet.remove(keyPair);
				//DEBUG
				++count;
				//end DEBUG
			}
		}
		//DEBUG
		System.out.println("Removed edges from EFG: " + count + " of " + edgeSet.size());
		//end DEBUG
	}

	public void removeDiscardableNodes() {
        if (!memorize)
            vertexSet = createVertexSet();
		Vector<Integer> allVertexKeys = new Vector<Integer>(vertexSet.keySet());
		//DEBUG
		int count = 0;
		//end DEBUG
		for(Integer vertexId : allVertexKeys) {
			EFGVertex v = vertexSet.get(vertexId);
			
			if(v.isDiscardable() == true) {
				vertexSet.remove(vertexId);
				//DEBUG
				++count;
				//end DEBUG
			}
		}
		//DEBUG
		System.out.println("Removed nodes from EFG: " + count +" of " + vertexSet.size());
		//end DEBUG
	}
	public LinkedHashMap<Integer, EFGVertex> createVertexSet() {
		LinkedHashMap<Integer, EFGVertex> vertexSet = new LinkedHashMap<Integer, EFGVertex>();
		vertexSet.put(entryVertex.getId(), entryVertex);
		if (exitVertex != null && exitVertex.getId() != -1)
			vertexSet.put(exitVertex.getId(), exitVertex);
		for (EFGEdge e: edgeSet.values()) {
			if (e.getFromVertex().getId() != -1)
				vertexSet.put(e.getFromVertex().getId(), e.getFromVertex());
			if (e.getToVertex().getId() != -1)
				vertexSet.put(e.getToVertex().getId(), e.getToVertex());
		}
		return vertexSet;
	}

	public LinkedHashMap<Pair<Integer, Integer>, EFGEdge> removeDummyEdges() {
		LinkedHashMap<Pair<Integer, Integer>, EFGEdge> newEdgeSet = new LinkedHashMap<Pair<Integer, Integer>, EFGEdge>();
		for (Pair<Integer, Integer> p: edgeSet.keySet()){
			EFGEdge e = edgeSet.get(p);
			if (e.getToVertex().getId() != -1)
				newEdgeSet.put(p, e);
		}
		//Exception e = new RuntimeException();
		//e.printStackTrace();
		//System.out.println(edgeSet);
		//System.out.println(newEdgeSet);
		return newEdgeSet;
	}
}
