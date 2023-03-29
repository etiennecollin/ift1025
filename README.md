# ift1025-tp2

This is the repository containing my term project for the IFT1025 Programming 2 class at Université de Montréal.

This project involves creating a Java client-server application that allows students to register for courses.

- The server and the CLI client were implemented
- A GUI client was added using JavaFX
- The server implements multithreading to support connecting to multiple clients at once
- Everything was properly documented using JavaDoc

## Execution

To execute the JAR and to make sure the data files are properly found, launch your favourite terminal and **make sure that you are inside the repository directory** such that your environment variable `$PWD=.../ift1025-tp2/`. The data files required for the server to properly run are in the `/ift1025-tp2/data/` directory.

To run the jar files, use the command `java -jar jars/theFile.jar`.

### Example

First, launch the server:
```bash
java -jar jars/server.jar
```

Then, either launch the CLI client:
```bash
java -jar jars/clientCLI.jar
```

Or launch the GUI client:
```bash
java -jar jars/clientGUI.jar
```
