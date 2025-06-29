public class Main {
    public static void main(String[] args) {
        String dataDir = "CS4330_cwkData";
        FloodResponseManager manager = new FloodResponseManager(dataDir);
        TUI tui = new TUI(manager);
        tui.start();
    }
} 