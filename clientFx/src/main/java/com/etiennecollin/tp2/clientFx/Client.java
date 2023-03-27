/*
 * Copyright (c) 2023. Etienne Collin #20237904
 */

package com.etiennecollin.tp2.clientFx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import com.etiennecollin.tp2.server.Server;
import com.etiennecollin.tp2.server.models.Course;
import com.etiennecollin.tp2.server.models.RegistrationForm;
import com.etiennecollin.tp2.server.models.Student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Client extends Application {
    private static Socket client;
    protected static ObjectInputStream objectInputStream;
    protected static ObjectOutputStream objectOutputStream;

    /**
     * Runs the client, allowing the user to interact with the server.
     *
     * @param port The port number of the server to connect to.
     *
     * @throws IOException            If the methods {@link #launch(String...) launch()} or
     *                                {@link #cleanup() cleanup()} throw the exception or when dealing with the client
     *                                input/output streams.
     * @throws ClassNotFoundException If the method {@link #launch(String...) launch()} throws the exception.
     */
    public static void run(int port) throws IOException, ClassNotFoundException {
        // Connect to the server
        client = new Socket("localhost", port);

        // Create streams to write/read to/from the server
        objectOutputStream = new ObjectOutputStream(client.getOutputStream());
        objectInputStream = new ObjectInputStream(client.getInputStream());

        // Run GUI
        launch();

        // Close the streams and client
        cleanup();
    }

    /**
     * Closes the objectOutputStream, objectInputStream, and client.
     *
     * @throws IOException If an I/O error occurs when closing the streams or the client socket.
     */
    public static void cleanup() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

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
}
