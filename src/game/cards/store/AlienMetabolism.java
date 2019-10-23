package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.game.cards.store.StoreCard;
import src.game.Effect;
import src.game.EventQueue;
import src.game.events.BuyRequestEvent;
import src.game.events.Event;

public class AlienMetabolism extends StoreCard implements Observer {

  public AlienMetabolism() {
    super("Alien Metabolism", 1);
  }

  @Override
  public void update(Observable queue, Object event) {
    EventQueue eventQueue = (EventQueue) queue;
    Event e = (Event) event;
    if (e instanceof BuyRequestEvent) {
      BuyRequestEvent buyRequestEvent = (BuyRequestEvent) e;
      if (buyRequestEvent.getBuyer().hasCard(this.getName())) {
        System.out.println(
          buyRequestEvent.getBuyer().getName() +
            " Applied card: " +
            this.getName()
        ); //TODO: Remove this eventually
        buyRequestEvent.cost -= 1; //Buying cards costs you 1 less [Energy].
      }
    }
  }
}
