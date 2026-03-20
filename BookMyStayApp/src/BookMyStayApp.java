import java.util.*;

/**
 * Abstract Room class
 */
abstract class Room {

    private String roomType;
    private int beds;
    private int size;
    private double price;

    public Room(String roomType, int beds, int size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Beds      : " + beds);
        System.out.println("Size      : " + size + " sq ft");
        System.out.println("Price     : $" + price + " per night");
    }
}

/**
 * Room Types
 */
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200, 80);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350, 120);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600, 250);
    }
}

/**
 * Centralized Inventory
 */
class RoomInventory {

    private HashMap<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 10);
        inventory.put("Double Room", 6);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

/**
 * Reservation (Booking Request)
 */
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayRequest() {
        System.out.println("Guest: " + guestName + " | Requested: " + roomType);
    }
}

/**
 * Booking Queue (FIFO)
 */
class BookingQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    /**
     * Add request to queue
     */
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    /**
     * View all queued requests
     */
    public void displayQueue() {
        System.out.println("\nBooking Requests in Queue (FIFO Order):\n");

        for (Reservation r : queue) {
            r.displayRequest();
        }
    }
}

/**
 * Main Application
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("       Book My Stay App");
        System.out.println("==================================\n");

        // Initialize inventory (read-only here)
        RoomInventory inventory = new RoomInventory();

        // Initialize booking queue
        BookingQueue bookingQueue = new BookingQueue();

        // Simulating guest booking requests
        System.out.println("Guests submitting booking requests...\n");

        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));
        bookingQueue.addRequest(new Reservation("David", "Single Room"));

        // Display queue (FIFO order)
        bookingQueue.displayQueue();

        System.out.println("\nAll requests stored in arrival order.");
        System.out.println("No rooms allocated yet (inventory unchanged).");
    }
}