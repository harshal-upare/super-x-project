package com.desgin.view.operator;

import java.util.ArrayList;
import java.util.List;

import com.desgin.dao.PayoutDAO;
import com.desgin.dao.RentalRequestDAO;
import com.desgin.model.PayoutModel;
import com.desgin.model.RentalRequestModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
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

public class OperatorEarnings {

    public static class WageTransaction {
        public String txnId;
        public String title;
        public String date;
        public String channel;
        public int amount;
        public String type; // "CREDIT", "WITHDRAWAL"
        public String status;

        public WageTransaction(String txnId, String title, String date, String channel, int amount, String type, String status) {
            this.txnId = txnId;
            this.title = title;
            this.date = date;
            this.channel = channel;
            this.amount = amount;
            this.type = type;
            this.status = status;
        }
    }

    private static int totalLifetime = 0;
    private static int availableBalance = 0;
    private static int pendingEscrow = 0;
    private static int totalWithdrawn = 0;
    private static int thisMonthEarnings = 0;
    private static int completedJobsCount = 0;

    private static List<WageTransaction> txnList = new ArrayList<>();
    private static VBox txnListContainer;
    private static Text availBalanceText;
    private static Text withdrawnText;

    static {
        initTransactions();
    }

    private static void initTransactions() {
        txnList.clear();
        totalLifetime = 0;
        availableBalance = 0;
        pendingEscrow = 0;
        totalWithdrawn = 0;
        thisMonthEarnings = 0;
        completedJobsCount = 0;

        String currentMonth = java.time.LocalDate.now().getMonth().name().substring(0, 3); // e.g. "SEP"

        try {
            String opEmail = OperatorProfileStore.email;
            if (opEmail == null || opEmail.trim().isEmpty()) return;
            List<RentalRequestModel> list = new RentalRequestDAO().getRequestsByOperator(opEmail);
            for (RentalRequestModel r : list) {
                int wage = r.getOperatorAmount() > 0 ? r.getOperatorAmount() : (500 * Math.max(1, r.getDays()));
                String st = r.getStatus() != null ? r.getStatus().toUpperCase() : "PENDING";

                if ("COMPLETED".equals(st)) {
                    totalLifetime += wage;
                    completedJobsCount++;
                    if (r.getStartDate() != null && r.getStartDate().toUpperCase().contains(currentMonth)) {
                        thisMonthEarnings += wage;
                    }
                    txnList.add(new WageTransaction(
                            "#" + (r.getRequestId() != null ? r.getRequestId() : "TXN"),
                            "Shift Wage: " + (r.getMachineryName() != null ? r.getMachineryName() : "Field Operation") + " (" + (r.getFarmerName() != null ? r.getFarmerName() : "Farmer") + ")",
                            r.getStartDate() != null ? r.getStartDate() : "Recent",
                            "Platform Escrow Direct Settlement",
                            wage,
                            "CREDIT",
                            "SETTLED"
                    ));
                } else if ("ACTIVE".equals(st) || "CONFIRMED".equals(st)) {
                    pendingEscrow += wage;
                }
            }

            // Withdrawals from PayoutDAO
            List<PayoutModel> payouts = new PayoutDAO().getPayoutsByUser(opEmail);
            for (PayoutModel po : payouts) {
                if ("PAID".equalsIgnoreCase(po.getStatus()) && po.getTransactionReference() != null && po.getTransactionReference().contains("WITHDRAW")) {
                    totalWithdrawn += po.getAmount();
                }
            }
        } catch (Exception ignored) {}
        availableBalance = Math.max(0, totalLifetime - totalWithdrawn);
    }

