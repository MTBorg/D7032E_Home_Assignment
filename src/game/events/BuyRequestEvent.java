package src.game.events;

import java.util.ArrayList;
import src.game.cards.store.StoreCard;
import src.game.EventQueue;
import src.game.events.Event;
import src.game.GameState;
import src.game.Monster;

public class BuyRequestEvent extends Event {
  private Monster buyer;
  public int cost;
  private StoreCard card;

  public BuyRequestEvent(EventQueue queue, Monster buyer, StoreCard card) {
    super(queue);
    this.buyer = buyer;
    this.card = card;
    this.cost = card.cost;
  }

  public void execute(GameState gameState) {
    if (buyer.energy >= card.cost) {
      this.queue.add(
          new BuyEvent(this.queue, buyer, card, this.cost),
          gameState
        );
      this.queue.get(this.queue.size() - 1).execute(gameState);
    }
  }

  public Monster getBuyer() {
    return this.buyer;
  }
}
