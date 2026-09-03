package com.desgin.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.desgin.dao.MachineryDAO;
import com.desgin.dao.NotificationDAO;
import com.desgin.dao.PaymentDAO;
import com.desgin.dao.RentalRequestDAO;
import com.desgin.model.NotificationModel;
import com.desgin.model.PaymentModel;
import com.desgin.model.RentalRequestModel;

public class BookingService {

    private final RentalRequestDAO rentalDAO = new RentalRequestDAO();
    private final NotificationDAO notifDAO = new NotificationDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final MachineryDAO machineryDAO = new MachineryDAO();

    private static final DateTimeFormatter[] DATE_FORMATS = new DateTimeFormatter[]{
        DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    };

    public static class AvailabilityResult {
        private final boolean available;
        private final String message;

        public AvailabilityResult(boolean available, String message) {
            this.available = available;
            this.message = message;
        }

        public boolean isAvailable() {
            return available;
        }

        public String getMessage() {
            return message;
        }

        public static AvailabilityResult success() {
            return new AvailabilityResult(true, "Resources are available for selected dates.");
        }

        public static AvailabilityResult fail(String msg) {
            return new AvailabilityResult(false, msg);
        }
    }

    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return LocalDate.now();
        String clean = dateStr.trim();
        for (DateTimeFormatter fmt : DATE_FORMATS) {
            try {
                return LocalDate.parse(clean, fmt);
            } catch (Exception ignored) {}
        }
        return LocalDate.now();
    }

    public static boolean datesOverlap(LocalDate s1, LocalDate e1, LocalDate s2, LocalDate e2) {
        return !s1.isAfter(e2) && !s2.isAfter(e1);
    }

    /**
     * Checks whether the specified equipment and/or operator are available for the desired dates.
     * Prevents double bookings.
     */
    public AvailabilityResult checkResourceAvailability(
            String equipmentId,
            String startDateStr,
            String endDateStr,
            String operatorId) {

        LocalDate reqStart = parseDate(startDateStr);
        LocalDate reqEnd = parseDate(endDateStr);

        if (reqEnd.isBefore(reqStart)) {
            return AvailabilityResult.fail("Rental end date cannot be before start date.");
        }

        List<RentalRequestModel> allRequests = rentalDAO.getAllRequests();
        for (RentalRequestModel b : allRequests) {
            String status = b.getStatus() != null ? b.getStatus().toUpperCase() : "";
            // Ignore cancelled or rejected bookings
            if ("CANCELLED".equals(status) || "REJECTED".equals(status) || "DECLINED".equals(status)) {
                continue;
            }

            LocalDate bStart = parseDate(b.getStartDate());
            LocalDate bEnd = parseDate(b.getEndDate());

            // 1. Check Equipment Overlap
            if (equipmentId != null && equipmentId.equalsIgnoreCase(b.getMachineryId())) {
                if (datesOverlap(reqStart, reqEnd, bStart, bEnd)) {
                    return AvailabilityResult.fail("This equipment is already reserved from "
                            + b.getStartDate() + " to " + b.getEndDate() + " (Booking #" + b.getRequestId() + "). Please choose alternate dates.");
                }
            }

            // 2. Check Operator Overlap
            if (operatorId != null && !operatorId.trim().isEmpty() && operatorId.equalsIgnoreCase(b.getOperatorId())) {
                if (datesOverlap(reqStart, reqEnd, bStart, bEnd)) {
                    return AvailabilityResult.fail("The selected operator is already assigned from "
                            + b.getStartDate() + " to " + b.getEndDate() + ". Please choose another operator or change dates.");
                }
            }
        }

        return AvailabilityResult.success();
    }

    /**
     * Creates a new booking request and sends real notifications to the Provider and Operator.
     */
    public void createBookingRequest(RentalRequestModel request) throws Exception {
        if (request == null) return;
        rentalDAO.createRequest(request);

        // Notify Provider
        if (request.getProviderEmail() != null && !request.getProviderEmail().isEmpty()) {
            NotificationModel pNotif = new NotificationModel(
                    "NOTIF_" + System.currentTimeMillis(),
                    request.getProviderEmail().trim().toLowerCase(),
                    "🚜 New Equipment Rental Request",
                    request.getFarmerName() + " requested " + request.getMachineryName() + " (" + request.getDays() + " days: " + request.getStartDate() + " to " + request.getEndDate() + ").",
                    "BOOKING",
                    request.getRequestId()
            );
            notifDAO.sendNotification(pNotif);
        }

        // Notify Operator if requested
        if (request.isOperatorRequired() && request.getOperatorId() != null && !request.getOperatorId().isEmpty()) {
            NotificationModel oNotif = new NotificationModel(
                    "NOTIF_" + (System.currentTimeMillis() + 1),
                    request.getOperatorId().trim().toLowerCase(),
                    "👷 New Operator Dispatch Assignment",
                    "You have been requested to operate " + request.getMachineryName() + " for " + request.getFarmerName() + " (" + request.getStartDate() + " to " + request.getEndDate() + ").",
                    "BOOKING",
                    request.getRequestId()
            );
            notifDAO.sendNotification(oNotif);
        }
    }

    /**
     * Provider accepts the machinery rental request.
     */
    public void providerAccept(String requestId) throws Exception {
        RentalRequestModel req = rentalDAO.getRequestById(requestId);
        if (req == null) return;

        rentalDAO.updateRequestStatus(requestId, "ACCEPTED");

        if (req.getFarmerEmail() != null) {
            NotificationModel fNotif = new NotificationModel(
                    "NOTIF_" + System.currentTimeMillis(),
                    req.getFarmerEmail().trim().toLowerCase(),
                    "✔ Booking Request Approved!",
                    "Provider " + req.getProviderName() + " approved your request for " + req.getMachineryName() + ". Please open My Bookings and complete payment to confirm your booking.",
                    "BOOKING",
                    requestId
            );
            notifDAO.sendNotification(fNotif);
        }
    }

    /**
     * Provider rejects the machinery rental request.
     */
    public void providerReject(String requestId, String reason) throws Exception {
        RentalRequestModel req = rentalDAO.getRequestById(requestId);
        if (req == null) return;

        rentalDAO.updateRequestStatus(requestId, "REJECTED");

        if (req.getFarmerEmail() != null) {
            String rText = (reason != null && !reason.trim().isEmpty()) ? ": " + reason.trim() : ".";
            NotificationModel fNotif = new NotificationModel(
                    "NOTIF_" + System.currentTimeMillis(),
                    req.getFarmerEmail().trim().toLowerCase(),
                    "✖ Booking Request Declined",
                    "Provider " + req.getProviderName() + " was unable to accept your request for " + req.getMachineryName() + rText,
                    "BOOKING",
                    requestId
            );
            notifDAO.sendNotification(fNotif);
        }
    }

    /**
     * Operator accepts the assigned job.
     */
    public void operatorAccept(String requestId) throws Exception {
        RentalRequestModel req = rentalDAO.getRequestById(requestId);
        if (req == null) return;

        rentalDAO.updateOperatorStatus(requestId, "ACCEPTED");

        if (req.getFarmerEmail() != null) {
            NotificationModel fNotif = new NotificationModel(
                    "NOTIF_" + System.currentTimeMillis(),
                    req.getFarmerEmail().trim().toLowerCase(),
                    "👷 Operator Confirmed Assignment",
                    "Operator " + (req.getOperatorName() != null ? req.getOperatorName() : "Assigned Operator") + " accepted your machinery operation task.",
                    "STATUS",
                    requestId
            );
            notifDAO.sendNotification(fNotif);
        }
    }

    /**
     * Operator declines the assigned job.
     */
    public void operatorDecline(String requestId, String reason) throws Exception {
        RentalRequestModel req = rentalDAO.getRequestById(requestId);
        if (req == null) return;

        rentalDAO.updateOperatorStatus(requestId, "REJECTED");

        if (req.getFarmerEmail() != null) {
            NotificationModel fNotif = new NotificationModel(
                    "NOTIF_" + System.currentTimeMillis(),
                    req.getFarmerEmail().trim().toLowerCase(),
                    "Notice: Operator Assignment Update",
                    "The selected operator is currently unavailable for " + req.getMachineryName() + ". You may continue with self-drive or select an alternate operator.",
                    "STATUS",
                    requestId
            );
            notifDAO.sendNotification(fNotif);
        }
    }

    /**
     * Completes and confirms payment via Razorpay.
     */
    public void confirmPayment(String bookingId, String razorpayPaymentId, String razorpayOrderId, String paymentMode) throws Exception {
        RentalRequestModel req = rentalDAO.getRequestById(bookingId);
        if (req == null) return;

        int total = req.getTotalAmount() > 0 ? req.getTotalAmount() : (req.getDailyRate() * Math.max(1, req.getDays()));

        rentalDAO.updatePaymentStatus(bookingId, "PAID", razorpayPaymentId, paymentMode);

        // Record payment in payments collection
        PaymentModel payment = new PaymentModel(
                "PAY_" + System.currentTimeMillis(),
                razorpayOrderId != null ? razorpayOrderId : ("ORD_" + System.currentTimeMillis()),
                razorpayPaymentId != null ? razorpayPaymentId : ("PAY_RZP_" + System.currentTimeMillis()),
                bookingId,
                req.getFarmerEmail(),
                req.getFarmerName(),
                req.getProviderEmail(),
                req.getProviderName(),
                req.getOperatorId(),
                total
        );
        paymentDAO.recordPayment(payment);

        // Notify Farmer
        if (req.getFarmerEmail() != null) {
            notifDAO.sendNotification(new NotificationModel(
                    "NOTIF_" + System.currentTimeMillis(),
                    req.getFarmerEmail().trim().toLowerCase(),
                    "💳 Payment Received - Booking Confirmed!",
                    "Payment of ₹" + total + " verified successfully. Your rental of " + req.getMachineryName() + " is now CONFIRMED.",
                    "PAYMENT",
                    bookingId
            ));
        }

        // Notify Provider
        if (req.getProviderEmail() != null) {
            notifDAO.sendNotification(new NotificationModel(
                    "NOTIF_" + (System.currentTimeMillis() + 1),
                    req.getProviderEmail().trim().toLowerCase(),
                    "💰 Rental Payment Secured in Escrow",
                    "Farmer " + req.getFarmerName() + " has completed payment for " + req.getMachineryName() + " (₹" + total + " in Escrow). Prepare equipment dispatch.",
                    "PAYMENT",
                    bookingId
            ));
        }

        // Notify Operator if applicable
        if (req.isOperatorRequired() && req.getOperatorId() != null && !req.getOperatorId().isEmpty()) {
            notifDAO.sendNotification(new NotificationModel(
                    "NOTIF_" + (System.currentTimeMillis() + 2),
                    req.getOperatorId().trim().toLowerCase(),
                    "📋 Assignment Confirmed",
                    "Rental booking #" + bookingId + " for " + req.getMachineryName() + " has been paid and confirmed. Shift is scheduled from " + req.getStartDate() + " to " + req.getEndDate() + ".",
                    "BOOKING",
                    bookingId
            ));
        }

        // Create Escrow Payout allocation for Provider
        int eqAmt = req.getEquipmentAmount() > 0 ? req.getEquipmentAmount() : total;
        int netProviderAmt = (int) (eqAmt * 0.93); // 7% platform take rate
        if (req.getProviderEmail() != null) {
            new com.desgin.dao.PayoutDAO().recordPayout(new com.desgin.model.PayoutModel(
                    "PO_PROV_" + bookingId,
                    req.getProviderEmail().trim().toLowerCase(),
                    "PROVIDER",
                    bookingId,
                    payment.getPaymentId(),
                    netProviderAmt,
                    "PROCESSING",
                    "ESCROW_HOLD_" + bookingId,
                    req.getProviderBankName() != null ? (req.getProviderBankName() + " A/C: " + req.getProviderAccountNumber()) : "Registered Bank A/C"
            ));
        }

        // Create Escrow Payout allocation for Operator if requested
        if (req.isOperatorRequired() && req.getOperatorId() != null && !req.getOperatorId().isEmpty()) {
            int opAmt = req.getOperatorAmount() > 0 ? req.getOperatorAmount() : (500 * Math.max(1, req.getDays()));
            new com.desgin.dao.PayoutDAO().recordPayout(new com.desgin.model.PayoutModel(
                    "PO_OP_" + bookingId,
                    req.getOperatorId().trim().toLowerCase(),
                    "OPERATOR",
                    bookingId,
                    payment.getPaymentId(),
                    opAmt,
                    "PROCESSING",
                    "ESCROW_HOLD_" + bookingId,
                    "Direct UPI / Wage Escrow"
            ));
        }
    }

    /**
     * Marks booking as completed, releases machinery back to AVAILABLE,
     * settles escrow payouts to PAID for provider and operator, and notifies parties.
     */
    public void completeBooking(String bookingId) throws Exception {
        RentalRequestModel req = rentalDAO.getRequestById(bookingId);
        if (req == null) return;

        rentalDAO.updateRequestStatus(bookingId, "COMPLETED");

        if (req.getMachineryId() != null) {
            machineryDAO.updateMachineryStatus(req.getMachineryId(), "AVAILABLE");
        }

        // Settle payouts in PayoutDAO
        com.desgin.dao.PayoutDAO payoutDAO = new com.desgin.dao.PayoutDAO();
        payoutDAO.updatePayoutStatus("PO_PROV_" + bookingId, "PAID", "IMPS_SETTLED_" + System.currentTimeMillis());
        if (req.isOperatorRequired()) {
            payoutDAO.updatePayoutStatus("PO_OP_" + bookingId, "PAID", "UPI_SETTLED_" + System.currentTimeMillis());
        }

        if (req.getFarmerEmail() != null) {
            notifDAO.sendNotification(new NotificationModel("NOTIF_" + System.currentTimeMillis(), req.getFarmerEmail().trim().toLowerCase(), "⭐ Job Complete - Leave a Review", "Your rental for " + req.getMachineryName() + " is complete. Please share your rating & feedback!", "REVIEW", bookingId));
        }
        if (req.getProviderEmail() != null) {
            notifDAO.sendNotification(new NotificationModel("NOTIF_" + (System.currentTimeMillis() + 1), req.getProviderEmail().trim().toLowerCase(), "💰 Escrow Settled & Transferred", "Job complete for " + req.getMachineryName() + ". Rental earnings settled to your account.", "PAYOUT", bookingId));
        }
        if (req.isOperatorRequired() && req.getOperatorId() != null && !req.getOperatorId().isEmpty()) {
            notifDAO.sendNotification(new NotificationModel("NOTIF_" + (System.currentTimeMillis() + 2), req.getOperatorId().trim().toLowerCase(), "💵 Wages Settled", "Field shift finished for " + req.getMachineryName() + ". Daily wages credited.", "PAYOUT", bookingId));
        }
    }

    /**
     * Cancels an active or pending booking.
     */
    public void cancelBooking(String bookingId, String cancelledByRole) throws Exception {
        RentalRequestModel req = rentalDAO.getRequestById(bookingId);
        if (req == null) return;

        rentalDAO.cancelRequest(bookingId);

        String title = "Booking Cancelled (#" + bookingId + ")";
        String msg = "The rental booking for " + req.getMachineryName() + " was cancelled by the " + cancelledByRole + ".";

        if (req.getFarmerEmail() != null) {
            notifDAO.sendNotification(new NotificationModel("NOTIF_" + System.currentTimeMillis(), req.getFarmerEmail().trim().toLowerCase(), title, msg, "STATUS", bookingId));
        }
        if (req.getProviderEmail() != null) {
            notifDAO.sendNotification(new NotificationModel("NOTIF_" + (System.currentTimeMillis() + 1), req.getProviderEmail().trim().toLowerCase(), title, msg, "STATUS", bookingId));
        }
        if (req.isOperatorRequired() && req.getOperatorId() != null && !req.getOperatorId().isEmpty()) {
            notifDAO.sendNotification(new NotificationModel("NOTIF_" + (System.currentTimeMillis() + 2), req.getOperatorId().trim().toLowerCase(), title, msg, "STATUS", bookingId));
        }
    }
}
