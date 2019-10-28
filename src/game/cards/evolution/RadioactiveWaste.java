package src.game.cards.evolution;

import java.util.ArrayList;
import src.game.cards.evolution.EvolutionCard;
import src.game.events.GainHealthEvent;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

public class RadioactiveWaste extends TemporaryEvolutionCard {

  public RadioactiveWaste() {
    super("Radioactive Waste", "Gain 2 ENERGY and 1 HEART", "Gigazaur");
  }

  public void effect(Monster executor, GameState gameState) {
    int gigazaur_index = 0;
    for (int i = 0; i < gameState.monsters.size(); i++) {
      if (gameState.monsters.get(i).getName() == this.getMonster()) {
        gigazaur_index = i;
        break;
      }
    }

    Monster gigazaur = gameState.monsters.get(gigazaur_index);
    String power = Server.sendQuestion(
      gigazaur.stream,
      "POWERUP:Receive 2 energy and 1 health\n"
    );
    gigazaur.energy += 2;
    gameState.pushEvent(
      new GainHealthEvent(executor, gigazaur.getCurrentHealth() + 1)
    );
  }
}
