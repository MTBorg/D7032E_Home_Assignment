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

  public KingTokyoPowerUpClient(boolean bot) {
    String name = "";

    //Server stuffs
    try {
      Stream stream = new Stream(new Socket("localhost", SOCKET_PORT));
      name = stream.readLine();

      System.out.println(name);

      if (bot) {
        messageLoopBot(stream);
      } else {
        messageLoop(stream);
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  private void messageLoopBot(Stream stream) {
    Random rnd = ThreadLocalRandom.current();
    while (true) {
      String[] message = stream.readLine().split(":");
      printMessage(message);
      switch (message[0].toUpperCase()) {
        case "VICTORY":
          stream.writeBytes("Bye!\n");
          break;
        case "ATTACKED":
          stream.writeBytes("YES\n");
          break;
        case "ROLLED":
          rnd = ThreadLocalRandom.current();
          int num1 = rnd.nextInt(2) + 4;
          int num2 = rnd.nextInt(2) + 1;
          String reroll = "" + num1 + "," + num2 + "\n";
          stream.writeBytes(reroll); // Some randomness at least
          break;
        case "PURCHASE":
          stream.writeBytes("-1\n");
          break;
        case "MESSAGE":
          break;
        default:
          stream.writeBytes("OK\n");
          break;
      }
      System.out.println("\n");
    }
  }

  private void messageLoop(Stream stream) throws Exception {
    Random rnd = ThreadLocalRandom.current();
    while (true) {
      String[] message = stream.readLine().split(":");
      printMessage(message);
      switch (message[0].toUpperCase()) {
        case "VICTORY":
          stream.writeBytes("Bye!\n");
          break;
        case "ATTACKED":
        case "ROLLED":
        case "PURCHASE":
          stream.writeBytes(sc.nextLine() + "\n");
          break;
        case "MESSAGE":
          break;
        default:
          System.out.println("Press [ENTER]");
          sc.nextLine();
          stream.writeBytes("OK\n");
          break;
      }
      System.out.println("\n");
    }
  }

  private void printMessage(String[] message) {
    for (int i = 0; i < message.length; i++) {
      // Don't print message tag
      // TODO: This is pretty hacky
      if (i != 0 || !message[0].equalsIgnoreCase("MESSAGE")) {
        System.out.println(message[i].toString());
      }
    }
  }

  public static void main(String argv[]) {
    KingTokyoPowerUpClient client;
    if (argv.length != 0) client = //Syntax: java KingTokyoPowerUpClient bot
      new KingTokyoPowerUpClient(true); else client =
      new KingTokyoPowerUpClient(false);
  }
}
