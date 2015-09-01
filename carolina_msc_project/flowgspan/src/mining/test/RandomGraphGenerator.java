package mining.test;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.Vector;

public class RandomGraphGenerator {
	
	static int MAX_VERTEX_HOTNESS = 500;
	static int MAX_EDGE_FREQUENCY = 500;
	static int MAX_ATTRIBUTE_WEIGHT = 500;
	static int EDGE_COUNT = 0;
	
	public static void main(String[] args) {
		//if file name was provided...
		try {
			FileOutputStream fstream;
			fstream = new FileOutputStream(args[0]);
		    DataOutputStream outputStream = new DataOutputStream(fstream);
		    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

		    
		    //control parameters for EFG dataset generation.
		    int maxGraphNum = Integer.valueOf(args[1]);
		    int maxNumVertex = Integer.valueOf(args[2]);
		    int maxNumAttrs = Integer.valueOf(args[3]);
		    
		    Random gen = new Random();
		    
		    Integer numGraphs = maxGraphNum;//gen.nextInt(maxGraphNum) + 1;
		    bw.write(numGraphs.toString());
		    bw.newLine();
		    
		    //EFG generation.
		    for(int i = 0; i < numGraphs; ++i) {
		    	Vector<Integer> vertexSet = new Vector<Integer>();
		    	Integer numVertex = maxNumVertex;
		    	bw.write(numVertex.toString());
		    	bw.newLine();
		    	
		    	//vertex generation.
		    	for(int j = 0; j < numVertex; ++j) {
		    		Integer vertexIndex = j;
		    		vertexSet.add(vertexIndex);
		    		bw.write(vertexIndex.toString());
		    		bw.newLine();
		    		
		    		Integer vertexHotness = gen.nextInt(RandomGraphGenerator.MAX_VERTEX_HOTNESS) + 1;
		    		bw.write(vertexHotness.toString());
		    		bw.newLine();
		    		
		    		Integer numAttrs = gen.nextInt(maxNumAttrs) + 1;
		    		bw.write(numAttrs.toString());
		    		bw.newLine();
		    		
		    		//attribute generation for each vertex.
		    		for(int k = 0; k < numAttrs; ++k) {
		    			Integer attrIndex = k;
		    			Integer attrWeight = gen.nextInt(RandomGraphGenerator.MAX_ATTRIBUTE_WEIGHT) + 1;
		    			bw.write(attrIndex.toString() + " " + attrWeight.toString());
		    			bw.newLine();
		    		}
		    	}
		    	
		    	//edge generation.
		    	generateEdges(vertexSet.get(0), vertexSet.get(numVertex - 1), vertexSet, bw);
		    	bw.flush();
		    }
		    bw.close();
		}
		catch(Exception e) {
	    	e.printStackTrace();
	    }
	}

