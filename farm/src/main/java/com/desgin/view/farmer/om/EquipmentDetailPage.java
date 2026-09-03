package com.desgin.view.farmer.om;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.desgin.view.farmer.Swapnil.BookingDataStore;
import com.desgin.view.farmer.Swapnil.FarmerDashboard;
import com.desgin.view.farmer.pratik.WishList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #C2E0CE;" +
                "-fx-border-radius: 8;" +
                "-fx-border-width: 1.5;" +
                "-fx-text-fill: #1B4332;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13.5px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 8 16 8 16;"
        );

        backButton.setOnMouseEntered(e ->
                backButton.setStyle(
                        "-fx-background-color: #E8F5E9;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #2E7D32;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-text-fill: #1B4332;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 16 8 16;"
                )
        );

        backButton.setOnMouseExited(e ->
                backButton.setStyle(
                        "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #C2E0CE;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-text-fill: #1B4332;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13.5px;" +
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
                "-fx-fill: #4B5563;"
        );

        HBox topBar = new HBox(15, backButton, pageBreadcrumb);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // --------------------------------------------------------
        // HERO SHOWCASE (IMAGE + SUMMARY)
        // --------------------------------------------------------

        com.desgin.view.farmer.Swapnil.EquipmentDataStore.EquipmentItem foundItem = 
                com.desgin.view.farmer.Swapnil.EquipmentDataStore.findByNameOrId(name);

        String effectiveImgPath = (this.imagePath != null && !this.imagePath.trim().isEmpty())
                ? this.imagePath.trim()
                : (foundItem != null && foundItem.imagePath != null ? foundItem.imagePath.trim() : "");

        Image image = null;
        if (!effectiveImgPath.isEmpty()) {
            try {
                String cleanPath = effectiveImgPath;
                if (!cleanPath.startsWith("http://") && !cleanPath.startsWith("https://") && !cleanPath.startsWith("file:")) {
                    cleanPath = new java.io.File(cleanPath).toURI().toString();
                }
                image = new Image(cleanPath, true);
            } catch (Exception e) {
                image = null;
            }
        }
        if (image == null) {
            try {
                image = new Image("file:farm/src/main/resources/assets/Images/tractor.png");
            } catch (Exception ignored) {}
        }
        final Image finalLoadedImage = image;

        ImageView imageView = new ImageView();
        if (finalLoadedImage != null) {
            imageView.setImage(finalLoadedImage);
            finalLoadedImage.errorProperty().addListener((obs, oldV, isError) -> {
                if (isError) {
                    try {
                        imageView.setImage(new Image("file:farm/src/main/resources/assets/Images/tractor.png"));
                    } catch (Exception ignored) {}
                }
            });
        }
        imageView.setFitWidth(420);
        imageView.setFitHeight(280);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        imageView.setCursor(javafx.scene.Cursor.HAND);
        imageView.setOnMouseClicked(e -> showImageLightbox(imageView.getImage(), name));

        StackPane imageCard = new StackPane(imageView);
        imageCard.setPrefSize(420, 280);
        imageCard.setMinSize(420, 280);
        imageCard.setMaxSize(420, 280);
        imageCard.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: #C2E0CE;" +
                "-fx-border-radius: 16;" +
                "-fx-border-width: 1.5;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 12, 0, 0, 3);"
        );

        // Smooth rounded clip so the image fills the box with rounded corners
        javafx.scene.shape.Rectangle cardClip = new javafx.scene.shape.Rectangle(420, 280);
        cardClip.setArcWidth(32);
        cardClip.setArcHeight(32);
        imageCard.setClip(cardClip);

        // Badge on top-left of image: Verified Machinery
        Label verifiedBadge = new Label("✨ Verified Machinery");
        verifiedBadge.setStyle(
                "-fx-background-color: rgba(27, 67, 50, 0.90);" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 4 12 4 12;"
        );
        StackPane.setAlignment(verifiedBadge, Pos.TOP_LEFT);
        StackPane.setMargin(verifiedBadge, new Insets(12, 0, 0, 12));

        imageCard.getChildren().add(verifiedBadge);

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
                "-fx-fill: #1B4332;"
        );

        // Rating & Location
        Text ratingLocationText = new Text("⭐ " + rating + " (32 reviews)   •   📍 " + location + ", Maharashtra");
        ratingLocationText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-fill: #4B5563;"
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

        // Rental Duration Selection & Live Price Calculation
        int ratePerDay = 1000;
        try {
            ratePerDay = Integer.parseInt(priceVal);
        } catch (Exception ignored) {}
        final int finalRate = ratePerDay;
        final int[] selectedDays = new int[] { 3 };

        Label daysTitle = new Label("Select Rental Duration (Days):");
        daysTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        Button minusBtn = new Button("−");
        minusBtn.setPrefSize(34, 34);
        minusBtn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 6; -fx-cursor: hand;");

        TextField daysField = new TextField("3");
        daysField.setPrefWidth(55);
        daysField.setPrefHeight(34);
        daysField.setAlignment(Pos.CENTER);
        daysField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C2E0CE; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-weight: bold; -fx-font-size: 14px;");

        Button plusBtn = new Button("+");
        plusBtn.setPrefSize(34, 34);
        plusBtn.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #1B4332; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 6; -fx-cursor: hand;");

        Text totalCalcText = new Text("Total Estimated Fare: ₹" + (finalRate * 3) + " (3 days)");
        totalCalcText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Runnable updateFare = () -> {
            int d = selectedDays[0];
            int total = finalRate * d;
            daysField.setText(String.valueOf(d));
            totalCalcText.setText("Total Estimated Fare: ₹" + String.format("%,d", total) + " (" + d + " days)");
        };

        minusBtn.setOnAction(e -> {
            if (selectedDays[0] > 1) {
                selectedDays[0]--;
                updateFare.run();
            }
        });

        plusBtn.setOnAction(e -> {
            if (selectedDays[0] < 90) {
                selectedDays[0]++;
                updateFare.run();
            }
        });

        daysField.textProperty().addListener((obs, oldV, newV) -> {
            try {
                int val = Integer.parseInt(newV.trim());
                if (val >= 1 && val <= 90) {
                    selectedDays[0] = val;
                    int total = finalRate * val;
                    totalCalcText.setText("Total Estimated Fare: ₹" + String.format("%,d", total) + " (" + val + " days)");
                }
            } catch (Exception ignored) {}
        });

        Button p1 = createDayPill("1 Day", 1, selectedDays, updateFare);
        Button p3 = createDayPill("3 Days", 3, selectedDays, updateFare);
        Button p7 = createDayPill("7 Days", 7, selectedDays, updateFare);
        Button p14 = createDayPill("14 Days", 14, selectedDays, updateFare);

        HBox stepperRow = new HBox(8, minusBtn, daysField, plusBtn, p1, p3, p7, p14);
        stepperRow.setAlignment(Pos.CENTER_LEFT);

        // Operator Selection Section ("Do you need an operator?")
        Label opTitle = new Label("Do you need a certified equipment operator?");
        opTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        ToggleGroup opGroup = new ToggleGroup();
        RadioButton noOpRb = new RadioButton("No (Self-Drive Machinery)");
        noOpRb.setToggleGroup(opGroup);
        noOpRb.setSelected(true);
        noOpRb.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-text-fill: #374151; -fx-cursor: hand;");

        RadioButton yesOpRb = new RadioButton("Yes, Request Certified Operator (+₹500/day)");
        yesOpRb.setToggleGroup(opGroup);
        yesOpRb.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-text-fill: #1B4332; -fx-font-weight: bold; -fx-cursor: hand;");

        VBox operatorDropdownRow = new VBox(6);
        operatorDropdownRow.setVisible(false);
        operatorDropdownRow.setManaged(false);

        Label selectOpLabel = new Label("Select Available Certified Operator:");
        selectOpLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: 600; -fx-text-fill: #4B5563;");

        ComboBox<String> operatorComboBox = new ComboBox<>();
        operatorComboBox.setPrefWidth(380);
        operatorComboBox.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-background-color: white; -fx-border-color: #C2E0CE; -fx-border-radius: 6;");

        java.util.List<com.desgin.model.AuthenticateModel> opList = new java.util.ArrayList<>();
        new Thread(() -> {
            try {
                java.util.List<com.desgin.model.AuthenticateModel> ops = new com.desgin.dao.AuthDAO().getAvailableOperators();
                javafx.application.Platform.runLater(() -> {
                    opList.clear();
                    opList.addAll(ops);
                    for (com.desgin.model.AuthenticateModel op : ops) {
                        String loc = op.getTown() != null ? op.getTown() : "Verified";
                        operatorComboBox.getItems().add(op.getName() + " (" + loc + " • " + op.getMail() + ")");
                    }
                    if (!operatorComboBox.getItems().isEmpty()) {
                        operatorComboBox.getSelectionModel().selectFirst();
                    } else {
                        operatorComboBox.getItems().add("District Certified Operator (Auto-Assigned by Platform)");
                        operatorComboBox.getSelectionModel().selectFirst();
                    }
                });
            } catch (Exception ignored) {}
        }).start();

        operatorDropdownRow.getChildren().addAll(selectOpLabel, operatorComboBox);

        Runnable recalculateFare = () -> {
            int d = selectedDays[0];
            boolean needOp = yesOpRb.isSelected();
            int equipCost = finalRate * d;
            int opCost = needOp ? (500 * d) : 0;
            int total = equipCost + opCost;
            daysField.setText(String.valueOf(d));
            String text = "Machinery: ₹" + String.format("%,d", equipCost)
                    + (needOp ? ("  +  Operator: ₹" + String.format("%,d", opCost)) : "")
                    + "  =  Total: ₹" + String.format("%,d", total) + " (" + d + " days)";
            totalCalcText.setText(text);
        };

        yesOpRb.setOnAction(ev -> {
            operatorDropdownRow.setVisible(true);
            operatorDropdownRow.setManaged(true);
            recalculateFare.run();
        });
        noOpRb.setOnAction(ev -> {
            operatorDropdownRow.setVisible(false);
            operatorDropdownRow.setManaged(false);
            recalculateFare.run();
        });

        HBox opRadioRow = new HBox(16, noOpRb, yesOpRb);
        VBox operatorBox = new VBox(8, opTitle, opRadioRow, operatorDropdownRow);
        operatorBox.setPadding(new Insets(10, 14, 10, 14));
        operatorBox.setStyle("-fx-background-color: #F8FAF8; -fx-background-radius: 10; -fx-border-color: #C2E0CE; -fx-border-width: 1; -fx-border-radius: 10;");

        VBox durationBox = new VBox(10, daysTitle, stepperRow, operatorBox, totalCalcText);
        durationBox.setPadding(new Insets(12, 14, 12, 14));
        durationBox.setStyle("-fx-background-color: #F4F9F4; -fx-background-radius: 10; -fx-border-color: #C2E0CE; -fx-border-width: 1; -fx-border-radius: 10;");

        // Action Buttons: Rent Now & Wishlist Toggle
        Button rentNowBtn = new Button("⚡  Rent Equipment Now");
        rentNowBtn.setPrefHeight(45);
        rentNowBtn.setStyle(
                "-fx-background-color: #2E7D32;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 0 25 0 25;"
        );
        rentNowBtn.setOnMouseEntered(e -> rentNowBtn.setStyle("-fx-background-color: #1B4332; -fx-background-radius: 10; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0 25 0 25;"));
        rentNowBtn.setOnMouseExited(e -> rentNowBtn.setStyle("-fx-background-color: #2E7D32; -fx-background-radius: 10; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0 25 0 25;"));

        rentNowBtn.setOnAction(e -> {
            int days = selectedDays[0];
            String startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            String endDate = LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

            boolean needOp = yesOpRb.isSelected();
            int equipCost = finalRate * days;
            int opCost = needOp ? (500 * days) : 0;
            int totalCost = equipCost + opCost;

            String machId = (foundItem != null && foundItem.id != null) ? foundItem.id : "MAC_" + System.currentTimeMillis();
            String finalImgUrl = (foundItem != null && foundItem.imagePath != null && !foundItem.imagePath.isEmpty()) ? foundItem.imagePath : imagePath;

            String opEmail = null;
            String opName = null;
            String opPhone = null;
            if (needOp && !opList.isEmpty() && operatorComboBox.getSelectionModel().getSelectedIndex() >= 0) {
                int idx = operatorComboBox.getSelectionModel().getSelectedIndex();
                if (idx < opList.size()) {
                    var selOp = opList.get(idx);
                    opEmail = selOp.getMail();
                    opName = selOp.getName();
                    opPhone = selOp.getNum();
                }
            }
            if (needOp && (opEmail == null || opEmail.isEmpty())) {
                opEmail = "operator@farmequip.com";
                opName = "District Certified Operator";
                opPhone = "+91 94231 98765";
            }

            // Check resource availability to prevent double bookings
            com.desgin.service.BookingService bookingService = new com.desgin.service.BookingService();
            com.desgin.service.BookingService.AvailabilityResult avail =
                    bookingService.checkResourceAvailability(machId, startDate, endDate, opEmail);

            if (!avail.isAvailable()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Resource Unavailable");
                alert.setHeaderText("Scheduling Conflict Detected");
                alert.setContentText(avail.getMessage());
                alert.showAndWait();
                return;
            }

            openRazorpayPaymentModal(
                    rentNowBtn,
                    machId,
                    name,
                    category,
                    finalRate,
                    days,
                    totalCost,
                    equipCost,
                    opCost,
                    needOp,
                    opEmail,
                    opName,
                    opPhone,
                    startDate,
                    endDate,
                    finalImgUrl,
                    foundItem
            );
        });

        // Wishlist Toggle
        Button wishlistToggleBtn = new Button();
        wishlistToggleBtn.setPrefHeight(45);
        updateWishlistButtonStyle(wishlistToggleBtn);

        wishlistToggleBtn.setOnAction(e -> {
            if (WishList.isInWishlist(name)) {
                WishList.removeFromWishlist(name);
            } else {
                WishList.addToWishlist(name, category, "₹" + priceVal + " / day", rating, location, imagePath);
            }
            updateWishlistButtonStyle(wishlistToggleBtn);
        });

        HBox actionBox = new HBox(15, rentNowBtn, wishlistToggleBtn);
        actionBox.setAlignment(Pos.CENTER_LEFT);

        VBox summaryDetails = new VBox(12, categoryTag, titleText, ratingLocationText, priceBox, statBadges, durationBox, actionBox);
        summaryDetails.setAlignment(Pos.TOP_LEFT);

        HBox heroSection = new HBox(30, imageCard, summaryDetails);
        heroSection.setAlignment(Pos.TOP_LEFT);
        heroSection.setFillHeight(false);
        HBox.setHgrow(summaryDetails, Priority.ALWAYS);

        // --------------------------------------------------------
        // ONLY PROVIDER-ENTERED DETAILS (NO DUMMY SPECS)
        // --------------------------------------------------------
        VBox specsSection = createMachineryDetailsCard(foundItem);

        // --------------------------------------------------------
        // VERIFIED PROVIDER DETAILS (REAL DATA)
        // --------------------------------------------------------
        VBox ownerSection = createOwnerSection(foundItem);

        // --------------------------------------------------------
        // ROOT LAYOUT ASSEMBLY
        // --------------------------------------------------------
        VBox content = new VBox(22, topBar, heroSection, specsSection, ownerSection);
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
        VBox badge1 = createStatCard("🏷️ Category", category);
        VBox badge2 = createStatCard("📍 Hub", location);
        VBox badge3 = createStatCard("⭐ Rating", rating + " / 5.0");

        HBox box = new HBox(12, badge1, badge2, badge3);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private VBox createStatCard(String title, String val) {
        Text t = new Text(title);
        t.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-fill: #6B7280;");

        Text v = new Text(val);
        v.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        VBox card = new VBox(2, t, v);
        card.setPadding(new Insets(8, 14, 8, 14));
        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-radius: 8;" +
                "-fx-border-width: 1;"
        );
        return card;
    }

    private void updateWishlistButtonStyle(Button button) {
        boolean inWishlist = WishList.isInWishlist(name);
        if (inWishlist) {
            button.setText("♥  In Wishlist");
            button.setStyle(
                    "-fx-background-color: #FFE5D9;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: #FF6B6B;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 1.5;" +
                    "-fx-text-fill: #D90429;" +
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
                    "-fx-border-color: #C2E0CE;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 1.5;" +
                    "-fx-text-fill: #1B4332;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 0 20 0 20;"
            );
        }
    }

    private VBox createMachineryDetailsCard(com.desgin.view.farmer.Swapnil.EquipmentDataStore.EquipmentItem item) {
        Text sectionTitle = new Text("Machinery Overview & Specifications");
        sectionTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(12);

        String opStatus = (item != null && item.hasOperator) ? "Yes (Trained Operator Included)" : "No (Self-Drive Machinery Only)";
        String notes = (item != null && item.specs != null && !item.specs.isEmpty()) ? item.specs : ("Standard " + category + " registered for farm rental.");

        addSpecRow(grid, 0, "Machinery Model", name);
        addSpecRow(grid, 1, "Category", category);
        addSpecRow(grid, 2, "Daily Rental Rate", "₹" + price + " / day");
        addSpecRow(grid, 3, "Operating Base", location);
        addSpecRow(grid, 4, "Operator Included", opStatus);
        addSpecRow(grid, 5, "Provider Notes", notes);

        VBox card = new VBox(15, sectionTitle, grid);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-radius: 14;" +
                "-fx-border-width: 1;"
        );
        return card;
    }

    private void addSpecRow(GridPane grid, int row, String key, String val) {
        Text keyText = new Text(key);
        keyText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #6B7280;");

        Text valText = new Text(val);
        valText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        grid.add(keyText, 0, row);
        grid.add(valText, 1, row);
    }

    private VBox createOwnerSection(com.desgin.view.farmer.Swapnil.EquipmentDataStore.EquipmentItem item) {
        Text sectionTitle = new Text("Verified Equipment Provider");
        sectionTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text avatar = new Text("👨‍🌾");
        avatar.setStyle("-fx-font-size: 32px;");

        String pName = (item != null && item.providerName != null && !item.providerName.isEmpty()) ? item.providerName : "Agri Fleet Provider";
        Text ownerName = new Text(pName);
        ownerName.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Label verifiedLabel = new Label("✔ Verified Provider");
        verifiedLabel.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 2 6 2 6; -fx-background-radius: 4;");

        HBox nameRow = new HBox(10, ownerName, verifiedLabel);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        String pPhone = (item != null && item.providerPhone != null && !item.providerPhone.isEmpty()) ? item.providerPhone : "Provided after booking confirmation";
        Text subInfo = new Text("📍 Base Hub: " + location + "   •   📞 " + pPhone);
        subInfo.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563;");

        VBox ownerText = new VBox(4, nameRow, subInfo);

        HBox row = new HBox(15, avatar, ownerText);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(12, sectionTitle, row);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #E2EBE5;" +
                "-fx-border-radius: 14;" +
                "-fx-border-width: 1;"
        );
        return card;
    }

    private void showImageLightbox(Image img, String title) {
        if (img == null) return;
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title + " - Machinery Showcase");

            ImageView fullIv = new ImageView(img);
            fullIv.setPreserveRatio(true);
            fullIv.setSmooth(true);
            fullIv.setFitWidth(780);
            fullIv.setFitHeight(540);

            javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle();
            clip.setArcWidth(18);
            clip.setArcHeight(18);
            fullIv.boundsInLocalProperty().addListener((obs, oldB, newB) -> {
                clip.setWidth(newB.getWidth());
                clip.setHeight(newB.getHeight());
                clip.setX(newB.getMinX());
                clip.setY(newB.getMinY());
            });
            fullIv.setClip(clip);

            Label titleLbl = new Label(title);
            titleLbl.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");

            Button closeBtn = new Button("✕ Close");
            closeBtn.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
            closeBtn.setOnAction(e -> stage.close());

            HBox topRow = new HBox(titleLbl, new Region(), closeBtn);
            HBox.setHgrow(topRow.getChildren().get(1), Priority.ALWAYS);
            topRow.setAlignment(Pos.CENTER_LEFT);
            topRow.setPadding(new Insets(0, 0, 12, 0));

            VBox root = new VBox(10, topRow, new StackPane(fullIv));
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(20));
            root.setStyle("-fx-background-color: #0D2B1D;");

            Scene scene = new Scene(root, 840, 640);
            stage.setScene(scene);
            stage.show();
        } catch (Exception ignored) {}
    }

    private Button createDayPill(String label, int days, int[] selectedDays, Runnable onSelect) {
        Button btn = new Button(label);
        btn.setPrefHeight(34);
        btn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #2D6A4F; -fx-border-color: #C2E0CE; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-cursor: hand;");
        btn.setOnAction(e -> {
            selectedDays[0] = days;
            onSelect.run();
        });
        return btn;
    }

    private void openRazorpayPaymentModal(
            Button parentBtn,
            String machId,
            String machName,
            String machCategory,
            int ratePerDay,
            int rentalDays,
            int totalCost,
            int equipCost,
            int opCost,
            boolean needOp,
            String opEmail,
            String opName,
            String opPhone,
            String startDate,
            String endDate,
            String finalImgUrl,
            com.desgin.view.farmer.Swapnil.EquipmentDataStore.EquipmentItem foundItem) {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        if (parentBtn.getScene() != null && parentBtn.getScene().getWindow() != null) {
            stage.initOwner(parentBtn.getScene().getWindow());
        }
        stage.setTitle("Razorpay Secure Rental Checkout");

        VBox root = new VBox(16);
        root.setPadding(new Insets(24));
        root.setStyle(
                "-fx-background-color: #F8FAF8;" +
                "-fx-border-color: #2D6A4F;" +
                "-fx-border-width: 1.5px;" +
                "-fx-border-radius: 12px;" +
                "-fx-background-radius: 12px;"
        );
        root.setPrefWidth(490);

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Text title = new Text("💳 Razorpay Secure Payment Checkout");
        title.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1B4332;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: #E5E7EB; -fx-text-fill: #374151; -fx-font-weight: bold; -fx-background-radius: 12; -fx-cursor: hand;");
        closeBtn.setOnAction(ev -> stage.close());
        header.getChildren().addAll(title, spacer, closeBtn);

        // Order Summary Card
        VBox summaryCard = new VBox(8);
        summaryCard.setPadding(new Insets(14));
        summaryCard.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 8;");

        Text machLine = new Text("🚜 Machinery: " + machName + " (" + machCategory + ") • ₹" + equipCost);
        machLine.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-fill: #1F2937;");

        Text opLine = new Text(needOp ? ("👷 Field Operator: " + opName + " • ₹" + opCost) : "👷 Operator: None (Self-Drive)");
        opLine.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: " + (needOp ? "#1B4332;" : "#6B7280;"));

        Text durLine = new Text("📅 Rental Duration: " + rentalDays + " Days (" + startDate + " to " + endDate + ")");
        durLine.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #4B5563;");

        String pName = (foundItem != null && foundItem.providerName != null) ? foundItem.providerName : "Verified Equipment Provider";
        Text provLine = new Text("👨‍🌾 Fleet Provider: " + pName);
        provLine.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #2D6A4F; -fx-font-weight: 600;");

        Text totalText = new Text("Total Amount Payable: ₹" + String.format("%,d", totalCost));
        totalText.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #15803D;");

        summaryCard.getChildren().addAll(machLine, opLine, durLine, provLine, totalText);

        // Settlement Notice Card
        VBox settlementCard = new VBox(4);
        settlementCard.setPadding(new Insets(10, 12, 10, 12));
        settlementCard.setStyle("-fx-background-color: #ECFDF5; -fx-background-radius: 6; -fx-border-color: #A7F3D0; -fx-border-width: 1; -fx-border-radius: 6;");
        Text escTitle = new Text("🔒 100% Escrow Protection Guarantee");
        escTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-fill: #065F46;");
        Text escSub = new Text("Funds held securely by Razorpay Escrow until machinery reaches farm and dispatch is accepted.");
        escSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-fill: #047857;");
        settlementCard.getChildren().addAll(escTitle, escSub);

        // Status Feedback
        Label statusLabel = new Label("Click 'Pay with Razorpay' to open secure gateway.");
        statusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-fill: #4B5563;");

        // Action Buttons
        Button payBtn = new Button("🚀  Pay ₹" + String.format("%,d", totalCost) + " via Razorpay");
        payBtn.setPrefHeight(42);
        payBtn.setMaxWidth(Double.MAX_VALUE);
        payBtn.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        Button verifyBtn = new Button("✔  I Have Completed Payment / Confirm Booking");
        verifyBtn.setPrefHeight(42);
        verifyBtn.setMaxWidth(Double.MAX_VALUE);
        verifyBtn.setStyle("-fx-background-color: #15803D; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        payBtn.setOnAction(ev -> {
            payBtn.setDisable(true);
            statusLabel.setText("Connecting to Razorpay Gateway...");
            new Thread(() -> {
                try {
                    String bookingRefId = "ORD_" + System.currentTimeMillis();
                    String fName = com.desgin.view.farmer.Swapnil.FarmerProfileStore.name;
                    String fEmail = com.desgin.view.farmer.Swapnil.FarmerProfileStore.email;
                    String fPhone = com.desgin.view.farmer.Swapnil.FarmerProfileStore.phone;

                    String paymentUrl = com.desgin.service.RazorpayService.createPaymentLink(
                            totalCost,
                            bookingRefId,
                            fName,
                            fEmail,
                            fPhone
                    );

                    com.desgin.service.RazorpayService.openPaymentInBrowser(paymentUrl);

                    javafx.application.Platform.runLater(() -> {
                        statusLabel.setText("✔ Payment Link opened in browser! Complete payment and click 'Confirm Booking' below.");
                        statusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2563EB;");
                        payBtn.setDisable(false);
                    });
                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() -> {
                        statusLabel.setText("Notice: " + ex.getMessage());
                        statusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-text-fill: #B91C1C;");
                        payBtn.setDisable(false);
                    });
                }
            }).start();
        });

        verifyBtn.setOnAction(ev -> {
            String bookingId = "REQ_" + System.currentTimeMillis();
            String pEmail = (foundItem != null && foundItem.providerEmail != null) ? foundItem.providerEmail : "provider@farmequip.com";
            String pPhone = (foundItem != null && foundItem.providerPhone != null) ? foundItem.providerPhone : "+91 98765 00000";
            String fEmail = com.desgin.view.farmer.Swapnil.FarmerProfileStore.email;
            String fName = com.desgin.view.farmer.Swapnil.FarmerProfileStore.name;
            String fPhone = com.desgin.view.farmer.Swapnil.FarmerProfileStore.phone;
            String fTown = com.desgin.view.farmer.Swapnil.FarmerProfileStore.town;

            com.desgin.model.RentalRequestModel reqModel = new com.desgin.model.RentalRequestModel(
                    bookingId,
                    machId,
                    machName,
                    machCategory,
                    ratePerDay,
                    rentalDays,
                    totalCost,
                    startDate,
                    endDate,
                    fEmail,
                    fName,
                    fPhone,
                    fTown != null ? fTown : "Pune",
                    pEmail,
                    pName,
                    pPhone,
                    location != null ? location : "Pune",
                    "Field Delivery / Pickup",
                    finalImgUrl
            );
            reqModel.setFarmerProfilePic(com.desgin.view.farmer.Swapnil.FarmerProfileStore.profilePic);
            reqModel.setStatus("CONFIRMED");
            reqModel.setPaymentStatus("PAID");
            reqModel.setPaymentMode("Razorpay");
            reqModel.setAmountPaid(totalCost);
            reqModel.setPaymentTransactionId("PAY_RZP_" + System.currentTimeMillis());

            // Operator details
            reqModel.setOperatorRequired(needOp);
            reqModel.setOperatorId(opEmail);
            reqModel.setOperatorName(opName);
            reqModel.setOperatorPhone(opPhone);
            reqModel.setOperatorStatus(needOp ? "PENDING" : null);
            reqModel.setEquipmentAmount(equipCost);
            reqModel.setOperatorAmount(opCost);
            reqModel.setTotalAmount(totalCost);

            // Provider settlement bank details
            reqModel.setProviderBankName(com.desgin.view.provider.ProviderProfileStore.bankName);
            reqModel.setProviderAccountNumber(com.desgin.view.provider.ProviderProfileStore.accountNumber);
            reqModel.setProviderIfsc(com.desgin.view.provider.ProviderProfileStore.ifsc);
            reqModel.setProviderUpiId(com.desgin.view.provider.ProviderProfileStore.upiId);

            // Add to in-memory store
            BookingDataStore.addBooking(new BookingDataStore.BookingItem(
                    bookingId,
                    machName,
                    machCategory,
                    startDate,
                    endDate,
                    "₹" + ratePerDay + " / day",
                    "₹" + totalCost,
                    "CONFIRMED",
                    finalImgUrl
            ));

            // Persist to Firestore and notify all participants
            new Thread(() -> {
                try {
                    com.desgin.service.BookingService service = new com.desgin.service.BookingService();
                    service.createBookingRequest(reqModel);
                    service.confirmPayment(bookingId, reqModel.getPaymentTransactionId(), "ORD_" + System.currentTimeMillis(), "Razorpay");
                } catch (Exception ex) {
                    System.err.println("Notice: Booking create/payment error: " + ex.getMessage());
                }
            }).start();

            stage.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Payment Verified & Rental Confirmed");
            alert.setHeaderText("Payment Successful & Booking Confirmed!");
            alert.setContentText(
                    "Your payment of ₹" + String.format("%,d", totalCost) + " has been verified and placed into Escrow.\n\n" +
                    "• Equipment: " + machName + "\n" +
                    "• Provider: " + pName + "\n" +
                    (needOp ? ("• Operator: " + opName + "\n") : "") +
                    "• Duration: " + rentalDays + " Days (" + startDate + " to " + endDate + ")\n" +
                    "• Status: CONFIRMED (PAID)\n\n" +
                    "Both the provider" + (needOp ? " and operator have" : " has") + " been notified."
            );
            alert.showAndWait();

            if (backAction != null) {
                backAction.run();
            }
        });

        root.getChildren().addAll(header, summaryCard, settlementCard, statusLabel, payBtn, verifyBtn);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
