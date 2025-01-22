/*
 * Copyright (c) 2023. Etienne Collin #20237904
 */

package com.etiennecollin.tp2.clientGUI;

import com.etiennecollin.tp2.server.models.Course;
import com.etiennecollin.tp2.server.models.RegistrationForm;
import com.etiennecollin.tp2.server.models.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.etiennecollin.tp2.clientGUI.Client.objectInputStream;
import static com.etiennecollin.tp2.clientGUI.Client.objectOutputStream;
import static com.etiennecollin.tp2.clientGUI.ClientLauncher.*;
import static com.etiennecollin.tp2.server.Server.*;
import static com.etiennecollin.tp2.server.models.Validator.isEmailValid;
import static com.etiennecollin.tp2.server.models.Validator.isStudentIDValid;

/**
 * The ClientController class is responsible for handling user input and communicating with the server.
 * It implements the Initializable interface, which allows it to initialize components from a JavaFX FXML file.
 */
public class ClientController implements Initializable {
    /**
     * A list of the available semesters.
     */
    private static final String[] semesters = new String[]{"Automne", "Hiver", "Ete"};
    /**
     * A text field in the GUI that displays feedback to the user.
     */
    @FXML
    private Label labelClientFeedback;
    /**
     * A tableView in the GUI used to display the available courses.
     */
    @FXML
    private TableView<Course> courseTable;
    /**
     * A ChoiceBox in the GUI used to get the semester for which to load the available courses.
     */
    @FXML
    private ChoiceBox choiceBox;
    /**
     * A BorderPane in the GUI used to drag the client window.
     */
    @FXML
    private BorderPane borderPane;
    /**
     * Stores the x-coordinate of the client window.
     */
    private double x = 0;
    /**
     * Stores the y-coordinate of the client window.
     */
    private double y = 0;
    /**
     * A TableView column in the GUI that stores the code of a course.
     */
    @FXML
    private TableColumn<Course, String> courseTableColumnCode;
    /**
     * A TableView column in the GUI that stores the name of a course.
     */
    @FXML
    private TableColumn<Course, String> courseTableColumnName;
    /**
     * A text field in the GUI used to get the first name of the student registering.
     */
    @FXML
    private TextField textFieldFirstName;
    /**
     * A text field in the GUI used to get the last name of the student registering.
     */
    @FXML
    private TextField textFieldLastName;
    /**
     * A text field in the GUI used to get the email of the student registering.
     */
    @FXML
    private TextField textFieldEmail;
    /**
     * A text field in the GUI used to get the student ID of the student registering.
     */
    @FXML
    private TextField textFieldStudentID;

