package src.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;
import src.network.Stream;

public class KingTokyoPowerUpClient {
  private final int SOCKET_PORT = 2048;
  private Scanner sc = new Scanner(System.in);
  private boolean bot;

  public KingTokyoPowerUpClient(boolean bot) {
    String name = "";
    this.bot = bot;

    //Server stuffs
    try {
      Stream stream = new Stream(new Socket("localhost", SOCKET_PORT));
      name = stream.readLine();

      System.out.println(name);

      messageLoop(stream);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  private void messageLoop(Stream stream) throws Exception {
    Random rnd = ThreadLocalRandom.current();
    while (true) {
      String[] message = stream.readLine().split(":");
      for (int i = 0; i < message.length; i++) {
        // Don't print message tag
        // TODO: This is pretty hacky
        if (i != 0 || !message[0].equalsIgnoreCase("MESSAGE")) {
          System.out.println(message[i].toString());
        }
      }
      if (message[0].equalsIgnoreCase("VICTORY")) {
        stream.writeBytes("Bye!\n");
      } else if (message[0].equalsIgnoreCase("ATTACKED")) {
        if (bot) stream.writeBytes("YES\n"); else {
          stream.writeBytes(sc.nextLine() + "\n");
        }
      } else if (message[0].equalsIgnoreCase("ROLLED")) {
        if (bot) {
          rnd = ThreadLocalRandom.current();
          int num1 = rnd.nextInt(2) + 4;
          int num2 = rnd.nextInt(2) + 1;
          String reroll = "" + num1 + "," + num2 + "\n";
          stream.writeBytes(reroll); // Some randomness at least
        } else {
          stream.writeBytes(sc.nextLine() + "\n");
        }
      } else if (message[0].equalsIgnoreCase("PURCHASE")) {
        if (bot) stream.writeBytes("-1\n"); else stream.writeBytes(
          sc.nextLine() + "\n"
        );
      } else if (message[0].equalsIgnoreCase("MESSAGE")) {
        // TODO: This is a hack
      // If there is a  message do nothing
      } else {
        if (bot) stream.writeBytes("OK\n"); else {
          System.out.println("Press [ENTER]");
          sc.nextLine();
          stream.writeBytes("OK\n");
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
