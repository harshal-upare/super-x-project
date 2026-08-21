package com.desgin.view.farmer.om;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.desgin.view.farmer.Swapnil.BookingDataStore;
import com.desgin.view.farmer.Swapnil.FarmerDashboard;
import com.desgin.view.farmer.pratik.WishList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class EquipmentDetailPage {

    private final String name;
    private final String category;
    private final String price;
    private final String rating;
    private final String location;
    private final String imagePath;
    private final Runnable backAction;

    public EquipmentDetailPage(
            String name,
            String category,
            String price,
            String rating,
            String location,
            String imagePath,
            Runnable backAction) {

        this.name = name != null ? name : "Agricultural Equipment";
        this.category = category != null ? category : "Equipment";
        this.price = price != null ? price : "1000";
        this.rating = rating != null ? rating : "4.5";
        this.location = location != null ? location : "Pune";
        this.imagePath = imagePath != null ? imagePath : "";
        this.backAction = backAction;
    }

    public ScrollPane getDetailPage() {

        // --------------------------------------------------------
        // BACK NAVIGATION HEADER
        // --------------------------------------------------------

        Button backButton = new Button("←  Back");
        backButton.setStyle(
                "-fx-background-color: #E4D3C2;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: #4A2C20;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 8 16 8 16;"
        );

        backButton.setOnMouseEntered(e ->
                backButton.setStyle(
                        "-fx-background-color: #D8C7B5;" +
                        "-fx-background-radius: 8;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 16 8 16;"
                )
        );

        backButton.setOnMouseExited(e ->
                backButton.setStyle(
                        "-fx-background-color: #E4D3C2;" +
                        "-fx-background-radius: 8;" +
                        "-fx-text-fill: #4A2C20;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 16 8 16;"
                )
        );

        backButton.setOnAction(e -> {
            if (backAction != null) {
                backAction.run();
            }
        });

        Text pageBreadcrumb = new Text("Browse / Equipment Details / " + name);
        pageBreadcrumb.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-fill: #806A5B;"
        );

        HBox topBar = new HBox(15, backButton, pageBreadcrumb);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // --------------------------------------------------------
        // HERO SHOWCASE (IMAGE + SUMMARY)
        // --------------------------------------------------------

        Image image;
        try {
            image = new Image(imagePath, true);
        } catch (Exception e) {
            image = null;
        }

        ImageView imageView = new ImageView();
        if (image != null) {
            imageView.setImage(image);
        }
        imageView.setFitWidth(400);
        imageView.setFitHeight(250);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        StackPane imageCard = new StackPane(imageView);
        imageCard.setPrefSize(400, 250);
        imageCard.setStyle(
                "-fx-background-color: #E8DED2;" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-radius: 16;" +
                "-fx-border-width: 1;"
        );

        // Category Tag
        Label categoryTag = new Label(category.toUpperCase());
        categoryTag.setStyle(
                "-fx-background-color: #E8F5E9;" +
                "-fx-text-fill: #2E7D32;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 4 10 4 10;"
        );

        // Equipment Name
        Text titleText = new Text(name);
        titleText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;"
        );

        // Rating & Location
        Text ratingLocationText = new Text("⭐ " + rating + " (32 reviews)   •   📍 " + location + ", Maharashtra");
        ratingLocationText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-fill: #5C4033;"
        );

        // Price Section
        String rawPrice = price.replaceAll("[^0-9]", "");
        final String priceVal = rawPrice.isEmpty() ? "1000" : rawPrice;

        Text priceText = new Text("₹" + priceVal + " / day");
        priceText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #6B8E23;"
        );

        Label discountBadge = new Label("🏷️ 10% OFF for 7+ days booking");
        discountBadge.setStyle(
                "-fx-background-color: #FFF3E0;" +
                "-fx-text-fill: #E65100;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 4 10 4 10;"
        );

        HBox priceBox = new HBox(12, priceText, discountBadge);
        priceBox.setAlignment(Pos.CENTER_LEFT);

        // Quick Stats Badges
        HBox statBadges = createQuickStatBadges();

        // Action Buttons: Rent Now & Wishlist Toggle
        Button rentNowBtn = new Button("⚡  Rent Equipment Now");
        rentNowBtn.setPrefHeight(45);
        rentNowBtn.setStyle(
                "-fx-background-color: #6B8E23;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 0 25 0 25;"
        );
        rentNowBtn.setOnMouseEntered(e ->
                rentNowBtn.setStyle(
                        "-fx-background-color: #55751C;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 0 25 0 25;"
                )
        );
        rentNowBtn.setOnMouseExited(e ->
                rentNowBtn.setStyle(
                        "-fx-background-color: #6B8E23;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 0 25 0 25;"
                )
        );

        rentNowBtn.setOnAction(e -> {
            String bookingId = "BK" + (10000 + (int)(Math.random() * 90000));
            String startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            String endDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            int rate = 1000;
            try {
                rate = Integer.parseInt(priceVal);
            } catch (Exception ignored) {}
            String totalCost = "₹" + (rate * 3);

            BookingDataStore.addBooking(new BookingDataStore.BookingItem(
                    bookingId,
                    name,
                    category,
                    startDate,
                    endDate,
                    "₹" + priceVal + "/day",
                    totalCost,
                    "ACTIVE",
                    imagePath
            ));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Confirmation");
            alert.setHeaderText("Rental Booking Confirmed!");
            alert.setContentText("Your booking for '" + name + "' (Booking ID: " + bookingId + ") for " + startDate + " to " + endDate + " at " + "₹" + priceVal + "/day has been confirmed!");
            alert.showAndWait();
        });

        Button wishlistToggleBtn = new Button();
        updateWishlistToggleStyle(wishlistToggleBtn);

        wishlistToggleBtn.setOnAction(e -> {
            if (WishList.isInWishlist(name)) {
                WishList.removeFromWishlist(name);
            } else {
                WishList.addToWishlist(name, category, priceVal, rating, location, imagePath);
            }
            updateWishlistToggleStyle(wishlistToggleBtn);
        });

        HBox actionBox = new HBox(15, rentNowBtn, wishlistToggleBtn);
        actionBox.setAlignment(Pos.CENTER_LEFT);

        VBox summaryDetails = new VBox(12, categoryTag, titleText, ratingLocationText, priceBox, statBadges, actionBox);
        summaryDetails.setAlignment(Pos.TOP_LEFT);

        HBox heroSection = new HBox(30, imageCard, summaryDetails);
        heroSection.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(summaryDetails, Priority.ALWAYS);

        // --------------------------------------------------------
        // SPECIFICATIONS GRID
        // --------------------------------------------------------

        VBox specsSection = createSpecsSection();

        // --------------------------------------------------------
        // PROVIDER / OWNER INFORMATION
        // --------------------------------------------------------

        VBox ownerSection = createOwnerSection();

        // --------------------------------------------------------
        // RENTAL TERMS & GUIDELINES
        // --------------------------------------------------------

        VBox termsSection = createTermsSection();

        // --------------------------------------------------------
        // ROOT LAYOUT ASSEMBLY
        // --------------------------------------------------------

        VBox content = new VBox(25, topBar, heroSection, specsSection, ownerSection, termsSection);
        content.setPadding(new Insets(25, 35, 35, 35));
        content.setMaxWidth(Double.MAX_VALUE);
        content.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private HBox createQuickStatBadges() {
        VBox badge1 = createStatCard("⚡ Power", getPowerForCategory());
        VBox badge2 = createStatCard("⛽ Fuel Type", "Diesel");
        VBox badge3 = createStatCard("⚙️ Model Year", "2023");
        VBox badge4 = createStatCard("🛡️ Condition", "Serviced & Verified");

        HBox box = new HBox(12, badge1, badge2, badge3, badge4);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private VBox createStatCard(String label, String value) {
        Text lbl = new Text(label);
        lbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #4B5563;");

        Text val = new Text(value);
        val.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        VBox box = new VBox(3, lbl, val);
        box.setPadding(new Insets(8, 12, 8, 12));
        box.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: rgba(45, 106, 79, 0.2);" +
                "-fx-border-radius: 8;" +
                "-fx-border-width: 1.2;"
        );
        return box;
    }

    private String getPowerForCategory() {
        switch (category.toLowerCase()) {
            case "tractor": return "45 HP";
            case "harvester": return "75 HP";
            case "seeder": return "PTO Driven";
            case "plough": return "Heavy Duty";
            case "cultivator": return "9 Tynes";
            default: return "40 HP";
        }
    }

    private void updateWishlistToggleStyle(Button button) {
        button.setPadding(Insets.EMPTY);
        button.setPrefHeight(45);

        if (WishList.isInWishlist(name)) {
            button.setText("♥  Saved in Wishlist");
            button.setStyle(
                    "-fx-background-color: #FFEBEE;" +
                    "-fx-background-radius: 10;" +
                    "-fx-text-fill: #E53935;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 0 20 0 20;"
            );
        } else {
            button.setText("♡  Add to Wishlist");
            button.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #D8C7B5;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 1;" +
                    "-fx-text-fill: #4A2C20;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 0 20 0 20;"
            );
        }
    }

    private VBox createSpecsSection() {
        Text sectionTitle = new Text("Technical Specifications");
        sectionTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(12);

        addSpecRow(grid, 0, "Engine Power", getPowerForCategory());
        addSpecRow(grid, 1, "Fuel Capacity", "47 Liters");
        addSpecRow(grid, 2, "Lifting Capacity", "1600 kg");
        addSpecRow(grid, 3, "Transmission Type", "Partial Constant Mesh");
        addSpecRow(grid, 4, "Total Hours Used", "320 hrs");
        addSpecRow(grid, 5, "Servicing History", "Regular Authorized Dealer Servicing");

        VBox card = new VBox(15, sectionTitle, grid);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #E0D4C7;" +
                "-fx-border-radius: 14;" +
                "-fx-border-width: 1;"
        );
        return card;
    }

    private void addSpecRow(GridPane grid, int row, String key, String val) {
        Text keyText = new Text(key);
        keyText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #7A6658;");

        Text valText = new Text(val);
        valText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        grid.add(keyText, 0, row);
        grid.add(valText, 1, row);
    }

    private VBox createOwnerSection() {
        Text sectionTitle = new Text("Verified Equipment Owner");
        sectionTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text avatar = new Text("👨‍🌾");
        avatar.setStyle("-fx-font-size: 32px;");

        Text ownerName = new Text("Ramesh Agro Services");
        ownerName.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Label verifiedLabel = new Label("✔ Verified Provider");
        verifiedLabel.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 2 6 2 6; -fx-background-radius: 4;");

        HBox nameRow = new HBox(10, ownerName, verifiedLabel);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        Text subInfo = new Text("📍 " + location + "   •   ⏱️ < 1 hr Response Time   •   ⭐ 4.9 Owner Rating");
        subInfo.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #7A6658;");

        VBox ownerText = new VBox(4, nameRow, subInfo);

        HBox row = new HBox(15, avatar, ownerText);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(12, sectionTitle, row);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #E0D4C7;" +
                "-fx-border-radius: 14;" +
                "-fx-border-width: 1;"
        );
        return card;
    }

    private VBox createTermsSection() {
        Text sectionTitle = new Text("Rental Policy & Guidelines");
        sectionTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4A2C20;");

        Text term1 = new Text("• Identity Verification: Valid Government ID (Aadhaar / Driving License) required at pickup.");
        Text term2 = new Text("• Delivery Options: Self-pickup or doorstep transport delivery within 25km (additional charges apply).");
        Text term3 = new Text("• Fuel Terms: Returned with equivalent fuel tank level (Full-to-Full policy).");
        Text term4 = new Text("• Safety & Maintenance: Fully insured against accidental mechanical failure during field operation.");

        String termStyle = "-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #5C4033;";
        term1.setStyle(termStyle);
        term2.setStyle(termStyle);
        term3.setStyle(termStyle);
        term4.setStyle(termStyle);

        VBox termsList = new VBox(8, term1, term2, term3, term4);

        VBox card = new VBox(12, sectionTitle, termsList);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #E0D4C7;" +
                "-fx-border-radius: 14;" +
                "-fx-border-width: 1;"
        );
        return card;
    }
}