    public static ScrollPane getEarningsSection(StackPane root) {
        initTransactions();

        Text headerTitle = new Text("Operator Wages & Earnings Settlement");
        headerTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text headerSubtitle = new Text("Track your daily operator wages, per-acre incentives, verified timesheet payouts, and initiate instant bank withdrawals.");
        headerSubtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(4, headerTitle, headerSubtitle);

        Button withdrawBtn = new Button("💸  Withdraw Wages to Bank");
        withdrawBtn.setPrefHeight(42);
        withdrawBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 20 0 20;");
        withdrawBtn.setOnAction(e -> showWithdrawModal(root));

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox topBar = new HBox(titleBox, topSpacer, withdrawBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // 5 KPI Financial Cards (Requirement 8)
        HBox metricRow = createFinancialMetrics();

        // Dynamic Operator Charts (Requirement 8)
        HBox chartsRow = createOperatorCharts();

        // Transaction History Table Section (Requirement 9)
        VBox txnSection = createTransactionHistorySection(root);

        VBox content = new VBox(22, topBar, metricRow, chartsRow, txnSection);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static HBox createFinancialMetrics() {
        VBox c1 = createMetricCard("💰 Total Earnings", "₹" + String.format("%,d", totalLifetime), completedJobsCount + " jobs completed", "#1B4332");

        availBalanceText = new Text("₹" + String.format("%,d", availableBalance));
        availBalanceText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #2E7D32;");
        VBox c2 = createCustomMetricCard("🏦 Available for Payout", availBalanceText, "Instant 24/7 bank transfer", "#2E7D32");

        VBox c3 = createMetricCard("⏱ Pending Earnings", "₹" + String.format("%,d", pendingEscrow), "In active field escrow", "#E65100");

        withdrawnText = new Text("₹" + String.format("%,d", totalWithdrawn));
        withdrawnText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #374151;");
        VBox c4 = createCustomMetricCard("💳 Paid Out", withdrawnText, "Transferred to bank", "#374151");

        VBox c5 = createMetricCard("📅 This Month", "₹" + String.format("%,d", thisMonthEarnings), "Current calendar month", "#2D6A4F");

        HBox row = new HBox(12, c1, c2, c3, c4, c5);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(c1, Priority.ALWAYS);
        HBox.setHgrow(c2, Priority.ALWAYS);
        HBox.setHgrow(c3, Priority.ALWAYS);
        HBox.setHgrow(c4, Priority.ALWAYS);
        HBox.setHgrow(c5, Priority.ALWAYS);
        return row;
    }

    private static VBox createMetricCard(String title, String value, String sub, String color) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        Text v = new Text(value);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #374151;");

        VBox b = new VBox(6, t, v, s);
        b.setPadding(new Insets(16));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    private static VBox createCustomMetricCard(String title, Text vText, String sub, String color) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #374151;");

        VBox b = new VBox(6, t, vText, s);
        b.setPadding(new Insets(16));
        b.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
        return b;
    }

    // =========================================================
    // Dynamic Operator Graphs (Requirement 8)
    // =========================================================
    private static HBox createOperatorCharts() {
        VBox monthlyChart = createMonthlyEarningsChart();
        VBox bookingChart = createEarningsByBookingChart();
        VBox statusChart = createPendingVsPaidChart();

        monthlyChart.setMinWidth(0);
        bookingChart.setMinWidth(0);
        statusChart.setMinWidth(0);

        HBox.setHgrow(monthlyChart, Priority.ALWAYS);
        HBox.setHgrow(bookingChart, Priority.ALWAYS);
        HBox.setHgrow(statusChart, Priority.ALWAYS);

        HBox row = new HBox(16, monthlyChart, bookingChart, statusChart);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private static VBox createMonthlyEarningsChart() {
        Text title = new Text("📈 Monthly Wage Trend");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Shift wages earned across 2026 calendar months");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Wages (₹)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setAnimated(false);
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(240);
        barChart.setMinHeight(240);

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        java.util.Map<String, Integer> monthTotals = new java.util.LinkedHashMap<>();
        for (String m : months) monthTotals.put(m, 0);

        for (WageTransaction t : txnList) {
            if ("CREDIT".equals(t.type) && t.date != null) {
                for (String m : months) {
                    if (t.date.toLowerCase().contains(m.toLowerCase())) {
                        monthTotals.put(m, monthTotals.get(m) + t.amount);
                        break;
                    }
                }
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (String m : months) {
            series.getData().add(new XYChart.Data<>(m, monthTotals.get(m)));
        }
        barChart.getData().add(series);

        VBox card = new VBox(8, new VBox(2, title, sub), barChart);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createEarningsByBookingChart() {
        Text title = new Text("🥧 Wages by Machine Type");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Operator earnings per equipment category");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        java.util.Map<String, Integer> catMap = new java.util.HashMap<>();
        for (WageTransaction t : txnList) {
            if ("CREDIT".equals(t.type)) {
                String desc = t.title != null ? t.title.replaceAll("Shift Wage:\\s*", "") : "Field Operation";
                if (desc.contains("(")) desc = desc.substring(0, desc.indexOf("(")).trim();
                catMap.put(desc, catMap.getOrDefault(desc, 0) + t.amount);
            }
        }

        if (catMap.isEmpty()) {
            VBox emptyBox = new VBox(8);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPrefHeight(240);
            Text emptyIco = new Text("🚜");
            emptyIco.setStyle("-fx-font-size: 32px;");
            Text emptyTxt = new Text("No completed jobs yet.");
            emptyTxt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");
            Text emptySub = new Text("Completed job wage shares will display here.");
            emptySub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #6B7280;");
            emptyBox.getChildren().addAll(emptyIco, emptyTxt, emptySub);

            VBox card = new VBox(8, new VBox(2, title, sub), emptyBox);
            card.setPadding(new Insets(16));
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
            return card;
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (java.util.Map.Entry<String, Integer> entry : catMap.entrySet()) {
            pieData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        PieChart pieChart = new PieChart(pieData);
        pieChart.setAnimated(false);
        pieChart.setLegendVisible(true);
        pieChart.setPrefHeight(240);
        pieChart.setMinHeight(240);

        VBox card = new VBox(8, new VBox(2, title, sub), pieChart);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createPendingVsPaidChart() {
        Text title = new Text("📊 Pending vs Paid Wages");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text sub = new Text("Settled earnings vs active shift escrow");
        sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        if (totalLifetime == 0 && pendingEscrow == 0) {
            VBox emptyBox = new VBox(8);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPrefHeight(240);
            Text emptyIco = new Text("💵");
            emptyIco.setStyle("-fx-font-size: 32px;");
            Text emptyTxt = new Text("No earnings recorded.");
            emptyTxt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");
            Text emptySub = new Text("Pending and paid wage breakdown will appear here.");
            emptySub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #6B7280;");
            emptyBox.getChildren().addAll(emptyIco, emptyTxt, emptySub);

            VBox card = new VBox(8, new VBox(2, title, sub), emptyBox);
            card.setPadding(new Insets(16));
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
            return card;
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        if (totalLifetime > 0) pieData.add(new PieChart.Data("Settled Wages (₹" + totalLifetime + ")", totalLifetime));
        if (pendingEscrow > 0) pieData.add(new PieChart.Data("In Escrow (₹" + pendingEscrow + ")", pendingEscrow));

        PieChart pieChart = new PieChart(pieData);
        pieChart.setAnimated(false);
        pieChart.setLegendVisible(true);
        pieChart.setPrefHeight(240);
        pieChart.setMinHeight(240);

        VBox card = new VBox(8, new VBox(2, title, sub), pieChart);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");
        return card;
    }

    private static VBox createTransactionHistorySection(StackPane root) {
        Text title = new Text("Wage Settlement & Bank Transfer History");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        txnListContainer = new VBox(10);
        renderTxnList(root);

        return new VBox(12, title, txnListContainer);
    }

    private static void renderTxnList(StackPane root) {
        txnListContainer.getChildren().clear();

        if (txnList.isEmpty()) {
            VBox emptyBox = new VBox(8);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(30));
            emptyBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12;");
            Text emptyIco = new Text("💳");
            emptyIco.setStyle("-fx-font-size: 32px;");
            Text empty = new Text("No completed jobs yet.");
            empty.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");
            Text subEmpty = new Text("When you complete assigned operator shifts, your wage settlement records will appear here.");
            subEmpty.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #6B7280;");
            emptyBox.getChildren().addAll(emptyIco, empty, subEmpty);
            txnListContainer.getChildren().add(emptyBox);
            return;
        }

        for (WageTransaction txn : txnList) {
            Text id = new Text(txn.txnId);
            id.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #2D6A4F;");

            Text desc = new Text(txn.title);
            desc.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");

            Text dt = new Text("📅 " + txn.date + " • " + txn.channel);
            dt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #4B5563;");

            VBox infoBox = new VBox(2, id, desc, dt);

            boolean isCredit = "CREDIT".equals(txn.type);
            Text amt = new Text((isCredit ? "+ ₹" : "- ₹") + String.format("%,d", txn.amount));
            amt.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: " + (isCredit ? "#2E7D32" : "#8B3A3A") + ";");

            Label status = new Label(txn.status);
            status.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 4;");

            Button receipt = new Button("📄 Pay Slip");
            receipt.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
            receipt.setOnAction(e -> showPaySlipModal(txn, root));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox row = new HBox(20, infoBox, spacer, amt, status, receipt);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");

            txnListContainer.getChildren().add(row);
        }
    }

    private static void showPaySlipModal(WageTransaction txn, StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.55);");

        VBox modal = new VBox(14);
        modal.setPrefWidth(460);
        modal.setMaxWidth(460);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 16;");

        Text appName = new Text("🌱 FarmEquip Operator Wage Slip");
        appName.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 17px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Text subtitle = new Text("Verified Timesheet & Escrow Wage Settlement");
        subtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 0, 10, 0));

        grid.add(createLabel("Settlement ID:"), 0, 0);
        grid.add(new Text(txn.txnId), 1, 0);

        grid.add(createLabel("Operation Title:"), 0, 1);
        grid.add(new Text(txn.title), 1, 1);

        grid.add(createLabel("Shift Date:"), 0, 2);
        grid.add(new Text(txn.date), 1, 2);

        grid.add(createLabel("Disbursement Channel:"), 0, 3);
        grid.add(new Text(txn.channel), 1, 3);

        grid.add(createLabel("Payment Status:"), 0, 4);
        Label stLabel = new Label("✔ " + txn.status);
        stLabel.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 4;");
        grid.add(stLabel, 1, 4);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Text totalTitle = new Text("Total Wage Amount:");
        totalTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");
        Text totalValue = new Text("₹" + String.format("%,d", txn.amount));
        totalValue.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        HBox totalRow = new HBox(totalTitle, spacer, totalValue);
        totalRow.setAlignment(Pos.CENTER_LEFT);
        totalRow.setPadding(new Insets(10, 14, 10, 14));
        totalRow.setStyle("-fx-background-color: #F8FAF9; -fx-background-radius: 8; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 8;");

        Button closeBtn = new Button("Close Slip");
        closeBtn.setStyle("-fx-background-color: #374151; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 20;");
        closeBtn.setOnAction(e -> root.getChildren().remove(overlay));

        HBox btnBox = new HBox(closeBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        modal.getChildren().addAll(new VBox(2, appName, subtitle), grid, totalRow, btnBox);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private static void showWithdrawModal(StackPane root) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox modal = new VBox(15);
        modal.setPrefWidth(460);
        modal.setMaxWidth(460);
        modal.setPadding(new Insets(24));
        modal.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0.3, 0, 8);");

        Text title = new Text("Withdraw Operator Wages to Bank");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text avail = new Text("Available Balance: ₹" + String.format("%,d", availableBalance));
        avail.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);

        TextField amountField = new TextField("5000");
        amountField.setPrefHeight(36);

        ComboBox<String> bankSelect = new ComboBox<>();
        bankSelect.getItems().addAll(
            "State Bank of India (A/C •••• 4120, IFSC: SBIN0004512)",
            "Bank of Maharashtra (A/C •••• 9812, IFSC: MAHB0001092)",
            "UPI Direct: ramesh.operator@okhdfcbank"
        );
        bankSelect.setValue("State Bank of India (A/C •••• 4120, IFSC: SBIN0004512)");
        bankSelect.setPrefWidth(280);

        ComboBox<String> modeSelect = new ComboBox<>();
        modeSelect.getItems().addAll("Instant IMPS (Immediate Credit)", "UPI Transfer (PhonePe / GPay)", "NEFT Settlement");
        modeSelect.setValue("Instant IMPS (Immediate Credit)");
        modeSelect.setPrefWidth(280);

        form.add(createLabel("Payout Amount (₹):"), 0, 0);
        form.add(amountField, 1, 0);

        form.add(createLabel("Payout Account:"), 0, 1);
        form.add(bankSelect, 1, 1);

        form.add(createLabel("Transfer Method:"), 0, 2);
        form.add(modeSelect, 1, 2);

        Button submitBtn = new Button("Confirm & Transfer Wages");
        submitBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        submitBtn.setPrefHeight(36);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        cancelBtn.setPrefHeight(36);

        submitBtn.setOnAction(e -> {
            try {
                int amt = Integer.parseInt(amountField.getText().trim());
                if (amt > 0 && amt <= availableBalance) {
                    availableBalance -= amt;
                    totalWithdrawn += amt;
                    availBalanceText.setText("₹" + String.format("%,d", availableBalance));
                    withdrawnText.setText("₹" + String.format("%,d", totalWithdrawn));
                    txnList.add(0, new WageTransaction("#WD-OP-" + (205 + txnList.size()), "Wage Payout to " + bankSelect.getValue().substring(0, 10), "Today (Instant)", modeSelect.getValue().substring(0, 12), amt, "WITHDRAWAL", "SETTLED"));
                    renderTxnList(root);
                    new Thread(() -> {
                        try {
                            new PayoutDAO().recordPayout(new PayoutModel("PO_OP_WD_" + System.currentTimeMillis(), OperatorProfileStore.email, "OPERATOR", null, null, amt, "PAID", "WITHDRAWAL_SETTLED", "Bank IMPS"));
                        } catch (Exception ignored) {}
                    }).start();
                    root.getChildren().remove(overlay);
                }
            } catch (Exception ignored) {}
        });

        cancelBtn.setOnAction(e -> root.getChildren().remove(overlay));

        HBox btnBox = new HBox(10, submitBtn, cancelBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        modal.getChildren().addAll(title, avail, form, btnBox);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private static Label createLabel(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        return l;
    }
}
