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
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        int count = getAvailability(roomType);
        if (count > 0) {
            inventory.put(roomType, count - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\nUpdated Inventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }
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
}

/**
 * Booking Queue (FIFO)
 */
class BookingQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

/**
 * Booking Service (Allocation Engine)
 */
class BookingService {

    private RoomInventory inventory;

    // Prevent duplicate room IDs globally
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type → allocated room IDs
    private Map<String, Set<String>> allocationMap = new HashMap<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Process booking queue
     */
    public void processBookings(BookingQueue queue) {

        System.out.println("\nProcessing Booking Requests...\n");

        while (!queue.isEmpty()) {

            Reservation req = queue.getNextRequest();
            String roomType = req.getRoomType();

            int available = inventory.getAvailability(roomType);

            if (available > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Ensure uniqueness (Set)
                allocatedRoomIds.add(roomId);

                // Add to allocation map
                allocationMap
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                // Update inventory (atomic step)
                inventory.decrementRoom(roomType);

                System.out.println("Booking CONFIRMED for " + req.getGuestName() +
                        " | Room Type: " + roomType +
                        " | Room ID: " + roomId);

            } else {
                System.out.println("Booking FAILED for " + req.getGuestName() +
                        " | Room Type: " + roomType +
                        " (No Availability)");
            }
        }
    }

    /**
     * Generate unique room ID
     */
    private String generateRoomId(String roomType) {
        String prefix = roomType.replaceAll(" ", "").substring(0, 2).toUpperCase();
        String id;

        do {
            id = prefix + new Random().nextInt(1000);
        } while (allocatedRoomIds.contains(id));

        return id;
    }

    /**
     * Display allocations
     */
    public void displayAllocations() {
        System.out.println("\nFinal Room Allocations:");

        for (Map.Entry<String, Set<String>> entry : allocationMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

/**
 * Main Application
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====\n");

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Queue
        BookingQueue queue = new BookingQueue();

        // Add booking requests (FIFO)
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Double Room"));

        // Booking Service
        BookingService service = new BookingService(inventory);

        // Process bookings
        service.processBookings(queue);

        // Show results
        service.displayAllocations();
        inventory.displayInventory();
    }
}