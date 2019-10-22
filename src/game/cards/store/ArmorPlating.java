package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.game.cards.store.StoreCard;
import src.game.Effect;
import src.game.EventQueue;
import src.game.events.AttackEvent;
import src.game.events.Event;
import src.server.KingTokyoPowerUpServer;

public class ArmorPlating extends StoreCard implements Observer {

  public ArmorPlating() {
    super("Armor Plating", 1, false);
  }

  @Override
  public void update(Observable queue, Object event) {
    EventQueue eventQueue = (EventQueue) queue;
    Event e = (Event) event;
    if (e instanceof AttackEvent) {
      AttackEvent attackEvent = (AttackEvent) e;
      if (attackEvent.getAttacked().hasCard(this.name)) {
        System.out.println(
          attackEvent.getAttacked().name + " Applied card: " + this.name
        ); //TODO: Remove this eventually
        attackEvent.damage -= 1; //Ignore damage of 1.
      } else {
        System.out.println(
          "attacked " +
            attackEvent.getAttacked().name +
            " does not have Armor Plating"
        ); //TODO: Remove this eventually
      }
    }
  }
}
