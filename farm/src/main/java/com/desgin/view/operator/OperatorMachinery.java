package com.desgin.view.operator;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Enhanced Assigned Machinery & Equipment View
 * Displays assigned machinery in an attractive 3-card per row responsive grid
 * with real-time telemetry indicators, handbook modals, and inspection logs.
 */
public class OperatorMachinery {

    public static class MachineItem {
        public String name;
        public String category;
        public String icon;
        public String modelNumber;
        public String hp;
        public String fuelLevel;
        public double fuelPct;
        public String status; // "IN_USE", "READY"
        public String assignedImplement;
        public String nextServiceIn;
        public String imagePath;
        public String specs;

        public MachineItem(String name, String category, String icon, String modelNumber, String hp, String fuelLevel, double fuelPct, String status, String assignedImplement, String nextServiceIn, String imagePath, String specs) {
            this.name = name;
            this.category = category;
            this.icon = icon;
            this.modelNumber = modelNumber;
            this.hp = hp;
            this.fuelLevel = fuelLevel;
            this.fuelPct = fuelPct;
            this.status = status;
            this.assignedImplement = assignedImplement;
            this.nextServiceIn = nextServiceIn;
            this.imagePath = imagePath;
            this.specs = specs;
        }
    }

    private static List<MachineItem> machineList = new ArrayList<>();
    private static GridPane machinesGrid;

    static {
        initMachines();
    }

    private static void initMachines() {
        if (!machineList.isEmpty()) return;
        machineList.add(new MachineItem(
                "John Deere 5310 4WD", "Tractor", "🚜", "JD-5310-2024", "55 HP PowerTech™ Engine",
                "78%", 0.78, "IN_USE", "Heavy Duty Rotavator (7 ft)", "42 Engine Hours",
                "file:farm/src/main/resources/assets/Images/tractor.png",
                "• 3-Cylinder Turbocharged Engine\n• 9 Forward + 3 Reverse Collarshift\n• 2,000 kg Hitch Capacity\n• Dual Clutch with Independent PTO"
        ));
        machineList.add(new MachineItem(
                "Mahindra 575 DI", "Tractor", "🚜", "MHD-575-2023", "45 HP m-BOOST Engine",
                "92%", 0.92, "READY", "9-Tyne Rigid Cultivator", "110 Engine Hours",
                "file:farm/src/main/resources/assets/Images/tractor.png",
                "• 4-Cylinder DI Engine\n• High Torque at Low RPM (185 Nm)\n• 1,600 kg Hydraulic Lift\n• Mechanical Synchromesh Transmission"
        ));
        machineList.add(new MachineItem(
                "Preet 987 Multicrop", "Harvester", "🌾", "PRT-987-STD", "101 HP Turbo Diesel",
                "65%", 0.65, "READY", "14 ft Floating Cutter Bar", "18 Engine Hours",
                "file:farm/src/main/resources/assets/Images/tractor.png",
                "• 6-Cylinder Water-Cooled Engine\n• 5 Straw Walkers System\n• Grain Tank Capacity: 2.4 m³\n• Hydrostatic Power Steering"
        ));
        machineList.add(new MachineItem(
                "Hexacopter Agri Drone", "Drone", "🚁", "DRN-AG16-V2", "16L Smart Tank System",
                "88%", 0.88, "READY", "Centrifugal Atomizing Nozzles", "5 Flight Cycles",
                "file:farm/src/main/resources/assets/Images/tractor.png",
                "• Autonomous Obstacle Avoidance Radar\n• Spray Swath: 4.5 - 6.0 Meters\n• Dual FPV Live Cameras\n• Dual Smart Lipo Batteries (22000 mAh)"
        ));
        machineList.add(new MachineItem(
                "Laser Land Leveler 800", "Implement", "⚙", "LVL-LASER-800", "Compatible with 45+ HP",
                "100%", 1.0, "READY", "Transmitter & Mast Receiver", "Routine Greasing OK",
                "file:farm/src/main/resources/assets/Images/tractor.png",
                "• Dual Grade Laser Transmitter (600m range)\n• High-Precision Proportional Hydraulic Valve\n• Heavy Duty 7 ft Bucket Blade\n• Quick-Mount Telescopic Mast"
        ));
        machineList.add(new MachineItem(
                "Kubota 4-Row Transplanter", "Transplanter", "🌾", "KBT-NSP-4W", "4.3 HP OHV Gasoline",
                "82%", 0.82, "READY", "Rotary Planting Arm Claws", "35 Engine Hours",
                "file:farm/src/main/resources/assets/Images/tractor.png",
                "• 4-Row High-Speed Paddy Planter\n• Adjustable Row Spacing (30 cm)\n• Low Soil Compaction Flotation Wheels\n• Automatic Leveling Hydraulic Sensor"
        ));
    }

