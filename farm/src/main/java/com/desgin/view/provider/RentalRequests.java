package com.desgin.view.provider;

import java.util.ArrayList;
import java.util.List;

import com.desgin.dao.MachineryDAO;
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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.Node;

public class RentalRequests {

    private static List<RentalRequestModel> requestsList = new ArrayList<>();
    private static VBox listContainer;
    private static String activeTab = "PENDING";
    private static String searchFilter = "";
    private static Label syncStatusLabel;
    private static HBox tabBoxRef;

    public static ScrollPane getRequestsSection(StackPane root) {
        Text headerTitle = new Text("Rental Requests & Bookings");
        headerTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text headerSubtitle = new Text("Manage incoming farmer rental requests, confirm equipment schedules, and track active field deployments.");
        headerSubtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(4, headerTitle, headerSubtitle);

        Button refreshBtn = new Button("🔄 Refresh Requests");
        refreshBtn.setPrefHeight(38);
        refreshBtn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-border-color: #C2E0CE; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 16;");
        refreshBtn.setOnAction(e -> loadRequestsFromFirestore(root));

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox topBar = new HBox(titleBox, topSpacer, refreshBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Search Bar & Status
        TextField searchInput = new TextField();
        searchInput.setPromptText("Search by Farmer, Location, Machine or ID...");
        searchInput.setPrefHeight(38);
        searchInput.setPrefWidth(380);
        searchInput.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-radius: 8; -fx-font-family: 'Poppins'; -fx-font-size: 13px;");
        searchInput.textProperty().addListener((obs, oldV, newV) -> {
            searchFilter = newV.toLowerCase().trim();
            renderRequestsList(root);
        });

        syncStatusLabel = new Label("Live Sync");
        syncStatusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #2E7D32; -fx-font-weight: bold;");

        HBox searchRow = new HBox(14, searchInput, syncStatusLabel);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        // Tab Navigation
        tabBoxRef = createTabNavigation(root);

        // Container for request cards
        listContainer = new VBox(14);

        // Load requests
        loadRequestsFromFirestore(root);

        VBox content = new VBox(18, topBar, searchRow, tabBoxRef, listContainer);
        content.setPadding(new Insets(22, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    public static void loadRequestsFromFirestore(StackPane root) {
        if (syncStatusLabel != null) syncStatusLabel.setText("Updating...");
        Thread bg = new Thread(() -> {
            try {
                RentalRequestDAO dao = new RentalRequestDAO();
                String providerEmail = ProviderProfileStore.email;
                List<RentalRequestModel> fetched = dao.getRequestsByProvider(providerEmail);

                if (fetched.isEmpty()) {
                    fetched = dao.getAllRequests();
                }

                List<RentalRequestModel> finalFetched = fetched;
                Platform.runLater(() -> {
                    requestsList.clear();
                    requestsList.addAll(finalFetched);
                    if (syncStatusLabel != null) {
                        syncStatusLabel.setText("✓ " + finalFetched.size() + " Requests");
                    }
                    updateTabCounts(root);
                    renderRequestsList(root);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    if (syncStatusLabel != null) syncStatusLabel.setText("Offline");
                });
            }
        });
        bg.setDaemon(true);
        bg.start();
    }

    private static HBox createTabNavigation(StackPane root) {
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER_LEFT);
        updateTabButtons(bar, root);
        return bar;
    }

    private static void updateTabCounts(StackPane root) {
        if (tabBoxRef != null) {
            updateTabButtons(tabBoxRef, root);
        }
    }

    private static void updateTabButtons(HBox bar, StackPane root) {
        bar.getChildren().clear();

        long pendingCount = requestsList.stream().filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count();
        long activeCount = requestsList.stream().filter(r -> "APPROVED".equalsIgnoreCase(r.getStatus()) || "ACTIVE".equalsIgnoreCase(r.getStatus()) || "ACCEPTED".equalsIgnoreCase(r.getStatus()) || "CONFIRMED".equalsIgnoreCase(r.getStatus())).count();
        long completedCount = requestsList.stream().filter(r -> "COMPLETED".equalsIgnoreCase(r.getStatus()) || "DECLINED".equalsIgnoreCase(r.getStatus()) || "CANCELLED".equalsIgnoreCase(r.getStatus()) || "REJECTED".equalsIgnoreCase(r.getStatus())).count();

        Button tabPending = new Button("Pending Approvals (" + pendingCount + ")");
        Button tabActive = new Button("Active On-Field (" + activeCount + ")");
        Button tabCompleted = new Button("Completed History (" + completedCount + ")");

        styleTabButton(tabPending, "PENDING".equalsIgnoreCase(activeTab));
        styleTabButton(tabActive, "ACTIVE".equalsIgnoreCase(activeTab));
        styleTabButton(tabCompleted, "COMPLETED".equalsIgnoreCase(activeTab));

        tabPending.setOnAction(e -> {
            activeTab = "PENDING";
            updateTabButtons(bar, root);
            renderRequestsList(root);
        });

        tabActive.setOnAction(e -> {
            activeTab = "ACTIVE";
            updateTabButtons(bar, root);
            renderRequestsList(root);
        });

        tabCompleted.setOnAction(e -> {
            activeTab = "COMPLETED";
            updateTabButtons(bar, root);
            renderRequestsList(root);
        });

        bar.getChildren().addAll(tabPending, tabActive, tabCompleted);
    }

    private static void styleTabButton(Button btn, boolean active) {
        if (active) {
            btn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 7 16; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-border-color: #E2EBE5; -fx-border-radius: 8; -fx-padding: 7 16; -fx-cursor: hand;");
        }
    }

    private static void renderRequestsList(StackPane root) {
        if (listContainer == null) return;
        listContainer.getChildren().clear();

        for (RentalRequestModel item : requestsList) {
            String st = item.getStatus() != null ? item.getStatus().toUpperCase() : "PENDING";
            boolean matchesTab = false;

            if ("PENDING".equals(activeTab) && "PENDING".equals(st)) matchesTab = true;
            else if ("ACTIVE".equals(activeTab) && ("ACTIVE".equals(st) || "APPROVED".equals(st) || "ACCEPTED".equals(st) || "CONFIRMED".equals(st))) matchesTab = true;
            else if ("COMPLETED".equals(activeTab) && ("COMPLETED".equals(st) || "DECLINED".equals(st) || "CANCELLED".equals(st) || "REJECTED".equals(st))) matchesTab = true;

            if (!matchesTab) continue;

            if (!searchFilter.isEmpty()) {
                boolean match = (item.getFarmerName() != null && item.getFarmerName().toLowerCase().contains(searchFilter))
                        || (item.getRequestId() != null && item.getRequestId().toLowerCase().contains(searchFilter))
                        || (item.getFarmerLocation() != null && item.getFarmerLocation().toLowerCase().contains(searchFilter))
                        || (item.getMachineryName() != null && item.getMachineryName().toLowerCase().contains(searchFilter));
                if (!match) continue;
            }

            listContainer.getChildren().add(createRequestCard(item, root));
        }

        if (listContainer.getChildren().isEmpty()) {
            VBox empty = new VBox(8);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(40));
            Text t = new Text("No rental requests in this category.");
            t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14.5px; -fx-fill: #4B5563;");
            empty.getChildren().add(t);
            listContainer.getChildren().add(empty);
        }
    }

    /**
     * Simple, clean request card with:
     * - Farmer Name
     * - Farmer Location (prominently featured)
     * - Collapsible Payment Details on click
     * - Instant zero-delay actions
     */
    private static VBox createRequestCard(RentalRequestModel item, StackPane root) {
        String status = item.getStatus() != null ? item.getStatus().toUpperCase() : "PENDING";

        // Farmer Name & Location Header
        String fName = item.getFarmerName() != null ? item.getFarmerName() : "Farmer";
        Node avatarNode;
        if (item.getFarmerProfilePic() != null && !item.getFarmerProfilePic().isEmpty()) {
            try {
                ImageView fPic = new ImageView(new Image(item.getFarmerProfilePic(), true));
                fPic.setFitWidth(28);
                fPic.setFitHeight(28);
                fPic.setPreserveRatio(true);
                Circle clip = new Circle(14, 14, 14);
                fPic.setClip(clip);
                avatarNode = fPic;
            } catch (Exception ex) {
                Text avt = new Text("👨‍🌾");
                avt.setStyle("-fx-font-size: 18px;");
                avatarNode = avt;
            }
        } else {
            Text avt = new Text("👨‍🌾");
            avt.setStyle("-fx-font-size: 18px;");
            avatarNode = avt;
        }

        Text farmer = new Text(fName);
        farmer.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        String fLoc = item.getFarmerLocation() != null ? item.getFarmerLocation() : "Location Not Set";
        Label locBadge = new Label("📍 " + fLoc);
        locBadge.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 6;");

        Text reqIdText = new Text("#" + (item.getRequestId() != null ? item.getRequestId() : ""));
        reqIdText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #6B7280;");

        Label st = new Label(status);
        String stBg = "PENDING".equals(status) ? "#FFF3E0" : (("APPROVED".equals(status) || "ACTIVE".equals(status)) ? "#E8F5E9" : "#ECEFF1");
        String stColor = "PENDING".equals(status) ? "#E65100" : (("APPROVED".equals(status) || "ACTIVE".equals(status)) ? "#2E7D32" : "#37474F");
        st.setStyle("-fx-background-color: " + stBg + "; -fx-text-fill: " + stColor + "; -fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 4;");
        int total = item.getTotalAmount() > 0 ? item.getTotalAmount() : (item.getDailyRate() * Math.max(1, item.getDays()));

        Label paidBadge = new Label("🟢 PAID (₹" + total + " in Escrow)");
        paidBadge.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 6;");

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox topRow = new HBox(8, avatarNode, farmer, locBadge, paidBadge, reqIdText, topSpacer, st);
        topRow.setAlignment(Pos.CENTER_LEFT);

        // Machinery & Duration Details with Cloudinary Thumbnail
        String mName = item.getMachineryName() != null ? item.getMachineryName() : "Machinery";
        Text eq = new Text("🚜 " + mName + "  •  " + (item.getDays() > 0 ? item.getDays() : 3) + " Days");
        eq.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #2D6A4F;");

        String datesStr = (item.getStartDate() != null ? item.getStartDate() : "Today") + " to " + (item.getEndDate() != null ? item.getEndDate() : "Soon");
        String fPhone = item.getFarmerPhone() != null ? item.getFarmerPhone() : "";
        Text sched = new Text("📅 " + datesStr + (!fPhone.isEmpty() ? "   •   📞 " + fPhone : ""));
        sched.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        ImageView eqPic = new ImageView();
        eqPic.setFitWidth(65);
        eqPic.setFitHeight(52);
        eqPic.setPreserveRatio(true);
        if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
            try {
                eqPic.setImage(new Image(item.getImagePath(), true));
            } catch (Exception ex) {
                eqPic.setImage(new Image("file:farm/src/main/resources/assets/Images/tractor.png"));
            }
        } else {
            eqPic.setImage(new Image("file:farm/src/main/resources/assets/Images/tractor.png"));
        }
        VBox eqPicBox = new VBox(eqPic);
        eqPicBox.setPrefWidth(70);
        eqPicBox.setPrefHeight(56);
        eqPicBox.setAlignment(Pos.CENTER);
        eqPicBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 8; -fx-border-color: rgba(45, 106, 79, 0.2); -fx-border-radius: 8;");

        VBox eqTextDetails = new VBox(4, eq, sched);
        HBox eqContentRow = new HBox(12, eqPicBox, eqTextDetails);
        eqContentRow.setAlignment(Pos.CENTER_LEFT);

        // Collapsible Payment Details
        VBox paymentBox = new VBox(6);
        paymentBox.setPadding(new Insets(10, 12, 10, 12));
        paymentBox.setStyle("-fx-background-color: #F9FAFB; -fx-background-radius: 8; -fx-border-color: #E5E7EB; -fx-border-radius: 8;");
        paymentBox.setVisible(false);
        paymentBox.setManaged(false);

        Text payRateText = new Text("• Daily Tariff: ₹" + item.getDailyRate() + "/day");
        payRateText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #374151;");

        Text payTotalText = new Text("• Total Tariff Paid by Farmer: ₹" + String.format("%,d", total));
        payTotalText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        String pStatus = (item.getPaymentStatus() != null && !item.getPaymentStatus().isEmpty()) ? item.getPaymentStatus() : "PAID (ESCROW HELD)";
        String pMode = (item.getPaymentMode() != null && !item.getPaymentMode().isEmpty()) ? item.getPaymentMode() : "Razorpay Online";
        Text payStatusText = new Text("• Payment Status: " + pStatus + " via " + pMode);
        payStatusText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #15803D; -fx-font-weight: bold;");

        String bName = (item.getProviderBankName() != null && !item.getProviderBankName().isEmpty()) ? item.getProviderBankName() : ProviderProfileStore.bankName;
        String bAcc = (item.getProviderAccountNumber() != null && !item.getProviderAccountNumber().isEmpty()) ? item.getProviderAccountNumber() : ProviderProfileStore.accountNumber;
        String bIfsc = (item.getProviderIfsc() != null && !item.getProviderIfsc().isEmpty()) ? item.getProviderIfsc() : ProviderProfileStore.ifsc;
        String bUpi = (item.getProviderUpiId() != null && !item.getProviderUpiId().isEmpty()) ? item.getProviderUpiId() : ProviderProfileStore.upiId;

        String payoutStr = (!bAcc.isEmpty()) ? (bName + " (A/C: " + bAcc + ", IFSC: " + bIfsc + ")") : (!bUpi.isEmpty() ? ("UPI: " + bUpi) : "Primary Registered Bank (Pending setup in Settings)");
        Text payBankText = new Text("🏦 Payout Settlement Account: " + payoutStr);
        payBankText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #1B4332;");

        paymentBox.getChildren().addAll(payRateText, payTotalText, payStatusText, payBankText);

        Button togglePayBtn = new Button("💰 View Payment Details ▾");
        togglePayBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #2D6A4F; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        togglePayBtn.setOnAction(e -> {
            boolean show = !paymentBox.isVisible();
            paymentBox.setVisible(show);
            paymentBox.setManaged(show);
            togglePayBtn.setText(show ? "💰 Hide Payment Details ▴" : "💰 View Payment Details ▾");
        });

