javac -sourcepath src/ -d bin src/mining/**/*.java
java -cp .:bin:**/**/*.class mining.manager.MinerManager config.txt counters.txt output.txt $1
