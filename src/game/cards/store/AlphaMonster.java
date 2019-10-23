package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.game.cards.store.StoreCard;
import src.game.Effect;
import src.game.EventQueue;
import src.game.events.AttackEvent;
import src.game.events.Event;
import src.game.events.LoseHealthEvent;
import src.server.Server;

public class AlphaMonster extends StoreCard implements Observer {

  public AlphaMonster() {
    super("Alpha Monster", 1);
  }

  @Override
  public void update(Observable queue, Object event) {
    EventQueue eventQueue = (EventQueue) queue;
    Event e = (Event) event;
    if (e instanceof AttackEvent) {
      AttackEvent attackEvent = (AttackEvent) e;
      if (attackEvent.getAttacker().hasCard(this.getName())) {
        System.out.println(
          attackEvent.getAttacker().getName() +
            " Applied card: " +
            this.getName()
        ); //TODO: Remove this eventually
        attackEvent.getAttacker().stars++; //Gain 1[Star] when you attack.
      } else {
        System.out.println(
          "Attacker " +
            attackEvent.getAttacker().getName() +
            " does not have AlphaMonster"
        );
      }
    }
  }
}
