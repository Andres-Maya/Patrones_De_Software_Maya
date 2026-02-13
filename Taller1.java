interface SatelliteSystem {
    void transmitCommand(String command);
}

class GlobalSatelliteController implements SatelliteSystem {

    // ELEMENT 1: Unique static instance (The "One and Only")
    private static GlobalSatelliteController instance;

    // ATTRIBUTES (Encapsulation)
    private String satelliteId;
    private boolean isLinked;

    // ELEMENT 2: Private Constructor
    // Prevents other classes from using 'new GlobalSatelliteController()'
    private GlobalSatelliteController() {
        this.satelliteId = "NASA-SATELLITE-X1";
        this.isLinked = true;
    }

    // ELEMENT 3: Static method to get the instance
    public static GlobalSatelliteController getInstance() {
        if (instance == null) {
            instance = new GlobalSatelliteController();
        }
        return instance;
    }

    // METHODS (Behavior & Polymorphism)
    @Override
    public void transmitCommand(String command) {
        System.out.println("Sending to " + satelliteId + ": [" + command + "]");
    }

    // GETTERS AND SETTERS (Encapsulation)
    public String getSatelliteId() { return satelliteId; }
    public void setSatelliteId(String satelliteId) { this.satelliteId = satelliteId; }
}

public class Main {
    public static void main(String[] args) {
        // We cannot use 'new' here. We must call the static method.
        SatelliteSystem controllerA = GlobalSatelliteController.getInstance();
        SatelliteSystem controllerB = GlobalSatelliteController.getInstance();

        // Using the object
        controllerA.transmitCommand("Adjust trajectory +2.5 degrees");

        // PROOF OF SINGLETON:
        // Comparing memory addresses to see if they are the same object
        System.out.println("Are both controllers the same instance? " + (controllerA == controllerB));
    }
}