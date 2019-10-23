package src.game.cards.evolution;

import java.util.ArrayList;
import src.game.cards.evolution.TemporaryEvolutionCard;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

public class AlienScourge extends TemporaryEvolutionCard {

  public AlienScourge() {
    super("Alien Scourge", "Alienoid");
  }

  public void effect(Monster executor, GameState gameState) {
    System.out.println("Played " + this.getName());
    int alienoid = 0;
    for (int i = 0; i < gameState.monsters.size(); i++) {
      if (gameState.monsters.get(i).getName() == this.getMonster()) {
        alienoid = i;
        break;
      }
    }
    String power = Server.sendMessage(
      gameState.monsters.get(alienoid).stream,
      "POWERUP:Receive 2 stars\n"
    );
    gameState.monsters.get(alienoid).stars += 2;
  }
}
