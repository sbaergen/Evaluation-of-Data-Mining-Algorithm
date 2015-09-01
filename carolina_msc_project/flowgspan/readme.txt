How to run
==========
flowgspan <configuration file name, e.g. config.txt> <name of file with list of performance counters to be considered, e.g. counters.txt> <name of output file with mining results>

Description of configuration file
=================================
<database address, e.g. localhost if DB2 database is located at the same computer mining is going to happen>
<database name from which data is to be loaded>
<connection port to the database>
<user name to access database; tool prompts user for a password>
<prefix of dataset to be loaded, i.e. all tables have a prefix name followed by the table type, such as <PREFIX>_IA, <PREFIX>_DISASSM, etc.>
<whether each EFG node is considered to be a bytecode instead of an assembly instruction; 1 is true, 0 is false>
<maximum number of attributes, i.e. the number of attributes allowed per pattern>
<minimum method hotness, i.e. the threshold that determines whether a method is considered as part of the dataset to be mined. Calculation: method_hotness/total_hotness>
<maximum number of new forward edge additions allowed when extending a pattern. 0 means there is no limit>
<maximum number of new backward edge additions allowed when extending a pattern. 0 means there is no limit>
<gap value allowed when searching for pattern instances>
<number of threads during data loading phase>
<number of threads during mining phase>
<minimum support value, i.e. the threshold that determines whether a pattern is heavyweight and thus part of the output>
<maximum number of nodes that a pattern can be composed of>
