package src.game.cards.evolution;

import src.game.cards.evolution.EvolutionCard;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

abstract public class TemporaryEvolutionCard extends EvolutionCard {

  public TemporaryEvolutionCard(
    String name,
    String description,
    String monster
  ) {
    super(name, description, monster);
  }

  public void execute(Monster executor, GameState gameState) {
    announcePlayed(executor);
    effect(executor, gameState);
    executor.removeCard(this);

    // Place back in deck
      gameState.deck.evolutionCards.get(executor.getName()).add(this);
  }

  abstract protected void effect(Monster executor, GameState gameState);
}
