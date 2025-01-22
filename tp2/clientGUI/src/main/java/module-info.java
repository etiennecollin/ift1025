/**
 * The clientGUI module contains the necessary classes to launch a GUI client, connect it to a specific server port and
 * start its execution.
 * <p>
 * {@link com.etiennecollin.tp2.clientGUI} provides the necessary methods to launch a GUI client, connect it to a
 * specific server port and start its execution.
 */
module clientGUI {
    requires javafx.controls;
    requires javafx.fxml;
    requires server;

    opens com.etiennecollin.tp2.clientGUI to javafx.fxml;
    exports com.etiennecollin.tp2.clientGUI;
}