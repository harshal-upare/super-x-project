package com.desgin.view.farmer.om;

import com.desgin.view.farmer.Swapnil.FarmerDashboard;
import com.desgin.view.farmer.pratik.WishList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class BrowseEquip {

   
    /*
     * Equipment Model
     */
    private static class Equipment {

        String name;
        String category;
        int price;
        String rating;
        String location;
        String imagePath;

        Equipment(
                String name,
                String category,
                int price,
                String rating,
                String location,
                String imagePath) {

            this.name = name;
            this.category = category;
            this.price = price;
            this.rating = rating;
            this.location = location;
            this.imagePath = imagePath;
        }
    }

    /*
     * All equipment
     */
    private final static List<Equipment> allEquipment =
            new ArrayList<>();

    /*
     * Card container
     */
    private static TilePane equipmentCards;

    /*
     * Search field
     */
    private static TextField searchField;

    /*
     * Category filter
     */
    private static  ComboBox<String> filterEqSection;

    /*
     * Minimum price
     */
    private static ComboBox<String> filterPrSection1;

    /*
     * Maximum price
     */
    private static ComboBox<String> filterPrSection2;

    /*
     * Equipment count
     */
    private static Text equipmentCount;

    /*
     * Empty result message
     */
    private static Text noResultText;


    /*
     * Constructor
     */
    public BrowseEquip() {

        loadEquipment();
    }


    /*
     * Load equipment
     */
    private void loadEquipment() {

        allEquipment.clear();

        allEquipment.add(
                new Equipment(
                        "Mahindra 575 DI",
                        "Tractor",
                        1500,
                        "4.7",
                        "Pune",
                        "file:farm/src/main/resources/assets/Images/tractor.png"
                )
        );

        allEquipment.add(
                new Equipment(
                        "John Deere 5310",
                        "Tractor",
                        1800,
                        "4.8",
                        "Pune",
                        "file:farm/src/main/resources/assets/Images/background.jpeg"
                )
        );

        allEquipment.add(
                new Equipment(
                        "Sonalika Seeder",
                        "Seeder",
                        900,
                        "4.5",
                        "Pune",
                        "file:farm/src/main/resources/assets/Images/logo.png"
                )
        );

        allEquipment.add(
                new Equipment(
                        "Sonalika Plough",
                        "Plough",
                        700,
                        "4.4",
                        "Pune",
                        "file:farm/src/main/resources/assets/Images/logo.png"
                )
        );

        allEquipment.add(
                new Equipment(
                        "Mahindra Cultivator",
                        "Cultivator",
                        800,
                        "4.6",
                        "Pune",
                        "file:farm/src/main/resources/assets/Images/logo.png"
                )
        );

        allEquipment.add(
                new Equipment(
                        "New Holland Harvester",
                        "Harvester",
                        1900,
                        "4.9",
                        "Pune",
                        "file:farm/src/main/resources/assets/Images/background.jpeg"
                )
        );

        allEquipment.add(
                new Equipment(
                        "Swaraj Tractor",
                        "Tractor",
                        1200,
                        "4.5",
                        "Pune",
                        "file:farm/src/main/resources/assets/Images/tractor.png"
                )
        );
    }


    /*
     * Main Browse Equipment Page
     */
    public static ScrollPane getBrowseEquip() {

        /*
         * --------------------------------
         * TITLE
         * --------------------------------
         */

        Text browseTitle =
                new Text("Browse Equipment");

        browseTitle.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;"
        );


        Text subtitle =
                new Text(
                        "Find the right equipment for your farm"
                );

        subtitle.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-fill: #7A6658;"
        );


        VBox heading =
                new VBox(
                        5,
                        browseTitle,
                        subtitle
                );


        /*
         * --------------------------------
         * SEARCH FIELD
         * --------------------------------
         */

        searchField =
                new TextField();

        searchField.setPromptText(
                "🔍  Search tractors, harvesters, cultivators..."
        );

        searchField.setPrefHeight(45);

        searchField.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-padding: 0 15;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #4A2C20;"
        );


        /*
         * Search Button
         */

        Button searchButton =
                new Button("Search");

        searchButton.setPrefWidth(90);
        searchButton.setPrefHeight(45);

        setGreenButtonStyle(searchButton);


        /*
         * SEARCH FUNCTION
         */

        searchButton.setOnAction(
                e -> applyFilters()
        );


        /*
         * Search when ENTER pressed
         */

        searchField.setOnAction(
                e -> applyFilters()
        );


        /*
         * --------------------------------
         * CATEGORY FILTER
         * --------------------------------
         */

        filterEqSection =
                new ComboBox<>();

        filterEqSection.getItems().addAll(
                "All",
                "Tractor",
                "Harvester",
                "Seeder",
                "Plough",
                "Cultivator"
        );

        filterEqSection.setValue("All");

        filterEqSection.setPrefWidth(230);
        filterEqSection.setPrefHeight(45);

        setComboStyle(filterEqSection);


        /*
         * --------------------------------
         * MIN PRICE
         * --------------------------------
         */

        filterPrSection1 =
                new ComboBox<>();

        filterPrSection1.getItems().addAll(
                "No Min",
                "500",
                "600",
                "700",
                "800",
                "900",
                "1000",
                "1200",
                "1500"
        );

        filterPrSection1.setValue("No Min");

        filterPrSection1.setPrefWidth(150);
        filterPrSection1.setPrefHeight(45);

        setComboStyle(filterPrSection1);


        /*
         * TO TEXT
         */

        Text toText =
                new Text("TO");

        toText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;"
        );


        /*
         * --------------------------------
         * MAX PRICE
         * --------------------------------
         */

        filterPrSection2 =
                new ComboBox<>();

        filterPrSection2.getItems().addAll(
                "No Max",
                "1000",
                "1200",
                "1400",
                "1500",
                "1800",
                "1900",
                "2000"
        );

        filterPrSection2.setValue("No Max");

        filterPrSection2.setPrefWidth(150);
        filterPrSection2.setPrefHeight(45);

        setComboStyle(filterPrSection2);


        /*
         * --------------------------------
         * APPLY BUTTON
         * --------------------------------
         */

        Button applyFilterButton =
                new Button("Apply");

        applyFilterButton.setPrefWidth(90);
        applyFilterButton.setPrefHeight(45);

        setGreenButtonStyle(
                applyFilterButton
        );

        applyFilterButton.setOnAction(
                e -> applyFilters()
        );


        /*
         * --------------------------------
         * RESET BUTTON
         * --------------------------------
         */

        Button resetButton =
                new Button("Reset");

        resetButton.setPrefWidth(90);
        resetButton.setPrefHeight(45);

        resetButton.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-radius: 10;" +
                "-fx-text-fill: #4A2C20;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );

        resetButton.setOnAction(
                e -> resetFilters()
        );


        /*
         * --------------------------------
         * SEARCH BOX
         * --------------------------------
         */

        HBox searchBox =
                new HBox(
                        10,
                        searchField,
                        searchButton
                );

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                searchField,
                javafx.scene.layout.Priority.ALWAYS
        );


        /*
         * --------------------------------
         * FILTER BOX
         * --------------------------------
         */

        HBox filterBox =
                new HBox(
                        10,
                        filterEqSection,
                        filterPrSection1,
                        toText,
                        filterPrSection2,
                        applyFilterButton,
                        resetButton
                );

        filterBox.setAlignment(
                Pos.CENTER_LEFT
        );


        /*
         * --------------------------------
         * EQUIPMENT TITLE
         * --------------------------------
         */

        Text equipmentTitle =
                new Text(
                        "Available Equipment"
                );

        equipmentTitle.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;"
        );


        /*
         * --------------------------------
         * EQUIPMENT COUNT
         * --------------------------------
         */

        equipmentCount =
                new Text();

        equipmentCount.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-fill: #7A6658;"
        );


        /*
         * --------------------------------
         * NO RESULT
         * --------------------------------
         */

        noResultText =
                new Text(
                        "No equipment found."
                );

        noResultText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 16px;" +
                "-fx-fill: #7A6658;"
        );

        noResultText.setVisible(false);
        noResultText.setManaged(false);


        /*
         * --------------------------------
         * EQUIPMENT CARDS
         * --------------------------------
         */

        equipmentCards =
                new TilePane();

        equipmentCards.setHgap(15);
        equipmentCards.setVgap(15);

        equipmentCards.setPrefColumns(2);

        equipmentCards.setAlignment(
                Pos.TOP_LEFT
        );


        /*
         * --------------------------------
         * LOAD ALL CARDS
         * --------------------------------
         */

        refreshCards(allEquipment);


        /*
         * --------------------------------
         * CONTENT
         * --------------------------------
         */

        VBox root =
                new VBox(
                        18,
                        heading,
                        searchBox,
                        filterBox,
                        equipmentTitle,
                        equipmentCount,
                        noResultText,
                        equipmentCards
                );


        root.setPadding(
                new Insets(
                        25,
                        30,
                        30,
                        30
                )
        );


        root.setAlignment(
                Pos.TOP_LEFT
        );


        root.setMaxWidth(
                Double.MAX_VALUE
        );


        root.setStyle(
                "-fx-background-color: #FCF9F5;"
        );


        /*
         * --------------------------------
         * SCROLL PANE
         * --------------------------------
         */

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(root);

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


    /*
     * ================================================
     * APPLY SEARCH + FILTER
     * ================================================
     */

    private static void applyFilters() {

        String search =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();


        String category =
                filterEqSection.getValue();


        String minPriceText =
                filterPrSection1.getValue();


        String maxPriceText =
                filterPrSection2.getValue();


        int minPrice = 0;

        int maxPrice = Integer.MAX_VALUE;


        /*
         * Minimum price
         */

        if (minPriceText != null
                && !minPriceText.equals("No Min")) {

            try {

                minPrice =
                        Integer.parseInt(
                                minPriceText
                        );

            } catch (NumberFormatException ex) {

                minPrice = 0;
            }
        }


        /*
         * Maximum price
         */

        if (maxPriceText != null
                && !maxPriceText.equals("No Max")) {

            try {

                maxPrice =
                        Integer.parseInt(
                                maxPriceText
                        );

            } catch (NumberFormatException ex) {

                maxPrice = Integer.MAX_VALUE;
            }
        }


        /*
         * Filter equipment
         */

        List<Equipment> filtered =
                new ArrayList<>();


        for (Equipment equipment :
                allEquipment) {


            /*
             * SEARCH
             */

            boolean matchesSearch =
                    search.isEmpty()
                    ||
                    equipment.name
                            .toLowerCase()
                            .contains(search)
                    ||
                    equipment.category
                            .toLowerCase()
                            .contains(search)
                    ||
                    equipment.location
                            .toLowerCase()
                            .contains(search);


            /*
             * CATEGORY
             */

            boolean matchesCategory =
                    category == null
                    ||
                    category.equals("All")
                    ||
                    equipment.category
                            .equalsIgnoreCase(category);


            /*
             * PRICE
             */

            boolean matchesPrice =
                    equipment.price >= minPrice
                    &&
                    equipment.price <= maxPrice;


            /*
             * FINAL RESULT
             */

            if (matchesSearch
                    && matchesCategory
                    && matchesPrice) {

                filtered.add(equipment);
            }
        }


        /*
         * Display filtered equipment
         */

        refreshCards(filtered);
    }


    /*
     * ================================================
     * RESET FILTER
     * ================================================
     */

    private static void resetFilters() {

        searchField.clear();

        filterEqSection.setValue("All");

        filterPrSection1.setValue("No Min");

        filterPrSection2.setValue("No Max");


        refreshCards(
                allEquipment
        );
    }


    /*
     * ================================================
     * REFRESH CARDS
     * ================================================
     */

    private static void refreshCards(
            List<Equipment> equipmentList) {

        equipmentCards
                .getChildren()
                .clear();


        /*
         * Update count
         */

        equipmentCount.setText(
                equipmentList.size()
                        + " equipment available"
        );


        /*
         * No result
         */

        if (equipmentList.isEmpty()) {

            noResultText.setVisible(true);

            noResultText.setManaged(true);

            return;

        } else {

            noResultText.setVisible(false);

            noResultText.setManaged(false);
        }


        /*
         * Create cards
         */

        for (Equipment equipment :
                equipmentList) {

            VBox card =
                    createEquipmentCard(
                            equipment
                    );

            equipmentCards
                    .getChildren()
                    .add(card);
        }
    }


    /*
     * ================================================
     * CREATE EQUIPMENT CARD
     * ================================================
     */

    private static VBox createEquipmentCard(
            Equipment equipment) {


        /*
         * IMAGE
         */

        Image image;

        try {

            image =
                    new Image(
                            equipment.imagePath,
                            true
                    );

        } catch (Exception e) {

            image = null;
        }


        ImageView imageView =
                new ImageView();


        if (image != null) {

            imageView.setImage(image);
        }


        imageView.setFitWidth(280);

        imageView.setFitHeight(150);

        imageView.setPreserveRatio(false);

        imageView.setSmooth(true);


        /*
         * =================================
         * WISHLIST BUTTON
         * =================================
         */

        Button wishlistButton =
                new Button();


        updateWishlistButton(
                wishlistButton,
                equipment.name
        );


        wishlistButton.setPadding(Insets.EMPTY);

        wishlistButton.setMinSize(36, 36);

        wishlistButton.setPrefSize(36, 36);

        wishlistButton.setMaxSize(36, 36);


        /*
         * WISHLIST ACTION
         */

        wishlistButton.setOnAction(
                e -> {

                    /*
                     * Already in wishlist
                     */

                    if (WishList.isInWishlist(
                            equipment.name)) {


                        /*
                         * Remove
                         */

                        WishList.removeFromWishlist(
                                equipment.name
                        );


                        updateWishlistButton(
                                wishlistButton,
                                equipment.name
                        );


                        System.out.println(
                                equipment.name
                                        + " removed from wishlist"
                        );


                    } else {


                        /*
                         * Add
                         */

                        WishList.addToWishlist(
                                equipment.name,
                                equipment.category,
                                String.valueOf(
                                        equipment.price
                                ),
                                equipment.rating,
                                equipment.location,
                                equipment.imagePath
                        );


                        updateWishlistButton(
                                wishlistButton,
                                equipment.name
                        );


                        System.out.println(
                                equipment.name
                                        + " added to wishlist"
                        );
                    }
                }
        );


        /*
         * IMAGE CONTAINER
         */

        StackPane imageContainer =
                new StackPane(
                        imageView,
                        wishlistButton
                );


        imageContainer.setPrefWidth(280);

        imageContainer.setPrefHeight(150);


        imageContainer.setStyle(
                "-fx-background-color: #E8DED2;" +
                "-fx-background-radius: 12 12 0 0;"
        );


        StackPane.setAlignment(
                wishlistButton,
                Pos.TOP_RIGHT
        );


        StackPane.setMargin(
                wishlistButton,
                new Insets(10)
        );


        /*
         * =================================
         * NAME
         * =================================
         */

        Text nameText =
                new Text(
                        equipment.name
                );

        nameText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;"
        );


        /*
         * CATEGORY
         */

        Text categoryText =
                new Text(
                        equipment.category
                );

        categoryText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-fill: #7A6658;"
        );


        /*
         * RATING
         */

        Text ratingText =
                new Text(
                        "⭐ "
                                + equipment.rating
                );

        ratingText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #5C4033;"
        );


        /*
         * LOCATION
         */

        Text locationText =
                new Text(
                        "📍 "
                                + equipment.location
                );

        locationText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-fill: #7A6658;"
        );


        /*
         * INFO ROW
         */

        HBox infoRow =
                new HBox(
                        15,
                        ratingText,
                        locationText
                );

        infoRow.setAlignment(
                Pos.CENTER_LEFT
        );


        /*
         * PRICE
         */

        Text priceText =
                new Text(
                        "₹"
                                + equipment.price
                                + " / day"
                );

        priceText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #6B8E23;"
        );


        /*
         * =================================
         * VIEW DETAILS
         * =================================
         */

        Button viewButton =
                new Button(
                        "View Details"
                );


        viewButton.setPrefHeight(38);

        viewButton.setMaxWidth(
                Double.MAX_VALUE
        );


        setViewButtonStyle(
                viewButton
        );


        viewButton.setOnAction(
                e -> {
                    Runnable backAction = () -> {
                        if (FarmerDashboard.borderPane != null) {
                            FarmerDashboard.borderPane.setCenter(getBrowseEquip());
                        }
                    };

                    EquipmentDetailPage detailPage = new EquipmentDetailPage(
                            equipment.name,
                            equipment.category,
                            String.valueOf(equipment.price),
                            equipment.rating,
                            equipment.location,
                            equipment.imagePath,
                            backAction
                    );

                    if (FarmerDashboard.borderPane != null) {
                        FarmerDashboard.borderPane.setCenter(detailPage.getDetailPage());
                    }
                }
        );


        /*
         * =================================
         * CARD CONTENT
         * =================================
         */

        VBox content =
                new VBox(
                        8,
                        nameText,
                        categoryText,
                        infoRow,
                        priceText,
                        viewButton
                );


        content.setPadding(
                new Insets(15)
        );


        content.setMaxWidth(
                Double.MAX_VALUE
        );


        /*
         * =================================
         * COMPLETE CARD
         * =================================
         */

        VBox card =
                new VBox(
                        imageContainer,
                        content
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


    /*
     * ================================================
     * UPDATE WISHLIST BUTTON
     * ================================================
     */

    private static void updateWishlistButton(
            Button button,
            String equipmentName) {

        button.setPadding(Insets.EMPTY);
        button.setMinSize(36, 36);
        button.setPrefSize(36, 36);
        button.setMaxSize(36, 36);

        if (WishList.isInWishlist(
                equipmentName)) {


            /*
             * Equipment already saved
             */

            button.setText("♥");

            button.setStyle(
                    "-fx-background-color: #FFEBEE;" +
                    "-fx-background-radius: 20;" +
                    "-fx-text-fill: #E53935;" +
                    "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 0;" +
                    "-fx-alignment: center;" +
                    "-fx-cursor: hand;"
            );


        } else {


            /*
             * Equipment not saved
             */

            button.setText("♡");

            button.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-background-radius: 20;" +
                    "-fx-text-fill: #555555;" +
                    "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 0;" +
                    "-fx-alignment: center;" +
                    "-fx-cursor: hand;"
            );
        }
    }


    /*
     * ================================================
     * GREEN BUTTON STYLE
     * ================================================
     */

    private static void setGreenButtonStyle(
            Button button) {

        button.setStyle(
                "-fx-background-color: #6B8E23;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );


        button.setOnMouseEntered(
                e -> button.setStyle(
                        "-fx-background-color: #55751C;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
                )
        );


        button.setOnMouseExited(
                e -> button.setStyle(
                        "-fx-background-color: #6B8E23;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
                )
        );
    }


    /*
     * ================================================
     * COMBO BOX STYLE
     * ================================================
     */

    private static void setComboStyle(
            ComboBox<String> comboBox) {

        comboBox.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-padding: 5px;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #4A2C20;"
        );
    }


    /*
     * ================================================
     * VIEW BUTTON STYLE
     * ================================================
     */

    private static void setViewButtonStyle(
            Button button) {

        button.setStyle(
                "-fx-background-color: #4A2C20;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );


        button.setOnMouseEntered(
                e -> button.setStyle(
                        "-fx-background-color: #6B4A3A;" +
                        "-fx-background-radius: 8;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
                )
        );


        button.setOnMouseExited(
                e -> button.setStyle(
                        "-fx-background-color: #4A2C20;" +
                        "-fx-background-radius: 8;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
                )
        );
    }
}