package src.game.events;

import java.util.ArrayList;
import src.game.EventQueue;
import src.game.events.Event;
import src.game.events.LoseHealthEvent;
import src.game.Monster;
import src.server.Server;

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
    Server.sendOneWayMessage(
      attacker.stream,
      "You attacked " + attacked.name + " for " + damage + " damage\n"
    );
    Server.sendOneWayMessage(
      attacked.stream,
      "You were attacked by " + attacker.name + " for " + damage + " damage\n"
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
