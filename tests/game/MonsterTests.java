package tests.game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;
import src.game.Card;
import src.game.cards.store.ArmorPlating;
import src.game.cards.store.StoreCard;
import src.game.GameState;
import src.game.Monster;

public class MonsterTests {

  @Test
  public void cantSetHealthHigherThanMax() {
    Monster monster = new Monster("Test Monster");

    monster.setCurrentHealth(monster.getMaxHealth() * 10);

    assertEquals(monster.getCurrentHealth(), monster.getMaxHealth());
  }

  @Test
  public void giveCard() {
    Monster monster = new Monster("Test Monster");
    Card armorPlating = new ArmorPlating();
    assertFalse(monster.hasCard(armorPlating.getName()));
    monster.giveCard(armorPlating);
    assertTrue(monster.hasCard(armorPlating.getName()));
  }

  @Test
  public void removeCard() {
    Monster monster = new Monster("Test Monster");
    Card armorPlating = new ArmorPlating();
    monster.giveCard(armorPlating);
    assertTrue(monster.hasCard(armorPlating.getName()));
    monster.removeCard(armorPlating);
    assertFalse(monster.hasCard(armorPlating.getName()));
  }

  @Test
  public void attackMonster() {
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    int damage = 3;
    GameState gameState = new GameState(monsters);
    assertEquals(monster.getCurrentHealth(), monster.getMaxHealth());
    monster.attackMonster(monster, damage, gameState);
    assertEquals(monster.getCurrentHealth(), monster.getMaxHealth() - damage);
  }

  @Test
  public void buyCard() {
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    StoreCard armorPlating = new ArmorPlating();
    monster.energy = armorPlating.getCost();
    GameState gameState = new GameState(monsters);
    assertFalse(monster.hasCard(armorPlating.getName()));
    monster.buyCard(armorPlating, gameState);
    assertTrue(monster.hasCard(armorPlating.getName()));
  }

  @Test
  public void startWithNoStars() {
    Monster monster = new Monster("Test Monster");
    assertEquals(monster.stars, 0);
  }

  @Test
  public void startWith10Health() {
    Monster monster = new Monster("Test Monster");
    assertEquals(monster.getCurrentHealth(), 10);
  }
}
