package src.game.cards.store;

import java.util.Observer;
import src.game.Card;

public abstract class StoreCard extends Card {
  public int cost;
  public boolean discard;

  public StoreCard(String name, int cost, boolean discard) {
    super(name);
    this.cost = cost;
    this.discard = discard;
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
}
