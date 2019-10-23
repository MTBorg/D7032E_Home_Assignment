package src.game.cards.evolution;

import java.util.ArrayList;
import src.game.cards.evolution.EvolutionCard;
import src.game.events.GainHealthEvent;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

public class RadioactiveWaste extends TemporaryEvolutionCard {

  public RadioactiveWaste() {
    super("Radioactive Waste", "Gigazaur");
  }

  public void effect(Monster executor, GameState gameState) {
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
    gameState.eventQueue.add(
      new GainHealthEvent(
        gameState.eventQueue,
        executor,
        gigazaur.getCurrentHealth() + 1
      )
    );
    gameState.eventQueue.get(gameState.eventQueue.size() - 1).execute();
  }
}
