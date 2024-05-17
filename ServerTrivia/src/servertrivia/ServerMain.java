package servertrivia;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerMain {

    static List<ServerThread> players = new ArrayList<>();

    public static void main(String[] args){

        int numPlayers;
        int numPlayersConnected = 0;
        final int PORT = 2000;
        Scanner sc = new Scanner(new InputStreamReader(System.in));

        if(args.length > 0){
            numPlayers = Integer.parseInt(args[0]);
        }else{
            System.out.print("Number of players: ");
            numPlayers = Integer.parseInt(sc.nextLine());
        }

        try(ServerSocket server = new ServerSocket(PORT)) {
            while(numPlayersConnected != numPlayers){
                Socket socket = server.accept();
                players.add(new ServerThread(socket));
                numPlayersConnected++;
            }

            players.forEach(Thread::start);
            System.out.println("Connected");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Retrieves the statistics for all players in the game.
     * Marks the player with the highest correct answers as the winner.
     *
     * @return A string containing the general statistics for all players in the game.
     */
    public static String getStatistics() {
        StringBuilder generalStatistics = new StringBuilder();

        // Mark the player with the highest correct answers as the winner
        players.stream()
                .max(Comparator.comparingInt(player -> player.correctAnswer))
                .ifPresent(winner -> winner.gameWon = true);

        // Iterate through each player and append their statistics to the StringBuilder
        players.forEach(player ->
                generalStatistics.append("Name: ").append(player.playerName)
                        .append(" Correct: ").append(player.correctAnswer)
                        .append(" Incorrect: ").append(player.incorrectAnswer)
                        .append("\n")
        );

        // Return the general statistics as a string
        return generalStatistics.toString();
    }

}
