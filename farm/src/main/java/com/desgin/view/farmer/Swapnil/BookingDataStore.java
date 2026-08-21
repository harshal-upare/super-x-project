package com.desgin.view.farmer.Swapnil;

import java.util.ArrayList;
import java.util.List;

public class BookingDataStore {

    public static class BookingItem {
        public String bookingId;
        public String equipmentName;
        public String category;
        public String startDate;
        public String endDate;
        public String dailyRate;
        public String totalAmount;
        public String status; // "ACTIVE", "COMPLETED", "CANCELLED", "PENDING"
        public String imagePath;

        public BookingItem(String bookingId, String equipmentName, String category, String startDate,
                           String endDate, String dailyRate, String totalAmount, String status, String imagePath) {
            this.bookingId = bookingId != null ? bookingId : "BK" + (10000 + (int)(Math.random() * 90000));
            this.equipmentName = equipmentName;
            this.category = category != null ? category : "Agricultural Equipment";
            this.startDate = startDate;
            this.endDate = endDate;
            this.dailyRate = dailyRate;
            this.totalAmount = totalAmount;
            this.status = status != null ? status : "ACTIVE";
            this.imagePath = imagePath != null ? imagePath : "file:farm/src/main/resources/assets/Images/tractor.png";
        }
    }

    private static final List<BookingItem> bookings = new ArrayList<>();

    // Starts empty in building phase
    static {
        // Empty by default
    }

    public static synchronized List<BookingItem> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public static synchronized List<BookingItem> getActiveBookings() {
        List<BookingItem> active = new ArrayList<>();
        for (BookingItem b : bookings) {
            if ("ACTIVE".equalsIgnoreCase(b.status)) {
                active.add(b);
            }
        }
        return active;
    }

    public static synchronized List<BookingItem> getPendingBookings() {
        List<BookingItem> pending = new ArrayList<>();
        for (BookingItem b : bookings) {
            if ("PENDING".equalsIgnoreCase(b.status) || "UPCOMING".equalsIgnoreCase(b.status)) {
                pending.add(b);
            }
        }
        return pending;
    }

    public static synchronized List<BookingItem> getCompletedBookings() {
        List<BookingItem> completed = new ArrayList<>();
        for (BookingItem b : bookings) {
            if ("COMPLETED".equalsIgnoreCase(b.status)) {
                completed.add(b);
            }
        }
        return completed;
    }

    public static synchronized int getTotalCount() {
        return bookings.size();
    }

    public static synchronized int getActiveCount() {
        return getActiveBookings().size();
    }

    public static synchronized int getPendingCount() {
        return getPendingBookings().size();
    }

    public static synchronized int getCompletedCount() {
        return getCompletedBookings().size();
    }

    public static synchronized void addBooking(BookingItem booking) {
        if (booking != null) {
            bookings.add(0, booking);
        }
    }

    public static synchronized void cancelBooking(String bookingId) {
        if (bookingId == null) return;
        for (BookingItem b : bookings) {
            if (bookingId.equalsIgnoreCase(b.bookingId)) {
                b.status = "CANCELLED";
                break;
            }
        }
    }

    public static synchronized void clearAll() {
        bookings.clear();
    }
}
