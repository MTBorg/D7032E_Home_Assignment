package src.game.events;

import src.game.EventQueue;
import src.game.events.Event;
import src.game.Monster;

public class GainHealthEvent extends Event {
  public final Monster monster;
  public int amount;

  public GainHealthEvent(EventQueue queue, Monster monster, int amount) {
    super(queue);
    this.monster = monster;
    this.amount = amount;
  }

  public void execute() {
    monster.setCurrentHealth(monster.getCurrentHealth() + amount);
  }
}