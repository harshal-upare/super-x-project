package com.desgin.view.operator;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OperatorMaintenance {

    public static class DefectTicket {
        public String ticketId;
        public String machine;
        public String issueTitle;
        public String category;
        public String severity; // "CRITICAL", "MODERATE", "LOW"
        public String reportedDate;
        public String status; // "RESOLVED", "IN_REPAIR", "OPEN"

        public DefectTicket(String ticketId, String machine, String issueTitle, String category, String severity, String reportedDate, String status) {
            this.ticketId = ticketId;
            this.machine = machine;
            this.issueTitle = issueTitle;
            this.category = category;
            this.severity = severity;
            this.reportedDate = reportedDate;
            this.status = status;
        }
    }

    private static List<DefectTicket> ticketList = new ArrayList<>();
    private static VBox ticketContainer;

    static {
        initTickets();
    }

    private static void initTickets() {
        if (!ticketList.isEmpty()) return;
        ticketList.add(new DefectTicket("#SOS-402", "Preet 987 Combine", "Cutter bar belt tension adjustment & blade realignment", "Implements & Knives", "MODERATE", "12 Aug 2026", "IN_REPAIR"));
        ticketList.add(new DefectTicket("#SOS-395", "Mahindra 575 DI", "Hydraulic 3-point lift seal replacement & fluid top-up", "Hydraulics", "RESOLVED", "04 Aug 2026", "RESOLVED"));
        ticketList.add(new DefectTicket("#SOS-382", "John Deere 5310", "Front right tire puncture repair & valve stem fix", "Tires & Steering", "RESOLVED", "28 Jul 2026", "RESOLVED"));
    }

    public static ScrollPane getMaintenanceSection(StackPane root) {
        Text title = new Text("Machinery Maintenance & Field Defect SOS");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text subtitle = new Text("Report field breakdowns, request urgent mobile mechanic dispatch, and monitor scheduled service countdowns.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #806A5B;");

        VBox titleBox = new VBox(4, title, subtitle);

        Button reportSosBtn = new Button("⚠️  Report Field Breakdown / Issue");
        reportSosBtn.setPrefHeight(42);
        reportSosBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 18 0 18;");
        reportSosBtn.setOnAction(e -> showReportModal(root));

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox topBar = new HBox(titleBox, topSpacer, reportSosBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // 4 Summary Metrics
        HBox summaryMetrics = createSummaryMetrics();

        // Scheduled Maintenance Countdown Cards
        VBox countdownSection = createCountdownSection();

        // Defect Tickets Table
        VBox ticketsSection = createTicketsSection();

        VBox content = new VBox(22, topBar, summaryMetrics, countdownSection, ticketsSection);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static HBox createSummaryMetrics() {
        VBox c1 = createMetricCard("🚜 Assigned Fleet", "5 Machines", "All units telemetry-linked", "#4A2C20");
        VBox c2 = createMetricCard("✓ Field Operational", "4 Ready", "80% fleet available for duty", "#2E7D32");
        VBox c3 = createMetricCard("🛠 In Workshop / Tune", "1 Under Service", "Preet 987 Harvester", "#E65100");
        VBox c4 = createMetricCard("🚨 Active Field SOS", "0 Active", "No emergency breakdown", "#2E7D32");

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

    private static VBox createCountdownSection() {
        Text title = new Text("Scheduled Maintenance & Service Intervals");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        VBox s1 = createServiceRow("John Deere 5310 4WD", "Engine Oil & Filter 250-Hr Service", "Due in 42 Engine Hours", 0.83, "#2E7D32");
        VBox s2 = createServiceRow("Preet 987 Combine Harvester", "Cutter Bar Knife Sharpening & Chain Lubrication", "Due in 18 Engine Hours (Attention Required)", 0.93, "#E65100");
        VBox s3 = createServiceRow("Mahindra 575 DI Bhoomiputra", "Hydraulic Fluid & Transmission Filter Flush", "Due in 110 Engine Hours", 0.56, "#8B6F47");

        VBox card = new VBox(12, title, s1, s2, s3);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createServiceRow(String machine, String serviceDesc, String dueText, double progress, String barColor) {
        Text m = new Text("🚜 " + machine);
        m.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text d = new Text(serviceDesc);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        VBox left = new VBox(2, m, d);

        Text due = new Text(dueText);
        due.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: " + barColor + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(left, spacer, due);
        top.setAlignment(Pos.CENTER_LEFT);

        ProgressBar pb = new ProgressBar(progress);
        pb.setPrefWidth(980);
        pb.setPrefHeight(8);
        pb.setStyle("-fx-accent: " + barColor + ";");

        return new VBox(4, top, pb);
    }

    private static VBox createTicketsSection() {
        Text title = new Text("Defect Reports & Repair Tickets");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        ticketContainer = new VBox(10);
        renderTickets();

        return new VBox(12, title, ticketContainer);
    }

    private static void renderTickets() {
        ticketContainer.getChildren().clear();

        for (DefectTicket t : ticketList) {
            Text id = new Text(t.ticketId);
            id.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #8B6F47;");

            Text machine = new Text(t.machine + " • " + t.issueTitle);
            machine.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

            Text dt = new Text("Category: " + t.category + "  |  Reported: " + t.reportedDate);
            dt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #806A5B;");

            VBox info = new VBox(2, id, machine, dt);

            Label sevLabel = new Label(t.severity);
            if (t.severity.equals("CRITICAL")) {
                sevLabel.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #B91C1C; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");
            } else {
                sevLabel.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #D97706; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");
            }

            Label statusLabel = new Label(t.status);
            if (t.status.equals("RESOLVED")) {
                statusLabel.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");
            } else {
                statusLabel.setStyle("-fx-background-color: #E0F2FE; -fx-text-fill: #0284C7; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");
            }

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox row = new HBox(20, info, spacer, sevLabel, statusLabel);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 10; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 10;");

            ticketContainer.getChildren().add(row);
        }
    }

    private static void showReportModal(StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(480);
        modal.setMaxWidth(480);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFDF9; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0.3, 0, 8);");

        Text title = new Text("⚠️ Report Machine Breakdown / Field Issue");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        ComboBox<String> machineSelect = new ComboBox<>();
        machineSelect.getItems().addAll("John Deere 5310 4WD", "Mahindra 575 DI", "Preet 987 Harvester", "Hexacopter Agri Drone");
        machineSelect.setValue("John Deere 5310 4WD");
        machineSelect.setPrefWidth(260);

        ComboBox<String> catSelect = new ComboBox<>();
        catSelect.getItems().addAll("Engine / Transmission", "Hydraulic System", "Implements & PTO", "Tires & Steering", "Electrical & Sensor");
        catSelect.setValue("Hydraulic System");
        catSelect.setPrefWidth(260);

        ComboBox<String> sevSelect = new ComboBox<>();
        sevSelect.getItems().addAll("CRITICAL (Machine Inoperable)", "MODERATE (Needs Attention)", "LOW (Routine Fix)");
        sevSelect.setValue("MODERATE (Needs Attention)");
        sevSelect.setPrefWidth(260);

        TextField descField = new TextField();
        descField.setPromptText("Describe the symptom or failure...");
        descField.setPrefHeight(36);

        form.add(createLabel("Machine:"), 0, 0);
        form.add(machineSelect, 1, 0);

        form.add(createLabel("Category:"), 0, 1);
        form.add(catSelect, 1, 1);

        form.add(createLabel("Severity:"), 0, 2);
        form.add(sevSelect, 1, 2);

        form.add(createLabel("Description:"), 0, 3);
        form.add(descField, 1, 3);

        Button submitBtn = new Button("Submit Urgent SOS Ticket");
        submitBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        submitBtn.setOnAction(e -> {
            String desc = descField.getText().trim().isEmpty() ? "Field inspection required" : descField.getText().trim();
            String sev = sevSelect.getValue().startsWith("CRITICAL") ? "CRITICAL" : "MODERATE";
            ticketList.add(0, new DefectTicket("#SOS-" + (405 + ticketList.size()), machineSelect.getValue(), desc, catSelect.getValue(), sev, "Today (Just Now)", "OPEN"));
            renderTickets();
            root.getChildren().remove(overlay);
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #5C4033; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> root.getChildren().remove(overlay));

        HBox btnBox = new HBox(10, submitBtn, cancelBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        modal.getChildren().addAll(title, form, btnBox);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private static Label createLabel(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-text-fill: #5C4033;");
        return l;
    }
}
