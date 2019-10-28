package src.game.cards.store;

import java.util.Observer;
import src.game.cards.Card;
import src.game.cards.store.DiscardCard;
import src.game.GameState;
import src.game.Monster;

public abstract class StoreCard extends Card {
  private final int cost;

  public StoreCard(String name, String description, int cost) {
    super(name, description);
    this.cost = cost;
  }

  public String toString() {
    //TODO: This should print the card description as well
    return (
      this.name +
      ", Cost " +
      cost +
      ", " +
      (this.isDiscardCard() ? "DISCARD" : "KEEP") +
      ", Effect " +
      this.description
    );
  }

  /**
   * Return whether or not the card is a discard card.
   */
  public boolean isDiscardCard() {
    return this instanceof DiscardCard;
  }

  /**
   * Return the cost of the cards
   */
  public int getCost() {
    return this.cost;
  }
}
