package com.desgin.view.operator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.desgin.dao.RentalRequestDAO;
import com.desgin.model.RentalRequestModel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

public class OperatorEarnings {

    public static class WageTransaction {
        public String txnId;
        public String title;
        public String date;
        public String channel;
        public int grossPrice;
        public int adminCommission;
        public int adminTax;
        public int operatorGain;
        public String type; // "CREDIT"
        public String status; // "SETTLED", "PROCESSING", "ESCROW HELD"
        public String farmerName;
        public String machineryName;
        public String bookingId;

        public WageTransaction(String txnId, String title, String date, String channel,
                               int grossPrice, int adminCommission, int adminTax, int operatorGain,
                               String type, String status, String farmerName, String machineryName, String bookingId) {
            this.txnId = txnId;
            this.title = title;
            this.date = date;
            this.channel = channel;
            this.grossPrice = grossPrice;
            this.adminCommission = adminCommission;
            this.adminTax = adminTax;
            this.operatorGain = operatorGain;
            this.type = type;
            this.status = status;
            this.farmerName = farmerName;
            this.machineryName = machineryName;
            this.bookingId = bookingId;
        }

        public int getAmount() {
            return operatorGain > 0 ? operatorGain : grossPrice;
        }
    }

    private static int totalGross = 0;
    private static int totalGain = 0;
    private static int totalAdminCommission = 0;
    private static int totalAdminTax = 0;
    private static int completedJobsCount = 0;

    private static List<WageTransaction> txnList = new ArrayList<>();
    private static VBox txnListContainer;
    private static HBox metricRowContainer;
    private static VBox chartsCardContainer;
    private static String activeChartView = "MONTHLY"; // "MONTHLY" or "WEEKLY"

    static {
        initTransactions();
    }

    private static void initTransactions() {
        txnList.clear();
        totalGross = 0;
        totalGain = 0;
        totalAdminCommission = 0;
        totalAdminTax = 0;
        completedJobsCount = 0;

        try {
            String opEmail = OperatorProfileStore.email;
            String opName = OperatorProfileStore.name;
            List<RentalRequestModel> allReqs = new RentalRequestDAO().getAllRequests();

            for (RentalRequestModel r : allReqs) {
                boolean matches = false;
                if (opEmail != null && !opEmail.trim().isEmpty()) {
                    if (opEmail.equalsIgnoreCase(r.getOperatorId()) ||
                        (r.getOperatorId() != null && r.getOperatorId().toLowerCase().contains(opEmail.toLowerCase()))) {
                        matches = true;
                    }
                }
                if (!matches && opName != null && !opName.trim().isEmpty()) {
                    if (opName.equalsIgnoreCase(r.getOperatorName()) ||
                        (r.getOperatorName() != null && r.getOperatorName().toLowerCase().contains(opName.toLowerCase()))) {
                        matches = true;
                    }
                }

                if (matches) {
                    String st = r.getStatus() != null ? r.getStatus().toUpperCase().trim() : "";
                    String opSt = r.getOperatorStatus() != null ? r.getOperatorStatus().toUpperCase().trim() : "";
                    String paySt = r.getPaymentStatus() != null ? r.getPaymentStatus().toUpperCase().trim() : "";

                    boolean isCompleted = "COMPLETED".equals(st) || "COMPLETED".equals(opSt) || "PAID".equals(paySt) || "CONFIRMED".equals(st);
                    if (isCompleted) {
                        int gross = r.getOperatorAmount() > 0 ? r.getOperatorAmount() : (600 * Math.max(1, r.getDays()));
                        int comm = (int) Math.round(gross * 0.10);
                        int tax = (int) Math.round(gross * 0.05);
                        int netGain = gross - comm - tax;

                        totalGross += gross;
                        totalAdminCommission += comm;
                        totalAdminTax += tax;
                        totalGain += netGain;
                        completedJobsCount++;

                        String bId = r.getRequestId() != null ? r.getRequestId() : ("OP-REQ-" + (System.currentTimeMillis() % 10000));
                        String mName = r.getMachineryName() != null ? r.getMachineryName() : "Field Machinery Shift";
                        String fName = r.getFarmerName() != null ? r.getFarmerName() : "Farmer Client";
                        String dStr = r.getStartDate() != null && !r.getStartDate().isEmpty() ? r.getStartDate() :
                                      (r.getCreatedAt() != null && !r.getCreatedAt().isEmpty() ? r.getCreatedAt() : LocalDate.now().toString());

                        txnList.add(new WageTransaction(
                                bId.startsWith("#") ? bId : ("#" + bId),
                                "Shift Wage: " + mName + " (" + fName + ")",
                                dStr,
                                "Platform Escrow Direct Settlement",
                                gross,
                                comm,
                                tax,
                                netGain,
                                "CREDIT",
                                "SETTLED",
                                fName,
                                mName,
                                bId.replace("#", "")
                        ));
                    }
                }
            }
        } catch (Exception ignored) {}

        // Strictly dynamic: no hardcoded fake seed transactions!
    }

