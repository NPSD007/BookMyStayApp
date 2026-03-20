import java.io.*;
import java.util.*;

/**
 * Reservation (Serializable)
 */
class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

/**
 * Inventory (Serializable)
 */
class RoomInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    public void setInventory(Map<String, Integer> data) {
        this.inventory = data;
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void display() {
        System.out.println("\nInventory:");
        for (var e : inventory.entrySet()) {
            System.out.println(e.getKey() + " → " + e.getValue());
        }
    }
}

/**
 * Booking History (Serializable)
 */
class BookingHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Reservation> history = new ArrayList<>();

    public void add(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAll() {
        return history;
    }

    public void display() {
        System.out.println("\nBooking History:");
        for (Reservation r : history) {
            r.display();
        }
    }
}

/**
 * Persistence Service
 */
class PersistenceService {

    private static final String FILE_NAME = "bookmystay.dat";

    /**
     * Save state to file
     */
    public void save(BookingHistory history, RoomInventory inventory) {

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(history);
            oos.writeObject(inventory);

            System.out.println("\nData saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Load state from file
     */
    public Object[] load() {

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            BookingHistory history = (BookingHistory) ois.readObject();
            RoomInventory inventory = (RoomInventory) ois.readObject();

            System.out.println("Data loaded successfully!\n");

            return new Object[]{history, inventory};

        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data. Starting with safe defaults.");
        }

        return null;
    }
}

/**
 * Main Application
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App (Persistence Demo) =====");

        PersistenceService persistence = new PersistenceService();

        BookingHistory history;
        RoomInventory inventory;

        // 🔹 Load existing data (Recovery)
        Object[] data = persistence.load();

        if (data != null) {
            history = (BookingHistory) data[0];
            inventory = (RoomInventory) data[1];
        } else {
            // Fresh start
            history = new BookingHistory();
            inventory = new RoomInventory();
        }

        // 🔹 Simulate new bookings
        history.add(new Reservation("R101", "Alice", "Single Room"));
        history.add(new Reservation("R102", "Bob", "Double Room"));

        // 🔹 Display current state
        history.display();
        inventory.display();

        // 🔹 Save state before shutdown
        persistence.save(history, inventory);

        System.out.println("\nRestart app to see recovery in action!");
    }
}