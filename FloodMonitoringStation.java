public class FloodMonitoringStation {
    private String stationId;
    private String riverName;
    private String location;
    private String date;
    private int waterLevel;

    public FloodMonitoringStation(String stationId, String riverName, String location, String date, int waterLevel) {
        this.stationId = stationId;
        this.riverName = riverName;
        this.location = location;
        this.date = date;
        this.waterLevel = waterLevel;
    }

    public String getStationId() { return stationId; }
    public String getRiverName() { return riverName; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public int getWaterLevel() { return waterLevel; }

    @Override
    public String toString() {
        return stationId + " " + riverName + " " + location + " " + date + " " + waterLevel;
    }
} 