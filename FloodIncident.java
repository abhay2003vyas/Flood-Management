public class FloodIncident {
    private String incidentId;
    private String location;
    private String riverName;
    private int waterLevel;
    private String date;

    public FloodIncident(String incidentId, String location, String riverName, int waterLevel, String date) {
        this.incidentId = incidentId;
        this.location = location;
        this.riverName = riverName;
        this.waterLevel = waterLevel;
        this.date = date;
    }

    public String getIncidentId() { return incidentId; }
    public String getLocation() { return location; }
    public String getRiverName() { return riverName; }
    public int getWaterLevel() { return waterLevel; }
    public String getDate() { return date; }

    @Override
    public String toString() {
        return incidentId + " " + location + " " + riverName + " " + waterLevel + " " + date;
    }
} 