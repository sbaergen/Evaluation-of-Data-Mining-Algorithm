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

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.BitSet;
//import java.util.LinkedHashMap;
//import java.util.LinkedHashSet;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeSet;
//import java.util.Vector;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//import mining.algorithm.FlowGSpanController;
//import mining.data.BasicBlock;
//import mining.data.DataSet;
//import mining.data.EFGEdge;
//import mining.data.EFGVertex;
//import mining.data.ExecutionFlowGraph;
//
/**
 * The DBLoader class is the layer between the database and the mining tool.
 * It loads information from database tables for a specific profiled application
 * and converts this information into a dataset of Execution Flow Graphs (EFGs).
 * @author cgomes
 *
 */
//public class DBLoader implements Runnable {
	/**
	 * List of performance counters we are interested in.
	 */
//	List<String> counters;
//
//	/**
//	 * Connection with database.
//	 */
//	Connection conn;
//
//	/**
//	 * Database connection information.
//	 */
//	String url;
//	/**
//	 * Database connection information: the prefix indicates the application
//	 * whose information we want.
//	 */
//	String prefix;
//	/**
//	 * Database connection information.
//	 */
//	String name;
//	/**
//	 * Database connection information: password.
//	 */
//	String pass;
//
//	/**
//	 * List of method IDs. The methods are those in the database for the
//	 * application we want to analyze.
//	 */
//	Vector<Integer> ids;
//
//	int startMethodIdx;
//	int numMethodsToLoad;
//	boolean useBytecodes;
//
//	/**
//	 * Total tick counts (sum of node weights) for all nodes found in the database and
//	 * belonging to the analyzed application.
//	 */
//	double tickCount;
//	/**
//	 * Total frequency (sum of edge frequencies) for all edges found in the database
//	 * and belonging to the analyzed application.
//	 */
//	double freqTotal;
//
//	/**
//	 * Number of distinct attributes found in database.
//	 */
//	double attCount = 0;
//	/**
//	 * Number of nodes found in database.
//	 */
//	double numNodes = 0;
//	/**
//	 * Maximum number of attributes a single node in the database has.
//	 */
//	double maxAttrs = 0;
//
//	/**
//	 * List of method IDs of methods that have no control flow information.
//	 */
//	LinkedHashSet<Integer> noCFGIds;
//
//	/**
//	 * Set in which to insert loaded EFGs. It is local to each DBLoader instance.
//	 */
//	DataSet dataset;
//
//	/**
//	 * Thread in which this DBLoader instance is being executed.
//	 */
//	public int threadId;
//
//	/**
//	 * Creates a new database connection for mining
//	 * @param addr Database address.
//	 * @param dbName Database name.
//	 * @param port Database port.
//	 * @param name User name.
//	 * @param pass Password.
//	 * @param prefix Prefix for the tables to mine (indicating application of interest).
//	 * @param counters Names of the hardware counters to use as attributes.
//	 * @throws SQLException Thrown if a connection cannot be established for some reason.
//	 */
//	public DBLoader(String addr, String dbName, String port, String name, String pass,
//			String prefix, List<String> counters) throws SQLException {
//		this.url = "jdbc:db2j:net://"+addr + ":"+port+"/" + dbName;
//		this.prefix = prefix;
//		this.name = name;
//		this.pass = pass;
//
//		noCFGIds = new LinkedHashSet<Integer>();
//
//		this.counters = counters;
//		for(String counter: counters) {
//			//System.out.println("Counter = " + counter + "\n");
//			FlowGSpanController.addAttributeToTable(counter);
//		}
//
//		connect();
//
//		startMethodIdx = numMethodsToLoad = 0;
//		ids = null;
//		dataset = null;
//		useBytecodes = false;
//		tickCount = 0;
//		freqTotal = 0;
//	}
//
//	public DBLoader(String addr, String dbName, String port, String name, String pass,
//			String prefix, List<String> counters, Connection dbConn) throws SQLException {
//		this.url = "jdbc:db2j:net://"+addr + ":"+port+"/" + dbName;
//		this.prefix = prefix;
//		this.name = name;
//		this.pass = pass;
//
//		noCFGIds = new LinkedHashSet<Integer>();
//
//		this.counters = counters;
//		for(String counter: counters) {
//			//System.out.println("Counter = " + counter + "\n");
//			FlowGSpanController.addAttributeToTable(counter);
//		}
//
//		conn = dbConn;
//
//		startMethodIdx = numMethodsToLoad = 0;
//		ids = null;
//		dataset = null;
//		useBytecodes = false;
//		tickCount = 0;
//		freqTotal = 0;
//	}
//
//	/**
//	 * Forces connection to close if class instance is garbage-collected.
//	 */
//	protected void finalize() {
//		try {
//			conn.close();
//		} catch (SQLException e) {}
//	}
//
//	/**
//	 * Establishes the connection to the database
//	 * @throws SQLException
//	 */
//	public void connect() throws SQLException{
//		//DEBUG
//		//System.out.println("Connecting to " + url);
//		//end DEBUG
//
//		try {
//			conn = DriverManager.getConnection(url, name, pass);
//			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
//		} catch (SQLException e) {
//			System.out.println("Failed to connect with database");
//			throw e;
//		}
//
//		//DEBUG
//		//System.out.println("Connected to " + url);
//		//end DEBUG
//	}
//
//	/**
//	 * Returns connection object.
//	 * @return The raw JDBC connection object
//	 */
//	public Connection getConnection() {
//		return conn;
//	}
//
//	/**
//	 * Assigns a dataset already allocated to DBLoader.
//	 * @param ref DataSet instance to be assigned.
//	 */
//	public void setDataset(DataSet ref) {
//		dataset = ref;
//	}
//
//	/**
//	 * Assigns list of method IDs to be loaded by this DBLoader instance.
//	 * @param idList List of method IDs to be loaded.
//	 */
//	public void setIds(Vector<Integer> idList) {
//		ids = idList;
//	}
//
//	/**
//	 * Searches through the database with the given prefix and discovers all
//	 * the symbol (method) IDs.
//	 *
//	 * @param db The database to search in.
//	 * @param prefix The table prefix to use in the search.
//	 * @return A list of all the symbol names found in the database.
//	 * @throws SQLException
//	 */
//	public Vector<Integer> getIds() {
//		//DEBUG
//		//System.out.println("Fetching symbol names...");
//		//end DEBUG
//
//		ids = new Vector<Integer>();
//		String query = "SELECT DISTINCT sym.sid FROM " + prefix + "_symbol sym";
//		try {
//			Statement stat = conn.createStatement();
//			ResultSet results;
//			results = stat.executeQuery(query);
//
//			while(results.next())
//				ids.add(results.getInt(1));
//			results.close();
//			stat.close();
//		} catch (SQLException e) {
//			System.out.println("Error affecting ids with query:\n"+query+"\n");
//			e.printStackTrace();
//			System.exit(1);
//		}
//
//		//DEBUG
//		//System.out.println(ids.size() + " symbols found");
//		//end DEBUG
//		return ids;
//	}

	/**
	 * When mining bytecodes, we start with the symbol graph constructed
	 * out of instructions. We combine this information with the list of 
	 * bytecodes from the database in order to make the bytecode graph.
	 * 
	 * @param id ID of the method we want to compose the EFG from.
	 * @return Bytecode graph (i.e. EFG whose nodes are bytecodes).
	 */
