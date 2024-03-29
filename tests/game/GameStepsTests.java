package tests.game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import src.game.cards.Card;
import src.game.cards.store.AlienMetabolism;
import src.game.cards.store.ArmorPlating;
import src.game.cards.store.CommuterTrain;
import src.game.cards.store.StoreCard;
import src.game.Deck;
import src.game.Dice;
import src.game.events.BuyEvent;
import src.game.Game;
import src.game.GameState;
import src.game.GameSteps;
import src.game.Monster;

public class GameStepsTests {

  /*
   * Generate a dice roll (6 dice) with a specified amount of hearts and
   * the rest ones.
   */
  private HashMap<Dice, Integer> generateDiceRollHearts(int hearts) {
    HashMap<Dice, Integer> result = new HashMap<Dice, Integer>();
    result.put(new Dice(0), hearts);
    result.put(new Dice(1), 6 - hearts);
    for (int i = 2; i < 6; i++) {
      result.put(new Dice(i), 0);
    }
    return result;
  }

  private HashMap<Dice, Integer> generateDiceRollClaws(int claws) {
    HashMap<Dice, Integer> result = new HashMap<Dice, Integer>();
    result.put(new Dice(5), claws);
    for (int i = 0; i < 5; i++) {
      result.put(new Dice(i), 0);
    }
    return result;
  }

  private HashMap<Dice, Integer> generateDiceRollEnergy(int energy) {
    HashMap<Dice, Integer> result = new HashMap<Dice, Integer>();
    result.put(new Dice(0), 0); //Don't generate hearts in order to avoid powerups
    result.put(new Dice(1), 6 - energy);
    result.put(new Dice(2), 0);
    result.put(new Dice(3), 0);
    result.put(new Dice(4), energy);
    result.put(new Dice(5), 0); //Don't generate energy in order to avoid damage
    return result;
  }

  private HashMap<Dice, Integer> generateDiceRollNumber(
    int number,
    int amount
  ) {
    HashMap<Dice, Integer> result = new HashMap<Dice, Integer>();
    result.put(new Dice(0), 6 - amount);
    for (int i = 1; i < 6; i++) {
      result.put(new Dice(i), (i == number) ? amount : 0);
    }
    return result;
  }

  @Test
  public void countHeartsOneHeart() {
    //Tests 12e,ii
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollHearts(1);
    monster.setCurrentHealth(1);

    assertEquals(monster.getCurrentHealth(), 1);
    GameSteps.countHearts(monster, diceRoll, gameState);
    assertEquals(monster.getCurrentHealth(), 2);
  }

  @Test
  public void countHeartsZeroHearts() {
    //Tests 12e,ii
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollHearts(0);
    monster.setCurrentHealth(1);

    assertEquals(monster.getCurrentHealth(), 1);
    GameSteps.countHearts(monster, diceRoll, gameState);
    assertEquals(monster.getCurrentHealth(), 1);
  }

  @Test
  public void countHeartsDontExceedMaxHealth() {
    //Tests 12e,ii
    Monster monster = new Monster("Alienoid");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollHearts(3);

    assertEquals(monster.getCurrentHealth(), monster.getMaxHealth());
    GameSteps.countHearts(monster, diceRoll, gameState);
    assertEquals(monster.getCurrentHealth(), monster.getMaxHealth());
  }

  @Test
  public void shouldNotGetHealthWhenInTokyo() {
    //Tests 12e,i
    Monster monster = new Monster("Test Monster");
    monster.inTokyo = true;
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollHearts(1);
    monster.setCurrentHealth(1);

    assertEquals(monster.getCurrentHealth(), 1);
    GameSteps.countHearts(monster, diceRoll, gameState);
    assertEquals(monster.getCurrentHealth(), 1);
  }

  @Test
  public void countHeartsThreeHeartsGivePowerUpCard() {
    //Tests 12f
    Monster monster = new Monster("Alienoid");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollHearts(3);

    //TODO: This is currently not possible to test
    assertEquals(monster.cardsToString(), "[NO CARDS]:");
    int evolutionCardsAmount = gameState
      .deck.evolutionCards.get(monster.getName())
      .size();
    GameSteps.countHearts(monster, diceRoll, gameState);
    assertEquals(
      gameState.deck.evolutionCards.get(monster.getName()).size(),
      evolutionCardsAmount - 1
    );
  // assert assertFalse(monster.cardsToString().equals("[NO CARDS]"));
  }

  @Test
  public void checkIfInTokyoGiveStar() {
    //Tests 7
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);

    monster.inTokyo = true;

