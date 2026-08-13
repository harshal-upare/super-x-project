package com.desgin.view.Provider;

import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class ProviderDashboard {
    
    private Scene providerDashboardScene;

    public Scene getproviderDashboardScene() {

        
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(15));
        
        Image logoImage = new Image("file:farm/src/main/resources/assets/Images/logo.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(48);
        logoImageView.setFitHeight(48);
        logoImageView.setPreserveRatio(true);
        logoImageView.setSmooth(true);

        
        Text textName = new Text("FarmEquip");
        textName.setStyle(
            "-fx-font-size: 30px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Poppins';" +
            "-fx-fill: #4A2C20;"
        );

        Button dashboardBtn1 = new Button("⌂  Dashboard");
        HBox btnBox1 = new HBox(5,dashboardBtn1);
        styleMenuButton(dashboardBtn1);
        
        Button equipmentBtn1 = new Button("⚒  Add Equipment");
        HBox btnBox2 = new HBox(5,equipmentBtn1);
        styleMenuButton(equipmentBtn1);

        equipmentBtn1.setOnAction(event -> {

    // =====================================================
    // DIALOG
    // =====================================================

    Dialog<ButtonType> dialog = new Dialog<>();

    dialog.setTitle("Add Equipment");
    dialog.setHeaderText(null);

    dialog.getDialogPane().setStyle("-fx-background-color: #F8F4EE;");

    // =====================================================
    // TITLE
    // =====================================================

    Text title = new Text("Add New Agricultural Equipment");

    title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-font-family: Poppins; -fx-fill: #4A2C20;");

    Text subTitle = new Text("Provide equipment details for farmers to rent.");

    subTitle.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-family: Poppins;" +
            "-fx-fill: #7A6254;"
    );

    VBox headingBox = new VBox(
            5,
            title,
            subTitle
    );

    
    // EQUIPMENT NAME
  

    Label nameLabel = new Label("Equipment Name");

    nameLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #4A2C20;"
    );

    TextField equipmentName = new TextField();

    equipmentName.setPromptText(
            "Enter equipment name"
    );

    equipmentName.setPrefHeight(42);

    equipmentName.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #D8C7B5;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 0 12 0 12;" +
            "-fx-font-size: 14px;"
    );

    // =====================================================
    // TYPE
    // =====================================================

    Label typeLabel = new Label("Equipment Type");

    typeLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #4A2C20;"
    );

    ComboBox<String> equipmentType = new ComboBox<>();

    equipmentType.getItems().addAll(
            "Tractor",
            "Harvester",
            "Rotavator",
            "Cultivator",
            "Plough",
            "Seed Drill",
            "Other"
    );

    equipmentType.setPromptText(
            "Select equipment type"
    );

    equipmentType.setPrefHeight(42);
    equipmentType.setMaxWidth(Double.MAX_VALUE);

    // =====================================================
    // LOCATION
    // =====================================================

    Label locationLabel =  new Label("Location");

    locationLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #4A2C20;"
    );

    TextField location = new TextField();

    location.setPromptText(
            "Enter equipment location"
    );

    location.setPrefHeight(42);

    location.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #D8C7B5;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 0 12 0 12;" +
            "-fx-font-size: 14px;"
    );

    // =====================================================
    // PRICE
    // =====================================================

    Label priceLabel = new Label("Price Per Day");

    priceLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #4A2C20;"
    );

    TextField price = new TextField();

    price.setPromptText(
            "Enter price per day"
    );

    price.setPrefHeight(42);

    price.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #D8C7B5;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 0 12 0 12;" +
            "-fx-font-size: 14px;"
    );

    // =====================================================
    // IMAGE PREVIEW
    // =====================================================

    Label imageLabel = new Label("Equipment Image");

    imageLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #4A2C20;"
    );

    VBox imagePreview = new VBox();

    imagePreview.setPrefSize(
            220,
            150
    );

    imagePreview.setAlignment(
            Pos.CENTER
    );

    imagePreview.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #D8C7B5;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;"
    );

    Label imagePlaceholder =
            new Label(
                    "No image selected"
            );

    imagePlaceholder.setStyle(
            "-fx-text-fill: #9A887B;" +
            "-fx-font-size: 13px;"
    );

    imagePreview.getChildren().add(
            imagePlaceholder
    );

    // =====================================================
    // CHOOSE IMAGE BUTTON
    // =====================================================

    Button chooseImage =
            new Button(
                    "📁  Choose Image"
            );

    chooseImage.setPrefHeight(40);
    chooseImage.setPrefWidth(180);

    chooseImage.setStyle(
            "-fx-background-color: #E4D3C2;" +
            "-fx-text-fill: #4A2C20;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;"
    );

    final File[] selectedImage =
            new File[1];

    chooseImage.setOnAction(imageEvent -> {

        FileChooser fileChooser =
                new FileChooser();

        fileChooser.setTitle(
                "Select Equipment Image"
        );

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files",
                        "*.png",
                        "*.jpg",
                        "*.jpeg",
                        "*.webp"
                )
        );

        File file =
                fileChooser.showOpenDialog(
                        dialog.getOwner()
                );

        if (file != null) {

            selectedImage[0] = file;

            Image selected =
                    new Image(
                            file.toURI().toString()
                    );

            ImageView preview =
                    new ImageView(selected);

            preview.setFitWidth(190);
            preview.setFitHeight(125);
            preview.setPreserveRatio(true);

            imagePreview.getChildren().clear();

            imagePreview.getChildren().add(
                    preview
            );
        }
    });

    // =====================================================
    // IMAGE SECTION
    // =====================================================

    VBox imageBox =
            new VBox(
                    10,
                    imagePreview,
                    chooseImage
            );

    imageBox.setAlignment(
            Pos.CENTER
    );

    // =====================================================
    // FORM LAYOUT
    // =====================================================

    VBox form =
            new VBox(
                    7,
                    nameLabel,
                    equipmentName,

                    typeLabel,
                    equipmentType,

                    locationLabel,
                    location,

                    priceLabel,
                    price,

                    imageLabel,
                    imageBox
            );

    form.setFillWidth(true);

    // =====================================================
    // MAIN CONTENT
    // =====================================================

    VBox content =
            new VBox(
                    20,
                    headingBox,
                    new Separator(),
                    form
            );

    content.setPadding(
            new Insets(25)
    );

    content.setPrefWidth(500);

    // =====================================================
    // BUTTONS
    // =====================================================

    ButtonType addButton =
            new ButtonType(
                    "Add Equipment",
                    ButtonBar.ButtonData.OK_DONE
            );

    dialog.getDialogPane()
            .getButtonTypes()
            .addAll(
                    addButton,
                    ButtonType.CANCEL
            );

    dialog.getDialogPane()
            .setContent(content);

    // =====================================================
    // BUTTON STYLE
    // =====================================================

    Button addButtonNode =
            (Button) dialog.getDialogPane()
                    .lookupButton(addButton);

    addButtonNode.setStyle(
            "-fx-background-color: #4A2C20;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;"
    );

    Button cancelButton =
            (Button) dialog.getDialogPane()
                    .lookupButton(
                            ButtonType.CANCEL
                    );

    cancelButton.setStyle(
            "-fx-background-color: #E4D3C2;" +
            "-fx-text-fill: #4A2C20;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;"
    );

    // =====================================================
    // VALIDATION
    // =====================================================

    addButtonNode.addEventFilter(
            javafx.event.ActionEvent.ACTION,
            addEvent -> {

                if (
                        equipmentName.getText().isBlank()
                        ||
                        equipmentType.getValue() == null
                        ||
                        location.getText().isBlank()
                        ||
                        price.getText().isBlank()
                ) {

                    Alert alert =
                            new Alert(
                                    Alert.AlertType.WARNING
                            );

                    alert.setTitle(
                            "Incomplete Form"
                    );

                    alert.setHeaderText(
                            "Please fill all required fields"
                    );

                    alert.setContentText(
                            "Equipment name, type, location and price are required."
                    );

                    alert.showAndWait();

                    addEvent.consume();

                    return;
                }

                if (
                        selectedImage[0] == null
                ) {

                    Alert alert =
                            new Alert(
                                    Alert.AlertType.WARNING
                            );

                    alert.setTitle(
                            "Equipment Image Required"
                    );

                    alert.setHeaderText(
                            "Please select an equipment image"
                    );

                    alert.setContentText(
                            "An image helps farmers identify the equipment."
                    );

                    alert.showAndWait();

                    addEvent.consume();
                }
            }
    );

    // =====================================================
    // SHOW
    // =====================================================

    dialog.showAndWait();

});
        
        Button bookingBtn1 = new Button("📅  Bookings");
        HBox btnBox3 = new HBox(5,bookingBtn1);
        styleMenuButton(bookingBtn1);
        
        Button rentBtn1 = new Button(" 🏦 Rental History");
        HBox btnBox4 = new HBox(5,rentBtn1);
        styleMenuButton(rentBtn1);
        
        Button notificationBtn1 = new Button("🔔  Notification");
        HBox btnBox5 = new HBox(5,notificationBtn1);
        styleMenuButton(notificationBtn1);

        
        Button customerBtn1 = new Button("👬 Customer");
        HBox btnBox6 = new HBox(5,customerBtn1);
        styleMenuButton(customerBtn1);


        VBox vBoxBtn1 = new VBox(10,btnBox1,btnBox2,btnBox3,btnBox4,btnBox5,btnBox6);
        
        Button settingsBtn1 = new Button("⚙  Settings");
        HBox btnBox7 = new HBox(5,settingsBtn1);
        styleMenuButton(settingsBtn1);

        
        Button supportBtn1 = new Button("❓ Help & Support");
        HBox btnBox8 = new HBox(5,supportBtn1);
        styleMenuButton(supportBtn1);


        Button logoutBtn1 = new Button("↪  Logout");
        HBox btnBox9 = new HBox(5,logoutBtn1);
        styleMenuButton(logoutBtn1);


        VBox vBoxBtn2 = new VBox(10,btnBox7,btnBox8,btnBox9);
        
        VBox btnBox = new VBox(90,vBoxBtn1,vBoxBtn2);
        

        HBox logoTextHBox = new HBox(10, logoImageView, textName);
        logoTextHBox.setAlignment(Pos.CENTER_LEFT);
        logoTextHBox.setPadding(new Insets(0, 5, 10, 5));

        VBox leftVB = new VBox(logoTextHBox,btnBox);
        leftVB.setPrefWidth(260);
        leftVB.setMinWidth(260);
        leftVB.setMaxWidth(260);

        leftVB.setSpacing(25);

        leftVB.setPadding(
            new Insets(25, 18, 25, 18)
        );

       leftVB.setStyle(
            "-fx-background-color: #F5EFE6;" +
            "-fx-background-radius: 15;" +
            "-fx-border-color: #D8C7B5;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 15;"
        );

        
        borderPane.setLeft(leftVB);

        StackPane root = new StackPane();
        root.getChildren().addAll(borderPane);
    
        root.setStyle(
            "-fx-background-color: #EDE3D5;"
        );
        borderPane.prefWidthProperty().bind(root.widthProperty());
        borderPane.prefHeightProperty().bind(root.heightProperty());
        providerDashboardScene = new Scene(root);
        
        return providerDashboardScene;
    }
    private void styleMenuButton(Button button) {

        button.setPrefWidth(224);
        button.setMinWidth(224);
        button.setMaxWidth(224);

        button.setPrefHeight(48);
        button.setMinHeight(48);
        button.setMaxHeight(48);

        button.setAlignment(Pos.CENTER_LEFT);

        button.setPadding(
            new Insets(0, 15, 0, 15)
        );

        // Normal style
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 10;" +
            "-fx-text-fill: #5C4033;" +
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: normal;" +
            "-fx-cursor: hand;"
        );

        // Hover effect
        button.setOnMouseEntered(e -> {

            button.setStyle(
                "-fx-background-color: #E4D3C2;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #3E2723;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
            );
        });

        // Back to normal
        button.setOnMouseExited(e -> {

            button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #5C4033;" +
                "-fx-font-family: 'Poppins';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: normal;" +
                "-fx-cursor: hand;"
            );
        });
    }
    private void styleLogoutButton(Button button) {

        button.setPrefWidth(224);
        button.setMinWidth(224);
        button.setMaxWidth(224);

        button.setPrefHeight(48);
        button.setMinHeight(48);
        button.setMaxHeight(48);

        button.setAlignment(Pos.CENTER_LEFT);

        button.setPadding(
            new Insets(0, 15, 0, 15)
        );

        button.setFont(
            javafx.scene.text.Font.font(
                "Poppins",
                14
            )
        );

        button.setTextFill(
            javafx.scene.paint.Color.web("#FFFFFF")
        );

        button.setStyle(
            "-fx-background-color: #8B3A3A;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e -> {

            button.setStyle(
                "-fx-background-color: #A94442;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"
            );
        });

        button.setOnMouseExited(e -> {

            button.setStyle(
                "-fx-background-color: #8B3A3A;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;"
            );
        });
    }
}