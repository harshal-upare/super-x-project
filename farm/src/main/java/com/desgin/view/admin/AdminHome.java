package com.desgin.view.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

@SuppressWarnings("unchecked")
public class AdminHome {

    public static ScrollPane getPage(StackPane root) {
        // Top Welcome Header
        Text welcomeText = new Text("Platform Master Command & Operations 🛡️");
        welcomeText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text subtitleText = new Text("Real-time ecosystem intelligence, multi-district fleet deployment telemetry, escrow liquidity, and system moderation.");
        subtitleText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563;");

        VBox headerBox = new VBox(3, welcomeText, subtitleText);

        // 4 Master KPI Cards in responsive GridPane
        GridPane kpiGrid = createMasterKPIGrid(root);

        // Quick Action Command Strip
        FlowPane quickActionStrip = createAdminActionStrip(root);

        // Charts Row 1: AreaChart (GMV vs Commission) + PieChart (Category Share)
        GridPane chartsGrid1 = createChartsGrid1();

        // Charts Row 2: BarChart (District Demand) + System Health Audit Matrix
        GridPane chartsGrid2 = createChartsGrid2();

        // Charts Row 3: Booking Status PieChart + Payment Status BarChart + Provider vs Operator BarChart
        GridPane chartsGrid3 = createChartsGrid3();

        // Live System Audit Activity Feed (dynamic)
        VBox activityFeed = createLiveActivityFeed();

        VBox content = new VBox(20, headerBox, kpiGrid, quickActionStrip, chartsGrid1, chartsGrid2, chartsGrid3, activityFeed);
        content.setPadding(new Insets(20, 25, 35, 25));
        content.setStyle("-fx-background-color: transparent;");
        content.setMinWidth(0);
        content.setPrefWidth(Region.USE_COMPUTED_SIZE);
        content.setMaxWidth(Double.MAX_VALUE);

        loadAllChartsAsync();

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static GridPane createMasterKPIGrid(StackPane root) {
        Text v1 = new Text("...");
        Text s1 = new Text("Loading users...");
        VBox c1 = createMetricCardDynamic("👥 Total Registered Users", v1, "Live Active", s1, "#1B4332", "#FFFFFF", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.usersBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(UserManagement.getPage(root));
        });

        Text v2 = new Text("...");
        Text s2 = new Text("Loading fleet...");
        VBox c2 = createMetricCardDynamic("🚜 Listed Fleet", v2, "Verified Fleet", s2, "#2E7D32", "#E8F5E9", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.approvalsBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(MachineryApprovals.getPage(root));
        });

