package servertrivia;

import questionmodel.Card;
import questionmodel.Deck;

import java.io.*;

import java.net.Socket;
import java.nio.file.Path;

public class ServerThread extends Thread {
    private final ObjectOutputStream objOutput;
    private final ObjectInputStream objInput;

    /**
     * Constructs a new instance of ServerThread with the provided socket.
     * Initializes ObjectOutputStream and ObjectInputStream for communication with the client.
     *
     * @param socket The socket representing the connection with the client.
     * @throws RuntimeException Thrown if an input/output error occurs during the initialization of network connections.
     */
    public ServerThread(Socket socket) {
        try {
            objOutput = new ObjectOutputStream(socket.getOutputStream());
            objInput = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static final Deck deck;
    public static boolean endMarked = false;
    public int correctAnswer = 0;
    public int incorrectAnswer = 0;
    public String playerName = "";
    public boolean gameWon = false;
    public static String generalStatistics = "";

    static {
        try {
            deck = new Deck(Path.of("cards"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        //Receiving the name of the player
        try {
            playerName = (String) objInput.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Game loop: Continue until the game is marked as ended
        while (!endMarked) {
            try {
                // Get a card from the deck and send it to the player
                Card card = deck.getCard();
                objOutput.writeObject(card);

                // Receive the player's response
                String response = (String) objInput.readObject();

                // Process the player's response based on different scenarios
                switch (response) {
                    case "Pass" -> {
                        if (!endMarked) {
                            objOutput.writeObject("passed");
                        }
                    }
                    case "end" -> {
                        endMarked = true;
                    }

                    default -> {
                        if (!endMarked) {
                            // Check if the response is correct and update statistics
                            if (response.equals(card.getCorrectAnswer())) {
                                objOutput.writeObject("yes");
                                correctAnswer += 1;
                            } else {
                                objOutput.writeObject("no");
                                incorrectAnswer += 1;
                            }
                        }
                    }
                }

                // If the game is marked as ended, send final statistics to the player
                if (endMarked) {
                    objOutput.writeObject("end");
                    generalStatistics = ServerMain.getStatistics();
                    String winner = gameWon ? "You win" : "You lose";
                    String finalStatistics = winner + "\n" +
                            generalStatistics;

                    objOutput.writeObject(finalStatistics);
                }

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}