package com.desgin.view.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.desgin.dao.RentalRequestDAO;
import com.desgin.model.RentalRequestModel;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PaymentsDetail {

    private static List<RentalRequestModel> allRequests = new ArrayList<>();
    private static VBox transactionsContainer;
    private static String currentTab = "ALL"; // "ALL", "FARMER", "PROVIDER", "OPERATOR", "FAILED"
    private static String currentSearch = "";

    // KPI text nodes
    private static Text totalFarmerSpentText;
    private static Text totalProviderRevenueText;
    private static Text totalOperatorIncomeText;
    private static Text totalAdminCommissionText;

    private static String cachedTotalFarmerSpent = "₹22,950";
    private static String cachedTotalProviderRevenue = "₹21,344";
    private static String cachedTotalOperatorIncome = "₹0";
    private static String cachedTotalAdminCommission = "₹1,606";

    public static ScrollPane getPage(StackPane root) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20, 26, 35, 26));
        content.setMinWidth(0);
        content.setMaxWidth(Double.MAX_VALUE);
        content.setStyle("-fx-background-color: transparent;");

        // 1. 4 Master Financial KPI Cards
        GridPane kpiGrid = createRevenueKPIGrid();

        // 2. Ledger Section with Role Tabs & Search Bar
        VBox ledgerSection = createLedgerSection(root);

        content.getChildren().addAll(kpiGrid, ledgerSection);

        // Fetch live Firestore records
        loadFirestorePayments();

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static GridPane createRevenueKPIGrid() {
        totalFarmerSpentText = new Text(cachedTotalFarmerSpent);
        totalProviderRevenueText = new Text(cachedTotalProviderRevenue);
        totalOperatorIncomeText = new Text(cachedTotalOperatorIncome);
        totalAdminCommissionText = new Text(cachedTotalAdminCommission);

        VBox c1 = createMetricCard("🧑‍🌾 Total Farmer Spent", totalFarmerSpentText, "Total paid across all bookings");
        VBox c2 = createMetricCard("🚜 Total Provider Revenue", totalProviderRevenueText, "93% net payout to fleet owners");
        VBox c3 = createMetricCard("👨‍🔧 Total Operator Income", totalOperatorIncomeText, "Operator machinery duty income");
        VBox c4 = createMetricCard("🏦 Platform Commission & Tax", totalAdminCommissionText, "Direct admin revenue & taxes");

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

    private static VBox createMetricCard(String title, Text val, String sub) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563; -fx-font-weight: 600;");

        val.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #1B4332;");
        Text s = new Text(sub);
        s.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-fill: #6B7280;");

        VBox card = new VBox(6, t, val, s);
        card.setPadding(new Insets(16));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 8, 0, 0, 2);");

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #F8FAF8; -fx-background-radius: 12; -fx-border-color: #2D6A4F; -fx-border-width: 1.5; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(27,67,50,0.14), 8, 0.2, 0, 2);");
            card.setTranslateY(-2);
        });

        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 8, 0, 0, 2);");
            card.setTranslateY(0);
        });

        return card;
    }

    private static VBox createLedgerSection(StackPane root) {
        VBox card = new VBox(16);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 10, 0, 0, 2);");

        // Role Tabs (ALL, FARMER, PROVIDER, OPERATOR, FAILED)
        HBox tabBox = new HBox(8);
        tabBox.setAlignment(Pos.CENTER_LEFT);

        String[] tabs = {"ALL", "FARMER", "PROVIDER", "OPERATOR", "FAILED"};
        String[] tabLabels = {"📑 All Payments", "🧑‍🌾 Farmer Payments", "🚜 Provider Payments", "👨‍🔧 Operator Payments", "❌ Failed Payments"};
        List<Button> tabButtons = new ArrayList<>();

        for (int i = 0; i < tabs.length; i++) {
            final String tabKey = tabs[i];
            Button btn = new Button(tabLabels[i]);
            styleTabBtn(btn, tabKey.equals(currentTab));
            btn.setOnAction(e -> {
                currentTab = tabKey;
                for (int j = 0; j < tabButtons.size(); j++) {
                    styleTabBtn(tabButtons.get(j), tabs[j].equals(currentTab));
                }
                renderTransactionsList(root);
            });
            tabButtons.add(btn);
            tabBox.getChildren().add(btn);
        }

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search by ID, Farmer, Provider, Operator or Machine...");
        searchField.setPrefWidth(320);
        searchField.setPrefHeight(36);
        searchField.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-background-color: #F9FAFB; -fx-border-color: #D1D5DB; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 0 10;");
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            currentSearch = newV != null ? newV.trim().toLowerCase() : "";
            renderTransactionsList(root);
        });

        HBox topBar = new HBox(10, tabBox, topSpacer, searchField);
        topBar.setAlignment(Pos.CENTER_LEFT);

        transactionsContainer = new VBox(12);
        transactionsContainer.setMinWidth(0);
        transactionsContainer.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(topBar, transactionsContainer);
        return card;
    }

    private static void styleTabBtn(Button btn, boolean selected) {
        if (selected) {
            btn.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 8; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: #F3F4F6; -fx-text-fill: #4B5563; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: 500; -fx-padding: 6 12; -fx-background-radius: 8; -fx-cursor: hand;");
        }
    }

    private static void loadFirestorePayments() {
        new Thread(() -> {
            try {
                List<RentalRequestModel> reqs = new RentalRequestDAO().getAllRequests();
                Platform.runLater(() -> {
                    allRequests.clear();
                    allRequests.addAll(reqs);
                    updateSummaryFigures();
                    renderTransactionsList(null);
                });
            } catch (Exception e) {
                System.err.println("Notice: Failed to load financial records from Firestore: " + e.getMessage());
            }
        }).start();
    }

    private static void updateSummaryFigures() {
        int totalFarmerSpent = 0;
        int totalProviderRevenue = 0;
        int totalOperatorIncome = 0;
        int totalAdminCommission = 0;

        for (RentalRequestModel r : allRequests) {
            boolean isFailed = "CANCELLED".equalsIgnoreCase(r.getStatus()) || "DECLINED".equalsIgnoreCase(r.getStatus()) || "FAILED".equalsIgnoreCase(r.getPaymentStatus()) || "REFUNDED".equalsIgnoreCase(r.getPaymentStatus());
            if (isFailed) continue; // No revenue or commission for failed payments

            int totalPaid = r.getTotalAmount() > 0 ? r.getTotalAmount() : (r.getDailyRate() * Math.max(1, r.getDays()));
            int eqAmt = r.getEquipmentAmount() > 0 ? r.getEquipmentAmount() : totalPaid;
            int opAmt = r.getOperatorAmount();
            if (opAmt == 0 && r.isOperatorRequired()) {
                opAmt = 500 * Math.max(1, r.getDays());
            }

            int platformCut = (int) (eqAmt * 0.07);
            int provNet = eqAmt - platformCut;

            totalFarmerSpent += totalPaid;
            totalProviderRevenue += provNet;
            totalOperatorIncome += opAmt;
            totalAdminCommission += platformCut;
        }

        cachedTotalFarmerSpent = "₹" + String.format(Locale.ENGLISH, "%,d", totalFarmerSpent);
        cachedTotalProviderRevenue = "₹" + String.format(Locale.ENGLISH, "%,d", totalProviderRevenue);
        cachedTotalOperatorIncome = "₹" + String.format(Locale.ENGLISH, "%,d", totalOperatorIncome);
        cachedTotalAdminCommission = "₹" + String.format(Locale.ENGLISH, "%,d", totalAdminCommission);

        if (totalFarmerSpentText != null) totalFarmerSpentText.setText(cachedTotalFarmerSpent);
        if (totalProviderRevenueText != null) totalProviderRevenueText.setText(cachedTotalProviderRevenue);
        if (totalOperatorIncomeText != null) totalOperatorIncomeText.setText(cachedTotalOperatorIncome);
        if (totalAdminCommissionText != null) totalAdminCommissionText.setText(cachedTotalAdminCommission);
    }

    private static void renderTransactionsList(StackPane root) {
        if (transactionsContainer == null) return;
        transactionsContainer.getChildren().clear();

        List<RentalRequestModel> filtered = new ArrayList<>();
        for (RentalRequestModel r : allRequests) {
            String stat = r.getStatus() != null ? r.getStatus().toUpperCase() : "PENDING";
            String pStat = r.getPaymentStatus() != null ? r.getPaymentStatus().toUpperCase() : "ESCROW HELD";
            boolean isFailed = "CANCELLED".equals(stat) || "DECLINED".equals(stat) || "REFUNDED".equals(pStat) || "FAILED".equals(pStat);

            // Tab filtering
            if ("OPERATOR".equals(currentTab)) {
                boolean hasOp = r.isOperatorRequired() || (r.getOperatorName() != null && !r.getOperatorName().trim().isEmpty());
                if (!hasOp) continue;
            } else if ("FAILED".equals(currentTab)) {
                if (!isFailed) continue;
            } else if ("FARMER".equals(currentTab) || "PROVIDER".equals(currentTab) || "ALL".equals(currentTab)) {
                if (isFailed && !"FAILED".equals(currentTab) && !"ALL".equals(currentTab)) continue;
            }

            // Search filtering
            if (!currentSearch.isEmpty()) {
                String id = (r.getRequestId() != null ? r.getRequestId() : "").toLowerCase();
                String eq = (r.getMachineryName() != null ? r.getMachineryName() : "").toLowerCase();
                String farm = (r.getFarmerName() != null ? r.getFarmerName() : (r.getFarmerEmail() != null ? r.getFarmerEmail() : "")).toLowerCase();
                String prov = (r.getProviderName() != null ? r.getProviderName() : (r.getProviderEmail() != null ? r.getProviderEmail() : "")).toLowerCase();
                String op = (r.getOperatorName() != null ? r.getOperatorName() : "").toLowerCase();
                if (!id.contains(currentSearch) && !eq.contains(currentSearch) && !farm.contains(currentSearch) && !prov.contains(currentSearch) && !op.contains(currentSearch)) {
                    continue;
                }
            }
            filtered.add(r);
        }

        if (filtered.isEmpty()) {
            VBox empty = new VBox(8);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(30));
            Text icon = new Text("FAILED".equals(currentTab) ? "✅" : "💳");
            icon.setStyle("-fx-font-size: 32px;");
            Text title = new Text("FAILED".equals(currentTab) ? "No Failed Payments" : "No Payment Records Found");
            title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #1B4332;");
            Text sub = new Text("FAILED".equals(currentTab) ? "All platform transactions are paid and processed cleanly." : "No financial transactions match the selected filter.");
            sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");
            empty.getChildren().addAll(icon, title, sub);
            transactionsContainer.getChildren().add(empty);
            return;
        }

        for (RentalRequestModel r : filtered) {
            transactionsContainer.getChildren().add(createHorizontalPaymentCard(r));
        }
    }

    // =========================================================================
    // HORIZONTAL PAYMENT CARD:
    // Farmer Details | Provider Cost | Operator Cost (with reason) | Right-Most Admin Cut | Status Badge (PAID / FAILED)
    // =========================================================================
    private static VBox createHorizontalPaymentCard(RentalRequestModel r) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(14, 16, 14, 16));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E2EBE5; -fx-border-radius: 12; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.02), 6, 0, 0, 2);");

        String reqId = r.getRequestId() != null ? r.getRequestId() : "REQ_UNKNOWN";
        String machName = r.getMachineryName() != null ? r.getMachineryName() : "Equipment";
        int days = Math.max(1, r.getDays());
        int totalPaid = r.getTotalAmount() > 0 ? r.getTotalAmount() : (r.getDailyRate() * days);
        int eqAmt = r.getEquipmentAmount() > 0 ? r.getEquipmentAmount() : totalPaid;
        int opAmt = r.getOperatorAmount() > 0 ? r.getOperatorAmount() : (r.isOperatorRequired() ? 500 * days : 0);

        boolean isFailed = "CANCELLED".equalsIgnoreCase(r.getStatus()) || "DECLINED".equalsIgnoreCase(r.getStatus()) || "FAILED".equalsIgnoreCase(r.getPaymentStatus()) || "REFUNDED".equalsIgnoreCase(r.getPaymentStatus());

        int platformCut = isFailed ? 0 : (int) (eqAmt * 0.07);
        int provNet = isFailed ? 0 : (eqAmt - platformCut);

        String fName = r.getFarmerName() != null && !r.getFarmerName().isEmpty() ? r.getFarmerName() : (r.getFarmerEmail() != null ? r.getFarmerEmail() : "Farmer");
        String fPhone = r.getFarmerPhone() != null && !r.getFarmerPhone().isEmpty() ? r.getFarmerPhone() : "N/A";
        String pName = r.getProviderName() != null && !r.getProviderName().isEmpty() ? r.getProviderName() : "Equipment Provider";
        String opName = r.getOperatorName() != null && !r.getOperatorName().isEmpty() ? r.getOperatorName() : (r.isOperatorRequired() ? "Assigned Operator" : "None");

        // 1. Top Bar: ID, Machinery, Date, and ONLY PAID or FAILED badge
        Text idText = new Text("🆔 " + reqId);
        idText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text machText = new Text("• 🚜 " + machName + " (" + days + " Days)");
        machText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #374151; -fx-font-weight: 600;");

        Text dateInfo = new Text("• 📅 " + (r.getCreatedAt() != null ? r.getCreatedAt() : "Recent"));
        dateInfo.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #9CA3AF;");

        Region topSp = new Region();
        HBox.setHgrow(topSp, Priority.ALWAYS);

        // ONLY PAID or FAILED badge (No settled, disbursed, etc.)
        Label statusBadge = new Label(isFailed ? "FAILED" : "PAID");
        if (isFailed) {
            statusBadge.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 10; -fx-background-radius: 6;");
        } else {
            statusBadge.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #15803D; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 10; -fx-background-radius: 6;");
        }

        HBox headerRow = new HBox(8, idText, machText, dateInfo, topSp, statusBadge);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        // 2. Horizontal Columns Row: Farmer Details | Provider Cost | Operator Cost (With Reason) | Right-Most Admin Cut
        HBox columnsRow = new HBox(16);
        columnsRow.setAlignment(Pos.CENTER_LEFT);
        columnsRow.setPadding(new Insets(10, 14, 10, 14));
        columnsRow.setStyle("-fx-background-color: #F9FAFB; -fx-background-radius: 10; -fx-border-color: #E5E7EB; -fx-border-radius: 10; -fx-border-width: 1;");

        // Column 1: Farmer Details & Total Paid
        VBox col1 = new VBox(3);
        Text c1Header = new Text("🧑‍🌾 Farmer Details");
        c1Header.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #1E3A8A;");
        Text c1Name = new Text(fName + " (Ph: " + fPhone + ")");
        c1Name.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #374151;");
        Text c1Paid = new Text("Total Paid: ₹" + String.format(Locale.ENGLISH, "%,d", totalPaid));
        c1Paid.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #1E3A8A;");
        col1.getChildren().addAll(c1Header, c1Name, c1Paid);
        col1.setPrefWidth(220);

        // Column 2: Provider Equipment Cost
        VBox col2 = new VBox(3);
        Text c2Header = new Text("🚜 Provider Cost (Equipment)");
        c2Header.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #15803D;");
        Text c2Name = new Text(pName);
        c2Name.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #374151;");
        Text c2Cost = new Text(isFailed ? "Equipment Cost: ₹0 (Failed)" : "Gross: ₹" + String.format(Locale.ENGLISH, "%,d", eqAmt) + " • Net: ₹" + String.format(Locale.ENGLISH, "%,d", provNet));
        c2Cost.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: #15803D;");
        col2.getChildren().addAll(c2Header, c2Name, c2Cost);
        col2.setPrefWidth(220);

        // Column 3: Operator Cost & Purpose / Why Booked
        VBox col3 = new VBox(3);
        Text c3Header = new Text("👨‍🔧 Operator Cost & Duty");
        c3Header.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #B45309;");
        
        // Purpose of booking (e.g. machinery name / duty)
        String dutyReason = machName.contains("Tractor") || machName.contains("Plowing") || machName.contains("Hallar") ? "Field Operation (" + machName + ")" : "Machinery Operation";
        Text c3Name = new Text(opName + " • " + dutyReason);
        c3Name.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #374151;");
        
        Text c3Cost = new Text(opAmt > 0 ? (isFailed ? "Wages: ₹0 (Failed)" : "Wages Paid: ₹" + String.format(Locale.ENGLISH, "%,d", opAmt)) : "No Operator Required");
        c3Cost.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: " + (opAmt > 0 ? "#B45309" : "#6B7280") + ";");
        col3.getChildren().addAll(c3Header, c3Name, c3Cost);
        col3.setPrefWidth(220);

        Region colSpacer = new Region();
        HBox.setHgrow(colSpacer, Priority.ALWAYS);

        // Column 4 (Right-Most): Admin Earned (7%) - Shows ₹0 if failed
        VBox col4 = new VBox(3);
        col4.setAlignment(Pos.CENTER_RIGHT);
        Text c4Header = new Text("🏦 Admin Cut (7%)");
        c4Header.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #047857;");
        Text c4Earned = new Text(isFailed ? "₹0" : "+₹" + String.format(Locale.ENGLISH, "%,d", platformCut));
        c4Earned.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: " + (isFailed ? "#DC2626" : "#047857") + ";");
        Text c4Sub = new Text(isFailed ? "No Cut on Failed" : "Platform Tax & Cut");
        c4Sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10px; -fx-fill: #6B7280;");
        col4.getChildren().addAll(c4Header, c4Earned, c4Sub);

        columnsRow.getChildren().addAll(col1, col2, col3, colSpacer, col4);

        card.getChildren().addAll(headerRow, columnsRow);
        return card;
    }
}
