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

import java.util.Comparator;

/**
 * The PairComparator class is used to sort pairs according to some abstract rules.
 * @author cgomes
 *
 */
public class PairComparator<T1, T2> implements Comparator<Pair<T1, T2>>{

	@Override
	/**
	 * Compares two pairs.
	 */
	public int compare(Pair<T1, T2> p1, Pair<T1, T2> p2) {
		if(p1.getFirst() == p2.getFirst()) {
			if(p1.getSecond().hashCode() < p2.getSecond().hashCode()) {
				return -1;
			}
			else if(p1.getSecond().hashCode() > p2.getSecond().hashCode()) {
				return 1;
			}
			else {
				return 0;
			}
		}
		else if(p1.getFirst().hashCode() < p2.getFirst().hashCode()) {
			return -1;
		}
		else {
			return 1;
		}
	}
}
