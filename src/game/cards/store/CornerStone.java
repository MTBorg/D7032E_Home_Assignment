package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.game.cards.store.StoreCard;
import src.game.EventQueue;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

public class CornerStone extends DiscardCard implements Observer {

  public CornerStone() {
    super("Corner Stone", "+ 1[Star]", 3);
  }

  @Override
  public void update(Observable queue, Object event) {}

  @Override
  protected void effect(Monster monster, GameState gameState) {
    monster.stars += 1;
  }
}
