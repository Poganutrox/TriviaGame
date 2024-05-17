package com.miguelangel.triviafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import questionmodel.*;


import java.io.*;
import java.net.*;

public class Controller {
    
    @FXML
    private RadioButton rbAnswerPass;
    @FXML
    private VBox boxQuestion;
    @FXML
    private HBox boxButtons;
    @FXML
    private VBox boxStatistics;
    @FXML
    private Button btnSendAnswer;
    @FXML
    private Label lbGlobalStatistics;
    @FXML
    private Label lbResult;
    @FXML
    private ToggleGroup tgAnswers;
    @FXML
    private TextField tfHost;
    @FXML
    private TextField tfPort;
    @FXML
    private TextField tfPlayer;
    @FXML
    private Label labQuestion;
    @FXML
    private RadioButton rbAnswer1;
    @FXML
    private RadioButton rbAnswer2;
    @FXML
    private RadioButton rbAnswer3;
    @FXML
    private RadioButton rbAnswer4;
    InetAddress inetAddress = null;
    String host = "";
    String port = "";
    ObjectInputStream objInput = null;
    ObjectOutputStream objOutput = null;
    Socket service = null;
    int correctAnswers = 0;
    int incorrectAnswers = 0;


    /**
     * Handles the click event on the button to send an answer to the server.
     *
     * @param actionEvent The action event that triggers the function.
     * @throws IOException            Thrown if an input/output error occurs during communication with the server.
     * @throws ClassNotFoundException Thrown if the expected class is not found during deserialization.
     */
    public void onClickSendAnswer(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        // Check if an answer is selected
        if (tgAnswers.getSelectedToggle().isSelected()) {
            // Get the selected response
            String response = getSelectedResponse();

            // Send the response to the server
            objOutput.writeObject(response);

            // Get the response from the server
            String answer = (String) getObjectFromServer();

            // Handle the server response
            if (answer.equals("end")) {
                // If the response indicates the end, display global statistics and disable the send answer button
                String globalStatistics = (String) getObjectFromServer();
                lbGlobalStatistics.setText(globalStatistics);
                btnSendAnswer.setDisable(true);
            } else {
                // Handle "yes" and "no" responses
                switch (answer) {
                    case "yes" -> {
                        // Increment the correct answers count and update the global statistics label
                        correctAnswers += 1;
                        lbGlobalStatistics.setText("Correct");
                    }
                    case "no" -> {
                        // Increment the incorrect answers count and update the global statistics label
                        incorrectAnswers += 1;
                        lbGlobalStatistics.setText("Incorrect");
                    }
                    case "passed" -> {
                        lbGlobalStatistics.setText("Passed");
                    }
                }

                // Get a card from the server and print it
                Card card = (Card) getObjectFromServer();
                printCard(card);
            }

            // Update the result label with the count of correct and incorrect answers
            lbResult.setText("Correct: " + correctAnswers + " Incorrect: " + incorrectAnswers);
        } else {
            // Show an error message if no answer is selected
            showError("An answer must be selected");
        }
    }


    /**
     * Handles the click event on the button to stop playing.
     *
     * @param actionEvent The action event that triggers the function.
     * @throws IOException            Thrown if an input/output error occurs during communication with the server.
     * @throws ClassNotFoundException Thrown if the expected class is not found during deserialization.
     */
    public void onClickStopPlaying(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        // Send "end" to the server to indicate stopping the game
        objOutput.writeObject("end");

        // Retrieve the "end" message sent by the server
        String end = (String) getObjectFromServer();

        // Retrieve the final statistics sent by the server and update the global statistics label
        String globalStatistics = (String) getObjectFromServer();
        lbGlobalStatistics.setText(globalStatistics);

        // Disable the send answer button
        btnSendAnswer.setDisable(true);
    }


