package questionmodel;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Deck implements Serializable{
    List<Card> cards = new ArrayList<>();

    /**
     * Constructs a new instance of Deck using questions from the specified file path.
     * Reads and shuffles the questions to initialize the deck.
     *
     * @param path The path to the file containing questions and answers.
     * @throws IOException Thrown if an input/output error occurs during file reading.
     */
    public Deck(Path path) throws IOException {
        // Convert the Path to a File
        File questions = new File(String.valueOf(path));

        // Read and parse questions from the file to initialize the deck
        readQuestions(questions);

        // Shuffle the cards to randomize the order
        Collections.shuffle(cards);
    }


    /**
     * Retrieves a card from the deck in a synchronized manner.
     * Removes the card from the front of the deck and adds it back to the end.
     *
     * @return The card retrieved from the deck.
     */
    public synchronized Card getCard() {
        // Remove the card from the front of the deck
        Card card = cards.remove(0);

        // Add the card back to the end of the deck
        cards.add(cards.size() - 1, card);

        // Return the retrieved card
        return card;
    }


    /**
     * Reads and parses questions from a specified file, populating the deck with Card objects.
     *
     * @param file The file containing the questions and answers.
     * @throws IOException Thrown if an input/output error occurs during file reading.
     */
    private void readQuestions(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            // Read lines from the file, parse content, and create Card objects
            cards = reader.lines()
                    .map(line -> {
                        String[] content = line.split(";");
                        return new Card(content[0], new String[]{content[1], content[2], content[3], content[4]}, content[5], content[6]);
                    })
                    .collect(Collectors.toList());
        }
    }

}
