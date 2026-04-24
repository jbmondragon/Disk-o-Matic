package algorithms;

import java.util.*;

public class SSTF extends DiskSchedulingAlgorithm {
    
    @Override
    public int execute(int currentHead, List<Integer> pendingRequests, String direction) {
        reset();
        int current = currentHead;
        addToList(current);
        
        List<Integer> pending = new ArrayList<>(pendingRequests);
        
        while (!pending.isEmpty()) {
            int closest = findClosest(current, pending);
            addMove(current, closest);
            current = closest;
            pending.remove((Integer) closest);
        }
        
        return totalSeekTime;
    }
    
    private int findClosest(int current, List<Integer> requests) {
        int closest = requests.get(0);
        int minDistance = Math.abs(closest - current);
        
        for (int req : requests) {
            int distance = Math.abs(req - current);
            if (distance < minDistance) {
                minDistance = distance;
                closest = req;
            }
        }
        return closest;
    }
}
