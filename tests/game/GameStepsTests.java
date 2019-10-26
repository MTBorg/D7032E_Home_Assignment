package tests.game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import src.game.Card;
import src.game.cards.store.ArmorPlating;
import src.game.cards.store.StoreCard;
import src.game.Dice;
import src.game.events.BuyEvent;
import src.game.GameState;
import src.game.GameSteps;
import src.game.Monster;

public class GameStepsTests {

  /*
   * Generate a dice roll (6 dice) with a specified amount of hearts and
   * the rest ones.
   */
  private HashMap<Dice, Integer> generateDiceRoll(int hearts) {
    HashMap<Dice, Integer> result = new HashMap<Dice, Integer>();
    result.put(new Dice(0), hearts);
    result.put(new Dice(1), 6 - hearts);
    for (int i = 2; i < 6; i++) {
      result.put(new Dice(i), 0);
    }
    return result;
  }

  @Test
  public void countHeartsOneHeart() {
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRoll(1);
    monster.setCurrentHealth(1);

    assertEquals(monster.getCurrentHealth(), 1);
    GameSteps.countHearts(monster, diceRoll, gameState);
    assertEquals(monster.getCurrentHealth(), 2);
  }

  @Test
  public void countHeartsZeroHearts() {
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRoll(0);
    monster.setCurrentHealth(1);

    assertEquals(monster.getCurrentHealth(), 1);
    GameSteps.countHearts(monster, diceRoll, gameState);
    assertEquals(monster.getCurrentHealth(), 1);
  }

  @Test
  public void countHeartsDontExceedMaxHealth() {
    Monster monster = new Monster("Alienoid");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRoll(3);

    assertEquals(monster.getCurrentHealth(), monster.getMaxHealth());
    GameSteps.countHearts(monster, diceRoll, gameState);
    assertEquals(monster.getCurrentHealth(), monster.getMaxHealth());
  }

  @Test
  public void countHeartsInTokyoNoHealth() {
    Monster monster = new Monster("Test Monster");
    monster.inTokyo = true;
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRoll(1);
    monster.setCurrentHealth(1);

    //TODO: This feature is not implemented
    assertEquals(monster.getCurrentHealth(), 1);
    GameSteps.countHearts(monster, diceRoll, gameState);
    assertEquals(monster.getCurrentHealth(), 1);
  }

  @Test
  public void countHeartsThreeHeartsGivePowerUpCard() {
    Monster monster = new Monster("Alienoid");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRoll(3);

    //TODO: This is currently not possible to test
    assertEquals(monster.cardsToString(), "[NO CARDS]");
    GameSteps.countHearts(monster, diceRoll, gameState);
    assertFalse(monster.cardsToString().equals("[NO CARDS]"));
  }
}