    /**
     * Disconnects from the server and closes the application.
     */
    @FXML
    private void onCloseButtonClick() {
        try {
            disconnect();
        } catch (SocketException | EOFException e) {
            // Handle the case where the server crashes without disconnecting
            displayErrorAlert(e.getMessage() + ", the connection to the server was lost. The client will exit.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }

    /**
     * Disconnects the client from the server by sending a DISCONNECT_COMMAND to the server through the objectOutputStream.
     *
     * @throws IOException If an I/O error occurs when writing to the objectOutputStream.
     */
    private void disconnect() throws IOException {
        objectOutputStream.writeObject(DISCONNECT_COMMAND);
        objectOutputStream.flush();
        System.out.println(CLIENT + "Disconnecting from server...");
    }

    /**
     * Displays an error message alert to the user and also broadcasts the error message to the CLI error output.
     *
     * @param message The error message.
     */
    private void displayErrorAlert(String message) {
        // Send error message to CLI
        System.out.println(CLIENT_ERROR + message);

        // Create alert
        Alert alert = new Alert(Alert.AlertType.ERROR, message);

        // Give alert pane some CSS styling
        alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("MainWindowStyle.css")).toExternalForm());

        // Set alert window properties
        alert.setHeaderText("Error");
        alert.setTitle("Error");
        // alert.setGraphic(null);

        // Show alert
        alert.showAndWait();
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }

    /**
     * Sends the application the taskbar.
     */
    @FXML
    private void onHideButtonClick() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Loads the available courses for the semester selected in the choiceBox.
     */
    @FXML
    private void onLoadButtonClick() {
        String semester = choiceBox.getValue().toString();

        ArrayList<Course> courses = null;

        try {
            courses = getCourses(semester);

            // Convert courses to Observable
            ObservableList<Course> observableCourses = FXCollections.observableArrayList(courses);

            // Print available courses
            courseTable.setItems(observableCourses);
        } catch (SocketException | EOFException e) {
            // Handle the case where the server crashes without disconnecting
            displayErrorAlert("The connection to the server was lost. The client will exit.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a command to the server to load available courses for a specific semester.
     *
     * @param semester The semester to filter courses with.
     *
     * @return An ArrayList containing the available courses.
     *
     * @throws IOException            If an I/O error occurs when dealing with the client input/output streams.
     * @throws ClassNotFoundException If the returned Course object by the server is invalid.
     */
    private ArrayList<Course> getCourses(String semester) throws IOException, ClassNotFoundException {
        // Send command to server
        objectOutputStream.writeObject(LOAD_COMMAND + " " + semester);
        objectOutputStream.flush();

        // Get server reply
        ArrayList<Course> courses = (ArrayList<Course>) objectInputStream.readObject();

        // Check if there are available courses or not
        if (courses.isEmpty()) {
            labelClientFeedback.setText("No courses are available during the " + semester + " semester.");
        } else {
            labelClientFeedback.setText("Loaded the available courses for the " + semester + " semester.");
        }

        return courses;
    }

    /**
     * Sends a registration request to the server and notifies the user of the success of the registration.
     */
    @FXML
    private void onRegisterButtonClick() {
        try {
            String serverAnswer = register();
            // Print server answer
            displayInformationAlert(serverAnswer);
            labelClientFeedback.setText("Successful registration.");
        } catch (IllegalArgumentException e) {
            labelClientFeedback.setText(e.getMessage());
        } catch (SocketException | EOFException e) {
            // Handle the case where the server crashes without disconnecting
            displayErrorAlert(e.getMessage() + ", the connection to the server was lost. The client will exit.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a registration form to the server.
     *
     * @return The answer from the server.
     *
     * @throws IOException              If an I/O error occurs when dealing with the client input/output streams.
     * @throws ClassNotFoundException   If the returned String by the server is invalid.
     * @throws IllegalArgumentException Iif the {@link #createStudent() createStudent()} method throws the exception.
     */
    private String register() throws IOException, IllegalArgumentException, ClassNotFoundException {
        // Create course object
        Course course = courseTable.getSelectionModel().getSelectedItem();

        // Check that a course is selected
        if (course == null) {
            throw new IllegalArgumentException("No course is selected.");
        }

        // Create student object
        Student student = createStudent();

        // Create form object
        RegistrationForm form = new RegistrationForm(student, course);

        // Send command to the server
        objectOutputStream.writeObject(REGISTER_COMMAND);
        objectOutputStream.flush();

        // Send form to the server
        objectOutputStream.writeObject(form);
        objectOutputStream.flush();

        // Get the answer from the server
        return (String) objectInputStream.readObject();
    }

    /**
     * Displays an information alert to the user and and also broadcasts the information to the CLI output.
     *
     * @param message The error message.
     */
    private void displayInformationAlert(String message) {
        // Send information to CLI
        System.out.println(CLIENT_SUCCESS + message);

        // Create alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);

        // Give alert pane some CSS styling
        alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("MainWindowStyle.css")).toExternalForm());

        // Set alert window properties
        alert.setHeaderText("Information");
        alert.setTitle("Information");

        // Show alert
        alert.showAndWait();
    }

    /**
     * Creates a student object containing the information about a student.
     *
     * @return A student object.
     *
     * @throws IllegalArgumentException If the student information is invalid.
     */
    private Student createStudent() throws IllegalArgumentException {
        // Get first name
        String firstName = textFieldFirstName.getText();
        if (firstName.isBlank()) {
            throw new IllegalArgumentException("The first name is invalid.");
        }

        // Get last name
        String lastName = textFieldLastName.getText();
        if (lastName.isBlank()) {
            throw new IllegalArgumentException("The last name is invalid.");
        }

        // Get valid email
        String email = textFieldEmail.getText();
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("The email is invalid.");
        }

        // Get student ID
        String studentID = textFieldStudentID.getText();
        if (!isStudentIDValid(studentID)) {
            throw new IllegalArgumentException("The student ID is invalid.");
        }

        // Return student information
        return new Student(firstName, lastName, email, studentID);
    }

    /**
     * Allows the dragging of the application window.
     *
     * @param event An event representing the actions of the mouse.
     */
    @FXML
    private void onBorderPaneDragged(MouseEvent event) {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    /**
     * Saves the current coordinates of the application window.
     *
     * @param event An event representing the actions of the mouse.
     */
    @FXML
    private void onBorderPanePressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    /**
     * Initializes the client controller.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Fill choiceBox and set its default value
        for (String semester : semesters) {
            choiceBox.getItems().add(semester);
        }
        choiceBox.setValue(semesters[0]);

        // Set table placeholder text
        courseTable.setPlaceholder(new Label("No courses loaded"));

        // Initialize table columns
        courseTableColumnCode.setCellValueFactory(new PropertyValueFactory<>("Code"));
        courseTableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
    }
}