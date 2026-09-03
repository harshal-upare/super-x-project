package com.desgin.view.operator;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
        jobList.clear();
        try {
            String opEmail = OperatorProfileStore.email;
            if (opEmail == null || opEmail.trim().isEmpty()) return;
            java.util.List<com.desgin.model.RentalRequestModel> list = new com.desgin.dao.RentalRequestDAO().getRequestsByOperator(opEmail);
            for (com.desgin.model.RentalRequestModel r : list) {
                int opWage = r.getOperatorAmount() > 0 ? r.getOperatorAmount() : (500 * Math.max(1, r.getDays()));
                String st = r.getStatus() != null ? r.getStatus().toUpperCase() : "SCHEDULED";
                if ("ACTIVE".equalsIgnoreCase(st)) st = "IN_PROGRESS";
                else if ("ACCEPTED".equalsIgnoreCase(st) || "PENDING".equalsIgnoreCase(st) || "CONFIRMED".equalsIgnoreCase(st)) st = "SCHEDULED";
                else if ("COMPLETED".equalsIgnoreCase(st)) st = "COMPLETED";

                jobList.add(new JobOrder(
                        r.getRequestId(),
                        "Field Operation: " + (r.getMachineryName() != null ? r.getMachineryName() : "Machinery Task"),
                        r.getFarmerName() != null ? r.getFarmerName() : "Client Farmer",
                        r.getFarmerPhone() != null ? r.getFarmerPhone() : "+91 98000 00000",
                        r.getFarmerLocation() != null ? r.getFarmerLocation() : "Field Sector",
                        r.getMachineryName() != null ? r.getMachineryName() : "Machinery Unit",
                        (r.getStartDate() != null ? r.getStartDate() : "Start") + " to " + (r.getEndDate() != null ? r.getEndDate() : "End"),
                        "₹500 / Day",
                        "₹" + String.format("%,d", opWage),
                        st
                ));
            }
        } catch (Exception ignored) {}
    }

    public static ScrollPane getJobsSection(StackPane root) {
        Text title = new Text("Field Work Orders & Job Dispatch");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text subtitle = new Text("Review work orders assigned by farmers and equipment fleet managers, accept dispatches, and track completion.");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(4, title, subtitle);

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("Search job ID, farmer name, farm sector, crop task...");
        searchField.setPrefHeight(38);
        searchField.setPrefWidth(320);
        searchField.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-background-color: white; -fx-border-color: #E2EBE5; -fx-border-radius: 8;");
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
        idText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #2D6A4F;");

        Text titleText = new Text(j.taskTitle);
        titleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text farmerText = new Text("👨‍🌾 Farmer: " + j.farmerName + "  |  📞 " + j.phone);
        farmerText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #374151;");

        Text locText = new Text("📍 Location: " + j.location);
        locText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        Text machText = new Text("🚜 Machinery: " + j.machineRequired);
        machText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Text timeText = new Text("📅 Schedule: " + j.schedule);
        timeText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #374151;");

        VBox infoBox = new VBox(4, idText, titleText, farmerText, locText, machText, timeText);

        // Wage Box
        Text wageRateText = new Text("Rate: " + j.wageRate);
        wageRateText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        Text estWageText = new Text(j.estimatedWage);
        estWageText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Label statusLabel = new Label(j.status.replace("_", " "));
        if (j.status.equals("IN_PROGRESS")) {
            statusLabel.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 10 4 10; -fx-background-radius: 4;");
        } else if (j.status.equals("COMPLETED")) {
            statusLabel.setStyle("-fx-background-color: #E0F2FE; -fx-text-fill: #0284C7; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 10 4 10; -fx-background-radius: 4;");
        } else {
            statusLabel.setStyle("-fx-background-color: #F4F9F4; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 10 4 10; -fx-background-radius: 4;");
        }

        VBox wageBox = new VBox(4, wageRateText, estWageText, statusLabel);
        wageBox.setAlignment(Pos.CENTER_RIGHT);

        Button updateBtn = new Button("🔄 Update Status");
        updateBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12 6 12;");
        updateBtn.setOnAction(e -> showStatusModal(j, root));

        Button routeBtn = new Button("📍 Farm Route");
        routeBtn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12 6 12;");
        routeBtn.setOnAction(e -> showRouteModal(j, root));

        HBox btnRow = new HBox(8, routeBtn, updateBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        VBox rightSide = new VBox(12, wageBox, btnRow);
        rightSide.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(20, infoBox, spacer, rightSide);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16, 20, 16, 20));
        row.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");

        row.setOnMouseEntered(e -> {
            row.setStyle("-fx-background-color: #F0FDF4; -fx-background-radius: 12; -fx-border-color: #2D6A4F; -fx-border-width: 1; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(27,67,50,0.15), 8, 0.2, 0, 3);");
        });
        row.setOnMouseExited(e -> {
            row.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        });

        return new VBox(row);
    }

    private static Button createFilterBtn(String text, boolean active) {
        Button btn = new Button(text);
        btn.setPrefHeight(34);
        if (active) {
            btn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14 0 14;");
        } else {
            btn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14 0 14;");
        }
        return btn;
    }

    private static void setActiveFilter(Button activeBtn, HBox filterBox) {
        for (var node : filterBox.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14 0 14;");
            }
        }
        activeBtn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14 0 14;");
    }

    private static void showStatusModal(JobOrder j, StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(460);
        modal.setMaxWidth(460);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text title = new Text("Update Job Status: " + j.id);
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text task = new Text(j.taskTitle + "\nClient: " + j.farmerName);
        task.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #374151;");

        modal.getChildren().add(title);
        modal.getChildren().add(task);

        if (j.status.equals("SCHEDULED")) {
            Label assignmentInfo = new Label("📋 New job assignment pending your response. Once accepted, the farmer will be notified.");
            assignmentInfo.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-text-fill: #374151; -fx-wrap-text: true;");

            Button acceptBtn = new Button("✔  Accept Assignment");
            acceptBtn.setMaxWidth(Double.MAX_VALUE);
            acceptBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
            acceptBtn.setOnAction(e -> {
                j.status = "IN_PROGRESS";
                renderJobs("ALL", root);
                root.getChildren().remove(overlay);
                new Thread(() -> {
                    try {
                        new com.desgin.service.BookingService().operatorAccept(j.id);
                    } catch (Exception ex) {
                        javafx.application.Platform.runLater(() -> {
                            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                            alert.setTitle("Accept Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Could not accept: " + ex.getMessage());
                            alert.showAndWait();
                        });
                    }
                }).start();
            });

            Button declineBtn = new Button("✕  Decline Assignment");
            declineBtn.setMaxWidth(Double.MAX_VALUE);
            declineBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
            declineBtn.setOnAction(e -> {
                j.status = "COMPLETED"; // hide from active list
                renderJobs("ALL", root);
                root.getChildren().remove(overlay);
                new Thread(() -> {
                    try {
                        new com.desgin.service.BookingService().operatorDecline(j.id, "Operator unavailable for this schedule.");
                    } catch (Exception ex) {
                        javafx.application.Platform.runLater(() -> {
                            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                            alert.setTitle("Decline Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Could not decline: " + ex.getMessage());
                            alert.showAndWait();
                        });
                    }
                }).start();
            });

            modal.getChildren().addAll(assignmentInfo, acceptBtn, declineBtn);
        } else if (j.status.equals("IN_PROGRESS")) {
            Button s2 = new Button("✓  Complete Job & Trigger Wage Settlement");
            s2.setStyle("-fx-background-color: #0284C7; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
            s2.setMaxWidth(Double.MAX_VALUE);
            s2.setOnAction(e -> {
                j.status = "COMPLETED";
                renderJobs("ALL", root);
                root.getChildren().remove(overlay);
                new Thread(() -> { try { new com.desgin.service.BookingService().completeBooking(j.id); } catch (Exception ignored) {} }).start();
            });
            modal.getChildren().add(s2);
        } else {
            Button s1 = new Button("▶  Start Job (In-Progress)");
            s1.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 16 8 16;");
            s1.setMaxWidth(Double.MAX_VALUE);
            s1.setOnAction(e -> {
                j.status = "IN_PROGRESS";
                renderJobs("ALL", root);
                root.getChildren().remove(overlay);
                new Thread(() -> {
                    try {
                        new com.desgin.dao.RentalRequestDAO().updateRequestStatus(j.id, "ACTIVE");
                    } catch (Exception ignored) {}
                }).start();
            });
            modal.getChildren().add(s1);
        }

        Button cancel = new Button("Close");
        cancel.setMaxWidth(Double.MAX_VALUE);
        cancel.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        cancel.setOnAction(e -> root.getChildren().remove(overlay));
        modal.getChildren().add(cancel);

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
        modal.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text title = new Text("📍 Field Navigation & Farm Route");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text desc = new Text("Destination: " + j.location + "\nEstimated Distance: 14.8 km (approx. 25 min by tractor road)\nTurn-by-turn routing available via FarmEquip GPS Navigation.");
        desc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #374151; -fx-line-spacing: 3px;");

        Button close = new Button("Close Map Directions");
        close.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        close.setOnAction(e -> root.getChildren().remove(overlay));

        modal.getChildren().addAll(title, desc, close);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }
}
