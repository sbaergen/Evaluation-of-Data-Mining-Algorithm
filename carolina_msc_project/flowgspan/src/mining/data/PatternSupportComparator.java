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
import java.util.Map;
import java.util.Vector;

public class PatternSupportComparator implements Comparator<Integer>{

	/**
	 * Reference to sgMap of FlowGSpan class.
	 */
	Map<String, Vector<Double>> patternMap;
	/**
	 * Reference to resultSet of FlowGSpan class.
	 */
	Vector<String> patternSet;
	
	/**
	 * Constructor. Just sets the necessary references to the structures that we use to obtain
	 * information on the support value of patterns being sorted.
	 * @param codeSet Reference to resultSet of FlowGSpan class.
	 * @param map Reference to sgMap of FlowGSpan class.
	 */
	public PatternSupportComparator(Vector<String> codeSet, Map<String, Vector<Double>> map) {
		patternSet = codeSet;
		patternMap = map;
	}
	@Override
	/**
	 * Determines the order in which two integers should be listed. They are indices
	 * into the resultSet structure in FlowGSpan class. In order to determine the listing
	 * order, the support values of the patterns they reference are used.
	 */
	public int compare(Integer patternId1, Integer patternId2) {
		String patternCode1 = patternSet.get(patternId1);
		String patternCode2 = patternSet.get(patternId2);
		
		double weightSupport = patternMap.get(patternCode1).get(0);
		double freqSupport = patternMap.get(patternCode1).get(1);
		double support1 = Math.max(weightSupport, freqSupport);
		weightSupport = patternMap.get(patternCode2).get(0);
		freqSupport = patternMap.get(patternCode2).get(1);
		double support2 = Math.max(weightSupport, freqSupport);
		
		if(support1 < support2) {
			return 1;
		}
		else if(support1 > support2) {
			return -1;
		}
		return 0;
	}
}
