for i in 0 1 2 3 4 5 6 7 8 9
do
	echo $i
	echo Compiling
	javac exploration.java
	javac -sourcepath src/ -d bin src/*.java src/data/*.java src/mining/**/*.java
	echo Creating Parameters
	java exploration $1
	echo Generating Graph
	java -Xmx4096m -cp .:bin:**/**/*.class Main $1
done