        Text v3 = new Text("...");
        Text s3 = new Text("Loading bookings...");
        VBox c3 = createMetricCardDynamic("💳 Total Rental GMV", v3, "Escrow Secured", s3, "#374151", "#FFFFFF", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.escrowBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(EscrowFinancials.getPage(root));
        });

        Text v4 = new Text("...");
        Text s4 = new Text("Platform Take Rate");
        VBox c4 = createMetricCardDynamic("🏦 Platform Net Cut (7%)", v4, "Realized", s4, "#2E7D32", "#E8F5E9", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.escrowBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(EscrowFinancials.getPage(root));
        });

        // Query Firestore asynchronously
        new Thread(() -> {
            try {
                java.util.Map<String, Integer> userCounts = new com.desgin.dao.AuthDAO().getUserRoleCounts();
                int fCount = userCounts.getOrDefault("Farmer", 0);
                int pCount = userCounts.getOrDefault("Provider", 0);
                int oCount = userCounts.getOrDefault("Operator", 0);
                int totalUsers = fCount + pCount + oCount + userCounts.getOrDefault("Admin", 1);

                java.util.List<com.desgin.model.MachineryModel> machs = new com.desgin.dao.MachineryDAO().getAllMachinery();
                int totalFleet = machs.size();
                long availFleet = machs.stream().filter(m -> "AVAILABLE".equalsIgnoreCase(m.getStatus())).count();

                java.util.List<com.desgin.model.RentalRequestModel> reqs = new com.desgin.dao.RentalRequestDAO().getAllRequests();
                int totalGmv = 0;
                int escrowSecured = 0;
                for (com.desgin.model.RentalRequestModel r : reqs) {
                    int amt = r.getTotalAmount() > 0 ? r.getTotalAmount() : (r.getDailyRate() * Math.max(1, r.getDays()));
                    totalGmv += amt;
                    if ("PAID".equalsIgnoreCase(r.getPaymentStatus())) {
                        escrowSecured += amt;
                    }
                }
                int netCut = (int) (totalGmv * 0.07);

                final int fTotalUsers = totalUsers;
                final int fFCount = fCount;
                final int fPCount = pCount;
                final int fOCount = oCount;
                final int fTotalFleet = totalFleet;
                final long fAvailFleet = availFleet;
                final int fTotalGmv = totalGmv;
                final int fEscrow = escrowSecured;
                final int fNetCut = netCut;

                javafx.application.Platform.runLater(() -> {
                    v1.setText(String.valueOf(fTotalUsers));
                    s1.setText(fFCount + " Farmers • " + fPCount + " Providers • " + fOCount + " Operators");

                    v2.setText(fTotalFleet + " Units");
                    s2.setText(fAvailFleet + " Available • " + (fTotalFleet - fAvailFleet) + " Reserved");

                    v3.setText("₹" + String.format("%,d", fTotalGmv));
                    s3.setText("₹" + String.format("%,d", fEscrow) + " secured in escrow");

                    v4.setText("₹" + String.format("%,d", fNetCut));
                    s4.setText("Direct Platform Earnings YTD");
                });
            } catch (Exception ignored) {}
        }).start();

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);
        grid.setMinWidth(0);
        grid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(25);
        col2.setHgrow(Priority.ALWAYS);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(25);
        col3.setHgrow(Priority.ALWAYS);

        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(25);
        col4.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2, col3, col4);

        grid.add(c1, 0, 0);
        grid.add(c2, 1, 0);
        grid.add(c3, 2, 0);
        grid.add(c4, 3, 0);

        return grid;
    }

    private static VBox createMetricCardDynamic(String title, Text v, String badge, Text s, String color, String bgColor, Runnable onClick) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        Text b = new Text(badge);
        b.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-fill: " + color + ";");
        StackPane badgeBox = new StackPane(b);
        badgeBox.setPadding(new Insets(2, 6, 2, 6));
        badgeBox.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 6; -fx-border-color: #E2EBE5; -fx-border-radius: 6;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(6, t, spacer, badgeBox);
        top.setAlignment(Pos.CENTER_LEFT);

        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: " + color + ";");
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: #374151;");

        VBox card = new VBox(5, top, v, s);
        card.setPadding(new Insets(14));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12; -fx-cursor: hand;");

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #F0FDF4; -fx-background-radius: 12; -fx-border-color: #2D6A4F; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(27,67,50,0.18), 8, 0.2, 0, 2); -fx-cursor: hand;");
            card.setTranslateY(-2);
        });

        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12; -fx-cursor: hand;");
            card.setTranslateY(0);
        });

        if (onClick != null) {
            card.setOnMouseClicked(e -> onClick.run());
        }
        return card;
    }

    private static VBox createMetricCard(String title, String value, String badge, String sub, String color, String bgColor, Runnable onClick) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

        Text b = new Text(badge);
        b.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-fill: " + color + ";");
        StackPane badgeBox = new StackPane(b);
        badgeBox.setPadding(new Insets(2, 6, 2, 6));
        badgeBox.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 6; -fx-border-color: #E2EBE5; -fx-border-radius: 6;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(6, t, spacer, badgeBox);
        top.setAlignment(Pos.CENTER_LEFT);

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: #374151;");

        VBox card = new VBox(5, top, v, s);
        card.setPadding(new Insets(14));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12; -fx-cursor: hand;");

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #F0FDF4; -fx-background-radius: 12; -fx-border-color: #2D6A4F; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(27,67,50,0.18), 8, 0.2, 0, 2); -fx-cursor: hand;");
            card.setTranslateY(-2);
        });

        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12; -fx-cursor: hand;");
            card.setTranslateY(0);
        });

        if (onClick != null) {
            card.setOnMouseClicked(e -> onClick.run());
        }

        return card;
    }

    private static FlowPane createAdminActionStrip(StackPane root) {
        Button b1 = createAdminBtn("🚜 Verify Machinery (12)", "#2E7D32", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.approvalsBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(MachineryApprovals.getPage(root));
        });

        Button b2 = createAdminBtn("👥 Review KYC (5)", "#2D6A4F", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.usersBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(UserManagement.getPage(root));
        });

        Button b3 = createAdminBtn("💰 Audit Escrow", "#374151", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.escrowBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(EscrowFinancials.getPage(root));
        });

        Button b4 = createAdminBtn("⚖ Resolve Disputes (2)", "#8B3A3A", () -> {
            AdminLeftSideBar.setActiveButton(AdminLeftSideBar.disputesBtn, AdminLeftSideBar.navigationButtons);
            AdminDashboard.borderPane.setCenter(DisputeResolution.getPage(root));
        });

        FlowPane bar = new FlowPane(10, 10, b1, b2, b3, b4);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setMinWidth(0);
        bar.setMaxWidth(Double.MAX_VALUE);
        return bar;
    }

    private static Button createAdminBtn(String text, String bg, Runnable action) {
        Button btn = new Button(text);
        btn.setPrefHeight(36);
        btn.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 14 0 14;");
        btn.setOnAction(e -> action.run());
        return btn;
    }

    // =========================================================
    // ASYNC CHART CONTAINERS & LOADERS
    // =========================================================
    private static final StackPane gmvContainer = new StackPane();
    private static final StackPane catContainer = new StackPane();
    private static final StackPane distContainer = new StackPane();
    private static final StackPane statusContainer = new StackPane();
    private static final StackPane paymentContainer = new StackPane();
    private static final StackPane provOpContainer = new StackPane();

    private static void loadAllChartsAsync() {
        gmvContainer.getChildren().setAll(createEmptyState("📈", "Loading Volume Analytics...", "Connecting to Firestore ecosystem..."));
        catContainer.getChildren().setAll(createEmptyState("🥧", "Loading Fleet Distribution...", "Calculating machinery inventory..."));
        distContainer.getChildren().setAll(createEmptyState("📊", "Loading District Velocity...", "Analyzing regional demand..."));
        statusContainer.getChildren().setAll(createEmptyState("📊", "Loading Booking Status...", "Aggregating platform requests..."));
        paymentContainer.getChildren().setAll(createEmptyState("💳", "Loading Payment Metrics...", "Retrieving financial ledger..."));
        provOpContainer.getChildren().setAll(createEmptyState("🏦", "Loading Earnings Distribution...", "Comparing provider & operator payouts..."));

        new Thread(() -> {
            try {
                java.util.List<com.desgin.model.RentalRequestModel> reqs = new com.desgin.dao.RentalRequestDAO().getAllRequests();
                java.util.List<com.desgin.model.MachineryModel> machs = new com.desgin.dao.MachineryDAO().getAllMachinery();

                javafx.application.Platform.runLater(() -> {
                    gmvContainer.getChildren().setAll(buildGMVAreaChart(reqs));
                    catContainer.getChildren().setAll(buildCategoryPieChart(machs));
                    distContainer.getChildren().setAll(buildDistrictDemandBarChart(reqs));
                    statusContainer.getChildren().setAll(buildBookingStatusPieChart(reqs));
                    paymentContainer.getChildren().setAll(buildPaymentStatusBarChart(reqs));
                    provOpContainer.getChildren().setAll(buildProviderVsOperatorBarChart(reqs));
                });
            } catch (Exception e) {
                System.err.println("Notice: Error loading charts async: " + e.getMessage());
            }
        }).start();
    }

    // =========================================================
    // CHARTS ROW 1: AreaChart (GMV vs Commission) + PieChart (Category Distribution)
    // =========================================================
    private static GridPane createChartsGrid1() {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);
        grid.setMinWidth(0);
        grid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(55);
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(45);
        col2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2);

        grid.add(gmvContainer, 0, 0);
        grid.add(catContainer, 1, 0);
        return grid;
    }

    private static VBox buildGMVAreaChart(java.util.List<com.desgin.model.RentalRequestModel> reqs) {
        Text cardTitle = new Text("📈 Platform Booking Volume & Net Commission");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Gross Booking Volume (₹k) vs 7% Platform Commission (₹k)");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Operating Month (2026)");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount (₹ in Thousands)");

        AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setAnimated(false);
        areaChart.setLegendVisible(true);
        areaChart.setPrefHeight(250);
        areaChart.setMinHeight(220);
        areaChart.setMinWidth(0);
        areaChart.setMaxWidth(Double.MAX_VALUE);

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        java.util.Map<String, Double> gmvMap = new java.util.LinkedHashMap<>();
        java.util.Map<String, Double> commMap = new java.util.LinkedHashMap<>();
        for (String m : months) {
            gmvMap.put(m, 0.0);
            commMap.put(m, 0.0);
        }

        int count = 0;
        if (reqs != null) {
            for (com.desgin.model.RentalRequestModel r : reqs) {
                int amt = r.getTotalAmount() > 0 ? r.getTotalAmount() : (r.getDailyRate() * Math.max(1, r.getDays()));
                if (r.getStartDate() != null) {
                    for (String m : months) {
                        if (r.getStartDate().toLowerCase().contains(m.toLowerCase())) {
                            double kVal = amt / 1000.0;
                            gmvMap.put(m, gmvMap.get(m) + kVal);
                            commMap.put(m, commMap.get(m) + (kVal * 0.07));
                            count++;
                            break;
                        }
                    }
                }
            }
        }

        if (count == 0) {
            VBox emptyBox = createEmptyState("📈", "No rental transactions yet.", "Platform volume curves will appear as farmer rentals occur.");
            VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), emptyBox);
            card.setPadding(new Insets(16));
            card.setMinWidth(0);
            card.setMaxWidth(Double.MAX_VALUE);
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
            return card;
        }

        XYChart.Series<String, Number> seriesGMV = new XYChart.Series<>();
        seriesGMV.setName("Gross Volume (₹k)");
        XYChart.Series<String, Number> seriesComm = new XYChart.Series<>();
        seriesComm.setName("Platform Cut (₹k)");

        for (String m : months) {
            seriesGMV.getData().add(new XYChart.Data<>(m, gmvMap.get(m)));
            seriesComm.getData().add(new XYChart.Data<>(m, commMap.get(m)));
        }

        areaChart.getData().addAll(seriesGMV, seriesComm);

        VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), areaChart);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox buildCategoryPieChart(java.util.List<com.desgin.model.MachineryModel> list) {
        Text cardTitle = new Text("🥧 Fleet Category Distribution");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Fleet units distributed across equipment categories");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        if (list != null) {
            java.util.Map<String, Integer> counts = new java.util.HashMap<>();
            for (com.desgin.model.MachineryModel m : list) {
                String cat = m.getCategory() != null ? m.getCategory() : "General Machinery";
                counts.put(cat, counts.getOrDefault(cat, 0) + 1);
            }
            for (java.util.Map.Entry<String, Integer> entry : counts.entrySet()) {
                pieData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
            }
        }

        if (pieData.isEmpty()) {
            VBox emptyBox = createEmptyState("🚜", "No equipment listed yet.", "Fleet categories will appear when providers list machines.");
            VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), emptyBox);
            card.setPadding(new Insets(16));
            card.setMinWidth(0);
            card.setMaxWidth(Double.MAX_VALUE);
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
            return card;
        }

        PieChart pieChart = new PieChart(pieData);
        pieChart.setAnimated(false);
        pieChart.setLegendVisible(true);
        pieChart.setPrefHeight(250);
        pieChart.setMinHeight(220);
        pieChart.setMinWidth(0);
        pieChart.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), pieChart);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    // =========================================================
    // CHARTS ROW 2: BarChart (District Demand) + Security Matrix
    // =========================================================
    private static GridPane createChartsGrid2() {
        VBox securityCard = createSystemSecurityCard();

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);
        grid.setMinWidth(0);
        grid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        col2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2);

        grid.add(distContainer, 0, 0);
        grid.add(securityCard, 1, 0);
        return grid;
    }

    private static VBox buildDistrictDemandBarChart(java.util.List<com.desgin.model.RentalRequestModel> reqs) {
        Text cardTitle = new Text("📊 District-Wise Rental Velocity");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Active rental days logged across agricultural districts");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("District");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Rental Days");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(false);
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(250);
        barChart.setMinHeight(220);
        barChart.setMinWidth(0);
        barChart.setMaxWidth(Double.MAX_VALUE);

        java.util.Map<String, Integer> distMap = new java.util.HashMap<>();
        if (reqs != null) {
            for (com.desgin.model.RentalRequestModel r : reqs) {
                String dist = r.getDistrict() != null ? r.getDistrict() : "Maharashtra";
                distMap.put(dist, distMap.getOrDefault(dist, 0) + Math.max(1, r.getDays()));
            }
        }

        if (distMap.isEmpty()) {
            VBox emptyBox = createEmptyState("📊", "No rental activity recorded yet.", "Regional utilization metrics will appear as bookings are confirmed.");
            VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), emptyBox);
            card.setPadding(new Insets(16));
            card.setMinWidth(0);
            card.setMaxWidth(Double.MAX_VALUE);
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
            return card;
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Rental Days");
        for (java.util.Map.Entry<String, Integer> entry : distMap.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().add(series);

        VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), barChart);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createSystemSecurityCard() {
        Text cardTitle = new Text("🛡️ Enterprise Security & Integrity Matrix");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Automated compliance verifications and security audit status");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        VBox c1 = createComplianceRow("AES-256 Escrow Vault Encryption", "Active & Compliant", "SECURE", "#2E7D32");
        VBox c2 = createComplianceRow("Razorpay Automated Settlement Gateway", "Webhook Verified", "OPERATIONAL", "#15803D");
        VBox c3 = createComplianceRow("5-Admin Master Allocation Quota", "Strict Enforcement", "ACTIVE", "#0284C7");
        VBox c4 = createComplianceRow("Firestore Document Audit Logging", "Full Traceability", "ONLINE", "#2E7D32");

        VBox card = new VBox(10, new VBox(2, cardTitle, cardSub), c1, c2, c3, c4);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createComplianceRow(String title, String desc, String badge, String badgeColor) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text d = new Text(desc);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #4B5563;");

        VBox left = new VBox(1, t, d);
        left.setMinWidth(0);

        Label b = new Label(badge);
        b.setStyle("-fx-background-color: " + badgeColor + "; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 3 7 3 7; -fx-background-radius: 4;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(8, left, spacer, b);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 12, 8, 12));
        row.setMinWidth(0);
        row.setStyle("-fx-background-color: #FFFDFC; -fx-background-radius: 8; -fx-border-color: #E2D7CB; -fx-border-radius: 8; -fx-border-width: 1;");

        return new VBox(row);
    }

    private static VBox createLiveActivityFeed() {
        Text title = new Text("⚡ Real-Time Platform Event Feed");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        VBox feedContainer = new VBox(8);
        feedContainer.setMinWidth(0);

        // Load last 5 real booking events from Firestore
        new Thread(() -> {
            try {
                java.util.List<com.desgin.model.RentalRequestModel> reqs = new com.desgin.dao.RentalRequestDAO().getAllRequests();
                // Sort by createdAt descending (most recent first) and take up to 5
                reqs.sort((a, b) -> {
                    String da = a.getCreatedAt() != null ? a.getCreatedAt() : "";
                    String db2 = b.getCreatedAt() != null ? b.getCreatedAt() : "";
                    return db2.compareTo(da);
                });
                java.util.List<com.desgin.model.RentalRequestModel> recent = reqs.subList(0, Math.min(5, reqs.size()));

                javafx.application.Platform.runLater(() -> {
                    feedContainer.getChildren().clear();
                    if (recent.isEmpty()) {
                        Text empty = new Text("No platform events recorded yet.");
                        empty.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #6B7280;");
                        feedContainer.getChildren().add(empty);
                        return;
                    }
                    for (com.desgin.model.RentalRequestModel r : recent) {
                        String st = r.getStatus() != null ? r.getStatus().toUpperCase() : "PENDING";
                        String statusColor = "PENDING".equals(st) ? "#E65100"
                            : "COMPLETED".equals(st) ? "#2E7D32"
                            : "CANCELLED".equals(st) || "REJECTED".equals(st) ? "#B91C1C"
                            : "#1976D2";
                        int total = r.getTotalAmount() > 0 ? r.getTotalAmount() : (r.getDailyRate() * Math.max(1, r.getDays()));
                        String eventTitle = "PENDING".equals(st) ? "🌾 New Booking Request"
                            : "ACCEPTED".equals(st) || "CONFIRMED".equals(st) ? "✔ Booking Accepted"
                            : "COMPLETED".equals(st) ? "💰 Rental Completed"
                            : "CANCELLED".equals(st) ? "✖ Booking Cancelled"
                            : "📋 Booking " + st;
                        String farmerN = r.getFarmerName() != null ? r.getFarmerName() : "Farmer";
                        String machN = r.getMachineryName() != null ? r.getMachineryName() : "Equipment";
                        String desc = farmerN + " booked " + machN + " for ₹" + String.format("%,d", total)
                            + (r.getStartDate() != null ? " (from " + r.getStartDate() + ")" : "");
                        String date = r.getCreatedAt() != null ? r.getCreatedAt() : "Recently";
                        feedContainer.getChildren().add(createFeedRow(eventTitle, desc, date, st, statusColor));
                    }
                });
            } catch (Exception ignored) {
                javafx.application.Platform.runLater(() -> {
                    Text err = new Text("Could not load platform events.");
                    err.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #9CA3AF;");
                    feedContainer.getChildren().add(err);
                });
            }
        }).start();

        VBox box = new VBox(8, title, feedContainer);
        box.setMinWidth(0);
        return box;
    }

    private static HBox createFeedRow(String title, String desc, String time, String status, String statusColor) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text d = new Text(desc);
        d.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #374151;");

        VBox left = new VBox(1, t, d);
        left.setMinWidth(0);

        Text tm = new Text("📅 " + time);
        tm.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #4B5563;");

        Label badge = new Label(status);
        badge.setStyle("-fx-background-color: " + statusColor + "; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 9.5px; -fx-font-weight: bold; -fx-padding: 2 6 2 6; -fx-background-radius: 4;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(10, left, spacer, tm, badge);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 14, 10, 14));
        row.setMinWidth(0);
        row.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");

        return row;
    }

    // =========================================================
    // CHARTS ROW 3: Booking Status PieChart + Payment Status BarChart + Provider vs Operator Earnings BarChart
    // =========================================================
    private static GridPane createChartsGrid3() {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);
        grid.setMinWidth(0);
        grid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33.33);
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33.33);
        col2.setHgrow(Priority.ALWAYS);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(33.34);
        col3.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2, col3);

        grid.add(statusContainer, 0, 0);
        grid.add(paymentContainer, 1, 0);
        grid.add(provOpContainer, 2, 0);

        return grid;
    }

    private static VBox buildBookingStatusPieChart(java.util.List<com.desgin.model.RentalRequestModel> reqs) {
        Text cardTitle = new Text("📊 Booking Status Distribution");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Breakdown of all bookings by current status");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        int totalCount = 0;
        if (reqs != null) {
            java.util.Map<String, Integer> statusMap = new java.util.LinkedHashMap<>();
            for (com.desgin.model.RentalRequestModel r : reqs) {
                String st = r.getStatus() != null ? r.getStatus().toUpperCase() : "PENDING";
                // Normalize statuses for display
                if ("APPROVED".equals(st) || "ACCEPTED".equals(st) || "CONFIRMED".equals(st)) st = "CONFIRMED";
                if ("DECLINED".equals(st)) st = "CANCELLED";
                statusMap.put(st, statusMap.getOrDefault(st, 0) + 1);
            }
            for (java.util.Map.Entry<String, Integer> entry : statusMap.entrySet()) {
                if (entry.getValue() > 0) {
                    pieData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
                    totalCount += entry.getValue();
                }
            }
        }

        if (totalCount == 0) {
            VBox emptyBox = createEmptyState("📊", "No bookings yet.", "Booking distribution will appear when rentals are placed.");
            VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), emptyBox);
            card.setPadding(new Insets(16));
            card.setMinWidth(0);
            card.setMaxWidth(Double.MAX_VALUE);
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
            return card;
        }

        PieChart pieChart = new PieChart(pieData);
        pieChart.setAnimated(false);
        pieChart.setLegendVisible(true);
        pieChart.setPrefHeight(240);
        pieChart.setMinHeight(200);
        pieChart.setMinWidth(0);
        pieChart.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), pieChart);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox buildPaymentStatusBarChart(java.util.List<com.desgin.model.RentalRequestModel> reqs) {
        Text cardTitle = new Text("💳 Payment Status by Month");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("PAID vs PENDING bookings per month (2026)");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Bookings");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(false);
        barChart.setLegendVisible(true);
        barChart.setPrefHeight(240);
        barChart.setMinHeight(200);
        barChart.setMinWidth(0);
        barChart.setMaxWidth(Double.MAX_VALUE);

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        java.util.Map<String, Integer> paidMap = new java.util.LinkedHashMap<>();
        java.util.Map<String, Integer> pendingMap = new java.util.LinkedHashMap<>();
        for (String m : months) { paidMap.put(m, 0); pendingMap.put(m, 0); }

        int count = 0;
        if (reqs != null) {
            for (com.desgin.model.RentalRequestModel r : reqs) {
                if (r.getStartDate() == null) continue;
                for (String m : months) {
                    if (r.getStartDate().toLowerCase().contains(m.toLowerCase())) {
                        boolean isPaid = "PAID".equalsIgnoreCase(r.getPaymentStatus());
                        if (isPaid) paidMap.put(m, paidMap.get(m) + 1);
                        else pendingMap.put(m, pendingMap.get(m) + 1);
                        count++;
                        break;
                    }
                }
            }
        }

        if (count == 0) {
            VBox emptyBox = createEmptyState("💳", "No payment data yet.", "Payment trends will appear as bookings are confirmed.");
            VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), emptyBox);
            card.setPadding(new Insets(16));
            card.setMinWidth(0);
            card.setMaxWidth(Double.MAX_VALUE);
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
            return card;
        }

        XYChart.Series<String, Number> paidSeries = new XYChart.Series<>();
        paidSeries.setName("PAID");
        XYChart.Series<String, Number> pendingSeries = new XYChart.Series<>();
        pendingSeries.setName("PENDING");

        for (String m : months) {
            paidSeries.getData().add(new XYChart.Data<>(m, paidMap.get(m)));
            pendingSeries.getData().add(new XYChart.Data<>(m, pendingMap.get(m)));
        }
        barChart.getData().addAll(paidSeries, pendingSeries);

        VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), barChart);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox buildProviderVsOperatorBarChart(java.util.List<com.desgin.model.RentalRequestModel> reqs) {
        Text cardTitle = new Text("🏦 Provider vs Operator Earnings");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text cardSub = new Text("Net provider payout (93%) vs operator wages by month");
        cardSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount (₹k)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(false);
        barChart.setLegendVisible(true);
        barChart.setPrefHeight(240);
        barChart.setMinHeight(200);
        barChart.setMinWidth(0);
        barChart.setMaxWidth(Double.MAX_VALUE);

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        java.util.Map<String, Double> provMap = new java.util.LinkedHashMap<>();
        java.util.Map<String, Double> opMap = new java.util.LinkedHashMap<>();
        for (String m : months) { provMap.put(m, 0.0); opMap.put(m, 0.0); }

        int count = 0;
        if (reqs != null) {
            for (com.desgin.model.RentalRequestModel r : reqs) {
                if (r.getStartDate() == null || !"PAID".equalsIgnoreCase(r.getPaymentStatus())) continue;
                for (String m : months) {
                    if (r.getStartDate().toLowerCase().contains(m.toLowerCase())) {
                        int eqAmt = r.getEquipmentAmount() > 0 ? r.getEquipmentAmount() : r.getTotalAmount();
                        double provNet = (eqAmt * 0.93) / 1000.0;
                        provMap.put(m, provMap.get(m) + provNet);
                        if (r.isOperatorRequired()) {
                            int opAmt = r.getOperatorAmount() > 0 ? r.getOperatorAmount() : (500 * Math.max(1, r.getDays()));
                            opMap.put(m, opMap.get(m) + (opAmt / 1000.0));
                        }
                        count++;
                        break;
                    }
                }
            }
        }

        if (count == 0) {
            VBox emptyBox = createEmptyState("🏦", "No payout data yet.", "Provider and operator earnings will appear after paid bookings complete.");
            VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), emptyBox);
            card.setPadding(new Insets(16));
            card.setMinWidth(0);
            card.setMaxWidth(Double.MAX_VALUE);
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
            return card;
        }

        XYChart.Series<String, Number> provSeries = new XYChart.Series<>();
        provSeries.setName("Provider (₹k net)");
        XYChart.Series<String, Number> opSeries = new XYChart.Series<>();
        opSeries.setName("Operator Wages (₹k)");

        for (String m : months) {
            provSeries.getData().add(new XYChart.Data<>(m, provMap.get(m)));
            opSeries.getData().add(new XYChart.Data<>(m, opMap.get(m)));
        }
        barChart.getData().addAll(provSeries, opSeries);

        VBox card = new VBox(6, new VBox(2, cardTitle, cardSub), barChart);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createEmptyState(String icon, String title, String sub) {
        VBox emptyBox = new VBox(8);
        emptyBox.setAlignment(Pos.CENTER);
        emptyBox.setPrefHeight(200);
        Text ico = new Text(icon);
        ico.setStyle("-fx-font-size: 28px;");
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");
        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #6B7280;");
        emptyBox.getChildren().addAll(ico, t, s);
        return emptyBox;
    }
}
