package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.game.cards.store.KeepCard;
import src.game.EventQueue;
import src.game.events.BuyRequestEvent;
import src.game.events.Event;

public class AlienMetabolism extends KeepCard implements Observer {

  public AlienMetabolism() {
    super("Alien Metabolism", 1);
  }

  @Override
  public void update(Observable queue, Object notification) {
    EventQueue eventQueue = (EventQueue) queue;
    EventQueue.EventNotification eventNotification = (EventQueue.EventNotification) notification;
    Event e = eventNotification.event;
    if (e instanceof BuyRequestEvent) {
      BuyRequestEvent buyRequestEvent = (BuyRequestEvent) e;
      if (buyRequestEvent.getBuyer().hasCard(this.getName())) {
        announcePlayed(buyRequestEvent.getBuyer());
        buyRequestEvent.cost -= 1; //Buying cards costs you 1 less [Energy].
      }
    }
  }
}
