package com.desgin.view.provider;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import javafx.scene.text.Text;

public class MyEquipment {

    public static class FleetItem {
        public String name;
        public String category;
        public String icon;
        public int pricePerDay;
        public String specs;
        public String status; // "AVAILABLE", "RENTED OUT", "IN SERVICE"
        public String imagePath;
        public int totalRentals;
        public int lifetimeEarned;
        public boolean hasOperator;

        public FleetItem(String name, String category, String icon, int pricePerDay, String specs, String status, String imagePath, int totalRentals, int lifetimeEarned, boolean hasOperator) {
            this.name = name;
            this.category = category;
            this.icon = icon;
            this.pricePerDay = pricePerDay;
            this.specs = specs;
            this.status = status;
            this.imagePath = imagePath;
            this.totalRentals = totalRentals;
            this.lifetimeEarned = lifetimeEarned;
            this.hasOperator = hasOperator;
        }
    }

    private static List<FleetItem> fleetList = new ArrayList<>();
    private static FlowPane cardsGrid;
    private static StackPane currentRoot;
    private static String activeCategory = "All";
    private static String activeStatusFilter = "All";
    private static String searchQuery = "";

    static {
        initDefaultFleet();
    }

    private static void initDefaultFleet() {
        if (!fleetList.isEmpty()) return;
        fleetList.add(new FleetItem("Mahindra 575 DI Sarpanch", "Tractors", "🚜", 1200, "45 HP • 4-Cyl Diesel • Dual Clutch", "AVAILABLE", "file:farm/src/main/resources/assets/Images/tractor.png", 28, 67200, true));
        fleetList.add(new FleetItem("John Deere 5310 PowerTech", "Tractors", "🚜", 1500, "55 HP • Turbocharged • 4WD • AC Cabin", "RENTED OUT", "file:farm/src/main/resources/assets/Images/tractor.png", 42, 126000, true));
        fleetList.add(new FleetItem("Sonalika DI 745 III Sikander", "Tractors", "🚜", 1100, "50 HP • Heavy Duty • Low Fuel Consumption", "AVAILABLE", "file:farm/src/main/resources/assets/Images/tractor.png", 19, 41800, false));
        fleetList.add(new FleetItem("Kartar 4000 Multi-Crop Harvester", "Harvesters", "🌾", 3500, "76 HP • Paddy & Wheat Cutter • 14ft Width", "RENTED OUT", "file:farm/src/main/resources/assets/Images/tractor.png", 31, 217000, true));
        fleetList.add(new FleetItem("Preet 987 Self-Propelled Harvester", "Harvesters", "🌾", 3800, "101 HP • Heavy Straw Chopper • High Speed", "AVAILABLE", "file:farm/src/main/resources/assets/Images/tractor.png", 14, 106400, true));
        fleetList.add(new FleetItem("Shaktiman Semi-Champion 7ft", "Rotavators", "⚙", 800, "Multi-Speed Gearbox • Boron Steel Blades", "AVAILABLE", "file:farm/src/main/resources/assets/Images/tractor.png", 22, 35200, false));
        fleetList.add(new FleetItem("FieldKing 9-Tyne Spring Cultivator", "Cultivators", "🌱", 600, "Heavy Duty Frame • For Hard Soil Tillage", "AVAILABLE", "file:farm/src/main/resources/assets/Images/tractor.png", 18, 21600, false));
        fleetList.add(new FleetItem("Agri-Drone 16L Autonomous Sprayer", "Sprayers & Drones", "🚁", 1800, "16 Liters • Radar Obstacle Sensing • 25 Acre/Day", "IN SERVICE", "file:farm/src/main/resources/assets/Images/tractor.png", 15, 54000, true));
        fleetList.add(new FleetItem("National Zero-Till Seed Drill", "Seeders & Tillers", "🌾", 750, "9-Row Seed & Fertilizer Drill • Accurate Spacing", "AVAILABLE", "file:farm/src/main/resources/assets/Images/tractor.png", 12, 18000, false));
    }

