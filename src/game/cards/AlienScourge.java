package src.game.cards;

import java.util.ArrayList;
import src.game.cards.EvolutionCard;
import src.game.Monster;
import src.server.Server;

public class AlienScourge extends EvolutionCard {

  public AlienScourge() {
    super("Alien Scourge", "Alienoid", EvolutionCard.CardDuration.Temporary);
  }

  public void executeEffect(ArrayList<Monster> monsters) {
    System.out.println("Played " + this.getName());
    int alienoid = 0;
    for (int i = 0; i < monsters.size(); i++) {
      if (monsters.get(i).name == this.getMonster()) {
        alienoid = i;
        break;
      }
    }
    String power = Server.sendMessage(
      monsters.get(alienoid).stream,
      "POWERUP:Receive 2 stars\n"
    );
    monsters.get(alienoid).stars += 2;
  }
}
