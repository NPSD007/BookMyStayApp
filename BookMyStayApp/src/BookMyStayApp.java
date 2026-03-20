import java.util.*;

/**
 * Reservation (Confirmed Booking)
 */
class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * Add-On Service (Optional Feature)
 */
class AddOnService {

    private String serviceName;
    private double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void displayService() {
        System.out.println(serviceName + " ($" + price + ")");
    }
}

/**
 * Add-On Service Manager
 */
class AddOnServiceManager {

    // Map: Reservation ID → List of Services
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    /**
     * Add service to reservation
     */
    public void addService(String reservationId, AddOnService service) {

        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Service added: " + service.getServiceName() +
                " → Reservation ID: " + reservationId);
    }

    /**
     * Display services for a reservation
     */
    public void displayServices(String reservationId) {

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services for Reservation: " + reservationId);
            return;
        }

        System.out.println("\nServices for Reservation " + reservationId + ":");

        for (AddOnService service : services) {
            service.displayService();
        }
    }

    /**
     * Calculate total add-on cost
     */
    public double calculateTotalCost(String reservationId) {

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null) return 0;

        double total = 0;

        for (AddOnService s : services) {
            total += s.getPrice();
        }

        return total;
    }
}

/**
 * Main Application
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====\n");

        // Simulated confirmed reservations (from Use Case 6)
        Reservation r1 = new Reservation("R101", "Alice", "Single Room");
        Reservation r2 = new Reservation("R102", "Bob", "Double Room");

        // Add-On Services
        AddOnService wifi = new AddOnService("WiFi", 10);
        AddOnService breakfast = new AddOnService("Breakfast", 15);
        AddOnService parking = new AddOnService("Parking", 20);

        // Service Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects services
        System.out.println("Adding services...\n");

        manager.addService(r1.getReservationId(), wifi);
        manager.addService(r1.getReservationId(), breakfast);

        manager.addService(r2.getReservationId(), parking);

        // Display services
        manager.displayServices(r1.getReservationId());
        manager.displayServices(r2.getReservationId());

        // Calculate cost
        System.out.println("\nTotal Add-On Cost:");

        System.out.println("Reservation " + r1.getReservationId() +
                " → $" + manager.calculateTotalCost(r1.getReservationId()));

        System.out.println("Reservation " + r2.getReservationId() +
                " → $" + manager.calculateTotalCost(r2.getReservationId()));

        System.out.println("\nCore booking & inventory remain unchanged.");
    }
}