import java.util.*;

public class TUI {
    private FloodResponseManager manager;
    private Scanner scanner;

    public TUI(FloodResponseManager manager) {
        this.manager = manager;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nBirmingham Flood Response Management System");
            System.out.println("1. List flood monitoring stations by water level for a date");
            System.out.println("2. Allocate emergency response teams to incidents");
            System.out.println("3. Find nearest evacuation centre for a location");
            System.out.println("4. Analyze water level trends");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    handleListStations();
                    break;
                case "2":
                    handleAllocateTeams();
                    break;
                case "3":
                    handleFindEvacuationCentre();
                    break;
                case "4":
                    handleAnalyzeTrends();
                    break;
                case "0":
                    System.out.println("Exiting. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleListStations() {
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        List<FloodMonitoringStation> stations = manager.getStationsByDateSorted(date);
        if (stations.isEmpty()) {
            System.out.println("No stations found for this date.");
        } else {
            System.out.println("\nStation ID | River Name | Location | Date | Water Level (cm)");
            for (FloodMonitoringStation s : stations) {
                System.out.println(s);
            }
        }
    }

    private void handleAllocateTeams() {
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        List<String> results = manager.allocateTeamsToIncidents(date);
        if (results.isEmpty()) {
            System.out.println("No allocation results available.");
        } else {
            for (String line : results) {
                System.out.println(line);
            }
        }
    }

    private void handleFindEvacuationCentre() {
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        String result = manager.findNearestEvacuationCentre(location);
        if (result.isEmpty()) {
            System.out.println("No evacuation route available.");
        } else {
            System.out.println(result);
        }
    }

    private void handleAnalyzeTrends() {
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        List<String> trends = manager.analyzeWaterLevelTrends(date);
        if (trends.isEmpty()) {
            System.out.println("No trend data available.");
        } else {
            for (String line : trends) {
                System.out.println(line);
            }
        }
    }
} 