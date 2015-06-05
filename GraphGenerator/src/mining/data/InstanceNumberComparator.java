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
import java.util.Vector;

public class InstanceNumberComparator<T> implements Comparator<PatternGraph>{
	Vector<PatternGraph> patterns;
	
	@Override
	public int compare(PatternGraph g0, PatternGraph g1) {
		if(g0.getNumInstances() < g1.getNumInstances()) {
			return 1;
		}
		else if(g0.getNumInstances() > g1.getNumInstances()) {
			return -1;
		}
		return 0;
	}

	public InstanceNumberComparator(Vector<PatternGraph> patterns) {
		this.patterns = patterns;
	}
}

