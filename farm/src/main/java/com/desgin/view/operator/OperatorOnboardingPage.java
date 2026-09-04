package com.desgin.view.operator;

import java.io.File;

import com.desgin.config.CloudinaryConfig;
import com.desgin.dao.AuthDAO;
import com.desgin.view.handling_start.WelcomePage;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class OperatorOnboardingPage {

    private final String initialName;
    private final String initialEmail;
    private final String initialMobile;
    private final Runnable backToLogin;

    private String uploadedPhotoUrl = "";
    private String uploadedLicenseImgUrl = "";

    public OperatorOnboardingPage(String name, String email, String mobile, Runnable backToLogin) {
        this.initialName = name != null ? name : "";
        this.initialEmail = email != null ? email : "";
        this.initialMobile = mobile != null ? mobile : "";
        this.backToLogin = backToLogin;
    }

    public Scene getOnboardingScene() {
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: transparent;");

        // ------------------ BACKGROUND IMAGE ------------------
        Image backgroundImage;
        try {
            backgroundImage = new Image("/assets/Images/background.jpeg");
            if (backgroundImage.isError()) {
                backgroundImage = new Image("file:farm/src/main/resources/assets/Images/background.jpeg");
            }
        } catch (Exception e) {
            backgroundImage = new Image("file:farm/src/main/resources/assets/Images/background.jpeg");
        }

        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);
        backgroundImageView.setMouseTransparent(true);

        StackPane rootStackPane = new StackPane();
        backgroundImageView.fitWidthProperty().bind(rootStackPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(rootStackPane.heightProperty());

        // ------------------ MAIN FORM CARD ------------------
        VBox formCard = createOnboardingCard(rootStackPane);

        StackPane cardHolder = new StackPane(formCard);
        cardHolder.setAlignment(Pos.CENTER);
        cardHolder.setPadding(new Insets(30, 20, 30, 20));
        cardHolder.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(cardHolder);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        borderPane.setCenter(scrollPane);
        rootStackPane.getChildren().addAll(backgroundImageView, borderPane);

        return new Scene(rootStackPane);
    }

    private VBox createOnboardingCard(StackPane root) {
        // Logo / Icon Header
        Text headerIcon = new Text("👷");
        headerIcon.setStyle("-fx-font-size: 34px;");

        Text mainTitle = new Text("Operator Professional Profile");
        mainTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text subTitle = new Text("Set up your machinery business credentials to start receiving farm hiring requests.");
        subTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-fill: #4B5563;");

        VBox headerBox = new VBox(4, headerIcon, mainTitle, subTitle);
        headerBox.setAlignment(Pos.CENTER);

        // ------------------ 1. PROFILE PHOTO SECTION ------------------
        Text photoIcon = new Text("👨‍🌾");
        photoIcon.setStyle("-fx-font-size: 28px;");

        ImageView photoView = new ImageView();
        photoView.setFitWidth(64);
        photoView.setFitHeight(64);
        Circle photoClip = new Circle(32, 32, 32);
        photoView.setClip(photoClip);
        photoView.setVisible(false);
        photoView.setManaged(false);

        StackPane avatarBox = new StackPane(photoIcon, photoView);
        avatarBox.setPrefSize(64, 64);
        avatarBox.setMaxSize(64, 64);
        avatarBox.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 32; -fx-border-color: #A5D6A7; -fx-border-width: 1.5; -fx-border-radius: 32;");

        Button choosePhotoBtn = new Button("📷 Upload Operator Photo");
        choosePhotoBtn.setStyle("-fx-background-color: #2D6A4F; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 6 14;");

        Label photoStatusLabel = new Label("Upload photo for farm provider verification");
        photoStatusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #6B7280;");

        choosePhotoBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Operator Profile Photo");
            chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp", "*.bmp")
            );
            Window win = root.getScene() != null ? root.getScene().getWindow() : null;
            File file = chooser.showOpenDialog(win);
            if (file != null) {
                photoStatusLabel.setText("Uploading photo to Cloudinary...");
                choosePhotoBtn.setDisable(true);
                Task<String> task = new Task<>() {
                    @Override
                    protected String call() {
                        return CloudinaryConfig.uploadImage(file);
                    }
                };
                task.setOnSucceeded(ev -> {
                    choosePhotoBtn.setDisable(false);
                    String url = task.getValue();
                    if (url != null && !url.isEmpty()) {
                        uploadedPhotoUrl = url;
                        photoStatusLabel.setText("✓ Photo uploaded successfully");
                        photoStatusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-text-fill: #15803D; -fx-font-weight: bold;");
                        try {
                            photoView.setImage(new Image(url, true));
                            photoView.setVisible(true);
                            photoView.setManaged(true);
                            photoIcon.setVisible(false);
                            photoIcon.setManaged(false);
                        } catch (Exception ignored) {}
                    } else {
                        photoStatusLabel.setText("Upload failed. Try again.");
                    }
                });
                task.setOnFailed(ev -> {
                    choosePhotoBtn.setDisable(false);
                    photoStatusLabel.setText("Upload error. Try again.");
                });
                new Thread(task).start();
            }
        });

        VBox photoActions = new VBox(5, choosePhotoBtn, photoStatusLabel);
        photoActions.setAlignment(Pos.CENTER_LEFT);

        HBox photoSection = new HBox(16, avatarBox, photoActions);
        photoSection.setAlignment(Pos.CENTER_LEFT);
        photoSection.setPadding(new Insets(10, 14, 10, 14));
        photoSection.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #C2E0CE; -fx-border-radius: 12; -fx-border-width: 1;");

        // ------------------ 2. FORM FIELDS ------------------
        TextField nameField = new TextField(initialName);
        TextField mobileField = new TextField(initialMobile);
        TextField emailField = new TextField(initialEmail);

        styleInputField(nameField);
        styleInputField(mobileField);
        styleInputField(emailField);

        ComboBox<String> expCombo = new ComboBox<>();
        expCombo.getItems().addAll(
            "1-2 Years (Junior Machinery Operator)",
            "3-5 Years (Certified Heavy Machinery Operator)",
            "5-8 Years (Senior Field Specialist)",
            "8+ Years (Master Agro-Equipment Expert)"
        );
        expCombo.setValue("3-5 Years (Certified Heavy Machinery Operator)");
        styleComboBox(expCombo);

        ComboBox<String> profCombo = new ComboBox<>();
        profCombo.getItems().addAll(
            "Tractors & Heavy Tillage",
            "Combined Harvesters & Threshers",
            "Rotavators, Cultivators & Seeders",
            "High-Capacity Sprayers & Agri Drones",
            "Multi-Machinery Operator (All Types)"
        );
        profCombo.setValue("Tractors & Heavy Tillage");
        styleComboBox(profCombo);

        // ------------------ 3. DRIVING LICENSE IMAGE ------------------
        Text dlIcon = new Text("📄");
        dlIcon.setStyle("-fx-font-size: 20px;");

        ImageView dlView = new ImageView();
        dlView.setFitWidth(65);
        dlView.setFitHeight(45);
        dlView.setPreserveRatio(true);
        dlView.setVisible(false);
        dlView.setManaged(false);

        StackPane dlPreviewBox = new StackPane(dlIcon, dlView);
        dlPreviewBox.setPrefSize(70, 48);
        dlPreviewBox.setMaxSize(70, 48);
        dlPreviewBox.setStyle("-fx-background-color: #F4F9F4; -fx-background-radius: 8; -fx-border-color: #C2E0CE; -fx-border-radius: 8; -fx-border-width: 1;");

        Button chooseDlBtn = new Button("📁 Upload Driving License");
        chooseDlBtn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 5 12;");

        Label dlStatusLabel = new Label("Attach DL or Heavy Machinery Certificate");
        dlStatusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-text-fill: #6B7280;");

        chooseDlBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Driving License Document/Image");
            chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp", "*.bmp")
            );
            Window win = root.getScene() != null ? root.getScene().getWindow() : null;
            File file = chooser.showOpenDialog(win);
            if (file != null) {
                dlStatusLabel.setText("Uploading license...");
                chooseDlBtn.setDisable(true);
                Task<String> task = new Task<>() {
                    @Override
                    protected String call() {
                        return CloudinaryConfig.uploadImage(file);
                    }
                };
                task.setOnSucceeded(ev -> {
                    chooseDlBtn.setDisable(false);
                    String url = task.getValue();
                    if (url != null && !url.isEmpty()) {
                        uploadedLicenseImgUrl = url;
                        dlStatusLabel.setText("✓ License attached");
                        dlStatusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 10.5px; -fx-text-fill: #15803D; -fx-font-weight: bold;");
                        try {
                            dlView.setImage(new Image(url, true));
                            dlView.setVisible(true);
                            dlView.setManaged(true);
                            dlIcon.setVisible(false);
                            dlIcon.setManaged(false);
                        } catch (Exception ignored) {}
                    } else {
                        dlStatusLabel.setText("Upload failed. Try again.");
                    }
                });
                task.setOnFailed(ev -> {
                    chooseDlBtn.setDisable(false);
                    dlStatusLabel.setText("Upload error. Try again.");
                });
                new Thread(task).start();
            }
        });

        VBox dlActionBox = new VBox(3, chooseDlBtn, dlStatusLabel);
        dlActionBox.setAlignment(Pos.CENTER_LEFT);

        HBox dlRow = new HBox(12, dlPreviewBox, dlActionBox);
        dlRow.setAlignment(Pos.CENTER_LEFT);

        // ------------------ GRID LAYOUT ------------------
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(12);

        grid.add(createFieldBlock("Full Name", nameField), 0, 0);
        grid.add(createFieldBlock("Mobile Number", mobileField), 1, 0);
        grid.add(createFieldBlock("Email Address", emailField), 0, 1);
        grid.add(createFieldBlock("Driving Experience", expCombo), 1, 1);
        grid.add(createFieldBlock("Equipment Profession", profCombo), 0, 2);
        grid.add(createFieldBlock("Driving License (DL Image)", dlRow), 1, 2);

        VBox formContent = new VBox(14, photoSection, grid);
        formContent.setPadding(new Insets(16));
        formContent.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-radius: 14; -fx-border-width: 1;");

        // ------------------ ACTION BUTTONS ------------------
        Button submitBtn = new Button("✓ Save Business Profile & Launch Dashboard 🚀");
        submitBtn.setStyle("-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 24; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.3), 8, 0, 0, 3);");
        submitBtn.setOnMouseEntered(e -> submitBtn.setStyle("-fx-background-color: linear-gradient(to right, #1B4332, #2D6A4F); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 24; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.4), 10, 0, 0, 4);"));
        submitBtn.setOnMouseExited(e -> submitBtn.setStyle("-fx-background-color: linear-gradient(to right, #2D6A4F, #40916C); -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 24; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(45, 106, 79, 0.3), 8, 0, 0, 3);"));

        Button skipBtn = new Button("Skip for Now →");
        skipBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #4B5563; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: 500; -fx-cursor: hand; -fx-underline: true;");
        skipBtn.setOnMouseEntered(e -> skipBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #1B4332; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-cursor: hand; -fx-underline: true;"));
        skipBtn.setOnMouseExited(e -> skipBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #4B5563; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: 500; -fx-cursor: hand; -fx-underline: true;"));

        Runnable navigateToDashboard = () -> {
            OperatorProfileManagement.updateHeaderGreeting();
            OperatorDashboard obj = new OperatorDashboard();
            WelcomePage.welcomePageStage.setScene(obj.getOperatorDashboardScene(backToLogin));
        };

        submitBtn.setOnAction(e -> {
            String fName = nameField.getText().trim().isEmpty() ? initialName : nameField.getText().trim();
            String fPhone = mobileField.getText().trim().isEmpty() ? initialMobile : mobileField.getText().trim();
            String fEmail = emailField.getText().trim().isEmpty() ? initialEmail : emailField.getText().trim();
            String fExp = expCombo.getValue();
            String fProf = profCombo.getValue();

            OperatorProfileStore.setBusinessProfile(fName, fPhone, fEmail, fExp, fProf, uploadedPhotoUrl, uploadedLicenseImgUrl);

            // Persist to Firestore asynchronously
            new Thread(() -> {
                try {
                    new AuthDAO().updateOperatorBusinessInfo(fEmail, fName, fPhone, uploadedPhotoUrl, fExp, fProf, uploadedLicenseImgUrl);
                } catch (Exception ignored) {}
            }).start();

            navigateToDashboard.run();
        });

        skipBtn.setOnAction(e -> navigateToDashboard.run());

        HBox btnRow = new HBox(16, submitBtn, skipBtn);
        btnRow.setAlignment(Pos.CENTER);

        // Main Card Container
        VBox card = new VBox(16, headerBox, formContent, btnRow);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(620);
        card.setMaxHeight(Region.USE_PREF_SIZE);
        card.setPadding(new Insets(24, 30, 24, 30));
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.88);" +
            "-fx-background-radius: 20px;" +
            "-fx-border-color: rgba(255, 255, 255, 0.90);" +
            "-fx-border-radius: 20px;" +
            "-fx-border-width: 1.5px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.22), 26, 0.12, 0, 8);"
        );

        return card;
    }

    private VBox createFieldBlock(String labelText, javafx.scene.Node inputNode) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1B4332;");

        VBox block = new VBox(4, label, inputNode);
        block.setAlignment(Pos.CENTER_LEFT);
        return block;
    }

    private void styleInputField(TextField tf) {
        tf.setPrefHeight(38);
        tf.setPrefWidth(240);
        tf.setMaxWidth(240);
        tf.setStyle("-fx-background-color: #F8FAF8; -fx-border-color: #C2E0CE; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-padding: 0 10;");
        tf.focusedProperty().addListener((obs, oldV, isFoc) -> {
            if (isFoc) {
                tf.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #2D6A4F; -fx-border-width: 1.5; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-padding: 0 10;");
            } else {
                tf.setStyle("-fx-background-color: #F8FAF8; -fx-border-color: #C2E0CE; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-padding: 0 10;");
            }
        });
    }

    private void styleComboBox(ComboBox<String> cb) {
        cb.setPrefHeight(38);
        cb.setPrefWidth(240);
        cb.setMaxWidth(240);
        cb.setStyle("-fx-background-color: #F8FAF8; -fx-border-color: #C2E0CE; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 11.5px;");
    }
}
