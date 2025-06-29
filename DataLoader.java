import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.*;

public class DataLoader {
    public static List<FloodMonitoringStation> loadFloodMonitoringStations(String filePath) {
        List<FloodMonitoringStation> stations = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.US_ASCII);
            for (int i = 1; i < lines.size(); i++) { // skip header
                String[] parts = lines.get(i).split(",");
                if (parts.length < 5) continue;
                stations.add(new FloodMonitoringStation(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    Integer.parseInt(parts[4].trim())
                ));
            }
        } catch (IOException e) {
            System.out.println("Error loading flood monitoring stations: " + e.getMessage());
        }
        return stations;
    }

    public static List<EmergencyResponseTeam> loadEmergencyResponseTeams(String filePath) {
        List<EmergencyResponseTeam> teams = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.US_ASCII);
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts.length < 3) continue;
                teams.add(new EmergencyResponseTeam(
                    parts[0].trim(),
                    Integer.parseInt(parts[1].trim()),
                    parts[2].trim().equalsIgnoreCase("Yes")
                ));
            }
        } catch (IOException e) {
            System.out.println("Error loading emergency response teams: " + e.getMessage());
        }
        return teams;
    }

    public static List<FloodIncident> loadFloodIncidents(String filePath) {
        List<FloodIncident> incidents = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.US_ASCII);
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts.length < 5) continue;
                incidents.add(new FloodIncident(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    Integer.parseInt(parts[3].trim()),
                    parts[4].trim()
                ));
            }
        } catch (IOException e) {
            System.out.println("Error loading flood incidents: " + e.getMessage());
        }
        return incidents;
    }

    public static List<Road> loadRoads(String filePath) {
        List<Road> roads = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.US_ASCII);
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts.length < 3) continue;
                roads.add(new Road(
                    parts[0].trim(),
                    parts[1].trim(),
                    Double.parseDouble(parts[2].trim())
                ));
            }
        } catch (IOException e) {
            System.out.println("Error loading roads: " + e.getMessage());
        }
        return roads;
    }

    public static List<EvacuationCentre> loadEvacuationCentres(String filePath) {
        List<EvacuationCentre> centres = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.US_ASCII);
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts.length < 2) continue;
                centres.add(new EvacuationCentre(
                    parts[0].trim(),
                    parts[1].trim()
                ));
            }
        } catch (IOException e) {
            System.out.println("Error loading evacuation centres: " + e.getMessage());
        }
        return centres;
    }
} 