package data;

import java.util.LinkedHashMap;

public class EFG {

	/**
	 * LinkedHashMap mapping an integer node ID and the node it corresponds to.
	 */
	private LinkedHashMap<Integer, Node> nodes;
	
	public EFG(){
		this.nodes = new LinkedHashMap<Integer, Node>();
	}
	
	/**
	 * Adds a node to the LinkedHashMap.
	 * @param id
	 * @param node
	 */
	public void addNode (int id, Node node){
		nodes.put(id, node);
	}

	public LinkedHashMap<Integer, Node> getNodes() {
		return nodes;
	}

	public void setNodes(LinkedHashMap<Integer, Node> nodes) {
		this.nodes = nodes;
	}
}