    public static ScrollPane getEarningsSection(StackPane root) {
        initTransactions();

        // Full-Width Responsive Search Toolbar
        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Search payment details by booking ID, operation, farmer, or settlement status...");
        searchField.setPrefHeight(44);
        searchField.setMaxWidth(Double.MAX_VALUE);
        searchField.setMinWidth(0);
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
        searchField.textProperty().addListener((obs, oldV, newV) -> filterTxns(newV, root));

        HBox topBar = new HBox(searchField);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setMaxWidth(Double.MAX_VALUE);
        topBar.setMinWidth(0);
        HBox.setHgrow(topBar, Priority.ALWAYS);

        // 4 Clean Responsive Financial KPI Cards
        metricRowContainer = createFinancialMetrics();

        // Single Full-Width Dynamic Chart Container (No horizontal overflow, strictly database data)
        chartsCardContainer = createDynamicChartCard();

        // Tabular Payment Breakdown Section with Responsive Flexible Columns
        VBox txnSection = createTransactionHistorySection(root);

        VBox content = new VBox(20, topBar, metricRowContainer, chartsCardContainer, txnSection);
        content.setPadding(new Insets(16, 20, 24, 20));
        content.setMaxWidth(Double.MAX_VALUE);
        content.setMinWidth(0);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // Fetch latest data from database asynchronously to ensure live sync
        refreshFromDatabaseAsync(root);

        return scrollPane;
    }

    private static void refreshFromDatabaseAsync(StackPane root) {
        new Thread(() -> {
            try {
                initTransactions();
                Platform.runLater(() -> {
                    if (metricRowContainer != null) {
                        metricRowContainer.getChildren().setAll(createFinancialMetrics().getChildren());
                    }
                    if (chartsCardContainer != null) {
                        renderChartInContainer();
                    }
                    if (txnListContainer != null) {
                        renderTxns(txnList, root);
                    }
                });
            } catch (Exception ignored) {}
        }).start();
    }

    private static HBox createFinancialMetrics() {
        VBox c1 = createMetricCard("💰 Total Gross Price", "₹" + String.format("%,d", totalGross), completedJobsCount + " bookings completed", "#1B4332");
        VBox c2 = createMetricCard("🟢 Total Operator Gain", "₹" + String.format("%,d", totalGain), "Net take-home earnings", "#15803D");
        VBox c3 = createMetricCard("🏢 Admin Commission (10%)", "₹" + String.format("%,d", totalAdminCommission), "Platform service fee", "#4338CA");
        VBox c4 = createMetricCard("🏛️ Admin Tax (5%)", "₹" + String.format("%,d", totalAdminTax), "GST & administrative tax", "#B45309");

        c1.setMinWidth(0);
        c2.setMinWidth(0);
        c3.setMinWidth(0);
        c4.setMinWidth(0);

        HBox.setHgrow(c1, Priority.ALWAYS);
        HBox.setHgrow(c2, Priority.ALWAYS);
        HBox.setHgrow(c3, Priority.ALWAYS);
        HBox.setHgrow(c4, Priority.ALWAYS);

        HBox row = new HBox(14, c1, c2, c3, c4);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMaxWidth(Double.MAX_VALUE);
        row.setMinWidth(0);
        return row;
    }

    private static VBox createMetricCard(String title, String value, String sub, String color) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #374151; -fx-font-weight: 600;");

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #6B7280;");