    public static ScrollPane getMachinerySection(StackPane root) {
        // ================= HEADER =================
        Text title = new Text("Assigned Machinery & Equipment");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text subtitle = new Text("View machinery telemetry, fuel diagnostics, assigned implements, and operator operating manuals.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(4, title, subtitle);

        Button preTripBtn = new Button("📋  Pre-Operation Checklist");
        preTripBtn.setPrefHeight(40);
        preTripBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 16 0 16;");
        preTripBtn.setOnAction(e -> showPreTripModal(root));

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox topBar = new HBox(titleBox, topSpacer, preTripBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // ================= FILTER TABS =================
        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        Button allBtn = createFilterBtn("All Machinery (6)", true);
        Button tractorBtn = createFilterBtn("Tractors (2)", false);
        Button harvesterBtn = createFilterBtn("Harvesters & Planters (2)", false);
        Button droneBtn = createFilterBtn("Drones & Implements (2)", false);

        allBtn.setOnAction(e -> { setActiveFilter(allBtn, filterBox); renderMachines("ALL", root); });
        tractorBtn.setOnAction(e -> { setActiveFilter(tractorBtn, filterBox); renderMachines("Tractor", root); });
        harvesterBtn.setOnAction(e -> { setActiveFilter(harvesterBtn, filterBox); renderMachines("Harvester", root); });
        droneBtn.setOnAction(e -> { setActiveFilter(droneBtn, filterBox); renderMachines("Drone", root); });

        filterBox.getChildren().addAll(allBtn, tractorBtn, harvesterBtn, droneBtn);

        // ================= 3-COLUMN CARD GRID =================
        machinesGrid = new GridPane();
        machinesGrid.setHgap(18);
        machinesGrid.setVgap(20);
        machinesGrid.setAlignment(Pos.TOP_LEFT);

        // Set 3 equal columns (33.33% each) for perfectly balanced 3-card layout
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33.33);
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33.33);
        col2.setHgrow(Priority.ALWAYS);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(33.33);
        col3.setHgrow(Priority.ALWAYS);

        machinesGrid.getColumnConstraints().addAll(col1, col2, col3);

        renderMachines("ALL", root);

        VBox content = new VBox(22, topBar, filterBox, machinesGrid);
        content.setPadding(new Insets(25, 30, 40, 30));
        content.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static void renderMachines(String filter, StackPane root) {
        machinesGrid.getChildren().clear();

        List<MachineItem> filtered = new ArrayList<>();
        for (MachineItem m : machineList) {
            if (filter.equals("ALL")) {
                filtered.add(m);
            } else if (filter.equals("Tractor") && m.category.equals("Tractor")) {
                filtered.add(m);
            } else if (filter.equals("Harvester") && (m.category.equals("Harvester") || m.category.equals("Transplanter"))) {
                filtered.add(m);
            } else if (filter.equals("Drone") && (m.category.equals("Drone") || m.category.equals("Implement"))) {
                filtered.add(m);
            }
        }

        int col = 0;
        int row = 0;
        for (MachineItem m : filtered) {
            VBox card = createMachineCard(m, root);
            machinesGrid.add(card, col, row);
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }

    private static VBox createMachineCard(MachineItem m, StackPane root) {
        // ================= IMAGE & FLOATING BADGES =================
        Image img = null;
        try {
            img = new Image(m.imagePath);
        } catch (Exception ignored) {}

        ImageView imgView = new ImageView();
        if (img != null && !img.isError()) {
            imgView.setImage(img);
            imgView.setFitWidth(230);
            imgView.setFitHeight(115);
            imgView.setPreserveRatio(true);
            imgView.setSmooth(true);
        } else {
            imgView.setFitWidth(230);
            imgView.setFitHeight(115);
        }

        // Category Badge on Top-Left
        Label catBadge = new Label(m.icon + " " + m.category);
        catBadge.setStyle("-fx-background-color: rgba(27,67,50,0.85); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 6;");

        // Status Badge on Top-Right
        Label statusLabel = new Label("● " + m.status.replace("_", " "));
        if (m.status.equals("IN_USE")) {
            statusLabel.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 6;");
        } else {
            statusLabel.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 6;");
        }

        HBox topBadges = new HBox(catBadge, new Region(), statusLabel);
        HBox.setHgrow(topBadges.getChildren().get(1), Priority.ALWAYS);
        topBadges.setAlignment(Pos.CENTER);
        topBadges.setPadding(new Insets(8));

        StackPane imgBox = new StackPane(imgView, topBadges);
        StackPane.setAlignment(topBadges, Pos.TOP_CENTER);
        imgBox.setPrefHeight(125);
        imgBox.setMinHeight(125);
        imgBox.setMaxHeight(125);
        imgBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10;");

        // ================= MACHINE INFO =================
        Text nameText = new Text(m.name);
        nameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text modelText = new Text(m.modelNumber + " • " + m.hp);
        modelText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        // Implement Chip
        Label impChip = new Label("⚙ Implement: " + m.assignedImplement);
        impChip.setMaxWidth(Double.MAX_VALUE);
        impChip.setStyle("-fx-background-color: #F4F9F4; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 8 4 8; -fx-background-radius: 6;");

        // Telemetry Fuel Progress
        Text fuelLabel = new Text("Fuel / Power: " + m.fuelLevel);
        fuelLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Region spacerFuel = new Region();
        HBox.setHgrow(spacerFuel, Priority.ALWAYS);

        Text serviceText = new Text("🛠 " + m.nextServiceIn);
        serviceText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #4B5563;");

        HBox fuelHeader = new HBox(fuelLabel, spacerFuel, serviceText);
        fuelHeader.setAlignment(Pos.CENTER_LEFT);

        ProgressBar fuelBar = new ProgressBar(m.fuelPct);
        fuelBar.setMaxWidth(Double.MAX_VALUE);
        fuelBar.setPrefHeight(7);
        fuelBar.setStyle("-fx-accent: " + (m.fuelPct > 0.3 ? "#2E7D32" : "#E65100") + ";");

        VBox telemetryBox = new VBox(4, fuelHeader, fuelBar);

        // ================= ACTION BUTTONS =================
        Button manualBtn = new Button("📖 Handbook");
        manualBtn.setMaxWidth(Double.MAX_VALUE);
        manualBtn.setPrefHeight(34);
        manualBtn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 7; -fx-cursor: hand;");
        manualBtn.setOnAction(e -> showManualModal(m, root));
        HBox.setHgrow(manualBtn, Priority.ALWAYS);

        Button specsBtn = new Button("⚙ Specs");
        specsBtn.setMaxWidth(Double.MAX_VALUE);
        specsBtn.setPrefHeight(34);
        specsBtn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 7; -fx-cursor: hand;");
        specsBtn.setOnAction(e -> showSpecsModal(m, root));
        HBox.setHgrow(specsBtn, Priority.ALWAYS);

        HBox actionRow = new HBox(8, manualBtn, specsBtn);
        actionRow.setAlignment(Pos.CENTER);

        // ================= CARD WRAPPER =================
        VBox card = new VBox(10, imgBox, nameText, modelText, impChip, telemetryBox, actionRow);
        card.setPadding(new Insets(14));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");

        // Hover Effect
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #F0FDF4; -fx-background-radius: 14; -fx-border-color: #2D6A4F; -fx-border-width: 1.5; -fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(27,67,50,0.18), 10, 0.2, 0, 4);");
            card.setTranslateY(-2);
        });
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
            card.setTranslateY(0);
        });

        return card;
    }

    private static Button createFilterBtn(String text, boolean active) {
        Button btn = new Button(text);
        btn.setPrefHeight(34);
        if (active) {
            btn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 0 16 0 16;");
        } else {
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-border-color: #E2EBE5; -fx-border-radius: 20; -fx-cursor: hand; -fx-padding: 0 16 0 16;");
        }
        return btn;
    }

    private static void setActiveFilter(Button activeBtn, HBox filterBox) {
        for (var node : filterBox.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-border-color: #E2EBE5; -fx-border-radius: 20; -fx-cursor: hand; -fx-padding: 0 16 0 16;");
            }
        }
        activeBtn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 0 16 0 16;");
    }

    private static void showManualModal(MachineItem m, StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.55);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(500);
        modal.setMaxWidth(500);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text title = new Text("📖 Operator Handbook: " + m.name);
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text info = new Text(
                "• Recommended Operating RPM: 1,600 - 2,000 RPM\n" +
                "• PTO Speed: 540 RPM @ 1,790 Engine RPM\n" +
                "• Hydraulic Lift Capacity: 2,000 kg at Hitch Points\n" +
                "• Daily Grease Points: King pins, 3-point linkage, front axle pivot\n" +
                "• Tire Pressure (Field): Front 22 PSI, Rear 14 PSI\n" +
                "• Safety Interlock: Neutral safety switch with clutch pedal override."
        );
        info.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #374151; -fx-line-spacing: 4px;");

        Button closeBtn = new Button("Close Handbook");
        closeBtn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 18 8 18;");
        closeBtn.setOnAction(e -> root.getChildren().remove(overlay));

        modal.getChildren().addAll(title, info, closeBtn);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private static void showSpecsModal(MachineItem m, StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.55);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(480);
        modal.setMaxWidth(480);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text title = new Text("⚙ Technical Specifications: " + m.name);
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text modelText = new Text("Model: " + m.modelNumber + "  |  Power Rating: " + m.hp);
        modelText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #4B5563;");

        Text specs = new Text(m.specs);
        specs.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #374151; -fx-line-spacing: 4px;");

        Button closeBtn = new Button("Close Specs");
        closeBtn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 18 8 18;");
        closeBtn.setOnAction(e -> root.getChildren().remove(overlay));

        modal.getChildren().addAll(title, modelText, specs, closeBtn);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private static void showPreTripModal(StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.55);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(480);
        modal.setMaxWidth(480);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text title = new Text("Submit Pre-Operation Inspection");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("All machinery items have been verified according to FarmEquip safety protocol.");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        TextField noteField = new TextField("All assigned machines inspected and verified field-ready.");
        noteField.setPrefHeight(36);
        noteField.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 6;");

        Button submitBtn = new Button("Submit Inspection Log");
        submitBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
        submitBtn.setOnAction(e -> root.getChildren().remove(overlay));

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
        cancelBtn.setOnAction(e -> root.getChildren().remove(overlay));

        HBox btnBox = new HBox(10, submitBtn, cancelBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        modal.getChildren().addAll(title, sub, noteField, btnBox);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }
}