	private static void generateEdges(Integer entryVertex, Integer exitVertex,
			Vector<Integer> vertexSet, BufferedWriter bw) {
		Random gen = new Random();
		Vector<Integer> worklist = new Vector<Integer>();
		worklist.addAll(vertexSet);
		worklist.remove(exitVertex);
		
		Integer numEdges = 0;
		String[] edgeStream = new String[1];
		edgeStream[0] = "";
		Integer currVertex = entryVertex;
		
		while(worklist.isEmpty() == false) {
			currVertex = worklist.firstElement();
			worklist.remove(currVertex);
			
			if(worklist.size() == 0) {
				break;
			}
			int branches = gen.nextInt(50)%3;
			
			//needs at least 3 more nodes for branch region: join node, and 1 node for each branch
			if(branches == 0 && worklist.size() > 2) {
				Vector<Integer> leftBranch = new Vector<Integer>();
				Vector<Integer> rightBranch = new Vector<Integer>();
				
				int branchRegionSize = gen.nextInt(worklist.size() - 1);//can encompass whole worklist, except join node
				
				if(branchRegionSize < 2) {
					branchRegionSize = 2;
				}
				int leftBranchSize = gen.nextInt(branchRegionSize);
				int rightBranchSize = branchRegionSize - leftBranchSize;
				
				if(leftBranchSize == 0 || rightBranchSize == 0) {
					leftBranchSize = branchRegionSize/2;
					rightBranchSize = branchRegionSize/2;
				}
				int joinPointIdx = branchRegionSize; //next node bigger than all nodes in branch region, to guarantee that edges are forward
				Integer joinPoint = worklist.get(joinPointIdx);
				worklist.remove(joinPoint);
				
				if(worklist.size() == 0) {
					break;
				}
		
				//left branch path
				for(int j = 0; j < leftBranchSize; ++j) {
					if(j == 0) {
						if(j == leftBranchSize - 1) {
							writeEdge(currVertex, worklist.get(j), edgeStream);
							writeEdge(worklist.get(j), joinPoint, edgeStream);
							numEdges += 2;
						}
						else {
							numEdges += 1;
							writeEdge(currVertex, worklist.get(j), edgeStream);
						}
					}
					else if(j == leftBranchSize - 1){
						writeEdge(worklist.get(j-1), worklist.get(j), edgeStream);
						writeEdge(worklist.get(j), joinPoint, edgeStream);
						numEdges += 2;
					}
					else {
						numEdges += 1;
						writeEdge(worklist.get(j-1), worklist.get(j), edgeStream);
					}
					leftBranch.add(worklist.get(j));
				}
				
				//removing vertices belonging to left branch path
				for(int j = 0; j < leftBranch.size(); ++j) {
					worklist.remove(leftBranch.get(j));
				}
				
				//right branch path
				for(int j = 0; j < rightBranchSize; ++j) {
					if(j == 0) {
						if(j == leftBranchSize - 1) {
							writeEdge(currVertex, worklist.get(j), edgeStream);
							writeEdge(worklist.get(j), joinPoint, edgeStream);
							numEdges += 2;
						}
						else {
							numEdges += 1;
							writeEdge(currVertex, worklist.get(j), edgeStream);
						}
					}
					else if(j == rightBranchSize - 1){
						writeEdge(worklist.get(j-1), worklist.get(j), edgeStream);
						writeEdge(worklist.get(j), joinPoint, edgeStream);
						numEdges += 2;
					}
					else {
						numEdges += 1;
						writeEdge(worklist.get(j-1), worklist.get(j), edgeStream);
					}
					rightBranch.add(worklist.get(j));
				}
				
				//removing vertices belonging to right branch path
				for(int j = 0; j < rightBranch.size(); ++j) {
					worklist.remove(rightBranch.get(j));
				}
				//join point of branch should be next vertex to be analyzed, and
				//flow proceeds from it
				worklist.insertElementAt(joinPoint, 0);
			}
			else {
				//link current vertex with next vertex (no branch)
				if(worklist.size() > 0) {
					int toVertex = worklist.firstElement();
					numEdges += 1;
					writeEdge(currVertex, toVertex, edgeStream);
				}
			}
			
			int loops = gen.nextInt(50)%3;
			
			if(loops == 0) {
				if(currVertex > 0) {
					int toVertexIdx = gen.nextInt(currVertex);
					numEdges += 1;
					writeEdge(currVertex, toVertexIdx, edgeStream);
				}
			}
		}
		numEdges += 1;
		writeEdge(currVertex, exitVertex, edgeStream);
		writeEdgeStream(bw, edgeStream, numEdges);
		System.out.println("EDGE COUNT = " + RandomGraphGenerator.EDGE_COUNT + "\n");
	}

	private static void writeEdge(Integer fromEdge, Integer toEdge, String[] edgeStream) {
		Random gen = new Random();
		Integer edgeFreq = gen.nextInt(RandomGraphGenerator.MAX_EDGE_FREQUENCY) + 1;
		edgeStream[0] = edgeStream[0].concat(fromEdge.toString() + " " + toEdge.toString() + " " + edgeFreq.toString() + "\n");
		RandomGraphGenerator.EDGE_COUNT += 1;
	}
	
	private static void writeEdgeStream(BufferedWriter bw, String[] edgeStream, Integer numEdges) {
		try {
			edgeStream[0] = numEdges.toString().concat("\n" + edgeStream[0]);
			bw.write(edgeStream[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
