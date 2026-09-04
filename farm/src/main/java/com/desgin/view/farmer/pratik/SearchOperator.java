package com.desgin.view.farmer.pratik;

import java.util.ArrayList;
import java.util.List;

import com.desgin.view.farmer.Swapnil.FarmerProfileStore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SearchOperator {

    public static class OperatorItem {
        public String id;
        public String name;
        public String phone;
        public String specialty;
        public String category;
        public String experience;
        public String town;
        public String locationDisplay;
        public String rate;
        public double rating;
        public int completedJobs;
        public String status; // "AVAILABLE" or "ON ASSIGNMENT"
        public String bio;
        public List<String> skills;
        public boolean requestApproved;
        public boolean requestSent;

        public OperatorItem(String id, String name, String phone, String specialty, String category,
                            String experience, String town, String locationDisplay, String rate, double rating,
                            int completedJobs, String status, String bio, List<String> skills) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.specialty = specialty;
            this.category = category;
            this.experience = experience;
            this.town = town;
            this.locationDisplay = locationDisplay;
            this.rate = rate;
            this.rating = rating;
            this.completedJobs = completedJobs;
            this.status = status;
            this.bio = bio;
            this.skills = skills != null ? skills : new ArrayList<>();
            this.requestApproved = false;
            this.requestSent = false;
        }
    }

    public static final List<OperatorItem> operatorsList = new ArrayList<>();

    public static void syncOperatorsFromFirestore(StackPane root) {
        Thread t = new Thread(() -> {
            try {
                com.google.cloud.firestore.Firestore db = com.desgin.config.FirestoreConfig.getFirestore();
                if (db != null) {
                    var snapshot = db.collection("Operator").get().get();
                    List<OperatorItem> list = new ArrayList<>();
                    for (var doc : snapshot.getDocuments()) {
                        String name = doc.getString("name");
                        if (name == null || name.trim().isEmpty()) continue;
                        String phone = doc.getString("num");
                        String town = doc.getString("town");
                        String district = doc.getString("district");
                        String loc = (town != null && !town.isEmpty()) ? (town + (district != null ? ", " + district : "")) : "Maharashtra";
                        list.add(new OperatorItem(
                            doc.getId(),
                            name,
                            phone != null ? phone : "Contact via Hire Request",
                            "🚜 Certified Machinery Operator",
                            "Tractor",
                            "Verified Operator",
                            town != null ? town : "Pune",
                            loc,
                            "₹600 / day",
                            5.0,
                            0,
                            "AVAILABLE",
                            "Registered agricultural machinery operator available for on-field contract hire.",
                            List.of("Tractor Driving", "Tillage Operations", "Safety Certified")
                        ));
                    }
                    javafx.application.Platform.runLater(() -> {
                        operatorsList.clear();
                        operatorsList.addAll(list);
                        updateKpis();
                        filterOperators(root);
                    });
                }
            } catch (Exception ignored) {}
        });
        t.setDaemon(true);
        t.start();
    }

    private static VBox operatorsContainer;
    private static TextField searchField;
    private static ComboBox<String> categoryFilter;
    private static ComboBox<String> statusFilter;
    private static Label feedbackBanner;
    private static Label totalOpsLabel = new Label("0");
    private static Label availOpsLabel = new Label("0");
    private static Label assignedOpsLabel = new Label("0");

    private static void updateKpis() {
        long totalOps = operatorsList.size();
        long availOps = operatorsList.stream().filter(o -> "AVAILABLE".equalsIgnoreCase(o.status)).count();
        long assignedOps = operatorsList.stream().filter(o -> "ON ASSIGNMENT".equalsIgnoreCase(o.status)).count();
        if (totalOpsLabel != null) totalOpsLabel.setText(String.valueOf(totalOps));
        if (availOpsLabel != null) availOpsLabel.setText(String.valueOf(availOps));
        if (assignedOpsLabel != null) assignedOpsLabel.setText(String.valueOf(assignedOps));
    }

    public static ScrollPane getSearchOperatorSection(StackPane root) {

        // Feedback Banner
        feedbackBanner = new Label();
        feedbackBanner.setVisible(false);
        feedbackBanner.setManaged(false);

        // ================= 3 KPI SUMMARY CARDS =================
        updateKpis();

        VBox c1 = createStatCardWithLabel("👷 Total Operators", totalOpsLabel, "Registered in your region", "#1B4332");
        VBox c2 = createStatCardWithLabel("🟢 Available to Hire", availOpsLabel, "Ready for field dispatch", "#2D6A4F");
        VBox c3 = createStatCardWithLabel("🟠 Rented Out / Assigned", assignedOpsLabel, "Currently on active fields", "#B45309");

        HBox summaryCards = new HBox(16, c1, c2, c3);
        summaryCards.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(c1, Priority.ALWAYS);
        HBox.setHgrow(c2, Priority.ALWAYS);
        HBox.setHgrow(c3, Priority.ALWAYS);

        // ================= SEARCH & FILTER CONTROLS =================
        searchField = new TextField();
        searchField.setPromptText("🔍  Search operator by name, machinery specialty, or location...");
        searchField.setPrefHeight(44);
        searchField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.3);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 12px;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #1F2937;" +
                "-fx-padding: 0 15px;"
        );

        categoryFilter = new ComboBox<>();
        categoryFilter.getItems().addAll("All Specializations", "Tractor", "Harvester", "Drone", "Rotavator");
        categoryFilter.setValue("All Specializations");
        categoryFilter.setPrefHeight(44);
        styleFilterCombo(categoryFilter);

        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Status", "Available to Hire", "On Assignment");
        statusFilter.setValue("All Status");
        statusFilter.setPrefHeight(44);
        styleFilterCombo(statusFilter);

        Button resetBtn = new Button("↺ Reset Filters");
        resetBtn.setPrefHeight(44);
        resetBtn.setStyle(
                "-fx-background-color: #E8F5E9;" +
                "-fx-text-fill: #2D6A4F;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12.5px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 0 16px;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.3);" +
                "-fx-border-radius: 12px;" +
                "-fx-cursor: hand;"
        );

        HBox filterBar = new HBox(12, searchField, categoryFilter, statusFilter, resetBtn);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        // ================= OPERATORS HORIZONTAL CARDS CONTAINER =================
        operatorsContainer = new VBox(14);
        operatorsContainer.setMaxWidth(Double.MAX_VALUE);

        filterOperators(root);

        searchField.textProperty().addListener((obs, oldV, newV) -> filterOperators(root));
        categoryFilter.setOnAction(e -> filterOperators(root));
        statusFilter.setOnAction(e -> filterOperators(root));

        resetBtn.setOnAction(e -> {
            searchField.clear();
            categoryFilter.setValue("All Specializations");
            statusFilter.setValue("All Status");
            filterOperators(root);
        });

        // ================= MAIN CONTAINER =================
        VBox mainContainer = new VBox(20, feedbackBanner, summaryCards, filterBar, operatorsContainer);
        mainContainer.setPadding(new Insets(20, 30, 35, 30));
        mainContainer.setMaxWidth(Double.MAX_VALUE);
        mainContainer.setStyle("-fx-background-color: transparent;");

        // Initial render & sync from database
        syncOperatorsFromFirestore(root);

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static VBox createStatCardWithLabel(String label, Label valNode, String subText, String valColor) {
        Text lbl = new Text(label);
        lbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        valNode.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + valColor + ";");

        Text sub = new Text(subText);
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #5C6B5F;");

        VBox card = new VBox(4, lbl, valNode, sub);
        card.setPadding(new Insets(14, 18, 14, 18));
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
        );
        return card;
    }

    private static VBox createStatCard(String label, String value, String subText, String valColor) {
        Text lbl = new Text(label);
        lbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label valNode = new Label(value);
        valNode.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + valColor + ";");

        Text sub = new Text(subText);
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #5C6B5F;");

        VBox card = new VBox(4, lbl, valNode, sub);
        card.setPadding(new Insets(14, 18, 14, 18));
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
        );
        return card;
    }

    private static void filterOperators(StackPane root) {
        String query = searchField.getText() != null ? searchField.getText().trim().toLowerCase() : "";
        String cat = categoryFilter.getValue();
        String st = statusFilter.getValue();
        String currentFarmerTown = FarmerProfileStore.town.toLowerCase();

        List<OperatorItem> filtered = new ArrayList<>();
        for (OperatorItem op : operatorsList) {
            boolean matchesQuery = query.isEmpty() ||
                    op.name.toLowerCase().contains(query) ||
                    op.specialty.toLowerCase().contains(query) ||
                    op.locationDisplay.toLowerCase().contains(query) ||
                    op.category.toLowerCase().contains(query) ||
                    op.bio.toLowerCase().contains(query);

            boolean matchesCat = "All Specializations".equals(cat) || op.category.equalsIgnoreCase(cat);

            boolean matchesStatus = "All Status".equals(st) ||
                    ("Available to Hire".equals(st) && "AVAILABLE".equalsIgnoreCase(op.status)) ||
                    ("On Assignment".equals(st) && "ON ASSIGNMENT".equalsIgnoreCase(op.status));

            boolean matchesLocation = op.town.equalsIgnoreCase(FarmerProfileStore.town) ||
                    op.locationDisplay.toLowerCase().contains(currentFarmerTown) ||
                    currentFarmerTown.isEmpty();

            if (matchesQuery && matchesCat && matchesStatus && matchesLocation) {
                filtered.add(op);
            }
        }

        if (filtered.isEmpty() && !operatorsList.isEmpty()) {
            for (OperatorItem op : operatorsList) {
                boolean matchesQuery = query.isEmpty() ||
                        op.name.toLowerCase().contains(query) ||
                        op.specialty.toLowerCase().contains(query);
                boolean matchesCat = "All Specializations".equals(cat) || op.category.equalsIgnoreCase(cat);
                boolean matchesStatus = "All Status".equals(st) ||
                        ("Available to Hire".equals(st) && "AVAILABLE".equalsIgnoreCase(op.status)) ||
                        ("On Assignment".equals(st) && "ON ASSIGNMENT".equalsIgnoreCase(op.status));
                if (matchesQuery && matchesCat && matchesStatus) {
                    filtered.add(op);
                }
            }
        }

        renderOperatorsHorizontalList(filtered, root);
    }

    private static void renderOperatorsHorizontalList(List<OperatorItem> list, StackPane root) {
        operatorsContainer.getChildren().clear();

        if (list.isEmpty()) {
            VBox emptyBox = new VBox(10);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(40));
            emptyBox.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                    "-fx-background-radius: 14px;" +
                    "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                    "-fx-border-width: 1.2px;" +
                    "-fx-border-radius: 14px;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
            );

            Text icon = new Text("🚜");
            icon.setStyle("-fx-font-size: 38px;");

            Text title = new Text(operatorsList.isEmpty() ? "No Registered Operators Available" : "No Operators Match Your Filter");
            title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text sub = new Text(operatorsList.isEmpty() ?
                    "Machine operators registered on the platform will automatically appear here once they join." :
                    "Try selecting 'All Specializations' or resetting filters to see verified machinery operators.");
            sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");

            emptyBox.getChildren().addAll(icon, title, sub);
            operatorsContainer.getChildren().add(emptyBox);
            return;
        }

        for (OperatorItem op : list) {
            HBox card = createHorizontalOperatorCard(op, root);
            operatorsContainer.getChildren().add(card);
        }
    }

    // ============================================================
    // HORIZONTAL OPERATOR CARD
    // ============================================================
    private static HBox createHorizontalOperatorCard(OperatorItem op, StackPane root) {
        HBox card = new HBox(16);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
        );

        // 1. Avatar & Status Badge
        Text avatarIcon = new Text("👨‍🌾");
        if (op.category.equalsIgnoreCase("Harvester")) avatarIcon.setText("🌾");
        if (op.category.equalsIgnoreCase("Drone")) avatarIcon.setText("🚁");
        if (op.category.equalsIgnoreCase("Tractor")) avatarIcon.setText("🚜");
        avatarIcon.setStyle("-fx-font-size: 32px;");

        StackPane avatarBox = new StackPane(avatarIcon);
        avatarBox.setPrefSize(56, 56);
        avatarBox.setMinSize(56, 56);
        avatarBox.setMaxSize(56, 56);
        avatarBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 12px; -fx-border-color: rgba(45, 106, 79, 0.2); -fx-border-radius: 12px;");

        // 2. Middle Information Box
        Text nameText = new Text(op.name);
        nameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label verifiedBadge = new Label("✓ Verified Operator");
        verifiedBadge.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 2px 8px; -fx-background-radius: 10px;");

        HBox nameRow = new HBox(8, nameText, verifiedBadge);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        Text specialtyText = new Text(op.specialty);
        specialtyText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: 600; -fx-fill: #2D6A4F;");

        Text ratingText = new Text("⭐ " + op.rating);
        ratingText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #B45309;");

        Text expText = new Text("💼 " + op.experience);
        expText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        Text locText = new Text("📍 " + op.locationDisplay);
        locText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        HBox metaRow = new HBox(14, ratingText, expText, locText);
        metaRow.setAlignment(Pos.CENTER_LEFT);

        // Skills FlowPane
        FlowPane skillsPane = new FlowPane(6, 6);
        for (String sk : op.skills) {
            Label skLbl = new Label(sk);
            skLbl.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2D6A4F; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: 500; -fx-padding: 3px 8px; -fx-background-radius: 8px;");
            skillsPane.getChildren().add(skLbl);
        }

        VBox centerBox = new VBox(4, nameRow, specialtyText, metaRow, skillsPane);
        HBox.setHgrow(centerBox, Priority.ALWAYS);

        // 3. Right Action Box: Rate, Status, Info & Request Button
        Label statusPill = new Label("AVAILABLE".equalsIgnoreCase(op.status) ? "🟢 Available to Hire" : "🟠 On Assignment");
        statusPill.setStyle("AVAILABLE".equalsIgnoreCase(op.status) ?
                "-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-padding: 4px 10px; -fx-background-radius: 12px;" :
                "-fx-background-color: #FEF3C7; -fx-text-fill: #B45309; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-padding: 4px 10px; -fx-background-radius: 12px;");

        Text rateText = new Text(op.rate);
        rateText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Button infoBtn = new Button("ℹ️  View Info");
        infoBtn.setPrefHeight(36);
        infoBtn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2D6A4F; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6px 14px; -fx-background-radius: 8px; -fx-cursor: hand; -fx-border-color: rgba(45, 106, 79, 0.25); -fx-border-radius: 8px;");
        infoBtn.setOnAction(e -> showOperatorInfoModal(op, root));

        Button requestBtn = new Button(op.requestApproved ? "📞 Contact: " + op.phone : (op.requestSent ? "⏳ Request Sent" : "📩  Send Request"));
        requestBtn.setPrefHeight(36);
        requestBtn.setStyle(op.requestApproved ?
                "-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand;" :
                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.25), 6, 0, 0, 2);");

        requestBtn.setOnAction(e -> {
            if (op.requestApproved) {
                showOperatorInfoModal(op, root);
            } else if (op.requestSent) {
                op.requestApproved = true;
                requestBtn.setText("📞 Contact: " + op.phone);
                requestBtn.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand;");
                showFeedback("✓ Operator " + op.name + " approved your request! You can now call directly at " + op.phone);
            } else {
                op.requestSent = true;
                requestBtn.setText("⏳ Request Sent (Click to approve)");
                requestBtn.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #B45309; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand;");
                showFeedback("✓ Work request sent to " + op.name + "! Once approved, their direct contact will unlock.");
            }
        });

        HBox btnRow = new HBox(8, infoBtn, requestBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        VBox rightBox = new VBox(6, statusPill, rateText, btnRow);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        card.getChildren().addAll(avatarBox, centerBox, rightBox);

        // Hover Effect
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 14px; -fx-border-color: #2D6A4F; -fx-border-width: 1.5px; -fx-border-radius: 14px; -fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.15), 10, 0, 0, 3);");
            card.setTranslateY(-1.5);
        });

        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 14px; -fx-border-color: rgba(45, 106, 79, 0.25); -fx-border-width: 1.2px; -fx-border-radius: 14px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);");
            card.setTranslateY(0);
        });

        return card;
    }

    // ============================================================
    // IN-APP OPERATOR INFO MODAL (CLEAN & MODERN OVERLAY)
    // ============================================================
    private static void showOperatorInfoModal(OperatorItem op, StackPane root) {
        if (root == null) return;

        VBox modal = new VBox(14);
        modal.setPadding(new Insets(20, 24, 20, 24));
        modal.setPrefWidth(480);
        modal.setMaxWidth(480);
        modal.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 18px;" +
                "-fx-border-color: #D1E7DD;" +
                "-fx-border-radius: 18px;" +
                "-fx-border-width: 1.5px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.20), 24, 0, 0, 8);"
        );

        // Header with Avatar, Name, Specialization, and Close Button
        Text avatar = new Text("👨‍🌾");
        if (op.category.equalsIgnoreCase("Harvester")) avatar.setText("🌾");
        if (op.category.equalsIgnoreCase("Drone")) avatar.setText("🚁");
        if (op.category.equalsIgnoreCase("Tractor")) avatar.setText("🚜");
        avatar.setStyle("-fx-font-size: 22px;");

        StackPane avatarBox = new StackPane(avatar);
        avatarBox.setPrefSize(44, 44);
        avatarBox.setMinSize(44, 44);
        avatarBox.setMaxSize(44, 44);
        avatarBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 12px; -fx-border-color: rgba(45, 106, 79, 0.2); -fx-border-radius: 12px;");

        Text nameT = new Text(op.name);
        nameT.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label verifiedBadge = new Label("✓ Verified Operator");
        verifiedBadge.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 2px 7px; -fx-background-radius: 8px;");

        HBox nameRow = new HBox(8, nameT, verifiedBadge);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        Text specT = new Text(op.specialty);
        specT.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: 600; -fx-fill: #2D6A4F;");

        VBox titleBox = new VBox(2, nameRow, specT);
        HBox titleGroup = new HBox(10, avatarBox, titleBox);
        titleGroup.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(titleGroup, Priority.ALWAYS);

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.45);");

        Button close = new Button("✕");
        close.setPrefSize(30, 30);
        close.setMinSize(30, 30);
        close.setMaxSize(30, 30);
        close.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 15px; -fx-text-fill: #6B7280; -fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        close.setOnMouseEntered(e -> close.setStyle("-fx-background-color: #FEE2E2; -fx-background-radius: 15px; -fx-text-fill: #DC2626; -fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        close.setOnMouseExited(e -> close.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 15px; -fx-text-fill: #6B7280; -fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"));
        close.setOnAction(e -> root.getChildren().remove(overlay));

        HBox topBar = new HBox(titleGroup, close);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 12, 0));
        topBar.setStyle("-fx-border-color: #E2EBE5; -fx-border-width: 0 0 1px 0;");

        // Bio Box
        Text bioTitle = new Text("📝 Operator's Statement & Experience Bio:");
        bioTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text bioText = new Text(op.bio);
        bioText.setWrappingWidth(420);
        bioText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #374151; -fx-line-spacing: 3px;");

        VBox bioBox = new VBox(4, bioTitle, bioText);
        bioBox.setPadding(new Insets(12, 14, 12, 14));
        bioBox.setStyle("-fx-background-color: #F8FAF8; -fx-background-radius: 10px; -fx-border-color: #E2EBE5; -fx-border-radius: 10px; -fx-border-width: 1px;");

        // Skills Chips
        FlowPane skillsPane = new FlowPane(6, 6);
        for (String sk : op.skills) {
            Label skLbl = new Label(sk);
            skLbl.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2D6A4F; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: 500; -fx-padding: 3px 8px; -fx-background-radius: 8px;");
            skillsPane.getChildren().add(skLbl);
        }

        // Details grid
        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(16);
        detailsGrid.setVgap(8);
        detailsGrid.add(createModalRow("📍 Location:", op.locationDisplay), 0, 0);
        detailsGrid.add(createModalRow("💰 Daily Wage:", op.rate), 1, 0);
        detailsGrid.add(createModalRow("⭐ Rating:", op.rating + " / 5.0 (" + op.completedJobs + " jobs)"), 0, 1);
        detailsGrid.add(createModalRow("💼 Experience:", op.experience), 1, 1);

        // Action Button
        Button actionBtn = new Button(op.requestApproved ? "📞 Call Operator: " + op.phone : (op.requestSent ? "⏳ Request Pending (Click to Unlock Contact)" : "📩  Send Work Request"));
        actionBtn.setPrefHeight(40);
        actionBtn.setMaxWidth(Double.MAX_VALUE);
        actionBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10px;" +
                "-fx-cursor: hand;"
        );
        actionBtn.setOnAction(e -> {
            if (op.requestApproved) {
                root.getChildren().remove(overlay);
                showFeedback("Calling " + op.name + " at " + op.phone + "...");
            } else {
                op.requestSent = true;
                op.requestApproved = true;
                root.getChildren().remove(overlay);
                showFeedback("✓ Request sent & approved! You can now contact " + op.name + " directly at " + op.phone);
                filterOperators(root);
            }
        });

        modal.getChildren().addAll(topBar, bioBox, skillsPane, detailsGrid, actionBtn);
        overlay.getChildren().add(modal);
        overlay.setOnMouseClicked(e -> {
            if (e.getTarget() == overlay) {
                root.getChildren().remove(overlay);
            }
        });

        root.getChildren().add(overlay);
    }

    private static HBox createModalRow(String label, String value) {
        Label l = new Label(label);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
        l.setPrefWidth(95);

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        HBox row = new HBox(6, l, v);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private static void styleFilterCombo(ComboBox<String> combo) {
        combo.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-border-color: rgba(45, 106, 79, 0.3);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 12px;" +
                "-fx-background-radius: 12px;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;"
        );
    }

    private static void showFeedback(String msg) {
        if (feedbackBanner != null) {
            feedbackBanner.setText(msg);
            feedbackBanner.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-padding: 10px 16px; -fx-background-radius: 10px; -fx-border-color: #86EFAC; -fx-border-radius: 10px;");
            feedbackBanner.setVisible(true);
            feedbackBanner.setManaged(true);
        }
    }
}
