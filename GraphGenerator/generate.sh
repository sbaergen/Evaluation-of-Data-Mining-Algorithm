javac -sourcepath src/ -d bin src/*.java src/data/*.java src/mining/**/*.java
java -cp .:bin:**/**/*.class Main $1
