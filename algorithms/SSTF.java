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
            int closest = findClosest(current, pending, direction);
            addMove(current, closest);
            current = closest;
            pending.remove((Integer) closest);
        }

        return totalSeekTime;
    }

    /**
     * Finds the closest request to the current head. If there is a tie,
     * breaks it using the direction: "left" prefers lower, "right" prefers higher.
     */
    private int findClosest(int current, List<Integer> requests, String direction) {
        int closest = requests.get(0);
        int minDistance = Math.abs(closest - current);

        for (int req : requests) {
            int distance = Math.abs(req - current);
            if (distance < minDistance) {
                minDistance = distance;
                closest = req;
            } else if (distance == minDistance && req != closest) {
                // Tie: break by direction
                if (direction != null && direction.equalsIgnoreCase("left")) {
                    if (req < closest)
                        closest = req;
                } else {
                    if (req > closest)
                        closest = req;
                }
            }
        }
        return closest;
    }
}
