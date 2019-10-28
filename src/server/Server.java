package src.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import src.game.Game;
import src.game.Monster;
import src.network.Stream;

public class Server {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    // TODO code application logic here
    // https://www.youtube.com/watch?v=HqdOaAzPtek
    // https://boardgamegeek.com/thread/1408893/categorizing-cards
    Server.start();
  }

  private static ArrayList<Stream> connections = new ArrayList<Stream>();
  private static final int PLAYER_AMOUNT = 2;
  private static final int SERVER_SOCKET = 2048;

  public static void start() {
    System.out.println("Server started");

    try {
      waitForPlayers();
    } catch (Exception e) {
      System.out.println(e);
    }

    Game game = new Game(connections);
    game.loop();
  }

  private static void waitForPlayers() throws Exception {
    ServerSocket aSocket = new ServerSocket(SERVER_SOCKET);

    //assume two online clients
    for (int onlineClient = 0; onlineClient < PLAYER_AMOUNT; onlineClient++) {
      System.out.println(
        "Waiting for " + (PLAYER_AMOUNT - onlineClient) + " more players"
      );
      Socket connectionSocket = aSocket.accept();
      Stream stream = new Stream(connectionSocket);

      stream.writeBytes("Connected to server\n");

      connections.add(stream);
      System.out.println("Connected to player");
    }
  }

  public static String sendQuestion(Stream stream, String message) {
    if (stream == null) return "";
    Scanner sc = new Scanner(System.in);
    String response = "";
    stream.writeBytes(message);
    response = stream.readLine();
    return response;
  }

  public static void sendMessage(Stream stream, String message) {
    if (stream != null) stream.writeBytes("Message:" + message);
  }

  public static void broadCastMessage(String message, Monster exception) {
    for (Stream stream : Server.connections) {
      if (exception != null) {
        if (stream == exception.stream) {
          continue; //Don't write to the exception
        }
      }
      sendMessage(stream, message);
    }
  }
}
