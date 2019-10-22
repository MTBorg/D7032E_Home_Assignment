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
  private static final int PLAYER_AMOUNT = 2; //TODO: Change this back to two
  private static final int SERVER_SOCKET = 2048;

  public static void start() {
    System.out.println("Server started");

    List<String> monsterNames = Arrays.asList(
      new String[] { "Kong", "Gigazaur", "Alienoid" }
    );

    //Shuffle which monsters will played (if not all)
    Collections.shuffle(monsterNames);

    for (int i = 0; i < PLAYER_AMOUNT; i++) {
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
        " You are the monster: " + monsters.get(onlineClient).getName() + "\n"
      );
      monsters.get(onlineClient).stream = stream;

      System.out.println(
        "Connected to " + monsters.get(onlineClient).getName()
      );
    }
  }

  public static String sendMessage(Stream stream, String message) {
    Scanner sc = new Scanner(System.in);
    String response = "";
    stream.writeBytes(message);
    response = stream.readLine();
    return response;
  }

  public static void sendOneWayMessage(Stream stream, String message) {
    stream.writeBytes("Message:" + message);
  }

  public static void broadCastMessage(String message, Monster exception) {
    for (Monster monster : Server.monsters) {
      if (exception != null) {
        if (monster == exception) {
          continue; //Don't write to the exception
        }
      }
      sendOneWayMessage(monster.stream, message);
    }
  }
}
