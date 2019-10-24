package src.game.events;

import src.game.EventQueue;
import src.game.events.Event;
import src.game.GameState;
import src.game.Monster;

public class GainHealthEvent extends Event {
  public final Monster monster;
  public int amount;

  public GainHealthEvent(Monster monster, int amount) {
    super(null);
    this.monster = monster;
    this.amount = amount;
  }

  public void execute(GameState gameState) {
    monster.setCurrentHealth(monster.getCurrentHealth() + amount);
  }
}
