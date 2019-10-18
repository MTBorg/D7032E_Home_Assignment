package src.game.cards;

import java.util.ArrayList;
import src.game.cards.EvolutionCard;
import src.game.Monster;
import src.server.KingTokyoPowerUpServer;

public class RedDawn extends EvolutionCard {

  public RedDawn() {
    super("Red Dawn", "Kong", EvolutionCard.CardDuration.Permanent);
  }

  public void executeEffect(ArrayList<Monster> monsters) {
    System.out.println("Played red dawn");
    int kong = 0;
    for (int i = 0; i < monsters.size(); i++) {
      if (monsters.get(i).name == this.getMonster()) {
        kong = i;
        break;
      }
    }
    String power = KingTokyoPowerUpServer.sendMessage(
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
