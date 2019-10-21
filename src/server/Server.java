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

  private static ArrayList<Monster> monsters = new ArrayList<Monster>();
  private static final int PLAYER_AMOUNT = 1; //TODO: Change this back to two
  private static final int SERVER_SOCKET = 2048;

  public static void start() {
    System.out.println("Server started");

    List<String> monsterNames = Arrays.asList(
      new String[] { "Kong", "Gigazaur", "Alienoid" }
    );

    //Shuffle which monsters will played (if not all)
    Collections.shuffle(monsterNames);

    for (int i = 0; i <= PLAYER_AMOUNT; i++) {
      monsters.add(new Monster(monsterNames.get(i)));
    }

    //Shuffle which player is which monster
    Collections.shuffle(monsters);

    //Server stuffs
    try {
      waitForPlayers();
    } catch (Exception e) {
      System.out.println(e);
    }

    //Shuffle the starting order
    Collections.shuffle(monsters);

    Game game = new Game(monsters);
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

      stream.writeBytes(
        " You are the monster: " + monsters.get(onlineClient).name + "\n"
      );
      monsters.get(onlineClient).stream = stream;

      System.out.println("Connected to " + monsters.get(onlineClient).name);
    }
  }

  public static String sendMessage(Stream stream, String message) {
    Scanner sc = new Scanner(System.in);
    String response = "";
    if (stream != null) {
      try {
        stream.writeBytes(message);
        response = stream.readLine();
      } catch (Exception e) {
        System.out.println(e);
      }
    } else { //If null send to localhost
      String[] theMessage = message.split(":");
      for (int i = 0; i < theMessage.length; i++) {
        // Don't print message tag
        if (i != 0 || !theMessage[0].equalsIgnoreCase("MESSAGE")) {
          System.out.println(theMessage[i].toString());
        }
      }
      if (
        !(
          theMessage[0].equals("ATTACKED") ||
          theMessage[0].equals("ROLLED") ||
          theMessage[0].equals("PURCHASE")
        )
      ) System.out.println("Press [ENTER]");
      response = sc.nextLine();
    }
    return response;
  }

  public static void sendOneWayMessage(Stream stream, String message) {
    if (stream != null) {
      try {
        stream.writeBytes(message);
      } catch (Exception e) {
        System.out.println(e);
      }
    } else { //If null send to localhost
      String[] theMessage = message.split(":");
      for (int i = 0; i < theMessage.length; i++) {
        // Don't print message tag
        if (i != 0 || !theMessage[0].equalsIgnoreCase("MESSAGE")) {
          System.out.println(theMessage[i].toString());
        }
      }
    }
  }
}
