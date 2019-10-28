package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.game.cards.store.StoreCard;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

abstract public class KeepCard extends StoreCard implements Observer {

  public KeepCard(String name, String description, int cost) {
    super(name, description, cost);
  }

  abstract public void update(Observable queue, Object event);
}
