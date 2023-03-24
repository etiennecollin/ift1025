module clientFx {
    requires javafx.controls;
    requires javafx.fxml;
    requires server;
    requires clientSimple;

    requires org.controlsfx.controls;

    opens com.etiennecollin.tp2.clientFx to javafx.fxml;
    exports com.etiennecollin.tp2.clientFx;
}