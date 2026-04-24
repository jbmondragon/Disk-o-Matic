package algorithms;

import java.util.*;

public class FCFS extends DiskSchedulingAlgorithm {
    
    @Override
    public int execute(int currentHead, List<Integer> pendingRequests, String direction) {
        reset();
        int current = currentHead;
        addToList(current);
        
        for (int request : pendingRequests) {
            addMove(current, request);
            current = request;
        }
        
        return totalSeekTime;
    }
}