    public static ScrollPane getFleetSection(StackPane root) {
        currentRoot = root;

        Text headerTitle = new Text("My Fleet & Machinery Management");
        headerTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text headerSubtitle = new Text("Manage your active machinery inventory, set rental tariffs, toggle live availability, and add new equipment.");
        headerSubtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #806A5B;");

        VBox titleBox = new VBox(4, headerTitle, headerSubtitle);

        Button addEquipmentBtn = new Button("➕  Add New Machinery");
        addEquipmentBtn.setPrefHeight(42);
        addEquipmentBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 20 0 20;");
        addEquipmentBtn.setOnAction(e -> showAddEquipmentModal(root));

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox topBar = new HBox(titleBox, topSpacer, addEquipmentBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Fleet Metrics Summary Mini Strip
        HBox metricStrip = createFleetMetricStrip();

        // Search and Filter Bar
        TextField searchInput = new TextField();
        searchInput.setPromptText("Search by machine name, HP, model or category...");
        searchInput.setPrefHeight(40);
        searchInput.setPrefWidth(380);
        searchInput.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D8C7B5; -fx-border-radius: 8; -fx-font-family: 'Poppins'; -fx-font-size: 13px;");
        searchInput.textProperty().addListener((obs, oldV, newV) -> {
            searchQuery = newV.toLowerCase().trim();
            renderFleetGrid();
        });

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Statuses", "AVAILABLE Only", "RENTED OUT", "IN SERVICE");
        statusFilter.setValue("All Statuses");
        statusFilter.setPrefHeight(40);
        statusFilter.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D8C7B5; -fx-border-radius: 8; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px;");
        statusFilter.setOnAction(e -> {
            activeStatusFilter = statusFilter.getValue();
            renderFleetGrid();
        });

        HBox searchFilterBox = new HBox(12, searchInput, statusFilter);
        searchFilterBox.setAlignment(Pos.CENTER_LEFT);

        // Category Filter Pills
        HBox categoryPills = createCategoryPills();

        // Cards Grid
        cardsGrid = new FlowPane();
        cardsGrid.setHgap(18);
        cardsGrid.setVgap(18);
        cardsGrid.setPrefWrapLength(1080);
        renderFleetGrid();

        VBox content = new VBox(20, topBar, metricStrip, searchFilterBox, categoryPills, cardsGrid);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static HBox createFleetMetricStrip() {
        long total = fleetList.size();
        long avail = fleetList.stream().filter(f -> "AVAILABLE".equals(f.status)).count();
        long rented = fleetList.stream().filter(f -> "RENTED OUT".equals(f.status)).count();
        long service = fleetList.stream().filter(f -> "IN SERVICE".equals(f.status)).count();
        int totalEarnings = fleetList.stream().mapToInt(f -> f.lifetimeEarned).sum();

        HBox strip = new HBox(15,
            createMetricCard("🚜 Total Fleet Units", total + " Machines", "#4A2C20"),
            createMetricCard("✔ Ready / Available", avail + " Units", "#2E7D32"),
            createMetricCard("⏱ Currently Rented", rented + " Units", "#E65100"),
            createMetricCard("🛠 In Maintenance", service + " Units", "#C62828"),
            createMetricCard("💰 Fleet Total Earned", "₹" + String.format("%,d", totalEarnings), "#5C4033")
        );
        strip.setAlignment(Pos.CENTER_LEFT);
        return strip;
    }

    private static VBox createMetricCard(String title, String value, String color) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #806A5B;");
        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        VBox b = new VBox(4, t, v);
        b.setPrefWidth(190);
        b.setPadding(new Insets(12, 16, 12, 16));
        b.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 10; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 10;");
        return b;
    }

    private static HBox createCategoryPills() {
        String[] cats = {"All", "Tractors", "Harvesters", "Rotavators", "Cultivators", "Sprayers & Drones", "Seeders & Tillers"};
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER_LEFT);

        for (String cat : cats) {
            Button pill = new Button(cat);
            pill.setPrefHeight(32);
            stylePill(pill, cat.equals(activeCategory));
            pill.setOnAction(e -> {
                activeCategory = cat;
                for (var node : bar.getChildren()) {
                    if (node instanceof Button btn) {
                        stylePill(btn, btn.getText().equals(activeCategory));
                    }
                }
                renderFleetGrid();
            });
            bar.getChildren().add(pill);
        }
        return bar;
    }

    private static void stylePill(Button btn, boolean active) {
        if (active) {
            btn.setStyle("-fx-background-color: #4A2C20; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 0 16 0 16; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: #F5EFE6; -fx-text-fill: #5C4033; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-background-radius: 20; -fx-border-color: #D8C7B5; -fx-border-radius: 20; -fx-padding: 0 16 0 16; -fx-cursor: hand;");
        }
    }

