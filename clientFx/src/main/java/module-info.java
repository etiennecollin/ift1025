/**
 * The clientFx module contains the necessary classes to launch a GUI client, connect it to a specific server port and
 * start its execution.
 * <p>
 * {@link com.etiennecollin.tp2.clientFx} provides the necessary methods to launch a GUI client, connect it to a
 * specific server port and start its execution.
 */
module clientFx {
    requires javafx.controls;
    requires javafx.fxml;
    requires server;
    requires clientSimple;

    requires org.controlsfx.controls;

    opens com.etiennecollin.tp2.clientFx to javafx.fxml;
    exports com.etiennecollin.tp2.clientFx;
}