        // Actions
        HBox actions = new HBox(8);
        actions.setAlignment(Pos.CENTER_RIGHT);

        if ("PENDING".equalsIgnoreCase(status)) {
            Button approve = new Button("✔ Approve");
            approve.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 16;");
            approve.setOnAction(e -> {
                item.setStatus("ACCEPTED");
                updateTabCounts(root);
                renderRequestsList(root);

                Thread t = new Thread(() -> {
                    try {
                        new com.desgin.service.BookingService().providerAccept(item.getRequestId());
                        if (item.getMachineryId() != null) {
                            new MachineryDAO().updateMachineryStatus(item.getMachineryId(), "RESERVED");
                        }
                    } catch (Exception ignored) {}
                });
                t.setDaemon(true);
                t.start();
            });

            Button decline = new Button("✕ Decline");
            decline.setStyle("-fx-background-color: #8B3A3A; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 14;");
            decline.setOnAction(e -> {
                item.setStatus("REJECTED");
                updateTabCounts(root);
                renderRequestsList(root);

                Thread t = new Thread(() -> {
                    try {
                        new com.desgin.service.BookingService().providerReject(item.getRequestId(), "Provider currently unavailable for selected dates.");
                    } catch (Exception ignored) {}
                });
                t.setDaemon(true);
                t.start();
            });

            actions.getChildren().addAll(decline, approve);

        } else if ("APPROVED".equalsIgnoreCase(status) || "ACCEPTED".equalsIgnoreCase(status) || "CONFIRMED".equalsIgnoreCase(status) || "ACTIVE".equalsIgnoreCase(status)) {
            Button complete = new Button("✔ Mark Job Completed");
            complete.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 16;");
            complete.setOnAction(e -> {
                item.setStatus("COMPLETED");
                updateTabCounts(root);
                renderRequestsList(root);

                Thread t = new Thread(() -> {
                    try {
                        new com.desgin.service.BookingService().completeBooking(item.getRequestId());
                    } catch (Exception ignored) {}
                });
                t.setDaemon(true);
                t.start();
            });
            actions.getChildren().add(complete);
        } else {
            Label done = new Label("Completed");
            done.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #6B7280;");
            actions.getChildren().add(done);
        }

        Region botSpacer = new Region();
        HBox.setHgrow(botSpacer, Priority.ALWAYS);
        HBox bottomRow = new HBox(togglePayBtn, botSpacer, actions);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(10, topRow, eqContentRow, paymentBox, bottomRow);
        card.setPadding(new Insets(14, 18, 14, 18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 10;");

        return card;
    }
}