    private static void renderFleetGrid() {
        cardsGrid.getChildren().clear();

        for (FleetItem item : fleetList) {
            if (!activeCategory.equals("All") && !item.category.equalsIgnoreCase(activeCategory)) {
                continue;
            }

            if (activeStatusFilter.equals("AVAILABLE Only") && !"AVAILABLE".equals(item.status)) continue;
            if (activeStatusFilter.equals("RENTED OUT") && !"RENTED OUT".equals(item.status)) continue;
            if (activeStatusFilter.equals("IN SERVICE") && !"IN SERVICE".equals(item.status)) continue;

            if (!searchQuery.isEmpty()) {
                boolean matches = item.name.toLowerCase().contains(searchQuery)
                        || item.category.toLowerCase().contains(searchQuery)
                        || item.specs.toLowerCase().contains(searchQuery);
                if (!matches) continue;
            }

            cardsGrid.getChildren().add(createFleetCard(item));
        }

        if (cardsGrid.getChildren().isEmpty()) {
            VBox emptyBox = new VBox(10);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(40));
            Text emptyText = new Text("No machinery matches the selected filter.");
            emptyText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-fill: #806A5B;");
            cardsGrid.getChildren().add(emptyBox);
        }
    }

    private static VBox createFleetCard(FleetItem item) {
        Image img = null;
        try {
            img = new Image(item.imagePath);
        } catch (Exception ignored) {}

        ImageView iv = new ImageView();
        if (img != null && !img.isError()) {
            iv.setImage(img);
            iv.setFitWidth(200);
            iv.setFitHeight(95);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
        } else {
            iv.setFitWidth(200);
            iv.setFitHeight(95);
        }

        Text iconBadge = new Text(item.icon);
        iconBadge.setStyle("-fx-font-size: 26px;");

        StackPane imgBox = new StackPane(iv, iconBadge);
        StackPane.setAlignment(iconBadge, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(iconBadge, new Insets(0, 10, 6, 0));
        imgBox.setPrefHeight(105);
        imgBox.setStyle("-fx-background-color: #E4D3C2; -fx-background-radius: 10;");

        // Status Badge & Operator Indicator
        String stColor = "AVAILABLE".equals(item.status) ? "#2E7D32" : ("RENTED OUT".equals(item.status) ? "#E65100" : "#C62828");
        Label statusBadge = new Label("● " + item.status);
        statusBadge.setStyle("-fx-background-color: " + stColor + "; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");

        Label opBadge = new Label(item.hasOperator ? "👨‍🌾 Operator Incl." : "🚜 Machine Only");
        opBadge.setStyle("-fx-background-color: #EDE3D5; -fx-text-fill: #5C4033; -fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-padding: 3 6 3 6; -fx-background-radius: 4;");

        Region bSpacer = new Region();
        HBox.setHgrow(bSpacer, Priority.ALWAYS);
        HBox badgeRow = new HBox(statusBadge, bSpacer, opBadge);
        badgeRow.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text(item.name);
        title.setWrappingWidth(225);
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text specs = new Text(item.specs);
        specs.setWrappingWidth(225);
        specs.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #806A5B;");

        Text price = new Text("₹" + item.pricePerDay + " / day");
        price.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Text stats = new Text("Booked " + item.totalRentals + " times • Earned ₹" + String.format("%,d", item.lifetimeEarned));
        stats.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #806A5B;");

        // Action Buttons: Toggle Status + Edit Tariff
        Button toggleStatusBtn = new Button("Toggle Status 🔄");
        toggleStatusBtn.setStyle("-fx-background-color: #8B6F47; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        toggleStatusBtn.setPrefHeight(30);

        toggleStatusBtn.setOnAction(e -> {
            if ("AVAILABLE".equals(item.status)) item.status = "RENTED OUT";
            else if ("RENTED OUT".equals(item.status)) item.status = "IN SERVICE";
            else item.status = "AVAILABLE";
            renderFleetGrid();
        });

        Button editBtn = new Button("Edit Tariff ✏");
        editBtn.setStyle("-fx-background-color: #5C4033; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        editBtn.setPrefHeight(30);
        editBtn.setOnAction(e -> showEditTariffModal(item, currentRoot));

        HBox btnRow = new HBox(8, toggleStatusBtn, editBtn);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(8, imgBox, badgeRow, title, specs, price, stats, btnRow);
        card.setPrefWidth(250);
        card.setPadding(new Insets(14));
        card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #FFF9F0; -fx-background-radius: 12; -fx-border-color: #8B6F47; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(74,44,32,0.2), 10, 0.2, 0, 3);");
            card.setTranslateY(-2);
        });

        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");
            card.setTranslateY(0);
        });

        return card;
    }

    private static void showAddEquipmentModal(StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(15);
        modal.setPrefWidth(520);
        modal.setMaxWidth(520);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFDF9; -fx-background-radius: 16; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 25, 0.3, 0, 8);");

        Text title = new Text("Register New Machinery to Fleet");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);

        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Swaraj 855 FE Tractor");

        ComboBox<String> catCombo = new ComboBox<>();
        catCombo.getItems().addAll("Tractors", "Harvesters", "Rotavators", "Cultivators", "Sprayers & Drones", "Seeders & Tillers");
        catCombo.setValue("Tractors");
        catCombo.setPrefWidth(280);

        TextField priceField = new TextField();
        priceField.setPromptText("Daily rate in ₹ (e.g. 1400)");

        TextField specsField = new TextField();
        specsField.setPromptText("e.g. 52 HP • Dual Clutch • 4WD");

        CheckBox opCheck = new CheckBox("Trained Driver / Operator Available with Machine");
        opCheck.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-fill: #5C4033;");

        form.add(createLabel("Machinery Title:"), 0, 0);
        form.add(nameField, 1, 0);

        form.add(createLabel("Category:"), 0, 1);
        form.add(catCombo, 1, 1);

        form.add(createLabel("Daily Rate (₹):"), 0, 2);
        form.add(priceField, 1, 2);

        form.add(createLabel("Key Specs:"), 0, 3);
        form.add(specsField, 1, 3);

        form.add(opCheck, 1, 4);

        Button saveBtn = new Button("Register Machinery");
        saveBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
        saveBtn.setPrefHeight(38);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
        cancelBtn.setPrefHeight(38);

        saveBtn.setOnAction(e -> {
            String n = nameField.getText().trim();
            String p = priceField.getText().trim();
            if (!n.isEmpty() && !p.isEmpty()) {
                int pr = 1000;
                try {
                    pr = Integer.parseInt(p);
                } catch (Exception ignored) {}
                String sp = specsField.getText().trim().isEmpty() ? "Standard Agriculture Spec" : specsField.getText().trim();
                String icon = catCombo.getValue().contains("Harvester") ? "🌾" : (catCombo.getValue().contains("Drone") ? "🚁" : "🚜");
                fleetList.add(0, new FleetItem(n, catCombo.getValue(), icon, pr, sp, "AVAILABLE", "file:farm/src/main/resources/assets/Images/tractor.png", 0, 0, opCheck.isSelected()));
                renderFleetGrid();
                root.getChildren().remove(overlay);
            }
        });

        cancelBtn.setOnAction(e -> root.getChildren().remove(overlay));

        HBox btnBox = new HBox(12, saveBtn, cancelBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        modal.getChildren().addAll(title, form, btnBox);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private static void showEditTariffModal(FleetItem item, StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(15);
        modal.setPrefWidth(420);
        modal.setMaxWidth(420);
        modal.setPadding(new Insets(22));
        modal.setStyle("-fx-background-color: #FFFDF9; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text title = new Text("Update Daily Tariff: " + item.name);
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        TextField rateField = new TextField(String.valueOf(item.pricePerDay));
        rateField.setPrefHeight(36);

        Button save = new Button("Update Rate");
        save.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");

        Button cancel = new Button("Cancel");
        cancel.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");

        save.setOnAction(e -> {
            try {
                item.pricePerDay = Integer.parseInt(rateField.getText().trim());
                renderFleetGrid();
            } catch (Exception ignored) {}
            root.getChildren().remove(overlay);
        });

        cancel.setOnAction(e -> root.getChildren().remove(overlay));

        HBox btns = new HBox(10, save, cancel);
        btns.setAlignment(Pos.CENTER_RIGHT);

        modal.getChildren().addAll(title, new Label("New Daily Rental Rate (₹):"), rateField, btns);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private static Label createLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #5C4033;");
        return l;
    }
}
