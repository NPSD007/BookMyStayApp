import java.util.*;

/**
 * Custom Exception for Invalid Booking
 */
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * Reservation
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
 * Room Inventory
 */
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    public void decrement(String roomType) throws InvalidBookingException {
        int count = getAvailability(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }

        inventory.put(roomType, count - 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }
}

/**
 * Validator Class
 */
class BookingValidator {

    public static void validate(Reservation reservation, RoomInventory inventory)
            throws InvalidBookingException {

        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException("Room not available: " + reservation.getRoomType());
        }
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

    public Reservation getNext() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

/**
 * Booking Service with Validation
 */
class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBookings(BookingQueue queue) {

        System.out.println("\nProcessing Bookings...\n");

        while (!queue.isEmpty()) {

            Reservation r = queue.getNext();

            try {
                // Validate first (Fail-Fast)
                BookingValidator.validate(r, inventory);

                // If valid → allocate
                inventory.decrement(r.getRoomType());

                System.out.println("Booking SUCCESS for " + r.getGuestName()
                        + " | Room: " + r.getRoomType());

            } catch (InvalidBookingException e) {
                // Graceful failure
                System.out.println("Booking FAILED for " + r.getGuestName()
                        + " → " + e.getMessage());
            }
        }
    }
}

/**
 * Main Application
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====\n");

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Valid booking
        queue.addRequest(new Reservation("Alice", "Single Room"));

        // Invalid room type
        queue.addRequest(new Reservation("Bob", "Luxury Room"));

        // No availability
        queue.addRequest(new Reservation("Charlie", "Suite Room"));

        // Empty guest name
        queue.addRequest(new Reservation("", "Double Room"));

        BookingService service = new BookingService(inventory);

        service.processBookings(queue);

        System.out.println("\nSystem continues running safely after errors.");
    }
}