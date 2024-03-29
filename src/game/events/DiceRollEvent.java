package src.game.events;

import java.util.ArrayList;
import src.game.Dice;
import src.game.EventQueue;
import src.game.events.Event;
import src.game.GameState;
import src.game.Monster;

public class DiceRollEvent implements Event {
  public Monster roller;
  public ArrayList<Dice> dices = new ArrayList<Dice>();

  public DiceRollEvent(Monster roller, ArrayList<Dice> dices) {
    this.roller = roller;
    this.dices = dices;
  }

  public void execute(GameState gameState) {}

  public Monster getRoller() {
    return this.roller;
  }

  public ArrayList<Dice> getDices() {
    return this.dices;
  }
}
