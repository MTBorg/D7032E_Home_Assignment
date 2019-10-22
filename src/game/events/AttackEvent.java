package src.game.events;

import java.util.ArrayList;
import src.game.EventQueue;
import src.game.events.Event;
import src.game.events.LoseHealthEvent;
import src.game.Monster;

public class AttackEvent extends Event {
  private Monster attacker;
  Monster attacked;
  public int damage;

  public AttackEvent(
    EventQueue queue,
    Monster attacker,
    Monster attacked,
    int damage
  ) {
    super(queue);
    this.attacker = attacker;
    this.attacked = attacked;
    this.damage = damage;
  }

  public void execute() {
    System.out.println(
      "You attacked " + attacked.name + " for " + damage + " damage"
    );
    this.queue.add(new LoseHealthEvent(this.queue, attacked, damage));
    this.queue.get(this.queue.size() - 1).execute();
  }

  public Monster getAttacker() {
    return this.attacker;
  }

  public Monster getAttacked() {
    return this.attacked;
  }
}
