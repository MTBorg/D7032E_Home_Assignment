package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.game.cards.store.StoreCard;
import src.game.Dice;
import src.game.EventQueue;
import src.game.EventQueue;
import src.game.events.DiceRollEvent;
import src.game.events.Event;
import src.server.Server;

public class CompleteDestruction extends StoreCard implements Observer {

  public CompleteDestruction() {
    super(
      "Complete Destruction",
      "If you roll [1][2][3][Heart][Attack][Energy] gain 9[Star] in addition to the regular results.",
      3
    );
  }

  @Override
  public void update(Observable queue, Object notification) {
    EventQueue eventQueue = (EventQueue) queue;
    EventQueue.EventNotification eventNotification = (EventQueue.EventNotification) notification;
    Event e = eventNotification.event;
    if (e instanceof DiceRollEvent) {
      DiceRollEvent rollEvent = (DiceRollEvent) e;
      boolean correctInput = true;
      for (int i = 0; i < 6; i++) {
        if (!rollEvent.getDices().contains(new Dice(i))) correctInput = false;
      }
      if (correctInput) {
        System.out.println(
          rollEvent.getRoller().getName() +
            " applied card " +
            this.getName() +
            " and was awarded 9 stars"
        );
        rollEvent.getRoller().stars += 9;
      }
    }
  }
}
