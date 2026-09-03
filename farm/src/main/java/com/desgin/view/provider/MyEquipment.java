package com.desgin.view.provider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.desgin.config.CloudinaryConfig;
import com.desgin.dao.MachineryDAO;
import com.desgin.exception.MachineryValidationException;
import com.desgin.model.MachineryModel;
import com.desgin.view.farmer.Swapnil.EquipmentDataStore;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
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
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class MyEquipment {

    public static class FleetItem {
        public String id;
        public String name;
        public String category;
        public String icon;
        public int pricePerDay;
        public String location;
        public String specs;
        public String status; // "AVAILABLE", "RENTED OUT", "IN SERVICE"
        public String imagePath;
        public int totalRentals;
        public int lifetimeEarned;
        public boolean hasOperator;

        public FleetItem(String id, String name, String category, String icon, int pricePerDay, String location,
                         String specs, String status, String imagePath, int totalRentals, int lifetimeEarned, boolean hasOperator) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.icon = icon;
            this.pricePerDay = pricePerDay;
            this.location = location;
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
    private static Label dbStatusLabel;

    public static ScrollPane getFleetSection(StackPane root) {
        currentRoot = root;

        Text headerTitle = new Text("My Fleet & Machinery Management");
        headerTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text headerSubtitle = new Text("Manage your active machinery inventory, set rental tariffs, toggle live availability, and add new equipment.");
        headerSubtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(4, headerTitle, headerSubtitle);

        Button refreshBtn = new Button("🔄 Refresh Fleet");
        refreshBtn.setPrefHeight(42);
        refreshBtn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #C2E0CE; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 16;");
        refreshBtn.setOnAction(e -> loadFleetFromFirestore());

        Button addEquipmentBtn = new Button("➕  Add New Machinery");
        addEquipmentBtn.setPrefHeight(42);
        addEquipmentBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 20;");
        addEquipmentBtn.setOnAction(e -> showAddEquipmentModal(root));

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox topBar = new HBox(12, titleBox, topSpacer, refreshBtn, addEquipmentBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Fleet Metrics Summary Strip
        HBox metricStrip = createFleetMetricStrip();

        // Search and Filter Bar
        TextField searchInput = new TextField();
        searchInput.setPromptText("Search by machine name, category or town...");
        searchInput.setPrefHeight(40);
        searchInput.setPrefWidth(380);
        searchInput.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-radius: 8; -fx-font-family: 'Poppins'; -fx-font-size: 13px;");
        searchInput.textProperty().addListener((obs, oldV, newV) -> {
            searchQuery = newV.toLowerCase().trim();
            renderFleetGrid();
        });

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Statuses", "AVAILABLE Only", "RENTED OUT", "IN SERVICE");
        statusFilter.setValue("All Statuses");
        statusFilter.setPrefHeight(40);
        statusFilter.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-radius: 8; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px;");
        statusFilter.setOnAction(e -> {
            activeStatusFilter = statusFilter.getValue();
            renderFleetGrid();
        });

        dbStatusLabel = new Label("Live Sync");
        dbStatusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #2E7D32; -fx-font-weight: bold;");

        HBox searchFilterBox = new HBox(12, searchInput, statusFilter, dbStatusLabel);
        searchFilterBox.setAlignment(Pos.CENTER_LEFT);

        // Category Filter Pills
        HBox categoryPills = createCategoryPills();

        // Cards Grid
        cardsGrid = new FlowPane();
        cardsGrid.setHgap(18);
        cardsGrid.setVgap(18);
        cardsGrid.setPrefWrapLength(1080);

        // Initial Load from Firestore
        loadFleetFromFirestore();

        VBox content = new VBox(20, topBar, metricStrip, searchFilterBox, categoryPills, cardsGrid);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    public static void loadFleetFromFirestore() {
        if (dbStatusLabel != null) dbStatusLabel.setText("Updating...");
        Thread bg = new Thread(() -> {
            try {
                MachineryDAO dao = new MachineryDAO();
                String providerEmail = ProviderProfileStore.email;
                List<MachineryModel> models = dao.getMachineryByProvider(providerEmail);

                // If user has not added anything under this specific email or during testing, load all machinery
                if (models.isEmpty()) {
                    models = dao.getAllMachinery();
                }

                List<FleetItem> items = new ArrayList<>();
                for (MachineryModel m : models) {
                    String cat = m.getCategory() != null ? m.getCategory() : "Tractors";
                    String icon = cat.contains("Harvester") ? "🌾" : (cat.contains("Drone") ? "🚁" : (cat.contains("Rotavator") ? "⚙" : (cat.contains("Cultivator") ? "🌱" : "🚜")));
                    items.add(new FleetItem(
                        m.getId(),
                        m.getName(),
                        m.getCategory(),
                        icon,
                        m.getPricePerDay(),
                        m.getLocation() != null ? m.getLocation() : "Pune",
                        m.getSpecs() != null ? m.getSpecs() : "Operating Location: " + m.getLocation(),
                        m.getStatus() != null ? m.getStatus() : "AVAILABLE",
                        m.getImagePath(),
                        0,
                        0,
                        m.isHasOperator()
                    ));
                }

                Platform.runLater(() -> {
                    fleetList.clear();
                    fleetList.addAll(items);
                    if (dbStatusLabel != null) dbStatusLabel.setText("✓ " + items.size() + " Active Units");
                    renderFleetGrid();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    if (dbStatusLabel != null) dbStatusLabel.setText("Offline");
                });
            }
        });
        bg.setDaemon(true);
        bg.start();
    }

    private static HBox createFleetMetricStrip() {
        long total = fleetList.size();
        long avail = fleetList.stream().filter(f -> "AVAILABLE".equalsIgnoreCase(f.status)).count();
        long rented = fleetList.stream().filter(f -> "RENTED OUT".equalsIgnoreCase(f.status)).count();
        long service = fleetList.stream().filter(f -> "IN SERVICE".equalsIgnoreCase(f.status)).count();

        HBox strip = new HBox(15,
            createMetricCard("🚜 Total Fleet Units", total + " Machines", "#1B4332"),
            createMetricCard("✔ Ready / Available", avail + " Units", "#2E7D32"),
            createMetricCard("⏱ Currently Rented", rented + " Units", "#E65100"),
            createMetricCard("🛠 In Maintenance", service + " Units", "#C62828")
        );
        strip.setAlignment(Pos.CENTER_LEFT);
        return strip;
    }

    private static VBox createMetricCard(String title, String value, String color) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");
        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        VBox b = new VBox(4, t, v);
        b.setPrefWidth(210);
        b.setPadding(new Insets(12, 16, 12, 16));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");
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
            btn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 0 16 0 16; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-background-radius: 20; -fx-border-color: #E2EBE5; -fx-border-radius: 20; -fx-padding: 0 16 0 16; -fx-cursor: hand;");
        }
    }

    private static void renderFleetGrid() {
        if (cardsGrid == null) return;
        cardsGrid.getChildren().clear();

        for (FleetItem item : fleetList) {
            if (!activeCategory.equals("All") && !item.category.equalsIgnoreCase(activeCategory)) {
                continue;
            }

            if (activeStatusFilter.equals("AVAILABLE Only") && !"AVAILABLE".equalsIgnoreCase(item.status)) continue;
            if (activeStatusFilter.equals("RENTED OUT") && !"RENTED OUT".equalsIgnoreCase(item.status)) continue;
            if (activeStatusFilter.equals("IN SERVICE") && !"IN SERVICE".equalsIgnoreCase(item.status)) continue;

            if (!searchQuery.isEmpty()) {
                boolean matches = item.name.toLowerCase().contains(searchQuery)
                        || item.category.toLowerCase().contains(searchQuery)
                        || (item.location != null && item.location.toLowerCase().contains(searchQuery));
                if (!matches) continue;
            }

            cardsGrid.getChildren().add(createFleetCard(item));
        }

        if (cardsGrid.getChildren().isEmpty()) {
            VBox emptyBox = new VBox(10);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(40));
            Text emptyText = new Text("No machinery in database matches the filter.");
            emptyText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-fill: #4B5563;");
            cardsGrid.getChildren().add(emptyBox);
        }
    }

    private static VBox createFleetCard(FleetItem item) {
        ImageView iv = new ImageView();
        iv.setFitWidth(220);
        iv.setFitHeight(110);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);

        if (item.imagePath != null && !item.imagePath.trim().isEmpty()) {
            try {
                Image img = new Image(item.imagePath, true);
                iv.setImage(img);
            } catch (Exception ignored) {}
        }

        Text iconBadge = new Text(item.icon);
        iconBadge.setStyle("-fx-font-size: 24px;");

        StackPane imgBox = new StackPane(iv, iconBadge);
        StackPane.setAlignment(iconBadge, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(iconBadge, new Insets(0, 10, 6, 0));
        imgBox.setPrefHeight(115);
        imgBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10;");

        // Status Badge & Operator Indicator
        String stColor = "AVAILABLE".equalsIgnoreCase(item.status) ? "#2E7D32" : ("RENTED OUT".equalsIgnoreCase(item.status) ? "#E65100" : "#C62828");
        Label statusBadge = new Label("● " + item.status);
        statusBadge.setStyle("-fx-background-color: " + stColor + "; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 4;");

        Label opBadge = new Label(item.hasOperator ? "👨‍🌾 Operator Incl." : "🚜 Machine Only");
        opBadge.setStyle("-fx-background-color: #F4F9F4; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-padding: 3 6; -fx-background-radius: 4;");

        Region bSpacer = new Region();
        HBox.setHgrow(bSpacer, Priority.ALWAYS);
        HBox badgeRow = new HBox(statusBadge, bSpacer, opBadge);
        badgeRow.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text(item.name);
        title.setWrappingWidth(235);
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text locText = new Text("📍 Location: " + (item.location != null ? item.location : "Not Specified"));
        locText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #2D6A4F; -fx-font-weight: 500;");

        Text price = new Text("₹" + item.pricePerDay + " / day");
        price.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        // Action Buttons: Toggle Status + Edit Tariff + Delete
        Button toggleStatusBtn = new Button("Toggle Status 🔄");
        toggleStatusBtn.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        toggleStatusBtn.setPrefHeight(30);

        toggleStatusBtn.setOnAction(e -> {
            if ("AVAILABLE".equalsIgnoreCase(item.status)) item.status = "RENTED OUT";
            else if ("RENTED OUT".equalsIgnoreCase(item.status)) item.status = "IN SERVICE";
            else item.status = "AVAILABLE";

            EquipmentDataStore.updateStatus(item.name, item.status);
            renderFleetGrid();

            Thread t = new Thread(() -> {
                try {
                    new MachineryDAO().updateMachineryStatus(item.id, item.status);
                } catch (Exception ignored) {}
            });
            t.setDaemon(true);
            t.start();
        });

        Button editBtn = new Button("Edit Tariff ✏");
        editBtn.setStyle("-fx-background-color: #374151; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        editBtn.setPrefHeight(30);
        editBtn.setOnAction(e -> showEditTariffModal(item, currentRoot));

        Button deleteBtn = new Button("Delete 🗑");
        deleteBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        deleteBtn.setPrefHeight(30);
        deleteBtn.setOnAction(e -> {
            fleetList.remove(item);
            EquipmentDataStore.removeEquipment(item.name);
            renderFleetGrid();

            Thread t = new Thread(() -> {
                try {
                    new MachineryDAO().deleteMachinery(item.id);
                } catch (Exception ignored) {}
            });
            t.setDaemon(true);
            t.start();
        });

        HBox btnRow = new HBox(6, toggleStatusBtn, editBtn, deleteBtn);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(8, imgBox, badgeRow, title, locText, price, btnRow);
        card.setPrefWidth(260);
        card.setPadding(new Insets(14));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #F0FDF4; -fx-background-radius: 12; -fx-border-color: #2D6A4F; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(27,67,50,0.2), 10, 0.2, 0, 3);");
            card.setTranslateY(-2);
        });

        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
            card.setTranslateY(0);
        });

        return card;
    }

    /**
     * Streamlined "Add New Machinery" Dialog with FEWER FIELDS:
     * 1. Machinery Title
     * 2. Category (Dropdown)
     * 3. Daily Rate (₹)
     * 4. Operating Location/Town
     * 5. Operator Included CheckBox
     * 6. Machinery Photo (Cloudinary upload)
     */
    private static void showAddEquipmentModal(StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(15);
        modal.setPrefWidth(500);
        modal.setMaxWidth(500);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 25, 0.3, 0, 8);");

        Text title = new Text("Register New Machinery");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 19px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text subtitle = new Text("Add equipment to your active fleet for local farmers to rent.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);

        // 1. Machinery Name
        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Swaraj 855 FE Tractor");
        nameField.setPrefHeight(36);
        nameField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px;");

        // 2. Category
        ComboBox<String> catCombo = new ComboBox<>();
        catCombo.getItems().addAll("Tractors", "Harvesters", "Rotavators", "Cultivators", "Sprayers & Drones", "Seeders & Tillers");
        catCombo.setValue("Tractors");
        catCombo.setPrefWidth(280);
        catCombo.setPrefHeight(36);
        catCombo.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px;");

        // 3. Daily Rate (₹)
        TextField priceField = new TextField();
        priceField.setPromptText("e.g. 1500");
        priceField.setPrefHeight(36);
        priceField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px;");

        // 4. Operating Location/Town (pre-filled with Provider's town)
        TextField locField = new TextField(ProviderProfileStore.town != null ? ProviderProfileStore.town : "Pune");
        locField.setPromptText("e.g. Pune, Baramati, Indapur");
        locField.setPrefHeight(36);
        locField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px;");

        // 5. Operator Checkbox
        CheckBox opCheck = new CheckBox("Trained Operator / Driver Included");
        opCheck.setSelected(true);
        opCheck.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-fill: #374151;");

        // 6. Image Upload
        final String defaultImage = "file:farm/src/main/resources/assets/Images/tractor.png";
        final String[] finalImageUri = new String[] { defaultImage };

        Button uploadImgBtn = new Button("📁 Choose Photo");
        uploadImgBtn.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12;");

        Label uploadStatusLabel = new Label("Photo selected");
        uploadStatusLabel.setMaxWidth(160);
        uploadStatusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #4B5563; -fx-font-style: italic;");

        ImageView previewImg = new ImageView();
        previewImg.setFitWidth(55);
        previewImg.setFitHeight(40);
        previewImg.setPreserveRatio(true);
        try {
            previewImg.setImage(new Image(defaultImage));
        } catch (Exception ignored) {}

        StackPane previewBox = new StackPane(previewImg);
        previewBox.setPrefSize(60, 45);
        previewBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 6; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 6;");

        uploadImgBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose Machinery Photo");
            chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp", "*.bmp")
            );
            Window win = root.getScene() != null ? root.getScene().getWindow() : null;
            File file = chooser.showOpenDialog(win);
            if (file != null) {
                uploadStatusLabel.setText("Attaching photo...");
                uploadImgBtn.setDisable(true);
                Task<String> uploadTask = new Task<>() {
                    @Override
                    protected String call() {
                        return CloudinaryConfig.uploadImage(file);
                    }
                };
                uploadTask.setOnSucceeded(ev -> {
                    uploadImgBtn.setDisable(false);
                    String url = uploadTask.getValue();
                    if (url != null) {
                        finalImageUri[0] = url;
                        uploadStatusLabel.setText("✓ Photo attached");
                        uploadStatusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #15803D; -fx-font-weight: bold;");
                        try {
                            previewImg.setImage(new Image(url));
                        } catch (Exception ignored) {}
                    } else {
                        finalImageUri[0] = file.toURI().toString();
                        uploadStatusLabel.setText("Photo attached");
                        try {
                            previewImg.setImage(new Image(finalImageUri[0]));
                        } catch (Exception ignored) {}
                    }
                });
                uploadTask.setOnFailed(ev -> {
                    uploadImgBtn.setDisable(false);
                    uploadStatusLabel.setText("Upload error.");
                });
                new Thread(uploadTask).start();
            }
        });

        HBox imgRow = new HBox(8, uploadImgBtn, previewBox, uploadStatusLabel);
        imgRow.setAlignment(Pos.CENTER_LEFT);

        form.add(createLabel("Machinery Title:"), 0, 0);
        form.add(nameField, 1, 0);

        form.add(createLabel("Category:"), 0, 1);
        form.add(catCombo, 1, 1);

        form.add(createLabel("Daily Rate (₹):"), 0, 2);
        form.add(priceField, 1, 2);

        form.add(createLabel("Operating Town:"), 0, 3);
        form.add(locField, 1, 3);

        form.add(createLabel("Machinery Photo:"), 0, 4);
        form.add(imgRow, 1, 4);

        form.add(opCheck, 1, 5);

        Button saveBtn = new Button("Register Machinery");
        saveBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
        saveBtn.setPrefHeight(38);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
        cancelBtn.setPrefHeight(38);

        saveBtn.setOnAction(e -> {
            try {
                String n = nameField.getText() != null ? nameField.getText().trim() : "";
                String p = priceField.getText() != null ? priceField.getText().trim() : "";
                String loc = locField.getText() != null ? locField.getText().trim() : "";

                if (n.isEmpty()) {
                    throw new MachineryValidationException("Machinery title cannot be empty.");
                }
                if (p.isEmpty()) {
                    throw new MachineryValidationException("Please enter a daily rental rate.");
                }
                int pr;
                try {
                    pr = Integer.parseInt(p);
                    if (pr <= 0) {
                        throw new MachineryValidationException("Daily rate must be greater than ₹0.");
                    }
                } catch (NumberFormatException nfe) {
                    throw new MachineryValidationException("Daily rate must be a valid whole number.");
                }
                if (loc.isEmpty()) {
                    throw new MachineryValidationException("Operating location/town is required.");
                }

                String cat = catCombo.getValue();
                String icon = cat.contains("Harvester") ? "🌾" : (cat.contains("Drone") ? "🚁" : (cat.contains("Rotavator") ? "⚙" : (cat.contains("Cultivator") ? "🌱" : "🚜")));
                String macId = "MAC_" + System.currentTimeMillis();

                MachineryModel model = new MachineryModel(
                    macId,
                    n,
                    cat,
                    pr,
                    loc,
                    ProviderProfileStore.email,
                    ProviderProfileStore.name,
                    ProviderProfileStore.phone,
                    opCheck.isSelected(),
                    finalImageUri[0],
                    "Operating in " + loc + (opCheck.isSelected() ? " • With Operator" : "")
                );

                // Instant optimistic UI update (0 delay)
                fleetList.add(0, new FleetItem(macId, n, cat, icon, pr, loc, model.getSpecs(), "AVAILABLE", finalImageUri[0], 0, 0, opCheck.isSelected()));
                EquipmentDataStore.addEquipment(new EquipmentDataStore.EquipmentItem(
                    macId, n, cat, pr, "4.8", loc, finalImageUri[0], model.getSpecs(), "AVAILABLE", opCheck.isSelected()
                ));
                renderFleetGrid();
                root.getChildren().remove(overlay);

                // Asynchronous background write to database
                Thread t = new Thread(() -> {
                    try {
                        new MachineryDAO().addMachinery(model);
                    } catch (Exception ignored) {}
                });
                t.setDaemon(true);
                t.start();

            } catch (MachineryValidationException valEx) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Warning");
                alert.setHeaderText("Invalid Machinery Data");
                alert.setContentText(valEx.getMessage());
                alert.showAndWait();
            }
        });

        cancelBtn.setOnAction(e -> root.getChildren().remove(overlay));

        HBox btnBox = new HBox(12, saveBtn, cancelBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        modal.getChildren().addAll(title, subtitle, form, btnBox);
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
        modal.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text title = new Text("Update Daily Tariff: " + item.name);
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        TextField rateField = new TextField(String.valueOf(item.pricePerDay));
        rateField.setPrefHeight(36);

        Button save = new Button("Save Daily Rate");
        save.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");

        Button cancel = new Button("Cancel");
        cancel.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");

        save.setOnAction(e -> {
            try {
                int newPr = Integer.parseInt(rateField.getText().trim());
                item.pricePerDay = newPr;
                EquipmentDataStore.updatePrice(item.name, item.pricePerDay);
                renderFleetGrid();
                root.getChildren().remove(overlay);

                Thread t = new Thread(() -> {
                    try {
                        new MachineryDAO().updateMachineryPrice(item.id, newPr);
                    } catch (Exception ignored) {}
                });
                t.setDaemon(true);
                t.start();
            } catch (Exception ignored) {
                root.getChildren().remove(overlay);
            }
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
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        return l;
    }
}
