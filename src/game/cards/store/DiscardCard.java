package src.game.cards.store;

import src.game.cards.store.StoreCard;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

abstract public class DiscardCard extends StoreCard {

  public DiscardCard(String name, int cost) {
    super(name, cost);
  }

  public void execute(Monster executor, GameState gameState) {
    // Announce play
    Server.broadCastMessage(
      executor.getName() + " played card " + this.name + "\n",
      executor
    );
    Server.sendOneWayMessage(
      executor.stream,
      "You played card " + this.name + "\n"
    );
    System.out.println(executor.getName() + " played card " + this.name + "\n");

    effect(executor, gameState); // Play card

    // Remove card from player and place back in deck
    executor.removeCard(this);
    gameState.deck.deck.add(this);
  }

  abstract protected void effect(Monster executor, GameState gameState);
}
