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
import src.game.Monsters;

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

  private ArrayList<Monsters> monsters = new ArrayList<Monsters>();
  private final int PLAYER_AMOUNT = 2;
  private final int SERVER_SOCKET = 2048;

  public KingTokyoPowerUpServer() {
    Monsters kong = new Monsters("Kong");
    Monsters gigazaur = new Monsters("Gigazaur");
    Monsters alien = new Monsters("Alienoid");
    monsters.add(kong);
    monsters.add(gigazaur);
    monsters.add(alien);

    //Shuffle which player is which monster
    Collections.shuffle(monsters);

    //Server stuffs
    try {
      ServerSocket aSocket = new ServerSocket(SERVER_SOCKET);

      //assume two online clients
      for (int onlineClient = 0; onlineClient < PLAYER_AMOUNT; onlineClient++) {
        Socket connectionSocket = aSocket.accept();
        BufferedReader inFromClient = new BufferedReader(
          new InputStreamReader(connectionSocket.getInputStream())
        );
        DataOutputStream outToClient = new DataOutputStream(
          connectionSocket.getOutputStream()
        );
        outToClient.writeBytes(
          "You are the monster: " + monsters.get(onlineClient).name + "\n"
        );
        monsters.get(onlineClient).connection = connectionSocket;
        monsters.get(onlineClient).inFromClient = inFromClient;
        monsters.get(onlineClient).outToClient = outToClient;
        System.out.println("Connected to " + monsters.get(onlineClient).name);
      }
    } catch (Exception e) {}

    //Shuffle the starting order
    Collections.shuffle(monsters);
    Game game = new Game(monsters);
    game.loop();
  }

  public static String sendMessage(Monsters recipient, String message) {
    Scanner sc = new Scanner(System.in);
    Monsters aMonster = recipient;
    String response = "";
    if (aMonster.connection != null) {
      try {
        aMonster.outToClient.writeBytes(message);
        response = aMonster.inFromClient.readLine();
      } catch (Exception e) {}
    } else {
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
