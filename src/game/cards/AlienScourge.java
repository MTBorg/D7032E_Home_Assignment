package src.game.cards;

import java.util.ArrayList;
import src.game.cards.EvolutionCard;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

public class AlienScourge extends EvolutionCard {

  public AlienScourge() {
    super("Alien Scourge", "Alienoid", EvolutionCard.CardDuration.Temporary);
  }

  public void executeEffect(GameState gameState, Monster executor) {
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
