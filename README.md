# ift1025-tp2 <!-- omit from toc -->

## Table of Contents <!-- omit from toc -->

- [Description](#description)
- [Compatibility](#compatibility)
- [Launching the server](#launching-the-server)
- [Launching the clients](#launching-the-clients)
    - [CLI client](#cli-client)
    - [GUI client](#gui-client)
- [Evaluation](#evaluation)
    - [Bonus](#bonus)
    - [Demo](#demo)
    - [GitHub](#github)
    - [JavaDoc](#javadoc)
    - [Team](#team)

## Description

This is the repository containing my term project for the IFT1025 Programming 2 class at Université de Montréal.

This project involves creating a Java client-server application that allows students to register for courses.

- The server and the CLI client were implemented
- A GUI client was added using JavaFX
- The server implements multithreading to support connecting to multiple clients at once
- Everything was properly documented using JavaDoc

## Compatibility

- Java JDK ≥ 20
  - _This project was compiled using Java JDK 20 with the JDK 19 feature set due to an incompatibility between JDK 19's JavaFX and Apple M1 Silicon as of 2022/03/27._

The JARs were generated and tested on Apple M1 Silicon, running macOS 13.3.1 (22E261), with the following Java version:

```bash
> java --version
openjdk 20 2023-03-21
OpenJDK Runtime Environment (build 20+36-2344)
OpenJDK 64-Bit Server VM (build 20+36-2344, mixed mode, sharing)
```

## Launching the server

To make sure the data files are properly found, launch your favourite terminal and **make sure that you are inside the repository directory**, such that your environment variable `$PWD=.../ift1025-tp2/`.

_The data files required for the server to properly run are in the `.../ift1025-tp2/data/` directory._

Then, once in the right directory, launch the server with:

```bash
java -jar jars/server.jar
```

## Launching the clients

You may execute the clients from anywhere (the value of `$PWD` does not matter). Assuming that `$PWD=.../ift1025-tp2/`, then the commands to launch the clients are:

### CLI client

```bash
java -jar jars/clientCLI.jar
```

### GUI client

```bash
java -jar jars/clientGUI.jar
```

## Evaluation

### Bonus

Server.java

- Translated javadoc to english
- Translated server messages to english
- Implemented Multithreading:
  - Only keeping the run() method in the Server class and putting the rest into a new ClientHandler.java class
  - The run() method accepts connections from clients and passes them to ClientHandler objects that implement Runnable.
    These objects are run in independent threads.
- Made the server "static" by removing its constructor and passing the server port directly to the run() method.
  Done because the server launcher will never launch more than one server.
- Added error handling
- Changed accessibility of methods/classes/fields

ServerLauncher.java

- Modified server startup message
- When the server class was made "static", the ServerLauncher was modified to simply do Server.run(PORT) to start a server
- Made the server port private as it is independently modified from the client port
- Changed accessibility of methods/classes/fields

ClientHandler.java

- Modified server messages and the way it sends information to the client
- run() continuously listens to the client without disconnections until the server receives a DISCONNECT commands
- Added error handling
- The now server accepts a DISCONNECT command to cleanly disconnect from a client
- Changed accessibility of methods/classes/fields

Course.java

- Uniformized code by translating it to english (variables, toString() method return, etc)

RegistrationForm.java

- Uniformized code by translating it to english (variables, toString() method return, etc)
- A Student class was created, and the form constructor was modified to only require a Student and a Course object:
  - Accordingly, the toString() method was modified to return the Student and Course objects which both have a toString() method
  - The setters were only kept for the student and course parameters passed to the RegistrationForm
  - The getters were all kept. For example, getName() used to return this.name, whereas it now returns this.student.getName()

Student.java

- The Student class was created to simplify and make clearer some procedures.

### Demo

- Link to demo: https://drive.google.com/file/d/1XeY425WgHUs8P1RyUsaz3EsGp8wnkEkV/view?usp=sharing

### GitHub

- GitHub Repository: https://github.com/etiennecollin/ift1025-tp2/

### JavaDoc

- Available at https://etiennecollin.com/ift1025-tp2/

_The JavaDoc is placed inside `.../ift1025-tp2/docs/`._

### Team

- Etienne Collin | 20237904
