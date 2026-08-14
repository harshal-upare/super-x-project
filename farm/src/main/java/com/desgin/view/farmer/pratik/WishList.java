package com.desgin.view.farmer.pratik;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import com.desgin.view.farmer.Swapnil.FarmerDashboard;
import com.desgin.view.farmer.om.EquipmentDetailPage;

public class WishList {

    // ============================================================
    // WISHLIST DATA
    // ============================================================

    private static final ObservableList<WishlistItem> wishlistItems =
            FXCollections.observableArrayList();


    // ============================================================
    // ADD TO WISHLIST
    // ============================================================

    public static void addToWishlist(
            String equipmentName,
            String category,
            String price,
            String rating,
            String location,
            String imagePath) {

        // Prevent duplicate equipment
        if (isInWishlist(equipmentName)) {
            System.out.println(
                    equipmentName + " is already in wishlist."
            );
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

        System.out.println(
                equipmentName + " added to wishlist."
        );
    }


    // ============================================================
    // REMOVE FROM WISHLIST
    // ============================================================

    public static void removeFromWishlist(
            String equipmentName) {

        wishlistItems.removeIf(
                item -> item.getEquipmentName()
                        .equals(equipmentName)
        );

        System.out.println(
                equipmentName + " removed from wishlist."
        );
    }


    // ============================================================
    // CHECK WISHLIST
    // ============================================================

    public static boolean isInWishlist(
            String equipmentName) {

        for (WishlistItem item : wishlistItems) {

            if (item.getEquipmentName()
                    .equals(equipmentName)) {

                return true;
            }
        }

        return false;
    }


    // ============================================================
    // GET WISHLIST ITEMS
    // ============================================================

    public static ObservableList<WishlistItem>
    getWishlistItems() {

        return wishlistItems;
    }


    // ============================================================
    // WISHLIST PAGE
    // ============================================================

    public static ScrollPane getWishList() {

        // --------------------------------------------------------
        // TITLE
        // --------------------------------------------------------

        Text wishlistTitle =
                new Text("My Wishlist");

        wishlistTitle.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;"
        );


        // --------------------------------------------------------
        // SUBTITLE
        // --------------------------------------------------------

        Text subtitle =
                new Text(
                        "Equipment you saved for later"
                );

        subtitle.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-fill: #7A6658;"
        );


        VBox heading =
                new VBox(
                        5,
                        wishlistTitle,
                        subtitle
                );


        // --------------------------------------------------------
        // WISHLIST COUNT
        // --------------------------------------------------------

        Text wishlistCount =
                new Text();

