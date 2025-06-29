import java.util.*;

public class FloodResponseManager {
    private List<FloodMonitoringStation> stations;
    private List<EmergencyResponseTeam> teams;
    private List<FloodIncident> incidents;
    private List<Road> roads;
    private List<EvacuationCentre> centres;

    public FloodResponseManager(String dataDir) {
        stations = DataLoader.loadFloodMonitoringStations(dataDir + "/flood_monitoring_stations.csv");
        teams = DataLoader.loadEmergencyResponseTeams(dataDir + "/emergency_response_teams.csv");
        incidents = DataLoader.loadFloodIncidents(dataDir + "/flood_incidents.csv");
        roads = DataLoader.loadRoads(dataDir + "/road_network.csv");
        centres = DataLoader.loadEvacuationCentres(dataDir + "/evacuation_centres.csv");
    }

    // 1. List stations by water level for a specific date
    public List<FloodMonitoringStation> getStationsByDateSorted(String date) {
        List<FloodMonitoringStation> result = new ArrayList<>();
        for (FloodMonitoringStation s : stations) {
            if (s.getDate().equals(date)) {
                result.add(s);
            }
        }
        result.sort((a, b) -> Integer.compare(b.getWaterLevel(), a.getWaterLevel()));
        return result;
    }

    // 2. Allocate emergency response teams to incidents
    public List<String> allocateTeamsToIncidents(String date) {
        // Filter incidents for the given date and sort by water level (descending)
        List<FloodIncident> todaysIncidents = new ArrayList<>();
        for (FloodIncident fi : incidents) {
            if (fi.getDate().equals(date)) {
                todaysIncidents.add(fi);
            }
        }
        todaysIncidents.sort((a, b) -> Integer.compare(b.getWaterLevel(), a.getWaterLevel()));

        // Prepare available teams and their current assignments
        List<EmergencyResponseTeam> availableTeams = new ArrayList<>();
        Map<String, Integer> teamAssignments = new HashMap<>();
        for (EmergencyResponseTeam team : teams) {
            if (team.isAvailable()) {
                availableTeams.add(team);
                teamAssignments.put(team.getTeamId(), 0);
            }
        }

        List<String> results = new ArrayList<>();
        results.add("Emergency Response Team Allocation (" + date + ")");
        results.add("-----------------------------------------------------");
        results.add(String.format("%-15s %-12s %-10s %-13s %-10s", "Flood Location", "River Name", "Level(cm)", "Assigned Team", "Status"));

        Queue<FloodIncident> waitingList = new LinkedList<>();

        for (FloodIncident incident : todaysIncidents) {
            // Find the least-loaded available team with capacity
            EmergencyResponseTeam chosenTeam = null;
            int minAssignments = Integer.MAX_VALUE;
            for (EmergencyResponseTeam team : availableTeams) {
                int assigned = teamAssignments.get(team.getTeamId());
                if (assigned < team.getMaxCapacity() && assigned < minAssignments) {
                    minAssignments = assigned;
                    chosenTeam = team;
                }
            }
            if (chosenTeam != null) {
                teamAssignments.put(chosenTeam.getTeamId(), teamAssignments.get(chosenTeam.getTeamId()) + 1);
                results.add(String.format("%-15s %-12s %-10d %-13s %-10s",
                    incident.getLocation(), incident.getRiverName(), incident.getWaterLevel(), chosenTeam.getTeamId(), "Assigned"));
            } else {
                waitingList.add(incident);
                results.add(String.format("%-15s %-12s %-10d %-13s %-10s",
                    incident.getLocation(), incident.getRiverName(), incident.getWaterLevel(), "-", "Waiting"));
            }
        }
        return results;
    }

    // 3. Find nearest evacuation centre using shortest route
    public String findNearestEvacuationCentre(String startLocation) {
        // Build the graph
        Map<String, List<Road>> graph = new HashMap<>();
        for (Road road : roads) {
            graph.computeIfAbsent(road.getStartLocation(), k -> new ArrayList<>()).add(road);
            // Bidirectional
            graph.computeIfAbsent(road.getEndLocation(), k -> new ArrayList<>()).add(
                new Road(road.getEndLocation(), road.getStartLocation(), road.getDistance()));
        }
        // Dijkstra's algorithm for each centre
        double minDistance = Double.MAX_VALUE;
        String bestCentre = null;
        List<String> bestPath = null;
        for (EvacuationCentre centre : centres) {
            DijkstraResult result = dijkstra(graph, startLocation, centre.getLocation());
            if (result != null && result.distance < minDistance) {
                minDistance = result.distance;
                bestCentre = centre.getCentreName();
                bestPath = result.path;
            }
        }
        if (bestCentre == null) {
            return "No evacuation route available.";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Nearest Evacuation Centre\n");
            sb.append("-----------------------------------------------------\n");
            sb.append(String.format("%-15s %-25s %-35s %-18s\n", "Given Location", "Nearest Evacuation Centre", "Evacuation Route", "Total Distance (km)"));
            sb.append(String.format("%-15s %-25s %-35s %-18s",
                startLocation,
                bestCentre,
                String.join(" -> ", bestPath),
                String.format("%.1f", minDistance)
            ));
            return sb.toString();
        }
    }

