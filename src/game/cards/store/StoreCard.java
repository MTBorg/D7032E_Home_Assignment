package src.game.cards.store;

import java.util.Observer;
import src.game.Card;
import src.game.Monster;

public abstract class StoreCard extends Card {
  public int cost;
  public boolean discard;

  public StoreCard(String name, int cost, boolean discard) {
    super(name);
    this.cost = cost;
    this.discard = discard;
  }

  public String toString() {
    //TODO: This should print the card description as well
    return (
      name +
      ", Cost " +
      cost +
      ", " +
      (this.discard ? "DISCARD" : "KEEP") +
      ", Effect "
    );
  }

  //Override this if the card should be executable (discard type)
  public void execute(Monster monster) {}
}
