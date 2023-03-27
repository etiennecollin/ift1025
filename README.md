# ift1025-tp2

This is the repository containing my term project for the IFT1025 Programming 2 class at Université de Montréal.

This project involves creating a Java client-server application that allows students to register for courses.

- The server and the CLI client were implemented
- A GUI client was added using JavaFX
- The server implements multithreading to support connecting to multiple clients at once
- Everything was properly documented using JavaDoc

## Execution

To execute the JAR and to make sure the data files are properly found, make sure that you are inside the repository directory such that your environment variable `$PWD=.../ift1025-tp2/`.

To run the jar files, use the command `java -jar theFile.jar`. To launch the clientFx (with the GUI), simply double-click the `.jar` file.

### Example

To launch the server:
```bash
java -jar target/jar/server.jar
```

To launch the CLI client:
```bash
java -jar target/jar/clientSimple.jar
```

To lunch the GUI client, double click jar file or:
```bash
java -jar target/jar/clientFx.jar
```