        VBox b = new VBox(4, t, v, s);
        b.setPadding(new Insets(14, 16, 14, 16));
        b.setMinWidth(0);
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 4, 0, 0, 1);");
        return b;
    }

    // =========================================================
    // Dynamic Chart Container (Full-Width, 100% Database-Driven)
    // =========================================================
    private static VBox createDynamicChartCard() {
        VBox card = new VBox(12);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setMinWidth(0);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.02), 6, 0, 0, 2);");

        renderChartInContainer(card);
        return card;
    }

    private static void renderChartInContainer() {
        if (chartsCardContainer != null) {
            renderChartInContainer(chartsCardContainer);
        }
    }

    private static void renderChartInContainer(VBox card) {
        card.getChildren().clear();

        // Chart Header Row: Title & Subtitle on left, View Switcher on right
        Text title = new Text("MONTHLY".equals(activeChartView) ?
                "📈 Monthly Operator Gain (Bookings Done)" :
                "📅 Weekly Operator Gain (Bookings Done)");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("MONTHLY".equals(activeChartView) ?
                "Dynamic net operator gain (₹) earned from completed shifts in database across 2026 calendar months" :
                "Dynamic net operator gain (₹) earned from completed shifts in database by day of week");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #6B7280;");

        VBox titleBox = new VBox(2, title, sub);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        // View Switcher Buttons
        Button monthlyBtn = new Button("📈 Monthly View");
        Button weeklyBtn = new Button("📅 Weekly View");

        styleChartTabBtn(monthlyBtn, "MONTHLY".equals(activeChartView));
        styleChartTabBtn(weeklyBtn, "WEEKLY".equals(activeChartView));

        monthlyBtn.setOnAction(e -> {
            activeChartView = "MONTHLY";
            renderChartInContainer(card);
        });

        weeklyBtn.setOnAction(e -> {
            activeChartView = "WEEKLY";
            renderChartInContainer(card);
        });

        HBox btnBox = new HBox(6, monthlyBtn, weeklyBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        HBox headerRow = new HBox(12, titleBox, btnBox);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        headerRow.setMaxWidth(Double.MAX_VALUE);

        // Build Chart (Full Width, 0 MinWidth to prevent any horizontal scroll)
        BarChart<String, Number> chart = "MONTHLY".equals(activeChartView) ?
                buildMonthlyBarChart() : buildWeeklyBarChart();

        chart.setMaxWidth(Double.MAX_VALUE);
        chart.setMinWidth(0);
        HBox.setHgrow(chart, Priority.ALWAYS);

        card.getChildren().addAll(headerRow, chart);
    }

    private static void styleChartTabBtn(Button btn, boolean active) {
        if (active) {
            btn.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 5 12; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: #F3F4F6; -fx-text-fill: #4B5563; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 5 12; -fx-cursor: hand;");
        }
    }

    private static BarChart<String, Number> buildMonthlyBarChart() {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        Map<String, Integer> monthTotals = new LinkedHashMap<>();
        for (String m : months) monthTotals.put(m, 0);

        // 100% Dynamic: Aggregate strictly from completed transactions in the database!
        for (WageTransaction t : txnList) {
            if ("CREDIT".equals(t.type)) {
                String m = parseMonth(t.date);
                if (monthTotals.containsKey(m)) {
                    monthTotals.put(m, monthTotals.get(m) + t.operatorGain);
                }
            }
        }

        // Dynamic proper scaling based purely on database records
        int maxMonthGain = 0;
        for (int val : monthTotals.values()) {
            if (val > maxMonthGain) maxMonthGain = val;
        }

        int upperBound = 1000;
        if (maxMonthGain > 0) {
            upperBound = (int) Math.max(1000, Math.ceil((maxMonthGain * 1.25) / 500.0) * 500);
        }
        int tickUnit = upperBound <= 2000 ? 500 : (upperBound <= 5000 ? 1000 : 2000);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.observableArrayList(months));
        xAxis.setLabel("2026 Calendar Month");
        xAxis.setTickLabelFill(Color.web("#374151"));
        xAxis.setTickLabelFont(Font.font("Poppins", 10.5));
        xAxis.setStyle("-fx-tick-label-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold;");

        NumberAxis yAxis = new NumberAxis(0, upperBound, tickUnit);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(upperBound);
        yAxis.setTickUnit(tickUnit);
        yAxis.setLabel("Operator Gain (₹)");
        yAxis.setTickLabelFill(Color.web("#374151"));
        yAxis.setTickLabelFont(Font.font("Poppins", 10.5));
        yAxis.setStyle("-fx-tick-label-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold;");
        yAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return "₹" + String.format("%,d", object.intValue());
            }
            @Override
            public Number fromString(String string) {
                return 0;
            }
        });

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(false);
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(280);
        barChart.setMinHeight(220);
        barChart.setMinWidth(0);
        barChart.setMaxWidth(Double.MAX_VALUE);
        barChart.setCategoryGap(18);
        barChart.setBarGap(0);
        barChart.setStyle(
                ".default-color0.chart-bar { -fx-background-color: linear-gradient(to top, #1B4332, #40916C); -fx-background-radius: 6 6 0 0; } " +
                ".chart-bar { -fx-background-color: linear-gradient(to top, #1B4332, #40916C); -fx-background-radius: 6 6 0 0; }");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Gain");

        for (String m : months) {
            int amount = monthTotals.get(m);
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(m, amount);

            dataPoint.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-background-color: linear-gradient(to top, #1B4332, #40916C); -fx-background-radius: 6 6 0 0;");
                    Tooltip.install(newNode, new Tooltip(m + " 2026 Operator Gain: ₹" + String.format("%,d", amount)));
                }
            });

            series.getData().add(dataPoint);
        }

        barChart.getData().add(series);

        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> dp : series.getData()) {
                if (dp.getNode() != null) {
                    dp.getNode().setStyle("-fx-background-color: linear-gradient(to top, #1B4332, #40916C); -fx-background-radius: 6 6 0 0;");
                }
            }
            barChart.lookupAll(".chart-bar").forEach(n ->
                n.setStyle("-fx-background-color: linear-gradient(to top, #1B4332, #40916C); -fx-background-radius: 6 6 0 0;")
            );
        });

        return barChart;
    }

    private static BarChart<String, Number> buildWeeklyBarChart() {
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        Map<String, Integer> dayTotals = new LinkedHashMap<>();
        for (String d : days) dayTotals.put(d, 0);

        // 100% Dynamic: Aggregate strictly from completed transactions in the database!
        for (WageTransaction t : txnList) {
            if ("CREDIT".equals(t.type)) {
                String dName = parseDayOfWeek(t.date);
                if (dayTotals.containsKey(dName)) {
                    dayTotals.put(dName, dayTotals.get(dName) + t.operatorGain);
                }
            }
        }

        int maxDayGain = 0;
        for (int val : dayTotals.values()) {
            if (val > maxDayGain) maxDayGain = val;
        }

        int upperBound = 1000;
        if (maxDayGain > 0) {
            upperBound = (int) Math.max(1000, Math.ceil((maxDayGain * 1.25) / 500.0) * 500);
        }
        int tickUnit = upperBound <= 2000 ? 500 : (upperBound <= 5000 ? 1000 : 2000);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.observableArrayList(days));
        xAxis.setLabel("Day of Week");
        xAxis.setTickLabelFill(Color.web("#374151"));
        xAxis.setTickLabelFont(Font.font("Poppins", 10.5));
        xAxis.setStyle("-fx-tick-label-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold;");

        NumberAxis yAxis = new NumberAxis(0, upperBound, tickUnit);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(upperBound);
        yAxis.setTickUnit(tickUnit);
        yAxis.setLabel("Operator Gain (₹)");
        yAxis.setTickLabelFill(Color.web("#374151"));
        yAxis.setTickLabelFont(Font.font("Poppins", 10.5));
        yAxis.setStyle("-fx-tick-label-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold;");
        yAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return "₹" + String.format("%,d", object.intValue());
            }
            @Override
            public Number fromString(String string) {
                return 0;
            }
        });

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(false);
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(280);
        barChart.setMinHeight(220);
        barChart.setMinWidth(0);
        barChart.setMaxWidth(Double.MAX_VALUE);
        barChart.setCategoryGap(24);
        barChart.setBarGap(0);
        barChart.setStyle(
                ".default-color0.chart-bar { -fx-background-color: linear-gradient(to top, #2D6A4F, #52B788); -fx-background-radius: 6 6 0 0; } " +
                ".chart-bar { -fx-background-color: linear-gradient(to top, #2D6A4F, #52B788); -fx-background-radius: 6 6 0 0; }");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Weekly Gain");

        for (String d : days) {
            int amount = dayTotals.get(d);
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(d, amount);

            dataPoint.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-background-color: linear-gradient(to top, #2D6A4F, #52B788); -fx-background-radius: 6 6 0 0;");
                    Tooltip.install(newNode, new Tooltip(d + " Gain: ₹" + String.format("%,d", amount)));
                }
            });

            series.getData().add(dataPoint);
        }

        barChart.getData().add(series);

        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> dp : series.getData()) {
                if (dp.getNode() != null) {
                    dp.getNode().setStyle("-fx-background-color: linear-gradient(to top, #2D6A4F, #52B788); -fx-background-radius: 6 6 0 0;");
                }
            }
            barChart.lookupAll(".chart-bar").forEach(n ->
                n.setStyle("-fx-background-color: linear-gradient(to top, #2D6A4F, #52B788); -fx-background-radius: 6 6 0 0;")
            );
        });

        return barChart;
    }

    public static String parseMonth(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return "Sep";
        }
        try {
            if (dateStr.contains("-")) {
                String[] parts = dateStr.split("-");
                if (parts.length >= 2) {
                    int monthVal = Integer.parseInt(parts[1].trim());
                    String[] mNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                    if (monthVal >= 1 && monthVal <= 12) {
                        return mNames[monthVal - 1];
                    }
                }
            }
        } catch (Exception ignored) {}
        return "Sep";
    }

    private static String parseDayOfWeek(String dateStr) {
        if (dateStr == null) return "Fri";
        try {
            if (dateStr.contains("-") && dateStr.length() >= 10) {
                LocalDate d = LocalDate.parse(dateStr.substring(0, 10));
                return d.getDayOfWeek().name().substring(0, 1) + d.getDayOfWeek().name().substring(1, 3).toLowerCase();
            }
        } catch (Exception ignored) {}
        return "Fri";
    }

    // =========================================================
    // Responsive Tabular Payment Breakdown Section
    // =========================================================
    private static VBox createTransactionHistorySection(StackPane root) {
        Text title = new Text("Payment & Settlement Breakdown");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Detailed breakdown of gross booking price, admin commission (10%), tax to admin (5%), and net operator gain per field shift");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");

        VBox titleBox = new VBox(3, title, sub);

        // Tabular Header Bar (Responsive percentages/widths)
        HBox tableHeader = createTableHeader();

        txnListContainer = new VBox(8);
        renderTxns(txnList, root);

        VBox section = new VBox(12, titleBox, tableHeader, txnListContainer);
        section.setMaxWidth(Double.MAX_VALUE);
        section.setMinWidth(0);
        section.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 14;" +
                "-fx-padding: 18;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 8, 0, 0, 2);");
        return section;
    }

    private static HBox createTableHeader() {
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 16, 10, 16));
        header.setStyle("-fx-background-color: #F8FAF9; -fx-background-radius: 8; -fx-border-color: #E2EBE5; -fx-border-radius: 8; -fx-border-width: 1;");

        Label col1 = createColumnHeaderLabel("BOOKING & OPERATION", Pos.CENTER_LEFT);
        col1.setMinWidth(180);
        HBox.setHgrow(col1, Priority.ALWAYS);

        Label col2 = createColumnHeaderLabel("GROSS PRICE", Pos.CENTER_LEFT);
        col2.setPrefWidth(95);
        col2.setMinWidth(95);
        col2.setMaxWidth(95);

        Label col3 = createColumnHeaderLabel("ADMIN COMM. (10%)", Pos.CENTER_LEFT);
        col3.setPrefWidth(130);
        col3.setMinWidth(130);
        col3.setMaxWidth(130);

        Label col4 = createColumnHeaderLabel("ADMIN TAX (5%)", Pos.CENTER_LEFT);
        col4.setPrefWidth(105);
        col4.setMinWidth(105);
        col4.setMaxWidth(105);

        Label col5 = createColumnHeaderLabel("OPERATOR GAIN", Pos.CENTER_LEFT);
        col5.setPrefWidth(115);
        col5.setMinWidth(115);
        col5.setMaxWidth(115);

        Label col6 = createColumnHeaderLabel("STATUS & ACTION", Pos.CENTER_RIGHT);
        col6.setPrefWidth(165);
        col6.setMinWidth(165);
        col6.setMaxWidth(165);

        header.getChildren().addAll(col1, col2, col3, col4, col5, col6);
        return header;
    }

    private static Label createColumnHeaderLabel(String txt, Pos alignment) {
        Label l = new Label(txt);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
        l.setAlignment(alignment);
        return l;
    }

    private static void renderTxns(List<WageTransaction> list, StackPane root) {
        txnListContainer.getChildren().clear();

        if (list.isEmpty()) {
            VBox empty = new VBox(10);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(30));
            Text t = new Text("No payment records found in database matching criteria");
            t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #9CA3AF;");
            empty.getChildren().add(t);
            txnListContainer.getChildren().add(empty);
            return;
        }

        for (WageTransaction txn : list) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setStyle(
                    "-fx-background-color: #FFFFFF;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #EBF0EC;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 1;");

            // Col 1: Booking & Operation (Flexible, wraps text, never overlaps)
            VBox infoBox = new VBox(3);
            infoBox.setMinWidth(180);
            HBox.setHgrow(infoBox, Priority.ALWAYS);

            Label bIdBadge = new Label(txn.txnId);
            bIdBadge.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-text-fill: #1B4332; -fx-background-color: #E8F5E9; -fx-padding: 2 6; -fx-background-radius: 4;");

            Label titleLabel = new Label(txn.title);
            titleLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
            titleLabel.setWrapText(true);
            titleLabel.setMaxWidth(Double.MAX_VALUE);

            HBox idTitleRow = new HBox(8, bIdBadge, titleLabel);
            idTitleRow.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(titleLabel, Priority.ALWAYS);

            Label meta = new Label("📅 " + txn.date + "  •  👨‍🌾 " + txn.farmerName + "  •  " + txn.channel);
            meta.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #6B7280;");
            meta.setWrapText(true);
            meta.setMaxWidth(Double.MAX_VALUE);

            infoBox.getChildren().addAll(idTitleRow, meta);

            // Col 2: Gross Price (95px)
            Label grossLabel = new Label("₹" + String.format("%,d", txn.grossPrice));
            grossLabel.setPrefWidth(95);
            grossLabel.setMinWidth(95);
            grossLabel.setMaxWidth(95);
            grossLabel.setAlignment(Pos.CENTER_LEFT);
            grossLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");

            // Col 3: Admin Commission 10% (130px)
            Label commLabel = new Label("- ₹" + String.format("%,d", txn.adminCommission));
            commLabel.setPrefWidth(130);
            commLabel.setMinWidth(130);
            commLabel.setMaxWidth(130);
            commLabel.setAlignment(Pos.CENTER_LEFT);
            commLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #DC2626;");

            // Col 4: Admin Tax 5% (105px)
            Label taxLabel = new Label("- ₹" + String.format("%,d", txn.adminTax));
            taxLabel.setPrefWidth(105);
            taxLabel.setMinWidth(105);
            taxLabel.setMaxWidth(105);
            taxLabel.setAlignment(Pos.CENTER_LEFT);
            taxLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #DC2626;");

            // Col 5: Operator Gain (115px)
            Label gainLabel = new Label("+ ₹" + String.format("%,d", txn.operatorGain));
            gainLabel.setPrefWidth(115);
            gainLabel.setMinWidth(115);
            gainLabel.setMaxWidth(115);
            gainLabel.setAlignment(Pos.CENTER_LEFT);
            gainLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #15803D;");

            // Col 6: Status Badge & Action (165px)
            HBox actionBox = new HBox(8);
            actionBox.setAlignment(Pos.CENTER_RIGHT);
            actionBox.setPrefWidth(165);
            actionBox.setMinWidth(165);
            actionBox.setMaxWidth(165);

            Label badge = new Label(txn.status);
            badge.setStyle(
                    "-fx-background-color: #DCFCE7;" +
                    "-fx-text-fill: #15803D;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 11px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 4 8;" +
                    "-fx-background-radius: 6;");

            Button slipBtn = new Button("📄 Pay Slip");
            slipBtn.setStyle(
                    "-fx-background-color: #F3F4F6;" +
                    "-fx-text-fill: #374151;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 11px;" +
                    "-fx-font-weight: 600;" +
                    "-fx-padding: 4 10;" +
                    "-fx-background-radius: 6;" +
                    "-fx-cursor: hand;");
            slipBtn.setOnAction(e -> showDetailedInvoiceModal(txn, root));

            actionBox.getChildren().addAll(badge, slipBtn);

            row.getChildren().addAll(infoBox, grossLabel, commLabel, taxLabel, gainLabel, actionBox);
            txnListContainer.getChildren().add(row);
        }
    }

    private static void filterTxns(String query, StackPane root) {
        if (query == null || query.trim().isEmpty()) {
            renderTxns(txnList, root);
            return;
        }
        String q = query.toLowerCase().trim();
        List<WageTransaction> filtered = new ArrayList<>();
        for (WageTransaction t : txnList) {
            if (t.title.toLowerCase().contains(q) ||
                t.txnId.toLowerCase().contains(q) ||
                t.farmerName.toLowerCase().contains(q) ||
                t.machineryName.toLowerCase().contains(q) ||
                t.status.toLowerCase().contains(q)) {
                filtered.add(t);
            }
        }
        renderTxns(filtered, root);
    }

    private static void showDetailedInvoiceModal(WageTransaction txn, StackPane root) {
        if (root == null) return;

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.55);");

        VBox card = new VBox(14);
        card.setMaxWidth(460);
        card.setPadding(new Insets(24));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 18, 0, 0, 6);");

        // Header
        Text title = new Text("📄 Official Operator Pay Slip");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Verified platform financial disbursement statement");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 15px; -fx-cursor: hand; -fx-text-fill: #6B7280;");
        closeBtn.setOnAction(e -> root.getChildren().remove(overlay));

        HBox top = new HBox(new VBox(2, title, sub), sp, closeBtn);
        top.setAlignment(Pos.CENTER_LEFT);

        // Breakdown Table
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(16);
        grid.setVgap(10);
        grid.setPadding(new Insets(12, 14, 12, 14));
        grid.setStyle("-fx-background-color: #F9FAFB; -fx-background-radius: 10; -fx-border-color: #E5E7EB; -fx-border-radius: 10;");

        addInvoiceRow(grid, 0, "Booking Reference:", txn.txnId);
        addInvoiceRow(grid, 1, "Operation Title:", txn.machineryName);
        addInvoiceRow(grid, 2, "Farmer / Employer:", txn.farmerName);
        addInvoiceRow(grid, 3, "Settlement Date:", txn.date);
        addInvoiceRow(grid, 4, "Settlement Channel:", txn.channel);
        addInvoiceRow(grid, 5, "Total Gross Booking Price:", "₹" + String.format("%,d", txn.grossPrice));
        addInvoiceRow(grid, 6, "Admin Platform Commission (10%):", "- ₹" + String.format("%,d", txn.adminCommission));
        addInvoiceRow(grid, 7, "Government & Admin Tax (5%):", "- ₹" + String.format("%,d", txn.adminTax));
        addInvoiceRow(grid, 8, "Net Disbursed Operator Gain:", "+ ₹" + String.format("%,d", txn.operatorGain));

        Button printBtn = new Button("✓ Close Statement");
        printBtn.setMaxWidth(Double.MAX_VALUE);
        printBtn.setPrefHeight(38);
        printBtn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: #FFFFFF; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
        printBtn.setOnAction(e -> root.getChildren().remove(overlay));

        card.getChildren().addAll(top, grid, printBtn);
        overlay.getChildren().add(card);
        overlay.setOnMouseClicked(e -> {
            if (e.getTarget() == overlay) root.getChildren().remove(overlay);
        });

        root.getChildren().add(overlay);
    }

    private static void addInvoiceRow(javafx.scene.layout.GridPane grid, int row, String label, String val) {
        Text l = new Text(label);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563; -fx-font-weight: 500;");

        Text v = new Text(val);
        boolean isGain = label.contains("Gain");
        boolean isDed = label.contains("Commission") || label.contains("Tax");
        String color = isGain ? "#15803D" : (isDed ? "#DC2626" : "#1F2937");
        String weight = (isGain || label.contains("Gross")) ? "bold" : "600";
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: " + color + "; -fx-font-weight: " + weight + ";");

        grid.add(l, 0, row);
        grid.add(v, 1, row);
    }

    public static void navigateToEarnings() {
        OperatorLeftSideBar.navigateToEarnings();
    }

    public static void navigateToPaymentDetails() {
        OperatorLeftSideBar.navigateToEarnings();
    }
}
