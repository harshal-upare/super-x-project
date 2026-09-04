package com.desgin.view.operator;

import java.util.ArrayList;
import java.util.List;

import com.desgin.dao.NotificationDAO;
import com.desgin.dao.RentalRequestDAO;
import com.desgin.model.RentalRequestModel;

import javafx.application.Platform;
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

public class OperatorJobRequests {

    public static class RequestOrder {
        public String id;
        public String machineryName;
        public String farmerName;
        public String farmerPhone;
        public String farmerEmail;
        public String location;
        public String schedule;
        public String wageRate;
        public String estimatedWage;
        public int wageValue;
        public String status; // "PENDING", "ACCEPTED", "IN_PROGRESS", "COMPLETED", "REJECTED"
        public String paymentStatus; // "PENDING", "PAID"
        public long shiftStartTime;
        public long shiftDurationMillis;
        public Boolean unlockedRef = false;

        public RequestOrder(String id, String machineryName, String farmerName, String farmerPhone, String farmerEmail,
                            String location, String schedule, String wageRate, String estimatedWage,
                            int wageValue, String status, String paymentStatus, long shiftStartTime, long shiftDurationMillis) {
            this.id = id;
            this.machineryName = machineryName;
            this.farmerName = farmerName;
            this.farmerPhone = farmerPhone;
            this.farmerEmail = farmerEmail;
            this.location = location;
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

    private static final List<RequestOrder> requestsList = new ArrayList<>();
    private static final java.util.Map<String, Label> requestCountdownLabels = new java.util.HashMap<>();
    private static javafx.animation.Timeline countdownTimeline;
    private static VBox requestContainer;
    private static Label feedbackBanner;
    private static String currentFilter = "ALL";

    private static Button allBtn;
    private static Button newBtn;
    private static Button paymentPendingBtn;
    private static Button readyToStartBtn;
    private static HBox filterBox;

    public static void loadRequestsFromFirestore(StackPane root) {
        new Thread(() -> {
            try {
                String opEmail = OperatorProfileStore.email;
                List<RequestOrder> loaded = new ArrayList<>();

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
                        boolean isInProgress = "IN_PROGRESS".equalsIgnoreCase(reqSt) || "IN_PROGRESS".equalsIgnoreCase(opSt) || shiftStart > 0;
                        boolean isCancelled = "CANCELLED".equalsIgnoreCase(reqSt) || "REJECTED".equalsIgnoreCase(reqSt) || "REJECTED".equalsIgnoreCase(opSt);

                        // Once approved and started or finished, it leaves Job Requests and belongs in Jobs!
                        if (isCompleted || isInProgress || isCancelled) {
                            continue;
                        }

                        String finalSt = "PENDING";
                        if ("ACCEPTED".equalsIgnoreCase(opSt) || "ACTIVE".equalsIgnoreCase(reqSt) || "CONFIRMED".equalsIgnoreCase(reqSt)) {
                            finalSt = "ACCEPTED";
                        } else {
                            finalSt = "PENDING";
                        }

                        loaded.add(new RequestOrder(
                                r.getRequestId() != null ? r.getRequestId() : "REQ-" + System.currentTimeMillis() % 10000,
                                r.getMachineryName() != null ? r.getMachineryName() : "Tractor Machinery",
                                r.getFarmerName() != null ? r.getFarmerName() : "Farmer Client",
                                r.getFarmerPhone() != null ? r.getFarmerPhone() : "+91 98220 12345",
                                r.getFarmerEmail() != null ? r.getFarmerEmail() : "farmer@farmmail.com",
                                r.getFarmerLocation() != null ? r.getFarmerLocation() : "Baramati Sector 4",
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
                    requestsList.clear();
                    requestsList.addAll(loaded);
                    updateFilterButtonLabels();
                    renderRequests(currentFilter, root);
                    ensureCountdownRunning(root);
                });
            } catch (Exception ignored) {}
        }).start();
    }

    private static void updateFilterButtonLabels() {
        long allCount = requestsList.size();
        long newCount = requestsList.stream().filter(r -> "PENDING".equalsIgnoreCase(r.status)).count();
        long payPendingCount = requestsList.stream().filter(r -> "ACCEPTED".equalsIgnoreCase(r.status) && !"PAID".equalsIgnoreCase(r.paymentStatus)).count();
        long readyCount = requestsList.stream().filter(r -> "ACCEPTED".equalsIgnoreCase(r.status) && "PAID".equalsIgnoreCase(r.paymentStatus)).count();

        if (allBtn != null) allBtn.setText("All Requests (" + allCount + ")");
        if (newBtn != null) newBtn.setText("📥 New Requests (" + newCount + ")");
        if (paymentPendingBtn != null) paymentPendingBtn.setText("⏳ Payment Pending (" + payPendingCount + ")");
        if (readyToStartBtn != null) readyToStartBtn.setText("▶ Ready to Start (" + readyCount + ")");
    }

    private static Button createFilterBtn(String text, boolean isActive) {
        Button btn = new Button(text);
        btn.setPrefHeight(34);
        if (isActive) {
            btn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14;");
        } else {
            btn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14;");
        }
        return btn;
    }

    private static void setActiveFilter(Button active) {
        if (filterBox == null) return;
        for (javafx.scene.Node node : filterBox.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14;");
            }
        }
        active.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 14;");
    }

