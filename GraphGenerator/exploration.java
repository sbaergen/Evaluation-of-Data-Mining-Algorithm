import java.io.*;
import java.util.Random;

public class exploration {

	public static void main(String[] args) {
        final int[] MAXATTR = { 5, 5 };
        final int[] MAXFORWARD = { 5, 5 };
        final int[] MAXBACKARD = { 5, 5 };
        final int[] GAP = { 0, 0 };
        final double[] MINSUPPORT = { 0.11, 0.11 };
        final int[] MAXNODES = { 10, 10 };
        final int[] EFGS = { 10, 10 };
        final int[] NODES = { 1000, 1000 };
        final int[] ATTR = { 10, 10 };
        final double[] APROB = { 0.30, 0.30 };
        final double[] EPROB = { .1, .1 };
        final char[] DIST = { 'U' };
        final double[] RATE = { 1, 1 };
        final int[] MIN = { 10, 10 };
        final int MAX = MIN[1];
        final double[] HEIGHT = { 1, 1 };
        final int[] CENTRE = { 10, 10 };
        final int[] WIDTH = { 10, 10 };
        final int[] PATEFGS = { 1, 1 };
        final int[] PATNODE = { 10, 10 };
        final double[] PATPROB = { 0., 0. };
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(args[0]));
            writer.write(""+randomInt(MAXATTR[0], MAXATTR[1]));
            writer.write("\n"+randomInt(MAXFORWARD[0], MAXFORWARD[1]));
            writer.write("\n"+randomInt(MAXBACKARD[0], MAXBACKARD[1]));
            writer.write("\n"+randomInt(GAP[0], GAP[1]));
            writer.write("\n"+randomDouble(MINSUPPORT[0], MINSUPPORT[1]));
            writer.write("\n"+randomInt(MAXNODES[0], MAXNODES[1]));
            int numEFGS = randomInt(EFGS[0], EFGS[1]);
            writer.write("\n"+numEFGS);
            int numNodes = randomInt(NODES[0], NODES[1]);
            if (numNodes < numEFGS)
                NODES[0] = numEFGS;
            if (NODES[1] < numEFGS)
                NODES[1] = numEFGS;
            writer.write("\n"+randomInt(NODES[0], NODES[1]));
            writer.write("\n"+randomInt(ATTR[0], ATTR[1]));
            writer.write("\n"+randomDouble(APROB[0], APROB[1]));
            writer.write("\n"+randomDouble(EPROB[0], EPROB[1]));
            for (int i = 0; i < 5; i++){
                char distribution = DIST[randomInt(0, 0)];
                writer.write("\n"+distribution);
                switch(distribution) {
                    case('U'):
                        int minimum = randomInt(MIN[0], MIN[1]);
                        writer.write("\n"+minimum);
                        writer.write("\n"+randomInt(minimum, MAX));
                        break;
                    case ('E'):
                        writer.write("\n"+randomDouble(RATE[0], RATE[1]));
                        break;
                    case ('G'):
                        writer.write("\n"+randomDouble(HEIGHT[0], HEIGHT[1]));
                        writer.write("\n"+randomInt(CENTRE[0], CENTRE[1]));
                        writer.write("\n"+randomInt(WIDTH[0], WIDTH[1]));
                        break;
                }
            }
            numEFGS = randomInt(PATEFGS[0], PATEFGS[1]);
            writer.write("\n"+numEFGS);
            numNodes = randomInt(PATNODE[0], PATNODE[1]);
            if (numNodes < numEFGS)
                PATNODE[0] = numEFGS;
            if (PATNODE[1] < numEFGS)
                PATNODE[1] = numEFGS;
            writer.write("\n"+randomInt(PATNODE[0], PATNODE[1]));
            writer.write("\n"+randomDouble(PATPROB[0], PATPROB[1]));
            
            writer.close();

        } catch (IOException e ){
            System.err.println("FILE ERROR");
        }
	}

	public static int randomInt(int min, int max) {
		Random rd = new Random();
		return rd.nextInt(max - min + 1) + min;
	}

	public static double randomDouble(double min, double max) {
		Random rd = new Random();
		return min + (max - min) * rd.nextDouble();
	}
}
