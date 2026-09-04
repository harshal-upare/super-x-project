package com.desgin.view.farmer.pratik;

import java.util.ArrayList;
import java.util.List;

import com.desgin.dao.RentalRequestDAO;
import com.desgin.model.RentalRequestModel;
import com.desgin.view.farmer.Swapnil.FarmerProfileStore;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class SearchOperator {

    public static class OperatorItem {
        public String id;
        public String email;
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
        public String profilePic;
        public String licenseImage;
        public boolean requestApproved;
        public boolean requestSent;

        public OperatorItem(String id, String email, String name, String phone, String specialty, String category,
                            String experience, String town, String locationDisplay, String rate, double rating,
                            int completedJobs, String status, String bio, List<String> skills, String profilePic, String licenseImage) {
            this.id = id;
            this.email = email != null ? email : id;
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
            this.profilePic = profilePic;
            this.licenseImage = licenseImage;
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

                        Boolean isAvailable = doc.getBoolean("available");
                        String docStatus = doc.getString("status");
                        if (Boolean.FALSE.equals(isAvailable)) {
                            continue;
                        }
                        if (docStatus != null) {
                            String st = docStatus.trim().toUpperCase();
                            if (st.contains("BUSY") || st.contains("UNAVAILABLE") || st.contains("NOT AVAILABLE") || st.contains("OFFLINE") || st.contains("OFF_DUTY") || st.contains("OFF-DUTY")) {
                                continue;
                            }
                        }

                        String email = doc.getId();
                        if (doc.contains("email") && doc.getString("email") != null && !doc.getString("email").trim().isEmpty()) {
                            email = doc.getString("email").trim();
                        }
                        String phone = doc.getString("num");
                        if (phone == null || phone.trim().isEmpty()) phone = doc.getString("phone");
                        if (phone == null || phone.trim().isEmpty()) phone = "Contact via Platform Request";

                        String town = doc.getString("town");
                        String district = doc.getString("district");
                        String loc = (town != null && !town.isEmpty()) ? (town + (district != null ? ", " + district : "")) : "Pune, Maharashtra";

                        String drivingExp = doc.getString("drivingExperience");
                        if (drivingExp == null || drivingExp.trim().isEmpty()) drivingExp = "3-5 Years (Certified Operator)";

                        String equipProf = doc.getString("equipmentProfession");
                        if (equipProf == null || equipProf.trim().isEmpty()) equipProf = "Tractors & Heavy Tillage";

                        String profilePic = doc.getString("profilePic");
                        if (profilePic == null || profilePic.trim().isEmpty()) profilePic = doc.getString("photoUrl");
                        if (profilePic == null || profilePic.trim().isEmpty()) profilePic = doc.getString("photo");
                        if (profilePic == null || profilePic.trim().isEmpty()) profilePic = doc.getString("profileImage");
                        if (profilePic == null || profilePic.trim().isEmpty()) profilePic = doc.getString("image");
                        String licenseImg = doc.getString("licenseImage");
                        if (licenseImg == null || licenseImg.trim().isEmpty()) licenseImg = doc.getString("license");

                        String cat = "Tractor";
                        if (equipProf.toLowerCase().contains("harvester")) cat = "Harvester";
                        else if (equipProf.toLowerCase().contains("drone") || equipProf.toLowerCase().contains("spray")) cat = "Drone";
                        else if (equipProf.toLowerCase().contains("rotavator") || equipProf.toLowerCase().contains("seed")) cat = "Rotavator";
                        else if (equipProf.toLowerCase().contains("multi")) cat = "Tractor";

                        List<String> skills = new ArrayList<>();
                        if (cat.equals("Harvester")) {
                            skills.addAll(List.of("Combined Harvesting", "Grain Threshing", "Straw Management", "Field Safety"));
                        } else if (cat.equals("Drone")) {
                            skills.addAll(List.of("Precision Foliar Spraying", "Crop Health Mapping", "UAV Flight Safety", "Agri Tech"));
                        } else if (cat.equals("Rotavator")) {
                            skills.addAll(List.of("Rotary Tillage", "Seedbed Preparation", "Implement Calibration", "Fuel Efficiency"));
                        } else {
                            skills.addAll(List.of("Deep Plowing", "Disk Harrowing", "Cultivator Operation", "Precision Tillage"));
                        }

                        list.add(new OperatorItem(
                                doc.getId(),
                                email,
                                name,
                                phone,
                                "🚜 " + equipProf,
                                cat,
                                drivingExp,
                                town != null ? town : "Pune",
                                loc,
                                "₹600 / day",
                                4.9,
                                12,
                                "AVAILABLE",
                                "Verified and licensed heavy machinery operator ready for field assignment, land preparation, and seasonal harvesting.",
                                skills,
                                profilePic,
                                licenseImg
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

    private static HBox feedbackBannerBox;

    public static ScrollPane getSearchOperatorSection(StackPane root) {

        // Feedback Banner
        feedbackBannerBox = new HBox(12);
        feedbackBannerBox.setAlignment(Pos.CENTER_LEFT);
        feedbackBannerBox.setStyle("-fx-background-color: #DCFCE7; -fx-padding: 10px 16px; -fx-background-radius: 10px; -fx-border-color: #86EFAC; -fx-border-radius: 10px;");
        feedbackBannerBox.setVisible(false);
        feedbackBannerBox.setManaged(false);

        // ================= 3 KPI SUMMARY CARDS =================
        updateKpis();

        VBox c1 = createStatCardWithLabel("👷 Total Registered Operators", totalOpsLabel, "Verified machinery specialists in network", "#1B4332");
        VBox c2 = createStatCardWithLabel("🟢 Available to Dispatch", availOpsLabel, "Ready for immediate field hire", "#2D6A4F");
        VBox c3 = createStatCardWithLabel("🟠 Active in Field", assignedOpsLabel, "Currently performing farm shifts", "#B45309");

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

        // ================= OPERATORS LIST CONTAINER =================
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
        VBox mainContainer = new VBox(20, feedbackBannerBox, summaryCards, filterBar, operatorsContainer);
        mainContainer.setPadding(new Insets(20, 30, 35, 30));
        mainContainer.setMaxWidth(Double.MAX_VALUE);
        mainContainer.setStyle("-fx-background-color: transparent;");

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

    private static void filterOperators(StackPane root) {
        String query = searchField != null && searchField.getText() != null ? searchField.getText().trim().toLowerCase() : "";
        String cat = categoryFilter != null ? categoryFilter.getValue() : "All Specializations";
        String st = statusFilter != null ? statusFilter.getValue() : "All Status";

        List<OperatorItem> filtered = new ArrayList<>();
        for (OperatorItem op : operatorsList) {
            boolean matchesQuery = query.isEmpty() ||
                    op.name.toLowerCase().contains(query) ||
                    op.specialty.toLowerCase().contains(query) ||
                    op.locationDisplay.toLowerCase().contains(query) ||
                    op.category.toLowerCase().contains(query) ||
                    op.experience.toLowerCase().contains(query);

            boolean matchesCat = "All Specializations".equals(cat) || op.category.equalsIgnoreCase(cat);

            boolean matchesStatus = "All Status".equals(st) ||
                    ("Available to Hire".equals(st) && "AVAILABLE".equalsIgnoreCase(op.status)) ||
                    ("On Assignment".equals(st) && "ON ASSIGNMENT".equalsIgnoreCase(op.status));

            if (matchesQuery && matchesCat && matchesStatus) {
                filtered.add(op);
            }
        }

        renderOperatorsHorizontalList(filtered, root);
    }

    private static void renderOperatorsHorizontalList(List<OperatorItem> list, StackPane root) {
        if (operatorsContainer == null) return;
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

            Text title = new Text(operatorsList.isEmpty() ? "Loading Registered Operators..." : "No Operators Match Your Filter");
            title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text sub = new Text(operatorsList.isEmpty() ?
                    "Connecting to Firestore to fetch registered machinery operators in your region." :
                    "Try selecting 'All Specializations' or resetting filters to see available operators.");
            sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");

            Button resetBtnInEmpty = new Button("Reset Search & Filters ➔");
            resetBtnInEmpty.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 7 14;");
            resetBtnInEmpty.setOnAction(e -> {
                if (searchField != null) searchField.clear();
                if (categoryFilter != null) categoryFilter.setValue("All Specializations");
                if (statusFilter != null) statusFilter.setValue("All Status");
                filterOperators(root);
            });

            emptyBox.getChildren().addAll(icon, title, sub, resetBtnInEmpty);
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

        // 1. Avatar with Cloudinary Image or Emoji
        StackPane avatarBox;
        String photoUrl = op.profilePic != null ? op.profilePic.trim() : "";
        if (photoUrl.isEmpty() && op.email != null && op.email.equalsIgnoreCase(com.desgin.view.operator.OperatorProfileStore.email)) {
            photoUrl = com.desgin.view.operator.OperatorProfileStore.profilePic != null ? com.desgin.view.operator.OperatorProfileStore.profilePic.trim() : "";
        }

        if (!photoUrl.isEmpty()) {
            ImageView iv = new ImageView();
            iv.setFitWidth(56);
            iv.setFitHeight(56);
            iv.setPreserveRatio(false);
            iv.setSmooth(true);
            Circle clip = new Circle(28, 28, 28);
            iv.setClip(clip);

            Text fallbackText = new Text("👨‍🌾");
            if (op.category.equalsIgnoreCase("Harvester")) fallbackText.setText("🌾");
            else if (op.category.equalsIgnoreCase("Drone")) fallbackText.setText("🚁");
            else if (op.category.equalsIgnoreCase("Tractor")) fallbackText.setText("🚜");
            fallbackText.setStyle("-fx-font-size: 28px;");

            avatarBox = new StackPane(fallbackText, iv);
            avatarBox.setPrefSize(56, 56);
            avatarBox.setMinSize(56, 56);
            avatarBox.setMaxSize(56, 56);

            try {
                Image img = new Image(photoUrl, true);
                img.errorProperty().addListener((obs, oldV, err) -> {
                    if (err) {
                        iv.setVisible(false);
                        fallbackText.setVisible(true);
                    }
                });
                img.progressProperty().addListener((obs, oldV, progress) -> {
                    if (progress.doubleValue() >= 1.0 && !img.isError()) {
                        iv.setVisible(true);
                        fallbackText.setVisible(false);
                    }
                });
                iv.setImage(img);
            } catch (Exception ex) {
                iv.setVisible(false);
                fallbackText.setVisible(true);
            }
        } else {
            Text avatarIcon = new Text("👨‍🌾");
            if (op.category.equalsIgnoreCase("Harvester")) avatarIcon.setText("🌾");
            else if (op.category.equalsIgnoreCase("Drone")) avatarIcon.setText("🚁");
            else if (op.category.equalsIgnoreCase("Tractor")) avatarIcon.setText("🚜");
            avatarIcon.setStyle("-fx-font-size: 28px;");

            avatarBox = new StackPane(avatarIcon);
            avatarBox.setPrefSize(56, 56);
            avatarBox.setMinSize(56, 56);
            avatarBox.setMaxSize(56, 56);
        }
        avatarBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 28px; -fx-border-color: rgba(45, 106, 79, 0.3); -fx-border-radius: 28px; -fx-border-width: 1.5px;");

        // 2. Middle Information Box
        Text nameText = new Text(op.name);
        nameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label verifiedBadge = new Label("✓ Verified Operator");
        verifiedBadge.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 2px 8px; -fx-background-radius: 10px;");

        HBox nameRow = new HBox(8, nameText, verifiedBadge);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        if (op.licenseImage != null && !op.licenseImage.trim().isEmpty()) {
            Label dlBadge = new Label("📄 DL Verified");
            dlBadge.setStyle("-fx-background-color: #E0F2FE; -fx-text-fill: #0284C7; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 2px 7px; -fx-background-radius: 8px;");
            nameRow.getChildren().add(dlBadge);
        }

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

        // 3. Right Action Box: Rate, Status, Info & Hire Button
        Label statusPill = new Label("AVAILABLE".equalsIgnoreCase(op.status) ? "🟢 Available to Hire" : "🟠 On Assignment");
        statusPill.setStyle("AVAILABLE".equalsIgnoreCase(op.status) ?
                "-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-padding: 4px 10px; -fx-background-radius: 12px;" :
                "-fx-background-color: #FEF3C7; -fx-text-fill: #B45309; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-padding: 4px 10px; -fx-background-radius: 12px;");

        Text rateText = new Text(op.rate);
        rateText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Button hireBtn = new Button(op.requestApproved ? "✓ Confirmed (Active)" : (op.requestSent ? "⏳ Request Sent" : "📩  Send Hire Request"));
        hireBtn.setPrefHeight(36);
        hireBtn.setStyle(op.requestApproved ?
                "-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand;" :
                (op.requestSent ?
                        "-fx-background-color: #FEF3C7; -fx-text-fill: #B45309; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand;" :
                        "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.25), 6, 0, 0, 2);"));

        hireBtn.setOnAction(e -> showHireRequestModal(op, root, hireBtn));

        HBox btnRow = new HBox(8, hireBtn);
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
    // SEND HIRE REQUEST MODAL
    // ============================================================
    private static void showHireRequestModal(OperatorItem op, StackPane root, Button cardHireBtn) {
        if (root == null) return;

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.50);");

        VBox modal = new VBox(14);
        modal.setPadding(new Insets(20, 24, 20, 24));
        modal.setPrefWidth(480);
        modal.setMaxWidth(480);
        modal.setMaxHeight(Region.USE_PREF_SIZE);
        modal.setMinHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(modal, Pos.CENTER);
        modal.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 18px;" +
                "-fx-border-color: #D1E7DD;" +
                "-fx-border-radius: 18px;" +
                "-fx-border-width: 1.5px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 24, 0, 0, 8);"
        );

        // Modal Header
        Text title = new Text("📩 Send Work Request to Operator");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Operator: " + op.name + " (" + op.specialty + ")");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        VBox header = new VBox(3, title, sub);

        // Inputs
        ComboBox<String> taskCombo = new ComboBox<>();
        taskCombo.setEditable(true);
        taskCombo.getItems().addAll(
                "Tractor Deep Tillage & Plowing",
                "Harvester & Threshing Operation",
                "Rotavator Soil Preparation",
                "High-Capacity Crop Spraying / Drone",
                "Laser Land Leveling & Grading",
                "Cultivator / Disc Harrow Operation",
                "Seed Drill / Sowing Operation",
                "Sugarcane Harvester Shift",
                "Paddy Transplanter Operation",
                "General Field Machinery Operation"
        );
        taskCombo.setValue("Tractor Deep Tillage & Plowing");
        taskCombo.setMaxWidth(Double.MAX_VALUE);
        taskCombo.setPrefHeight(38);
        if (taskCombo.getEditor() != null) {
            taskCombo.getEditor().setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-text-fill: #1B4332; -fx-background-color: transparent;");
        }
        styleFilterCombo(taskCombo);

        String defaultLoc = (FarmerProfileStore.town != null && !FarmerProfileStore.town.isEmpty() ? FarmerProfileStore.town : "Pune") +
                (FarmerProfileStore.district != null ? ", " + FarmerProfileStore.district : "") + " (Plot / Gat No. 112)";
        TextField locField = new TextField(defaultLoc);
        locField.setPrefHeight(38);
        locField.setStyle("-fx-background-color: #F8FAF8; -fx-border-color: #C2E0CE; -fx-border-radius: 8px; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-padding: 0 10;");

        ComboBox<String> durationCombo = new ComboBox<>();
        durationCombo.getItems().addAll("1 Day (₹600)", "2 Days (₹1,200)", "3 Days (₹1,800)", "5 Days (₹3,000)", "7 Days (₹4,200)");
        durationCombo.setValue("2 Days (₹1,200)");
        durationCombo.setMaxWidth(Double.MAX_VALUE);
        durationCombo.setPrefHeight(38);
        styleFilterCombo(durationCombo);

        TextField startDateField = new TextField("Tomorrow, 08:00 AM");
        startDateField.setPrefHeight(38);
        startDateField.setStyle("-fx-background-color: #F8FAF8; -fx-border-color: #C2E0CE; -fx-border-radius: 8px; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-padding: 0 10;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(12);
        formGrid.setVgap(10);
        formGrid.add(createFieldGroup("Operation / Machinery Task", taskCombo), 0, 0, 2, 1);
        formGrid.add(createFieldGroup("Farm Plot Location", locField), 0, 1, 2, 1);
        formGrid.add(createFieldGroup("Start Date & Time", startDateField), 0, 2);
        formGrid.add(createFieldGroup("Shift Duration & Offered Pay", durationCombo), 1, 2);

        // Farmer Info Banner
        Label farmerInfoLabel = new Label("👨‍🌾 Farmer Requesting: " + FarmerProfileStore.name + " (📞 " + FarmerProfileStore.phone + ")");
        farmerInfoLabel.setStyle("-fx-background-color: #F0FDF4; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-padding: 8 12; -fx-background-radius: 8; -fx-border-color: #BBF7D0; -fx-border-radius: 8;");
        farmerInfoLabel.setMaxWidth(Double.MAX_VALUE);

        // Action Buttons
        Button confirmBtn = new Button("🚀 Confirm & Send Request to Operator");
        confirmBtn.setStyle("-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 10px; -fx-padding: 9 20; -fx-cursor: hand;");
        confirmBtn.setMaxWidth(Double.MAX_VALUE);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #F3F4F6; -fx-text-fill: #4B5563; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-background-radius: 10px; -fx-padding: 8 16; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> root.getChildren().remove(overlay));

        confirmBtn.setOnAction(e -> {
            String selectedTask = (taskCombo.getEditor() != null && taskCombo.getEditor().getText() != null && !taskCombo.getEditor().getText().trim().isEmpty())
                    ? taskCombo.getEditor().getText().trim()
                    : (taskCombo.getValue() != null ? taskCombo.getValue() : "General Machinery Operation");
            String location = locField.getText().trim();
            String durationStr = durationCombo.getValue();
            String startDate = startDateField.getText().trim();

            int days = 1;
            int wage = 600;
            if (durationStr.contains("2 Days")) { days = 2; wage = 1200; }
            else if (durationStr.contains("3 Days")) { days = 3; wage = 1800; }
            else if (durationStr.contains("5 Days")) { days = 5; wage = 3000; }
            else if (durationStr.contains("7 Days")) { days = 7; wage = 4200; }

            final int finalWage = wage;
            final int finalDays = days;

            RentalRequestModel req = new RentalRequestModel();
            req.setRequestId("OP-REQ-" + (System.currentTimeMillis() % 100000));
            req.setFarmerEmail(FarmerProfileStore.email != null ? FarmerProfileStore.email : "farmer@farmmail.com");
            req.setFarmerName(FarmerProfileStore.name != null ? FarmerProfileStore.name : "Registered Farmer");
            req.setFarmerPhone(FarmerProfileStore.phone != null ? FarmerProfileStore.phone : "+91 98000 00000");
            req.setFarmerLocation(location);
            req.setOperatorId(op.email != null && !op.email.isEmpty() ? op.email.toLowerCase().trim() : op.id.toLowerCase().trim());
            req.setOperatorName(op.name);
            req.setOperatorPhone(op.phone);
            req.setMachineryName(selectedTask);
            req.setOperatorRequired(true);
            req.setOperatorStatus("PENDING");
            req.setStatus("PENDING");
            req.setStartDate(startDate);
            req.setEndDate("After " + finalDays + " Days");
            req.setDays(finalDays);
            req.setDailyRate(finalWage / Math.max(1, finalDays));
            req.setOperatorAmount(finalWage);
            req.setTotalAmount(finalWage);
            req.setPaymentStatus("PENDING");

            new Thread(() -> {
                try {
                    new RentalRequestDAO().createRequest(req);
                    String opId = (op.email != null && !op.email.trim().isEmpty()) ? op.email.trim().toLowerCase() : "operator@farmequip.com";
                    com.desgin.model.NotificationModel notif = new com.desgin.model.NotificationModel(
                        "NOTIF_" + System.currentTimeMillis(),
                        opId,
                        "🚜 New Work Request: " + selectedTask,
                        "Farmer " + req.getFarmerName() + " sent you a request for " + selectedTask + " (" + finalDays + " Days) at " + location + ". Total wage: ₹" + finalWage,
                        "REQUEST",
                        req.getRequestId()
                    );
                    new com.desgin.dao.NotificationDAO().sendNotification(notif);
                } catch (Exception ex) {
                    System.err.println("Notice: Could not save operator hire request: " + ex.getMessage());
                }
            }).start();

            op.requestSent = true;
            if (cardHireBtn != null) {
                cardHireBtn.setText("⏳ Request Sent (Pending)");
                cardHireBtn.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #B45309; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand;");
            }

            root.getChildren().remove(overlay);
            showFeedback("✓ Work request sent to operator " + op.name + "! The operator can now review, accept, or reject the job in their Requests portal.");
        });

        HBox actions = new HBox(12, cancelBtn, confirmBtn);
        actions.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(confirmBtn, Priority.ALWAYS);

        modal.getChildren().addAll(header, formGrid, farmerInfoLabel, actions);
        overlay.getChildren().add(modal);
        overlay.setOnMouseClicked(e -> {
            if (e.getTarget() == overlay) root.getChildren().remove(overlay);
        });

        root.getChildren().add(overlay);
    }

    private static VBox createFieldGroup(String label, javafx.scene.Node input) {
        Label l = new Label(label);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
        VBox box = new VBox(3, l, input);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
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
        if (feedbackBannerBox != null) {
            feedbackBannerBox.getChildren().clear();
            Text t = new Text(msg);
            t.setStyle("-fx-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold;");
            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);
            Button gotoBookingsBtn = new Button("📅 View in My Bookings ➔");
            gotoBookingsBtn.setStyle("-fx-background-color: #15803D; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-cursor: hand; -fx-padding: 5 12;");
            gotoBookingsBtn.setOnAction(e -> com.desgin.view.farmer.LeftSideBar.navigateToBookings());
            feedbackBannerBox.getChildren().addAll(t, sp, gotoBookingsBtn);
            feedbackBannerBox.setVisible(true);
            feedbackBannerBox.setManaged(true);
        }
    }
}
