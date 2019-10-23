package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.game.cards.store.StoreCard;
import src.game.Dice;
import src.game.Effect;
import src.game.EventQueue;
import src.game.EventQueue;
import src.game.events.DiceRollEvent;
import src.game.events.Event;
import src.server.Server;

public class CompleteDestruction extends StoreCard implements Observer {

  public CompleteDestruction() {
    super("Complete Destruction", 1);
  }

  @Override
  public void update(Observable queue, Object event) {
    EventQueue eventQueue = (EventQueue) queue;
    Event e = (Event) event;
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
