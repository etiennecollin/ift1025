/**
 * The clientCLI module contains the necessary classes to launch a CLI client, connect it to a specific server port
 * and start its execution.
 * <p>
 * {@link com.etiennecollin.tp2.clientCLI} provides the necessary methods to launch a CLI client, connect it to a
 * specific server port and start its execution.
 */
module clientCLI {
    requires server;
    exports com.etiennecollin.tp2.clientCLI;
}