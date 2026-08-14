package com.desgin.view.farmer.harshal;

import javax.swing.text.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BookingDetails {

        public VBox getBookingDetails(Runnable closeAction, Runnable cancelAction) {

                VBox mainBox = new VBox(20);
                mainBox.setPadding(new Insets(25));
                mainBox.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 15;");

                HBox header = new HBox();
                header.setAlignment(Pos.CENTER_LEFT);

                VBox titleBox = new VBox(5);

                Label title = new Label("Booking Details");
                title.setStyle(
                                "-fx-font-size: 24px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #1B4332;");

                Label bookingId = new Label("Booking ID: BK00123");
                bookingId.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #6B7280;");

                titleBox.getChildren().addAll(
                                title,
                                bookingId);

                HBox.setHgrow(titleBox, javafx.scene.layout.Priority.ALWAYS);

                Button closeButton = new Button("✕");
                closeButton.setOnAction(e -> closeAction.run());
                closeButton.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #6B7280;" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;");

                header.getChildren().addAll(
                                titleBox,
                                closeButton);

                HBox equipmentBox = new HBox(20);
                equipmentBox.setPadding(new Insets(15));
                equipmentBox.setAlignment(Pos.CENTER_LEFT);
                equipmentBox.setStyle(
                                "-fx-background-color: #F5F7F5;" +
                                                "-fx-background-radius: 10;");

                VBox imageBox = new VBox();
                imageBox.setPrefWidth(130);
                imageBox.setPrefHeight(100);
                imageBox.setAlignment(Pos.CENTER);
                imageBox.setStyle(
                                "-fx-background-color: #E8F1EB;" +
                                                "-fx-background-radius: 10;");

                Label imageLabel = new Label("Equipment Image");
                imageLabel.setStyle(
                                "-fx-text-fill: #52796F;" +
                                                "-fx-font-weight: bold;");

                imageBox.getChildren().add(imageLabel);

                VBox equipmentInfo = new VBox(7);

                Label equipmentName = new Label("John Deere Tractor");
                equipmentName.setStyle(
                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #1B4332;");

                Label equipmentType = new Label(
                                "Heavy Duty Tractor");
                equipmentType.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #6B7280;");

                equipmentInfo.getChildren().addAll(
                                equipmentName,
                                equipmentType);

                equipmentBox.getChildren().addAll(
                                imageBox,
                                equipmentInfo);

                Label rentalTitle = new Label("Rental Information");
                rentalTitle.setStyle(
                                "-fx-font-size: 17px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #1B4332;");

                VBox rentalBox = new VBox(10);
                rentalBox.setPadding(new Insets(15));
                rentalBox.setStyle(
                                "-fx-background-color: #F8FAF9;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: #E2E8E4;" +
                                                "-fx-border-radius: 10;");
                rentalBox.getChildren().addAll(

                                createInfoRow(
                                                "Start Date",
                                                "15 Aug 2026"),

                                createInfoRow(
                                                "End Date",
                                                "18 Aug 2026"),

                                createInfoRow(
                                                "Rental Duration",
                                                "3 Days"),

                                createInfoRow(
                                                "Price Per Day",
                                                "₹2,500"),

                                createInfoRow(
                                                "Total Amount",
                                                "₹7,500"));

                Label ownerTitle = new Label("Equipment Owner");
                ownerTitle.setStyle(
                                "-fx-font-size: 17px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #1B4332;");

                VBox ownerBox = new VBox(8);
                ownerBox.setPadding(new Insets(15));
                ownerBox.setStyle(
                                "-fx-background-color: #F8FAF9;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-border-color: #E2E8E4;" +
                                                "-fx-border-radius: 10;");

                Label ownerName = new Label("Rahul Patil");
                ownerName.setStyle(
                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #374151;");

                Label phone = new Label(
                                "Phone: +91 XXXXX XXXXX");
                phone.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #6B7280;");
                ownerBox.getChildren().addAll(
                                ownerName,
                                phone);

                HBox statusBox = new HBox();
                statusBox.setAlignment(Pos.CENTER_LEFT);

                Label status = new Label("ACTIVE");
                status.setPadding(
                                new Insets(7, 14, 7, 14));
                status.setStyle(
                                "-fx-background-color: #DCFCE7;" +
                                                "-fx-text-fill: #166534;" +
                                                "-fx-background-radius: 20;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-font-size: 12px;");
                statusBox.getChildren().add(status);

                HBox actions = new HBox(10);
                actions.setAlignment(Pos.CENTER_RIGHT);

                Button contactButton = new Button("Contact Owner");

                contactButton.setOnAction(e -> {
                        showContactOwnerPopup();
                });
                contactButton.setPrefHeight(38);

                contactButton.setStyle(
                                "-fx-background-color: #52796F;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-weight: bold;");

                Button cancelButton = new Button("Cancel Booking");
                cancelButton.setOnAction(e -> {
                        showCancelConfirmation(cancelAction);
                });
                cancelButton.setPrefHeight(38);

                cancelButton.setStyle(
                                "-fx-background-color: #FEE2E2;" +
                                                "-fx-text-fill: #B91C1C;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-weight: bold;");
                actions.getChildren().addAll(
                                contactButton,
                                cancelButton);

                mainBox.getChildren().addAll(
                                header,
                                equipmentBox,
                                rentalTitle,
                                rentalBox,
                                ownerTitle,
                                ownerBox,
                                statusBox,
                                actions);

                return mainBox;
        }

        private HBox createInfoRow(
                        String title,
                        String value) {

                HBox row = new HBox();
                row.setAlignment(Pos.CENTER_LEFT);

                Label titleLabel = new Label(title);
                titleLabel.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-text-fill: #6B7280;");

                Label valueLabel = new Label(value);
                valueLabel.setStyle(
                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #374151;");

                HBox.setHgrow(
                                titleLabel,
                                javafx.scene.layout.Priority.ALWAYS);

                row.getChildren().addAll(
                                titleLabel,
                                valueLabel);

                return row;
        }

        private void showContactOwnerPopup() {

                VBox contactBox = new VBox(15);

                contactBox.setPadding(new Insets(25));
                contactBox.setPrefWidth(400);

                contactBox.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 15;" +
                                                "-fx-border-color: #D8C7B5;" +
                                                "-fx-border-radius: 15;");

                Label title = new Label("Contact Equipment Owner");

                title.setStyle(
                                "-fx-font-size: 22px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #1B4332;");

                Label owner = new Label("Rahul Patil");

                owner.setStyle(
                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #374151;");

                Label phone = new Label("📞  +91 XXXXX XXXXX");

                phone.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #6B7280;");

                Label email = new Label("✉  rahul@example.com");

                email.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #6B7280;");

                Button closeButton = new Button("Close");

                closeButton.setStyle(
                                "-fx-background-color: #52796F;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-weight: bold;");

                contactBox.getChildren().addAll(
                                title,
                                owner,
                                phone,
                                email,
                                closeButton);

                Stage popup = new Stage();

                popup.setTitle("Contact Owner");

                Scene scene = new Scene(
                                contactBox,
                                400,
                                280);

                popup.setScene(scene);

                closeButton.setOnAction(e -> popup.close());

                popup.show();
        }

        private void showCancelConfirmation(Runnable cancelAction) {

                Stage popup = new Stage();

                VBox box = new VBox(15);
                box.setPadding(new Insets(25));
                box.setAlignment(Pos.CENTER);

                box.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 15;");

                Label title = new Label("Cancel Booking?");

                title.setStyle(
                                "-fx-font-size: 22px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #1B4332;");

                Label message = new Label(
                                "Are you sure you want to cancel this booking?");

                message.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-text-fill: #6B7280;");

                Label equipment = new Label(
                                "John Deere Tractor\n15 Aug 2026 → 18 Aug 2026");

                equipment.setStyle(
                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-text-fill: #374151;");

                HBox buttons = new HBox(10);
                buttons.setAlignment(Pos.CENTER);

                Button keepButton = new Button("Keep Booking");

                keepButton.setStyle(
                                "-fx-background-color: #E8F1EB;" +
                                                "-fx-text-fill: #1B4332;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-weight: bold;");

                Button cancelButton = new Button("Cancel Booking");

                cancelButton.setStyle(
                                "-fx-background-color: #B91C1C;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-weight: bold;");

                cancelButton.setOnAction(e -> {

                        cancelAction.run();

                        popup.close();
                });
                buttons.getChildren().addAll(
                                keepButton,
                                cancelButton);

                box.getChildren().addAll(
                                title,
                                message,
                                equipment,
                                buttons);

                Scene scene = new Scene(
                                box,
                                420,
                                250);

                popup.setScene(scene);

                keepButton.setOnAction(e -> {
                        popup.close();
                });

                cancelButton.setOnAction(e -> {

                        System.out.println(
                                        "Booking cancelled");

                        popup.close();
                });

                popup.show();
        }
}
