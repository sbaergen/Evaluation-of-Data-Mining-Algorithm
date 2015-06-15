package mining.algorithm;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by seanbaergen on 15-06-05.
 */
public class ReturnInfo {

    private LinkedHashMap<Integer, Integer> numPatternsPerNumEdges;
    private AtomicInteger numHotSubgraphs;
    private int count;

    public ReturnInfo() {

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public AtomicInteger getNumHotSubgraphs() {
        return numHotSubgraphs;
    }

    public void setNumHotSubgraphs(AtomicInteger numHotSubgraphs) {
        this.numHotSubgraphs = numHotSubgraphs;
    }

    public LinkedHashMap<Integer, Integer> getNumPatternsPerNumEdges() {
        return numPatternsPerNumEdges;
    }

    public void setNumPatternsPerNumEdges(LinkedHashMap<Integer, Integer> numPatternsPerNumEdges) {
        this.numPatternsPerNumEdges = numPatternsPerNumEdges;
    }
}
