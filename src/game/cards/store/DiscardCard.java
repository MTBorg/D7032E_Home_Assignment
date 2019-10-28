package src.game.cards.store;

import src.game.cards.store.StoreCard;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

abstract public class DiscardCard extends StoreCard {

  public DiscardCard(String name, String description, int cost) {
    super(name, description, cost);
  }

  public void execute(Monster executor, GameState gameState) {
    announcePlayed(executor);
    effect(executor, gameState); // Play card

    // Remove card from player and place back in deck
    executor.removeCard(this);
    gameState.deck.deck.add(this);
  }

  abstract protected void effect(Monster executor, GameState gameState);
}
