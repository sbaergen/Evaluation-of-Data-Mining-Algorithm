echo Compiling
javac exploration.java
javac -sourcepath src/ -d bin src/*.java src/data/*.java src/mining/**/*.java
for i in {1..1}
do
	echo Run $i	
	echo Creating Parameters
	java exploration $1
	echo Generating Graph
	java -Xmx4000m -cp .:bin:**/**/*.class Main $1 $2
done