    /**
     * Handles the click event on the button to connect to the server.
     *
     * @param actionEvent The action event that triggers the function.
     */
    public void onClickConnect(ActionEvent actionEvent) {
        // Extract host and port information from the input fields
        host = tfHost.getText().toLowerCase().trim();
        port = tfPort.getText().toLowerCase().trim();

        // Get the player's name from the input field
        String playerName = tfPlayer.getText();

        // Validate input fields
        if (host.isEmpty() || port.isEmpty()) {
            // Show error if host and port are not provided
            showError("The host and the port must be provided");
        } else if (playerName.isEmpty()) {
            // Show error if player's name is not provided
            showError("The name of the player must be provided");
        } else {
            try {
                // Initialize network connections with the server
                initializeConnections();

                // Make the UI visible to the user
                makeUIVisible();

                // Send the name of the player to the server
                objOutput.writeObject(playerName);

                // Receive the first card from the server
                Card initialCard = (Card) getObjectFromServer();

                // Print the initial card
                printCard(initialCard);

            } catch (IOException e) {
                // Show error if unable to establish a connection with the server
                showError("Impossible to establish the connection with the server");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Receives an object from the server through the input stream.
     *
     * @return The object received from the server.
     * @throws ClassNotFoundException Thrown if the expected class is not found during deserialization.
     * @throws IOException            Thrown if an input/output error occurs during communication with the server.
     */
    private Object getObjectFromServer() throws ClassNotFoundException, IOException {
        return objInput.readObject();
    }


    /**
     * Retrieves the text of the selected response radio button.
     *
     * @return The text of the selected response radio button, or an empty string if no response is selected.
     */
    private String getSelectedResponse() {
        if (rbAnswer1.isSelected()) return rbAnswer1.getText();
        if (rbAnswer2.isSelected()) return rbAnswer2.getText();
        if (rbAnswer3.isSelected()) return rbAnswer3.getText();
        if (rbAnswer4.isSelected()) return rbAnswer4.getText();
        if(rbAnswerPass.isSelected()) return rbAnswerPass.getText();
        return "";
    }


    /**
     * Makes specific UI components visible to the user.
     * This method sets the visibility of question vbox, buttons hbox, and statistics vbox to true.
     */
    private void makeUIVisible() {
        boxQuestion.setVisible(true);
        boxButtons.setVisible(true);
        boxStatistics.setVisible(true);
    }


    /**
     * Displays the content of a Card object on the user interface.
     *
     * @param card The Card object containing the question and answer options to be displayed.
     */
    private void printCard(Card card) {
        // Set the question text on the label
        labQuestion.setText(card.getQuestion());

        // Set the answer options on the radio buttons
        String[] options = card.getOptions();
        rbAnswer1.setText(options[0]);
        rbAnswer2.setText(options[1]);
        rbAnswer3.setText(options[2]);
        rbAnswer4.setText(options[3]);
    }


    /**
     * Displays an error message in an alert dialog box.
     *
     * @param message The error message to be shown in the alert.
     */
    private void showError(String message) {
        // Create an alert dialog of type ERROR with the specified error message
        Alert alert = new Alert(Alert.AlertType.ERROR, message);

        // Display the alert and wait for user interaction
        alert.showAndWait();
    }


    /**
     * Initializes network connections to the specified host and port.
     * Establishes a socket connection, ObjectOutputStream, and ObjectInputStream for communication with the server.
     *
     * @throws IOException Thrown if an input/output error occurs during the initialization of network connections.
     */
    private void initializeConnections() throws IOException {
        // Determine the InetAddress based on the provided host information
        if (host.equals("localhost")) {
            inetAddress = InetAddress.getLocalHost();
        } else {
            inetAddress = InetAddress.getByName(host);
        }

        // Create a socket connection to the specified host and port
        service = new Socket(inetAddress, Integer.parseInt(port));

        // Initialize ObjectOutputStream and ObjectInputStream for communication with the server
        objOutput = new ObjectOutputStream(service.getOutputStream());
        objInput = new ObjectInputStream(service.getInputStream());
    }

}