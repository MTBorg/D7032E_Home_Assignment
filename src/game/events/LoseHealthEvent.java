package src.game.events;

import java.util.ArrayList;
import src.game.EventQueue;
import src.game.events.Event;
import src.game.GameState;
import src.game.Monster;

public class LoseHealthEvent extends Event {
  private Monster monster;
  private int loss;

  public LoseHealthEvent(EventQueue queue, Monster monster, int loss) {
    super(queue);
    this.monster = monster;
    this.loss = loss;
  }

  public void execute(GameState gameState) {
    monster.setCurrentHealth(monster.getCurrentHealth() - loss);
  }
}
