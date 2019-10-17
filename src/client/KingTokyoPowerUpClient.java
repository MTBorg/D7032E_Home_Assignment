package src.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;

public class KingTokyoPowerUpClient {
  private final int SOCKET_HOST = 2048;
  private Scanner sc = new Scanner(System.in);
  private boolean bot;

  public KingTokyoPowerUpClient(boolean bot) {
    String name = "";
    this.bot = bot;

    //Server stuffs
    try {
      Socket aSocket = new Socket("localhost", SOCKET_HOST);
      DataOutputStream outToServer = new DataOutputStream(
        aSocket.getOutputStream()
      );
      BufferedReader inFromServer = new BufferedReader(
        new InputStreamReader(aSocket.getInputStream())
      );
      name = inFromServer.readLine();
      System.out.println(name);

      messageLoop(aSocket, outToServer, inFromServer);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  private void messageLoop(
    Socket aSocket,
    DataOutputStream outToServer,
    BufferedReader inFromServer
  )
    throws Exception {
    Random rnd = ThreadLocalRandom.current();
    while (true) {
      String[] message = inFromServer.readLine().split(":");
      for (int i = 0; i < message.length; i++) {
        System.out.println(message[i].toString());
      }
      if (message[0].equalsIgnoreCase("VICTORY")) {
        outToServer.writeBytes("Bye!\n");
      } else if (message[0].equalsIgnoreCase("ATTACKED")) {
        if (bot) outToServer.writeBytes("YES\n"); else {
          outToServer.writeBytes(sc.nextLine() + "\n");
        }
      } else if (message[0].equalsIgnoreCase("ROLLED")) {
        if (bot) {
          rnd = ThreadLocalRandom.current();
          int num1 = rnd.nextInt(2) + 4;
          int num2 = rnd.nextInt(2) + 1;
          String reroll = "" + num1 + "," + num2 + "\n";
          outToServer.writeBytes(reroll); // Some randomness at least
        } else {
          outToServer.writeBytes(sc.nextLine() + "\n");
        }
      } else if (message[0].equalsIgnoreCase("PURCHASE")) {
        if (bot) outToServer.writeBytes("-1\n"); else outToServer.writeBytes(
          sc.nextLine() + "\n"
        );
      } else {
        if (bot) outToServer.writeBytes("OK\n"); else {
          System.out.println("Press [ENTER]");
          sc.nextLine();
          outToServer.writeBytes("OK\n");
        }
      }
      System.out.println("\n");
    }
  }

  public static void main(String argv[]) {
    KingTokyoPowerUpClient client;
    if (argv.length != 0) client = //Syntax: java KingTokyoPowerUpClient bot
      new KingTokyoPowerUpClient(true); else client =
      new KingTokyoPowerUpClient(false);
  }
}