    assertEquals(monster.stars, 0);
    GameSteps.checkIfInTokyo(monster);
    assertEquals(monster.stars, 1);
  }

  @Test
  public void checkIfNotInTokyoNoStar() {
    //Tests 7
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);

    monster.inTokyo = false;

    assertEquals(monster.stars, 0);
    GameSteps.checkIfInTokyo(monster);
    assertEquals(monster.stars, 0);
  }

  @Test
  public void countVictoryPointsZeroOnes() {
    //Tests 12a
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollNumber(1, 0);

    assertEquals(monster.stars, 0);
    GameSteps.countVictoryPoints(monster, diceRoll);
    assertEquals(monster.stars, 0);
  }

  @Test
  public void countVictoryPointsTwoOnes() {
    //Tests 12a
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollNumber(1, 2);

    assertEquals(monster.stars, 0);
    GameSteps.countVictoryPoints(monster, diceRoll);
    assertEquals(monster.stars, 0);
  }

  @Test
  public void countVictoryPointsThreeOnes() {
    //Tests 12a
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollNumber(1, 3);

    assertEquals(monster.stars, 0);
    GameSteps.countVictoryPoints(monster, diceRoll);
    assertEquals(monster.stars, 1);
  }

  @Test
  public void countVictoryPointsFourOnes() {
    //Tests 12a
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollNumber(1, 4);

    assertEquals(monster.stars, 0);
    GameSteps.countVictoryPoints(monster, diceRoll);
    assertEquals(monster.stars, 2);
  }

  @Test
  public void countVictoryPointsZeroTwos() {
    //Tests 12b
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollNumber(2, 0);

    assertEquals(monster.stars, 0);
    GameSteps.countVictoryPoints(monster, diceRoll);
    assertEquals(monster.stars, 0);
  }

  @Test
  public void countVictoryPointsTwoTwos() {
    //Tests 12b
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollNumber(2, 2);

    assertEquals(monster.stars, 0);
    GameSteps.countVictoryPoints(monster, diceRoll);
    assertEquals(monster.stars, 0);
  }

  @Test
  public void countVictoryPointsThreeTwos() {
    //Tests 12b
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollNumber(2, 3);

    assertEquals(monster.stars, 0);
    GameSteps.countVictoryPoints(monster, diceRoll);
    assertEquals(monster.stars, 2);
  }

  @Test
  public void countVictoryPointsFourTwos() {
    //Tests 12b
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollNumber(2, 4);

    assertEquals(monster.stars, 0);
    GameSteps.countVictoryPoints(monster, diceRoll);
    assertEquals(monster.stars, 3);
  }

  @Test
  public void countVictoryPointsZeroThrees() {
    //Tests 12c
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollNumber(3, 0);

    assertEquals(monster.stars, 0);
    GameSteps.countVictoryPoints(monster, diceRoll);
    assertEquals(monster.stars, 0);
  }

  @Test
  public void countVictoryPointsTwoThrees() {
    //Tests 12c
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollNumber(3, 2);

    assertEquals(monster.stars, 0);
    GameSteps.countVictoryPoints(monster, diceRoll);
    assertEquals(monster.stars, 0);
  }

  @Test
  public void countVictoryPointsThreeThrees() {
    //Tests 12c
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollNumber(3, 3);

    assertEquals(monster.stars, 0);
    GameSteps.countVictoryPoints(monster, diceRoll);
    assertEquals(monster.stars, 3);
  }

  @Test
  public void countVictoryPointsFourThrees() {
    //Tests 12c
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    HashMap<Dice, Integer> diceRoll = generateDiceRollNumber(3, 4);

    assertEquals(monster.stars, 0);
    GameSteps.countVictoryPoints(monster, diceRoll);
    assertEquals(monster.stars, 4);
  }

  @Test
  public void countEnergy() {
    //Tests 12d
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);

    //Assert that the amount of energy received is the same as energy dice rolled
    for (int i = 0; i < 6; i++) {
      monster.energy = 0;
      assertEquals(monster.energy, 0);
      HashMap<Dice, Integer> diceRoll = generateDiceRollEnergy(i);
      GameSteps.countEnergy(monster, diceRoll);
      assertEquals(monster.energy, i);
    }
  }

  @Test
  public void buyCannotAffordCard() {
    //Tests 13
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    StoreCard[] store = new StoreCard[3];
    store[0] = new ArmorPlating();
    store[1] = new CommuterTrain();
    store[2] = new AlienMetabolism();
    Deck deck = new Deck(monsters);
    deck.store = store;
    gameState.deck = deck;
    monster.energy = 0;

    assertFalse(monster.hasCard("Armor Plating"));
    GameSteps.buy(monster, gameState, 0);
    assertFalse(monster.hasCard("Armor Plating"));
  }

  @Test
  public void buyCanAffordCard() {
    //Tests 13
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    StoreCard[] store = new StoreCard[3];
    store[0] = new ArmorPlating();
    store[1] = new CommuterTrain();
    store[2] = new AlienMetabolism();
    Deck deck = new Deck(monsters);
    deck.store = store;
    gameState.deck = deck;
    monster.energy = store[0].getCost();

    assertFalse(monster.hasCard("Armor Plating"));
    GameSteps.buy(monster, gameState, 0);
    assertTrue(monster.hasCard("Armor Plating"));
  }

  @Test
  public void buyStoreCardReplaced() {
    //Tests 13
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    StoreCard[] store = new StoreCard[3];
    store[0] = new ArmorPlating();
    store[1] = new CommuterTrain();
    store[2] = new AlienMetabolism();
    Deck deck = new Deck(monsters);
    deck.store = store;
    gameState.deck = deck;
    monster.energy = store[0].getCost();

    //TODO: This seems to fail some times
    assertTrue(store[0] instanceof ArmorPlating);
    GameSteps.buy(monster, gameState, 0);
    assertFalse(store[0] instanceof ArmorPlating);
  }

  @Test
  public void buyDiscardCardPlayedImmediately() {
    //Tests 13
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    StoreCard[] store = new StoreCard[3];
    store[0] = new ArmorPlating();
    store[1] = new CommuterTrain();
    store[2] = new AlienMetabolism();
    Deck deck = new Deck(monsters);
    deck.store = store;
    gameState.deck = deck;
    monster.energy = store[1].getCost();
    monster.stars = 0;

    // Buying the commuter train card should give the monster two stars when played
    // TODO: This is a pretty hacky way of checking if the card was played
    assertEquals(monster.stars, 0);
    GameSteps.buy(monster, gameState, 1);
    assertEquals(monster.stars, 2);
  }

  @Test
  public void shouldWinWithEnoughStars() {
    //Tests 16
    Monster monster1 = new Monster("Test Monster1");
    Monster monster2 = new Monster("Test Monster2");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster1);
    monsters.add(monster2);
    GameState gameState = new GameState(monsters);
    monster1.stars = Game.VICTORY_STARS;

    assertTrue(GameSteps.checkVictoryConditions(gameState));
  }

  @Test
  public void shouldNotWinWithTooFewStars() {
    //Tests 16
    Monster monster1 = new Monster("Test Monster1");
    Monster monster2 = new Monster("Test Monster2");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster1);
    monsters.add(monster2);
    GameState gameState = new GameState(monsters);
    monster1.stars = Game.VICTORY_STARS - 1;

    assertFalse(GameSteps.checkVictoryConditions(gameState));
  }

  @Test
  public void shouldWinWhenOnlyOneMonsterIsAlive() {
    //Tests 17
    Monster monster1 = new Monster("Test Monster1");
    Monster monster2 = new Monster("Test Monster2");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster1);
    monsters.add(monster2);
    GameState gameState = new GameState(monsters);
    monster1.setCurrentHealth(0);

    assertTrue(GameSteps.checkVictoryConditions(gameState));
  }

  @Test
  public void shouldNotWinWhenTwoMonstersAreAlive() {
    //Tests 16
    Monster monster1 = new Monster("Test Monster1");
    Monster monster2 = new Monster("Test Monster2");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster1);
    monsters.add(monster2);
    GameState gameState = new GameState(monsters);

    assertTrue(monster1.getCurrentHealth() > 0);
    assertTrue(monster2.getCurrentHealth() > 0);
    assertFalse(GameSteps.checkVictoryConditions(gameState));
  }

  @Test
  public void shouldRollSixDice() {
    //Tests 8
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);

    ArrayList<Dice> dice = GameSteps.rollDices(gameState, monster);
    assertEquals(dice.size(), 6);
    for (int i = 0; i < 6; i++) {
      assertTrue(dice.get(i).value >= 0 && dice.get(i).value <= 5);
    }
  }

  @Test
  public void shouldNotKeepSelectedDices() {
    //Tests 9
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);

    ArrayList<Dice> dices = new ArrayList<Dice>();
    for (int i = 0; i < 6; i++) {
      dices.add(new Dice(i));
    }

    assertEquals(dices.size(), 6);

    //Should  remove the first, second and fifth dice
    GameSteps.keepDices("5,2,1", dices, monster);

    assertEquals(dices.size(), 3);
    assertEquals(dices.get(0).value, 2);
    assertEquals(dices.get(1).value, 3);
    assertEquals(dices.get(2).value, 5);
  }

  @Test
  public void shouldRerollDice() {
    //Tests 10
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);

    ArrayList<Dice> dices = new ArrayList<Dice>();
    for (int i = 0; i < 6; i++) {
      dices.add(new Dice(i));
    }

    assertEquals(dices.size(), 6);

    GameSteps.keepDices("6,3", dices, monster);
    GameSteps.rerollDices(dices, gameState, monster);

    assertEquals(dices.size(), 6);
    for (int i = 0; i < 6; i++) {
      assertTrue(dices.get(i).value >= 0 && dices.get(i).value <= 5);
    }
  }

  @Test
  public void shouldBeAbleToResetTheStore() {
    // Tests 13b
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    monster.energy = 2;

    int deckSize = gameState.deck.deck.size();
    for (int i = 0; i < 3; i++) {
      assertTrue(gameState.deck.store[i] != null);
    }
    GameSteps.buy(monster, gameState, -2);
    assertEquals(monster.energy, 0);
    for (int i = 0; i < 3; i++) {
      assertTrue(gameState.deck.store[i] != null);
    }
    assertEquals(gameState.deck.deck.size(), deckSize); //Assert no cards were destroyed
  }

  @Test
  public void shouldDealDamageToMonstersOutsideTokyo() {
    // Tests 12g
    Monster monster1 = new Monster("Test Monster1");
    Monster monster2 = new Monster("Test Monster2");
    Monster monster3 = new Monster("Test Monster3");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster1);
    monsters.add(monster2);
    monsters.add(monster3);
    GameState gameState = new GameState(monsters);
    monster1.inTokyo = true;
    monster2.inTokyo = false;
    monster3.inTokyo = false;
    HashMap<Dice, Integer> diceRoll = generateDiceRollClaws(1);

    assertEquals(monster2.getCurrentHealth(), monster2.getMaxHealth());
    assertEquals(monster3.getCurrentHealth(), monster3.getMaxHealth());
    GameSteps.countClaws(monster1, diceRoll, gameState);
    assertEquals(monster2.getCurrentHealth(), monster2.getMaxHealth() - 1);
    assertEquals(monster3.getCurrentHealth(), monster3.getMaxHealth() - 1);
  }

  @Test
  public void shouldNotDealDamageToSelf() {
    // Tests 12g,i
    Monster monster1 = new Monster("Test Monster1");
    Monster monster2 = new Monster("Test Monster2");
    Monster monster3 = new Monster("Test Monster3");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster1);
    monsters.add(monster2);
    monsters.add(monster3);
    GameState gameState = new GameState(monsters);
    monster1.inTokyo = true;
    monster2.inTokyo = false;
    monster3.inTokyo = false;
    HashMap<Dice, Integer> diceRoll = generateDiceRollClaws(1);

    assertEquals(monster1.getCurrentHealth(), monster1.getMaxHealth());
    GameSteps.countClaws(monster1, diceRoll, gameState);
    assertEquals(monster1.getCurrentHealth(), monster1.getMaxHealth());
  }

  @Test
  public void shouldMoveIntoTokyoWhenEmpty() {
    // Tests 12g,ii,1
    Monster monster1 = new Monster("Test Monster1");
    Monster monster2 = new Monster("Test Monster2");
    Monster monster3 = new Monster("Test Monster3");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster1);
    monsters.add(monster2);
    monsters.add(monster3);
    GameState gameState = new GameState(monsters);
    monster1.inTokyo = false;
    monster2.inTokyo = false;
    monster3.inTokyo = false;
    HashMap<Dice, Integer> diceRoll = generateDiceRollClaws(1);

    GameSteps.countClaws(monster1, diceRoll, gameState);
    assertTrue(monster1.inTokyo);
  }

  @Test
  public void shouldDealDamageToMonsterInTokyo() {
    // Tests 12g,ii,2,a
    Monster monster1 = new Monster("Test Monster1");
    Monster monster2 = new Monster("Test Monster2");
    Monster monster3 = new Monster("Test Monster3");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster1);
    monsters.add(monster2);
    monsters.add(monster3);
    GameState gameState = new GameState(monsters);
    monster1.inTokyo = false;
    monster2.inTokyo = true;
    monster3.inTokyo = false;
    HashMap<Dice, Integer> diceRoll = generateDiceRollClaws(1);

    assertEquals(monster2.getCurrentHealth(), monster2.getMaxHealth());
    assertEquals(monster3.getCurrentHealth(), monster3.getMaxHealth());
    GameSteps.countClaws(monster1, diceRoll, gameState);
    assertEquals(monster2.getCurrentHealth(), monster2.getMaxHealth() - 1);
    assertEquals(monster3.getCurrentHealth(), monster3.getMaxHealth());
  }
}
