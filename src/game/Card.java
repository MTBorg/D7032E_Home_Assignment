package src.game;

import java.util.Observable;
import java.util.Observer;

public abstract class Card implements Observer {
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

  // Override this if the card needs to receive updates about the event queue
  public void update(Observable o, Object arg) {}
}
