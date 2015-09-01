javac -sourcepath src/ -d build src/mining/**/*.java
java -cp .:build:**/**/*.class mining.manager.MinerManager $1 $2 $3 $4
