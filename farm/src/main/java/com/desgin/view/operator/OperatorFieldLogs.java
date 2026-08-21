package com.desgin.view.operator;

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

public class OperatorFieldLogs {

    public static class FieldLogEntry {
        public String logId;
        public String date;
        public String machine;
        public String farmer;
        public double startMeter;
        public double endMeter;
        public double hours;
        public double acres;
        public double fuelUsed;
        public int wage;
        public String status;

        public FieldLogEntry(String logId, String date, String machine, String farmer, double startMeter, double endMeter, double hours, double acres, double fuelUsed, int wage, String status) {
            this.logId = logId;
            this.date = date;
            this.machine = machine;
            this.farmer = farmer;
            this.startMeter = startMeter;
            this.endMeter = endMeter;
            this.hours = hours;
            this.acres = acres;
            this.fuelUsed = fuelUsed;
            this.wage = wage;
            this.status = status;
        }
    }

    private static List<FieldLogEntry> logsList = new ArrayList<>();
    private static VBox logsContainer;
    private static Text totalHoursText;
    private static Text totalAcresText;
    private static double totalHours = 148.5;
    private static double totalAcres = 284.0;

    static {
        initLogs();
    }

    private static void initLogs() {
        if (!logsList.isEmpty()) return;
        logsList.add(new FieldLogEntry("#LOG-1048", "13 Aug 2026", "John Deere 5310 4WD", "Balasaheb Shirole", 1240.5, 1247.0, 6.5, 12.0, 24.5, 2800, "VERIFIED"));
        logsList.add(new FieldLogEntry("#LOG-1042", "11 Aug 2026", "Mahindra 575 DI", "Suresh Deshmukh", 890.0, 894.0, 4.0, 8.5, 14.0, 1900, "VERIFIED"));
        logsList.add(new FieldLogEntry("#LOG-1037", "09 Aug 2026", "Preet 987 Combine Harvester", "Vikas More", 342.0, 350.0, 8.0, 15.0, 42.0, 6000, "VERIFIED"));
        logsList.add(new FieldLogEntry("#LOG-1025", "06 Aug 2026", "Hexacopter Spray Drone", "Kiran Bhosale", 82.5, 85.0, 2.5, 10.0, 4.0, 3500, "VERIFIED"));
        logsList.add(new FieldLogEntry("#LOG-1014", "03 Aug 2026", "Mahindra 575 DI + Laser Unit", "Ganesh Jadhav", 878.0, 883.5, 5.5, 7.0, 18.5, 3850, "VERIFIED"));
    }

