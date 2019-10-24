package src.game;

import java.util.Observable;
import java.util.Observer;
import src.server.Server;

public abstract class Card {
  protected final String name;

  // public String description; //TODO: Add back eventually
  public Card(String name) {
    this.name = name;
  // this.description = description;
  }

  // public String toString() {
  //   return (
  //     name +
  //     ", Cost " +
  //     cost +
  //     ", " +
  //     (discard ? "DISCARD" : "KEEP") +
  //     ", Effect " +
  //     description
  //   );
  // }
  //
  public String getName() {
    return this.name;
  }

  protected void announcePlayed(Monster executor) {
    Server.broadCastMessage(
      executor.getName() + " played card " + this.name + "\n",
      executor
    );
    Server.sendOneWayMessage(
      executor.stream,
      "You played card " + this.name + "\n"
    );
    System.out.println(executor.getName() + " played card " + this.name + "\n");
  }
}
