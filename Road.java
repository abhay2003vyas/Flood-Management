public class Road {
    private String startLocation;
    private String endLocation;
    private double distance;

    public Road(String startLocation, String endLocation, double distance) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.distance = distance;
    }

    public String getStartLocation() { return startLocation; }
    public String getEndLocation() { return endLocation; }
    public double getDistance() { return distance; }

    @Override
    public String toString() {
        return startLocation + " <-> " + endLocation + " (" + distance + " km)";
    }
} 