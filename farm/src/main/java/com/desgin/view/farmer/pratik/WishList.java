package com.desgin.view.farmer.pratik;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import com.desgin.view.farmer.Swapnil.FarmerDashboard;
import com.desgin.view.farmer.om.EquipmentDetailPage;

public class WishList {

    public static class WishlistItem {
        private String equipmentName;
        private String category;
        private String price;
        private String rating;
        private String location;
        private String imagePath;

        public WishlistItem(String equipmentName, String category, String price, String rating, String location, String imagePath) {
            this.equipmentName = equipmentName;
            this.category = category;
            this.price = price;
            this.rating = rating;
            this.location = location;
            this.imagePath = imagePath;
        }

        public String getEquipmentName() { return equipmentName; }
        public String getCategory() { return category; }
        public String getPrice() { return price; }
        public String getRating() { return rating; }
        public String getLocation() { return location; }
        public String getImagePath() { return imagePath; }
    }

    public static class OperatorWishlistItem {
        public String id;
        public String name;
        public String specialty;
        public String experience;
        public String location;
        public String rate;
        public double rating;
        public String phone;

        public OperatorWishlistItem(String id, String name, String specialty, String experience, String location, String rate, double rating, String phone) {
            this.id = id;
            this.name = name;
            this.specialty = specialty;
            this.experience = experience;
            this.location = location;
            this.rate = rate;
            this.rating = rating;
            this.phone = phone;
        }
    }

    // ============================================================
    // WISHLIST DATA
    // ============================================================

    private static final ObservableList<WishlistItem> wishlistItems =
            FXCollections.observableArrayList();

    private static final ObservableList<OperatorWishlistItem> operatorWishlist =
            FXCollections.observableArrayList();

    static {
        // Initial sample saved operator in wishlist for demonstration
        if (operatorWishlist.isEmpty()) {
            operatorWishlist.add(new OperatorWishlistItem(
                    "OP-101",
                    "Dilip Shinde",
                    "🚜 55HP+ Heavy 4WD Tractor Driver",
                    "8 Years Exp • 142 Jobs",
                    "Pune (Baramati Hub)",
                    "₹600 / day",
                    4.9,
                    "+91 98901 44552"
            ));
        }
    }

    public static void addToWishlist(
            String equipmentName,
            String category,
            String price,
            String rating,
            String location,
            String imagePath) {

        if (isInWishlist(equipmentName)) {
            return;
        }

        WishlistItem item = new WishlistItem(
                equipmentName,
                category,
                price,
                rating,
                location,
                imagePath
        );

        wishlistItems.add(item);
    }

    public static void removeFromWishlist(String equipmentName) {
        wishlistItems.removeIf(item -> item.getEquipmentName().equals(equipmentName));
    }

    public static boolean isInWishlist(String equipmentName) {
        for (WishlistItem item : wishlistItems) {
            if (item.getEquipmentName().equals(equipmentName)) {
                return true;
            }
        }
        return false;
    }

    public static ObservableList<WishlistItem> getWishlistItems() {
        return wishlistItems;
    }

    public static void addOperatorToWishlist(OperatorWishlistItem op) {
        if (op == null) return;
        for (OperatorWishlistItem item : operatorWishlist) {
            if (item.name.equalsIgnoreCase(op.name)) return;
        }
        operatorWishlist.add(op);
    }

    public static void removeOperatorFromWishlist(String name) {
        operatorWishlist.removeIf(op -> op.name.equalsIgnoreCase(name));
    }

    private static boolean isEquipmentTab = true;

    // ============================================================
    // WISHLIST PAGE
    // ============================================================

    public static ScrollPane getWishList() {

        // --------------------------------------------------------
        // TAB BUTTONS (EQUIPMENT vs OPERATORS)
        // --------------------------------------------------------

        Button tabEquipmentBtn = new Button("🚜  Equipment Wishlist (" + wishlistItems.size() + ")");
        Button tabOperatorBtn = new Button("👷  Operator Wishlist (" + operatorWishlist.size() + ")");

        HBox tabBox = new HBox(10, tabEquipmentBtn, tabOperatorBtn);
        tabBox.setAlignment(Pos.CENTER_LEFT);

        // --------------------------------------------------------
        // CARDS & LIST CONTAINERS
        // --------------------------------------------------------

        TilePane equipmentCards = new TilePane();
        equipmentCards.setHgap(16);
        equipmentCards.setVgap(16);
        equipmentCards.setPrefColumns(2);
        equipmentCards.setAlignment(Pos.TOP_LEFT);

        VBox operatorCardsBox = new VBox(12);
        operatorCardsBox.setMaxWidth(Double.MAX_VALUE);

        // --------------------------------------------------------
        // EMPTY MESSAGE
        // --------------------------------------------------------

        Text emptyIcon = new Text("❤️");
        emptyIcon.setStyle("-fx-font-size: 38px;");

        Text emptyText = new Text("No Items in Your Wishlist");
        emptyText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #1B4332;"
        );

