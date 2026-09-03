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
        public String status; // "PENDING", "ACCEPTED", "CONFIRMED", "ACTIVE", "COMPLETED", "CANCELLED"
        public String paymentStatus; // "PENDING", "PAID", "FAILED", "REFUNDED"
        public String imagePath;
        public String providerName;
        public String providerEmail;
        public String farmerEmail; // for ownership validation
        public boolean operatorRequired;
        public String operatorName;
        public String operatorId;
        public int equipmentAmount;
        public int operatorAmount;

        public BookingItem(String bookingId, String equipmentName, String category, String startDate,
                           String endDate, String dailyRate, String totalAmount, String status, String imagePath) {
            this.bookingId = bookingId != null ? bookingId : "BK" + (10000 + (int)(Math.random() * 90000));
            this.equipmentName = equipmentName;
            this.category = category != null ? category : "Agricultural Equipment";
            this.startDate = startDate;
            this.endDate = endDate;
            this.dailyRate = dailyRate;
            this.totalAmount = totalAmount;
            this.status = status != null ? status : "PENDING";
            this.paymentStatus = "PENDING";
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
            if ("ACTIVE".equalsIgnoreCase(b.status) || "CONFIRMED".equalsIgnoreCase(b.status)) {
                active.add(b);
            }
        }
        return active;
    }

    public static synchronized List<BookingItem> getPendingBookings() {
        List<BookingItem> pending = new ArrayList<>();
        for (BookingItem b : bookings) {
            if ("PENDING".equalsIgnoreCase(b.status) || "ACCEPTED".equalsIgnoreCase(b.status) || "UPCOMING".equalsIgnoreCase(b.status)) {
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
            bookings.removeIf(b -> b.bookingId != null && b.bookingId.equalsIgnoreCase(booking.bookingId));
            bookings.add(0, booking);
        }
    }

    public static synchronized void syncFromFirestore(List<com.desgin.model.RentalRequestModel> requests) {
        if (requests == null || requests.isEmpty()) {
            return; // Don't wipe existing in-memory bookings if query returned empty
        }
        bookings.clear();
        java.util.Set<String> seenIds = new java.util.HashSet<>();
        for (com.desgin.model.RentalRequestModel r : requests) {
            String bId = r.getRequestId() != null ? r.getRequestId() : "";
            if (!bId.isEmpty() && !seenIds.add(bId)) {
                continue; // Skip duplicate
            }
            String st = r.getStatus() != null ? r.getStatus().toUpperCase() : "PENDING";
            if ("APPROVED".equals(st)) st = "ACCEPTED";
            int total = r.getTotalAmount() > 0 ? r.getTotalAmount() : (r.getDailyRate() * Math.max(1, r.getDays()));

            BookingItem item = new BookingItem(
                bId,
                r.getMachineryName(),
                r.getCategory(),
                r.getStartDate(),
                r.getEndDate(),
                "₹" + r.getDailyRate() + " / day",
                "₹" + total,
                st,
                r.getImagePath()
            );
            item.paymentStatus = r.getPaymentStatus() != null ? r.getPaymentStatus() : "PENDING";
            item.farmerEmail = r.getFarmerEmail();
            item.providerName = r.getProviderName();
            item.providerEmail = r.getProviderEmail();
            item.operatorRequired = r.isOperatorRequired();
            item.operatorName = r.getOperatorName();
            item.operatorId = r.getOperatorId();
            item.equipmentAmount = r.getEquipmentAmount();
            item.operatorAmount = r.getOperatorAmount();

            bookings.add(item);
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