//	public ExecutionFlowGraph getBytecodeGraph(int id) {
//		//We need the instruction graph before we can continue. For an
//		//instruction i which has successor instruction i', we make bytecode
//		//B' follow bytecode B where B is the bytecode that created i and B' is
//		//the bytecode that created i'.
//		ExecutionFlowGraph instructionGraph = getInstructionGraph(id);
//
//		Map<String, EFGVertex> bytecodes = getBytecodes(id);
//		if(bytecodes.isEmpty())
//			return null;
//
//		ExecutionFlowGraph bytecodeGraph = new ExecutionFlowGraph();
//
//		// We need to track the instructions for each bytecode so we can find
//		// the last instruction in each bytecode.
//		Map<String, List<EFGVertex> > instructions = new LinkedHashMap<String, List<EFGVertex> >();
//
//		tickCount = 0;
//		//Iterate through the nodes in the graph and amalgamate the counter information.
//		ConcurrentLinkedQueue<EFGVertex> q = new ConcurrentLinkedQueue<EFGVertex>();
//		q.addAll(instructionGraph.getVertexSet().values());
//		List<EFGVertex> alreadySeen = new LinkedList<EFGVertex>();
//		while(!q.isEmpty()) {
//			EFGVertex current = q.poll();
//
//			// Cycle detection.
//			if(alreadySeen.contains(current))
//				continue;
//			alreadySeen.add(current);
//
//			String bc = current.getBytecodeId();
//			if(bc == null || bc.equals("")) {
//				/*
//				 * they do exist, after all
//				 *
//				 * For now, continue without them
//				 */
//				continue;
//			}
//
//			// Add this instruction to the list of instructions for this bytecode.
//			List<EFGVertex> bcInstructions = null;
//			if(instructions.containsKey(bc))
//				bcInstructions = (List<EFGVertex>) instructions.get(bc);
//			else
//				bcInstructions = new LinkedList<EFGVertex>();
//			bcInstructions.add(current);
//			instructions.put(bc, bcInstructions);
//
//			// Also, amalgamate the counter information and ticks for this instruction.
//			EFGVertex bytecode = bytecodes.get(bc);
//			bytecode.addAttributes((BitSet)current.getAttributes());
//			bytecode.addAttrWeights((LinkedHashMap<Integer, Double>)current.getAttrWeights());
//			bytecode.setWeight(bytecode.getWeight() + current.getWeight());
//			tickCount += current.getWeight();
//
//			q.addAll(current.getForwardChildren());
//		}

		//Now we need to connect the bytecode instructions. We use the list of 
		//instructions for each bytecode that we collected earlier.
