public class EvacuationCentre {
    private String centreName;
    private String location;

    public EvacuationCentre(String centreName, String location) {
        this.centreName = centreName;
        this.location = location;
    }

    public String getCentreName() { return centreName; }
    public String getLocation() { return location; }

    @Override
    public String toString() {
        return centreName + " (" + location + ")";
    }
} 