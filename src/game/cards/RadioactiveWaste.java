package src.game.cards;

import java.util.ArrayList;
import src.game.cards.EvolutionCard;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

public class RadioactiveWaste extends EvolutionCard {

  public RadioactiveWaste() {
    super(
      "Radioactive Waste",
      "Gigazaur",
      EvolutionCard.CardDuration.Temporary
    );
  }

  public void executeEffect(GameState gameState, Monster executor) {
    System.out.println("Played radioactive waste");
    int gigazaur_index = 0;
    for (int i = 0; i < gameState.monsters.size(); i++) {
      if (gameState.monsters.get(i).getName() == this.getMonster()) {
        gigazaur_index = i;
        break;
      }
    }

    Monster gigazaur = gameState.monsters.get(gigazaur_index);
    String power = Server.sendMessage(
      gigazaur.stream,
      "POWERUP:Receive 2 energy and 1 health\n"
    );
    gigazaur.energy += 2;
    if (gigazaur.currentHealth + 1 >= gigazaur.getMaxHealth()) {
      gigazaur.currentHealth = gigazaur.getMaxHealth();
    } else {
      gigazaur.currentHealth += 1;
    }
  }
}
