package src.game.cards.evolution;

import src.game.cards.evolution.EvolutionCard;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

abstract public class TemporaryEvolutionCard extends EvolutionCard {

  public TemporaryEvolutionCard(String name, String monster) {
    super(name, monster);
  }

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
      gameState.deck.evolutionCards.get(executor.getName()).add(this);
  }

  abstract protected void effect(Monster executor, GameState gameState);
}
