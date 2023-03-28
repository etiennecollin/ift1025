/*
 * Copyright (c) 2023. Etienne Collin #20237904
 */

package com.etiennecollin.tp2.clientFx;

import com.etiennecollin.tp2.server.Server;
import com.etiennecollin.tp2.server.models.Course;
import com.etiennecollin.tp2.server.models.RegistrationForm;
import com.etiennecollin.tp2.server.models.Student;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static com.etiennecollin.tp2.clientFx.Client.objectInputStream;
import static com.etiennecollin.tp2.clientFx.Client.objectOutputStream;

public class ClientController implements Initializable {
    private static final String[] semesters = new String[]{"Automne", "Hiver", "Ete"};
    @FXML
    private Label labelClientFeedback;
    @FXML
    private TableView<Course> table;
    @FXML
    private ImageView btnClose;
    @FXML
    private ImageView btnHide;
    @FXML
    private Pane btnLoad;
    @FXML
    private Pane btnRegister;
    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private BorderPane borderPane;
    private double x = 0;
    private double y = 0;
    @FXML
    private TableColumn<Course, String> tableColumnCode;
    @FXML
    private TableColumn<Course, String> tableColumnCourse;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldStudentID;

    // TODO verify and create javadoc

    /**
     * Closes the application.
     */
    @FXML
    protected void onCloseButtonClick() {
        try {
            disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }

    /**
     * Disconnects the client from the server by sending a DISCONNECT_COMMAND to the server through the objectOutputStream.
     *
     * @throws IOException If an I/O error occurs when writing to the objectOutputStream.
     */
    public void disconnect() throws IOException {
        objectOutputStream.writeObject(Server.DISCONNECT_COMMAND);
        objectOutputStream.flush();
        labelClientFeedback.setText("[Client] Disconnecting from server...");
    }

    /**
     * Displays an error message prompt and broadcasts the error message to the CLI error output.
     *
     * @param message The error message.
     */
    protected void displayErrorAlert(String message) {
        System.err.println("[Client] " + message);
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }

    protected void displayInformationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }

    /**
     * Sends the application the taskbar.
     */
    @FXML
    protected void onHideButtonClick() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void onLoadButtonClick() {
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
     * Sends a command to the server to load available courses with the option to filter for a specific semester.
     *
     * @param semester The semester to filter courses with.
     *
     * @return A list containing the available courses.
     *
     * @throws IOException              If an I/O error occurs when dealing with the client input/output streams.
     * @throws IllegalArgumentException If the command has an incorrect number of arguments.
     * @throws ClassNotFoundException   If the returned Course object by the server is invalid.
     */
    public ArrayList<Course> getCourses(String semester) throws IOException, ClassNotFoundException {
        // Send command to server
        objectOutputStream.writeObject(Server.LOAD_COMMAND + " " + semester);
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

    @FXML
    protected void onRegisterButtonClick() {
        String serverAnswer = null;
        try {
            serverAnswer = register();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Print server answer
        labelClientFeedback.setText("[Client]" + serverAnswer);
    }

    /**
     * Sends a registration form to the server.
     *
     * @return The answer from the server.
     *
     * @throws IOException            If an I/O error occurs when dealing with the client input/output streams.
     * @throws ClassNotFoundException If the returned String by the server is invalid.
     */
    public String register() throws IOException, ClassNotFoundException {
        // Create student and course objects
        Student student = createStudent();

        // TODO get values of course from table in GUI
        Course course = new Course("Programmation2", "IFT1025", "Automne");

        // Create form
        RegistrationForm form = new RegistrationForm(student, course);

        // Send command to the server
        objectOutputStream.writeObject(Server.REGISTER_COMMAND);
        objectOutputStream.flush();

        // Send form to the server
        objectOutputStream.writeObject(form);
        objectOutputStream.flush();

        // Get the answer from the server
        return (String) objectInputStream.readObject();
    }

    /**
     * Creates an array of strings containing the information about a student.
     *
     * @return A student object.
     */
    public Student createStudent() throws IllegalArgumentException {
        // Get first name
        String firstName = textFieldFirstName.getText();

        // Get last name
        String lastName = textFieldLastName.getText();

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
     * Validates an email.
     * <p>
     * Source of pattern: <a href="https://stackoverflow.com/a/8204716">StackOverflow</a>
     *
     * @param email The email to validate.
     *
     * @return Whether the email is valid or not.
     */
    public boolean isEmailValid(String email) {
        // Generate email regex pattern
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(email).matches();
    }

    /**
     * Validates a student ID.
     *
     * @param studentID The student ID to validate.
     *
     * @return Whether the student ID is valid or not.
     */
    public boolean isStudentIDValid(String studentID) {
        // Generate email regex pattern
        Pattern pattern = Pattern.compile("^\\d{8}$");
        return pattern.matcher(studentID).matches();
    }

    /**
     * Allows the dragging of the application window.
     *
     * @param event An event representing the actions of the mouse.
     */
    @FXML
    protected void onBorderPaneDragged(MouseEvent event) {
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
    protected void onBorderPanePressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Fill choicebox and set its default value
        for (String semester : semesters) {
            choiceBox.getItems().add(semester);
        }
        choiceBox.setValue(semesters[0]);
    }
}