/*
 * Copyright (c) 2023. Etienne Collin #20237904 & Justin Villeneuve #20132792
 */

package com.etiennecollin.tp2.clientFx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Client extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load window style
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clientInterface.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);

        // Set window properties
        stage.setTitle("UdeM Course Registration Form");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(true);

        // Display window
        stage.show();
    }

    public void run() {
        launch();
    }
}