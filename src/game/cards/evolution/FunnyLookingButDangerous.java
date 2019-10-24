package src.game.cards.evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import src.game.cards.evolution.PermanentEvolutionCard;
import src.game.Dice;
import src.game.EventQueue;
import src.game.EventQueue.EventNotification;
import src.game.events.DiceRollEvent;
import src.game.events.Event;
import src.game.events.LoseHealthEvent;
import src.game.Monster;
import src.server.Server;

public class FunnyLookingButDangerous extends PermanentEvolutionCard {

  public FunnyLookingButDangerous() {
    super("Funny Looking But Dangerous", "Alienoid");
  }

  public void update(Observable queue, Object notification) {
    EventQueue eventQueue = (EventQueue) queue;
    EventQueue.EventNotification eventNotification = (EventQueue.EventNotification) notification;
    Event event = eventNotification.event;
    if (event instanceof DiceRollEvent) {
      DiceRollEvent rollEvent = (DiceRollEvent) event;
      if (
        rollEvent.getRoller().hasCard(this.name) &&
        Collections.frequency(rollEvent.getDices(), new Dice(2)) >= 3
      ) {
        Server.broadCastMessage(
          rollEvent.getRoller().getName() + " played card " + this.name + "\n",
          rollEvent.getRoller()
        );
        Server.sendOneWayMessage(
          rollEvent.getRoller().stream,
          "You played card " + this.name + "\n"
        );

        for (Monster monster : eventNotification.gameState.monsters) {
          if (monster != rollEvent.getRoller()) {
            eventNotification.gameState.pushEvent(
              new LoseHealthEvent(monster, 1)
            );
          }
        }
      }
    }
  }
}
