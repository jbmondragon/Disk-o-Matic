package algorithms;

import java.util.*;

public class CLOOK extends DiskSchedulingAlgorithm {
    
    @Override
    public int execute(int currentHead, List<Integer> pendingRequests, String direction) {
        reset();
        int current = currentHead;
        addToList(current);
        
        List<Integer> sortedRequests = new ArrayList<>(pendingRequests);
        Collections.sort(sortedRequests);
        
        if (direction.equalsIgnoreCase("right")) {
            // Move right
            for (int req : sortedRequests) {
                if (req >= current) {
                    addMove(current, req);
                    current = req;
                }
            }
            // Jump to smallest request
            if (!sortedRequests.isEmpty() && sortedRequests.get(0) < currentHead) {
                addMove(current, sortedRequests.get(0));
                current = sortedRequests.get(0);
            }
            // Move right again
            for (int req : sortedRequests) {
                if (req > current && req < currentHead) {
                    addMove(current, req);
                    current = req;
                }
            }
        } else {
            // Move left
            for (int i = sortedRequests.size() - 1; i >= 0; i--) {
                int req = sortedRequests.get(i);
                if (req <= current) {
                    addMove(current, req);
                    current = req;
                }
            }
            // Jump to largest request
            if (!sortedRequests.isEmpty() && sortedRequests.get(sortedRequests.size() - 1) > currentHead) {
                addMove(current, sortedRequests.get(sortedRequests.size() - 1));
                current = sortedRequests.get(sortedRequests.size() - 1);
            }
            // Move left again
            for (int i = sortedRequests.size() - 1; i >= 0; i--) {
                int req = sortedRequests.get(i);
                if (req < current && req > currentHead) {
                    addMove(current, req);
                    current = req;
                }
            }
        }
        
        return totalSeekTime;
    }
}
