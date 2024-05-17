# Trivia Game Project using Sockets and JavaFX

This project implements a trivia game using sockets for client-server communication and JavaFX for the client's graphical interface. The game allows multiple players to connect to a server and answer trivia questions in real-time.

## Table of Contents

1. [Project Setup](#project-setup)
2. [Model Project](#model-project)
   - [Card Class](#card-class)
   - [Deck Class](#deck-class)
3. [Server Project](#server-project)
   - [ServerMain Class](#servermain-class)
   - [ServerThread Class](#serverthread-class)
4. [Client Project](#client-project)
   - [Main Class](#main-class)
   - [FXML and Controller](#fxml-and-controller)
5. [Using QuestionModel Project](#using-questionmodel-project)
6. [Launching Multiple Client Instances](#launching-multiple-client-instances)

## Project Setup

We need to create three projects:

- **Server**: `ServerTrivia`, a Java project with a single package `servertrivia`.
- **Client**: `TriviaFX`, a JavaFX application with a single package `triviafx`, including the main class, controller, and FXML file in the `resources` folder.
- **Model**: `QuestionModel`, shared between the server and client, with a `questionmodel` package.

## Model Project

### Card Class

The `Card` class contains:

- Three `String` attributes: `question`, `correctAnswer`, and `category`.
- An array of 4 `String` options.

### Deck Class

The `Deck` class manages a list of `Card` objects and has methods to get and shuffle the cards:

- Constructor with a `Path` parameter that reads a `.txt` file with questions, adds them to the list, and shuffles.
- Method to get a `Card`, removing it from the beginning and adding it to the end.

## Server Project

### ServerMain Class

Configures the server to accept connections from a specified number of players and manages connections using `ServerThread`.

### ServerThread Class

Manages each client connection:

1. Creates a deck of cards (using `Deck`).
2. Sends a card to the client and waits for a response.
3. Verifies the response and sends confirmation.
4. Ends the game when a client requests and sends results to all clients.

## Client Project

### Main Class

Starts the JavaFX application and loads the FXML file.

### FXML and Controller

The `trivia-view.fxml` file defines the client interface. The controller handles interface events such as sending answers and ending the game.

### Functionality

1. Specify the server address, port, and player name.
2. Connect to the server.
3. Receive and respond to questions.
4. End the game and display results.

![image](https://github.com/Poganutrox/TriviaGame/assets/63597815/01a0ee8f-dde4-45e5-b2f2-974afd3035aa)

![image](https://github.com/Poganutrox/TriviaGame/assets/63597815/3945616f-a0b0-4c69-aba5-bd7cb6a64e51)

![image](https://github.com/Poganutrox/TriviaGame/assets/63597815/db923c4b-b0f2-4c07-bcab-c4cdcff04c46)




