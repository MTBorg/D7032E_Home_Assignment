package src.events;

import java.util.ArrayList;
import src.events.Event;
import src.game.cards.store.StoreCard;
import src.game.EventQueue;
import src.game.Monster;

public class BuyEvent extends Event {
  private Monster buyer;
  private int cost;
  private StoreCard card;

  public BuyEvent(EventQueue queue, Monster buyer, StoreCard card, int cost) {
    super(queue);
    this.buyer = buyer;
    this.card = card;
    this.cost = card.cost;
  }

  public void execute() {
    System.out.println(
      this.buyer.name +
        " buys card " +
        this.card.getName() +
        " for " +
        this.cost +
        " energy"
    ); //TODO: Remove this eventually

    this.buyer.giveCard(card);
    this.buyer.energy -= this.cost;
  }
}
