package src.game.cards;

import java.util.ArrayList;
import src.game.cards.EvolutionCard;
import src.game.Monster;
import src.server.KingTokyoPowerUpServer;

public class RadioactiveWaste extends EvolutionCard {

  public RadioactiveWaste() {
    super(
      "Radioactive Waste",
      "Gigazaur",
      EvolutionCard.CardDuration.Temporary
    );
  }

  public void executeEffect(ArrayList<Monster> monsters) {
    System.out.println("Played radioactive waste");
    int gigazaur_index = 0;
    for (int i = 0; i < monsters.size(); i++) {
      if (monsters.get(i).name == this.getMonster()) {
        gigazaur_index = i;
        break;
      }
    }

    Monster gigazaur = monsters.get(gigazaur_index);
    String power = KingTokyoPowerUpServer.sendMessage(
      gigazaur.stream,
      "POWERUP:Receive 2 energy and 1 health\n"
    );
    gigazaur.energy += 2;
    if (gigazaur.currentHealth + 1 >= gigazaur.maxHealth) {
      gigazaur.currentHealth = gigazaur.maxHealth;
    } else {
      gigazaur.currentHealth += 1;
    }
  }
}
