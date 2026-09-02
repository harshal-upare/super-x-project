package com.desgin.view.operator;

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

public class OperatorJobs {

    public static class JobOrder {
        public String id;
        public String taskTitle;
        public String farmerName;
        public String phone;
        public String location;
        public String machineRequired;
        public String schedule;
        public String wageRate;
        public String estimatedWage;
        public String status; // "IN_PROGRESS", "SCHEDULED", "CONFIRMED", "COMPLETED"

        public JobOrder(String id, String taskTitle, String farmerName, String phone, String location, String machineRequired, String schedule, String wageRate, String estimatedWage, String status) {
            this.id = id;
            this.taskTitle = taskTitle;
            this.farmerName = farmerName;
            this.phone = phone;
            this.location = location;
            this.machineRequired = machineRequired;
            this.schedule = schedule;
            this.wageRate = wageRate;
            this.estimatedWage = estimatedWage;
            this.status = status;
        }
    }

    private static List<JobOrder> jobList = new ArrayList<>();
    private static VBox jobContainer;

    static {
        initJobs();
    }

    private static void initJobs() {
        if (!jobList.isEmpty()) return;
        jobList.add(new JobOrder("JOB-7821", "Deep Tillage & Soil Preparation (14.0 Acres)", "Balasaheb Shirole", "+91 98231 44552", "Plot B, Sector 4, Baramati, Pune", "John Deere 5310 4WD + 7ft Rotavator", "Today, 08:00 AM - 04:30 PM", "₹400 / Acre", "₹5,600", "IN_PROGRESS"));
        jobList.add(new JobOrder("JOB-7830", "Wheat Harvesting & Grain Threshing (18.0 Acres)", "Vikas More", "+91 98502 11234", "Gat No. 112, Daund Road, Pune", "Preet 987 Multicrop Harvester", "Tomorrow, 07:00 AM - 05:00 PM", "₹450 / Acre", "₹8,100", "CONFIRMED"));
        jobList.add(new JobOrder("JOB-7842", "Laser Land Leveling & Grading (8.0 Acres)", "Kiran Bhosale", "+91 94220 89761", "Shiraswadi Farm, Baramati", "Mahindra 575 DI + Laser Unit", "16 Aug 2026, 08:30 AM", "₹700 / Hour (~6 hrs)", "₹4,200", "SCHEDULED"));
        jobList.add(new JobOrder("JOB-7855", "Micronutrient Spraying for Sugarcane (12.0 Acres)", "Ganesh Jadhav", "+91 97631 55670", "Hol Village, Baramati Sub-District", "Hexacopter Agri Spray Drone", "17 Aug 2026, 06:00 AM", "₹350 / Acre", "₹4,200", "SCHEDULED"));
        jobList.add(new JobOrder("JOB-7790", "Paddy Nursery Bed Preparation (6.5 Acres)", "Pravin Jagtap", "+91 98904 33211", "Bhigwan Basin Road, Pune", "Mahindra 575 DI + Cultivator", "12 Aug 2026 (Completed)", "₹380 / Acre", "₹2,470", "COMPLETED"));
    }

