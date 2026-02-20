interface SatelliteControl {
    void sendCommand(String command);
}

class GlobalSatelliteController implements SatelliteControl {

    // ELEMENT A: Unique static instance
    private static GlobalSatelliteController instance;

    // ATTRIBUTES (Encapsulation)
    private String satelliteId;
    private int orbitLevel;

    // ELEMENT B: Private Constructor (Prevents 'new' from outside)
    private GlobalSatelliteController() {
        this.satelliteId = "ALPHA-CENTAURI-01";
        this.orbitLevel = 500;
    }

    // ELEMENT C: Static method to get the instance
    public static GlobalSatelliteController getInstance() {
        if (instance == null) {
            instance = new GlobalSatelliteController();
        }
        return instance;
    }

    // METHODS (Behavior & Polymorphism)
    @Override
    public void sendCommand(String command) {
        System.out.println("Encrypting and sending to " + satelliteId + ": " + command);
    }

    // GETTERS AND SETTERS (Encapsulation)
    public String getSatelliteId() { return satelliteId; }
    public void setSatelliteId(String satelliteId) { this.satelliteId = satelliteId; }
}

public class Main {
    public static void main(String[] args) {
        // We obtain the unique instances using the Singleton method
        GlobalSatelliteController controllerA = GlobalSatelliteController.getInstance();
        GlobalSatelliteController controllerB = GlobalSatelliteController.getInstance();

        // Testing the object behavior
        controllerA.sendCommand("Initiate Landing Protocol");

        // Proof of Singleton: Both variables point to the same memory address
        System.out.println("Checking Singleton Pattern...");
        System.out.println("Are both instances the same? " + (controllerA == controllerB));

        // Changing data in one affects the other (because they are the same object)
        controllerB.setSatelliteId("GALAXY-X");
        System.out.println("Updated ID in A via B: " + controllerA.getSatelliteId());
    }
}