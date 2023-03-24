/*
 * Copyright (c) 2023. Etienne Collin #20237904 & Justin Villeneuve #20132792
 */

package com.etiennecollin.tp2.clientFx;

import com.etiennecollin.tp2.server.models.Course;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientController {
    @FXML
    private Label labelClientFeedback;
    @FXML
    private TableView<Course> table;

    @FXML
    private TableColumn<Course, String> tableColumnCode;

    @FXML
    private TableColumn<Course, String> tableColumnCourse;

    @FXML
    protected void onCloseButtonClick() {
        labelClientFeedback.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onHideButtonClick() {
        labelClientFeedback.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onLoadButtonClick() {
        tableColumnCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        tableColumnCourse.setCellValueFactory(new PropertyValueFactory<>("name"));
        Course course = new Course("Prog2", "IFT1025", "Automne");
        table.getItems().add(course);

    }

    @FXML
    protected void onRegisterButtonClick() {
        labelClientFeedback.setText("Welcome to JavaFX Application!");
    }
}