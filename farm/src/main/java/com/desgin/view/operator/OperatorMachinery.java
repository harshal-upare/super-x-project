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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OperatorMachinery {

    public static class MachineItem {
        public String name;
        public String category;
        public String icon;
        public String modelNumber;
        public String hp;
        public String fuelLevel;
        public double fuelPct;
        public String status; // "IN_USE", "READY", "SERVICE_DUE"
        public String assignedImplement;
        public String nextServiceIn;
        public String imagePath;

        public MachineItem(String name, String category, String icon, String modelNumber, String hp, String fuelLevel, double fuelPct, String status, String assignedImplement, String nextServiceIn, String imagePath) {
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
        }
    }

    private static List<MachineItem> machineList = new ArrayList<>();
    private static VBox machinesContainer;

    static {
        initMachines();
    }

    private static void initMachines() {
        if (!machineList.isEmpty()) return;
        machineList.add(new MachineItem("John Deere 5310 4WD", "Tractor", "🚜", "JD-5310-2024", "55 HP PowerTech™ Engine", "78%", 0.78, "IN_USE", "Heavy Duty Rotavator (7 ft)", "42 Engine Hours", "file:farm/src/main/resources/assets/Images/tractor.png"));
        machineList.add(new MachineItem("Mahindra 575 DI Bhoomiputra", "Tractor", "🚜", "MHD-575-2023", "45 HP m-BOOST Engine", "92%", 0.92, "READY", "9-Tyne Rigid Cultivator", "110 Engine Hours", "file:farm/src/main/resources/assets/Images/tractor.png"));
        machineList.add(new MachineItem("Preet 987 Multicrop Combine", "Harvester", "🌾", "PRT-987-STD", "101 HP Turbocharged Engine", "65%", 0.65, "READY", "14 ft Floating Cutter Bar", "18 Engine Hours", "file:farm/src/main/resources/assets/Images/tractor.png"));
        machineList.add(new MachineItem("Hexacopter Agri Spray Drone", "Drone / Sprayer", "🚁", "DRN-AG16-V2", "16L Smart Tank Capacity", "88% (Dual Lipo)", 0.88, "READY", "Centrifugal Atomizing Nozzles", "5 Flight Cycles", "file:farm/src/main/resources/assets/Images/tractor.png"));
        machineList.add(new MachineItem("Laser Land Leveler System", "Implement", "⚙", "LVL-LASER-800", "Compatible with 45+ HP", "N/A (PTO Driven)", 1.0, "READY", "Transmitter & Mast Receiver", "Routine Greasing OK", "file:farm/src/main/resources/assets/Images/tractor.png"));
    }

    public static ScrollPane getMachinerySection(StackPane root) {
        Text title = new Text("Assigned Machinery & Equipment");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text subtitle = new Text("View machinery telemetry, fuel diagnostics, assigned implements, and operator operating manuals.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #806A5B;");

        VBox titleBox = new VBox(4, title, subtitle);

        Button preTripBtn = new Button("📋  Pre-Operation Checklist");
        preTripBtn.setPrefHeight(40);
        preTripBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 16 0 16;");
        preTripBtn.setOnAction(e -> showPreTripModal(root));

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox topBar = new HBox(titleBox, topSpacer, preTripBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Filter Tabs
        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        Button allBtn = createFilterBtn("All Machines (5)", true);
        Button tractorBtn = createFilterBtn("Tractors (2)", false);
        Button harvesterBtn = createFilterBtn("Harvesters (1)", false);
        Button droneBtn = createFilterBtn("Drones & Implements (2)", false);

        allBtn.setOnAction(e -> { setActiveFilter(allBtn, filterBox); renderMachines("ALL", root); });
        tractorBtn.setOnAction(e -> { setActiveFilter(tractorBtn, filterBox); renderMachines("Tractor", root); });
        harvesterBtn.setOnAction(e -> { setActiveFilter(harvesterBtn, filterBox); renderMachines("Harvester", root); });
        droneBtn.setOnAction(e -> { setActiveFilter(droneBtn, filterBox); renderMachines("Drone", root); });

        filterBox.getChildren().addAll(allBtn, tractorBtn, harvesterBtn, droneBtn);

        machinesContainer = new VBox(14);
        renderMachines("ALL", root);

        VBox content = new VBox(20, topBar, filterBox, machinesContainer);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static void renderMachines(String filter, StackPane root) {
        machinesContainer.getChildren().clear();

        for (MachineItem m : machineList) {
            if (!filter.equals("ALL")) {
                if (filter.equals("Drone") && !m.category.contains("Drone") && !m.category.contains("Implement")) {
                    continue;
                } else if (!filter.equals("Drone") && !m.category.equals(filter)) {
                    continue;
                }
            }

            VBox card = createMachineCard(m, root);
            machinesContainer.getChildren().add(card);
        }
    }

    private static VBox createMachineCard(MachineItem m, StackPane root) {
        Image img = null;
        try {
            img = new Image(m.imagePath);
        } catch (Exception ignored) {}

        ImageView imgView = new ImageView();
        if (img != null && !img.isError()) {
            imgView.setImage(img);
            imgView.setFitWidth(140);
            imgView.setFitHeight(80);
            imgView.setPreserveRatio(true);
        } else {
            imgView.setFitWidth(140);
            imgView.setFitHeight(80);
        }

        Text iconBadge = new Text(m.icon);
        iconBadge.setStyle("-fx-font-size: 26px;");

        StackPane imgBox = new StackPane(imgView, iconBadge);
        StackPane.setAlignment(iconBadge, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(iconBadge, new Insets(0, 8, 4, 0));
        imgBox.setPrefSize(145, 95);
        imgBox.setStyle("-fx-background-color: #E4D3C2; -fx-background-radius: 10;");

        // Info
        Text nameText = new Text(m.name);
        nameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text modelText = new Text("Model: " + m.modelNumber + "  |  " + m.hp);
        modelText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        Text impText = new Text("⚙ Attached Implement: " + m.assignedImplement);
        impText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #5C4033;");

        Text servText = new Text("🛠 Next Scheduled Service in: " + m.nextServiceIn);
        servText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #806A5B;");

        VBox infoBox = new VBox(4, nameText, modelText, impText, servText);

        // Telemetry Box
        Text fuelLabel = new Text("Fuel / Power: " + m.fuelLevel);
        fuelLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        ProgressBar fuelBar = new ProgressBar(m.fuelPct);
        fuelBar.setPrefWidth(160);
        fuelBar.setPrefHeight(8);
        fuelBar.setStyle("-fx-accent: " + (m.fuelPct > 0.3 ? "#2E7D32" : "#E65100") + ";");

        Label statusLabel = new Label("● " + m.status.replace("_", " "));
        if (m.status.equals("IN_USE")) {
            statusLabel.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");
        } else {
            statusLabel.setStyle("-fx-background-color: #EDE3D5; -fx-text-fill: #5C4033; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");
        }

        VBox telemetryBox = new VBox(6, fuelLabel, fuelBar, statusLabel);
        telemetryBox.setAlignment(Pos.CENTER_LEFT);

        // Actions
        Button manualBtn = new Button("📖 Handbook");
        manualBtn.setStyle("-fx-background-color: #E4D3C2; -fx-text-fill: #4A2C20; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12 6 12;");
        manualBtn.setOnAction(e -> showManualModal(m, root));

        Button reportBtn = new Button("⚠️ Report Issue");
        reportBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12 6 12;");
        reportBtn.setOnAction(e -> {
            OperatorLeftSideBar.setActiveButton(OperatorLeftSideBar.maintenanceBtn, OperatorLeftSideBar.navigationButtons);
            OperatorDashboard.borderPane.setCenter(OperatorMaintenance.getMaintenanceSection(OperatorDashboard.root));
        });

        VBox actionBox = new VBox(8, manualBtn, reportBtn);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(20, imgBox, infoBox, spacer, telemetryBox, actionBox);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 18, 14, 18));
        row.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");

        row.setOnMouseEntered(e -> {
            row.setStyle("-fx-background-color: #FFF9F0; -fx-background-radius: 12; -fx-border-color: #8B6F47; -fx-border-width: 1; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(74,44,32,0.15), 8, 0.2, 0, 3);");
        });
        row.setOnMouseExited(e -> {
            row.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");
        });

        return new VBox(row);
    }

    private static Button createFilterBtn(String text, boolean active) {
        Button btn = new Button(text);
        btn.setPrefHeight(34);
        if (active) {
            btn.setStyle("-fx-background-color: #4A2C20; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14 0 14;");
        } else {
            btn.setStyle("-fx-background-color: #E4D3C2; -fx-text-fill: #4A2C20; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14 0 14;");
        }
        return btn;
    }

    private static void setActiveFilter(Button activeBtn, HBox filterBox) {
        for (var node : filterBox.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setStyle("-fx-background-color: #E4D3C2; -fx-text-fill: #4A2C20; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14 0 14;");
            }
        }
        activeBtn.setStyle("-fx-background-color: #4A2C20; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14 0 14;");
    }

    private static void showManualModal(MachineItem m, StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(500);
        modal.setMaxWidth(500);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFDF9; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text title = new Text("Operator Handbook: " + m.name);
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text info = new Text(
                "• Recommended Operating RPM: 1,600 - 2,000 RPM\n" +
                "• PTO Speed: 540 RPM @ 1,790 Engine RPM\n" +
                "• Hydraulic Lift Capacity: 2,000 kg at Hitch Points\n" +
                "• Daily Grease Points: King pins, 3-point linkage, front axle pivot\n" +
                "• Tire Pressure (Field): Front 22 PSI, Rear 14 PSI\n" +
                "• Safety Interlock: Neutral safety switch with clutch pedal override."
        );
        info.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #5C4033; -fx-line-spacing: 4px;");

        Button closeBtn = new Button("Close Handbook");
        closeBtn.setStyle("-fx-background-color: #4A2C20; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
        closeBtn.setOnAction(e -> root.getChildren().remove(overlay));

        modal.getChildren().addAll(title, info, closeBtn);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private static void showPreTripModal(StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(480);
        modal.setMaxWidth(480);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFDF9; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text title = new Text("Submit Pre-Operation Inspection");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text sub = new Text("All items have been verified according to FarmEquip safety protocol.");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        TextField noteField = new TextField("All 4 machines inspected and field-ready.");
        noteField.setPrefHeight(36);
        noteField.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-background-color: white; -fx-border-color: #D8C7B5; -fx-border-radius: 6;");

        Button submitBtn = new Button("Submit Inspection Log");
        submitBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        submitBtn.setOnAction(e -> root.getChildren().remove(overlay));

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> root.getChildren().remove(overlay));

        HBox btnBox = new HBox(10, submitBtn, cancelBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        modal.getChildren().addAll(title, sub, noteField, btnBox);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }
}
