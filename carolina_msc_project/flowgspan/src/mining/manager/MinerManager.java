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
 */package mining.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import mining.algorithm.FlowGSpanController;
import mining.algorithm.ReturnInfo;
import mining.data.DataSet;
import mining.data.EFGEdge;
import mining.data.EFGVertex;
import mining.data.ExecutionFlowGraph;

/**
 * @author cgomes
 *
 */
public class MinerManager {

	public static boolean readFromDB = false; //Changed from true to false to avoid database
	public static BufferedWriter FILE_WRITER;
	
	/**
	 * @param args
	 */
    public static ReturnInfo main (String[] args) {
	//public static void main(String[] args) {
		FileOutputStream fstream;
		long startTime = System.currentTimeMillis();

		try {
			fstream = new FileOutputStream(args[2]);
			 DataOutputStream outputStream = new DataOutputStream(fstream);
			 FILE_WRITER = new BufferedWriter(new OutputStreamWriter(outputStream));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MinerManager pm = new MinerManager();
        ReturnInfo info = pm.readInputFromFile(args);
		long endTime = System.currentTimeMillis();
		System.out.println(endTime-startTime + "ms");
        return info;
	}
	
	private void readInputFromDB(String[] args) {
		// Read the configuration file
		List<String> arguments = readFile(args[0]);

		// DB connection info
		String dbAddress = "", dbName = "", port = "", username = "", prefix = "";
		// Mining parameters
		boolean useBytecodes = false;
		double minSupport = 0;
		double maxNodes = 0;
		
		// Parse the configuration file
		try {
			dbAddress = arguments.get(0);
			dbName = arguments.get(1);
			port = arguments.get(2);
			username = arguments.get(3);
			prefix = arguments.get(4);
			useBytecodes = Boolean.parseBoolean(arguments.get(5));
			
			FlowGSpanController.MAX_ATTRIBUTES_TOTAL = Integer.valueOf(arguments.get(6));
			FlowGSpanController.MIN_METHOD_HOTNESS = Double.valueOf(arguments.get(7));
			FlowGSpanController.MAX_NEW_FWD_EDGE_ADDITIONS = Integer.valueOf(arguments.get(8));
			FlowGSpanController.MAX_NEW_BACK_EDGE_ADDITIONS = Integer.valueOf(arguments.get(9));
			FlowGSpanController.GAP = Integer.valueOf(arguments.get(10));
			FlowGSpanController.NUM_DB_THREADS = Integer.valueOf(arguments.get(11));
			FlowGSpanController.NUM_FGSPAN_THREADS = Integer.valueOf(arguments.get(12));
			
			minSupport = Double.valueOf(arguments.get(13));
			maxNodes = Double.valueOf(arguments.get(14));
			
			if(FlowGSpanController.MAX_NEW_FWD_EDGE_ADDITIONS > 0) {
				FlowGSpanController.LIMIT_FWD_EDGE_ADDITIONS = true;
			}
			else {
				FlowGSpanController.LIMIT_FWD_EDGE_ADDITIONS = false;
			}
			
			if(FlowGSpanController.MAX_NEW_BACK_EDGE_ADDITIONS > 0) {
				FlowGSpanController.LIMIT_BACK_EDGE_ADDITIONS = true;
			}
			else {
				FlowGSpanController.LIMIT_BACK_EDGE_ADDITIONS = false;
			}
		} catch (NumberFormatException e) {
			System.err.println("Malformed configuration file");
			System.exit(1);
		}
		
		List<String> hwCounters = readFile(args[1]);
		
		// Get DB p/w from user
		String password = "";
		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter password:");
		try {
			password = consoleReader.readLine();
		} catch (IOException e) {
			System.err.println("invalid input");
			System.exit(1);
		}
			
		// Connect to DB
		Vector<DBLoader> loaders = new Vector<DBLoader>();
		DBLoader origLoader = null;
		Vector<Integer> methodList = null;
		double tickCount = 0;
		double totalFreq = 0;
		try {
			origLoader = new DBLoader(dbAddress, dbName, port, username, password, prefix, hwCounters);
			origLoader.threadId = 0;
			loaders.add(origLoader);
			methodList = origLoader.getIds();
			tickCount = origLoader.getTotalTicks();
			totalFreq = origLoader.getFreqTotal();
		} catch(SQLException e) {
			System.err.println("Could not connect to database:");
			e.printStackTrace();
			System.err.println("Aborting.");
			System.exit(1);
		}
		
		int numMethods = methodList.size();
		DataSet dataset = new DataSet();
		dataset.setUsesBytecodes(useBytecodes);
		
		//DEBUG
		System.out.println("Num of methods: " + numMethods);
		//end DEBUG
		
		Long startTime = System.currentTimeMillis();
		
		origLoader.setDataset(new DataSet());
		origLoader.setBytecodeUsage(useBytecodes);
		origLoader.setStartMethodIndex(0);
		
		if(FlowGSpanController.NUM_DB_THREADS == 0) {
			origLoader.setNumMethodsToLoad(numMethods);
			origLoader.run();
		}
		else {
			int blockSize = numMethods/FlowGSpanController.NUM_DB_THREADS;
			int extraBlockSize = numMethods%FlowGSpanController.NUM_DB_THREADS;
			//DEBUG
			//System.out.println("Number of methods in thread 0: " + (blockSize + extraBlockSize));
			//end DEBUG
			
			if(FlowGSpanController.NUM_DB_THREADS > 1) {
				origLoader.setNumMethodsToLoad(blockSize + extraBlockSize);
			}
			else {
				origLoader.setNumMethodsToLoad(numMethods);
			}
			
			ExecutorService executor = Executors.newFixedThreadPool(FlowGSpanController.NUM_DB_THREADS);
			
			Runnable worker0 = origLoader;
			executor.execute(worker0);
			
			for (int i = 1; i < FlowGSpanController.NUM_DB_THREADS; i++) {
				DBLoader db;
				try {
					db = new DBLoader(dbAddress, dbName, port, username, password, prefix, hwCounters);
				
					db.threadId = i;
					loaders.add(db);
					db.setIds(methodList);
					db.setTickCount(tickCount);
					db.addTotalFreq(totalFreq);
					db.setDataset(new DataSet());
					db.setBytecodeUsage(useBytecodes);
					
					int startIdx = i*(blockSize + extraBlockSize) - (i-1)*extraBlockSize;
					db.setStartMethodIndex(startIdx);	
					db.setNumMethodsToLoad(blockSize);
					
					//DEBUG
					//System.out.println("Start index for thread " + i + ": " + startIdx);
					//end DEBUG
					
					Runnable worker = db;
					executor.execute(worker);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			// This will make the executor accept no new threads
			// and finish all existing threads in the queue.
			executor.shutdown();
			try {
				executor.awaitTermination(24, TimeUnit.HOURS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			//System.out.println("Finished all threads");
		}
		
		dataset.putAll(origLoader.getDataSet());
		
		for(int i = 1; i < loaders.size(); ++i) {
			DBLoader db = loaders.get(i);
			dataset.putAll(db.getDataSet());
		}
		
		//DEBUG
		System.out.println("Dataset size: " + dataset.size()+"\n");
		//end DEBUG
		
		writeOutputFileHeader(minSupport, maxNodes, dataset.size());
		
		FlowGSpanController fgspanController = new FlowGSpanController(dataset, tickCount, 
				totalFreq, minSupport, maxNodes, FlowGSpanController.NUM_FGSPAN_THREADS);
		Long startMiningTime = System.currentTimeMillis();
	    fgspanController.run();
	    Long endTime = System.currentTimeMillis(); 
	    
	    fgspanController.writeResults();
	    String numSubgraphs = "\n\nNumber of Frequent Subgraphs: " + FlowGSpanController.NUMBER_SUBGRAPHS;
	    
	    Long totalTime = endTime - startTime;
	    Long miningTime = endTime - startMiningTime;
	    String totalTimeMeasurement = "\n\nTotal runtime: " + totalTime.toString();
	    String miningTimeMeasurement = "\n\nMining time: " + miningTime.toString();
	    writeOutputToFile(numSubgraphs + totalTimeMeasurement + miningTimeMeasurement);
	    
	    try {
			FILE_WRITER.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Read data from the specified file
	 * 
	 * @param fileName The file to read from
	 * @return An array of each line of the file, unparsed
	 */
	private List<String> readFile(String fileName) {
		List<String> arguments = new ArrayList<String>();
		try {
			BufferedReader reader = 
				new BufferedReader(new FileReader(new File(fileName)));
			String line = reader.readLine();
			int index = 0;
			while(line != null) {
				line = line.trim();
				arguments.add(line);
				++index;
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Unable to read configuration information");
			System.exit(1);
		}
		return arguments;
	}

	private ReturnInfo readInputFromFile(String[] args) {
		//if file name was provided...
        ReturnInfo info = new ReturnInfo();
		try {
			// Read the configuration file
			List<String> arguments = readFile(args[0]);
			
			// Mining parameters
			boolean useBytecodes = false;
			double minSupport = 0;
			double maxNodes = 0;
			
			// Parse the configuration file
			try {
				FlowGSpanController.MAX_ATTRIBUTES_TOTAL = Integer.valueOf(arguments.get(6));
				FlowGSpanController.MIN_METHOD_HOTNESS = Double.valueOf(arguments.get(7));
				FlowGSpanController.MAX_NEW_FWD_EDGE_ADDITIONS = Integer.valueOf(arguments.get(8));
				FlowGSpanController.MAX_NEW_BACK_EDGE_ADDITIONS = Integer.valueOf(arguments.get(9));
				FlowGSpanController.GAP = Integer.valueOf(arguments.get(10));
				FlowGSpanController.NUM_DB_THREADS = Integer.valueOf(arguments.get(11));
				FlowGSpanController.NUM_FGSPAN_THREADS = Integer.valueOf(arguments.get(12));
				
				minSupport = Double.valueOf(arguments.get(13));
				maxNodes = Double.valueOf(arguments.get(14));
				
				if(FlowGSpanController.MAX_NEW_FWD_EDGE_ADDITIONS > 0) {
					FlowGSpanController.LIMIT_FWD_EDGE_ADDITIONS = true;
				}
				else {
					FlowGSpanController.LIMIT_FWD_EDGE_ADDITIONS = false;
				}
				
				if(FlowGSpanController.MAX_NEW_BACK_EDGE_ADDITIONS > 0) {
					FlowGSpanController.LIMIT_BACK_EDGE_ADDITIONS = true;
				}
				else {
					FlowGSpanController.LIMIT_BACK_EDGE_ADDITIONS = false;
				}
				useBytecodes = Boolean.parseBoolean(arguments.get(5));
				//minSupport = Double.valueOf(arguments.get(13)); //Changed from get(6)
				//maxNodes = Double.valueOf(arguments.get(14)); //Changed from get(7)
			} catch (NumberFormatException e) {
				System.err.println("Malformed configuration file");
				System.exit(1);
			}
			
			List<String> hwCounters = readFile(args[1]);
				
			FileInputStream fstream;
			fstream = new FileInputStream(args[3]);
		    DataInputStream inputStream = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

		    //reading number of transactions (EFGs) in input file
		    int graphNum = Integer.valueOf(br.readLine());
		    DataSet dataset = new DataSet();
		    
		    //DEBUG
    		System.out.println("There is(are)  " + graphNum + " EFG(s) in this dataset."); //Changed to println from print
    		//end DEBUG
		    while(graphNum > 0) {
		    	 //DEBUG
	    		//System.out.print("\n\nEFG: \n\n");
	    		//end DEBUG
		    	//reading vertex info
		    	int vertexNum =  Integer.valueOf(br.readLine());//number of vertices
		    	ExecutionFlowGraph efg = new ExecutionFlowGraph();
		        
		    	while(vertexNum > 0) {
		    		long vertexId = Long.valueOf(br.readLine());
		    		double hotness = Double.valueOf(br.readLine());
		    		int attrNum = Integer.valueOf(br.readLine());//number of attributes
		    		EFGVertex v = new EFGVertex(hotness, vertexId);
		    		//DEBUG
		    		//System.out.print("EFGVertex " + vertexId + " has " + attrNum + " attribute(s)\n"); //Changed from vertexIndex to VertexId
		    		//end DEBUG
		    		while(attrNum > 0) {
		    			String tempStr = br.readLine();
		    			String[] attrValStr = tempStr.split(" "); //tuple (attribute, weight) split by space on file
		    			//int attr = Integer.valueOf(attrValStr[0]); //1st part of string: attribute order in bitvector
		    			double weight = Double.valueOf(attrValStr[1]); //2nd string: weight of attribute
		    			v.setAttribute(FlowGSpanController.addAttributeToTable(attrValStr[0]), weight); //insert attr into vertex
		    			//DEBUG
		    			//System.out.println("(" + attrValStr[0] + ", " + weight + ") ");// changed from attr to attrValStr[0]
		    			//end DEBUG
		    			--attrNum;
		    		}
		    		if(vertexId == 0) {
		    			efg.setEntryVertex(v);
		    		}
		    		else if(vertexNum == 1) {
		    			efg.setExitVertex(v);
		    		}
		    		efg.insertVertex(v); //insert vertex into current EFG
		    		--vertexNum;
		    	}
		    
		    	int edgeNum = Integer.valueOf(br.readLine());//number of edges
		    	//DEBUG
		    	//System.out.println("\n\nNumber of edges: " + edgeNum + "\n\n");
		    	//end DEBUG
		    	while(edgeNum > 0) {
		    		String tempStr = br.readLine();
		    		String[] edgeStr = tempStr.split(" "); //tuple (attribute, weight) split by space on file
		    		long fromEFGVertex = Long.valueOf(edgeStr[0]); //1st part of string: attribute order in bitvector
		    		long toEFGVertex = Long.valueOf(edgeStr[1]); 
		    		double edgeFreq = Double.valueOf(edgeStr[2]);//2nd string: weight of attribute
		    		//DEBUG
		    		//System.out.println("( " + fromEFGVertex + ", " + toEFGVertex + ", " + edgeFreq + ") ");
		    		//end DEBUG
		    		EFGEdge e = new EFGEdge(efg.getVertex(fromEFGVertex), efg.getVertex(toEFGVertex), edgeFreq);
		    		efg.insertEdge(e);
		    		--edgeNum;
		    	}
		    	--graphNum;
		    	//DEBUG
		    	//efg.print();
		    	//System.out.println("\n\nNormalized:");
		    	//efg.normalize();
		    	//efg.print();
		    	//end DEBUG
		    	dataset.put(graphNum, efg);
		    }
		    
		    writeOutputFileHeader(minSupport, maxNodes, dataset.size());
		    
		    FlowGSpanController fgspanController = new FlowGSpanController(dataset, minSupport, 
		    		maxNodes, FlowGSpanController.NUM_FGSPAN_THREADS);
			Long startTime = System.currentTimeMillis();
		    fgspanController.run();
		    Long endTime = System.currentTimeMillis(); 
		    info.setNumPatternsPerNumEdges(fgspanController.writeResults());
            info.setCount(0);
		    String numSubgraphs = "\n\nNumber of Frequent Subgraphs: " + FlowGSpanController.NUMBER_SUBGRAPHS;
            info.setNumHotSubgraphs(FlowGSpanController.NUMBER_SUBGRAPHS);
			System.out.println(FlowGSpanController.NUMBER_SUBGRAPHS);
		    
		    Long totalTime = endTime - startTime;
		    String timeMeasurement = "\n\nTotal runtime: " + totalTime.toString();
		    
		    writeOutputToFile(numSubgraphs + timeMeasurement);
		    FILE_WRITER.close();
		}
		catch(Exception e) {
	    	e.printStackTrace();
	    }
        return info;
	}
	
	private void writeOutputFileHeader(double minSupport, double maxWt, int datasetSize) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date today = new Date();
		MinerManager.writeOutputToFile("Results for Data Mining on " + dateFormat.format(today));
		MinerManager.writeOutputToFile("\r\n\tSupport Threshold:\t\t" + minSupport);
		MinerManager.writeOutputToFile("\r\n\tMax Number of Nodes (Wt):\t\t" + (int)maxWt);
		MinerManager.writeOutputToFile("\r\n\tMax Number of Attributes Allowed:\t\t" + FlowGSpanController.MAX_ATTRIBUTES_TOTAL);
		
		if(FlowGSpanController.LIMIT_FWD_EDGE_ADDITIONS == false) {
			MinerManager.writeOutputToFile("\r\n\tMax Number of EFGEdge Additions:\t\tall");
		}
		else {
			MinerManager.writeOutputToFile("\r\n\tMax Number of EFGEdge Additions:\t\t" + (FlowGSpanController.MAX_NEW_FWD_EDGE_ADDITIONS + FlowGSpanController.MAX_NEW_BACK_EDGE_ADDITIONS));
		}
		MinerManager.writeOutputToFile("\r\n\tMin Method Hotness:\t\t" + FlowGSpanController.MIN_METHOD_HOTNESS);
		MinerManager.writeOutputToFile("\r\n\tDataset Size (in number of graphs):\t\t" + datasetSize);
	}

	public static void writeOutputToFile(String outputStream) {
		try {
			//System.out.println("Subgraph: " + outputStream);
			MinerManager.FILE_WRITER.write(outputStream);
			//ProgramManager.FILE_WRITER.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
