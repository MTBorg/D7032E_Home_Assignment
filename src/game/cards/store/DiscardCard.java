package src.game.cards.store;

import src.game.cards.store.StoreCard;
import src.game.GameState;
import src.game.Monster;

abstract public class DiscardCard extends StoreCard {

  public DiscardCard(String name, int cost) {
    super(name, cost, true);
  }

  @Override
  public void execute(Monster executor, GameState gameState) {
    effect(executor, gameState);
    executor.removeCard(this);
  //TODO: Place back in deck
  }

  abstract protected void effect(Monster executor, GameState gameState);
}
