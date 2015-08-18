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

import java.util.Collection;
import java.util.HashMap;

public class BasicBlock {
	/**
	 * First instruction of a basic block.
	 */
	EFGVertex first;
	/**
	 * Last instruction of a basic block.
	 */
	EFGVertex last;
	
	/**
	 * Execution Flow Graph (EFG) that the instructions in this basic block
	 * are a part of.
	 */
	ExecutionFlowGraph parentGraph;
	
	/**
	 * All instructions in the basic block.
	 */
	HashMap<Integer, EFGVertex> instructions;
	
	/**
	 * Basic block's unique identifier.
	 */
	int id;
	
	/**
	 * Basic block's starting address (i.e. address of the first instruction in it).
	 * Can also be used as an unique identifier.
	 */
	int startAddress;
	
	/**
	 * Frequency of the edge going into the basic block. All edges in-between
	 * basic block instructions have this same frequency, as does the basic block's
	 * outgoing edge.
	 */
	double freq;
	
	boolean trueLastWasFound;
	
	/**
	 * Creates a new basic block.
	 * @param id The integer ID of this block.
	 * @param freq The frequency with which this block was executed.
	 */
	public BasicBlock(int id, int startAddr, double freq, ExecutionFlowGraph parentGraph) {
		this.id = id;
		startAddress = startAddr;
		instructions = new HashMap<Integer, EFGVertex>();
		this.freq = freq;
		this.parentGraph = parentGraph;
		trueLastWasFound = false;
		first = last = null;
	}

	/**
	 * Returns number of basic block instructions.
	 * @return Number of instructions in basic block.
	 */
	public int getInstructionCount() {
		return instructions.size() + 2;
	}
	
	/**
	 * Returns first basic block instruction.
	 * @return The first instruction in the basic block.
	 */
	public EFGVertex getFirst() {
		return first;
	}
	
	/**
	 * Returns the last instruction in the basic block.
	 * @return The last instruction in the basic block.
	 */
	public EFGVertex getLast() {
		return last;
	}

	/**
	 * Returns the basic block's ID.
	 * @return Basic block's ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the basic block's starting address (i.e. the address of its first
	 * instruction).
	 * @return The starting address of the basic block.
	 */
	public int getAddress() {
		return startAddress;
	}
	
	/**
	 * Returns the incoming edge frequency for this basic block.
	 * @return Incoming edge frequency.
	 */
	public double getFreq() {
		return freq;
	}
	
	/**
	 * Returns whether two basic blocks are equal. They must have
	 * same ID and same starting address.
	 * @return True if basic blocks are equal; false otherwise.
	 */
	public boolean equals(Object other) {
		if(!(other instanceof BasicBlock))
			return false;
		
		BasicBlock b = (BasicBlock) other;
		
		return b.startAddress == startAddress && b.id == id;
	}
	
	/**
	 * String version of basic block.
	 * @return String representation of basic block.
	 */
	public String toString() {
		return id + " (" + startAddress+")";
	}
	
	/**
	 * Adds a new instruction to the end of the basic block. If the basic
	 * block is empty it will be added as the only instruction in the block.
	 * @param toAdd The instruction to add.
	 */
	public void addVertex(EFGVertex toAdd) {
		instructions.put(toAdd.getId(), toAdd);
		parentGraph.insertVertex(toAdd);
		toAdd.setBBN(id);
		// If there are no nodes in this BB, add this as the first and only.
		if(first == null) {
			first = toAdd;
		}
		
		// Add this node as the new last one.
		if(trueLastWasFound == false) {
			last = toAdd;
		}
	}
	
	/**
	 * Connects all instructions in the basic block, by creating edges between them
	 * and inserting such edges in the EFG that the instructions in the basic block
	 * correspond to.
	 * @param freq Frequency of the edges to be created in-between instructions.
	 */
	public void connect(double freq) {
		EFGVertex iterLast = first;
		for(Integer address : instructions.keySet()) {
			EFGVertex current = instructions.get(address);
			EFGEdge currEdge = new EFGEdge(iterLast, current, freq);
			parentGraph.insertEdge(currEdge);
			
			iterLast = current;
		}
		
		EFGEdge lastEdge = new EFGEdge(iterLast, last, freq);
		parentGraph.insertEdge(lastEdge);
	}

	/**
	 * Returns the set of instructions in the basic block.
	 * @return Set of instructions in basic block.
	 */
	public Collection<EFGVertex> getInstructions() {
		return instructions.values();
	}

	public void addLastInstruction(EFGVertex toAdd) {
		parentGraph.insertVertex(toAdd);
		toAdd.setBBN(id);
		last = toAdd;
		trueLastWasFound = true;
		
		if(first == null) {
			first = toAdd;
		}
	}

	public void addFirstInstruction(EFGVertex toAdd) {
		parentGraph.insertVertex(toAdd);
		toAdd.setBBN(id);
		first = toAdd;
		
		if(trueLastWasFound == false && last == null) {
			last = toAdd;
		}
	}
	
	public void removeInstruction(int address) {
		EFGVertex toRemove = instructions.get(address);
		if(toRemove != null) {
			instructions.remove(address);
		}
	}
}
