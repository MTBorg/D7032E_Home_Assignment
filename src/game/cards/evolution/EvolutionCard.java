package src.game.cards.evolution;

import java.util.ArrayList;
import src.game.cards.Card;
import src.game.cards.evolution.TemporaryEvolutionCard;
import src.game.GameState;
import src.game.Monster;

public abstract class EvolutionCard extends Card {
  private final String monster;

  public EvolutionCard(String name, String monster) {
    super(name);
    this.monster = monster;
  }

  public String getMonster() {
    return this.monster;
  }

  public String toString() {
    //TODO: This should print the card description as well
    return (
      this.name +
      ", " +
      (this instanceof TemporaryEvolutionCard ? "Temporary" : "Permanent") +
      ", Effect "
    );
  }

  public boolean isTemporary() {
    return this instanceof TemporaryEvolutionCard;
  }
}
