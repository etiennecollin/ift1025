# ift1025-tp2

This is the repository containing my term project for the IFT1025 Programming 2 class at Université de Montréal.

This project involves creating a Java client-server application that allows students to register for courses.

- The server and the CLI client were implemented
- A GUI client was added using JavaFX
- The server implements multithreading to support connecting to multiple clients at once
- Everything was properly documented using JavaDoc

## Execution

To execute the JAR and to make sure the data files are properly found, make sure that you are inside the repository directory such that your environment variable `$PWD=.../ift1025-tp2/`.

To run the jar files, use the command `java -jar jars/theFile.jar`.

### Example

To launch the server:
```bash
java -jar jars/server.jar
```

To launch the CLI client:
```bash
java -jar jars/clientCLI.jar
```

To lunch the GUI client:
```bash
java -jar jars/clientGUI.jar
```