        wishlistCount.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #6B8E23;"
        );


        // --------------------------------------------------------
        // CARDS CONTAINER
        // --------------------------------------------------------

        TilePane wishlistCards =
                new TilePane();

        wishlistCards.setHgap(15);
        wishlistCards.setVgap(15);

        wishlistCards.setPrefColumns(2);

        wishlistCards.setAlignment(
                Pos.TOP_LEFT
        );


        // --------------------------------------------------------
        // EMPTY MESSAGE
        // --------------------------------------------------------

        Text emptyText =
                new Text(
                        "❤️  No equipment added to wishlist yet."
                );

        emptyText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 15px;" +
                "-fx-fill: #7A6658;"
        );


        VBox emptyBox =
                new VBox(emptyText);

        emptyBox.setAlignment(
                Pos.CENTER
        );

        emptyBox.setPadding(
                new Insets(50)
        );


        // --------------------------------------------------------
        // REFRESH METHOD
        // --------------------------------------------------------

        Runnable refreshWishlist = new Runnable() {

            @Override
            public void run() {

                wishlistCards
                        .getChildren()
                        .clear();


                // Update count

                wishlistCount.setText(
                        wishlistItems.size()
                                + " equipment saved"
                );


                // Empty wishlist

                if (wishlistItems.isEmpty()) {

                    emptyBox.setVisible(true);

                    emptyBox.setManaged(true);

                }

                // Wishlist contains items

                else {

                    emptyBox.setVisible(false);

                    emptyBox.setManaged(false);


                    for (WishlistItem item :
                            wishlistItems) {

                        VBox card =
                                createWishlistCard(
                                        item,
                                        this
                                );

                        wishlistCards
                                .getChildren()
                                .add(card);
                    }
                }
            }
        };


        // --------------------------------------------------------
        // INITIAL DISPLAY
        // --------------------------------------------------------

        refreshWishlist.run();


        // --------------------------------------------------------
        // MAIN CONTENT
        // --------------------------------------------------------

        VBox content =
                new VBox(
                        18,
                        heading,
                        wishlistCount,
                        emptyBox,
                        wishlistCards
                );


        content.setPadding(
                new Insets(
                        25,
                        30,
                        30,
                        30
                )
        );

        content.setAlignment(
                Pos.TOP_LEFT
        );

        content.setMaxWidth(
                Double.MAX_VALUE
        );

        content.setStyle(
                "-fx-background-color: #FCF9F5;"
        );


        // --------------------------------------------------------
        // SCROLL PANE
        // --------------------------------------------------------

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                content
        );

        scrollPane.setFitToWidth(true);

        scrollPane.setFitToHeight(false);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;"
        );


        return scrollPane;
    }


    // ============================================================
    // CREATE WISHLIST CARD
    // ============================================================

    private static VBox createWishlistCard(
            WishlistItem item,
            Runnable refreshWishlist) {


        // --------------------------------------------------------
        // IMAGE
        // --------------------------------------------------------
        WishList obj = new WishList();
        Image image =
                obj.loadImage(
                        item.getImagePath()
                );


        ImageView imageView =
                new ImageView(image);

        imageView.setFitWidth(280);

        imageView.setFitHeight(150);

        imageView.setPreserveRatio(false);

        imageView.setSmooth(true);


        // --------------------------------------------------------
        // REMOVE BUTTON
        // --------------------------------------------------------

        Button removeButton =
                new Button("♥");

        removeButton.setPadding(Insets.EMPTY);

        removeButton.setMinSize(36, 36);

        removeButton.setPrefSize(36, 36);

        removeButton.setMaxSize(36, 36);

        removeButton.setStyle(
                "-fx-background-color: #FFEBEE;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: #E53935;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 0;" +
                "-fx-alignment: center;" +
                "-fx-cursor: hand;"
        );


        // Hover

        removeButton.setOnMouseEntered(
                event ->
                        removeButton.setStyle(
                                "-fx-background-color: #FFCDD2;" +
                                "-fx-background-radius: 20;" +
                                "-fx-text-fill: #D32F2F;" +
                                "-fx-font-size: 18px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 0;" +
                                "-fx-alignment: center;" +
                                "-fx-cursor: hand;"
                        )
        );


        removeButton.setOnMouseExited(
                event ->
                        removeButton.setStyle(
                                "-fx-background-color: #FFEBEE;" +
                                "-fx-background-radius: 20;" +
                                "-fx-text-fill: #E53935;" +
                                "-fx-font-size: 18px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 0;" +
                                "-fx-alignment: center;" +
                                "-fx-cursor: hand;"
                        )
        );


        // --------------------------------------------------------
        // REMOVE ACTION
        // --------------------------------------------------------

        removeButton.setOnAction(
                event -> {

                    removeFromWishlist(
                            item.getEquipmentName()
                    );

                    refreshWishlist.run();
                }
        );


        // --------------------------------------------------------
        // IMAGE CONTAINER
        // --------------------------------------------------------

        StackPane imageContainer =
                new StackPane(
                        imageView,
                        removeButton
                );

        imageContainer.setPrefWidth(280);

        imageContainer.setPrefHeight(150);

        imageContainer.setStyle(
                "-fx-background-color: #E8DED2;" +
                "-fx-background-radius: 12 12 0 0;"
        );


        StackPane.setAlignment(
                removeButton,
                Pos.TOP_RIGHT
        );


        StackPane.setMargin(
                removeButton,
                new Insets(10)
        );


        // --------------------------------------------------------
        // NAME
        // --------------------------------------------------------

        Text nameText =
                new Text(
                        item.getEquipmentName()
                );

        nameText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;"
        );


        // --------------------------------------------------------
        // CATEGORY
        // --------------------------------------------------------

        Text categoryText =
                new Text(
                        item.getCategory()
                );

        categoryText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-fill: #7A6658;"
        );


        // --------------------------------------------------------
        // RATING
        // --------------------------------------------------------

        Text ratingText =
                new Text(
                        "⭐ " +
                        item.getRating()
                );

        ratingText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #5C4033;"
        );


        // --------------------------------------------------------
        // LOCATION
        // --------------------------------------------------------

        Text locationText =
                new Text(
                        "📍 " +
                        item.getLocation()
                );

        locationText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-fill: #7A6658;"
        );


        // --------------------------------------------------------
        // INFO ROW
        // --------------------------------------------------------

        HBox infoRow =
                new HBox(
                        15,
                        ratingText,
                        locationText
                );

        infoRow.setAlignment(
                Pos.CENTER_LEFT
        );


        // --------------------------------------------------------
        // PRICE
        // --------------------------------------------------------

        Text priceText =
                new Text(
                        "₹" +
                        item.getPrice() +
                        " / day"
                );

        priceText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #6B8E23;"
        );


        // --------------------------------------------------------
        // VIEW DETAILS
        // --------------------------------------------------------

        Button viewButton =
                new Button(
                        "View Details"
                );

        viewButton.setPrefHeight(38);

        viewButton.setMaxWidth(
                Double.MAX_VALUE
        );

        viewButton.setStyle(
                "-fx-background-color: #4A2C20;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );


        // Hover

        viewButton.setOnMouseEntered(
                event ->
                        viewButton.setStyle(
                                "-fx-background-color: #6B4A3A;" +
                                "-fx-background-radius: 8;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-family: 'Poppins';" +
                                "-fx-font-size: 12px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-cursor: hand;"
                        )
        );


        viewButton.setOnMouseExited(
                event ->
                        viewButton.setStyle(
                                "-fx-background-color: #4A2C20;" +
                                "-fx-background-radius: 8;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-family: 'Poppins';" +
                                "-fx-font-size: 12px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-cursor: hand;"
                        )
        );


        viewButton.setOnAction(
                event -> {
                    Runnable backAction = () -> {
                        if (FarmerDashboard.borderPane != null) {
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
                        FarmerDashboard.borderPane.setCenter(detailPage.getDetailPage());
                    }
                }
        );


        // --------------------------------------------------------
        // CARD CONTENT
        // --------------------------------------------------------

        VBox cardContent =
                new VBox(
                        8,
                        nameText,
                        categoryText,
                        infoRow,
                        priceText,
                        viewButton
                );

        cardContent.setPadding(
                new Insets(15)
        );

        cardContent.setMaxWidth(
                Double.MAX_VALUE
        );


        // --------------------------------------------------------
        // COMPLETE CARD
        // --------------------------------------------------------

        VBox card =
                new VBox(
                        imageContainer,
                        cardContent
                );

        card.setPrefWidth(280);

        card.setMinWidth(260);

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E0D4C7;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 12;"
        );


        return card;
    }


    // ============================================================
    // LOAD IMAGE FROM MAVEN RESOURCES
    // ============================================================

    private  Image loadImage(
            String imagePath) {

        try {

            /*
             * Example:
             *
             * /assets/Images/tractor.png
             */

            if (imagePath == null ||
                    imagePath.isEmpty()) {

                return createEmptyImage();
            }


            /*
             * If BrowseEquip sends:
             *
             * file:farm/src/main/resources/...
             *
             * convert it to resource path.
             */

            if (imagePath.startsWith("file:")) {

                int resourceIndex =
                        imagePath.indexOf(
                                "/assets/"
                        );

                if (resourceIndex >= 0) {

                    imagePath =
                            imagePath.substring(
                                    resourceIndex
                            );
                }
            }


            /*
             * Make sure path starts with /
             */

            if (!imagePath.startsWith("/")) {

                int resourceIndex =
                        imagePath.indexOf(
                                "/assets/"
                        );

                if (resourceIndex >= 0) {

                    imagePath =
                            imagePath.substring(
                                    resourceIndex
                            );
                } else {

                    imagePath =
                            "/" + imagePath;
                }
            }


            /*
             * Load Maven resource
             */

            var resource =
                    getClass().getResource(
                            imagePath
                    );


            if (resource != null) {

                return new Image(
                        resource.toExternalForm()
                );
            }


            System.out.println(
                    "Image not found: " +
                    imagePath
            );


        } catch (Exception e) {

            e.printStackTrace();
        }


        return createEmptyImage();
    }


    // ============================================================
    // EMPTY IMAGE
    // ============================================================

    private static Image createEmptyImage() {

        return new Image(
                "data:image/png;base64," +
                "iVBORw0KGgoAAAANSUhEUgAAAAEAAAAB" +
                "CAQAAAC1HAwCAAAAC0lEQVR42mNk+A8A" +
                "AQAFAgJp4AAAAABJRU5ErkJggg=="
        );
    }


    // ============================================================
    // WISHLIST ITEM MODEL
    // ============================================================

    public static class WishlistItem {

        private final String equipmentName;

        private final String category;

        private final String price;

        private final String rating;

        private final String location;

        private final String imagePath;


        public WishlistItem(
                String equipmentName,
                String category,
                String price,
                String rating,
                String location,
                String imagePath) {

            this.equipmentName =
                    equipmentName;

            this.category =
                    category;

            this.price =
                    price;

            this.rating =
                    rating;

            this.location =
                    location;

            this.imagePath =
                    imagePath;
        }


        public String getEquipmentName() {

            return equipmentName;
        }


        public String getCategory() {

            return category;
        }


        public String getPrice() {

            return price;
        }


        public String getRating() {

            return rating;
        }


        public String getLocation() {

            return location;
        }


        public String getImagePath() {

            return imagePath;
        }
    }
}