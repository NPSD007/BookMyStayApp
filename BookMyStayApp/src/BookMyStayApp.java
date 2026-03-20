import java.util.*;

/**
 * Reservation (Confirmed Booking)
 */
class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isActive;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isActive = true;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
    public boolean isActive() { return isActive; }

    public void cancel() {
        this.isActive = false;
    }

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType +
                " | RoomID: " + roomId +
                " | Status: " + (isActive ? "ACTIVE" : "CANCELLED"));
    }
}

/**
 * Inventory
 */
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        inventory.put(type, getAvailability(type) - 1);
    }

    public void increment(String type) {
        inventory.put(type, getAvailability(type) + 1);
    }

    public void display() {
        System.out.println("\nInventory:");
        for (var e : inventory.entrySet()) {
            System.out.println(e.getKey() + " → " + e.getValue());
        }
    }
}

/**
 * Booking History
 */
class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    public void add(Reservation r) {
        history.add(r);
    }

    public Reservation findById(String id) {
        for (Reservation r : history) {
            if (r.getReservationId().equals(id)) return r;
        }
        return null;
    }

    public void display() {
        System.out.println("\nBooking History:");
        for (Reservation r : history) {
            r.display();
        }
    }
}

/**
 * Booking Service (Simple Allocation)
 */
class BookingService {

    private RoomInventory inventory;
    private Set<String> usedRoomIds = new HashSet<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public Reservation book(String guest, String type, String resId) {

        if (inventory.getAvailability(type) <= 0) {
            System.out.println("Booking FAILED for " + guest);
            return null;
        }

        String roomId = generateRoomId(type);

        inventory.decrement(type);

        System.out.println("Booking CONFIRMED for " + guest +
                " | RoomID: " + roomId);

        return new Reservation(resId, guest, type, roomId);
    }

    private String generateRoomId(String type) {
        String id;
        do {
            id = type.substring(0, 2).toUpperCase() + new Random().nextInt(100);
        } while (usedRoomIds.contains(id));

        usedRoomIds.add(id);
        return id;
    }

    public void releaseRoomId(String roomId) {
        usedRoomIds.remove(roomId);
    }
}

/**
 * Cancellation Service (Rollback using Stack)
 */
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;
    private BookingService bookingService;

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(RoomInventory inventory,
                               BookingHistory history,
                               BookingService bookingService) {
        this.inventory = inventory;
        this.history = history;
        this.bookingService = bookingService;
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nAttempting cancellation for ID: " + reservationId);

        Reservation r = history.findById(reservationId);

        // Validation
        if (r == null) {
            System.out.println("Cancellation FAILED → Reservation not found");
            return;
        }

        if (!r.isActive()) {
            System.out.println("Cancellation FAILED → Already cancelled");
            return;
        }

        // Step 1: Push to rollback stack
        rollbackStack.push(r.getRoomId());

        // Step 2: Restore inventory
        inventory.increment(r.getRoomType());

        // Step 3: Release room ID
        bookingService.releaseRoomId(r.getRoomId());

        // Step 4: Mark cancelled
        r.cancel();

        System.out.println("Cancellation SUCCESS for " + r.getGuestName());
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (LIFO): " + rollbackStack);
    }
}

/**
 * Main Application
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====");

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        BookingService bookingService = new BookingService(inventory);

        // Create bookings
        Reservation r1 = bookingService.book("Alice", "Single Room", "R1");
        Reservation r2 = bookingService.book("Bob", "Double Room", "R2");

        history.add(r1);
        history.add(r2);

        history.display();
        inventory.display();

        // Cancellation Service
        CancellationService cancelService =
                new CancellationService(inventory, history, bookingService);

        // Perform cancellations
        cancelService.cancelBooking("R1"); // valid
        cancelService.cancelBooking("R1"); // duplicate
        cancelService.cancelBooking("R3"); // invalid

        // Final state
        history.display();
        inventory.display();
        cancelService.displayRollbackStack();
    }
}