package algorithms;

import java.util.*;

public class LOOK extends DiskSchedulingAlgorithm {
    
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
            // Move left
            for (int i = sortedRequests.size() - 1; i >= 0; i--) {
                int req = sortedRequests.get(i);
                if (req < currentHead) {
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
            // Move right
            for (int req : sortedRequests) {
                if (req > currentHead) {
                    addMove(current, req);
                    current = req;
                }
            }
        }
        
        return totalSeekTime;
    }
}