    public static ScrollPane getJobRequestsSection(StackPane root) {
        feedbackBanner = new Label();
        feedbackBanner.setVisible(false);
        feedbackBanner.setManaged(false);

        // Full-Width Search Bar
        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Search incoming job requests by farmer name, machinery, or plot location...");
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

        // Category Filter Tabs: All Requests | New Requests | Payment Pending | Ready to Start
        filterBox = new HBox(8);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        allBtn = createFilterBtn("All Requests", true);
        newBtn = createFilterBtn("📥 New Requests", false);
        paymentPendingBtn = createFilterBtn("⏳ Payment Pending", false);
        readyToStartBtn = createFilterBtn("▶ Ready to Start", false);

        allBtn.setOnAction(e -> { currentFilter = "ALL"; setActiveFilter(allBtn); renderRequests("ALL", root); });
        newBtn.setOnAction(e -> { currentFilter = "NEW"; setActiveFilter(newBtn); renderRequests("NEW", root); });
        paymentPendingBtn.setOnAction(e -> { currentFilter = "PAYMENT_PENDING"; setActiveFilter(paymentPendingBtn); renderRequests("PAYMENT_PENDING", root); });
        readyToStartBtn.setOnAction(e -> { currentFilter = "READY_TO_START"; setActiveFilter(readyToStartBtn); renderRequests("READY_TO_START", root); });

        filterBox.getChildren().addAll(allBtn, newBtn, paymentPendingBtn, readyToStartBtn);

        requestContainer = new VBox(14);
        requestContainer.setMaxWidth(Double.MAX_VALUE);

        loadRequestsFromFirestore(root);

        VBox content = new VBox(14, feedbackBanner, searchField, filterBox, requestContainer);
        content.setPadding(new Insets(20, 28, 30, 28));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static void renderRequests(StackPane root) {
        renderRequests(currentFilter, root);
    }

    private static void renderRequests(String filter, StackPane root) {
        if (requestContainer == null) return;
        requestContainer.getChildren().clear();

        List<RequestOrder> filtered = new ArrayList<>();
        for (RequestOrder req : requestsList) {
            if ("ALL".equalsIgnoreCase(filter)) {
                filtered.add(req);
            } else if ("NEW".equalsIgnoreCase(filter) && "PENDING".equalsIgnoreCase(req.status)) {
                filtered.add(req);
            } else if ("PAYMENT_PENDING".equalsIgnoreCase(filter) && "ACCEPTED".equalsIgnoreCase(req.status) && !"PAID".equalsIgnoreCase(req.paymentStatus)) {
                filtered.add(req);
            } else if ("READY_TO_START".equalsIgnoreCase(filter) && "ACCEPTED".equalsIgnoreCase(req.status) && "PAID".equalsIgnoreCase(req.paymentStatus)) {
                filtered.add(req);
            }
        }

        if (filtered.isEmpty()) {
            VBox emptyBox = new VBox(10);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(45));
            emptyBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14px; -fx-border-color: #E2EBE5; -fx-border-width: 1px; -fx-border-radius: 14px;");

            Text icon = new Text("📥");
            icon.setStyle("-fx-font-size: 38px;");

            String tText = "No Incoming Job Requests";
            String sText = "When farmers hire you for field tasks, new job request cards will appear here immediately.";
            if ("PAYMENT_PENDING".equalsIgnoreCase(filter)) {
                icon.setText("⏳");
                tText = "No Payment Pending Requests";
                sText = "Approved requests where farmer payment is awaiting clearance will show here.";
            } else if ("READY_TO_START".equalsIgnoreCase(filter)) {
                icon.setText("▶");
                tText = "No Jobs Ready to Start";
                sText = "Requests with verified farmer payments ready for shift start will show here.";
            } else if ("NEW".equalsIgnoreCase(filter)) {
                icon.setText("📥");
                tText = "No New Requests Awaiting Approval";
                sText = "You have reviewed all incoming requests.";
            }

            Text t = new Text(tText);
            t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text s = new Text(sText);
            s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #6B7280;");

            Button gotoJobsBtn = new Button("🚜  View Active & Scheduled Jobs ➔");
            gotoJobsBtn.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 8 16;");
            gotoJobsBtn.setOnAction(e -> OperatorLeftSideBar.navigateToJobs());

            emptyBox.getChildren().addAll(icon, t, s, gotoJobsBtn);
            requestContainer.getChildren().add(emptyBox);
            return;
        }

        for (RequestOrder req : filtered) {
            VBox card = createRequestCard(req, root);
            requestContainer.getChildren().add(card);
        }
    }

