package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.game.cards.store.StoreCard;
import src.game.EventQueue;
import src.game.events.AttackEvent;
import src.game.events.Event;
import src.game.events.LoseHealthEvent;
import src.server.Server;

public class AlphaMonster extends KeepCard implements Observer {

  public AlphaMonster() {
    super("Alpha Monster", 1);
  }

  @Override
  public void update(Observable queue, Object notification) {
    EventQueue eventQueue = (EventQueue) queue;
    EventQueue.EventNotification eventNotification = (EventQueue.EventNotification) notification;
    Event e = eventNotification.event;
    if (e instanceof AttackEvent) {
      AttackEvent attackEvent = (AttackEvent) e;
      if (attackEvent.getAttacker().hasCard(this.getName())) {
        announcePlayed(attackEvent.getAttacker());
        attackEvent.getAttacker().stars++; //Gain 1[Star] when you attack.
      }
    }
  }
}
