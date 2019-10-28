package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.game.cards.store.StoreCard;
import src.game.EventQueue;
import src.game.events.AttackEvent;
import src.game.events.Event;

public class ArmorPlating extends KeepCard implements Observer {

  public ArmorPlating() {
    super("Armor Plating", 1);
  }

  @Override
  public void update(Observable queue, Object notification) {
    EventQueue eventQueue = (EventQueue) queue;
    EventQueue.EventNotification eventNotification = (EventQueue.EventNotification) notification;
    Event e = eventNotification.event;
    if (e instanceof AttackEvent) {
      AttackEvent attackEvent = (AttackEvent) e;
      if (attackEvent.getAttacked().hasCard(this.getName())) {
        announcePlayed(attackEvent.getAttacked());
        attackEvent.damage -= 1; //Ignore damage of 1.
      }
    }
  }
}
