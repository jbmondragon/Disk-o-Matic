package algorithms;

import java.util.*;

public class SCAN extends DiskSchedulingAlgorithm {
    
    @Override
    public int execute(int currentHead, List<Integer> pendingRequests, String direction) {
        reset();
        int current = currentHead;
        addToList(current);
        
        List<Integer> sortedRequests = new ArrayList<>(pendingRequests);
        Collections.sort(sortedRequests);
        
        if (direction.equalsIgnoreCase("right")) {
            // Move right first
            for (int req : sortedRequests) {
                if (req >= current) {
                    addMove(current, req);
                    current = req;
                }
            }
            // Go to end
            if (current < CYLINDER_MAX) {
                addMove(current, CYLINDER_MAX);
                current = CYLINDER_MAX;
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
            // Move left first
            for (int i = sortedRequests.size() - 1; i >= 0; i--) {
                int req = sortedRequests.get(i);
                if (req <= current) {
                    addMove(current, req);
                    current = req;
                }
            }
            // Go to start
            if (current > CYLINDER_MIN) {
                addMove(current, CYLINDER_MIN);
                current = CYLINDER_MIN;
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
