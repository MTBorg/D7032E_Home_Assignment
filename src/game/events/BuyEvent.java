package src.game.events;

import java.util.ArrayList;
import src.game.cards.store.StoreCard;
import src.game.EventQueue;
import src.game.events.Event;
import src.game.Monster;
import src.server.Server;

public class BuyEvent extends Event {
  private Monster buyer;
  private int cost;
  private StoreCard card;

  public BuyEvent(EventQueue queue, Monster buyer, StoreCard card, int cost) {
    super(queue);
    this.buyer = buyer;
    this.card = card;
    this.cost = cost;
  }

  public void execute() {
    String message =
      "bought card \"" +
        this.card.getName() +
        "\" for " +
        this.cost +
        " energy\n";
    Server.broadCastMessage("Player " + this.buyer.name + message, this.buyer); // Send to other players
    System.out.println("Player " + this.buyer.name + message); // Print on server
    Server.sendOneWayMessage(this.buyer.stream, "You " + message); // Send to buyer

    this.buyer.giveCard(card);
    this.queue.addObserver(card);
    this.buyer.energy -= this.cost;
  }
}
