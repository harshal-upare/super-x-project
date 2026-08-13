package com.desgin.view.farmer.om;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import javafx.scene.text.Text;

public class BrowseEquip {

    public ScrollPane getBrowseEquip() {

        Text browseTitle = new Text("Browse Equipment");
        browseTitle.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;"
        );

        Text subtitle = new Text(
                "Find the right equipment for your farm"
        );
        subtitle.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-fill: #7A6658;"
        );

        VBox heading = new VBox(
                5,
                browseTitle,
                subtitle
        );

        TextField searchField = new TextField();
        searchField.setPromptText(
                "🔍  Search tractors, harvesters, cultivators..."
        );
        searchField.setPrefHeight(45);
        searchField.setMaxWidth(Double.MAX_VALUE);

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

        Button searchButton = new Button("Search");
        searchButton.setPrefWidth(90);
        searchButton.setPrefHeight(45);

        searchButton.setStyle(
                "-fx-background-color: #6B8E23;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );

        searchButton.setOnMouseEntered(e ->
                searchButton.setStyle(
                        "-fx-background-color: #55751C;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
                )
        );

        searchButton.setOnMouseExited(e ->
                searchButton.setStyle(
                        "-fx-background-color: #6B8E23;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
                )
        );

        HBox searchBox = new HBox(
                10,
                searchField,
                searchButton
        );

        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(
                searchField,
                javafx.scene.layout.Priority.ALWAYS
        );

        VBox filterSection = createFilterSection();

        Text equipmentTitle = new Text("Available Equipment");
        equipmentTitle.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;"
        );

        Text equipmentCount = new Text(
                "Browse equipment available for rent"
        );
        equipmentCount.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-fill: #7A6658;"
        );

        VBox equipmentHeading = new VBox(
                4,
                equipmentTitle,
                equipmentCount
        );

        TilePane equipmentCards = new TilePane();

        equipmentCards.setHgap(15);
        equipmentCards.setVgap(15);
        equipmentCards.setPrefColumns(2);
        equipmentCards.setAlignment(Pos.TOP_LEFT);

        VBox card1 = createEquipmentCard(
                "Mahindra 575 DI",
                "Tractor",
                "1500",
                "4.7",
                "Pune",
                "file:farm/src/main/resources/assets/Images/tractor.png"
        );

        VBox card2 = createEquipmentCard(
                "John Deere 5310",
                "Tractor",
                "1800",
                "4.8",
                "Pune",
                "file:farm/src/main/resources/assets/Images/background.jpeg"
        );

        VBox card3 = createEquipmentCard(
                "Sonalika Seeder",
                "Seeder",
                "900",
                "4.5",
                "Pune",
                "file:farm/src/main/resources/assets/Images/logo.png"
        );

        equipmentCards.getChildren().addAll(
                card1,
                card2,
                card3
        );

        VBox equipmentSection = new VBox(
                15,
                equipmentHeading,
                equipmentCards
        );

        equipmentSection.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(
                equipmentSection,
                javafx.scene.layout.Priority.ALWAYS
        );

        HBox mainContent = new HBox(
                20,
                filterSection,
                equipmentSection
        );

        mainContent.setMaxWidth(Double.MAX_VALUE);
        mainContent.setAlignment(Pos.TOP_LEFT);

        VBox root = new VBox(
                22,
                heading,
                searchBox,
                mainContent
        );

        root.setPadding(
                new Insets(25, 30, 30, 30)
        );

        root.setAlignment(Pos.TOP_LEFT);
        root.setMaxWidth(Double.MAX_VALUE);

        root.setStyle(
                "-fx-background-color: #FCF9F5;"
        );

        ScrollPane scrollPane = new ScrollPane();

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

    private VBox createEquipmentCard(
            String equipmentName,
            String category,
            String price,
            String rating,
            String location,
            String imagePath) {

        Image image = new Image(imagePath);

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(280);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        Button wishlistButton = new Button("♡");

        wishlistButton.setPrefWidth(36);
        wishlistButton.setPrefHeight(36);

        wishlistButton.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: #8B3A3A;" +
                "-fx-font-size: 20px;" +
                "-fx-cursor: hand;"
        );

        wishlistButton.setOnMouseEntered(e ->
                wishlistButton.setStyle(
                        "-fx-background-color: #F5E7E3;" +
                        "-fx-background-radius: 20;" +
                        "-fx-text-fill: #8B3A3A;" +
                        "-fx-font-size: 20px;" +
                        "-fx-cursor: hand;"
                )
        );

        wishlistButton.setOnMouseExited(e ->
                wishlistButton.setStyle(
                        "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-text-fill: #8B3A3A;" +
                        "-fx-font-size: 20px;" +
                        "-fx-cursor: hand;"
                )
        );

        StackPane imageContainer = new StackPane(
                imageView,
                wishlistButton
        );

        imageContainer.setPrefHeight(150);
        imageContainer.setPrefWidth(280);
        imageContainer.setMaxWidth(Double.MAX_VALUE);

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

        Text nameText = new Text(equipmentName);
        nameText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;"
        );

        Text categoryText = new Text(category);
        categoryText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-fill: #7A6658;"
        );

        Text ratingText = new Text(
                "⭐ " + rating
        );

        ratingText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #5C4033;"
        );

        Text locationText = new Text(
                "📍 " + location
        );

        locationText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-fill: #7A6658;"
        );

        HBox infoRow = new HBox(
                15,
                ratingText,
                locationText
        );

        infoRow.setAlignment(Pos.CENTER_LEFT);

        Text priceText = new Text(
                "₹" + price + " / day"
        );

        priceText.setStyle(
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #6B8E23;"
        );

        Button viewButton = new Button(
                "View Details"
        );

        viewButton.setPrefHeight(38);
        viewButton.setMaxWidth(Double.MAX_VALUE);

        viewButton.setStyle(
                "-fx-background-color: #4A2C20;" +
                "-fx-background-radius: 8;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );

        viewButton.setOnMouseEntered(e ->
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

        viewButton.setOnMouseExited(e ->
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

        VBox content = new VBox(
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

        content.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(
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

    private VBox createFilterSection() {

        Text filterTitle = new Text("Filters");

        filterTitle.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;" +
                "-fx-font-family: 'Poppins';"
        );

        Text categoryTitle = new Text("Category");

        categoryTitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;" +
                "-fx-font-family: 'Poppins';"
        );

        CheckBox tractor = new CheckBox("Tractor");
        CheckBox harvester = new CheckBox("Harvester");
        CheckBox seeder = new CheckBox("Seeder");
        CheckBox plough = new CheckBox("Plough");
        CheckBox cultivator = new CheckBox("Cultivator");

        styleCheckBox(tractor);
        styleCheckBox(harvester);
        styleCheckBox(seeder);
        styleCheckBox(plough);
        styleCheckBox(cultivator);

        VBox categoryBox = new VBox(
                10,
                categoryTitle,
                tractor,
                harvester,
                seeder,
                plough,
                cultivator
        );

        Text priceTitle = new Text("Price / Day");

        priceTitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;" +
                "-fx-font-family: 'Poppins';"
        );

        TextField minPrice = new TextField();
        minPrice.setPromptText("Min ₹");

        TextField maxPrice = new TextField();
        maxPrice.setPromptText("Max ₹");

        stylePriceField(minPrice);
        stylePriceField(maxPrice);

        HBox priceBox = new HBox(
                10,
                minPrice,
                maxPrice
        );

        priceBox.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                minPrice,
                javafx.scene.layout.Priority.ALWAYS
        );

        HBox.setHgrow(
                maxPrice,
                javafx.scene.layout.Priority.ALWAYS
        );

        minPrice.setMaxWidth(
                Double.MAX_VALUE
        );

        maxPrice.setMaxWidth(
                Double.MAX_VALUE
        );

        Text ratingTitle = new Text("Rating");

        ratingTitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;" +
                "-fx-font-family: 'Poppins';"
        );

        CheckBox rating4 = new CheckBox(
                "⭐ 4.0 & above"
        );

        CheckBox rating3 = new CheckBox(
                "⭐ 3.0 & above"
        );

        styleCheckBox(rating4);
        styleCheckBox(rating3);

        VBox ratingBox = new VBox(
                10,
                ratingTitle,
                rating4,
                rating3
        );

        Text availabilityTitle = new Text(
                "Availability"
        );

        availabilityTitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: #4A2C20;" +
                "-fx-font-family: 'Poppins';"
        );

        CheckBox availableOnly = new CheckBox(
                "Available only"
        );

        styleCheckBox(
                availableOnly
        );

        VBox availabilityBox = new VBox(
                10,
                availabilityTitle,
                availableOnly
        );

        Button applyButton = new Button(
                "Apply Filters"
        );

        applyButton.setMaxWidth(
                Double.MAX_VALUE
        );

        applyButton.setPrefHeight(
                42
        );

        applyButton.setStyle(
                "-fx-background-color: #6B8E23;" +
                "-fx-background-radius: 9;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );

        applyButton.setOnMouseEntered(e ->
                applyButton.setStyle(
                        "-fx-background-color: #55751C;" +
                        "-fx-background-radius: 9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
                )
        );

        applyButton.setOnMouseExited(e ->
                applyButton.setStyle(
                        "-fx-background-color: #6B8E23;" +
                        "-fx-background-radius: 9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
                )
        );

        Button resetButton = new Button(
                "Reset"
        );

        resetButton.setMaxWidth(
                Double.MAX_VALUE
        );

        resetButton.setPrefHeight(
                42
        );

        resetButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 9;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 9;" +
                "-fx-text-fill: #5C4033;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );

        resetButton.setOnMouseEntered(e ->
                resetButton.setStyle(
                        "-fx-background-color: #E4D3C2;" +
                        "-fx-background-radius: 9;" +
                        "-fx-border-color: #C7B39E;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 9;" +
                        "-fx-text-fill: #4A2C20;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
                )
        );

        resetButton.setOnMouseExited(e ->
                resetButton.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 9;" +
                        "-fx-border-color: #D8C7B5;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 9;" +
                        "-fx-text-fill: #5C4033;" +
                        "-fx-font-family: 'Poppins';" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
                )
        );

        VBox buttonBox = new VBox(
                10,
                applyButton,
                resetButton
        );

        VBox filterBox = new VBox(
                22,
                filterTitle,
                categoryBox,
                priceBoxWithTitle(
                        priceTitle,
                        priceBox
                ),
                ratingBox,
                availabilityBox,
                buttonBox
        );

        filterBox.setPadding(
                new Insets(22)
        );

        filterBox.setPrefWidth(235);
        filterBox.setMinWidth(235);
        filterBox.setMaxWidth(235);

        filterBox.setAlignment(
                Pos.TOP_LEFT
        );

        filterBox.setStyle(
                "-fx-background-color: #F5EFE6;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 14;"
        );

        return filterBox;
    }

    private void styleCheckBox(
            CheckBox checkBox) {

        checkBox.setStyle(
                "-fx-text-fill: #5C4033;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;"
        );
    }

    private void stylePriceField(
            TextField field) {

        field.setPrefHeight(38);

        field.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #D8C7B5;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 8;" +
                "-fx-padding: 0 10;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #4A2C20;"
        );
    }

    private VBox priceBoxWithTitle(
            Text priceTitle,
            HBox priceBox) {

        return new VBox(
                10,
                priceTitle,
                priceBox
        );
    }
}