//		EFGVertex firstBytecode = null;
//		for(String bc: instructions.keySet()) {
//			EFGVertex currBytecode = bytecodes.get(bc);
//			List<EFGVertex> bcInsts = instructions.get(bc);
//
//			bytecodeGraph.insertVertex(currBytecode);
//
//			// We need to find the first bytecode, i.e. the one with id 0.
//			String bcNum = currBytecode.getBytecodeId().split(" ")[0].trim();
//			if(Integer.parseInt(bcNum) == 0)
//				firstBytecode = currBytecode;
//
//			//We need to find the last instruction(s) for each bytecode,
//			//then add the bytecodes of all following instructions as
//			//children. Edge frequencies are maintained.
//			EFGVertex last = null;
//			for(EFGVertex inst: bcInsts)
//				if(last == null || inst.getId() > last.getId())
//					last = inst;
//
//			// Should not happen because we are iterating through the instruction
//			// hash map.
//			if(last == null) {
//				System.out.println("Bytecode " + bc + " had no instructions");
//				continue;
//			}
//
//			// We have the last instruction, add the bytecodes of its children.
//			List<EFGVertex> children = (List<EFGVertex>) last.getChildren();
//			List<Double> freqs = last.getOutEdgesFreq();
//			for(int i = 0; i < children.size(); i++) {
//				EFGVertex child = children.get(i);
//				double freq = freqs.get(i);
//
//				EFGVertex childBytecode = bytecodes.get(child.getBytecodeId());
//				if(childBytecode == null)
//					continue;
//
//				bytecodeGraph.insertVertex(childBytecode);
//				// If we haven't seen it, add it as a child.
//				if(!currBytecode.getChildren().contains(childBytecode)) {
//					EFGEdge e = new EFGEdge(currBytecode, childBytecode, freq);
//					bytecodeGraph.insertEdge(e);
//					freqTotal += freq;
//				}
//				// If we have, we take the largest of the frequencies seen so far.
//				else {
//					int index = ((List<EFGVertex>) currBytecode.getChildren()).indexOf(childBytecode);
//					if(freq > currBytecode.getOutEdgesFreq().get(index)) {
//						double diff = freq - currBytecode.getOutEdge(index).getFrequency();
//						freqTotal += diff;
//
//						currBytecode.getOutEdge(index).setFrequency(freq);
//					}
//				}
//			}
//			bytecodeGraph.setEntryVertex(firstBytecode);
//			bytecodeGraph.setExitVertex(last);
//		}
//
//		return bytecodeGraph;
//	}
//
//	/**
//	 * Returns the set of distinct bytecodes contained in the listing table.
//	 * @return The set of distinct bytecodes contained in the listing table.
//	 */
//	public TreeSet<String> getDistinctBytecodes() {
//		String query = "select distinct bytecodefull" +
//		" from "+prefix+"_listing";
//
//		TreeSet<String> codes = new TreeSet<String>();
//
//		Statement stat = null;
//		try {
//			stat = conn.createStatement();
//			ResultSet rs = stat.executeQuery(query);
//
//			while(rs.next()) {
//				String bytecode = rs.getString(1);
//				if(bytecode == null || bytecode.equals(""))
//					continue;
//				codes.add(bytecode.split(" ")[1]);
//			}
//
//			rs.close();
//			stat.close();
//		} catch (SQLException e) {
//			return codes;
//		}
//
//		return codes;
//	}
//
//	/**
//	 * Returns a map of each bytecode ID to the node that will represent
//	 * that bytecode in the final EFG.
//	 * @param id ID of the method from which we want to get the list of bytecodes.
//	 * @return Map between bytecode IDs and nodes in EFG they correspond to.
//	 */
//	public Map<String, EFGVertex> getBytecodes(int id) {
//		LinkedHashMap<String, EFGVertex> bytecodes = new LinkedHashMap<String, EFGVertex>();
//
//		String query = "select distinct bytecodefull" +
//		" from "+prefix+"_listing " +
//		"where sid="+id;
//
//		Statement stat = null;
//		try{
//			ResultSet rs;
//			synchronized(conn) {
//				stat = conn.createStatement();
//				rs = stat.executeQuery(query);
//			}
//
//			// For each row, make a new entry in the map
//			while(rs.next())  {
//				String bytecodeName = rs.getString(1);
//				if(bytecodeName == null)
//					bytecodeName = "";
//				EFGVertex bytecode = new EFGVertex(0, 0, bytecodeName);
//
//				if(bytecodeName.equals(""))
//					continue;
//
//				// bytecodes are of the form "id bytecode ....", so we want index 1
//				bytecode.setAttribute(FlowGSpanController.addAttributeToTable(bytecodeName.split(" ")[1]));
//				bytecodes.put(rs.getString(1), bytecode);
//			}
//
//			rs.close();
//			stat.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//
//		return bytecodes;
//	}
//
//	/**
//	 * Returns the EFG corresponding to the instructions for the given
//	 * symbol (method).
//	 *
//	 * @param id The method for which to construct the graph.
//	 * @return The EFG instance.
//	 */
//	public ExecutionFlowGraph getInstructionGraph(int id) {
//		//We have to perform a left-join on the IA table as there
//		//may not be an IA record for every instruction in the
//		//method. If we didn't then we may not get data for some
//		//of the instructions.
//		String query = "SELECT dis.*, lst.*, ia.* " +
//		"FROM "+	prefix+"_ia ia " +
//		"RIGHT JOIN "+prefix+"_disassm dis "+
//		"ON ia.sid = dis.sid AND ia.address = dis.address "+
//		"LEFT JOIN " +prefix+"_listing lst " +
//		"ON ia.sid = lst.sid AND lst.inst_addr = ia.address " +
//		"WHERE 	dis.sid="+id + " " +
//		"ORDER BY dis.offset";
//
//		Statement stat = null;
//		try {
//			synchronized(conn) {
//				stat = conn.createStatement();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//
//
//		boolean uselisting = true;
//		String testQuery = "SELECT * FROM "+prefix+"_listing lst WHERE sid="+id;
//
//		try {
//			ResultSet testrs;
//			synchronized(conn) {
//				testrs = stat.executeQuery(testQuery);
//			}
//
//			//Now rows in listing table for this sid, don't use the listing table.
//			if(!testrs.next()) {
//				uselisting = false;
//				noCFGIds.add(id);
//			}
//			testrs.close();
//		} catch(SQLException e) {
//			//This means there was no listing table, change the query.
//			uselisting = false;
//			noCFGIds.add(id);
//			System.out.println("WARNING: no listing information found for symId " + id);
//		}
//
//		if(!uselisting)
//			query = "SELECT dis.*, ia.* " +
//			"FROM " +
//			prefix+"_disassm dis " +
//			"LEFT JOIN "+prefix+"_ia ia "+
//			"ON dis.sid = ia.sid AND dis.offset = ia.offset "+
//			"WHERE 	dis.sid="+id;
//
//		ResultSet res = null;
//		try {
//			synchronized(conn) {
//				res = stat.executeQuery(query);
//			}
//		} catch (SQLException e) {
//			//System.err.println("Error executing query:\n"+query);
//			System.err.println("Error executing query for method id: " + id);
//			//e.printStackTrace();
//			//System.exit(1);
//			return null;
//		}
//
//		ExecutionFlowGraph instructionGraph = null;
//		try {
//			instructionGraph = makeGraph(res, id, uselisting);
//			res.close();
//			stat.close();
//		} catch (SQLException e) {
//			System.err.println("Error constructing graph");
//			e.printStackTrace();
//			System.exit(1);
//		}
//
//		return instructionGraph;
//	}
//
//	/**
//	 * Checks to see whether this connection's parameters match those
//	 * passed in here. This is used to see whether a new connection must
//	 * be opened or whether this connection will do.
//	 * @param url Connection address.
//	 * @param name Connection name.
//	 * @param pass Connection password.
//	 * @param prefix Prefix (application of interest).
//	 * @return Whether the connection is the same.
//	 */
//	public boolean isSameConnection(String url, String name, String pass,
//			String prefix) {
//		return this.url.equals(url) && this.name.equals(name) &&
//		this.pass.equals(pass) && this.prefix.equals(prefix);
//	}
//
//	/**
//	 * Closes the database connection.
//	 */
//	public void close() {
//		try {
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Calculates, if not previously calculated, all execution ticks across
//	 * all symbols (methods) in the given profile.
//	 * @return The sum of all execution ticks across all the symbols in the
//	 * given profile.
//	 */
//	public double getTotalTicks() {
//		if(tickCount > 0)
//			return tickCount;
//
//		String query = "SELECT SUM(Ticks) FROM "+prefix+"_ia";
//
//		ResultSet result;
//		try {
//			Statement stat = conn.createStatement();
//			result = stat.executeQuery(query);
//			result.next();
//			tickCount =  result.getDouble(1);
//			result.close();
//			stat.close();
//		} catch (Exception e) {
//			System.err.println("Could not get tick count");
//			tickCount = 1;
//		}
//
//		return tickCount;
//	}
//
//	/**
//	 * Returns total edge frequency across all EFGs in database for given profile.
//	 * @return The total edge frequencies encountered while building EFGs.
//	 */
//	public double getFreqTotal() {
//		if(freqTotal > 0)
//			return freqTotal;
//
//		String query = "SELECT SUM(FREQ) FROM "+prefix+"_cfgedge";
//
//		ResultSet result;
//		try {
//			Statement stat = conn.createStatement();
//			result = stat.executeQuery(query);
//			result.next();
//			freqTotal =  result.getDouble(1);
//			result.close();
//			stat.close();
//		} catch (Exception e) {
//			System.err.println("Could not get total frequency");
//			freqTotal = 1;
//		}
//
//		return freqTotal;
//	}
//
//	/**
//	 * Returns list of opcodes from the current profile.
//	 * @return A list of distinct opcodes from the current profile.
//	 */
//	public List<String> getOpcodes() {
//		String query = "";
//		try {
//			Statement stat = conn.createStatement();
//			query = "SELECT DISTINCT Opcode FROM "+prefix+"_disassm dis";
//			ResultSet result = stat.executeQuery(query);
//
//			List<String> codes = new LinkedList<String>();
//			while(result.next())
//				codes.add(result.getString(1));
//
//			result.close();
//			stat.close();
//			return codes;
//		} catch (SQLException e) {
//			System.err.println(query);
//			e.printStackTrace();
//			return new LinkedList<String>();
//		}
//	}
//
//	/**
//	 * Returns the maximum inline depth across all methods in the profile.
//	 * @return The maximum inline depth across all methods in the profile.
//	 */
//	public int getMaxInliningDepth() {
//		String query = "SELECT MAX(nestingdepth) FROM "+prefix+"_listing";
//		int depth = 0;
//
//		try {
//			Statement stat = conn.createStatement();
//			ResultSet rs = stat.executeQuery(query);
//
//			rs.next();
//			depth = rs.getInt(1);
//
//			rs.close();
//			stat.close();
//		} catch (SQLException e) {
//			return depth;
//		}
//
//		return depth;
//	}
//
//
//	/**
//	 * Resets the frequency counter between generations.
//	 * Actually this is not used, just here for convenience.
//	 */
//	public void resetFrequencyCounter() {
//		this.freqTotal = 0;
//	}
//
//	/**
//	 * Returns a list of the names of symbols without CFG data.
//	 * @return A list of the names of symbols without CFG data.
//	 */
//	public List<String> getSymbolsWithNoCFG() {
//		List<String> syms = new LinkedList<String>();
//
//		for(int id: noCFGIds)
//			syms.add(getNameForSymbolId(id));
//
//		return syms;
//	}
//
//	/**
//	 * Helper method. Takes a symbol ID and returns its full name.
//	 * @param Symbol ID of required symbol.
//	 * @return Full name of symbol.
//	 */
//	private String getNameForSymbolId(int id) {
//		String query = "SELECT name FROM " + prefix + "_SYMBOL WHERE sid = " + id;
//
//		String name = "unknown";
//		try {
//			synchronized(conn) {
//				Statement stat = conn.createStatement();
//				ResultSet set = stat.executeQuery(query);
//				set.next();
//				name = set.getString(1);
//			}
//		} catch (SQLException e) {
//			// Fall through
//		}
//
//		return name;
//	}
//
//	/**
//	 * Constructs an EFG from a result set containing the instructions in
//	 * the method.
//	 * @param rawData Result set with all instructions in method.
//	 * @param symbol ID of method whose EFG is to be created.
//	 * @param hasCFGInfo Whether method has control flow information.
//	 * @return EFG created from method's profile data.
//	 */
//	private ExecutionFlowGraph makeGraph(ResultSet rawData, int symbol,
//			boolean hasCFGInfo)
//	throws SQLException {
//
//		//DEBUG
//		//System.out.println("Mining symbol "+symbol);
//		//end DEBUG
//
//		EFGVertex head = null;
//		Map<Long, BasicBlock> blocks = new LinkedHashMap<Long, BasicBlock>();
//
//		double currAttrs = 0;
//
//		ExecutionFlowGraph instructionGraph = new ExecutionFlowGraph();
//
//		// First, check for the presence of CFG information in the DB.
//		try {
//			// Build CFG nodes.
//			blocks = createBlocks(symbol, instructionGraph);
//
//			if(blocks.isEmpty()) {
//				//DEBUG
//				//System.out.println("Blocks were empty");
//				//end DEBUG
//				noCFGIds.add(symbol);
//				hasCFGInfo = false;
//				return null;
//			}
//			else {
//				hasCFGInfo = true;
//			}
//		} catch (SQLException e) {
//			//If the CFG node table doesn't exist or something went wrong,
//			//then we just proceed with the naive formulation (no control flow
//			//considered).
//			noCFGIds.add(symbol);
//			hasCFGInfo = false;
//			return null;
//		}
//
//		//We need the total number of sampling ticks, as well as the sum of
//		//the edge frequencies in order to normalize the frequency and time
//		//support values.
//		getTotalTicks();
//
//		long bid = -1;
//		long prevbid = -1;
//
//		//Now we actually iterate through all the instructions and process them.
//		//If we have CFG information the instructions will be added as components
//		//of their respective BBs. If no CFG information was found, then the
//		//instructions will just be processed as a continuous list.
//		EFGVertex last = null;
//		LinkedHashSet<Long> existingOffsets = new LinkedHashSet<Long>();
//		LinkedHashMap<Integer, LinkedHashMap<String, Double>> carryOnAttributes = new LinkedHashMap<Integer, LinkedHashMap<String, Double>>();
//		LinkedHashMap<Integer, LinkedHashMap<String, Double>> secondCarryOnAttributes = new LinkedHashMap<Integer, LinkedHashMap<String, Double>>();
//		LinkedHashMap<Integer, Vector<Double>> branchAttributes = new LinkedHashMap<Integer, Vector<Double>>();
//
//		Vector<Long> branchTargets = new Vector<Long>();
//
//		while(rawData.next()) {
//			String bytecode = "";
//			try{
//				bytecode = rawData.getString("Bytecodefull");
//			} catch (SQLException sql) {
//				// Fall through...
//			}
//
//			String disassm = null;
//			try {
//				disassm = rawData.getString("Disassm");
//			} catch (SQLException sql) {
//				//fall through
//			}
//
//			if(disassm != null &&
//					(disassm.contains("ASSOCREGS") ||
//							disassm.contains("DEPEND") ||
//							disassm.contains("DS") ||
//							disassm.contains("FENCE") ||
//							disassm.contains("SCHEDFENCE") ||
//							disassm.contains("PROC") ||
//							disassm.contains("RET") ||
//							disassm.contains("SCHEDOFF") ||
//							disassm.contains("SCHEDON") ||
//							disassm.contains("WRTBAR") ||
//							disassm.contains("XPCALLDESC") ||
//							disassm.contains("DC") ||
//							disassm.contains("DC2") ||
//							disassm.contains("VGNOP") ||
//							disassm.contains("NOP") ||
//							disassm.contains("ASM") ||
//							disassm.contains("LOCK") ||
//							disassm.contains("UNLOCK") ||
//							disassm.contains("SNAPSHOT") ||
//							disassm.contains("LABEL"))) {
//				continue;
//			}
//
//			long addr = 0;
//			try{
//				addr = rawData.getLong("Address");
//			} catch (SQLException sql) {
//				continue;
//			}
//
//			if(existingOffsets.contains(addr)) {
//				//DEBUG
//				//System.out.println("Ignored: " + addr + "\n");
//				//end DEBUG
//				continue;
//			}
//			existingOffsets.add(addr);
//
//			String instruction = rawData.getString("Disassembly");
//
//			String opcode = rawData.getString("Opcode");
//
//			Long address = rawData.getLong("Address");
//			//We want to print addresses always in hex.
//			String addressInHex = Long.toHexString(address);
//
//			EFGVertex current = new EFGVertex(rawData.getDouble("Ticks"), address, bytecode);
//			current.addInstruction(instruction);
//			current.addAddress(addressInHex);
//
//			current.setSymbolId(symbol);
//			numNodes++;
//
//			//Threshold this instruction's data...
//			LinkedHashMap<String, Double> carryOnMap = carryOnAttributes.get(rawData.getRow());
//			if(carryOnMap != null) {
//				for(String att : carryOnMap.keySet()) {
//					current.setAttribute(FlowGSpanController.addAttributeToTable(att), carryOnMap.get(att));
//					currAttrs++;
//				}
//			}
//			carryOnAttributes.remove(rawData.getRow());
//
//			LinkedHashMap<String, Double> secondCarryOnMap = secondCarryOnAttributes.get(rawData.getRow());
//			if(secondCarryOnMap != null) {
//				for(String att : secondCarryOnMap.keySet()) {
//					current.setAttribute(FlowGSpanController.addAttributeToTable(att), secondCarryOnMap.get(att));
//					currAttrs++;
//				}
//			}
//			secondCarryOnAttributes.remove(rawData.getRow());
//
//			for(String att: counters) {
//				//System.out.println("Counter = " + att + "\n");
//				if(att.contains("RR1") || att.contains("ISR1")) {
//					double val = rawData.getDouble(att);
//					if(val > 0) {
//						int targetRow = rawData.getRow() + 1;
//						LinkedHashMap<String, Double> attrMap = carryOnAttributes.get(targetRow);
//						if(attrMap == null) {
//							attrMap = new LinkedHashMap<String, Double>();
//						}
//						att = att.replace('1', '0');
//						attrMap.put(att, val);
//						carryOnAttributes.put(targetRow, attrMap);
//					}
//				}
//				else if(att.contains("RR2") || att.contains("ISR2")) {
//					double val = rawData.getDouble(att);
//					if(val > 0) {
//						int targetRow = rawData.getRow() + 2;
//						LinkedHashMap<String, Double> attrMap = secondCarryOnAttributes.get(targetRow);
//						if(attrMap == null) {
//							attrMap = new LinkedHashMap<String, Double>();
//						}
//						att = att.replace('2', '0');
//						attrMap.put(att, val);
//						secondCarryOnAttributes.put(targetRow, attrMap);
//					}
//				}
//				else if(att.contains("BR_")) {
//					if(att.equals("BR_NTCD")) {
//						if(opcode.equals("BASL") || opcode.equals("BRASL") || opcode.equals("BR")) {
//							int currRow = rawData.getRow();
//							if(branchAttributes.get(currRow) != null) {
//								branchAttributes.remove(currRow);
//							}
//							continue;
//						}
//
//						double br_ntcd = rawData.getDouble(att);
//						double br_tcd = rawData.getDouble("BR_TCD");
//						double br_twd = rawData.getDouble("BR_TWD");
//						double br_ntwd = rawData.getDouble("BR_NTWD");
//						double br_tcdwt = rawData.getDouble("BR_TCDWT");
//
//						int currRow = rawData.getRow();
//
//						if(opcode.matches("B[a-zA-Z0-9]*") || opcode.matches("C[a-zA-Z0-9]*J")) {
//							Vector<Double> branchAttrValues = branchAttributes.get(currRow);
//							if(branchAttrValues != null) {
//								br_ntcd += branchAttrValues.get(0);
//								br_tcd += branchAttrValues.get(1);
//								br_ntwd += branchAttrValues.get(2);
//								br_twd += branchAttrValues.get(3);
//								br_tcdwt += branchAttrValues.get(4);
//
//								branchAttributes.remove(currRow);
//							}
//
//							double br_pred = (br_ntwd + br_twd);
//							double br_dir = br_tcd + br_twd;
//
//							if(br_ntcd != 0 || br_tcd !=0) {
//								br_pred /= (br_ntcd + br_tcd);
//							}
//							if(br_ntcd != 0 || br_ntwd != 0) {
//								br_dir /= (br_ntcd + br_ntwd);
//							}
//
//							if(br_pred > 0) {
//								current.setAttribute(FlowGSpanController.addAttributeToTable("BR_PRED"), br_pred);
//								currAttrs += 1;
//							}
//							if(br_dir > 0) {
//								current.setAttribute(FlowGSpanController.addAttributeToTable("BR_DIR"), br_dir);
//								currAttrs += 1;
//							}
//							if(br_ntcd > 0) {
//								current.setAttribute(FlowGSpanController.addAttributeToTable("BR_NTCD"), br_ntcd);
//								currAttrs += 1;
//							}
//							if(br_tcd > 0) {
//								current.setAttribute(FlowGSpanController.addAttributeToTable("BR_TCD"), br_tcd);
//								currAttrs += 1;
//							}
//							if(br_ntwd > 0) {
//								current.setAttribute(FlowGSpanController.addAttributeToTable("BR_NTWD"), br_ntwd);
//								currAttrs += 1;
//							}
//							if(br_twd > 0) {
//								current.setAttribute(FlowGSpanController.addAttributeToTable("BR_TWD"), br_twd);
//								currAttrs += 1;
//							}
//							if(br_tcdwt > 0) {
//								current.setAttribute(FlowGSpanController.addAttributeToTable("BR_TCDWT"), br_tcdwt);
//								currAttrs += 1;
//							}
//						}
//						else {
//							Vector<Double> branchAttrValues = branchAttributes.get(currRow);
//							if(branchAttrValues != null) {
//								br_ntcd += branchAttrValues.get(0);
//								br_tcd += branchAttrValues.get(1);
//								br_ntwd += branchAttrValues.get(2);
//								br_twd += branchAttrValues.get(3);
//								br_tcdwt += branchAttrValues.get(4);
//
//								branchAttributes.remove(currRow);
//							}
//
//							if(br_ntcd != 0 || br_tcd != 0 || br_ntwd != 0 || br_twd != 0 || br_tcdwt != 0) {
//								Vector<Double> attrValues = new Vector<Double>();
//								attrValues.add(br_ntcd);
//								attrValues.add(br_tcd);
//								attrValues.add(br_ntwd);
//								attrValues.add(br_twd);
//								attrValues.add(br_tcdwt);
//
//								branchAttributes.put(currRow + 1, attrValues);
//							}
//						}
//					}
//				}
//				else {
//					double val = rawData.getDouble(att);
//					if(val > 0) {
//						current.setAttribute(FlowGSpanController.addAttributeToTable(att), val);
//						currAttrs++;
//					}
//				}
//			}
//
//			//Adds prologue...
//			long offset = rawData.getLong("Offset");
//			if(offset < 0x26L) {
//				current.setAttribute(FlowGSpanController.addAttributeToTable("Prologue"));
//				currAttrs++;
//			}
//
//			if(opcode.matches("B[a-zA-Z0-9]*") || opcode.matches("C[a-zA-Z0-9]*J")) {
//				current.setAttribute(FlowGSpanController.addAttributeToTable("BR_OP"));
//			}
//			else {
//				current.setAttribute(FlowGSpanController.addAttributeToTable(opcode));
//			}
//			currAttrs++;
//
//			// Inlining depth...
//			try {
//				int nestDepth = rawData.getInt("Nestingdepth");
//				if(nestDepth > 0) {
//					current.setAttribute(FlowGSpanController.addAttributeToTable("inline"+nestDepth));
//					currAttrs++;
//				}
//			} catch (SQLException e) {
//				// Do nothing, this information just isn't here for this node.
//			}
//
//			// JIT target...
//			if(rawData.getString("Disassembly").trim().equalsIgnoreCase("STG GPR14,-8(,GPR5)")) {
//				current.setAttribute(FlowGSpanController.addAttributeToTable("JITtarget"));
//				currAttrs++;
//			}
//
//			if(hasCFGInfo) {
//				// We have Listing and CFG information...
//				String value = null;
//				try {
//					value = rawData.getString("BBN");
//					if(value == null) {
//						if(prevbid == -1) {
//							System.out.println("Empty bid at start");
//							continue;
//						}
//						else
//							bid = prevbid;
//					}
//					else {
//						value = value.replaceAll(",", "");
//						bid = Long.parseLong(value);
//						prevbid = bid;
//					}
//				} catch (NumberFormatException e) {
//					System.out.println("No bb information for " + current.getId());
//					continue;
//				}
//
//				if(blocks.containsKey(bid)) {
//					BasicBlock block = blocks.get(bid);
//					boolean isFirstOrLast = false;
//
//					if(opcode.matches("B[a-zA-Z0-9]*") || opcode.matches("C[a-zA-Z0-9]*J")) {
//						block.addLastInstruction(current);
//
//						long targetAddress = rawData.getLong("targetaddress");
//
//						EFGVertex targetVertex = instructionGraph.getVertex(targetAddress);
//						if(targetVertex != null) {
//							BasicBlock targetBlock = blocks.get(targetVertex.getBBN());
//							targetBlock.removeInstruction(targetAddress);
//							targetBlock.addFirstInstruction(targetVertex);
//						}
//						else {
//							branchTargets.add(targetAddress);
//						}
//						isFirstOrLast = true;
//					}
//					if(branchTargets.contains(address)) {
//						block.addFirstInstruction(current);
//						isFirstOrLast = true;
//					}
//
//					if(isFirstOrLast == false) {
//						block.addVertex(current);
//					}
//
//				}
//				else {
//					System.out.println("Bad bid: " + bid);
//				}
//			}
//			else {
//				// We don't have any CFG information, so listing information is
//				// useless in terms of nodes.
//
//				if(head == null) {
//					// We're the first instruction...
//					head = current;
//					last = current;
//					instructionGraph.insertVertex(head);
//					instructionGraph.setEntryVertex(head);
//					instructionGraph.setExitVertex(head);
//				}
//				else {
//					// Add us as a child of the last instruction...
//					EFGEdge currEdge = new EFGEdge(last, current, 1);
//					instructionGraph.insertVertex(current);
//					instructionGraph.insertEdge(currEdge);
//
//					freqTotal++;
//
//					last = current;
//					instructionGraph.setExitVertex(last);
//				}
//			}
//
//			if(currAttrs > maxAttrs)
//				maxAttrs = currAttrs;
//
//			attCount += currAttrs;
//			currAttrs = 0;
//		}
//
//		// If we're using the CFG to create blocks, we need to join them now.
//		if(hasCFGInfo)
//			instructionGraph = assembleBlocks(symbol, blocks, instructionGraph);
//		else if(head != null){
//			instructionGraph.setEntryEdge(new EFGVertex(0, 0, true), head, 1.0);
//			freqTotal++;
//		}
//		return instructionGraph;
//	}
//
//	/**
//	 * Assembles the given CFG nodes (basic blocks) for the given symbol according to the
//	 * information in the database. Returns the corresponding EFG.
//	 * @param symbol Method ID whose CFG nodes are to be assembled.
//	 * @param blocks Mapping of basic block ID to basic block instance.
//	 * @param instructionGraph EFG to be assembled after basic blocks of CFG are connected.
//	 */
//	private ExecutionFlowGraph assembleBlocks(int symbol, Map<Long, BasicBlock> blocks,
//			ExecutionFlowGraph instructionGraph)
//	throws SQLException{
//
//		Statement statement = conn.createStatement();
//		ResultSet rs = null;
//
//		String query =
//			"SELECT cfge.* "+
//			"FROM "+prefix+"_cfgedge cfge "+
//			"WHERE cfge.sid="+symbol;
//		try {
//			synchronized(conn) {
//				rs = statement.executeQuery(query);
//			}
//		} catch (SQLException e) {
//			System.err.println("Failed query:\n" + query);
//			e.printStackTrace();
//			System.exit(1);
//		}
//
//		if(!blocks.isEmpty())
//			instructionGraph = joinCFGNodes(blocks, rs, instructionGraph);
//
//		rs.close();
//		statement.close();
//
//		return instructionGraph;
//	}
//
//	/**
//	 * Pull and assemble the basic block information from database.
//	 * @param symbol Method ID.
//	 * @param instructionGraph EFG that will be created later on.
//	 * @return Mappings of basic block unique ID and basic block itself.
//	 */
//	private Map<Long, BasicBlock> createBlocks(int symbol,
//			ExecutionFlowGraph instructionGraph)
//			throws SQLException {
//		Map<Long, BasicBlock> blocks;
//		Statement statement = conn.createStatement();
//
//		String query =
//			"SELECT cfgn.* "+
//			"FROM "+prefix+"_cfgnode cfgn " +
//			"WHERE cfgn.sid="+symbol;
//
//		ResultSet rs;
//		synchronized(conn){
//			rs = statement.executeQuery(query);
//		}
//		blocks = buildCFGNodes(rs, instructionGraph);
//
//		rs.close();
//		statement.close();
//
//		return blocks;
//	}
//
//	/**
//	 * Creates the CFG's basic blocks from the information in the provided
//	 * database query result set. The basic blocks are stored in a map indexed by
//	 * their basic block number.
//	 * @param Result set with the table of basic blocks for a certain method.
//	 * @param instructionGraph EFG that will be composed from the instruction information
//	 * in the basic blocks, later on.
//	 * @return Mapping of basic block IDs and basic blocks.
//	 */
//	private Map<Long, BasicBlock>
//	buildCFGNodes(ResultSet blockTable, ExecutionFlowGraph instructionGraph)
//	throws SQLException {
//		Map<Long, BasicBlock> blocks = new LinkedHashMap<Long, BasicBlock>();
//
//		while(blockTable.next()) {
//			BasicBlock block =
//				new BasicBlock(blockTable.getLong("BID"),
//						blockTable.getLong("START_ADDR"),
//						blockTable.getDouble("FREQ"), instructionGraph);
//			blocks.put(block.getId(), block);
//		}
//
//		//DEBUG
//		//System.out.println("Found "+blocks.size() + " BBs");
//		//end DEBUG
//
//		return blocks;
//	}
//
//	/**
//	 * Creates the edges between basic blocks in the CFG according
//	 * to edge data in the provided result set.
//	 * @param blocks Basic blocks to be connected.
//	 * @param edges Edges to connect basic blocks.
//	 * @param instructionGraph EFG that is composed in this method, using the instructions in
//	 * basic blocks and the edge information.
//	 * @return EFG (instructionGraph) formed by control flow information (edges) and instructions
//	 * in basic blocks.
//	 */
//	private ExecutionFlowGraph joinCFGNodes(Map<Long, BasicBlock> blocks, ResultSet edges,
//			ExecutionFlowGraph instructionGraph)
//	throws SQLException {
//		BasicBlock start = null;
//
//		// At this point we go through the BBs and add dummy nodes to empty
//		// blocks...Dummy nodes are those without any profile information. In
//		//other words, they are empty.
//		for(BasicBlock b: blocks.values())
//			// Empty block, add a dummy.
//			if(b.getFirst() == null)
//				b.addVertex(new EFGVertex(0, Long.MAX_VALUE, true));
//
//		LinkedHashMap<Long, Double> incFreqs = new LinkedHashMap<Long, Double>();
//
//		// Now connect the blocks, which are all guaranteed to be non-empty.
//		while(edges.next()) {
//			String type = edges.getString("TYPE");
//			long from = edges.getLong("BID");
//			long to = edges.getLong("DEST_BID");
//			double freq = edges.getDouble("FREQ");
//
//			// Outgoing edges + exception outgoing edges.
//			if(type.equals("OUT") || type.equals("EX_OUT")) {
//				if(!blocks.containsKey(from) || !blocks.containsKey(to)) {
//					System.out.println("Bid not found");
//					continue;
//				}
//
//				// Increment the total edge frequency.
//				//freqTotal += freq;
//
//				//Connect all basic blocks...
//				BasicBlock current = blocks.get(from);
//				BasicBlock next = blocks.get(to);
//
//				EFGEdge betweenBlocks = new EFGEdge(current.getLast(), next.getFirst(), freq);
//
//				//DEBUG
//				//System.out.println("BLOCK CONNECTS " + current.getLast().getId() + " TO " + next.getFirst().getId());
//				//end DEBUG
//
//				instructionGraph.insertEdge(betweenBlocks);
//
//				// Update the incoming edge frequencies for the next block.
//				double oldVal = 0;
//				if(incFreqs.containsKey(to))
//					oldVal = incFreqs.get(to);
//				incFreqs.put(to, oldVal+freq);
//
//				// Check to see if this is the first basic block.
//				EFGVertex cand = current.getFirst();
//				if(start == null || cand.getId() < start.getFirst().getId())
//					start = current;
//			}
//		}
//
//		//Connect all basic blocks after updating their incoming and outgoing edge information.
//		//If a basic block has N instructions (nodes in the EFG), we need to update the
//		//total edge frequency in the profile by the frequency of the block times N-1.
//		for(long bid: incFreqs.keySet()) {
//			BasicBlock block = blocks.get(bid);
//			block.connect(incFreqs.get(bid));
//			// We don't count the first node - it's already been added.
//			//freqTotal += (block.getInstructionCount()-1)*incFreqs.get(bid);
//		}
//
//
//		// Now we should have the first real instruction.
//		EFGVertex first = start.getFirst();
//		//Find who the exit vertex is.
//		instructionGraph.findEntryExitVertices();
//		//Set the first instruction as entry node to the EFG.
//		instructionGraph.setEntryVertex(first);
//
//		return instructionGraph;
//	}
//
//	@Override
//	/**
//	 * Runs the EFG loading process.
//	 */
//	public void run() {
//		int finalMethodIdx = startMethodIdx + numMethodsToLoad;
//		for(int i = startMethodIdx; i < finalMethodIdx; ++i) {
//			Integer methodId = ids.get(i);
//			ExecutionFlowGraph efg = null;
//
//			if(useBytecodes) {
//				efg = getBytecodeGraph(methodId);
//			}
//			else {
//				efg = getInstructionGraph(methodId);
//			}
//
//			if(efg == null) {
//				System.out.println("EFG could not be created!");
//				continue;
//			}
//			else {
//				//efg.normalize();
//				double totalHotness = tickCount;
//				double hotness = efg.getTotalWeight();
//				//System.out.println("Testing method " + methodId + " using thread " + threadId);
//
//				if(hotness/totalHotness >= FlowGSpanController.MIN_METHOD_HOTNESS) {
//					dataset.put(methodId, efg);
//				}
//			}
//		}
//	}
//
//	/**
//	 * Sets whether EFG nodes are made of bytecodes or not.
//	 * @param usingBytecodes True if nodes are made of bytecodes.
//	 */
//	public void setBytecodeUsage(boolean usingBytecodes) {
//		useBytecodes = usingBytecodes;
//	}
//
//	/**
//	 * Sets the starting method index in the "ids" list.
//	 * @param startIdx Starting index.
//	 */
//	public void setStartMethodIndex(int startIdx) {
//		startMethodIdx = startIdx;
//	}

	/**
	 * Sets number of methods to be loaded.
	 * @param methodsToLoad Number of methods to be loaded.
	 */
//	public void setNumMethodsToLoad(int methodsToLoad) {
//		numMethodsToLoad = methodsToLoad;
//	}
//
//	/**
//	 * Returns the dataset of EFGs loaded by this DBLoader instance.
//	 * @return DataSet instance loaded.
//	 */
//	public DataSet getDataSet() {
//		return dataset;
//	}
//
//	/**
//	 * Sets total tick counts to be considered in this DBLoader.
//	 * @param ticks Total tick count.
//	 */
//	public void setTickCount(double ticks) {
//		tickCount = ticks;
//	}
//
//	/**
//	 * Adds to the total frequency considered in this DBLoader.
//	 * @param freq Frequency to be added.
//	 */
//	public void addTotalFreq(double freq) {
//		freqTotal += freq;
//	}
//}
