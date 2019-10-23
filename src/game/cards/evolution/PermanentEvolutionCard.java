package src.game.cards.evolution;

import java.util.Observable;
import java.util.Observer;
import src.game.cards.evolution.EvolutionCard;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

abstract public class PermanentEvolutionCard
  extends EvolutionCard
  implements Observer {

  public PermanentEvolutionCard(String name, String monster) {
    super(name, monster);
  }

  abstract public void update(Observable queue, Object event);
}
