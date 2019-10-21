package src.events;

import java.util.ArrayList;
import src.events.Event;
import src.game.cards.store.StoreCard;
import src.game.EventQueue;
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

  public void execute() {
    System.out.println(
      this.buyer.name +
        " wants to buy card " +
        this.card.getName() +
        " for " +
        this.cost +
        " energy"
    ); //TODO: Remove this eventually

    if (buyer.energy >= card.cost) {
      this.queue.add(new BuyEvent(this.queue, buyer, card, cost));
    }
    this.queue.get(this.queue.size() - 1).execute();
  }

  public Monster getBuyer() {
    return this.buyer;
  }
}
