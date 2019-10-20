package src.game;

import java.util.Observer;

public abstract class Card implements Observer {
  public String name;
  public int cost;
  public boolean discard;
  public Effect effect;
  public String description;

  public Card(
    String name,
    int cost,
    boolean discard,
    Effect effect,
    String description
  ) {
    this.name = name;
    this.cost = cost;
    this.discard = discard;
    this.effect = effect;
    this.description = description;
  }

  public String toString() {
    return (
      name +
      ", Cost " +
      cost +
      ", " +
      (discard ? "DISCARD" : "KEEP") +
      ", Effect " +
      description
    );
  }
}
