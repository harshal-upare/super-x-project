package com.desgin.view.farmer.harshal;

import java.util.ArrayList;
import java.util.List;

import com.desgin.view.farmer.Swapnil.BookingDataStore;
import com.desgin.view.farmer.Swapnil.BookingDataStore.BookingItem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MyBookings {

    private VBox equipmentBookingList;
    private VBox operatorBookingList;
    private Button allBtn, upcomingBtn, activeBtn, completedBtn, cancelledBtn;
    private StackPane innerRoot;
    private Label totalStatVal, upcomingStatVal, activeStatVal, completedStatVal;
    private Label equipCountBadge, opCountBadge;
    private String currentFilter = "ALL";

    public VBox getBooking(StackPane root) {
        innerRoot = root;
        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Search by equipment, booking ID, or status...");
        searchField.setPrefWidth(420); searchField.setPrefHeight(44);
        searchField.setStyle("-fx-background-color: rgba(255,255,255,0.95); -fx-background-radius: 12px; -fx-border-color: rgba(45,106,79,0.3); -fx-border-width: 1.2px; -fx-border-radius: 12px; -fx-padding: 0 14px; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-text-fill: #1F2937;");
        searchField.textProperty().addListener((obs, o, n) -> applySearch(n));
        HBox header = new HBox(12, searchField); header.setAlignment(Pos.CENTER_LEFT);

        totalStatVal = new Label(String.valueOf(BookingDataStore.getTotalCount()));
        upcomingStatVal = new Label(String.valueOf(BookingDataStore.getPendingCount()));
        activeStatVal = new Label(String.valueOf(BookingDataStore.getActiveCount()));
        completedStatVal = new Label(String.valueOf(BookingDataStore.getCompletedCount()));
        VBox c1 = mkKpi("Total Bookings", totalStatVal, "All lifetime requests", "#1B4332");
        VBox c2 = mkKpi("Upcoming", upcomingStatVal, "Awaiting start date", "#2D6A4F");
        VBox c3 = mkKpi("Active On-Field", activeStatVal, "Currently in operation", "#15803D");
        VBox c4 = mkKpi("Completed", completedStatVal, "Ready to review", "#B45309");
        syncFromDB();
        HBox kpiRow = new HBox(14, c1, c2, c3, c4); kpiRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(c1, Priority.ALWAYS); HBox.setHgrow(c2, Priority.ALWAYS);
        HBox.setHgrow(c3, Priority.ALWAYS); HBox.setHgrow(c4, Priority.ALWAYS);

        allBtn = mkTab("All Bookings", true);
        upcomingBtn = mkTab("Upcoming", false); activeBtn = mkTab("Active", false);
        completedBtn = mkTab("Completed", false); cancelledBtn = mkTab("Cancelled", false);
        allBtn.setOnAction(e -> { currentFilter="ALL"; refresh(null); activateTab(allBtn); });
        upcomingBtn.setOnAction(e -> { currentFilter="UPCOMING"; refresh(null); activateTab(upcomingBtn); });
        activeBtn.setOnAction(e -> { currentFilter="ACTIVE"; refresh(null); activateTab(activeBtn); });
        completedBtn.setOnAction(e -> { currentFilter="COMPLETED"; refresh(null); activateTab(completedBtn); });
        cancelledBtn.setOnAction(e -> { currentFilter="CANCELLED"; refresh(null); activateTab(cancelledBtn); });
        HBox tabs = new HBox(10, allBtn, upcomingBtn, activeBtn, completedBtn, cancelledBtn);
        tabs.setAlignment(Pos.CENTER_LEFT);

        // ── EQUIPMENT SECTION ──
        equipCountBadge = new Label("0");
        equipCountBadge.setStyle("-fx-background-color:#2D6A4F;-fx-text-fill:white;-fx-font-family:'Poppins';-fx-font-size:11px;-fx-font-weight:bold;-fx-padding:2px 9px;-fx-background-radius:10px;");
        Text eIco = new Text("🚜"); eIco.setStyle("-fx-font-size:18px;");
        Label eTitle = new Label("Equipment Bookings");
        eTitle.setStyle("-fx-font-family:'Poppins';-fx-font-size:16px;-fx-font-weight:bold;-fx-text-fill:#1B4332;");
        Label eSub = new Label("Track your machinery & equipment rental requests");
        eSub.setStyle("-fx-font-family:'Poppins';-fx-font-size:12px;-fx-text-fill:#4B5563;");
        VBox eTitleBox = new VBox(1, eTitle, eSub);
        Region eSp = new Region(); HBox.setHgrow(eTitleBox, Priority.ALWAYS); HBox.setHgrow(eSp, Priority.ALWAYS);
        HBox eHdr = new HBox(10, eIco, eTitleBox, eSp, equipCountBadge);
        eHdr.setAlignment(Pos.CENTER_LEFT); eHdr.setPadding(new Insets(12,16,12,16));
        eHdr.setStyle("-fx-background-color:linear-gradient(to right,#E8F5E9,#F0FAF0);-fx-background-radius:12px 12px 0 0;-fx-border-color:rgba(45,106,79,0.2);-fx-border-width:0 0 1px 0;");
        equipmentBookingList = new VBox(10); equipmentBookingList.setPadding(new Insets(14));
        VBox equipSection = new VBox(0, eHdr, equipmentBookingList);
        equipSection.setStyle("-fx-background-color:rgba(255,255,255,0.95);-fx-background-radius:14px;-fx-border-color:rgba(45,106,79,0.25);-fx-border-width:1.2px;-fx-border-radius:14px;-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.05),8,0,0,2);");

        // ── OPERATOR SECTION ──
        opCountBadge = new Label("0");
        opCountBadge.setStyle("-fx-background-color:#B45309;-fx-text-fill:white;-fx-font-family:'Poppins';-fx-font-size:11px;-fx-font-weight:bold;-fx-padding:2px 9px;-fx-background-radius:10px;");
        Text oIco = new Text("👷"); oIco.setStyle("-fx-font-size:18px;");
        Label oTitle = new Label("Operator Bookings");
        oTitle.setStyle("-fx-font-family:'Poppins';-fx-font-size:16px;-fx-font-weight:bold;-fx-text-fill:#92400E;");
        Label oSub = new Label("Track your field operator hire requests & dispatches");
        oSub.setStyle("-fx-font-family:'Poppins';-fx-font-size:12px;-fx-text-fill:#4B5563;");
        VBox oTitleBox = new VBox(1, oTitle, oSub);
        Region oSp = new Region(); HBox.setHgrow(oTitleBox, Priority.ALWAYS); HBox.setHgrow(oSp, Priority.ALWAYS);
        HBox oHdr = new HBox(10, oIco, oTitleBox, oSp, opCountBadge);
        oHdr.setAlignment(Pos.CENTER_LEFT); oHdr.setPadding(new Insets(12,16,12,16));
        oHdr.setStyle("-fx-background-color:linear-gradient(to right,#FFF8E1,#FFFDE7);-fx-background-radius:12px 12px 0 0;-fx-border-color:rgba(180,83,9,0.2);-fx-border-width:0 0 1px 0;");
        operatorBookingList = new VBox(10); operatorBookingList.setPadding(new Insets(14));
        VBox opSection = new VBox(0, oHdr, operatorBookingList);
        opSection.setStyle("-fx-background-color:rgba(255,255,255,0.95);-fx-background-radius:14px;-fx-border-color:rgba(180,83,9,0.2);-fx-border-width:1.2px;-fx-border-radius:14px;-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.05),8,0,0,2);");

        VBox both = new VBox(20, equipSection, opSection);
        ScrollPane sp = new ScrollPane(both); sp.setFitToWidth(true); sp.setFitToHeight(false);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setStyle("-fx-background-color:transparent;-fx-background:transparent;");
        VBox.setVgrow(sp, Priority.ALWAYS);

        VBox main = new VBox(18, header, kpiRow, tabs, sp);
        main.setPadding(new Insets(20,30,35,30)); main.setStyle("-fx-background-color:transparent;");
        refresh(null);
        return main;
    }

    private void refresh(String kw) {
        List<BookingItem> all = BookingDataStore.getAllBookings();
        List<BookingItem> eq = new ArrayList<>(), op = new ArrayList<>();
        for (BookingItem b : all) {
            boolean sm = matchFilter(b); 
            boolean km = kw==null||kw.trim().isEmpty()||b.equipmentName.toLowerCase().contains(kw.toLowerCase())||b.bookingId.toLowerCase().contains(kw.toLowerCase())||b.category.toLowerCase().contains(kw.toLowerCase())||b.status.toLowerCase().contains(kw.toLowerCase());
            if (sm && km) { if (b.operatorRequired) op.add(b); else eq.add(b); }
        }
        if (equipCountBadge!=null) equipCountBadge.setText(String.valueOf(eq.size()));
        if (opCountBadge!=null) opCountBadge.setText(String.valueOf(op.size()));
        renderEquip(eq); renderOp(op);
    }

    private boolean matchFilter(BookingItem b) {
        switch(currentFilter) {
            case "UPCOMING": return "PENDING".equalsIgnoreCase(b.status)||"ACCEPTED".equalsIgnoreCase(b.status);
            case "ACTIVE": return "ACTIVE".equalsIgnoreCase(b.status)||"CONFIRMED".equalsIgnoreCase(b.status);
            case "COMPLETED": return "COMPLETED".equalsIgnoreCase(b.status);
            case "CANCELLED": return "CANCELLED".equalsIgnoreCase(b.status);
            default: return true;
        }
    }

    private void applySearch(String kw) { refresh(kw); }

    private void renderEquip(List<BookingItem> list) {
        if (equipmentBookingList==null) return;
        equipmentBookingList.getChildren().clear();
        if (list.isEmpty()) equipmentBookingList.getChildren().add(emptyState("🚜","No Equipment Bookings","Browse the machinery catalog and rent farm equipment to see bookings here."));
        else for (BookingItem b : list) equipmentBookingList.getChildren().add(equipCard(b, innerRoot));
    }

    private void renderOp(List<BookingItem> list) {
        if (operatorBookingList==null) return;
        operatorBookingList.getChildren().clear();
        if (list.isEmpty()) operatorBookingList.getChildren().add(emptyState("👷","No Operator Bookings","Search for field operators and send hire requests to see them here."));
        else for (BookingItem b : list) operatorBookingList.getChildren().add(opCard(b, innerRoot));
    }

    private VBox emptyState(String ico, String title, String sub) {
        VBox box = new VBox(8); box.setAlignment(Pos.CENTER); box.setPadding(new Insets(28,20,28,20));
        Label i = new Label(ico); i.setStyle("-fx-font-size:30px;");
        Label t = new Label(title); t.setStyle("-fx-font-family:'Poppins';-fx-font-size:14px;-fx-font-weight:bold;-fx-text-fill:#4B5563;");
        Label s = new Label(sub); s.setStyle("-fx-font-family:'Poppins';-fx-font-size:12px;-fx-text-fill:#9CA3AF;"); s.setWrapText(true);
        box.getChildren().addAll(i,t,s); return box;
    }

    private HBox equipCard(BookingItem item, StackPane root) {
        String status = item.status;
        String baseStyle = "-fx-background-color:#FAFCFA;-fx-background-radius:10px;-fx-border-color:rgba(45,106,79,0.18);-fx-border-width:1px;-fx-border-radius:10px;-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.03),4,0,0,1);";
        HBox card = new HBox(18); card.setPadding(new Insets(14,18,14,18)); card.setAlignment(Pos.CENTER_LEFT); card.setStyle(baseStyle);

        String ico = item.category.contains("Harvester")?"🌾":(item.category.contains("Drone")?"🚁":(item.category.contains("Rotavator")?"⚙️":(item.category.contains("Cultivator")?"🌱":"🚜")));
        VBox imgBox = new VBox(); imgBox.setPrefWidth(78); imgBox.setPrefHeight(74); imgBox.setAlignment(Pos.CENTER);
        imgBox.setStyle("-fx-background-color:#E8F5E9;-fx-background-radius:10px;-fx-border-color:rgba(45,106,79,0.2);-fx-border-radius:10px;");
        if (item.imagePath!=null&&!item.imagePath.isEmpty()) {
            try { javafx.scene.image.ImageView iv=new javafx.scene.image.ImageView(); iv.setFitWidth(68); iv.setFitHeight(64); iv.setPreserveRatio(true); iv.setImage(new javafx.scene.image.Image(item.imagePath,true)); imgBox.getChildren().add(iv); }
            catch(Exception ex) { Label l=new Label(ico); l.setStyle("-fx-font-size:28px;"); imgBox.getChildren().add(l); }
        } else { Label l=new Label(ico); l.setStyle("-fx-font-size:28px;"); imgBox.getChildren().add(l); }

        VBox info = new VBox(4);
        Label nm = new Label(item.equipmentName); nm.setStyle("-fx-font-family:'Poppins';-fx-font-size:15px;-fx-font-weight:bold;-fx-text-fill:#1B4332;");
        Label tp = new Label("🏷  "+item.category+"   •   ID: "+item.bookingId); tp.setStyle("-fx-font-family:'Poppins';-fx-font-size:11.5px;-fx-text-fill:#6B7280;");
        Label dt = new Label("📅  "+item.startDate+"  →  "+item.endDate); dt.setStyle("-fx-font-family:'Poppins';-fx-font-size:12px;-fx-font-weight:bold;-fx-text-fill:#2D6A4F;");
        info.getChildren().addAll(nm,tp,dt);
        if (item.providerName!=null&&!item.providerName.isEmpty()) { Label pv=new Label("🏢  Provider: "+item.providerName); pv.setStyle("-fx-font-family:'Poppins';-fx-font-size:11px;-fx-text-fill:#6B7280;"); info.getChildren().add(pv); }

        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        Label rl = new Label(item.dailyRate+" / day"); rl.setStyle("-fx-font-family:'Poppins';-fx-font-size:11px;-fx-text-fill:#5C6B5F;");
        Label tl = new Label("Total: "+item.totalAmount); tl.setStyle("-fx-font-family:'Poppins';-fx-font-size:14px;-fx-font-weight:bold;-fx-text-fill:#1B4332;");
        VBox priceBox = new VBox(2,rl,tl); priceBox.setAlignment(Pos.CENTER_RIGHT);

        Label stLbl = statusBadge(status,false);
        Button viewBtn = new Button("View Details"); viewBtn.setPrefHeight(32);
        viewBtn.setStyle("-fx-background-color:linear-gradient(to right,#2D6A4F,#40916C);-fx-text-fill:white;-fx-font-family:'Poppins';-fx-font-size:11.5px;-fx-font-weight:bold;-fx-background-radius:8px;-fx-cursor:hand;");
        viewBtn.setOnAction(e -> openDetails(item,root));
        HBox btns = new HBox(8,stLbl,viewBtn); btns.setAlignment(Pos.CENTER_RIGHT);

        if (!"PAID".equalsIgnoreCase(item.paymentStatus)&&!"CANCELLED".equalsIgnoreCase(status)&&!"REJECTED".equalsIgnoreCase(status)) {
            Button payBtn = new Button("💳 Pay"); payBtn.setPrefHeight(32);
            payBtn.setStyle("-fx-background-color:#2563EB;-fx-text-fill:white;-fx-font-family:'Poppins';-fx-font-size:11.5px;-fx-font-weight:bold;-fx-background-radius:8px;-fx-cursor:hand;-fx-padding:0 10;");
            payBtn.setOnAction(e -> payModal(item)); btns.getChildren().add(payBtn);
        }
        if (!"CANCELLED".equalsIgnoreCase(status)&&!"COMPLETED".equalsIgnoreCase(status)&&!"REJECTED".equalsIgnoreCase(status)) {
            Button canBtn = new Button("✖ Cancel"); canBtn.setPrefHeight(32);
            canBtn.setStyle("-fx-background-color:#FEE2E2;-fx-text-fill:#B91C1C;-fx-font-family:'Poppins';-fx-font-size:11px;-fx-font-weight:bold;-fx-background-radius:8px;-fx-cursor:hand;-fx-padding:0 10;");
            canBtn.setOnAction(e -> doCancel(item)); btns.getChildren().add(canBtn);
        }
        if ("COMPLETED".equalsIgnoreCase(status)) {
            Button rv = new Button("⭐ Rate"); rv.setPrefHeight(32);
            rv.setStyle("-fx-background-color:#E8F5E9;-fx-text-fill:#2D6A4F;-fx-font-family:'Poppins';-fx-font-weight:bold;-fx-font-size:11.5px;-fx-background-radius:8px;-fx-cursor:hand;-fx-border-color:rgba(45,106,79,0.3);-fx-border-radius:8px;");
            rv.setOnAction(e -> { com.desgin.view.farmer.LeftSideBar.setActiveButton(com.desgin.view.farmer.LeftSideBar.reviewBtn1,com.desgin.view.farmer.LeftSideBar.navigationButtons); com.desgin.view.farmer.Swapnil.FarmerDashboard.borderPane.setCenter(com.desgin.view.farmer.review.ReviewRating.getReviewRatingPage(root)); });
            btns.getChildren().add(rv);
        }
        VBox right = new VBox(6,priceBox,btns); right.setAlignment(Pos.CENTER_RIGHT);
        card.getChildren().addAll(imgBox,info,sp,right);
        card.setOnMouseEntered(e -> { card.setStyle("-fx-background-color:#FAFCFA;-fx-background-radius:10px;-fx-border-color:#2D6A4F;-fx-border-width:1.5px;-fx-border-radius:10px;-fx-effect:dropshadow(gaussian,rgba(45,106,79,0.15),8,0,0,2);"); card.setTranslateY(-1); });
        card.setOnMouseExited(e -> { card.setStyle(baseStyle); card.setTranslateY(0); });
        return card;
    }

    private void renderBookings(List<BookingItem> list, String emptyMessage) {
        bookingList.getChildren().clear();
        if (list.isEmpty()) {
            VBox emptyBox = new VBox(10);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(50));
            emptyBox.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                    "-fx-background-radius: 14px;" +
                    "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                    "-fx-border-width: 1.2px;" +
                    "-fx-border-radius: 14px;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
            );
            Label icon = new Label("📅");
            icon.setStyle("-fx-font-size: 36px;");
            Label emptyLabel = new Label(emptyMessage);
            emptyLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");
            Label sub = new Label("Browse the machinery catalog and rent farm equipment to see your bookings here.");
            sub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-text-fill: #4B5563;");

            Button browseBtn = new Button("⚒  Browse Equipment Catalog ➔");
            browseBtn.setStyle("-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 8 16;");
            browseBtn.setOnAction(e -> com.desgin.view.farmer.LeftSideBar.navigateToBrowseEquip());

            emptyBox.getChildren().addAll(icon, emptyLabel, sub, browseBtn);
            bookingList.getChildren().add(emptyBox);
        } else {
            for (BookingItem item : list) {
                bookingList.getChildren().add(
                        createBookingCard(item, innerRoot)
                );
            }
        }
        VBox right = new VBox(6,priceBox,btns); right.setAlignment(Pos.CENTER_RIGHT);
        card.getChildren().addAll(avBox,info,sp,right);
        card.setOnMouseEntered(e -> { card.setStyle("-fx-background-color:#FFFEF8;-fx-background-radius:10px;-fx-border-color:#B45309;-fx-border-width:1.5px;-fx-border-radius:10px;-fx-effect:dropshadow(gaussian,rgba(180,83,9,0.15),8,0,0,2);"); card.setTranslateY(-1); });
        card.setOnMouseExited(e -> { card.setStyle(baseStyle); card.setTranslateY(0); });
        return card;
    }

    private Label statusBadge(String status, boolean isOp) {
        Label lbl = new Label(status);
        String bg,fg;
        if ("ACTIVE".equalsIgnoreCase(status)||"CONFIRMED".equalsIgnoreCase(status)){bg="#DCFCE7";fg="#15803D";}
        else if ("PENDING".equalsIgnoreCase(status)||"ACCEPTED".equalsIgnoreCase(status)){bg=isOp?"#FFF8E1":"#FFF3E0";fg=isOp?"#B45309":"#E65100";}
        else if ("CANCELLED".equalsIgnoreCase(status)||"REJECTED".equalsIgnoreCase(status)){bg="#FEE2E2";fg="#B91C1C";}
        else if ("COMPLETED".equalsIgnoreCase(status)){bg="#E0E7FF";fg="#4338CA";}
        else{bg="#F3F4F6";fg="#4B5563";}
        lbl.setStyle("-fx-background-color:"+bg+";-fx-text-fill:"+fg+";-fx-font-family:'Poppins';-fx-font-size:10.5px;-fx-font-weight:bold;-fx-padding:4px 10px;-fx-background-radius:10px;");
        return lbl;
    }

    private void openDetails(BookingItem item, StackPane root) {
        StackPane overlay = new StackPane(); overlay.setStyle("-fx-background-color:rgba(0,0,0,0.45);");
        VBox db = new BookingDetails().getBookingDetails(item.bookingId,item.equipmentName,item.category,item.startDate,item.endDate,item.dailyRate,item.totalAmount,item.status,()->root.getChildren().remove(overlay),()->{root.getChildren().remove(overlay);refresh(null);});
        db.setMaxWidth(600); db.setMaxHeight(650);
        overlay.getChildren().add(db); StackPane.setAlignment(db,Pos.CENTER); root.getChildren().add(overlay);
    }

    private VBox mkKpi(String title, Label val, String sub, String color) {
        Label t = new Label(title); t.setStyle("-fx-font-family:'Poppins';-fx-font-size:12.5px;-fx-font-weight:bold;-fx-text-fill:#1B4332;");
        val.setStyle("-fx-font-family:'Poppins';-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:"+color+";");
        Text s = new Text(sub); s.setStyle("-fx-font-family:'Poppins';-fx-font-size:11px;-fx-fill:#5C6B5F;");
        VBox c = new VBox(3,t,val,s); c.setPadding(new Insets(14,18,14,18));
        c.setStyle("-fx-background-color:rgba(255,255,255,0.95);-fx-background-radius:14px;-fx-border-color:rgba(45,106,79,0.25);-fx-border-width:1.2px;-fx-border-radius:14px;-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.04),8,0,0,2);");
        return c;
    }

    private void updateKpi() {
        if (totalStatVal!=null) totalStatVal.setText(String.valueOf(BookingDataStore.getTotalCount()));
        if (upcomingStatVal!=null) upcomingStatVal.setText(String.valueOf(BookingDataStore.getPendingCount()));
        if (activeStatVal!=null) activeStatVal.setText(String.valueOf(BookingDataStore.getActiveCount()));
        if (completedStatVal!=null) completedStatVal.setText(String.valueOf(BookingDataStore.getCompletedCount()));
    }

    private void syncFromDB() {
        new Thread(()->{
            try {
                String email = com.desgin.view.farmer.Swapnil.FarmerProfileStore.email;
                if (email==null||email.trim().isEmpty()) return;
                List<com.desgin.model.RentalRequestModel> reqs = new com.desgin.dao.RentalRequestDAO().getRequestsByFarmer(email);
                BookingDataStore.syncFromFirestore(reqs);
                javafx.application.Platform.runLater(()->{updateKpi();refresh(null);});
            } catch(Exception ignored){}
        }).start();
    }

    private Button mkTab(String text, boolean active) {
        Button b = new Button(text); b.setPrefHeight(38); styleTab(b,active); return b;
    }

    private void styleTab(Button btn, boolean active) {
        if (active) btn.setStyle("-fx-background-color:linear-gradient(to right,#2D6A4F,#40916C);-fx-text-fill:white;-fx-font-family:'Poppins';-fx-font-size:13px;-fx-font-weight:bold;-fx-background-radius:20px;-fx-padding:8px 20px;-fx-cursor:hand;-fx-effect:dropshadow(gaussian,rgba(45,106,79,0.25),8,0,0,2);");
        else btn.setStyle("-fx-background-color:rgba(255,255,255,0.95);-fx-text-fill:#4B5563;-fx-font-family:'Poppins';-fx-font-size:13px;-fx-font-weight:500;-fx-background-radius:20px;-fx-border-color:rgba(45,106,79,0.25);-fx-border-width:1.2px;-fx-border-radius:20px;-fx-padding:8px 20px;-fx-cursor:hand;");
    }

    private void activateTab(Button sel) {
        for (Button b : new Button[]{allBtn,upcomingBtn,activeBtn,completedBtn,cancelledBtn})
            if (b!=null) styleTab(b,b==sel);
    }

    private void payModal(BookingItem item) {
        String me = com.desgin.view.farmer.Swapnil.FarmerProfileStore.email;
        if (me!=null&&item.farmerEmail!=null&&!item.farmerEmail.trim().isEmpty()&&!me.trim().equalsIgnoreCase(item.farmerEmail.trim())) { alert("Unauthorized","This booking belongs to another account."); return; }
        if ("PAID".equalsIgnoreCase(item.paymentStatus)) { alert("Already Paid","Already paid."); return; }
        if ("CANCELLED".equalsIgnoreCase(item.status)||"REJECTED".equalsIgnoreCase(item.status)) { alert("Booking Cancelled","Cannot pay a cancelled booking."); return; }
        Stage st = new Stage(); st.initModality(Modality.APPLICATION_MODAL); st.setTitle("Pay Booking #"+item.bookingId);
        VBox box = new VBox(14); box.setPadding(new Insets(24)); box.setStyle("-fx-background-color:#F8FAF8;-fx-border-color:#2D6A4F;-fx-border-width:1.5;-fx-background-radius:12;-fx-border-radius:12;"); box.setPrefWidth(460);
        Text tl = new Text("💳 Razorpay Payment Checkout"); tl.setStyle("-fx-font-family:'Poppins';-fx-font-size:18px;-fx-font-weight:bold;-fx-fill:#1B4332;");
        Text inf = new Text("• Equipment: "+item.equipmentName+"\n• Scheduled: "+item.startDate+" to "+item.endDate+"\n• Total Due: "+item.totalAmount); inf.setStyle("-fx-font-family:'Poppins';-fx-font-size:13px;-fx-fill:#374151;");
        Label sl = new Label("Click below to open Razorpay."); sl.setStyle("-fx-font-family:'Poppins';-fx-font-size:12px;-fx-text-fill:#6B7280;"); sl.setWrapText(true);
        int amt=1000; try{amt=Integer.parseInt(item.totalAmount.replaceAll("[^0-9]",""));}catch(Exception ig){}
        final int fa=amt;
        Button pb = new Button("🚀 Launch Razorpay ("+item.totalAmount+")"); pb.setMaxWidth(Double.MAX_VALUE); pb.setPrefHeight(42); pb.setStyle("-fx-background-color:#2563EB;-fx-text-fill:white;-fx-font-family:'Poppins';-fx-font-size:13.5px;-fx-font-weight:bold;-fx-background-radius:8;-fx-cursor:hand;");
        Button cb = new Button("✔ Confirm Payment"); cb.setMaxWidth(Double.MAX_VALUE); cb.setPrefHeight(42); cb.setStyle("-fx-background-color:#15803D;-fx-text-fill:white;-fx-font-family:'Poppins';-fx-font-size:13.5px;-fx-font-weight:bold;-fx-background-radius:8;-fx-cursor:hand;");
        pb.setOnAction(e->{pb.setDisable(true);sl.setText("Connecting...");new Thread(()->{try{String url=com.desgin.service.RazorpayService.createPaymentLink(fa,item.bookingId,com.desgin.view.farmer.Swapnil.FarmerProfileStore.name,com.desgin.view.farmer.Swapnil.FarmerProfileStore.email,com.desgin.view.farmer.Swapnil.FarmerProfileStore.phone);com.desgin.service.RazorpayService.openPaymentInBrowser(url);javafx.application.Platform.runLater(()->{sl.setText("Link launched! Click Confirm when done.");sl.setStyle("-fx-text-fill:#2563EB;");pb.setDisable(false);});}catch(Exception ex){javafx.application.Platform.runLater(()->{sl.setText("Error: "+ex.getMessage());sl.setStyle("-fx-text-fill:#B91C1C;");pb.setDisable(false);});}}).start();});
        cb.setOnAction(e->{cb.setDisable(true);sl.setText("Verifying...");sl.setStyle("-fx-text-fill:#2563EB;");new Thread(()->{try{new com.desgin.service.BookingService().confirmPayment(item.bookingId,"PAY_"+System.currentTimeMillis(),"ORD_"+System.currentTimeMillis(),"Razorpay");javafx.application.Platform.runLater(()->{st.close();syncFromDB();});}catch(Exception ex){javafx.application.Platform.runLater(()->{sl.setText("Failed: "+ex.getMessage());sl.setStyle("-fx-text-fill:#B91C1C;");cb.setDisable(false);});}}).start();});
        Button cl = new Button("Close"); cl.setStyle("-fx-background-color:#E5E7EB;-fx-text-fill:#374151;-fx-font-family:'Poppins';-fx-background-radius:6;-fx-cursor:hand;"); cl.setOnAction(e->st.close());
        HBox bot = new HBox(8,cl); bot.setAlignment(Pos.CENTER_RIGHT);
        box.getChildren().addAll(tl,inf,sl,pb,cb,bot); st.setScene(new Scene(box)); st.show();
    private HBox createBookingCard(BookingItem item, StackPane root) {
        String equipmentName = item.equipmentName;
        String equipmentType = item.category;
        String bookingId = item.bookingId;
        String startDate = item.startDate;
        String endDate = item.endDate;
        String pricePerDay = item.dailyRate;
        String totalPrice = item.totalAmount;
        String status = item.status;
        String imagePath = item.imagePath;

        HBox card = new HBox(20);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
        );

        // Machinery Icon Box
        String iconChar = equipmentType.contains("Harvester") ? "🌾" :
                (equipmentType.contains("Drone") ? "🚁" :
                (equipmentType.contains("Rotavator") ? "⚙️" :
                (equipmentType.contains("Cultivator") ? "🌱" : "🚜")));

        VBox imageBox = new VBox();
        imageBox.setPrefWidth(95);
        imageBox.setPrefHeight(90);
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setStyle(
                "-fx-background-color: #E8F5E9;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.2);" +
                "-fx-border-radius: 12px;"
        );

        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                javafx.scene.image.ImageView iv = new javafx.scene.image.ImageView();
                iv.setFitWidth(85);
                iv.setFitHeight(75);
                iv.setPreserveRatio(true);
                iv.setImage(new javafx.scene.image.Image(imagePath, true));
                imageBox.getChildren().add(iv);
            } catch (Exception ex) {
                Label iconLabel = new Label(iconChar);
                iconLabel.setStyle("-fx-font-size: 38px;");
                imageBox.getChildren().add(iconLabel);
            }
        } else {
            Label iconLabel = new Label(iconChar);
            iconLabel.setStyle("-fx-font-size: 38px;");
            imageBox.getChildren().add(iconLabel);
        }

        // Information Column
        VBox infoBox = new VBox(5);
        Label nameLabel = new Label(equipmentName);
        nameLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        Label typeLabel = new Label("🏷  " + equipmentType + "  •  ID: " + bookingId);
        typeLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-fill: #4B5563;");

        Label dateLabel = new Label("📅  " + startDate + "  →  " + endDate);
        dateLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #2D6A4F;");

        infoBox.getChildren().addAll(nameLabel, typeLabel, dateLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Price Column
        VBox priceBox = new VBox(2);
        priceBox.setAlignment(Pos.CENTER_RIGHT);

        Label rateLabel = new Label(pricePerDay + " / day");
        rateLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-fill: #5C6B5F;");

        Label totalLabel = new Label("Total: " + totalPrice);
        totalLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        priceBox.getChildren().addAll(rateLabel, totalLabel);

        // Status Badge
        Label statusLabel = new Label(status);
        String stBg = "#E8F5E9";
        String stColor = "#15803D";
        if ("PENDING".equalsIgnoreCase(status)) {
            stBg = "#FFF3E0";
            stColor = "#E65100";
        } else if ("CANCELLED".equalsIgnoreCase(status)) {
            stBg = "#FEE2E2";
            stColor = "#B91C1C";
        }
        statusLabel.setStyle(
                "-fx-background-color: " + stBg + ";" +
                "-fx-text-fill: " + stColor + ";" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 4px 12px;" +
                "-fx-background-radius: 12px;"
        );

        // Action Buttons
        Button viewButton = new Button("View Details");
        viewButton.setPrefHeight(34);
        viewButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.2), 6, 0, 0, 2);"
        );
        viewButton.setOnMouseEntered(e -> viewButton.setStyle("-fx-background-color: linear-gradient(to right, #1B4332, #2D6A4F); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand;"));
        viewButton.setOnMouseExited(e -> viewButton.setStyle("-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand;"));

        viewButton.setOnAction(e -> {
            StackPane overlay = new StackPane();
            overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");

            BookingDetails details = new BookingDetails();
            VBox detailsBox = details.getBookingDetails(
                    bookingId,
                    equipmentName,
                    equipmentType,
                    startDate,
                    endDate,
                    pricePerDay,
                    totalPrice,
                    status,
                    imagePath,
                    () -> root.getChildren().remove(overlay),
                    () -> {
                        root.getChildren().remove(overlay);
                        showAllBookings();
                    }
            );

            detailsBox.setMaxWidth(600);
            detailsBox.setMaxHeight(650);
            overlay.getChildren().add(detailsBox);
            StackPane.setAlignment(detailsBox, Pos.CENTER);
            root.getChildren().add(overlay);
        });

        HBox btnRow = new HBox(8, statusLabel, viewButton);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        if (!"PAID".equalsIgnoreCase(item.paymentStatus) && !"CANCELLED".equalsIgnoreCase(status) && !"REJECTED".equalsIgnoreCase(status)) {
            Button payBtn = new Button("💳 Pay with Razorpay");
            payBtn.setPrefHeight(34);
            payBtn.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 0 12;");
            payBtn.setOnAction(e -> openPayModal(item));
            btnRow.getChildren().add(payBtn);
        }

        if (!"CANCELLED".equalsIgnoreCase(status) && !"COMPLETED".equalsIgnoreCase(status) && !"REJECTED".equalsIgnoreCase(status)) {
            Button cancelBtn = new Button("✖ Cancel Booking");
            cancelBtn.setPrefHeight(34);
            cancelBtn.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #B91C1C; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 0 12;");
            cancelBtn.setOnAction(e -> promptCancelBooking(item));
            btnRow.getChildren().add(cancelBtn);
        }

        if ("COMPLETED".equalsIgnoreCase(status)) {
            Button rateReviewBtn = new Button("⭐ Rate & Review");
            rateReviewBtn.setPrefHeight(34);
            rateReviewBtn.setStyle(
                    "-fx-background-color: #E8F5E9;" +
                    "-fx-text-fill: #2D6A4F;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 12px;" +
                    "-fx-background-radius: 8px;" +
                    "-fx-cursor: hand;" +
                    "-fx-border-color: rgba(45, 106, 79, 0.3);" +
                    "-fx-border-radius: 8px;"
            );
            rateReviewBtn.setOnAction(e -> com.desgin.view.farmer.LeftSideBar.navigateToReviews());
            btnRow.getChildren().add(rateReviewBtn);
        }

        VBox rightBox = new VBox(8, priceBox, btnRow);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        card.getChildren().addAll(imageBox, infoBox, spacer, rightBox);

        card.setOnMouseEntered(e -> {
            card.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                    "-fx-background-radius: 14px;" +
                    "-fx-border-color: #2D6A4F;" +
                    "-fx-border-width: 1.5px;" +
                    "-fx-border-radius: 14px;" +
                    "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.18), 12, 0, 0, 3);"
            );
            card.setTranslateY(-1.5);
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                    "-fx-background-radius: 14px;" +
                    "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                    "-fx-border-width: 1.2px;" +
                    "-fx-border-radius: 14px;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
            );
            card.setTranslateY(0);
        });

        return card;
    }

    private void openPayModal(BookingItem item) {
        // Pre-payment validation
        String loggedInEmail = com.desgin.view.farmer.Swapnil.FarmerProfileStore.email;
        if (loggedInEmail != null && item.farmerEmail != null && !item.farmerEmail.trim().isEmpty() 
                && !loggedInEmail.trim().equalsIgnoreCase(item.farmerEmail.trim())) {
            showErrorAlert("Unauthorized", "This booking belongs to another account (" + item.farmerEmail + ").");
            return;
        }
        if ("PAID".equalsIgnoreCase(item.paymentStatus)) {
            showErrorAlert("Already Paid", "This booking has already been paid. No further action needed.");
            return;
        }
        if ("CANCELLED".equalsIgnoreCase(item.status) || "REJECTED".equalsIgnoreCase(item.status)) {
            showErrorAlert("Booking Cancelled", "This booking has been cancelled. Payment is not allowed.");
            return;
        }

        StackPane rootPane = com.desgin.view.farmer.Swapnil.FarmerDashboard.root;
        if (rootPane == null) return;

        StackPane overlay = new StackPane();
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        VBox rootBox = new VBox(14);
        rootBox.setPadding(new Insets(24));
        rootBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #2D6A4F; -fx-border-width: 1.5; -fx-background-radius: 14; -fx-border-radius: 14;");
        rootBox.setPrefWidth(460);
        rootBox.setMaxWidth(460);
        rootBox.setMaxHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(rootBox, Pos.CENTER);

        Text title = new Text("💳 Razorpay Payment Checkout");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text info = new Text("• Equipment: " + item.equipmentName + "\n• Scheduled: " + item.startDate + " to " + item.endDate + "\n• Total Due: " + item.totalAmount);
        info.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #374151;");

        Label statusLbl = new Label("Click below to open the Razorpay payment gateway.");
        statusLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-fill: #6B7280;");
        statusLbl.setWrapText(true);

        int amt = 1000;
        try {
            amt = Integer.parseInt(item.totalAmount.replaceAll("[^0-9]", ""));
        } catch (Exception ignored) {}
        final int finalAmt = amt;

        Button payBtn = new Button("🚀 Launch Razorpay (" + item.totalAmount + ")");
        payBtn.setMaxWidth(Double.MAX_VALUE);
        payBtn.setPrefHeight(42);
        payBtn.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        Button confirmBtn = new Button("✔ Confirm Payment & Secure Booking");
        confirmBtn.setMaxWidth(Double.MAX_VALUE);
        confirmBtn.setPrefHeight(42);
        confirmBtn.setStyle("-fx-background-color: #15803D; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        payBtn.setOnAction(e -> {
            payBtn.setDisable(true);
            statusLbl.setText("Connecting to Razorpay gateway...");
            new Thread(() -> {
                try {
                    String url = com.desgin.service.RazorpayService.createPaymentLink(
                            finalAmt,
                            item.bookingId,
                            com.desgin.view.farmer.Swapnil.FarmerProfileStore.name,
                            com.desgin.view.farmer.Swapnil.FarmerProfileStore.email,
                            com.desgin.view.farmer.Swapnil.FarmerProfileStore.phone
                    );
                    com.desgin.service.RazorpayService.openPaymentInBrowser(url);
                    javafx.application.Platform.runLater(() -> {
                        statusLbl.setText("Payment link launched in browser! Complete payment and click Confirm.");
                        statusLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2563EB;");
                        payBtn.setDisable(false);
                    });
                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() -> {
                        statusLbl.setText("Notice: " + ex.getMessage());
                        statusLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-fill: #B91C1C;");
                        payBtn.setDisable(false);
                    });
                }
            }).start();
        });

        confirmBtn.setOnAction(e -> {
            confirmBtn.setDisable(true);
            statusLbl.setText("Verifying and confirming payment...");
            statusLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-fill: #2563EB;");
            new Thread(() -> {
                try {
                    new com.desgin.service.BookingService().confirmPayment(
                            item.bookingId,
                            "PAY_RZP_" + System.currentTimeMillis(),
                            "ORD_" + System.currentTimeMillis(),
                            "Razorpay Online"
                    );
                    javafx.application.Platform.runLater(() -> {
                        rootPane.getChildren().remove(overlay);
                        syncBookingsFromFirestore();
                    });
                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() -> {
                        statusLbl.setText("Payment confirmation failed: " + ex.getMessage());
                        statusLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #B91C1C;");
                        confirmBtn.setDisable(false);
                    });
                }
            }).start();
        });

        Button cancelModalBtn = new Button("Close");
        cancelModalBtn.setStyle("-fx-background-color: #E5E7EB; -fx-text-fill: #374151; -fx-font-family: 'Poppins'; -fx-background-radius: 6; -fx-cursor: hand;");
        cancelModalBtn.setOnAction(e -> rootPane.getChildren().remove(overlay));

        HBox bottomRow = new HBox(8, cancelModalBtn);
        bottomRow.setAlignment(Pos.CENTER_RIGHT);

        rootBox.getChildren().addAll(title, info, statusLbl, payBtn, confirmBtn, bottomRow);
        overlay.getChildren().add(rootBox);
        overlay.setOnMouseClicked(e -> {
            if (e.getTarget() == overlay) rootPane.getChildren().remove(overlay);
        });

        rootPane.getChildren().add(overlay);
    }

    private void alert(String title, String msg) {
        javafx.scene.control.Alert a = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }

    private void doCancel(BookingItem item) {
        javafx.scene.control.Alert conf = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        conf.setTitle("Cancel Booking"); conf.setHeaderText("Cancel #"+item.bookingId+"?");
        conf.setContentText("Are you sure you want to cancel your booking for "+item.equipmentName+"?");
        java.util.Optional<javafx.scene.control.ButtonType> r = conf.showAndWait();
        if (r.isPresent()&&r.get()==javafx.scene.control.ButtonType.OK) {
            item.status="CANCELLED"; BookingDataStore.cancelBooking(item.bookingId); updateKpi(); refresh(null);
            new Thread(()->{try{new com.desgin.service.BookingService().cancelBooking(item.bookingId,"Farmer");javafx.application.Platform.runLater(this::syncFromDB);}catch(Exception ex){System.err.println("Cancel err: "+ex.getMessage());}}).start();
        }
    }
}
