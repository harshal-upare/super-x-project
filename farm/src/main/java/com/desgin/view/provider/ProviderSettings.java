package com.desgin.view.provider;

import com.desgin.dao.AuthDAO;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ProviderSettings {

    public static ScrollPane getSettingsSection() {
        Text headerTitle = new Text("Provider Account & Profile Settings");
        headerTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text headerSubtitle = new Text("Update your contact details, service area, and regional location for matching nearby farmers.");
        headerSubtitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-fill: #4B5563;");

        VBox titleBox = new VBox(4, headerTitle, headerSubtitle);

        // Required Profile & Location Fields Card
        VBox profileCard = new VBox(14);
        profileCard.setPadding(new Insets(22));
        profileCard.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text cardTitle = new Text("Account Profile & Operating Hub");
        cardTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(14);

        TextField nameField = createTextField(ProviderProfileStore.name != null ? ProviderProfileStore.name : "");
        nameField.setPromptText("Enter your full name");

        TextField phoneField = createTextField(ProviderProfileStore.phone != null ? ProviderProfileStore.phone : "");
        phoneField.setPromptText("Enter 10-digit mobile number");

        TextField emailField = createTextField(ProviderProfileStore.email != null ? ProviderProfileStore.email : "");
        emailField.setEditable(false);
        emailField.setStyle("-fx-background-color: #F3F4F6; -fx-border-color: #E5E7EB; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-text-fill: #6B7280;");

        TextField townField = createTextField(ProviderProfileStore.town != null ? ProviderProfileStore.town : "Pune");
        townField.setPromptText("Operating town/city");

        TextField districtField = createTextField(ProviderProfileStore.district != null ? ProviderProfileStore.district : "Pune");
        districtField.setPromptText("District");

        TextField stateField = createTextField(ProviderProfileStore.state != null ? ProviderProfileStore.state : "Maharashtra");
        stateField.setPromptText("State");

        TextField pincodeField = createTextField(ProviderProfileStore.pincode != null ? ProviderProfileStore.pincode : "411001");
        pincodeField.setPromptText("Postal PIN Code");

        form.add(createLabel("Full Name / Provider Name:"), 0, 0);
        form.add(nameField, 1, 0);

        form.add(createLabel("Phone / WhatsApp:"), 0, 1);
        form.add(phoneField, 1, 1);

        form.add(createLabel("Registered Email:"), 0, 2);
        form.add(emailField, 1, 2);

        form.add(createLabel("Operating Town / City:"), 0, 3);
        form.add(townField, 1, 3);

        form.add(createLabel("District:"), 0, 4);
        form.add(districtField, 1, 4);

        form.add(createLabel("State:"), 0, 5);
        form.add(stateField, 1, 5);

        form.add(createLabel("PIN Code:"), 0, 6);
        form.add(pincodeField, 1, 6);

        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        Button saveBtn = new Button("💾  Save Changes");
        saveBtn.setPrefHeight(40);
        saveBtn.setPrefWidth(200);
        saveBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        saveBtn.setOnAction(e -> {
            String newName = nameField.getText() != null ? nameField.getText().trim() : "";
            String newPhone = phoneField.getText() != null ? phoneField.getText().trim() : "";
            String newTown = townField.getText() != null ? townField.getText().trim() : "";
            String newDistrict = districtField.getText() != null ? districtField.getText().trim() : "";
            String newState = stateField.getText() != null ? stateField.getText().trim() : "";
            String newPincode = pincodeField.getText() != null ? pincodeField.getText().trim() : "";

            if (newName.isEmpty() || newTown.isEmpty()) {
                statusLabel.setText("⚠ Please fill in your name and operating town.");
                statusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #DC2626;");
                return;
            }

            // 1. Instant local store update (0 delay)
            ProviderProfileStore.name = newName;
            ProviderProfileStore.phone = newPhone;
            ProviderProfileStore.town = newTown;
            ProviderProfileStore.district = newDistrict;
            ProviderProfileStore.state = newState;
            ProviderProfileStore.pincode = newPincode;
            ProviderProfileStore.notifyListeners();

            statusLabel.setText("✔ Changes saved successfully!");
            statusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #15803D;");

            // 2. Asynchronous background write to database
            Thread bg = new Thread(() -> {
                try {
                    AuthDAO dao = new AuthDAO();
                    String mail = ProviderProfileStore.email;
                    dao.updateProfile(mail, "Provider", newName, newPhone);
                    dao.updateLocation(mail, "Provider", newTown, newDistrict, newState, newPincode);
                } catch (Exception ignored) {}
            });
            bg.setDaemon(true);
            bg.start();
        });

        HBox btnRow = new HBox(15, saveBtn, statusLabel);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        profileCard.getChildren().addAll(cardTitle, form, btnRow);

        // --------------------------------------------------------
        // BANK ACCOUNT & SETTLEMENT PAYOUTS CARD
        // --------------------------------------------------------
        VBox bankCard = new VBox(14);
        bankCard.setPadding(new Insets(22));
        bankCard.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14; -fx-border-color: #E2EBE5; -fx-border-width: 1; -fx-border-radius: 14;");

        Text bankTitle = new Text("🏦 Bank Account & Payout Settlement Details");
        bankTitle.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #1B4332;");

        Text bankSub = new Text("Linked bank account and UPI details where rental escrow payments from farmers will be transferred.");
        bankSub.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-fill: #6B7280;");

        GridPane bankForm = new GridPane();
        bankForm.setHgap(15);
        bankForm.setVgap(14);

        TextField holderField = createTextField(ProviderProfileStore.accountHolder != null ? ProviderProfileStore.accountHolder : "");
        holderField.setPromptText("Account Holder Name (as in bank passbook)");

        TextField bankNameField = createTextField(ProviderProfileStore.bankName != null ? ProviderProfileStore.bankName : "");
        bankNameField.setPromptText("e.g. State Bank of India, HDFC Bank");

        TextField accNumField = createTextField(ProviderProfileStore.accountNumber != null ? ProviderProfileStore.accountNumber : "");
        accNumField.setPromptText("e.g. 11-16 digit bank account number");

        TextField ifscField = createTextField(ProviderProfileStore.ifsc != null ? ProviderProfileStore.ifsc : "");
        ifscField.setPromptText("e.g. SBIN0001234");

        TextField upiField = createTextField(ProviderProfileStore.upiId != null ? ProviderProfileStore.upiId : "");
        upiField.setPromptText("e.g. provider@okhdfcbank");

        bankForm.add(createLabel("Account Holder Name:"), 0, 0);
        bankForm.add(holderField, 1, 0);

        bankForm.add(createLabel("Bank Name:"), 0, 1);
        bankForm.add(bankNameField, 1, 1);

        bankForm.add(createLabel("Bank Account Number:"), 0, 2);
        bankForm.add(accNumField, 1, 2);

        bankForm.add(createLabel("Bank IFSC Code:"), 0, 3);
        bankForm.add(ifscField, 1, 3);

        bankForm.add(createLabel("UPI ID (Optional):"), 0, 4);
        bankForm.add(upiField, 1, 4);

        Label bankStatusLabel = new Label("");
        bankStatusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        Button saveBankBtn = new Button("💳  Save Bank & Settlement Details");
        saveBankBtn.setPrefHeight(40);
        saveBankBtn.setPrefWidth(270);
        saveBankBtn.setStyle("-fx-background-color: #1B4332; -fx-text-fill: white; -fx-font-family: 'Poppins'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        saveBankBtn.setOnAction(e -> {
            String newHolder = holderField.getText() != null ? holderField.getText().trim() : "";
            String newBName = bankNameField.getText() != null ? bankNameField.getText().trim() : "";
            String newAcc = accNumField.getText() != null ? accNumField.getText().trim() : "";
            String newIfsc = ifscField.getText() != null ? ifscField.getText().trim() : "";
            String newUpi = upiField.getText() != null ? upiField.getText().trim() : "";

            if (newHolder.isEmpty() || newAcc.isEmpty() || newIfsc.isEmpty()) {
                bankStatusLabel.setText("⚠ Please fill Account Holder, Account Number, and IFSC.");
                bankStatusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #DC2626;");
                return;
            }

            // 1. Instant local store update
            ProviderProfileStore.setBankDetails(newHolder, newBName, newAcc, newIfsc, newUpi);

            bankStatusLabel.setText("✔ Bank details saved to database!");
            bankStatusLabel.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #15803D;");

            // 2. Background Firestore update
            Thread bg = new Thread(() -> {
                try {
                    AuthDAO dao = new AuthDAO();
                    String mail = ProviderProfileStore.email;
                    dao.updateBankDetails(mail, "Provider", newHolder, newBName, newAcc, newIfsc, newUpi);
                } catch (Exception ignored) {}
            });
            bg.setDaemon(true);
            bg.start();
        });

        HBox bankBtnRow = new HBox(15, saveBankBtn, bankStatusLabel);
        bankBtnRow.setAlignment(Pos.CENTER_LEFT);

        bankCard.getChildren().addAll(bankTitle, bankSub, bankForm, bankBtnRow);

        VBox content = new VBox(22, titleBox, profileCard, bankCard);
        content.setPadding(new Insets(25, 30, 35, 30));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private static Label createLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        l.setPrefWidth(190);
        return l;
    }

    private static TextField createTextField(String val) {
        TextField tf = new TextField(val != null ? val : "");
        tf.setPrefHeight(36);
        tf.setPrefWidth(320);
        tf.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2EBE5; -fx-border-radius: 6; -fx-font-family: 'Poppins'; -fx-font-size: 12.5px; -fx-text-fill: #1F2937;");
        return tf;
    }
}
