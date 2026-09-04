package com.desgin.view.operator;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class OperatorDashboard {

    private Scene operatorDashboardScene;
    public static BorderPane borderPane;
    public static StackPane root;

    public Scene getOperatorDashboardScene(Runnable ref) {
        root = new StackPane();

        // Left Sidebar for Machinery Operator
        OperatorLeftSideBar objLeftSideBar = new OperatorLeftSideBar(this);
        VBox leftVB = objLeftSideBar.getSideBar(ref);

        borderPane = new BorderPane();
        borderPane.setPadding(Insets.EMPTY);
        borderPane.setStyle("-fx-background-color: #F4F9F4;");

        BorderPane subroot = new BorderPane();
        subroot.setLeft(leftVB);
        subroot.setCenter(borderPane);
        subroot.setStyle("-fx-background-color: #F4F9F4;");

        root.getChildren().addAll(subroot);

        // Top Profile Management Header (Notifications + Profile Card)
        OperatorProfileManagement objProfileManagement = new OperatorProfileManagement();
        borderPane.setTop(objProfileManagement.getProfile(root));

        // Initial Home Page
        OperatorHome.getPage();

        root.setStyle("-fx-background-color: #F4F9F4;");

        subroot.prefWidthProperty().bind(root.widthProperty());
        subroot.prefHeightProperty().bind(root.heightProperty());
        operatorDashboardScene = new Scene(root);

        // Start watching user status in case admin suspends account live
        String opKey = (OperatorProfileStore.email != null && !OperatorProfileStore.email.isEmpty()) ? OperatorProfileStore.email : OperatorProfileStore.phone;
        if (opKey != null && !opKey.isEmpty()) {
            com.desgin.service.UserStatusWatcher.startWatching(opKey, "Operator", ref);
        }

        return operatorDashboardScene;
    }
}
