/*
 * Copyright (c) 2023. Etienne Collin #20237904
 */

package com.etiennecollin.tp2.clientFx;

import com.etiennecollin.tp2.server.Server;
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
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static com.etiennecollin.tp2.clientFx.Client.objectInputStream;
import static com.etiennecollin.tp2.clientFx.Client.objectOutputStream;

public class ClientController implements Initializable {
    private static final String[] semesters = new String[]{"Automne", "Hiver", "Ete"};
    @FXML
    private Label labelClientFeedback;
    @FXML
    private TableView<Course> courseTable;
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
     * Disconnects from the server and closes the application.
     */
    @FXML
    protected void onCloseButtonClick() {
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
    public void disconnect() throws IOException {
        objectOutputStream.writeObject(Server.DISCONNECT_COMMAND);
        objectOutputStream.flush();
        System.out.println("[Client] Disconnecting from server...");
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

    /**
     * Loads the available courses for the semester selected in the choiceBox.
     */
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
     * Sends a command to the server to load available courses for a specific semester.
     *
     * @param semester The semester to filter courses with.
     *
     * @return An ArrayList containing the available courses.
     *
     * @throws IOException            If an I/O error occurs when dealing with the client input/output streams.
     * @throws ClassNotFoundException If the returned Course object by the server is invalid.
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

    /**
     * Sends a registration request to the server and notifies the user of the success of the registration.
     */
    @FXML
    protected void onRegisterButtonClick() {
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
    public String register() throws IOException, IllegalArgumentException, ClassNotFoundException {
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
        objectOutputStream.writeObject(Server.REGISTER_COMMAND);
        objectOutputStream.flush();

        // Send form to the server
        objectOutputStream.writeObject(form);
        objectOutputStream.flush();

        // Get the answer from the server
        return (String) objectInputStream.readObject();
    }

    /**
     * Creates a student object containing the information about a student.
     *
     * @return A student object.
     *
     * @throws IllegalArgumentException If the email or student ID of the student is invalid.
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

    /**
     * Initializes the client controller.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
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
        tableColumnCode.setCellValueFactory(new PropertyValueFactory<>("Code"));
        tableColumnCourse.setCellValueFactory(new PropertyValueFactory<>("Name"));
    }
}