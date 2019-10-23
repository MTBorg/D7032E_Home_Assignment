package src.game.cards.store;

import src.game.cards.store.StoreCard;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

abstract public class DiscardCard extends StoreCard {

  public DiscardCard(String name, int cost) {
    super(name, cost, true);
  }

  @Override
  public void execute(Monster executor, GameState gameState) {
    Server.broadCastMessage(
      executor.getName() + " played card " + this.name + "\n",
      executor
    );
    Server.sendOneWayMessage(
      executor.stream,
      "You played card " + this.name + "\n"
    );
    effect(executor, gameState);
    executor.removeCard(this);

    // Place back in deck
    gameState.deck.deck.add(this);
  }

  abstract protected void effect(Monster executor, GameState gameState);
}
