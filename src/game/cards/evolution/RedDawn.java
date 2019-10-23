package src.game.cards.evolution;

import java.util.ArrayList;
import src.game.cards.evolution.TemporaryEvolutionCard;
import src.game.events.LoseHealthEvent;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

public class RedDawn extends TemporaryEvolutionCard {

  public RedDawn() {
    super("Red Dawn", "Kong");
  }

  public void effect(Monster executor, GameState gameState) {
    System.out.println("Played red dawn");
    int kong = 0;
    for (int i = 0; i < gameState.monsters.size(); i++) {
      if (gameState.monsters.get(i).getName() == this.getMonster()) {
        kong = i;
        break;
      }
    }
    String power = Server.sendMessage(
      gameState.monsters.get(kong).stream,
      "POWERUP:Deal 2 damage to all others\n"
    );
    for (int mon = 0; mon < gameState.monsters.size(); mon++) {
      if (mon != kong) {
        gameState.eventQueue.add(
          new LoseHealthEvent(
            gameState.eventQueue,
            gameState.monsters.get(mon),
            2
          ),
          gameState
        );
        gameState
          .eventQueue.get(gameState.eventQueue.size() - 1)
          .execute(gameState);
      // gameState.monsters.get(mon).currentHealth += -2;
      }
    }
  }
}
