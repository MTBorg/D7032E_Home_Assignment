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

  public BuyRequestEvent(Monster buyer, StoreCard card) {
    super(null);
    this.buyer = buyer;
    this.card = card;
    this.cost = card.getCost();
  }

  public void execute(GameState gameState) {
    if (buyer.energy >= card.getCost()) { //TODO: This should probably use this.cose instead?
      gameState.pushEvent(new BuyEvent(buyer, card, this.cost));
    }
  }

  public Monster getBuyer() {
    return this.buyer;
  }
}
