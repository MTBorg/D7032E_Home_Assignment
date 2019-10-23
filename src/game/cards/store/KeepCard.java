package src.game.cards.store;

import java.util.Observable;
import src.game.cards.store.StoreCard;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

abstract public class KeepCard extends StoreCard {

  public KeepCard(String name, int cost) {
    super(name, cost);
  }

  @Override
  abstract public void update(Observable queue, Object event);
}
