/*
 * Copyright (c) 2023. Etienne Collin #20237904 & Justin Villeneuve #20132792
 */

package com.etiennecollin.tp2.clientFx;

import com.etiennecollin.tp2.server.Server;
import com.etiennecollin.tp2.server.models.Course;
import com.etiennecollin.tp2.server.models.RegistrationForm;
import com.etiennecollin.tp2.server.models.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

import static com.etiennecollin.tp2.clientFx.Client.objectInputStream;
import static com.etiennecollin.tp2.clientFx.Client.objectOutputStream;

public class ClientController {
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
    public ArrayList<Course> getCourses(String semester) throws IOException, IllegalArgumentException, ClassNotFoundException {
        // Send command to server
        if (semester.equals("")) {
            objectOutputStream.writeObject(Server.LOAD_COMMAND);
        } else {
            objectOutputStream.writeObject(Server.LOAD_COMMAND + " " + semester);
        }
        objectOutputStream.flush();

        // Get server reply
        ArrayList<Course> courses = (ArrayList<Course>) objectInputStream.readObject();

        // Check if there are available courses or not
        if (courses.isEmpty()) {
            // Check if a semester was provided
            if (semester.equals("")) {
                labelClientFeedback.setText("[Client] No courses are available.");
            } else {
                labelClientFeedback.setText("[Client] No courses are available during the " + semester + " semester.");
            }
        }

        return courses;
    }

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
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Print available courses if there are any
        if (!courses.isEmpty()) {
            // TODO Add courses to table
            labelClientFeedback.setText("[Client] " + courses);
        }
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
     * @throws IOException            If the method {@link #courseSelectionMenu(Scanner) courseSelectionMenu()} throws the exception or if an I/O error occurs when dealing with the client input/output streams.
     * @throws ClassNotFoundException If the method {@link #courseSelectionMenu(Scanner) courseSelectionMenu()} throws the exception or if the returned String by the server is invalid.
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
    public Student createStudent() {
        // Get first name
        String firstName = textFieldFirstName.getText();

        // Get last name
        String lastName = textFieldLastName.getText();

        // Get valid email
        String email;
        do {
            email = textFieldEmail.getText();
            // TODO break/return and send prompt that email is invalid
        } while (!isEmailValid(email));

        // Get student ID
        String studentID;
        do {
            studentID = textFieldStudentID.getText();
            // TODO break/return and send prompt that ID is invalid
        } while (!isStudentIDValid(studentID));

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
        boolean isValid = pattern.matcher(email).matches();

        // Print message in case of invalid email
        if (!isValid) {
            labelClientFeedback.setText("[Client] The email is invalid.");
            // TODO send a prompt telling the user that the email is invalid just like in demo
        }

        return isValid;
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
        boolean isValid = pattern.matcher(studentID).matches();

        // Print message in case of invalid student ID
        if (!isValid) {
            labelClientFeedback.setText("[Client] The student ID is invalid.");
            // TODO send a prompt telling the user that the student ID is invalid just like in demo
        }

        return isValid;
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

    public void initialize() {
        // Fill choicebox and set its default value
        for (String semester : semesters) {
            choiceBox.getItems().add(semester);
        }
        choiceBox.setValue(semesters[0]);
    }
}