package src.game.events;

import java.util.ArrayList;
import src.game.EventQueue;
import src.game.events.Event;
import src.game.events.LoseHealthEvent;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

public class AttackEvent implements Event {
  private Monster attacker;
  Monster attacked;
  public int damage;

  public AttackEvent(Monster attacker, Monster attacked, int damage) {
    this.attacker = attacker;
    this.attacked = attacked;
    this.damage = damage;
  }

  public void execute(GameState gameState) {
    Server.sendOneWayMessage(
      attacker.stream,
      "You attacked " + attacked.getName() + " for " + damage + " damage\n"
    );
    Server.sendOneWayMessage(
      attacked.stream,
      "You were attacked by " +
        attacker.getName() +
        " for " +
        damage +
        " damage\n"
    );
    gameState.pushEvent(new LoseHealthEvent(attacked, damage));
  }

  public Monster getAttacker() {
    return this.attacker;
  }

  public Monster getAttacked() {
    return this.attacked;
  }
}
