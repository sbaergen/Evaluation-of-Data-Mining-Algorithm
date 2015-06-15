echo Compiling
#javac exploration.java
javac -sourcepath src/ -d bin src/*.java src/data/*.java src/mining/**/*.java
echo Creating Parameters
##java exploration $1
echo Generating Graph
java -cp .:bin:**/**/*.class Main $1
