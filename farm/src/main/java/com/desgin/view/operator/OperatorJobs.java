package com.desgin.view.operator;

import java.util.ArrayList;
import java.util.List;

import com.desgin.dao.RentalRequestDAO;
import com.desgin.model.RentalRequestModel;

import javafx.application.Platform;
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
        public String farmerEmail;
        public String location;
        public String machineRequired;
        public String schedule;
        public String wageRate;
        public String estimatedWage;
        public int wageValue;
        public String status; // "PENDING", "ACCEPTED", "IN_PROGRESS", "COMPLETED", "REJECTED"
        public String paymentStatus; // "PENDING", "PAID"
        public long shiftStartTime;
        public long shiftDurationMillis;

        public Boolean unlockedRef = false;

        public JobOrder(String id, String taskTitle, String farmerName, String phone, String farmerEmail,
                        String location, String machineRequired, String schedule, String wageRate,
                        String estimatedWage, int wageValue, String status, String paymentStatus,
                        long shiftStartTime, long shiftDurationMillis) {
            this.id = id;
            this.taskTitle = taskTitle;
            this.farmerName = farmerName;
            this.phone = phone;
            this.farmerEmail = farmerEmail;
            this.location = location;
            this.machineRequired = machineRequired;
            this.schedule = schedule;
            this.wageRate = wageRate;
            this.estimatedWage = estimatedWage;
            this.wageValue = wageValue;
            this.status = status;
            this.paymentStatus = (paymentStatus != null && !paymentStatus.isEmpty()) ? paymentStatus : "PENDING";
            this.shiftStartTime = shiftStartTime;
            this.shiftDurationMillis = shiftDurationMillis > 0 ? shiftDurationMillis : (3 * 3600 * 1000L);
        }
    }

    private static final List<JobOrder> jobList = new ArrayList<>();
    private static final java.util.Map<String, Label> activeCountdownLabels = new java.util.HashMap<>();
    private static javafx.animation.Timeline countdownTimeline;
    private static VBox jobContainer;
    private static Label feedbackBanner;
    private static String currentFilter = "ALL";

    private static Button allBtn;
    private static Button activeBtn;
    private static Button pendingBtn;
    private static Button completedBtn;
    private static Button cancelledBtn;
    private static HBox filterBox;

    public static void loadJobsFromFirestore(StackPane root) {
        new Thread(() -> {
            try {
                String opEmail = OperatorProfileStore.email;
                List<JobOrder> loaded = new ArrayList<>();

                if (opEmail != null && !opEmail.trim().isEmpty()) {
                    List<RentalRequestModel> list = new RentalRequestDAO().getRequestsByOperator(opEmail);
                    for (RentalRequestModel r : list) {
                        int opWage = r.getOperatorAmount() > 0 ? r.getOperatorAmount() : (600 * Math.max(1, r.getDays()));
                        String opSt = r.getOperatorStatus() != null ? r.getOperatorStatus().toUpperCase().trim() : "";
                        String reqSt = r.getStatus() != null ? r.getStatus().toUpperCase().trim() : "";
                        String paySt = r.getPaymentStatus() != null ? r.getPaymentStatus().toUpperCase().trim() : "PENDING";

                        long shiftStart = r.getShiftStartTime();
                        long shiftDur = r.getShiftDurationMillis() > 0 ? r.getShiftDurationMillis() : (3 * 3600 * 1000L);

                        boolean isCompleted = "COMPLETED".equalsIgnoreCase(opSt) || "COMPLETED".equalsIgnoreCase(reqSt);
                        boolean isInProgress = !isCompleted && ("IN_PROGRESS".equalsIgnoreCase(reqSt) || "IN_PROGRESS".equalsIgnoreCase(opSt) || shiftStart > 0);

                        if (!isCompleted && !isInProgress) {
                            continue; // Unapproved / pending requests are handled in Job Requests only!
                        }

                        String finalSt = isCompleted ? "COMPLETED" : "IN_PROGRESS";

                        loaded.add(new JobOrder(
                                r.getRequestId() != null ? r.getRequestId() : "REQ-" + System.currentTimeMillis() % 10000,
                                r.getMachineryName() != null ? r.getMachineryName() : "Tractor Tillage & Operation",
                                r.getFarmerName() != null ? r.getFarmerName() : "Farmer Client",
                                r.getFarmerPhone() != null ? r.getFarmerPhone() : "+91 98220 12345",
                                r.getFarmerEmail() != null ? r.getFarmerEmail() : "farmer@farmmail.com",
                                r.getFarmerLocation() != null ? r.getFarmerLocation() : "Baramati Sector 4 (Plot B)",
                                r.getMachineryName() != null ? r.getMachineryName() : "Tractor Unit",
                                (r.getStartDate() != null ? r.getStartDate() : "Today") + " • " + (r.getDays() > 0 ? r.getDays() + " Days" : "Standard Shift"),
                                "₹600 / Day",
                                "₹" + String.format("%,d", opWage),
                                opWage,
                                finalSt,
                                paySt,
                                shiftStart,
                                shiftDur
                        ));
                    }
                }

                Platform.runLater(() -> {
                    jobList.clear();
                    jobList.addAll(loaded);
                    updateFilterButtonLabels();
                    renderJobs(currentFilter, root);
                    ensureCountdownRunning(root);
                });
            } catch (Exception ignored) {}
        }).start();
    }

    private static void updateFilterButtonLabels() {
        long allCount = jobList.size();
        long activeCount = jobList.stream().filter(j -> "IN_PROGRESS".equalsIgnoreCase(j.status)).count();
        long completedCount = jobList.stream().filter(j -> "COMPLETED".equalsIgnoreCase(j.status)).count();

        if (allBtn != null) allBtn.setText("All (" + allCount + ")");
        if (activeBtn != null) activeBtn.setText("🟢 Active Shifts (" + activeCount + ")");
        if (completedBtn != null) completedBtn.setText("✓ Completed (" + completedCount + ")");
    }

    public static ScrollPane getJobsSection(StackPane root) {
        feedbackBanner = new Label();
        feedbackBanner.setVisible(false);
        feedbackBanner.setManaged(false);

        // Full-Width Search Bar
        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Search field jobs by ID, farmer name, farm plot location, or machinery...");
        searchField.setPrefHeight(44);
        searchField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchField.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #C2E0CE;" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1.2;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #1B4332;" +
                "-fx-padding: 0 16px;" +
                "-fx-prompt-text-fill: #9CA3AF;");
        searchField.textProperty().addListener((obs, oldV, newV) -> filterSearch(newV, root));

        // Filter Tabs: All | Active Shifts | Completed
        filterBox = new HBox(8);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        allBtn = createFilterBtn("All", true);
        activeBtn = createFilterBtn("🟢 Active Shifts", false);
        completedBtn = createFilterBtn("✓ Completed", false);

        allBtn.setOnAction(e -> { currentFilter = "ALL"; setActiveFilter(allBtn); renderJobs("ALL", root); });
        activeBtn.setOnAction(e -> { currentFilter = "ACTIVE"; setActiveFilter(activeBtn); renderJobs("ACTIVE", root); });
        completedBtn.setOnAction(e -> { currentFilter = "COMPLETED"; setActiveFilter(completedBtn); renderJobs("COMPLETED", root); });

        filterBox.getChildren().addAll(allBtn, activeBtn, completedBtn);

        jobContainer = new VBox(14);
        jobContainer.setMaxWidth(Double.MAX_VALUE);

        loadJobsFromFirestore(root);

        VBox content = new VBox(14, feedbackBanner, searchField, filterBox, jobContainer);
        content.setPadding(new Insets(20, 28, 30, 28));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static void renderJobs(String filter, StackPane root) {
        if (jobContainer == null) return;
        jobContainer.getChildren().clear();

        List<JobOrder> filtered = new ArrayList<>();
        for (JobOrder j : jobList) {
            if ("ALL".equalsIgnoreCase(filter)) {
                filtered.add(j);
            } else if ("ACTIVE".equalsIgnoreCase(filter) && ("ACCEPTED".equalsIgnoreCase(j.status) || "IN_PROGRESS".equalsIgnoreCase(j.status))) {
                filtered.add(j);
            } else if ("PENDING".equalsIgnoreCase(filter) && "PENDING".equalsIgnoreCase(j.status)) {
                filtered.add(j);
            } else if ("COMPLETED".equalsIgnoreCase(filter) && "COMPLETED".equalsIgnoreCase(j.status)) {
                filtered.add(j);
            } else if ("CANCELLED".equalsIgnoreCase(filter) && ("REJECTED".equalsIgnoreCase(j.status) || "CANCELLED".equalsIgnoreCase(j.status))) {
                filtered.add(j);
            }
        }

        if (filtered.isEmpty()) {
            VBox emptyBox = new VBox(10);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(40));
            emptyBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-width: 1px; -fx-border-radius: 12px;");

            Text icon = new Text("🚜");
            icon.setStyle("-fx-font-size: 36px;");

            Text t = new Text("No Live Shifts in This View");
            t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text s = new Text("All farmer hiring requests appear under Job Requests. Once approved and started, shifts appear here.");
            s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #6B7280;");

            Button gotoRequestsBtn = new Button("📥  Check Incoming Job Requests ➔");
            gotoRequestsBtn.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 8 16;");
            gotoRequestsBtn.setOnAction(e -> OperatorLeftSideBar.navigateToJobRequests());

            emptyBox.getChildren().addAll(icon, t, s, gotoRequestsBtn);
            jobContainer.getChildren().add(emptyBox);
            return;
        }

        for (JobOrder j : filtered) {
            VBox card = createJobCard(j, root);
            jobContainer.getChildren().add(card);
        }
    }

    private static void filterSearch(String query, StackPane root) {
        if (jobContainer == null) return;
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

    private static void ensureCountdownRunning(StackPane root) {
        if (countdownTimeline != null) return;
        countdownTimeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), ev -> {
                boolean unlockNeeded = false;
                for (JobOrder j : jobList) {
                    if ("IN_PROGRESS".equalsIgnoreCase(j.status)) {
                        Label lbl = activeCountdownLabels.get(j.id);
                        if (lbl != null) {
                            lbl.setText(formatRemainingTime(j));
                        }
                        if (j.shiftStartTime > 0) {
                            long elapsed = System.currentTimeMillis() - j.shiftStartTime;
                            long remaining = Math.max(0, j.shiftDurationMillis - elapsed);
                            if (remaining == 0 && !Boolean.TRUE.equals(j.unlockedRef)) {
                                j.unlockedRef = true;
                                unlockNeeded = true;
                            }
                        }
                    }
                }
                if (unlockNeeded && root != null) {
                    renderJobs(currentFilter, root);
                }
            })
        );
        countdownTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        countdownTimeline.play();
    }

    private static String formatRemainingTime(JobOrder j) {
        if (j.shiftStartTime <= 0) return "Starting Shift...";
        long elapsed = System.currentTimeMillis() - j.shiftStartTime;
        long remaining = Math.max(0, j.shiftDurationMillis - elapsed);
        if (remaining == 0) {
            return "✓ Shift Time Completed (00h : 00m : 00s)";
        }
        long hours = remaining / (3600 * 1000);
        long mins = (remaining % (3600 * 1000)) / (60 * 1000);
        long secs = (remaining % (60 * 1000)) / 1000;
        return String.format("%02dh : %02dm : %02ds remaining", hours, mins, secs);
    }

    private static VBox createJobCard(JobOrder j, StackPane root) {
        // --- 1. Top Header Row: [🏷 ID] [Status Badge] ------- Spacer ------- [Total Wage: ₹X,XXX]
        Text idText = new Text("🏷 " + j.id);
        idText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #2D6A4F;");

        Label statusBadge = new Label();
        if ("PENDING".equalsIgnoreCase(j.status)) {
            statusBadge.setText("⏳ Pending Farmer Payment");
            statusBadge.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #B45309; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 6;");
        } else if ("ACTIVE".equalsIgnoreCase(j.status)) {
            statusBadge.setText("💰 Payment Done • Ready");
            statusBadge.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 6;");
        } else if ("IN_PROGRESS".equalsIgnoreCase(j.status)) {
            statusBadge.setText("🟢 Shift In Progress");
            statusBadge.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 6;");
        } else if ("COMPLETED".equalsIgnoreCase(j.status)) {
            statusBadge.setText("✓ Completed & Settled");
            statusBadge.setStyle("-fx-background-color: #E0F2FE; -fx-text-fill: #0284C7; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 6;");
        } else if ("CANCELLED".equalsIgnoreCase(j.status) || "REJECTED".equalsIgnoreCase(j.status)) {
            statusBadge.setText("✕ Cancelled / Declined");
            statusBadge.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 6;");
        }

        HBox topTags = new HBox(8, idText, statusBadge);
        topTags.setAlignment(Pos.CENTER_LEFT);

        Text wageLabel = new Text("Total Wage: ");
        wageLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");
        Text wageVal = new Text(j.estimatedWage);
        wageVal.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 19px; -fx-font-weight: bold; -fx-fill: #15803D;");
        HBox wageBox = new HBox(3, wageLabel, wageVal);
        wageBox.setAlignment(Pos.CENTER_RIGHT);

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox headerRow = new HBox(12, topTags, topSpacer, wageBox);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        // --- 2. Middle Grid: 2-Column Info
        Text titleText = new Text(j.taskTitle);
        titleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text farmerText = new Text("👨‍🌾 Farmer: " + j.farmerName + " (📞 " + j.phone + ")");
        farmerText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: 600; -fx-fill: #374151;");

        VBox leftCol = new VBox(4, titleText, farmerText);
        HBox.setHgrow(leftCol, Priority.ALWAYS);

        Text locText = new Text("📍 Location: " + j.location);
        locText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        Text timeText = new Text("📅 Schedule: " + j.schedule);
        timeText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        VBox rightCol = new VBox(4, locText, timeText);
        rightCol.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(rightCol, Priority.ALWAYS);

        HBox bodyRow = new HBox(20, leftCol, rightCol);
        bodyRow.setAlignment(Pos.CENTER_LEFT);

        // --- 3. Bottom Footer Row: [Shift Timer Pill (if active)] ------- Spacer ------- [Action Buttons]
        HBox footerRow = new HBox(12);
        footerRow.setAlignment(Pos.CENTER_LEFT);

        if ("IN_PROGRESS".equalsIgnoreCase(j.status)) {
            HBox countdownPill = new HBox(6);
            countdownPill.setAlignment(Pos.CENTER_LEFT);
            countdownPill.setPadding(new Insets(4, 10, 4, 10));
            countdownPill.setStyle("-fx-background-color: #ECFDF5; -fx-background-radius: 6px; -fx-border-color: #A7F3D0; -fx-border-radius: 6px; -fx-border-width: 1px;");

            Text clockIcon = new Text("⏱ Shift Timer:");
            clockIcon.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-fill: #065F46;");

            Label timerLbl = new Label(formatRemainingTime(j));
            timerLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #047857;");

            activeCountdownLabels.put(j.id, timerLbl);
            countdownPill.getChildren().addAll(clockIcon, timerLbl);
            footerRow.getChildren().add(countdownPill);
            ensureCountdownRunning(root);
        }

        Region footerSpacer = new Region();
        HBox.setHgrow(footerSpacer, Priority.ALWAYS);
        footerRow.getChildren().add(footerSpacer);

        HBox btnRow = new HBox(8);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        if ("PENDING".equalsIgnoreCase(j.status)) {
            Label awaitingPay = new Label("⏳ Awaiting Farmer Payment");
            awaitingPay.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-text-fill: #B45309; -fx-font-weight: bold; -fx-background-color: #FEF3C7; -fx-padding: 5 10; -fx-background-radius: 6;");
            btnRow.getChildren().add(awaitingPay);
        } else if ("ACTIVE".equalsIgnoreCase(j.status)) {
            Button startBtn = new Button("▶ Start Job");
            startBtn.setStyle("-fx-background-color: linear-gradient(to right, #15803D, #22C55E); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 16; -fx-effect: dropshadow(gaussian, rgba(21,128,61,0.35), 6, 0, 0, 2);");
            startBtn.setOnAction(e -> {
                j.status = "IN_PROGRESS";
                j.shiftStartTime = System.currentTimeMillis();
                if (j.shiftDurationMillis <= 0) {
                    j.shiftDurationMillis = 3 * 3600 * 1000L;
                }
                new Thread(() -> {
                    try {
                        new RentalRequestDAO().startShift(j.id, j.shiftStartTime, j.shiftDurationMillis);
                        new com.desgin.dao.NotificationDAO().sendNotification(
                            j.farmerEmail,
                            "🚜 Shift Started: " + j.machineRequired,
                            "Operator " + OperatorProfileStore.name + " has started working on your field plot. Live shift countdown timer (3 hours) is active.",
                            "SHIFT"
                        );
                    } catch (Exception ignored) {}
                }).start();

                updateFilterButtonLabels();
                renderJobs(currentFilter, root);
                ensureCountdownRunning(root);
                showFeedback("✓ Job Started! Live field shift timer (3 hours) is running on both Operator & Farmer portals.");
            });
            btnRow.getChildren().add(startBtn);
        } else if ("IN_PROGRESS".equalsIgnoreCase(j.status)) {
            Label liveShiftBadge = new Label("🟢 Shift Active • Awaiting Farmer Completion");
            liveShiftBadge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-text-fill: #065F46; -fx-font-weight: bold; -fx-background-color: #D1FAE5; -fx-padding: 6 12; -fx-background-radius: 6;");
            btnRow.getChildren().add(liveShiftBadge);
        } else if ("COMPLETED".equalsIgnoreCase(j.status)) {
            Label compLabel = new Label("✓ Shift Finished & Paid");
            compLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-text-fill: #0284C7; -fx-font-weight: bold; -fx-background-color: #E0F2FE; -fx-padding: 5 10; -fx-background-radius: 6;");

            btnRow.getChildren().add(compLabel);
        }

        footerRow.getChildren().add(btnRow);

        VBox card = new VBox(10, headerRow, bodyRow, footerRow);
        card.setPadding(new Insets(14, 18, 14, 18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-width: 1px; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 6, 0, 0, 2);");

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #FBFDFB; -fx-background-radius: 12px; -fx-border-color: #2D6A4F; -fx-border-width: 1.2px; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(45,106,79,0.12), 8, 0, 0, 2);"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-width: 1px; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 6, 0, 0, 2);"));

        return card;
    }

    private static Button createFilterBtn(String text, boolean active) {
        Button btn = new Button(text);
        btn.setPrefHeight(34);
        if (active) {
            btn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14;");
        } else {
            btn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14;");
        }
        return btn;
    }

    private static void setActiveFilter(Button active) {
        if (filterBox == null) return;
        for (var node : filterBox.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14;");
            }
        }
        active.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14;");
    }

    private static void showLocationModal(String location, String farmerName, String phone, StackPane root) {
        if (root == null) return;
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.55);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(480);
        modal.setMaxWidth(480);
        modal.setMaxHeight(Region.USE_PREF_SIZE);
        modal.setMinHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(modal, Pos.CENTER);
        modal.setPadding(new Insets(24));
        modal.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 16px;" +
                "-fx-border-color: #C2E0CE;" +
                "-fx-border-width: 1.5px;" +
                "-fx-border-radius: 16px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.22), 20, 0, 0, 6);"
        );

        // Header
        Text title = new Text("📍 Field Plot Location & GPS Guidance");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Real-time plot navigation coordinates and tractor transit routing");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");

        VBox header = new VBox(2, title, sub);

        // Card 1: Farm Plot Location Details
        VBox plotCard = new VBox(8);
        plotCard.setPadding(new Insets(12, 14, 12, 14));
        plotCard.setStyle("-fx-background-color: #F8FAF8; -fx-background-radius: 10px; -fx-border-color: #E2EBE5; -fx-border-radius: 10px; -fx-border-width: 1px;");

        Text locHeader = new Text("🗺 Farm Plot Sector:");
        locHeader.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #2D6A4F;");

        Text locVal = new Text(location != null && !location.isEmpty() ? location : "Baramati Agri Sector (Plot / Gat No. 112)");
        locVal.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text gpsText = new Text("🌐 Geo Coordinates: 18.5204° N, 73.8567° E (Geo-fenced Plot Boundary)");
        gpsText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        Text farmerInfo = new Text("👨‍🌾 Farmer Contact: " + (farmerName != null ? farmerName : "Client") + " (📞 " + (phone != null ? phone : "Registered Phone") + ")");
        farmerInfo.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: 600; -fx-fill: #15803D;");

        plotCard.getChildren().addAll(locHeader, locVal, gpsText, farmerInfo);

        // Card 2: Machinery Road Transit
        VBox routeCard = new VBox(6);
        routeCard.setPadding(new Insets(12, 14, 12, 14));
        routeCard.setStyle("-fx-background-color: #ECFDF5; -fx-background-radius: 10px; -fx-border-color: #A7F3D0; -fx-border-radius: 10px; -fx-border-width: 1px;");

        Text transitHeader = new Text("🚜 Machinery Transit & Road Access:");
        transitHeader.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-fill: #065F46;");

        Text transitDetails = new Text("• Route: Wide Rural Agri Road ➔ Field Gate B\n• Estimated Transit: ~20–25 mins with heavy tractor & implement\n• Gate Condition: Wide clearance for harvesters, seeders & trailers");
        transitDetails.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #047857; -fx-line-spacing: 2.5px;");

        routeCard.getChildren().addAll(transitHeader, transitDetails);

        // Action Buttons
        Button closeBtn = new Button("Close");
        closeBtn.setPrefHeight(38);
        closeBtn.setStyle("-fx-background-color: #F3F4F6; -fx-text-fill: #4B5563; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 0 18px;");
        closeBtn.setOnAction(e -> root.getChildren().remove(overlay));

        Button openGpsBtn = new Button("🧭 Start Live GPS Route");
        openGpsBtn.setPrefHeight(38);
        openGpsBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 0 18px; -fx-effect: dropshadow(gaussian, rgba(46,125,50,0.3), 6, 0, 0, 2);");
        openGpsBtn.setOnAction(e -> {
            openGpsBtn.setText("✓ GPS Navigation Connected");
            openGpsBtn.setStyle("-fx-background-color: #15803D; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 0 18px;");
        });

        HBox btnRow = new HBox(10, closeBtn, openGpsBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        modal.getChildren().addAll(header, plotCard, routeCard, btnRow);
        overlay.getChildren().add(modal);
        overlay.setOnMouseClicked(e -> {
            if (e.getTarget() == overlay) root.getChildren().remove(overlay);
        });

        root.getChildren().add(overlay);
    }

    private static void showFeedback(String msg) {
        if (feedbackBanner != null) {
            feedbackBanner.setText(msg);
            feedbackBanner.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-padding: 10px 16px; -fx-background-radius: 10px; -fx-border-color: #86EFAC; -fx-border-radius: 10px;");
            feedbackBanner.setVisible(true);
            feedbackBanner.setManaged(true);
        }
    }
}
