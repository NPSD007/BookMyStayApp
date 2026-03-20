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

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

/**
 * Booking History (Stores confirmed reservations)
 */
class BookingHistory {

    // Maintains insertion order
    private List<Reservation> history = new ArrayList<>();

    /**
     * Add confirmed reservation
     */
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    /**
     * Get all reservations
     */
    public List<Reservation> getAllReservations() {
        return history;
    }

    /**
     * Display full history
     */
    public void displayHistory() {
        System.out.println("\nBooking History (Chronological Order):\n");

        for (Reservation r : history) {
            r.display();
        }
    }
}

/**
 * Booking Report Service
 */
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    /**
     * Generate summary report
     */
    public void generateSummaryReport() {

        List<Reservation> reservations = history.getAllReservations();

        System.out.println("\n===== Booking Summary Report =====");

        System.out.println("Total Bookings: " + reservations.size());

        // Count by room type
        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : reservations) {
            String type = r.getRoomType();
            roomCount.put(type, roomCount.getOrDefault(type, 0) + 1);
        }

        System.out.println("\nBookings by Room Type:");

        for (Map.Entry<String, Integer> entry : roomCount.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

/**
 * Main Application
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====\n");

        // Booking History
        BookingHistory history = new BookingHistory();

        // Simulated confirmed bookings (from Use Case 6)
        Reservation r1 = new Reservation("R101", "Alice", "Single Room");
        Reservation r2 = new Reservation("R102", "Bob", "Double Room");
        Reservation r3 = new Reservation("R103", "Charlie", "Single Room");

        // Add to history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin views booking history
        history.displayHistory();

        // Reporting Service
        BookingReportService reportService = new BookingReportService(history);

        // Generate report
        reportService.generateSummaryReport();

        System.out.println("\nReports generated without modifying booking data.");
    }
}
