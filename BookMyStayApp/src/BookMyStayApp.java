import java.util.*;

/**
 * Reservation Request
 */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

/**
 * Thread-Safe Booking Queue
 */
class BookingQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    // synchronized method for safe access
    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

/**
 * Thread-Safe Inventory
 */
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
    }

    // synchronized critical section
    public synchronized boolean allocateRoom(String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }

        return false;
    }

    public synchronized int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

/**
 * Booking Processor (Runnable Thread)
 */
class BookingProcessor implements Runnable {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation r;

            // synchronized fetch
            synchronized (queue) {
                if (queue.isEmpty()) break;
                r = queue.getRequest();
            }

            if (r != null) {

                boolean success;

                // critical section for allocation
                synchronized (inventory) {
                    success = inventory.allocateRoom(r.getRoomType());
                }

                if (success) {
                    System.out.println(Thread.currentThread().getName() +
                            " → Booking SUCCESS for " + r.getGuestName());
                } else {
                    System.out.println(Thread.currentThread().getName() +
                            " → Booking FAILED for " + r.getGuestName());
                }
            }
        }
    }
}

/**
 * Main Application
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Concurrent Booking Simulation =====\n");

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Simulate multiple guest requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room"));
        queue.addRequest(new Reservation("David", "Single Room"));

        // Create multiple threads
        Thread t1 = new Thread(new BookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(queue, inventory), "Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nFinal Availability: " +
                inventory.getAvailability("Single Room"));
    }
}