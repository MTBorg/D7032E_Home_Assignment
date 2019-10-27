package src.game.events;

import java.util.ArrayList;
import src.game.EventQueue;
import src.game.events.Event;
import src.game.events.MonsterKilledEvent;
import src.game.GameState;
import src.game.Monster;

public class LoseHealthEvent implements Event {
  private Monster monster;
  private int loss;

  public LoseHealthEvent(Monster monster, int loss) {
    this.monster = monster;
    this.loss = loss;
  }

  public void execute(GameState gameState) {
    monster.setCurrentHealth(monster.getCurrentHealth() - loss);
    if (monster.getCurrentHealth() <= 0) {
      gameState.pushEvent(new MonsterKilledEvent(this.monster));
    }
  }
}
