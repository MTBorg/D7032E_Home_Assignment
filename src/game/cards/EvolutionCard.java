package src.game.cards;

import java.util.ArrayList;
import src.game.Monster;

public abstract class EvolutionCard {

  public enum CardDuration {
    Permanent, Temporary, Gift
  }

  private final String monster;
  private final String name;

  private final CardDuration duration;

  public EvolutionCard(String name, String monster, CardDuration duration) {
    this.monster = monster;
    this.name = name;
    this.duration = duration;
  }

  public String getMonster() {
    return this.monster;
  }

  public String getName() {
    return this.name;
  }

  public CardDuration getDuration() {
    return this.duration;
  }

  public abstract void executeEffect(ArrayList<Monster> monsters);
}
