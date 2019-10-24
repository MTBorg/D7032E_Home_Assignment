package src.game.events;

import java.util.ArrayList;
import src.game.cards.store.KeepCard;
import src.game.cards.store.StoreCard;
import src.game.EventQueue;
import src.game.events.Event;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

public class BuyEvent implements Event {
  private Monster buyer;
  private int cost;
  private StoreCard card;

  public BuyEvent(Monster buyer, StoreCard card, int cost) {
    this.buyer = buyer;
    this.card = card;
    this.cost = cost;
  }

  public void execute(GameState gameState) {
    String message =
      " bought card " + this.card.getName() + " for " + this.cost + " energy";
    Server.broadCastMessage(
      "Player " + this.buyer.getName() + message + "\n",
      this.buyer
    ); // Send to other players
    System.out.println("Player " + this.buyer.getName() + message); // Print on server
    Server.sendOneWayMessage(this.buyer.stream, "You" + message + "\n"); // Send to buyer

    this.buyer.giveCard(card);
    if (card instanceof KeepCard) gameState.addEventObserver((KeepCard) card);
    this.buyer.energy -= this.cost;
  }
}
