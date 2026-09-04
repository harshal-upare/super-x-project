package com.desgin.view.farmer.pratik;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.desgin.view.farmer.Swapnil.BookingDataStore;
import com.desgin.view.farmer.Swapnil.FarmerProfileStore;
import com.desgin.dao.PaymentDAO;
import com.desgin.dao.RentalRequestDAO;
import com.desgin.model.PaymentModel;
import com.desgin.model.RentalRequestModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;



public class Payment {

    private static ArrayList<VBox> paymentCardList = new ArrayList<>();
    private static ArrayList<String> paymentSearchData = new ArrayList<>();
    private static ArrayList<String> paymentStatusList = new ArrayList<>();
    private static ArrayList<String> paymentMethodList = new ArrayList<>();

    private static TextField searchField;

private static CheckBox paid;
private static CheckBox pending;
private static CheckBox failed;

private static CheckBox upi;
private static CheckBox card;
private static CheckBox net;

//private VBox paymentCards;
   public static ScrollPane getPaymentSection() {

    Text paymentTitle = new Text("Payment Dashboard");
    paymentTitle.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:26px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#1B4332;"
    );

    Text subtitle = new Text(
            "Manage all your equipment payments and invoices"
    );

    subtitle.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:14px;" +
            "-fx-fill:#4B5563;"
    );

    VBox heading = new VBox(
            5,
            paymentTitle,
            subtitle
    );

    searchField = new TextField();
    searchField.setPromptText(
            "Search Transaction ID or Equipment..."
    );

    stylePaymentTextField(searchField);

    Button searchButton = new Button("Search");
    styleGreenButton(searchButton);
    searchButton.setOnAction(e -> {

    String keyword = searchField.getText().trim().toLowerCase();
    for(int i = 0; i < paymentCardList.size(); i++){
        VBox card = paymentCardList.get(i);
        String data = paymentSearchData.get(i);
        boolean found = keyword.isEmpty() || data.contains(keyword);
        card.setVisible(found);
        card.setManaged(found);
        }

    });
    searchField.textProperty().addListener((obs, oldValue, newValue) -> {
    String keyword = newValue.toLowerCase();
    for(int i = 0; i < paymentCardList.size(); i++){
        VBox card = paymentCardList.get(i);
        String data = paymentSearchData.get(i);
        boolean found = keyword.isEmpty() || data.contains(keyword);
        card.setVisible(found);
        card.setManaged(found);
    }

});

    HBox searchBox = new HBox(
            10,
            searchField,
            searchButton
    );

    HBox.setHgrow(
            searchField,
            Priority.ALWAYS
    );

    //---------------------------------------------------------
    // Dynamic Spending & Payment Data from Firestore
    //---------------------------------------------------------
    String farmerEmail = FarmerProfileStore.email != null ? FarmerProfileStore.email.trim().toLowerCase() : "";
    List<PaymentModel> farmerPayments = new PaymentDAO().getPaymentsByFarmer(farmerEmail);
    List<RentalRequestModel> farmerRequests = new RentalRequestDAO().getRequestsByFarmer(farmerEmail);

    paymentCardList.clear();
    paymentSearchData.clear();
    paymentStatusList.clear();
    paymentMethodList.clear();

    int totalPaidAmt = 0;
    int thisMonthAmt = 0;
    int pendingAmt = 0;
    int completedCount = 0;
    int refundAmt = 0;

    String currentMonthName = java.time.LocalDate.now().getMonth().name().substring(0, 3); // e.g. "SEP"

    for (PaymentModel p : farmerPayments) {
        if ("PAID".equalsIgnoreCase(p.getPaymentStatus())) {
            totalPaidAmt += p.getAmount();
            completedCount++;
            if (p.getCreatedAt() != null && p.getCreatedAt().toUpperCase().contains(currentMonthName)) {
                thisMonthAmt += p.getAmount();
            }
        } else if ("REFUNDED".equalsIgnoreCase(p.getPaymentStatus())) {
            refundAmt += p.getAmount();
        }
    }

    for (RentalRequestModel r : farmerRequests) {
        int rAmt = r.getTotalAmount() > 0 ? r.getTotalAmount() : (r.getDailyRate() * Math.max(1, r.getDays()));
        if ("PAID".equalsIgnoreCase(r.getPaymentStatus()) || "COMPLETED".equalsIgnoreCase(r.getStatus())) {
            if (farmerPayments.isEmpty()) {
                totalPaidAmt += rAmt;
                completedCount++;
                thisMonthAmt += rAmt;
            }
        } else if ("ACCEPTED".equalsIgnoreCase(r.getStatus()) || "PENDING".equalsIgnoreCase(r.getStatus())) {
            pendingAmt += rAmt;
        }
    }

    List<RentalRequestModel> activeShiftsRequests = new ArrayList<>();
    for (RentalRequestModel r : farmerRequests) {
        boolean isInProgress = "IN_PROGRESS".equalsIgnoreCase(r.getStatus()) || "IN_PROGRESS".equalsIgnoreCase(r.getOperatorStatus());
        if (isInProgress) {
            activeShiftsRequests.add(r);
        }
    }

    VBox activeShiftsSection = createActiveShiftsSection(activeShiftsRequests);

    List<RentalRequestModel> pendingPaymentRequests = new ArrayList<>();
    for (RentalRequestModel r : farmerRequests) {
        boolean isAccepted = "ACCEPTED".equalsIgnoreCase(r.getOperatorStatus()) || "ACCEPTED".equalsIgnoreCase(r.getStatus()) || "CONFIRMED".equalsIgnoreCase(r.getStatus());
        boolean isUnpaid = !"PAID".equalsIgnoreCase(r.getPaymentStatus()) && !"COMPLETED".equalsIgnoreCase(r.getStatus()) && !"IN_PROGRESS".equalsIgnoreCase(r.getStatus()) && !"IN_PROGRESS".equalsIgnoreCase(r.getOperatorStatus());
        if (isAccepted && isUnpaid) {
            pendingPaymentRequests.add(r);
        }
    }

    VBox pendingPaymentsSection = createPendingPaymentsSection(pendingPaymentRequests);

    // 5 Financial Cards (Requirement 4)
    HBox summaryCards = new HBox(14);
    summaryCards.getChildren().addAll(
            createSummaryCard("Total Spent", "₹" + String.format("%,d", totalPaidAmt), "💰", "#1B4332"),
            createSummaryCard("This Month", "₹" + String.format("%,d", thisMonthAmt), "📅", "#2D6A4F"),
            createSummaryCard("Pending Payment", "₹" + String.format("%,d", pendingAmt), "⏳", "#E65100"),
            createSummaryCard("Completed", completedCount + " Paid", "✔", "#2E7D32"),
            createSummaryCard("Refunds", "₹" + String.format("%,d", refundAmt), "↩", "#37474F")
    );

    // Dynamic Graphs Section (Requirement 5)
    HBox chartsRow = createFarmerSpendingCharts(farmerPayments, farmerRequests, totalPaidAmt, pendingAmt, refundAmt);

    VBox filterSection = createPaymentFilter();

    Text historyTitle = new Text("Payment & Invoice Ledger");
    historyTitle.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:20px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#1B4332;"
    );

    Text historySubtitle = new Text("Verified transaction receipts and invoices connected directly to Firestore");
    historySubtitle.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#4B5563;"
    );

    VBox historyHeading = new VBox(4, historyTitle, historySubtitle);
    VBox paymentCards = new VBox(18);

    if (farmerRequests.isEmpty() && farmerPayments.isEmpty()) {
        VBox emptyBox = new VBox(12);
        emptyBox.setAlignment(Pos.CENTER);
        emptyBox.setPadding(new Insets(40));
        emptyBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-radius: 14; -fx-border-width: 1;");

        Text emptyIcon = new Text("💳");
        emptyIcon.setStyle("-fx-font-size: 38px;");

        Text emptyTitle = new Text("No payment history yet.");
        emptyTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text emptySub = new Text("When you rent equipment and complete payment via Razorpay, transaction receipts and invoices will appear here.");
        emptySub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-text-fill: #6B7280;");

        emptyBox.getChildren().addAll(emptyIcon, emptyTitle, emptySub);
        paymentCards.getChildren().add(emptyBox);
    } else {
        for (RentalRequestModel r : farmerRequests) {
            String pStatus = ("PAID".equalsIgnoreCase(r.getPaymentStatus()) || "COMPLETED".equalsIgnoreCase(r.getStatus())) ? "Paid" : ("CANCELLED".equalsIgnoreCase(r.getStatus()) ? "Failed" : "Pending");
            String pMethod = r.getPaymentMode() != null ? r.getPaymentMode() : "Razorpay Online";
            String imgPath = r.getImagePath() != null && !r.getImagePath().isEmpty() ? r.getImagePath() : "file:farm/src/main/resources/assets/Images/tractor.png";
            int amt = r.getTotalAmount() > 0 ? r.getTotalAmount() : (r.getDailyRate() * Math.max(1, r.getDays()));

            VBox card = createPaymentCard(
                    r.getMachineryName() != null ? r.getMachineryName() : "Machinery",
                    r.getRequestId() != null ? r.getRequestId() : "TXN-" + System.currentTimeMillis(),
                    "₹" + String.format("%,d", amt),
                    pMethod,
                    r.getStartDate() != null ? r.getStartDate() : "Recent",
                    pStatus,
                    imgPath
            );

            paymentCardList.add(card);
            paymentSearchData.add(((r.getMachineryName() != null ? r.getMachineryName() : "") + " " + (r.getRequestId() != null ? r.getRequestId() : "")).toLowerCase());
            paymentStatusList.add(pStatus);
            paymentMethodList.add(pMethod);
            paymentCards.getChildren().add(card);
        }
    }

        VBox paymentSection = new VBox(
                18,
                historyHeading,
                paymentCards
        );

        HBox mainContent = new HBox(
                20,
                filterSection,
                paymentSection
        );

        HBox.setHgrow(
                paymentSection,
                Priority.ALWAYS
        );

        VBox root = new VBox(
                22,
                heading,
                searchBox,
                summaryCards,
                activeShiftsSection,
                pendingPaymentsSection,
                chartsRow,
                mainContent
        );

        root.setPadding(
                new Insets(25,30,30,30)
        );

        root.setStyle(
                "-fx-background-color:transparent;"
        );

        ScrollPane scrollPane = new ScrollPane(root);

        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
                "-fx-background:transparent;" +
                "-fx-background-color:transparent;"
        );

        return scrollPane;

    }

    public static void markShiftCompleted(String requestId) {
        if (requestId == null) return;
        try {
            for (com.desgin.view.farmer.Swapnil.BookingDataStore.BookingItem b : com.desgin.view.farmer.Swapnil.BookingDataStore.getAllBookings()) {
                if (requestId.equalsIgnoreCase(b.bookingId) || requestId.equalsIgnoreCase(b.operatorId) || ("OP-" + b.bookingId).equalsIgnoreCase(requestId)) {
                    b.status = "COMPLETED";
                }
            }
        } catch (Exception ignored) {}
    }

    private static final java.util.Map<String, Label> farmerCountdownLabels = new java.util.HashMap<>();
    private static javafx.animation.Timeline farmerCountdownTimeline;

    private static VBox createActiveShiftsSection(List<RentalRequestModel> activeList) {
        if (activeList == null || activeList.isEmpty()) {
            return new VBox();
        }

        Text title = new Text("🚜 Active Field Shifts in Progress (Live Countdown)");
        title.setStyle("-fx-font-family:'Poppins'; -fx-font-size:18px; -fx-font-weight:bold; -fx-fill:#065F46;");

        Text subtitle = new Text("The operator is actively working in your field. Escrow funds are held securely and will be settled upon shift completion.");
        subtitle.setStyle("-fx-font-family:'Poppins'; -fx-font-size:12.5px; -fx-fill:#4B5563;");

        VBox header = new VBox(3, title, subtitle);
        VBox cardsContainer = new VBox(12);

        for (RentalRequestModel r : activeList) {
            long startTime = r.getShiftStartTime() > 0 ? r.getShiftStartTime() : System.currentTimeMillis();
            long duration = r.getShiftDurationMillis() > 0 ? r.getShiftDurationMillis() : (3 * 3600 * 1000L);
            String opName = r.getOperatorName() != null ? r.getOperatorName() : "Assigned Operator";
            String opPhone = r.getOperatorPhone() != null ? r.getOperatorPhone() : "+91 98220 12345";
            String task = r.getMachineryName() != null ? r.getMachineryName() : "Field Agricultural Operation";
            String location = r.getFarmerLocation() != null ? r.getFarmerLocation() : "Your Farm Plot";

            HBox card = new HBox(16);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPadding(new Insets(16, 20, 16, 20));
            card.setStyle(
                "-fx-background-color: #ECFDF5;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: #10B981;" +
                "-fx-border-width: 1.5px;" +
                "-fx-border-radius: 12px;" +
                "-fx-effect: dropshadow(gaussian, rgba(16, 185, 129, 0.15), 10, 0, 0, 3);"
            );

            Text icon = new Text("🚜");
            icon.setStyle("-fx-font-size: 30px;");
            StackPane iconBox = new StackPane(icon);
            iconBox.setPrefSize(50, 50);
            iconBox.setStyle("-fx-background-color: #D1FAE5; -fx-background-radius: 10px;");

            Label badge = new Label("🟢 OPERATOR ACTIVE • SHIFT IN PROGRESS");
            badge.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-font-family:'Poppins'; -fx-font-size:11px; -fx-font-weight:bold; -fx-padding: 3 8; -fx-background-radius: 6;");

            Text taskTitle = new Text(task);
            taskTitle.setStyle("-fx-font-family:'Poppins'; -fx-font-size:16px; -fx-font-weight:bold; -fx-fill:#065F46;");

            Text opText = new Text("👷 On-Field Operator: " + opName + "  |  📞 " + opPhone);
            opText.setStyle("-fx-font-family:'Poppins'; -fx-font-size:12.5px; -fx-font-weight:600; -fx-fill:#374151;");

            Text locText = new Text("📍 Location: " + location + "  •  🔒 Escrow: SECURED & VERIFIED");
            locText.setStyle("-fx-font-family:'Poppins'; -fx-font-size:12px; -fx-fill:#4B5563;");

            VBox infoBox = new VBox(4, badge, taskTitle, opText, locText);
            HBox.setHgrow(infoBox, Priority.ALWAYS);

            Text timerTitle = new Text("⏱ Shift Countdown:");
            timerTitle.setStyle("-fx-font-family:'Poppins'; -fx-font-size:11px; -fx-font-weight:bold; -fx-fill:#065F46;");

            Label timerVal = new Label(formatCountdown(startTime, duration));
            timerVal.setStyle("-fx-font-family:'Poppins'; -fx-font-size:14px; -fx-font-weight:bold; -fx-text-fill:#047857; -fx-background-color:#D1FAE5; -fx-padding: 5 10; -fx-background-radius: 6;");

            farmerCountdownLabels.put(r.getRequestId(), timerVal);

            Button workDoneBtn = new Button("✔ Work Done");
            workDoneBtn.setStyle("-fx-background-color: linear-gradient(to right, #059669, #10B981); -fx-text-fill: white; -fx-font-family:'Poppins'; -fx-font-size:12px; -fx-font-weight:bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 6 16; -fx-effect: dropshadow(gaussian, rgba(16,185,129,0.3), 6, 0, 0, 2);");

            workDoneBtn.setOnAction(e -> {
                r.setStatus("COMPLETED");
                r.setOperatorStatus("COMPLETED");
                new Thread(() -> {
                    try {
                        new RentalRequestDAO().completeShift(r.getRequestId());
                        new com.desgin.dao.NotificationDAO().sendNotification(
                            r.getOperatorId(),
                            "✔ Work Done Confirmed by Farmer",
                            "Farmer confirmed work completed for " + r.getMachineryName() + ". Escrow funds have been settled to your account.",
                            "COMPLETED",
                            r.getRequestId()
                        );
                    } catch (Exception ignored) {}
                }).start();

                markShiftCompleted(r.getRequestId());

                workDoneBtn.setDisable(true);
                workDoneBtn.setText("✓ Completed");
                workDoneBtn.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-font-family:'Poppins'; -fx-font-size:12px; -fx-font-weight:bold; -fx-background-radius: 8px; -fx-padding: 6 16;");
                timerVal.setText("00h : 00m : 00s (Completed)");
                badge.setText("✅ SHIFT COMPLETED • ESCROW SETTLED");
                badge.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family:'Poppins'; -fx-font-size:11px; -fx-font-weight:bold; -fx-padding: 3 8; -fx-background-radius: 6;");
            });

            VBox rightSide = new VBox(6, timerTitle, timerVal, workDoneBtn);
            rightSide.setAlignment(Pos.CENTER_RIGHT);

            card.getChildren().addAll(iconBox, infoBox, rightSide);
            cardsContainer.getChildren().add(card);
        }

        ensureFarmerCountdownRunning(activeList);

        VBox section = new VBox(12, header, cardsContainer);
        section.setPadding(new Insets(16, 20, 16, 20));
        section.setStyle(
            "-fx-background-color: rgba(209, 250, 229, 0.4);" +
            "-fx-background-radius: 14px;" +
            "-fx-border-color: #10B981;" +
            "-fx-border-width: 1.5px;" +
            "-fx-border-radius: 14px;"
        );
        return section;
    }

    private static String formatCountdown(long startTime, long durationMillis) {
        long elapsed = System.currentTimeMillis() - startTime;
        long remaining = Math.max(0, durationMillis - elapsed);
        if (remaining == 0) {
            return "00h : 00m : 00s (Time Over)";
        }
        long hours = remaining / (3600 * 1000);
        long mins = (remaining % (3600 * 1000)) / (60 * 1000);
        long secs = (remaining % (60 * 1000)) / 1000;
        return String.format("%02dh : %02dm : %02ds remaining", hours, mins, secs);
    }

    private static void ensureFarmerCountdownRunning(List<RentalRequestModel> list) {
        if (farmerCountdownTimeline != null) farmerCountdownTimeline.stop();
        farmerCountdownTimeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), ev -> {
                for (RentalRequestModel r : list) {
                    Label lbl = farmerCountdownLabels.get(r.getRequestId());
                    if (lbl != null) {
                        long st = r.getShiftStartTime() > 0 ? r.getShiftStartTime() : System.currentTimeMillis();
                        long dur = r.getShiftDurationMillis() > 0 ? r.getShiftDurationMillis() : (3 * 3600 * 1000L);
                        lbl.setText(formatCountdown(st, dur));
                    }
                }
            })
        );
        farmerCountdownTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        farmerCountdownTimeline.play();
    }

    private static VBox createPendingPaymentsSection(List<RentalRequestModel> pendingList) {
        if (pendingList == null || pendingList.isEmpty()) {
            return new VBox();
        }

        Text title = new Text("⚠️ Action Required: Pending Operator Wage Payments");
        title.setStyle("-fx-font-family:'Poppins'; -fx-font-size:18px; -fx-font-weight:bold; -fx-fill:#B45309;");

        Text subtitle = new Text("The operator has accepted your hiring request. Please complete the escrow payment to authorize the operator to start work.");
        subtitle.setStyle("-fx-font-family:'Poppins'; -fx-font-size:12.5px; -fx-fill:#4B5563;");

        VBox header = new VBox(3, title, subtitle);

        VBox cardsContainer = new VBox(12);

        for (RentalRequestModel r : pendingList) {
            int amt = r.getTotalAmount() > 0 ? r.getTotalAmount() : (r.getDailyRate() * Math.max(1, r.getDays()));
            String opName = r.getOperatorName() != null ? r.getOperatorName() : "Assigned Operator";
            String opPhone = r.getOperatorPhone() != null ? r.getOperatorPhone() : "+91 98220 12345";
            String task = r.getMachineryName() != null ? r.getMachineryName() : "Agricultural Field Operation";
            String schedule = (r.getStartDate() != null ? r.getStartDate() : "Standard Schedule") + (r.getDays() > 0 ? " (" + r.getDays() + " Days)" : "");
            String location = r.getFarmerLocation() != null ? r.getFarmerLocation() : "Farmer Plot";

            HBox card = new HBox(16);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPadding(new Insets(16, 20, 16, 20));
            card.setStyle(
                "-fx-background-color: #FFFBEB;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: #FCD34D;" +
                "-fx-border-width: 1.5px;" +
                "-fx-border-radius: 12px;" +
                "-fx-effect: dropshadow(gaussian, rgba(180, 83, 9, 0.1), 8, 0, 0, 2);"
            );

            // Icon / Avatar
            Text icon = new Text("🚜");
            if (task.toLowerCase().contains("harvester")) icon.setText("🌾");
            else if (task.toLowerCase().contains("drone")) icon.setText("🚁");
            icon.setStyle("-fx-font-size: 28px;");

            StackPane iconBox = new StackPane(icon);
            iconBox.setPrefSize(50, 50);
            iconBox.setMinSize(50, 50);
            iconBox.setStyle("-fx-background-color: #FEF3C7; -fx-background-radius: 10px;");

            // Info Box
            Label badge = new Label("🟢 Operator Accepted • ⏳ Payment Required");
            badge.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #B45309; -fx-font-family:'Poppins'; -fx-font-size:11px; -fx-font-weight:bold; -fx-padding: 3 8; -fx-background-radius: 6;");

            Text taskTitle = new Text(task);
            taskTitle.setStyle("-fx-font-family:'Poppins'; -fx-font-size:16px; -fx-font-weight:bold; -fx-fill:#1B4332;");

            Text opText = new Text("👷 Assigned Operator: " + opName + "  |  📞 " + opPhone);
            opText.setStyle("-fx-font-family:'Poppins'; -fx-font-size:12.5px; -fx-font-weight:600; -fx-fill:#374151;");

            Text schedText = new Text("📅 Schedule: " + schedule + "  •  📍 Plot: " + location);
            schedText.setStyle("-fx-font-family:'Poppins'; -fx-font-size:12px; -fx-fill:#4B5563;");

            VBox infoBox = new VBox(4, badge, taskTitle, opText, schedText);
            HBox.setHgrow(infoBox, Priority.ALWAYS);

            // Right side: Amount & Pay Button
            Text amtLabel = new Text("Wage Amount Due:");
            amtLabel.setStyle("-fx-font-family:'Poppins'; -fx-font-size:11px; -fx-fill:#6B7280;");

            Text amtVal = new Text("₹" + String.format("%,d", amt));
            amtVal.setStyle("-fx-font-family:'Poppins'; -fx-font-size:20px; -fx-font-weight:bold; -fx-fill:#B45309;");

            Button payBtn = new Button("💳 Pay ₹" + String.format("%,d", amt) + " with Razorpay");
            payBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                "-fx-text-fill: white;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;" +
                "-fx-padding: 8 18;" +
                "-fx-effect: dropshadow(gaussian, rgba(45,106,79,0.3), 6, 0, 0, 2);"
            );
            payBtn.setOnAction(e -> showRazorpayPaymentModal(r, amt));

            VBox rightSide = new VBox(6, amtLabel, amtVal, payBtn);
            rightSide.setAlignment(Pos.CENTER_RIGHT);

            card.getChildren().addAll(iconBox, infoBox, rightSide);
            cardsContainer.getChildren().add(card);
        }

        VBox section = new VBox(12, header, cardsContainer);
        section.setPadding(new Insets(16, 20, 16, 20));
        section.setStyle(
            "-fx-background-color: rgba(254, 243, 199, 0.4);" +
            "-fx-background-radius: 14px;" +
            "-fx-border-color: #FCD34D;" +
            "-fx-border-width: 1.5px;" +
            "-fx-border-radius: 14px;"
        );
        return section;
    }

    private static void showRazorpayPaymentModal(RentalRequestModel r, int amount) {
        StackPane root = com.desgin.view.farmer.Swapnil.FarmerDashboard.root;
        if (root == null) return;

        StackPane overlay = new StackPane();
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.55);");

        VBox modal = new VBox(12);
        modal.setPrefWidth(470);
        modal.setMaxWidth(470);
        modal.setMaxHeight(Region.USE_PREF_SIZE);
        modal.setMinHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(modal, Pos.CENTER);
        modal.setPadding(new Insets(18, 22, 18, 22));
        modal.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: #D1E7DD;" +
            "-fx-border-width: 1.2;" +
            "-fx-border-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 16, 0, 0, 6);"
        );

        // Header with Razorpay badge
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(2);
        Text title = new Text("Razorpay Secure Escrow Checkout");
        title.setStyle("-fx-font-family:'Poppins'; -fx-font-size:17px; -fx-font-weight:bold; -fx-fill:#1B4332;");

        HBox keyBox = new HBox(6);
        keyBox.setAlignment(Pos.CENTER_LEFT);
        Label keyBadge = new Label("🔑 Key: rzp_test_TXfhlJNL2ajvs6 (Active)");
        keyBadge.setStyle("-fx-background-color: #EFF6FF; -fx-text-fill: #1D4ED8; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 2 6; -fx-background-radius: 4;");
        Text sub = new Text("100% Protected Farmer Escrow");
        sub.setStyle("-fx-font-family:'Poppins'; -fx-font-size:11px; -fx-fill:#6B7280;");
        keyBox.getChildren().addAll(keyBadge, sub);

        titleBox.getChildren().addAll(title, keyBox);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-font-size:15px; -fx-font-weight:bold; -fx-text-fill:#6B7280; -fx-cursor:hand;");
        closeBtn.setOnAction(e -> root.getChildren().remove(overlay));

        header.getChildren().addAll(titleBox, spacer, closeBtn);

        // Order Summary Box
        int dailyRate = r.getDailyRate() > 0 ? r.getDailyRate() : (amount / Math.max(1, r.getDays()));
        VBox summaryBox = new VBox(6);
        summaryBox.setPadding(new Insets(10, 14, 10, 14));
        summaryBox.setStyle("-fx-background-color: #F8FAF9; -fx-background-radius: 10; -fx-border-color: #E2E8E4; -fx-border-radius: 10;");

        summaryBox.getChildren().addAll(
            createModalRow("Hired Operator:", r.getOperatorName() != null ? r.getOperatorName() : "Certified Operator"),
            createModalRow("Operation / Machinery:", r.getMachineryName() != null ? r.getMachineryName() : "Tractor Operation"),
            createModalRow("Schedule / Days:", (r.getStartDate() != null ? r.getStartDate() : "Tomorrow, 08:00 AM") + (r.getDays() > 0 ? " (" + r.getDays() + " Days)" : "")),
            createModalRow("Operator Wage Rate:", "₹" + dailyRate + " / day"),
            createModalRow("Total Payable (INR):", "₹" + String.format("%,d", amount))
        );

        // Security & Gate Info Note
        VBox infoNote = new VBox(4);
        infoNote.setPadding(new Insets(10, 12, 10, 12));
        infoNote.setStyle("-fx-background-color: #F0FDF4; -fx-background-radius: 8px; -fx-border-color: #BBF7D0; -fx-border-radius: 8px;");

        Text noteHeader = new Text("🔒 Razorpay Gateway Verification Required");
        noteHeader.setStyle("-fx-font-family:'Poppins'; -fx-font-size:12px; -fx-font-weight:bold; -fx-fill:#166534;");

        Text noteDesc = new Text("Click below to open the Razorpay Console in your browser. Complete your payment via UPI (GPay/PhonePe/Paytm), Cards, or NetBanking. Once verified by Razorpay, this system will automatically advance and unlock the operator.");
        noteDesc.setStyle("-fx-font-family:'Poppins'; -fx-font-size:11px; -fx-fill:#374151; -fx-line-spacing: 2px;");
        noteDesc.setWrappingWidth(420);

        infoNote.getChildren().addAll(noteHeader, noteDesc);

        // Waiting Status Box
        VBox statusBox = new VBox(4);
        statusBox.setPadding(new Insets(10, 12, 10, 12));
        statusBox.setStyle("-fx-background-color: #EFF6FF; -fx-background-radius: 8px; -fx-border-color: #BFDBFE; -fx-border-radius: 8px;");
        statusBox.setVisible(false);
        statusBox.setManaged(false);

        Text statusTitle = new Text("🌐 Razorpay Console Active in Browser");
        statusTitle.setStyle("-fx-font-family:'Poppins'; -fx-font-size:12px; -fx-font-weight:bold; -fx-fill:#1D4ED8;");

        Text statusText = new Text("Please complete the payment in your browser. FarmEquip is listening in real-time and will automatically verify and proceed once done.");
        statusText.setStyle("-fx-font-family:'Poppins'; -fx-font-size:11px; -fx-fill:#374151;");
        statusText.setWrappingWidth(420);

        statusBox.getChildren().addAll(statusTitle, statusText);

        // Action Button: Only Razorpay Console
        Button openConsoleBtn = new Button("🌐 Pay ₹" + String.format("%,d", amount) + " on Razorpay Console");
        openConsoleBtn.setPrefHeight(44);
        openConsoleBtn.setMaxWidth(Double.MAX_VALUE);
        openConsoleBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #059669, #10b981);" +
            "-fx-text-fill: white;" +
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:14px;" +
            "-fx-font-weight:bold;" +
            "-fx-background-radius:8;" +
            "-fx-cursor:hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(16,185,129,0.35), 8, 0, 0, 3);"
        );

        openConsoleBtn.setOnAction(e -> {
            openConsoleBtn.setDisable(true);
            openConsoleBtn.setText("⏳ Waiting for Razorpay Payment in Browser...");
            infoNote.setVisible(false);
            infoNote.setManaged(false);
            statusBox.setVisible(true);
            statusBox.setManaged(true);

            com.desgin.service.RazorpayService.startRazorpayConsolePayment(
                amount,
                r.getRequestId(),
                r.getMachineryName() != null ? r.getMachineryName() : "Operator Escrow Wage",
                r.getFarmerName(),
                r.getFarmerEmail(),
                r.getFarmerPhone(),
                new com.desgin.service.RazorpayService.RazorpayCallback() {
                    @Override
                    public void onPaymentSuccess(String paymentId, String orderId) {
                        javafx.application.Platform.runLater(() -> {
                            processSuccessfulPayment(r, amount, paymentId, "Razorpay Online Console", root, overlay);
                        });
                    }

                    @Override
                    public void onPaymentFailure(String errorMessage) {
                        javafx.application.Platform.runLater(() -> {
                            openConsoleBtn.setDisable(false);
                            openConsoleBtn.setText("🌐 Pay ₹" + String.format("%,d", amount) + " on Razorpay Console");
                            statusTitle.setText("⚠️ Payment Incomplete or Cancelled");
                            statusText.setText("Razorpay did not confirm payment. Please click below to try again. (You cannot proceed without a valid payment).");
                        });
                    }
                }
            );
        });

        modal.getChildren().addAll(header, summaryBox, infoNote, statusBox, openConsoleBtn);
        overlay.getChildren().add(modal);
        overlay.setOnMouseClicked(e -> {
            if (e.getTarget() == overlay) root.getChildren().remove(overlay);
        });

        root.getChildren().add(overlay);
    }

    private static void processSuccessfulPayment(RentalRequestModel r, int amount, String txnId, String pMode, StackPane root, StackPane overlay) {
        new Thread(() -> {
            try {
                // 1. Update RentalRequest status & payment in Firestore
                new RentalRequestDAO().updatePaymentStatus(r.getRequestId(), "PAID", txnId, pMode);

                // 2. Record payment in payments collection
                PaymentModel pm = new PaymentModel(
                    "PAY_" + System.currentTimeMillis(),
                    "order_TXfhl_" + (System.currentTimeMillis() % 100000),
                    txnId,
                    r.getRequestId(),
                    r.getFarmerEmail(),
                    r.getFarmerName(),
                    r.getProviderEmail(),
                    r.getProviderName(),
                    r.getOperatorId(),
                    amount
                );
                pm.setPaymentMethod(pMode);
                new PaymentDAO().recordPayment(pm);

                // 3. Update in-memory models
                r.setPaymentStatus("PAID");
                r.setStatus("CONFIRMED");

                // 4. Send Notification to Operator
                String opEmail = (r.getOperatorId() != null && !r.getOperatorId().trim().isEmpty()) ? r.getOperatorId().trim().toLowerCase() : "operator@farmequip.com";
                com.desgin.model.NotificationModel opNotif = new com.desgin.model.NotificationModel(
                    "NOTIF_" + System.currentTimeMillis(),
                    opEmail,
                    "💰 Payment Received: Job #" + r.getRequestId(),
                    "Farmer " + (r.getFarmerName() != null ? r.getFarmerName() : "Farmer") + " has completed payment of ₹" + String.format("%,d", amount) + " via Razorpay. Shift is ready to start!",
                    "PAYMENT",
                    r.getRequestId()
                );
                new com.desgin.dao.NotificationDAO().sendNotification(opNotif);

                javafx.application.Platform.runLater(() -> {
                    if (root != null && overlay != null) root.getChildren().remove(overlay);
                    showPaymentSuccessPopup(root, amount, txnId, pMode, r.getOperatorName());
                });
            } catch (Exception ex) {
                javafx.application.Platform.runLater(() -> {
                    if (root != null && overlay != null) root.getChildren().remove(overlay);
                });
            }
        }).start();
    }

    private static void showPaymentSuccessPopup(StackPane root, int amount, String txnId, String pMode, String opName) {
        if (root == null) return;

        StackPane successOverlay = new StackPane();
        successOverlay.setAlignment(Pos.CENTER);
        successOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");

        VBox card = new VBox(12);
        card.setPrefWidth(450);
        card.setMaxWidth(450);
        card.setMaxHeight(Region.USE_PREF_SIZE);
        card.setMinHeight(Region.USE_PREF_SIZE);
        card.setPadding(new Insets(22));
        card.setAlignment(Pos.CENTER);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: #A7F3D0;" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 0, 8);"
        );
        StackPane.setAlignment(card, Pos.CENTER);

        // Success Icon
        Label iconLbl = new Label("🎉");
        iconLbl.setStyle("-fx-font-size: 34px; -fx-background-color: #DCFCE7; -fx-padding: 10 14; -fx-background-radius: 50;");

        Text title = new Text("Payment Verified & Escrow Secured!");
        title.setStyle("-fx-font-family:'Poppins'; -fx-font-size:17px; -fx-font-weight:bold; -fx-fill:#1B4332;");

        Text amtText = new Text("₹" + String.format("%,d", amount));
        amtText.setStyle("-fx-font-family:'Poppins'; -fx-font-size:24px; -fx-font-weight:bold; -fx-fill:#15803D;");

        VBox detailsBox = new VBox(6);
        detailsBox.setPadding(new Insets(10, 14, 10, 14));
        detailsBox.setStyle("-fx-background-color: #F8FAF9; -fx-background-radius: 10; -fx-border-color: #E2E8E4; -fx-border-radius: 10;");

        detailsBox.getChildren().addAll(
            createModalRow("Razorpay Txn ID:", txnId),
            createModalRow("API Gateway Key:", "rzp_test_TXfhlJNL2ajvs6"),
            createModalRow("Payment Method:", pMode),
            createModalRow("Escrow Protection:", "100% Guaranteed"),
            createModalRow("Assigned Operator:", opName != null ? opName : "Certified Operator")
        );

        Label notice = new Label("Operator has been notified and can now start the shift with live countdown tracking.");
        notice.setStyle("-fx-font-family:'Poppins'; -fx-font-size:11.5px; -fx-text-fill:#4B5563; -fx-text-alignment:center;");
        notice.setWrapText(true);

        Button continueBtn = new Button("✓ Continue to Dashboard");
        continueBtn.setPrefHeight(40);
        continueBtn.setMaxWidth(Double.MAX_VALUE);
        continueBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #15803D, #22C55E);" +
            "-fx-text-fill: white;" +
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-font-weight:bold;" +
            "-fx-background-radius:8;" +
            "-fx-cursor:hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(21,128,61,0.3), 6, 0, 0, 2);"
        );

        continueBtn.setOnAction(e -> {
            root.getChildren().remove(successOverlay);
            if (com.desgin.view.farmer.Swapnil.FarmerDashboard.borderPane != null) {
                com.desgin.view.farmer.Swapnil.FarmerDashboard.borderPane.setCenter(Payment.getPaymentSection());
            }
        });

        card.getChildren().addAll(iconLbl, title, amtText, detailsBox, notice, continueBtn);
        successOverlay.getChildren().add(card);
        root.getChildren().add(successOverlay);
    }

    private static HBox createModalRow(String label, String value) {
        Text l = new Text(label);
        l.setStyle("-fx-font-family:'Poppins'; -fx-font-size:12px; -fx-fill:#6B7280;");
        Text v = new Text(value);
        v.setStyle("-fx-font-family:'Poppins'; -fx-font-size:12px; -fx-font-weight:bold; -fx-fill:#1B4332;");
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        HBox row = new HBox(l, sp, v);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private static VBox createPaymentCard(
            String equipmentName,
            String transactionId,
            String amount,
            String paymentMethod,
            String paymentDate,
            String paymentStatus,
            String imagePath) {

        Image image = new Image(imagePath);

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(140);
        imageView.setFitHeight(110);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        StackPane imagePane = new StackPane(imageView);

        imagePane.setStyle(
                "-fx-background-color:#E8DED2;" +
                "-fx-background-radius:10;"
        );

    //-----------------------------------------------------
    // Equipment Name
    //-----------------------------------------------------

    Text name = new Text(equipmentName);
    name.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:18px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

    Text txn = new Text("Transaction ID : " + transactionId);
    txn.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#6F5A4D;"
    );

    Text date = new Text("Payment Date : " + paymentDate);
    date.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#6F5A4D;"
    );

    Text method = new Text("Method : " + paymentMethod);
    method.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#6F5A4D;"
    );

    Text price = new Text(amount);

    price.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:20px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#6B8E23;"
    );

    //-----------------------------------------------------
    // Status Badge
    //-----------------------------------------------------

    Label status = new Label(paymentStatus);

    if(paymentStatus.equalsIgnoreCase("Paid")){

        status.setStyle(
                "-fx-background-color:#DFF5E1;" +
                "-fx-background-radius:20;" +
                "-fx-padding:6 18;" +
                "-fx-text-fill:#2E7D32;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;"
        );

    }else if(paymentStatus.equalsIgnoreCase("Pending")){

        status.setStyle(
                "-fx-background-color:#FFF4D6;" +
                "-fx-background-radius:20;" +
                "-fx-padding:6 18;" +
                "-fx-text-fill:#EF6C00;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;"
        );

    }else{

        status.setStyle(
                "-fx-background-color:#FDE2E2;" +
                "-fx-background-radius:20;" +
                "-fx-padding:6 18;" +
                "-fx-text-fill:#C62828;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;"
        );

    }

    //-----------------------------------------------------
    // Buttons
    //-----------------------------------------------------

   Button invoiceButton = new Button("Download Invoice");

