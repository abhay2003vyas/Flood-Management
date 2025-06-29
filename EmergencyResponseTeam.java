public class EmergencyResponseTeam {
    private String teamId;
    private int maxCapacity;
    private boolean available;
    private int assignedIncidents;

    public EmergencyResponseTeam(String teamId, int maxCapacity, boolean available) {
        this.teamId = teamId;
        this.maxCapacity = maxCapacity;
        this.available = available;
        this.assignedIncidents = 0;
    }

    public String getTeamId() { return teamId; }
    public int getMaxCapacity() { return maxCapacity; }
    public boolean isAvailable() { return available; }
    public int getAssignedIncidents() { return assignedIncidents; }
    public void assignIncident() { this.assignedIncidents++; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return teamId + " (Capacity: " + maxCapacity + ", Available: " + available + ")";
    }
} 