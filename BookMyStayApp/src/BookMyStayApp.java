/**
 * Abstract class representing a generic Room.
 * Defines common properties shared by all room types.
 * This class cannot be instantiated directly.
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
 * Represents a Single Room in the hotel.
 */
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 200, 80);
    }
}

/**
 * Represents a Double Room in the hotel.
 */
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 350, 120);
    }
}

/**
 * Represents a Suite Room in the hotel.
 */
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 600, 250);
    }
}

/**
 * Book My Stay Application
 * Demonstrates basic room modeling and static availability.
 *
 * @author Developer
 * @version 1.0
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

        // Static availability variables
        int singleRoomAvailable = 10;
        int doubleRoomAvailable = 6;
        int suiteRoomAvailable = 3;

        // Display room information
        System.out.println("Available Room Types\n");

        singleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + singleRoomAvailable);
        System.out.println("----------------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleRoomAvailable);
        System.out.println("----------------------------------");

        suiteRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteRoomAvailable);
        System.out.println("----------------------------------");

        System.out.println("Thank you for exploring Book My Stay.");
    }
}