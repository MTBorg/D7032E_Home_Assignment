package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.events.AttackEvent;
import src.events.BuyRequestEvent;
import src.events.Event;
import src.events.LoseHealthEvent;
import src.game.cards.store.StoreCard;
import src.game.Effect;
import src.game.EventQueue;
import src.server.KingTokyoPowerUpServer;

public class AlienMetabolism extends StoreCard implements Observer {

  public AlienMetabolism() {
    super("Alien Metabolism", 1, false);
  }

  @Override
  public void update(Observable queue, Object event) {
    EventQueue eventQueue = (EventQueue) queue;
    Event e = (Event) event;
    if (e instanceof BuyRequestEvent) {
      BuyRequestEvent buyRequestEvent = (BuyRequestEvent) e;
      if (buyRequestEvent.getBuyer().hasCard(this.name)) {
        System.out.println(
          buyRequestEvent.getBuyer().name + " Applied card: " + this.name
        ); //TODO: Remove this eventually
        buyRequestEvent.cost -= 1; //Buying cards costs you 1 less [Energy].
      } else {
        System.out.println(
          "buyer " +
            buyRequestEvent.getBuyer().name +
            " does not have Alien Metabolism"
        );
      }
    }
  }
}
