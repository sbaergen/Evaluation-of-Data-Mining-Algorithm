javac -sourcepath src/ -d bin src/*.java src/data/*.java src/mining/**/*.java
java -Xmx4096m -cp .:bin:**/**/*.class Main $1