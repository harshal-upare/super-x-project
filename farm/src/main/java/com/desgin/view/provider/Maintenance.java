package com.desgin.view.provider;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Maintenance {

    public static class ServiceRecord {
        public String machineName;
        public String serviceType;
        public String date;
        public String nextDue;
        public int cost;
        public String technician;
        public String status; // "DUE SOON", "COMPLETED", "SCHEDULED"

        public ServiceRecord(String machineName, String serviceType, String date, String nextDue, int cost, String technician, String status) {
            this.machineName = machineName;
            this.serviceType = serviceType;
            this.date = date;
            this.nextDue = nextDue;
            this.cost = cost;
            this.technician = technician;
            this.status = status;
        }
    }

    private static List<ServiceRecord> serviceList = new ArrayList<>();
    private static VBox upcomingContainer;
    private static VBox historyContainer;

    static {
        initDefaultMaintenance();
    }

    private static void initDefaultMaintenance() {
        if (!serviceList.isEmpty()) return;
        serviceList.add(new ServiceRecord("John Deere 5310 Tractor (55HP)", "500-Hour Engine Oil & Hydraulic Fluid Change", "10 Aug 2026", "25 Nov 2026", 4200, "John Deere Authorized Service", "COMPLETED"));
        serviceList.add(new ServiceRecord("Kartar 4000 Combine Harvester", "Cutter Bar Blade Sharpening & Belt Tensioning", "08 Aug 2026", "22 Aug 2026", 2800, "Shree Ganesh Agro Repairs", "DUE SOON"));
        serviceList.add(new ServiceRecord("Agri-Drone 16L Autonomous Sprayer", "Nozzle Descaling & Motor Sensor Calibration", "12 Aug 2026", "18 Aug 2026", 1500, "Antigravity Drones Tech Center", "DUE SOON"));
        serviceList.add(new ServiceRecord("Shaktiman Semi-Champion 7ft", "Gearbox Oil Top-up & Flange Bolt Tightening", "02 Aug 2026", "15 Oct 2026", 1100, "Local Workshop", "COMPLETED"));
        serviceList.add(new ServiceRecord("Mahindra 575 DI (45HP)", "Clutch Plate & Brake Shoe Inspection", "25 Jul 2026", "20 Sep 2026", 3500, "Mahindra Tractor Care", "COMPLETED"));
    }

    public static ScrollPane getMaintenanceSection(StackPane root) {
        Text headerTitle = new Text("Machinery Health & Maintenance Log");
        headerTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text headerSubtitle = new Text("Schedule machinery servicing, log maintenance expenses, prevent breakdown during harvest peak, and track fleet health.");
        headerSubtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #806A5B;");

        VBox titleBox = new VBox(4, headerTitle, headerSubtitle);

        Button logBtn = new Button("➕  Log Service Record");
        logBtn.setPrefHeight(42);
        logBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 20 0 20;");
        logBtn.setOnAction(e -> showLogServiceModal(root));

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox topBar = new HBox(titleBox, topSpacer, logBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // 4 Health KPI Cards
        HBox healthKpis = createHealthKpis();

        // Section 1: Upcoming Maintenance Schedule & Alerts
        VBox upcomingSection = createUpcomingSection();

        // Section 2: Service History Log
        VBox historySection = createHistorySection();

        VBox content = new VBox(22, topBar, healthKpis, upcomingSection, historySection);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static HBox createHealthKpis() {
        VBox c1 = createMetricCard("🩺 Fleet Operational Health", "96% Healthy", "17 of 18 machines field-ready", "#2E7D32");
        VBox c2 = createMetricCard("⚠️ Services Due Soon", "2 Machines", "Action required in next 7 days", "#E65100");
        VBox c3 = createMetricCard("💰 Total Maintenance Spend", "₹24,800", "YTD service expenditure", "#4A2C20");
        VBox c4 = createMetricCard("🛡 Active AMC / Warranty", "14 Units", "Covered under service plans", "#5C4033");

        HBox row = new HBox(15, c1, c2, c3, c4);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private static VBox createMetricCard(String title, String value, String sub, String color) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #5C4033;");

        VBox b = new VBox(6, t, v, s);
        b.setPrefWidth(240);
        b.setPadding(new Insets(16));
        b.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    private static VBox createUpcomingSection() {
        Text title = new Text("Upcoming Maintenance & Inspection Schedule");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 19px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        upcomingContainer = new VBox(10);
        renderUpcoming();

        return new VBox(12, title, upcomingContainer);
    }

    private static void renderUpcoming() {
        upcomingContainer.getChildren().clear();

        for (ServiceRecord rec : serviceList) {
            if (!"DUE SOON".equals(rec.status)) continue;

            Text m = new Text("🚜 " + rec.machineName);
            m.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

            Text t = new Text("Task: " + rec.serviceType + "  •  Due: " + rec.nextDue);
            t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #5C4033;");

            VBox info = new VBox(2, m, t);

            Label st = new Label("DUE IN 3-7 DAYS");
            st.setStyle("-fx-background-color: #FFF3E0; -fx-text-fill: #E65100; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 10 4 10; -fx-background-radius: 4;");

            Button doneBtn = new Button("✔ Mark as Serviced");
            doneBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
            doneBtn.setOnAction(e -> {
                rec.status = "COMPLETED";
                renderUpcoming();
                renderHistory();
            });

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox row = new HBox(15, info, spacer, st, doneBtn);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(14, 18, 14, 18));
            row.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 10; -fx-border-color: #E65100; -fx-border-width: 1; -fx-border-radius: 10;");

            upcomingContainer.getChildren().add(row);
        }

        if (upcomingContainer.getChildren().isEmpty()) {
            VBox empty = new VBox(new Text("All machinery services are up to date! 🎉"));
            empty.setPadding(new Insets(15));
            empty.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 8;");
            upcomingContainer.getChildren().add(empty);
        }
    }

    private static VBox createHistorySection() {
        Text title = new Text("Maintenance Service History Log");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 19px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        historyContainer = new VBox(10);
        renderHistory();

        return new VBox(12, title, historyContainer);
    }

    private static void renderHistory() {
        historyContainer.getChildren().clear();

        for (ServiceRecord rec : serviceList) {
            if ("DUE SOON".equals(rec.status)) continue;

            Text m = new Text(rec.machineName);
            m.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

            Text t = new Text(rec.serviceType + " • Service Tech: " + rec.technician);
            t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #806A5B;");

            VBox info = new VBox(2, m, t);

            Text dt = new Text("📅 " + rec.date);
            dt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #5C4033;");

            Text cost = new Text("Cost: ₹" + String.format("%,d", rec.cost));
            cost.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

            Label st = new Label("COMPLETED");
            st.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox row = new HBox(25, info, dt, spacer, cost, st);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 10; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 10;");

            historyContainer.getChildren().add(row);
        }
    }

    private static void showLogServiceModal(StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(15);
        modal.setPrefWidth(480);
        modal.setMaxWidth(480);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFDF9; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0.3, 0, 8);");

        Text title = new Text("Log Machinery Service & Repairs");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);

        ComboBox<String> machineSelect = new ComboBox<>();
        machineSelect.getItems().addAll(
            "Mahindra 575 DI Sarpanch (45HP)",
            "John Deere 5310 PowerTech (55HP)",
            "Kartar 4000 Multi-Crop Harvester",
            "Shaktiman Semi-Champion 7ft",
            "Agri-Drone 16L Autonomous Sprayer",
            "FieldKing 9-Tyne Cultivator"
        );
        machineSelect.setValue("Mahindra 575 DI Sarpanch (45HP)");
        machineSelect.setPrefWidth(280);

        ComboBox<String> typeSelect = new ComboBox<>();
        typeSelect.getItems().addAll("Engine Oil & Filter Change", "Hydraulic System Inspection", "Blade Sharpening / Replacement", "Tire Tread & Pressure Check", "Electrical & Battery Diagnostics", "Full Annual Overhaul");
        typeSelect.setValue("Engine Oil & Filter Change");
        typeSelect.setPrefWidth(280);

        TextField costField = new TextField("2500");
        TextField techField = new TextField("Authorized Service Dealer");

        form.add(createLabel("Select Machinery:"), 0, 0);
        form.add(machineSelect, 1, 0);

        form.add(createLabel("Service Type:"), 0, 1);
        form.add(typeSelect, 1, 1);

        form.add(createLabel("Service Cost (₹):"), 0, 2);
        form.add(costField, 1, 2);

        form.add(createLabel("Service Center / Tech:"), 0, 3);
        form.add(techField, 1, 3);

        Button saveBtn = new Button("Save Service Record");
        saveBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        saveBtn.setPrefHeight(36);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        cancelBtn.setPrefHeight(36);

        saveBtn.setOnAction(e -> {
            try {
                int c = Integer.parseInt(costField.getText().trim());
                serviceList.add(0, new ServiceRecord(machineSelect.getValue(), typeSelect.getValue(), "Today", "In 90 Days", c, techField.getText().trim(), "COMPLETED"));
                renderHistory();
                root.getChildren().remove(overlay);
            } catch (Exception ignored) {}
        });

        cancelBtn.setOnAction(e -> root.getChildren().remove(overlay));

        HBox btnBox = new HBox(10, saveBtn, cancelBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        modal.getChildren().addAll(title, form, btnBox);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private static Label createLabel(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #5C4033;");
        return l;
    }
}
