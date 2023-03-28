/**
 * The server module contains the necessary classes to launch a server on a specified port and start its execution.
 * <p>
 * {@link com.etiennecollin.tp2.server} provides the necessary methods to launch a server on a specified port and start
 * its execution.
 * <p>
 * {@link com.etiennecollin.tp2.server.models} provides the necessary classes to create RegistrationForm, Student and
 * Course objects.
 */
module server {
    requires javafx.base;
    exports com.etiennecollin.tp2.server;
    exports com.etiennecollin.tp2.server.models;
}