package src.game.cards;

import java.util.ArrayList;
import src.game.Card;
import src.game.Monster;

public abstract class EvolutionCard extends Card {

  public enum CardDuration {
    Permanent, Temporary, Gift
  }

  private final String monster;

  private final CardDuration duration;

  public EvolutionCard(String name, String monster, CardDuration duration) {
    super(name);
    this.monster = monster;
    this.duration = duration;
  }

  public String getMonster() {
    return this.monster;
  }

  public CardDuration getDuration() {
    return this.duration;
  }

  public abstract void executeEffect(ArrayList<Monster> monsters);
}
