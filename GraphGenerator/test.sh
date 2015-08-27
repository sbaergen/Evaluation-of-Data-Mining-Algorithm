

for p in {2,1,0}
do
for i in {1..5}
do
    sed "s|final double\[\] MINSUPPORT = .*|final double\[\] MINSUPPORT = { 0.$p$i, 0.$p$i };|" exploration.java > explorationa.java
rm exploration.java
mv explorationa.java exploration.java
for j in {1..5}
do
for k in {0,5}
do
sed "s|final int\[\] ATTR = .*|final int\[\] ATTR = { $j$k, $j$k };|" exploration.java > explorationa.java
rm exploration.java
mv explorationa.java exploration.java
for n in {1..3}
do
for o in {0,5}
do
sed "s|final int\[\] PATNODE = .*|final int\[\] PATNODE = { $n$o, $n$o };|g" exploration.java > explorationa.java
rm exploration.java
mv explorationa.java exploration.java
for l in {0..7}
do
for m in {0,5}
do
sed "s|final double\[\] PATPROB = .*|final double\[\] PATPROB = { 0.$l$m, 0.$l$m };|g" exploration.java > explorationa.java
rm exploration.java
mv explorationa.java exploration.java
./generate.sh explore.txt patterns.txt
done
done
echo rename
    mv patterns.txt patterns$n$o.txt
done
done
done
done
done
done