    private static void filterSearch(String query, StackPane root) {
        if (requestContainer == null) return;
        requestContainer.getChildren().clear();
        String q = query.toLowerCase().trim();

        for (RequestOrder req : requestsList) {
            if (req.id.toLowerCase().contains(q) ||
                req.farmerName.toLowerCase().contains(q) ||
                req.machineryName.toLowerCase().contains(q) ||
                req.location.toLowerCase().contains(q)) {
                requestContainer.getChildren().add(createRequestCard(req, root));
            }
        }
    }

    private static void ensureCountdownRunning(StackPane root) {
        if (countdownTimeline != null) return;
        countdownTimeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), ev -> {
                boolean unlockNeeded = false;
                for (RequestOrder req : requestsList) {
                    if ("IN_PROGRESS".equalsIgnoreCase(req.status)) {
                        Label lbl = requestCountdownLabels.get(req.id);
                        if (lbl != null) {
                            lbl.setText(formatRemainingTime(req));
                        }
                        if (req.shiftStartTime > 0) {
                            long elapsed = System.currentTimeMillis() - req.shiftStartTime;
                            long remaining = Math.max(0, req.shiftDurationMillis - elapsed);
                            if (remaining == 0 && !Boolean.TRUE.equals(req.unlockedRef)) {
                                req.unlockedRef = true;
                                unlockNeeded = true;
                            }
                        }
                    }
                }
                if (unlockNeeded && root != null) {
                    renderRequests(root);
                }
            })
        );
        countdownTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        countdownTimeline.play();
    }

    private static String formatRemainingTime(RequestOrder req) {
        if (req.shiftStartTime <= 0) return "Starting Shift...";
        long elapsed = System.currentTimeMillis() - req.shiftStartTime;
        long remaining = Math.max(0, req.shiftDurationMillis - elapsed);
        if (remaining == 0) {
            return "✓ Shift Time Completed (00h : 00m : 00s)";
        }
        long hours = remaining / (3600 * 1000);
        long mins = (remaining % (3600 * 1000)) / (60 * 1000);
        long secs = (remaining % (60 * 1000)) / 1000;
        return String.format("%02dh : %02dm : %02ds remaining", hours, mins, secs);
    }

    private static VBox createRequestCard(RequestOrder req, StackPane root) {
        // --- 1. Top Header Row: [🏷 ID] [Status Badge] ------- Spacer ------- [Total Wage: ₹X,XXX]
        Text idText = new Text("🏷 " + req.id);
        idText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #2D6A4F;");

        Label statusBadge = new Label();
        if ("PENDING".equalsIgnoreCase(req.status)) {
            statusBadge.setText("⏳ Awaiting Your Approval");
            statusBadge.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #B45309; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 6;");
        } else if ("ACCEPTED".equalsIgnoreCase(req.status)) {
            if ("PAID".equalsIgnoreCase(req.paymentStatus)) {
                statusBadge.setText("💰 Payment Verified • Ready");
                statusBadge.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 6;");
            } else {
                statusBadge.setText("⏳ Pending for Payment");
                statusBadge.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #D97706; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 6;");
            }
        } else if ("IN_PROGRESS".equalsIgnoreCase(req.status)) {
            statusBadge.setText("🟢 Shift In Progress");
            statusBadge.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 6;");
        } else if ("COMPLETED".equalsIgnoreCase(req.status)) {
            statusBadge.setText("✓ Completed & Settled");
            statusBadge.setStyle("-fx-background-color: #E0F2FE; -fx-text-fill: #0284C7; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 6;");
        } else if ("REJECTED".equalsIgnoreCase(req.status)) {
            statusBadge.setText("✕ Declined");
            statusBadge.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 6;");
        }

        HBox topTags = new HBox(8, idText, statusBadge);
        topTags.setAlignment(Pos.CENTER_LEFT);

        Text wageLabel = new Text("Total Wage: ");
        wageLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");
        Text wageVal = new Text(req.estimatedWage);
        wageVal.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 19px; -fx-font-weight: bold; -fx-fill: #15803D;");
        HBox wageBox = new HBox(3, wageLabel, wageVal);
        wageBox.setAlignment(Pos.CENTER_RIGHT);

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox headerRow = new HBox(12, topTags, topSpacer, wageBox);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        // --- 2. Middle Grid: Farmer Info & Details
        Text farmerNameText = new Text("👨‍🌾 Farmer: " + req.farmerName);
        farmerNameText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Button farmerDetailBtn = new Button("ℹ Details of Farmer");
        farmerDetailBtn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 3 8; -fx-border-color: #A7F3D0; -fx-border-radius: 6;");
        farmerDetailBtn.setOnAction(e -> showFarmerDetailModal(req, root));

        HBox farmerRow = new HBox(8, farmerNameText, farmerDetailBtn);
        farmerRow.setAlignment(Pos.CENTER_LEFT);

        Text equipText = new Text("🚜 Machinery: " + req.machineryName);
        equipText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: 600; -fx-fill: #374151;");

        VBox leftCol = new VBox(4, farmerRow, equipText);
        HBox.setHgrow(leftCol, Priority.ALWAYS);

        Text locText = new Text("📍 Location: " + req.location);
        locText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        Text timeText = new Text("📅 Schedule: " + req.schedule);
        timeText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        VBox rightCol = new VBox(4, locText, timeText);
        rightCol.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(rightCol, Priority.ALWAYS);

        HBox bodyRow = new HBox(20, leftCol, rightCol);
        bodyRow.setAlignment(Pos.CENTER_LEFT);

        // --- 3. Bottom Footer Row: Spacer ------- [Action Buttons]
        HBox footerRow = new HBox(12);
        footerRow.setAlignment(Pos.CENTER_LEFT);

        Region footerSpacer = new Region();
        HBox.setHgrow(footerSpacer, Priority.ALWAYS);
        footerRow.getChildren().add(footerSpacer);

        HBox btnRow = new HBox(8);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        if ("PENDING".equalsIgnoreCase(req.status)) {
            Button approveBtn = new Button("✔ Approve");
            approveBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 14; -fx-effect: dropshadow(gaussian, rgba(46,125,50,0.3), 4, 0, 0, 2);");
            approveBtn.setOnAction(e -> {
                req.status = "ACCEPTED";
                new Thread(() -> {
                    try {
                        new RentalRequestDAO().updateOperatorStatus(req.id, "ACCEPTED");
                        new NotificationDAO().sendNotification(
                            req.farmerEmail,
                            "✔ Operator Approved Your Request",
                            "Operator " + OperatorProfileStore.name + " approved your hire request for " + req.machineryName + ". Complete payment to unlock the shift.",
                            "APPROVED",
                            req.id
                        );
                    } catch (Exception ignored) {}
                }).start();

                updateFilterButtonLabels();
                renderRequests(currentFilter, root);
                showFeedback("✓ Request Approved! Now under 'Payment Pending' awaiting farmer payment clearance.");
            });

            Button declineBtn = new Button("✕ Decline");
            declineBtn.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 12;");
            declineBtn.setOnAction(e -> {
                requestsList.remove(req);
                new Thread(() -> {
                    try {
                        new RentalRequestDAO().updateOperatorStatus(req.id, "REJECTED");
                        new RentalRequestDAO().updateRequestStatus(req.id, "CANCELLED");
                        new NotificationDAO().sendNotification(
                            req.farmerEmail,
                            "✕ Request Declined",
                            "Operator " + OperatorProfileStore.name + " is unavailable and declined the request for " + req.machineryName + ".",
                            "DECLINED",
                            req.id
                        );
                    } catch (Exception ignored) {}
                }).start();

                updateFilterButtonLabels();
                renderRequests(currentFilter, root);
                showFeedback("Request " + req.id + " declined.");
            });

            btnRow.getChildren().addAll(approveBtn, declineBtn);
        } else if ("ACCEPTED".equalsIgnoreCase(req.status)) {
            if ("PAID".equalsIgnoreCase(req.paymentStatus)) {
                Button startBtn = new Button("▶ Start Job");
                startBtn.setStyle("-fx-background-color: linear-gradient(to right, #15803D, #22C55E); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 16; -fx-effect: dropshadow(gaussian, rgba(21,128,61,0.35), 6, 0, 0, 2);");
                startBtn.setOnAction(e -> {
                    req.status = "IN_PROGRESS";
                    req.shiftStartTime = System.currentTimeMillis();
                    if (req.shiftDurationMillis <= 0) {
                        req.shiftDurationMillis = 3 * 3600 * 1000L;
                    }
                    OperatorProfileStore.setAvailability(false);
                    new Thread(() -> {
                        try {
                            new com.desgin.dao.AuthDAO().setOperatorAvailability(OperatorProfileStore.email, false);
                            new RentalRequestDAO().startShift(req.id, req.shiftStartTime, req.shiftDurationMillis);
                            new NotificationDAO().sendNotification(
                                req.farmerEmail,
                                "🚜 Shift Started: " + req.machineryName,
                                "Operator " + OperatorProfileStore.name + " has started working on your field. Live shift countdown timer (3 hours) is active.",
                                "SHIFT",
                                req.id
                            );
                        } catch (Exception ignored) {}
                    }).start();

                    // REMOVE FROM JOB REQUESTS: Once started, it is in the Jobs section!
                    requestsList.remove(req);
                    updateFilterButtonLabels();
                    renderRequests(currentFilter, root);
                    showFeedback("✓ Job Started! Shift has been moved from Job Requests to your Jobs section.");
                });
                btnRow.getChildren().add(startBtn);
            } else {
                Label awaitingPay = new Label("⏳ Payment Pending (Awaiting Farmer Clearance)");
                awaitingPay.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-text-fill: #B45309; -fx-font-weight: bold; -fx-background-color: #FEF3C7; -fx-padding: 6 12; -fx-background-radius: 6;");
                btnRow.getChildren().add(awaitingPay);
            }
        }

        footerRow.getChildren().add(btnRow);

        VBox card = new VBox(10, headerRow, bodyRow, footerRow);
        card.setPadding(new Insets(14, 18, 14, 18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-width: 1px; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 6, 0, 0, 2);");

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #FBFDFB; -fx-background-radius: 12px; -fx-border-color: #2D6A4F; -fx-border-width: 1.2px; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(45,106,79,0.12), 8, 0, 0, 2);"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #E2EBE5; -fx-border-width: 1px; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 6, 0, 0, 2);"));

        return card;
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

    private static void showFarmerDetailModal(RequestOrder req, StackPane root) {
        if (root == null) return;
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(440);
        modal.setMaxWidth(440);
        modal.setPadding(new Insets(22));
        modal.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-border-color: #D1E7DD; -fx-border-width: 1.5; -fx-border-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 6);");

        Text title = new Text("👨‍🌾 Farmer Contact & Plot Details");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        VBox dBox = new VBox(8);
        dBox.setPadding(new Insets(10));
        dBox.setStyle("-fx-background-color: #F8FAF8; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-radius: 10;");

        dBox.getChildren().addAll(
            createDetailRow("Farmer Name:", req.farmerName),
            createDetailRow("Phone Number:", req.farmerPhone),
            createDetailRow("Email Address:", req.farmerEmail),
            createDetailRow("Plot Location:", req.location),
            createDetailRow("Task Machinery:", req.machineryName),
            createDetailRow("Schedule:", req.schedule),
            createDetailRow("Offered Wage:", req.estimatedWage + " (" + req.wageRate + ")")
        );

        Button closeBtn = new Button("Close Details");
        closeBtn.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 16;");
        closeBtn.setOnAction(e -> root.getChildren().remove(overlay));

        modal.getChildren().addAll(title, dBox, closeBtn);
        overlay.getChildren().add(modal);
        StackPane.setAlignment(modal, Pos.CENTER);

        overlay.setOnMouseClicked(e -> {
            if (e.getTarget() == overlay) root.getChildren().remove(overlay);
        });

        root.getChildren().add(overlay);
    }

    private static HBox createDetailRow(String label, String value) {
        Text l = new Text(label);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #1B4332;");
        l.setWrappingWidth(130);

        Text v = new Text(value != null && !value.isEmpty() ? value : "Not specified");
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #374151;");
        v.setWrappingWidth(240);

        HBox row = new HBox(8, l, v);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
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
