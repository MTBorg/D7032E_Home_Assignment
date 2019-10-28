package src.game.events;

import java.util.ArrayList;
import src.game.events.Event;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

public class MonsterKilledEvent implements Event {
  private Monster monster;

  public MonsterKilledEvent(Monster monster) {
    this.monster = monster;
  }

  public void execute(GameState gameState) {
    Server.broadCastMessage(
      "Monster " + this.monster.getName() + " was killed\n",
      this.monster
    );
    Server.sendMessage(this.monster.stream, "You died, Game over!");
    System.out.println("Monster " + this.monster.getName() + " has died");
    gameState.monsters.remove(this.monster);
  }
}
