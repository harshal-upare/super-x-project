package com.desgin.view.farmer.Swapnil;

import com.desgin.view.farmer.LeftSideBar;
import com.desgin.view.farmer.ashutosh.profile.ProfileManagement;
import com.desgin.view.farmer.om.BrowseEquip;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FarmerDashboard {

        private Scene farmerDashboardScene;

        public static BorderPane borderPane;
        public static StackPane root;

        public Scene getfarmerDashboardScene() {

                root = new StackPane();

                LeftSideBar objLeftSideBar = new LeftSideBar(this);
                VBox leftVB = objLeftSideBar.getSideBar();

                borderPane = new BorderPane();
                borderPane.setPadding(Insets.EMPTY);

                BorderPane subroot = new BorderPane();
                subroot.setLeft(leftVB);
                subroot.setCenter(borderPane);

                root.getChildren().addAll(subroot);

                ProfileManagement objProfileManagement = new ProfileManagement();
                borderPane.setTop(objProfileManagement.getProfile(root));
                
                Dashboard.getPage();
                

                root.setStyle("-fx-background-color: #EDE3D5;");

                subroot.prefWidthProperty().bind(root.widthProperty());
                subroot.prefHeightProperty().bind(root.heightProperty());
                farmerDashboardScene = new Scene(root);

                return farmerDashboardScene;
        }

}