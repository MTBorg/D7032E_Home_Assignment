package src.game.cards.store;

import src.game.cards.store.StoreCard;
import src.game.Monster;

abstract public class DiscardCard extends StoreCard {

  public DiscardCard(String name, int cost) {
    super(name, cost, true);
  }

  @Override
  public void execute(Monster monster) {
    effect(monster);
    monster.removeCard(this);
  //TODO: Place back in deck
  }

  abstract protected void effect(Monster monster);
}
