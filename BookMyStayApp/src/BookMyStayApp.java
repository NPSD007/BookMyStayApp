import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class representing a generic Room.
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

    public int getBeds() {
        return beds;
    }

    public int getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Beds      : " + beds);
        System.out.println("Size      : " + size + " sq ft");
        System.out.println("Price     : $" + price + " per night");
    }
}

/**
 * Single Room type
 */
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200, 80);
    }
}

/**
 * Double Room type
 */
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350, 120);
    }
}

/**
 * Suite Room type
 */
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600, 250);
    }
}

/**
 * Centralized inventory using HashMap
 */
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 10);
        inventory.put("Double Room", 6);
        inventory.put("Suite Room", 0); // Example: Suite currently unavailable
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public HashMap<String, Integer> getInventory() {
        return inventory;
    }
}

/**
 * SearchService provides read-only access to room availability.
 */
class SearchService {

    private RoomInventory inventory;

    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Displays only available rooms
     */
    public void searchAvailableRooms(Room[] rooms) {

        System.out.println("Available Rooms\n");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            // Defensive check
            if (available > 0) {

                room.displayRoomDetails();
                System.out.println("Available Rooms: " + available);
                System.out.println("----------------------------------");

            }
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
        System.out.println("   Hotel Booking System v1.0");
        System.out.println("==================================\n");

        // Create room objects
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        Room[] rooms = {singleRoom, doubleRoom, suiteRoom};

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize search service
        SearchService searchService = new SearchService(inventory);

        // Guest performs search
        System.out.println("Guest searching for available rooms...\n");

        searchService.searchAvailableRooms(rooms);

        System.out.println("Search completed. Inventory remains unchanged.");
    }
}