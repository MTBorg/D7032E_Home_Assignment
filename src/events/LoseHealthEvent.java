package src.events;

import java.util.ArrayList;
import src.events.Event;
import src.game.EventQueue;
import src.game.Monster;

public class LoseHealthEvent extends Event {
  private Monster monster;
  private int loss;

  public LoseHealthEvent(EventQueue queue, Monster monster, int loss) {
    super(queue);
    this.monster = monster;
    this.loss = loss;
  }

  public void execute() {
    monster.currentHealth -= loss;
  }
}
