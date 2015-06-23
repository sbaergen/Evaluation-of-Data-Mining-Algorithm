import java.io.*;
import java.util.Random;

public class exploration {

	public static void main(String[] args) {
        final int[] MAXATTR = { 1, 1000 };
        final int[] MAXFORWARD = { 0, 100 };
        final int[] MAXBACKARD = { 0, 100 };
        final int[] GAP = { 0, 20 };
        final double[] MINSUPPORT = { 0.5, 1.0 };
        final int[] MAXNODES = { 10, 100 };
        final int[] EFGS = { 1, 100 };
        final int[] NODES = { 10, 10000 };
        final int[] ATTR = { 10, 100 };
        final double[] APROB = { 0.1, 1 };
        final double[] EPROB = { 0, 1 };
        final char[] DIST = { 'U', 'E', 'G' };
        final double[] RATE = { 0, 10 };
        final int[] MIN = { 1, 100 };
        final int MAX = MIN[1];
        final double[] HEIGHT = { 0.1, 5 };
        final int[] CENTRE = { 1, 100 };
        final int[] WIDTH = { 1, 100 };
        
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
            writer.write("\n"+randomInt(numEFGS, NODES[1]));
            writer.write("\n"+randomInt(ATTR[0], ATTR[1]));
            writer.write("\n"+randomDouble(APROB[0], APROB[1]));
            writer.write("\n"+randomDouble(EPROB[0], EPROB[1]));
            for (int i = 0; i < 4; i++){
                char distribution = DIST[randomInt(0,2)];
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
