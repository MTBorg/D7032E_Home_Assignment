package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.events.AttackEvent;
import src.events.Event;
import src.events.LoseHealthEvent;
import src.game.cards.store.StoreCard;
import src.game.Effect;
import src.game.EventQueue;
import src.server.KingTokyoPowerUpServer;

public class AlphaMonster extends StoreCard implements Observer {

  public AlphaMonster() {
    super("Alpha Monster", 5, false);
  }

  @Override
  public void update(Observable queue, Object event) {
    EventQueue eventQueue = (EventQueue) queue;
    Event e = (Event) event;
    if (e instanceof AttackEvent) {
      AttackEvent attackEvent = (AttackEvent) e;
      if (attackEvent.getAttacker().hasCard(this.name)) {
        System.out.println(
          attackEvent.getAttacker().name + " Applied card: " + this.name
        ); //TODO: Remove this eventually
        attackEvent.getAttacker().stars++; //Gain 1[Star] when you attack.
      } else {
        System.out.println(
          "Attacker " +
            attackEvent.getAttacker().name +
            " does not have AlphaMonster"
        );
      }
    }
  }
}