        Text emptySub = new Text(
                "Click the heart icon on any equipment in Browse Equipment or bookmark operators in Search Operators to save them here."
        );
        emptySub.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-fill: #4B5563;"
        );

        VBox emptyBox = new VBox(10, emptyIcon, emptyText, emptySub);
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

        // --------------------------------------------------------
        // REFRESH METHOD
        // --------------------------------------------------------

        Runnable refreshView = new Runnable() {
            @Override
            public void run() {
                tabEquipmentBtn.setText("🚜  Equipment Wishlist (" + wishlistItems.size() + ")");
                tabOperatorBtn.setText("👷  Operator Wishlist (" + operatorWishlist.size() + ")");

                styleTabPill(tabEquipmentBtn, isEquipmentTab);
                styleTabPill(tabOperatorBtn, !isEquipmentTab);

                equipmentCards.getChildren().clear();
                operatorCardsBox.getChildren().clear();

                if (isEquipmentTab) {
                    equipmentCards.setVisible(true);
                    equipmentCards.setManaged(true);
                    operatorCardsBox.setVisible(false);
                    operatorCardsBox.setManaged(false);

                    if (wishlistItems.isEmpty()) {
                        emptyText.setText("No Equipment in Your Wishlist");
                        emptySub.setText("Click the heart icon on any machinery in Browse Equipment to save it here for fast booking.");
                        emptyBox.setVisible(true);
                        emptyBox.setManaged(true);
                    } else {
                        emptyBox.setVisible(false);
                        emptyBox.setManaged(false);
                        for (WishlistItem item : wishlistItems) {
                            equipmentCards.getChildren().add(createWishlistCard(item, this));
                        }
                    }
                } else {
                    equipmentCards.setVisible(false);
                    equipmentCards.setManaged(false);
                    operatorCardsBox.setVisible(true);
                    operatorCardsBox.setManaged(true);

                    if (operatorWishlist.isEmpty()) {
                        emptyText.setText("No Operators in Your Wishlist");
                        emptySub.setText("Navigate to Search Operators to bookmark skilled drivers and drone pilots for your farm.");
                        emptyBox.setVisible(true);
                        emptyBox.setManaged(true);
                    } else {
                        emptyBox.setVisible(false);
                        emptyBox.setManaged(false);
                        for (OperatorWishlistItem op : operatorWishlist) {
                            operatorCardsBox.getChildren().add(createOperatorWishlistCard(op, this));
                        }
                    }
                }
            }
        };

        tabEquipmentBtn.setOnAction(e -> {
            isEquipmentTab = true;
            refreshView.run();
        });

        tabOperatorBtn.setOnAction(e -> {
            isEquipmentTab = false;
            refreshView.run();
        });

        refreshView.run();

        // --------------------------------------------------------
        // MAIN CONTENT
        // --------------------------------------------------------

        VBox content = new VBox(
                18,
                tabBox,
                emptyBox,
                equipmentCards,
                operatorCardsBox
        );
        content.setPadding(new Insets(20, 30, 35, 30));
        content.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static void styleTabPill(Button btn, boolean active) {
        if (active) {
            btn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                    "-fx-text-fill: white;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 20px;" +
                    "-fx-padding: 8px 20px;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.25), 8, 0, 0, 2);"
            );
        } else {
            btn.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                    "-fx-text-fill: #4B5563;" +
                    "-fx-font-family: 'Poppins';" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: 500;" +
                    "-fx-background-radius: 20px;" +
                    "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                    "-fx-border-width: 1.2px;" +
                    "-fx-border-radius: 20px;" +
                    "-fx-padding: 8px 20px;" +
                    "-fx-cursor: hand;"
            );
        }
    }

    // ============================================================
    // CREATE OPERATOR WISHLIST CARD (HORIZONTAL)
    // ============================================================
    private static HBox createOperatorWishlistCard(OperatorWishlistItem op, Runnable refreshView) {
        HBox card = new HBox(16);
        card.setPadding(new Insets(14, 18, 14, 18));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
        );

        Text icon = new Text("👨‍🌾");
        if (op.specialty.contains("Harvester")) icon.setText("🌾");
        if (op.specialty.contains("Drone")) icon.setText("🚁");
        if (op.specialty.contains("Tractor")) icon.setText("🚜");
        icon.setStyle("-fx-font-size: 28px;");

        StackPane iconBox = new StackPane(icon);
        iconBox.setPrefSize(48, 48);
        iconBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 10px;");

        Text nameT = new Text(op.name);
        nameT.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 15.5px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text specT = new Text(op.specialty);
        specT.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: 600; -fx-fill: #2D6A4F;");

        Text expT = new Text("⭐ " + op.rating + " • 💼 " + op.experience + " • 📍 " + op.location);
        expT.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        VBox center = new VBox(3, nameT, specT, expT);
        HBox.setHgrow(center, Priority.ALWAYS);

        Text rateT = new Text(op.rate);
        rateT.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Button removeBtn = new Button("♥");
        removeBtn.setPrefSize(34, 34);
        removeBtn.setStyle("-fx-background-color: #FFEBEE; -fx-text-fill: #E53935; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 20px; -fx-padding: 0; -fx-cursor: hand;");
        removeBtn.setOnAction(e -> {
            removeOperatorFromWishlist(op.name);
            refreshView.run();
        });

        HBox right = new HBox(12, rateT, removeBtn);
        right.setAlignment(Pos.CENTER_RIGHT);

        card.getChildren().addAll(iconBox, center, right);
        return card;
    }

    // ============================================================
    // CREATE WISHLIST CARD (EQUIPMENT)
    // ============================================================

    private static VBox createWishlistCard(
            WishlistItem item,
            Runnable refreshWishlist) {

        WishList obj = new WishList();
        Image image = obj.loadImage(item.getImagePath());

        ImageView imageView = new ImageView();
        if (image != null) {
            imageView.setImage(image);
        }
        imageView.setFitWidth(280);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        Button removeButton = new Button("♥");
        removeButton.setPadding(Insets.EMPTY);
        removeButton.setMinSize(36, 36);
        removeButton.setPrefSize(36, 36);
        removeButton.setMaxSize(36, 36);
        removeButton.setStyle(
                "-fx-background-color: #FFEBEE;" +
                "-fx-background-radius: 20px;" +
                "-fx-text-fill: #E53935;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 0;" +
                "-fx-alignment: center;" +
                "-fx-cursor: hand;"
        );

        removeButton.setOnAction(event -> {
            removeFromWishlist(item.getEquipmentName());
            refreshWishlist.run();
        });

        StackPane imageContainer = new StackPane(imageView, removeButton);
        imageContainer.setPrefWidth(280);
        imageContainer.setPrefHeight(150);
        imageContainer.setStyle(
                "-fx-background-color: #E8F5E9;" +
                "-fx-background-radius: 14 14 0 0;"
        );

        StackPane.setAlignment(removeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(removeButton, new Insets(10));

        Text nameText = new Text(item.getEquipmentName());
        nameText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #1B4332;"
        );

        Text categoryText = new Text(item.getCategory());
        categoryText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-fill: #2D6A4F;"
        );

        Text ratingText = new Text("⭐ " + item.getRating());
        ratingText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #B45309;"
        );

        Text locationText = new Text("📍 " + item.getLocation());
        locationText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-fill: #4B5563;"
        );

        HBox infoRow = new HBox(15, ratingText, locationText);
        infoRow.setAlignment(Pos.CENTER_LEFT);

        Text priceText = new Text("₹" + item.getPrice() + " / day");
        priceText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #1B4332;"
        );

        Button viewButton = new Button("View Details");
        viewButton.setPrefHeight(40);
        viewButton.setMaxWidth(Double.MAX_VALUE);
        viewButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C);" +
                "-fx-background-radius: 10px;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12.5px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.25), 8, 0, 0, 2);"
        );

        viewButton.setOnAction(event -> {
            Runnable backAction = () -> {
                if (FarmerDashboard.borderPane != null) {
                    com.desgin.view.farmer.ashutosh.profile.ProfileManagement.setHeaderTitle("My Saved Wishlist ❤️", "Saved machinery and certified operators for quick booking");
                    FarmerDashboard.borderPane.setCenter(getWishList());
                }
            };

            EquipmentDetailPage detailPage = new EquipmentDetailPage(
                    item.getEquipmentName(),
                    item.getCategory(),
                    item.getPrice(),
                    item.getRating(),
                    item.getLocation(),
                    item.getImagePath(),
                    backAction
            );

            if (FarmerDashboard.borderPane != null) {
                com.desgin.view.farmer.ashutosh.profile.ProfileManagement.setHeaderTitle("Equipment Details ⚒", "Detailed machinery specifications, rent & operator options");
                FarmerDashboard.borderPane.setCenter(detailPage.getDetailPage());
            }
        });

        VBox cardContent = new VBox(8, nameText, categoryText, infoRow, priceText, viewButton);
        cardContent.setPadding(new Insets(15));
        cardContent.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(imageContainer, cardContent);
        card.setPrefWidth(280);
        card.setMinWidth(260);
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: rgba(45, 106, 79, 0.25);" +
                "-fx-border-width: 1.2px;" +
                "-fx-border-radius: 14px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.04), 8, 0, 0, 2);"
        );

        return card;
    }

    private Image loadImage(String imagePath) {
        try {
            return new Image(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            try {
                return new Image(imagePath, true);
            } catch (Exception ex) {
                return null;
            }
        }
    }
}