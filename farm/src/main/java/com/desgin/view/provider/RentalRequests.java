package com.desgin.view.provider;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RentalRequests {

    public static class RequestItem {
        public String reqId;
        public String farmerName;
        public String phone;
        public String location;
        public String landDetails;
        public String equipmentName;
        public String dates;
        public int days;
        public int grossFare;
        public int netPayout;
        public String deliveryMode;
        public String status; // "PENDING", "ACTIVE", "COMPLETED", "CANCELLED"
        public String paymentStatus;

        public RequestItem(String reqId, String farmerName, String phone, String location, String landDetails, String equipmentName, String dates, int days, int grossFare, int netPayout, String deliveryMode, String status, String paymentStatus) {
            this.reqId = reqId;
            this.farmerName = farmerName;
            this.phone = phone;
            this.location = location;
            this.landDetails = landDetails;
            this.equipmentName = equipmentName;
            this.dates = dates;
            this.days = days;
            this.grossFare = grossFare;
            this.netPayout = netPayout;
            this.deliveryMode = deliveryMode;
            this.status = status;
            this.paymentStatus = paymentStatus;
        }
    }

    private static List<RequestItem> requestsList = new ArrayList<>();
    private static VBox listContainer;
    private static String activeTab = "PENDING";
    private static String searchFilter = "";

    static {
        initDefaultRequests();
    }

    private static void initDefaultRequests() {
        if (!requestsList.isEmpty()) return;
        requestsList.add(new RequestItem("#REQ-9102", "Suresh Patil", "+91 98221 44551", "Baramati (12 km away)", "15 Acres Sugarcane Field", "John Deere 5310 Tractor (55 HP)", "18 Aug - 22 Aug 2026", 5, 7500, 7125, "With Driver / Operator", "PENDING", "Deposit Held in Escrow"));
        requestsList.add(new RequestItem("#REQ-9105", "Anand Kadam", "+91 94230 78119", "Indapur (28 km away)", "25 Acres Wheat Harvest", "Kartar Combine Harvester 4000", "20 Aug - 23 Aug 2026", 4, 14000, 13300, "Self-Pickup by Farmer", "PENDING", "Payment Verified"));
        requestsList.add(new RequestItem("#REQ-9108", "Dnyaneshwar Shinde", "+91 98900 12894", "Saswad (18 km away)", "8 Acres Onion & Veg Beds", "Shaktiman Rotary Tiller (7ft)", "19 Aug - 20 Aug 2026", 2, 1600, 1520, "Provider Delivery Needed", "PENDING", "Payment Verified"));
        requestsList.add(new RequestItem("#REQ-9112", "Pravin Deshmukh", "+91 97632 99401", "Shirur (35 km away)", "18 Acres Cotton Land Prep", "Mahindra 575 DI (45HP)", "21 Aug - 24 Aug 2026", 4, 4800, 4560, "With Driver / Operator", "PENDING", "Deposit Held in Escrow"));
        requestsList.add(new RequestItem("#REQ-9115", "Kiran Bhosale", "+91 98504 33219", "Daund (40 km away)", "12 Acres Soybean Spraying", "Agri-Drone 16L Autonomous Sprayer", "22 Aug 2026", 1, 1800, 1710, "Provider Operator Required", "PENDING", "Payment Verified"));

        // Active Deployments
        requestsList.add(new RequestItem("#REQ-8991", "Ramesh Waghmare", "+91 98901 44552", "Saswad (Plot 4)", "20 Acres Soil Tillage", "John Deere 5310 Tractor (55HP)", "15 Aug - 19 Aug 2026", 5, 7500, 7125, "Driver Dilip Shinde Assigned", "ACTIVE", "Active Escrow"));
        requestsList.add(new RequestItem("#REQ-8984", "Balasaheb Shirole", "+91 98229 11029", "Baramati East", "30 Acres Paddy Harvesting", "Kartar Combine Harvester 4000", "14 Aug - 18 Aug 2026", 5, 17500, 16625, "Self Operated by Farmer", "ACTIVE", "Active Escrow"));

        // Completed
        requestsList.add(new RequestItem("#REQ-8890", "Vikas More", "+91 98555 12001", "Jejuri Agri Cluster", "10 Acres Rotavator Prep", "Shaktiman Rotary Tiller (7ft)", "10 Aug - 12 Aug 2026", 3, 2400, 2280, "Completed on Time", "COMPLETED", "Settled to Bank"));
        requestsList.add(new RequestItem("#REQ-8874", "Ganesh Jadhav", "+91 94220 88910", "Phaltan Border", "15 Acres Sowing", "FieldKing 9-Tyne Cultivator", "07 Aug - 09 Aug 2026", 3, 1800, 1710, "Completed on Time", "COMPLETED", "Settled to Bank"));
    }

    public static ScrollPane getRequestsSection(StackPane root) {
        Text headerTitle = new Text("Rental Requests & Booking Management");
        headerTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text headerSubtitle = new Text("Review farmer booking inquiries, accept or decline rental schedules, coordinate logistics, and track ongoing rentals.");
        headerSubtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #806A5B;");

        VBox titleBox = new VBox(4, headerTitle, headerSubtitle);

        // Search Bar
        TextField searchInput = new TextField();
        searchInput.setPromptText("Search by Request ID, Farmer Name, Location or Machinery...");
        searchInput.setPrefHeight(40);
        searchInput.setPrefWidth(420);
        searchInput.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D8C7B5; -fx-border-radius: 8; -fx-font-family: 'Poppins'; -fx-font-size: 13px;");
        searchInput.textProperty().addListener((obs, oldV, newV) -> {
            searchFilter = newV.toLowerCase().trim();
            renderRequestsList(root);
        });

        // Tab Buttons
        HBox tabBox = createTabNavigation(root);

        // Container for request cards
        listContainer = new VBox(16);
        renderRequestsList(root);

        VBox content = new VBox(20, titleBox, searchInput, tabBox, listContainer);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static HBox createTabNavigation(StackPane root) {
        long pendingCount = requestsList.stream().filter(r -> "PENDING".equals(r.status)).count();
        long activeCount = requestsList.stream().filter(r -> "ACTIVE".equals(r.status)).count();
        long completedCount = requestsList.stream().filter(r -> "COMPLETED".equals(r.status)).count();

        Button tabPending = new Button("Pending Approvals (" + pendingCount + ")");
        Button tabActive = new Button("Active On-Field (" + activeCount + ")");
        Button tabCompleted = new Button("Completed History (" + completedCount + ")");

        styleTabButton(tabPending, "PENDING".equals(activeTab));
        styleTabButton(tabActive, "ACTIVE".equals(activeTab));
        styleTabButton(tabCompleted, "COMPLETED".equals(activeTab));

        tabPending.setOnAction(e -> {
            activeTab = "PENDING";
            updateTabs(tabPending, tabActive, tabCompleted);
            renderRequestsList(root);
        });

        tabActive.setOnAction(e -> {
            activeTab = "ACTIVE";
            updateTabs(tabPending, tabActive, tabCompleted);
            renderRequestsList(root);
        });

        tabCompleted.setOnAction(e -> {
            activeTab = "COMPLETED";
            updateTabs(tabPending, tabActive, tabCompleted);
            renderRequestsList(root);
        });

        HBox bar = new HBox(12, tabPending, tabActive, tabCompleted);
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    private static void updateTabs(Button b1, Button b2, Button b3) {
        styleTabButton(b1, "PENDING".equals(activeTab));
        styleTabButton(b2, "ACTIVE".equals(activeTab));
        styleTabButton(b3, "COMPLETED".equals(activeTab));
    }

    private static void styleTabButton(Button btn, boolean active) {
        if (active) {
            btn.setStyle("-fx-background-color: #4A2C20; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 18 8 18; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: #F5EFE6; -fx-text-fill: #5C4033; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: #D8C7B5; -fx-border-radius: 8; -fx-padding: 8 18 8 18; -fx-cursor: hand;");
        }
    }

    private static void renderRequestsList(StackPane root) {
        listContainer.getChildren().clear();

        for (RequestItem item : requestsList) {
            if (!item.status.equals(activeTab)) continue;

            if (!searchFilter.isEmpty()) {
                boolean match = item.farmerName.toLowerCase().contains(searchFilter)
                        || item.reqId.toLowerCase().contains(searchFilter)
                        || item.location.toLowerCase().contains(searchFilter)
                        || item.equipmentName.toLowerCase().contains(searchFilter);
                if (!match) continue;
            }

            listContainer.getChildren().add(createRequestCard(item, root));
        }

        if (listContainer.getChildren().isEmpty()) {
            VBox empty = new VBox(10);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(50));
            Text t = new Text("No rental requests under this tab.");
            t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-fill: #806A5B;");
            empty.getChildren().add(t);
            listContainer.getChildren().add(empty);
        }
    }

    private static VBox createRequestCard(RequestItem item, StackPane root) {
        Text id = new Text(item.reqId);
        id.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #8B6F47;");

        Label st = new Label(item.status);
        String stBg = "PENDING".equals(item.status) ? "#FFF3E0" : ("ACTIVE".equals(item.status) ? "#E8F5E9" : "#ECEFF1");
        String stColor = "PENDING".equals(item.status) ? "#E65100" : ("ACTIVE".equals(item.status) ? "#2E7D32" : "#37474F");
        st.setStyle("-fx-background-color: " + stBg + "; -fx-text-fill: " + stColor + "; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox topRow = new HBox(id, topSpacer, st);
        topRow.setAlignment(Pos.CENTER_LEFT);

        // Farmer Info
        Text farmer = new Text("👤 " + item.farmerName + " (" + item.phone + ")");
        farmer.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text loc = new Text("📍 Location: " + item.location + "  •  🌾 Land: " + item.landDetails);
        loc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #5C4033;");

        // Equipment & Schedule
        Text eq = new Text("🚜 Equipment: " + item.equipmentName);
        eq.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Text dt = new Text("📅 Rental Dates: " + item.dates + " (" + item.days + " Days)  •  🚚 Logistics: " + item.deliveryMode);
        dt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #5C4033;");

        // Financials
        Text gross = new Text("Gross Fare: ₹" + String.format("%,d", item.grossFare));
        gross.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        Text net = new Text("Your Net Payout: ₹" + String.format("%,d", item.netPayout));
        net.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Label payStatus = new Label("💰 " + item.paymentStatus);
        payStatus.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 8 4 8; -fx-background-radius: 4;");

        HBox finRow = new HBox(15, gross, net, payStatus);
        finRow.setAlignment(Pos.CENTER_LEFT);

        // Action Buttons based on status
        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);

        if ("PENDING".equals(item.status)) {
            Button approve = new Button("✔ Approve & Confirm Booking");
            approve.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 14 6 14;");
            approve.setOnAction(e -> {
                item.status = "ACTIVE";
                renderRequestsList(root);
            });

            Button decline = new Button("✕ Decline Request");
            decline.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 14 6 14;");
            decline.setOnAction(e -> {
                item.status = "CANCELLED";
                renderRequestsList(root);
            });

            Button call = new Button("📞 Call Farmer");
            call.setStyle("-fx-background-color: #8B6F47; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12 6 12;");

            actions.getChildren().addAll(call, decline, approve);
        } else if ("ACTIVE".equals(item.status)) {
            Button complete = new Button("✔ Mark Job Completed & Returned");
            complete.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 14 6 14;");
            complete.setOnAction(e -> {
                item.status = "COMPLETED";
                renderRequestsList(root);
            });

            Button issue = new Button("⚠️ Report Delay / Issue");
            issue.setStyle("-fx-background-color: #E65100; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12 6 12;");

            actions.getChildren().addAll(issue, complete);
        } else {
            Button invoice = new Button("📄 Download Payout Invoice");
            invoice.setStyle("-fx-background-color: #5C4033; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 14 6 14;");

            Label review = new Label("Farmer Rating: ★★★★★ (5.0)");
            review.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

            actions.getChildren().addAll(review, invoice);
        }

        Region botSpacer = new Region();
        HBox.setHgrow(botSpacer, Priority.ALWAYS);
        HBox bottomRow = new HBox(finRow, botSpacer, actions);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(10, topRow, farmer, loc, eq, dt, bottomRow);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");
        return card;
    }
}