    // Helper class for Dijkstra's result
    private static class DijkstraResult {
        double distance;
        List<String> path;
        DijkstraResult(double distance, List<String> path) {
            this.distance = distance;
            this.path = path;
        }
    }

    // Dijkstra's algorithm implementation
    private DijkstraResult dijkstra(Map<String, List<Road>> graph, String start, String end) {
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        for (String node : graph.keySet()) {
            dist.put(node, Double.MAX_VALUE);
        }
        dist.put(start, 0.0);
        pq.add(start);
        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (u.equals(end)) break;
            for (Road road : graph.getOrDefault(u, Collections.emptyList())) {
                double alt = dist.get(u) + road.getDistance();
                if (alt < dist.getOrDefault(road.getEndLocation(), Double.MAX_VALUE)) {
                    dist.put(road.getEndLocation(), alt);
                    prev.put(road.getEndLocation(), u);
                    pq.add(road.getEndLocation());
                }
            }
        }
        if (!dist.containsKey(end) || dist.get(end) == Double.MAX_VALUE) return null;
        // Reconstruct path
        LinkedList<String> path = new LinkedList<>();
        String curr = end;
        while (curr != null) {
            path.addFirst(curr);
            curr = prev.get(curr);
        }
        return new DijkstraResult(dist.get(end), path);
    }

    // 4. Analyze water level trends
    public List<String> analyzeWaterLevelTrends(String date) {
        List<String> results = new ArrayList<>();
        results.add("Flood Monitoring Station Trend Analysis (" + date + ")");
        results.add("-----------------------------------------------------");
        results.add(String.format("%-10s %-15s %-15s %-20s %-20s", "Station ID", "River Name", "Location", "Trend (Last 7 Days)", "Trend (Last 30 Days)"));
        Map<String, List<FloodMonitoringStation>> stationMap = new HashMap<>();
        for (FloodMonitoringStation s : stations) {
            stationMap.computeIfAbsent(s.getStationId(), k -> new ArrayList<>()).add(s);
        }
        for (String stationId : stationMap.keySet()) {
            List<FloodMonitoringStation> records = stationMap.get(stationId);
            // Sort records by date ascending
            records.sort(Comparator.comparing(FloodMonitoringStation::getDate));
            String trend7 = getTrend(records, date, 7);
            String trend30 = getTrend(records, date, 30);
            FloodMonitoringStation s = records.get(records.size() - 1);
            results.add(String.format("%-10s %-15s %-15s %-20s %-20s",
                s.getStationId(), s.getRiverName(), s.getLocation(), trend7, trend30));
        }
        return results;
    }

    // Helper to get trend for a period ending at 'date'
    private String getTrend(List<FloodMonitoringStation> records, String endDate, int days) {
        // Find the last record on or before endDate
        FloodMonitoringStation last = null;
        for (int i = records.size() - 1; i >= 0; i--) {
            if (records.get(i).getDate().compareTo(endDate) <= 0) {
                last = records.get(i);
                break;
            }
        }
        if (last == null) return "No Data";
        // Find the record 'days' before endDate
        FloodMonitoringStation first = null;
        for (int i = records.size() - 1; i >= 0; i--) {
            if (daysBetween(records.get(i).getDate(), last.getDate()) >= days) {
                first = records.get(i);
                break;
            }
        }
        if (first == null) first = records.get(0);
        int diff = last.getWaterLevel() - first.getWaterLevel();
        if (diff > 2) return "Increasing";
        if (diff < -2) return "Decreasing";
        return "Stable";
    }

    // Helper to calculate days between two dates (YYYY-MM-DD)
    private int daysBetween(String d1, String d2) {
        try {
            java.time.LocalDate date1 = java.time.LocalDate.parse(d1);
            java.time.LocalDate date2 = java.time.LocalDate.parse(d2);
            return (int) java.time.temporal.ChronoUnit.DAYS.between(date1, date2);
        } catch (Exception e) {
            return 0;
        }
    }

    // Getters for TUI or other use
    public List<FloodMonitoringStation> getStations() { return stations; }
    public List<EmergencyResponseTeam> getTeams() { return teams; }
    public List<FloodIncident> getIncidents() { return incidents; }
    public List<Road> getRoads() { return roads; }
    public List<EvacuationCentre> getCentres() { return centres; }
} 