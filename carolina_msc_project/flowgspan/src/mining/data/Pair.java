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

/**
 * The Pair class represents generically any 2 values to be paired up together.
 * The idea is to use it as key in hash maps when the value in the hash map is 
 * an Edge, because Edges are uniquely identified by 2 values (from-node ID and
 * to-node ID).
 * @author cgomes
 *
 * @param <T1> Type of first value in pair.
 * @param <T2> Type of second value in pair.
 */
public class Pair<T1, T2> implements Comparable< Pair<T1, T2> > {
	/**
	 * First value in pair.
	 */
	protected final T1 first;
	/**
	 * Second value in pair.
	 */
	protected final T2 second;

	/**
	 * Simple constructor for Pair class.
	 */
	public Pair(T1 first, T2 second) {
		this.first   = first;
	    this.second = second;
	}
	/**
	 * Returns first value in pair.
	 * @return First value in pair.
	 */
	public T1 getFirst() {
		return first;
	}
	/**
	 * Returns second value in pair.
	 * @return Second value in pair.
	 */
	public T2 getSecond() {
		return second;
	}
	  
	/**
	 * Returns string representation of pair.
	 * @return String representation of pair.
	 */
	public String toString() {
		String str = "(" + first.toString() + ", " + second.toString() + ")";
		return str;
	}
	
	@Override
	/**
	 * Compares two pairs. They are equal if their values are respectively
	 * equal.
	 * @return 0 if both pairs are equal, 1 if the first pair is bigger than the second 
	 * pair, and -1 if the first pair is smaller than the second pair. 
	 */
	public int compareTo( Pair<T1, T2> otherPair ) { 
		if (otherPair != null) { 
			if (otherPair.equals(this)) { 
				return 0; 
		    } 
		    else if (otherPair.hashCode() > this.hashCode()) { 
		    	return 1;
		    }
		    else if (otherPair.hashCode() < this.hashCode()) { 
		    	  return -1;  
		    }
		}
		return -1;
	}
	
	@Override
	/**
	 * Returns whether two pairs are equal. They are equal if their values (first and second)
	 * are respectively the same.
	 * @return True if pairs are equal, false otherwise.
	 */
	public boolean equals(Object o) { 
	  if (this.getClass() == o.getClass()) { 
	    Pair<?, ?> otherPair = (Pair<?, ?>) o;
	    if (otherPair.getFirst().equals(first) && otherPair.getSecond().equals(second)) { 
	      return true;
	    }
	  }
	  return false;
	}
}
