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
import java.util.Random;
import java.util.Scanner;
import src.game.Game;
import src.game.Monster;
import src.network.Stream;

public class KingTokyoPowerUpServer {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    // TODO code application logic here
    // https://www.youtube.com/watch?v=HqdOaAzPtek
    // https://boardgamegeek.com/thread/1408893/categorizing-cards
    new KingTokyoPowerUpServer();
  }

  private ArrayList<Monster> monsters = new ArrayList<Monster>();
  private final int PLAYER_AMOUNT = 2; //TODO: Change this back to two
  private final int SERVER_SOCKET = 2048;

  public KingTokyoPowerUpServer() {
    System.out.println("Server started");

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

  private void waitForPlayers() throws Exception {
    Monster kong = new Monster("Kong");
    Monster gigazaur = new Monster("Gigazaur");
    Monster alien = new Monster("Alienoid");
    monsters.add(kong);
    monsters.add(gigazaur);
    monsters.add(alien);

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
        System.out.println(theMessage[i].toString());
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
}
