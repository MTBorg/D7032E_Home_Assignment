package src.game.cards;

import java.util.ArrayList;
import src.game.cards.EvolutionCard;
import src.game.Monster;
import src.server.Server;

public class RedDawn extends EvolutionCard {

  public RedDawn() {
    super("Red Dawn", "Kong", EvolutionCard.CardDuration.Permanent);
  }

  public void executeEffect(ArrayList<Monster> monsters) {
    System.out.println("Played red dawn");
    int kong = 0;
    for (int i = 0; i < monsters.size(); i++) {
      if (monsters.get(i).getName() == this.getMonster()) {
        kong = i;
        break;
      }
    }
    String power = Server.sendMessage(
      monsters.get(kong).stream,
      "POWERUP:Deal 2 damage to all others\n"
    );
    for (int mon = 0; mon < monsters.size(); mon++) {
      if (mon != kong) {
        monsters.get(mon).currentHealth += -2;
      }
    }
  }
}