invoiceButton.setOnAction(e -> {

    FileChooser chooser = new FileChooser();

    chooser.setTitle("Save Invoice");

    chooser.setInitialFileName(transactionId + ".txt");

    chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                    "Text File",
                    "*.txt"
            )
    );

    Window window = invoiceButton.getScene().getWindow();

    File file = chooser.showSaveDialog(window);

    if(file != null){

        try{

            FileWriter writer = new FileWriter(file);

            writer.write("=====================================\n");
            writer.write("         FARM RENTAL INVOICE\n");
            writer.write("=====================================\n\n");

            writer.write("Equipment      : " + equipmentName + "\n");
            writer.write("Transaction ID : " + transactionId + "\n");
            writer.write("Amount         : " + amount + "\n");
            writer.write("Payment Method : " + paymentMethod + "\n");
            writer.write("Payment Date   : " + paymentDate + "\n");
            writer.write("Status         : " + paymentStatus + "\n");

            writer.write("\n-------------------------------------\n");
            writer.write("Thank you for using our platform.\n");
            writer.write("-------------------------------------");

            writer.close();

            System.out.println("Invoice Downloaded Successfully");

        }catch(IOException ex){

            ex.printStackTrace();

        }

    }

});

    invoiceButton.setStyle(
            "-fx-background-color:#4A2C20;" +
            "-fx-background-radius:8;" +
            "-fx-text-fill:white;" +
            "-fx-font-family:'Poppins';" +
            "-fx-font-weight:bold;" +
            "-fx-cursor:hand;"
    );

    invoiceButton.setPrefHeight(38);

    invoiceButton.setOnMouseEntered(e->{

        invoiceButton.setStyle(
                "-fx-background-color:#6B4A3A;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:white;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

    });

    invoiceButton.setOnMouseExited(e->{

        invoiceButton.setStyle(
                "-fx-background-color:#4A2C20;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:white;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

    });

    Button payNowButton = new Button("Pay Now");

    payNowButton.setStyle(
            "-fx-background-color:#6B8E23;" +
            "-fx-background-radius:8;" +
            "-fx-text-fill:white;" +
            "-fx-font-family:'Poppins';" +
            "-fx-font-weight:bold;" +
            "-fx-cursor:hand;"
    );

    payNowButton.setPrefHeight(38);

    payNowButton.setVisible(
            paymentStatus.equalsIgnoreCase("Pending")
    );

    payNowButton.setManaged(
            paymentStatus.equalsIgnoreCase("Pending")
    );

    payNowButton.setOnMouseEntered(e->{

        payNowButton.setStyle(
                "-fx-background-color:#55751C;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:white;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

    });

    payNowButton.setOnMouseExited(e->{

        payNowButton.setStyle(
                "-fx-background-color:#6B8E23;" +
                "-fx-background-radius:8;" +
                "-fx-text-fill:white;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

    });

    payNowButton.setOnAction(e -> {

         showPaymentDialog(
            equipmentName,
            amount,
            status,
            payNowButton
    );

});

    HBox buttonBox = new HBox(
            12,
            invoiceButton,
            payNowButton
    );

    VBox details = new VBox(
            8,
            name,
            txn,
            date,
            method,
            price,
            status,
            buttonBox
    );

    HBox content = new HBox(
            20,
            imagePane,
            details
    );

    content.setAlignment(Pos.CENTER_LEFT);

    VBox card = new VBox(content);

    card.setPadding(new Insets(18));

    card.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:14;" +
            "-fx-border-color:#E0D4C7;" +
            "-fx-border-radius:14;" +
            "-fx-border-width:1;"
    );

    return card;
}

    // =========================================================
    // Dynamic Farmer Spending Graphs (Requirement 5)
    // =========================================================
    private static HBox createFarmerSpendingCharts(List<PaymentModel> payments, List<RentalRequestModel> requests,
                                                   int totalPaid, int pendingAmt, int refundAmt) {
        VBox monthlyChart = createMonthlySpendingChart(payments, requests);
        VBox categoryChart = createCategorySpendingChart(requests);
        VBox statusChart = createPaymentStatusChart(totalPaid, pendingAmt, refundAmt);

        monthlyChart.setMinWidth(0);
        categoryChart.setMinWidth(0);
        statusChart.setMinWidth(0);

        HBox.setHgrow(monthlyChart, Priority.ALWAYS);
        HBox.setHgrow(categoryChart, Priority.ALWAYS);
        HBox.setHgrow(statusChart, Priority.ALWAYS);

        HBox row = new HBox(16, monthlyChart, categoryChart, statusChart);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private static VBox createMonthlySpendingChart(List<PaymentModel> payments, List<RentalRequestModel> requests) {
        Text title = new Text("📈 Monthly Spending Curve");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Rental expenditures across 2026 calendar months");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Spent (₹)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(false);
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(240);
        barChart.setMinHeight(240);

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        java.util.Map<String, Integer> monthTotals = new java.util.LinkedHashMap<>();
        for (String m : months) {
            monthTotals.put(m, 0);
        }

        for (PaymentModel p : payments) {
            if ("PAID".equalsIgnoreCase(p.getPaymentStatus()) && p.getCreatedAt() != null) {
                for (String m : months) {
                    if (p.getCreatedAt().toLowerCase().contains(m.toLowerCase())) {
                        monthTotals.put(m, monthTotals.get(m) + p.getAmount());
                        break;
                    }
                }
            }
        }

        for (RentalRequestModel r : requests) {
            if (("PAID".equalsIgnoreCase(r.getPaymentStatus()) || "COMPLETED".equalsIgnoreCase(r.getStatus())) && r.getStartDate() != null) {
                int amt = r.getTotalAmount() > 0 ? r.getTotalAmount() : (r.getDailyRate() * Math.max(1, r.getDays()));
                for (String m : months) {
                    if (r.getStartDate().toLowerCase().contains(m.toLowerCase())) {
                        if (payments.isEmpty()) {
                            monthTotals.put(m, monthTotals.get(m) + amt);
                        }
                        break;
                    }
                }
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (String m : months) {
            series.getData().add(new XYChart.Data<>(m, monthTotals.get(m)));
        }
        barChart.getData().add(series);

        VBox card = new VBox(8, new VBox(2, title, sub), barChart);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return card;
    }

    private static VBox createCategorySpendingChart(List<RentalRequestModel> requests) {
        Text title = new Text("🥧 Spending by Equipment");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Rental share by machinery type");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        java.util.Map<String, Integer> catMap = new java.util.HashMap<>();
        for (RentalRequestModel r : requests) {
            String name = r.getMachineryName() != null ? r.getMachineryName() : "Machinery";
            int amt = r.getTotalAmount() > 0 ? r.getTotalAmount() : (r.getDailyRate() * Math.max(1, r.getDays()));
            catMap.put(name, catMap.getOrDefault(name, 0) + amt);
        }

        if (catMap.isEmpty()) {
            VBox emptyBox = new VBox(8);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPrefHeight(240);
            Text emptyIco = new Text("🚜");
            emptyIco.setStyle("-fx-font-size: 32px;");
            Text emptyTxt = new Text("No Category Spending Yet");
            emptyTxt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");
            Text emptySub = new Text("Rental spend by machine type will populate here.");
            emptySub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #6B7280;");
            emptyBox.getChildren().addAll(emptyIco, emptyTxt, emptySub);

            VBox card = new VBox(8, new VBox(2, title, sub), emptyBox);
            card.setPadding(new Insets(16));
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
            return card;
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (java.util.Map.Entry<String, Integer> entry : catMap.entrySet()) {
            pieData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        PieChart pieChart = new PieChart(pieData);
        pieChart.setAnimated(false);
        pieChart.setLegendVisible(true);
        pieChart.setPrefHeight(240);
        pieChart.setMinHeight(240);

        VBox card = new VBox(8, new VBox(2, title, sub), pieChart);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return card;
    }

    private static VBox createPaymentStatusChart(int paidAmt, int pendingAmt, int refundAmt) {
        Text title = new Text("📊 Payment Status Breakdown");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Real-time settlement proportions");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        if (paidAmt == 0 && pendingAmt == 0 && refundAmt == 0) {
            VBox emptyBox = new VBox(8);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPrefHeight(240);
            Text emptyIco = new Text("💳");
            emptyIco.setStyle("-fx-font-size: 32px;");
            Text emptyTxt = new Text("No Payments Recorded");
            emptyTxt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");
            Text emptySub = new Text("Paid and pending status breakdown will appear here.");
            emptySub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #6B7280;");
            emptyBox.getChildren().addAll(emptyIco, emptyTxt, emptySub);

            VBox card = new VBox(8, new VBox(2, title, sub), emptyBox);
            card.setPadding(new Insets(16));
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
            return card;
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        if (paidAmt > 0) pieData.add(new PieChart.Data("Paid (₹" + paidAmt + ")", paidAmt));
        if (pendingAmt > 0) pieData.add(new PieChart.Data("Pending (₹" + pendingAmt + ")", pendingAmt));
        if (refundAmt > 0) pieData.add(new PieChart.Data("Refunded (₹" + refundAmt + ")", refundAmt));

        PieChart pieChart = new PieChart(pieData);
        pieChart.setAnimated(false);
        pieChart.setLegendVisible(true);
        pieChart.setPrefHeight(240);
        pieChart.setMinHeight(240);

        VBox card = new VBox(8, new VBox(2, title, sub), pieChart);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return card;
    }

//---------------------------------------------------------
// Summary Card
//---------------------------------------------------------

private static VBox createSummaryCard(
        String title,
        String value,
        String icon,
        String color){

    Text iconText = new Text(icon);
    iconText.setStyle("-fx-font-size:24px;");

    Text titleText = new Text(title);
    titleText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;" +
            "-fx-fill:#7A6658;"
    );

    Text valueText = new Text(value);
    valueText.setStyle(
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:24px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:" + color + ";"
    );

    VBox card = new VBox(
            10,
            iconText,
            titleText,
            valueText
    );

    card.setPadding(new Insets(18));

    card.setPrefWidth(190);

    card.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:12;" +
            "-fx-border-color:#E0D4C7;" +
            "-fx-border-radius:12;" +
            "-fx-border-width:1;"
    );

    return card;
}

private static void showPaymentDialog(
        String equipmentName,
        String amount,
        Label statusLabel,
        Button payNowButton) {

    StackPane rootPane = com.desgin.view.farmer.Swapnil.FarmerDashboard.root;
    if (rootPane == null) return;

    StackPane overlay = new StackPane();
    overlay.setAlignment(Pos.CENTER);
    overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

    VBox root = new VBox(15);
    root.setPadding(new Insets(20));
    root.setAlignment(Pos.CENTER);
    root.setMaxWidth(400);
    root.setMaxHeight(Region.USE_PREF_SIZE);
    root.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
    StackPane.setAlignment(root, Pos.CENTER);

    Text title = new Text("Payment");
    title.setStyle("-fx-font-size:20; -fx-font-weight:bold; -fx-fill: #1B4332;");

    Label equipment = new Label("Equipment : " + equipmentName);
    Label price = new Label("Amount : " + amount);

    ComboBox<String> paymentMethod = new ComboBox<>();
    paymentMethod.getItems().addAll("UPI", "Card", "Net Banking");
    paymentMethod.setPromptText("Select Payment Method");

    TextField details = new TextField();
    details.setPromptText("Enter UPI ID / Card Number");

    Button confirm = new Button("Confirm Payment");
    confirm.setStyle("-fx-background-color:#2D6A4F; -fx-text-fill:white; -fx-font-weight:bold; -fx-background-radius:6; -fx-cursor:hand;");

    Button closeBtn = new Button("Cancel");
    closeBtn.setStyle("-fx-background-color:#E5E7EB; -fx-text-fill:#374151; -fx-background-radius:6; -fx-cursor:hand;");
    closeBtn.setOnAction(e -> rootPane.getChildren().remove(overlay));

    confirm.setOnAction(event -> {
        if (paymentMethod.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select payment method.");
            alert.show();
            return;
        }

        if (details.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Enter payment details.");
            alert.show();
            return;
        }

        statusLabel.setText("Paid");
        statusLabel.setStyle(
                "-fx-background-color:#DFF5E1;" +
                "-fx-background-radius:20;" +
                "-fx-padding:6 18;" +
                "-fx-text-fill:#2E7D32;" +
                "-fx-font-weight:bold;"
        );

        payNowButton.setVisible(false);
        payNowButton.setManaged(false);

        rootPane.getChildren().remove(overlay);
    });

    HBox btnRow = new HBox(10, closeBtn, confirm);
    btnRow.setAlignment(Pos.CENTER);

    root.getChildren().addAll(title, equipment, price, paymentMethod, details, btnRow);
    overlay.getChildren().add(root);
    overlay.setOnMouseClicked(e -> {
        if (e.getTarget() == overlay) rootPane.getChildren().remove(overlay);
    });

    rootPane.getChildren().add(overlay);
}
private static VBox createPaymentFilter(){

    Text title = new Text("Filters");

    title.setStyle(
            "-fx-font-size:20px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;" +
            "-fx-font-family:'Poppins';"
    );

    Text statusTitle = new Text("Payment Status");

    statusTitle.setStyle(
            "-fx-font-size:14px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

    paid = new CheckBox("Paid");
    pending = new CheckBox("Pending");
    failed = new CheckBox("Failed");

    styleCheckBox(paid);
    styleCheckBox(pending);
    styleCheckBox(failed);

    VBox statusBox = new VBox(
            10,
            statusTitle,
            paid,
            pending,
            failed
    );

    Text methodTitle = new Text("Payment Method");

    methodTitle.setStyle(
            "-fx-font-size:14px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#4A2C20;"
    );

        upi = new CheckBox("UPI");
        card = new CheckBox("Card");
        net = new CheckBox("Net Banking");

    styleCheckBox(upi);
    styleCheckBox(card);
    styleCheckBox(net);

    VBox methodBox = new VBox(
            10,
            methodTitle,
            upi,
            card,
            net
    );

    

   Button apply = new Button("Apply Filters");
styleGreenButton(apply);

apply.setOnAction(e -> {

    boolean paidSelected = paid.isSelected();
    boolean pendingSelected = pending.isSelected();
    boolean failedSelected = failed.isSelected();

    boolean upiSelected = upi.isSelected();
    boolean cardSelected = card.isSelected();
    boolean netSelected = net.isSelected();

    for(int i=0;i<paymentCardList.size();i++){

        VBox cardBox = paymentCardList.get(i);

        String status = paymentStatusList.get(i);
        String method = paymentMethodList.get(i);

        boolean statusMatch =
                (!paidSelected && !pendingSelected && !failedSelected)
                || (paidSelected && status.equalsIgnoreCase("Paid"))
                || (pendingSelected && status.equalsIgnoreCase("Pending"))
                || (failedSelected && status.equalsIgnoreCase("Failed"));

        boolean methodMatch =
                (!upiSelected && !cardSelected && !netSelected)
                || (upiSelected && method.equalsIgnoreCase("UPI"))
                || (cardSelected && method.equalsIgnoreCase("Card"))
                || (netSelected && method.equalsIgnoreCase("Net Banking"));

        boolean visible = statusMatch && methodMatch;

        cardBox.setVisible(visible);
        cardBox.setManaged(visible);
    }

});

    

    Button reset = new Button("Reset");

        reset.setMaxWidth(Double.MAX_VALUE);
        reset.setPrefHeight(42);

        reset.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-border-color:#D8C7B5;" +
                "-fx-border-radius:9;" +
                "-fx-background-radius:9;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

    reset.setOnAction(e -> {

    // Clear search
    searchField.clear();

    // Clear payment status filters
    paid.setSelected(false);
    pending.setSelected(false);
    failed.setSelected(false);

    // Clear payment method filters
    upi.setSelected(false);
    card.setSelected(false);
    net.setSelected(false);

    // Optional: reload all payment cards
    System.out.println("Filters Reset Successfully");
});

    VBox buttons = new VBox(
            10,
            apply,
            reset
    );

    VBox filter = new VBox(
            22,
            title,
            statusBox,
            methodBox,
            buttons
    );

    filter.setPadding(new Insets(20));

    filter.setPrefWidth(235);

    filter.setStyle(
            "-fx-background-color:#F5EFE6;" +
            "-fx-background-radius:14;" +
            "-fx-border-color:#D8C7B5;" +
            "-fx-border-radius:14;"
    );

    return filter;
}

private static void stylePaymentTextField(TextField field){

    field.setPrefHeight(45);

    field.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:10;" +
            "-fx-border-color:#D8C7B5;" +
            "-fx-border-radius:10;" +
            "-fx-padding:0 15;" +
            "-fx-font-family:'Poppins';" +
            "-fx-font-size:13px;"
    );
}
    private static void styleCheckBox(CheckBox checkBox) {

    checkBox.setStyle(
            "-fx-text-fill: #5C4033;" +
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 13px;" +
            "-fx-cursor: hand;"
    );

}

private static void styleGreenButton(Button button){

    button.setPrefHeight(45);

    button.setStyle(
            "-fx-background-color:#6B8E23;" +
            "-fx-background-radius:10;" +
            "-fx-text-fill:white;" +
            "-fx-font-family:'Poppins';" +
            "-fx-font-weight:bold;" +
            "-fx-cursor:hand;"
    );

    button.setOnMouseEntered(e->{

        button.setStyle(
                "-fx-background-color:#55751C;" +
                "-fx-background-radius:10;" +
                "-fx-text-fill:white;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

    });

    button.setOnMouseExited(e->{

        button.setStyle(
                "-fx-background-color:#6B8E23;" +
                "-fx-background-radius:10;" +
                "-fx-text-fill:white;" +
                "-fx-font-family:'Poppins';" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

    });
}
}

