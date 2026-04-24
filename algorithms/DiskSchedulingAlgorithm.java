package algorithms;

import java.util.*;

public abstract class DiskSchedulingAlgorithm {
    protected static final int CYLINDER_MIN = 0;
    protected static final int CYLINDER_MAX = 199;
    
    protected List<Integer> serviceSequence;
    protected int totalSeekTime;
    
    public DiskSchedulingAlgorithm() {
        serviceSequence = new ArrayList<>();
        totalSeekTime = 0;
    }
    
    /**
     * Execute the disk scheduling algorithm
     * @param currentHead Starting position of R/W head (0-199)
     * @param pendingRequests List of cylinder requests
     * @param direction Initial direction ("left" or "right") - used for SCAN, C-SCAN, LOOK, C-LOOK
     * @return Total seek time
     */
    public abstract int execute(int currentHead, List<Integer> pendingRequests, String direction);
    
    /**
     * Record a move from one cylinder to another
     */
    protected void addMove(int from, int to) {
        int seek = Math.abs(to - from);
        totalSeekTime += seek;
        addToList(to);
    }
    
    /**
     * Add a cylinder to the service sequence
     */
    protected void addToList(int cylinder) {
        serviceSequence.add(cylinder);
    }
    
    /**
     * Reset the algorithm state for a new simulation
     */
    protected void reset() {
        serviceSequence.clear();
        totalSeekTime = 0;
    }
    
    /**
     * Get the sequence of cylinders serviced (including starting position)
     */
    public List<Integer> getServiceSequence() {
        return new ArrayList<>(serviceSequence);
    }
    
    /**
     * Get the total seek time
     */
    public int getTotalSeekTime() {
        return totalSeekTime;
    }
    
    /**
     * Get the average seek time
     */
    public double getAverageSeekTime() {
        if (serviceSequence.size() <= 1) return 0;
        return (double) totalSeekTime / (serviceSequence.size() - 1);
    }
}