    public static ScrollPane getJobsSection(StackPane root) {
        Text title = new Text("Field Work Orders & Job Dispatch");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text subtitle = new Text("Review work orders assigned by farmers and equipment fleet managers, accept dispatches, and track completion.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #806A5B;");

        VBox titleBox = new VBox(4, title, subtitle);

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("Search job ID, farmer name, farm sector, crop task...");
        searchField.setPrefHeight(38);
        searchField.setPrefWidth(320);
        searchField.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-background-color: white; -fx-border-color: #D8C7B5; -fx-border-radius: 8;");
        searchField.textProperty().addListener((obs, oldV, newV) -> filterSearch(newV, root));

        HBox topBar = new HBox(titleBox, new Region(), searchField);
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Filter Tabs
        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        Button allBtn = createFilterBtn("All Jobs (5)", true);
        Button inProgressBtn = createFilterBtn("In-Progress (1)", false);
        Button upcomingBtn = createFilterBtn("Scheduled / Confirmed (3)", false);
        Button completedBtn = createFilterBtn("Completed (1)", false);

        allBtn.setOnAction(e -> { setActiveFilter(allBtn, filterBox); renderJobs("ALL", root); });
        inProgressBtn.setOnAction(e -> { setActiveFilter(inProgressBtn, filterBox); renderJobs("IN_PROGRESS", root); });
        upcomingBtn.setOnAction(e -> { setActiveFilter(upcomingBtn, filterBox); renderJobs("SCHEDULED", root); });
        completedBtn.setOnAction(e -> { setActiveFilter(completedBtn, filterBox); renderJobs("COMPLETED", root); });

        filterBox.getChildren().addAll(allBtn, inProgressBtn, upcomingBtn, completedBtn);

        jobContainer = new VBox(14);
        renderJobs("ALL", root);

        VBox content = new VBox(20, topBar, filterBox, jobContainer);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static void renderJobs(String filter, StackPane root) {
        jobContainer.getChildren().clear();

        for (JobOrder j : jobList) {
            if (!filter.equals("ALL")) {
                if (filter.equals("SCHEDULED") && !j.status.equals("SCHEDULED") && !j.status.equals("CONFIRMED")) {
                    continue;
                } else if (!filter.equals("SCHEDULED") && !j.status.equals(filter)) {
                    continue;
                }
            }

            VBox card = createJobCard(j, root);
            jobContainer.getChildren().add(card);
        }
    }

    private static void filterSearch(String query, StackPane root) {
        jobContainer.getChildren().clear();
        String q = query.toLowerCase().trim();

        for (JobOrder j : jobList) {
            if (j.id.toLowerCase().contains(q) ||
                j.taskTitle.toLowerCase().contains(q) ||
                j.farmerName.toLowerCase().contains(q) ||
                j.location.toLowerCase().contains(q) ||
                j.machineRequired.toLowerCase().contains(q)) {
                jobContainer.getChildren().add(createJobCard(j, root));
            }
        }
    }

    private static VBox createJobCard(JobOrder j, StackPane root) {
        Text idText = new Text(j.id);
        idText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #8B6F47;");

        Text titleText = new Text(j.taskTitle);
        titleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text farmerText = new Text("👨‍🌾 Farmer: " + j.farmerName + "  |  📞 " + j.phone);
        farmerText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #5C4033;");

        Text locText = new Text("📍 Location: " + j.location);
        locText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        Text machText = new Text("🚜 Machinery: " + j.machineRequired);
        machText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Text timeText = new Text("📅 Schedule: " + j.schedule);
        timeText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #5C4033;");

        VBox infoBox = new VBox(4, idText, titleText, farmerText, locText, machText, timeText);

        // Wage Box
        Text wageRateText = new Text("Rate: " + j.wageRate);
        wageRateText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #806A5B;");

        Text estWageText = new Text(j.estimatedWage);
        estWageText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Label statusLabel = new Label(j.status.replace("_", " "));
        if (j.status.equals("IN_PROGRESS")) {
            statusLabel.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 10 4 10; -fx-background-radius: 4;");
        } else if (j.status.equals("COMPLETED")) {
            statusLabel.setStyle("-fx-background-color: #E0F2FE; -fx-text-fill: #0284C7; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 10 4 10; -fx-background-radius: 4;");
        } else {
            statusLabel.setStyle("-fx-background-color: #EDE3D5; -fx-text-fill: #5C4033; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 10 4 10; -fx-background-radius: 4;");
        }

        VBox wageBox = new VBox(4, wageRateText, estWageText, statusLabel);
        wageBox.setAlignment(Pos.CENTER_RIGHT);

        // Buttons
        Button updateBtn = new Button("🔄 Update Status");
        updateBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12 6 12;");
        updateBtn.setOnAction(e -> showStatusModal(j, root));

        Button routeBtn = new Button("📍 Farm Route");
        routeBtn.setStyle("-fx-background-color: #E4D3C2; -fx-text-fill: #4A2C20; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12 6 12;");
        routeBtn.setOnAction(e -> showRouteModal(j, root));

        Button callBtn = new Button("📞 Call");
        callBtn.setStyle("-fx-background-color: #8B6F47; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12 6 12;");
        callBtn.setOnAction(e -> showCallModal(j, root));

        HBox btnRow = new HBox(8, routeBtn, callBtn, updateBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        VBox rightSide = new VBox(12, wageBox, btnRow);
        rightSide.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(20, infoBox, spacer, rightSide);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16, 20, 16, 20));
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

    private static void showStatusModal(JobOrder j, StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(460);
        modal.setMaxWidth(460);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFDF9; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text title = new Text("Update Job Status: " + j.id);
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text task = new Text(j.taskTitle + "\nClient: " + j.farmerName);
        task.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #5C4033;");

        Button s1 = new Button("▶  Start Job (In-Progress)");
        s1.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
        s1.setMaxWidth(Double.MAX_VALUE);
        s1.setOnAction(e -> { j.status = "IN_PROGRESS"; renderJobs("ALL", root); root.getChildren().remove(overlay); });

        Button s2 = new Button("✓  Complete Job & Trigger Wage Settlement");
        s2.setStyle("-fx-background-color: #0284C7; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
        s2.setMaxWidth(Double.MAX_VALUE);
        s2.setOnAction(e -> { j.status = "COMPLETED"; renderJobs("ALL", root); root.getChildren().remove(overlay); });

        Button cancel = new Button("Close");
        cancel.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        cancel.setOnAction(e -> root.getChildren().remove(overlay));

        modal.getChildren().addAll(title, task, s1, s2, cancel);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private static void showRouteModal(JobOrder j, StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(460);
        modal.setMaxWidth(460);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFDF9; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text title = new Text("📍 Field Navigation & Farm Route");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text desc = new Text("Destination: " + j.location + "\nEstimated Distance: 14.8 km (approx. 25 min by tractor road)\nTurn-by-turn routing available via FarmEquip GPS Navigation.");
        desc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #5C4033; -fx-line-spacing: 3px;");

        Button close = new Button("Close Map Directions");
        close.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        close.setOnAction(e -> root.getChildren().remove(overlay));

        modal.getChildren().addAll(title, desc, close);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private static void showCallModal(JobOrder j, StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(420);
        modal.setMaxWidth(420);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFDF9; -fx-background-radius: 14; -fx-border-color: #D8C7B5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text title = new Text("📞 Contact Client Farmer");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text farmerInfo = new Text("Farmer Name: " + j.farmerName + "\nMobile Phone: " + j.phone + "\nFarm: " + j.location);
        farmerInfo.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #5C4033; -fx-line-spacing: 4px;");

        Button callBtn = new Button("Dial " + j.phone);
        callBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");

        Button close = new Button("Close");
        close.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        close.setOnAction(e -> root.getChildren().remove(overlay));

        HBox btns = new HBox(10, callBtn, close);
        btns.setAlignment(Pos.CENTER_RIGHT);

        modal.getChildren().addAll(title, farmerInfo, btns);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }
}