    public static ScrollPane getLogsSection(StackPane root) {
        Text title = new Text("Field Work Logs & Engine Hour Timesheet");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text subtitle = new Text("Digital machine hour logbook, acreage tracking, fuel consumption logs, and daily field operation timesheets.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #806A5B;");

        VBox titleBox = new VBox(4, title, subtitle);

        Button newLogBtn = new Button("✍  Log New Field Session");
        newLogBtn.setPrefHeight(40);
        newLogBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 18 0 18;");
        newLogBtn.setOnAction(e -> showNewLogModal(root));

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox topBar = new HBox(titleBox, topSpacer, newLogBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Summary Metric Row
        HBox metricRow = createSummaryMetrics();

        // Logs List Table
        VBox tableSection = createLogsTable();

        VBox content = new VBox(22, topBar, metricRow, tableSection);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static HBox createSummaryMetrics() {
        totalHoursText = new Text(String.format("%.1f hrs", totalHours));
        totalHoursText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #4A2C20;");
        VBox c1 = createCustomCard("⏱ Total Hours Logged", totalHoursText, "Month of August 2026");

        totalAcresText = new Text(String.format("%.1f Acres", totalAcres));
        totalAcresText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #2E7D32;");
        VBox c2 = createCustomCard("🌾 Total Land Covered", totalAcresText, "Across 24 Field Operations");

        VBox c3 = createMetricCard("⛽ Avg. Fuel Burn Rate", "3.8 L / Hour", "Efficient field operation", "#5C4033");
        VBox c4 = createMetricCard("✓ Verified Sessions", "24 Completed", "100% Client approval rate", "#2E7D32");

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

    private static VBox createCustomCard(String title, Text valText, String sub) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #5C4033;");

        VBox b = new VBox(6, t, valText, s);
        b.setPrefWidth(240);
        b.setPadding(new Insets(16));
        b.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 12; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    private static VBox createLogsTable() {
        Text title = new Text("Operating History & Hour Meter Log Entries");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        logsContainer = new VBox(10);
        renderLogs();

        return new VBox(12, title, logsContainer);
    }

    private static void renderLogs() {
        logsContainer.getChildren().clear();

        for (FieldLogEntry e : logsList) {
            Text id = new Text(e.logId);
            id.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #8B6F47;");

            Text machineText = new Text("🚜 " + e.machine);
            machineText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

            Text details = new Text("Farmer: " + e.farmer + "  |  Date: " + e.date + "  |  Hour Meter: " + e.startMeter + " → " + e.endMeter);
            details.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

            VBox infoBox = new VBox(3, id, machineText, details);

            Text hrs = new Text(String.format("%.1f hrs", e.hours));
            hrs.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

            Text acres = new Text(String.format("%.1f Acres", e.acres));
            acres.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #5C4033;");

            Text fuel = new Text(String.format("⛽ %.1f L", e.fuelUsed));
            fuel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

            Text wage = new Text("₹" + String.format("%,d", e.wage));
            wage.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

            Label statusLabel = new Label(e.status);
            statusLabel.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox row = new HBox(25, infoBox, spacer, hrs, acres, fuel, wage, statusLabel);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setStyle("-fx-background-color: #F5EFE6; -fx-background-radius: 10; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 10;");

            logsContainer.getChildren().add(row);
        }
    }

    private static void showNewLogModal(StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(480);
        modal.setMaxWidth(480);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFDF9; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text title = new Text("Log New Field Session & Hours");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(10);

        ComboBox<String> machineSelect = new ComboBox<>();
        machineSelect.getItems().addAll("John Deere 5310 4WD", "Mahindra 575 DI", "Preet 987 Harvester", "Hexacopter Agri Drone");
        machineSelect.setValue("John Deere 5310 4WD");
        machineSelect.setPrefWidth(260);

        TextField farmerField = new TextField("Balasaheb Shirole");
        TextField startField = new TextField("1247.0");
        TextField endField = new TextField("1251.5");
        TextField acresField = new TextField("9.0");
        TextField fuelField = new TextField("18.0");

        form.add(createLabel("Select Machine:"), 0, 0);
        form.add(machineSelect, 1, 0);

        form.add(createLabel("Client Farmer:"), 0, 1);
        form.add(farmerField, 1, 1);

        form.add(createLabel("Start Hour Meter:"), 0, 2);
        form.add(startField, 1, 2);

        form.add(createLabel("End Hour Meter:"), 0, 3);
        form.add(endField, 1, 3);

        form.add(createLabel("Acres Covered:"), 0, 4);
        form.add(acresField, 1, 4);

        form.add(createLabel("Fuel Consumed (L):"), 0, 5);
        form.add(fuelField, 1, 5);

        Button submitBtn = new Button("Submit Verified Log");
        submitBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        submitBtn.setOnAction(e -> {
            try {
                double s = Double.parseDouble(startField.getText().trim());
                double end = Double.parseDouble(endField.getText().trim());
                double ac = Double.parseDouble(acresField.getText().trim());
                double fl = Double.parseDouble(fuelField.getText().trim());
                double h = end - s;
                int wg = (int) (ac * 400);

                totalHours += h;
                totalAcres += ac;
                totalHoursText.setText(String.format("%.1f hrs", totalHours));
                totalAcresText.setText(String.format("%.1f Acres", totalAcres));

                logsList.add(0, new FieldLogEntry("#LOG-" + (1050 + logsList.size()), "Today (Live)", machineSelect.getValue(), farmerField.getText(), s, end, h, ac, fl, wg, "VERIFIED"));
                renderLogs();
                root.getChildren().remove(overlay);
            } catch (Exception ignored) {}
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
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
