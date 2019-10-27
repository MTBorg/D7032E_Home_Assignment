package tests.game.events;

import static org.junit.Assert.*;

import java.net.Socket;
import java.util.ArrayList;
import org.junit.Test;
import src.game.events.LoseHealthEvent;
import src.game.Game;
import src.game.GameState;
import src.game.Monster;

public class LoseHealthEventTests {

  @Test
  public void shouldBeKilledWhenZeroHealth() throws Exception {
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);
    monster.setCurrentHealth(1);

    gameState.pushEvent(new LoseHealthEvent(monster, 1));
    assertEquals(gameState.monsters.size(), 0);
  }

  @Test
  public void shouldLoseHealth() throws Exception {
    Monster monster = new Monster("Test Monster");
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    monsters.add(monster);
    GameState gameState = new GameState(monsters);

    int damage = 3;
    assertEquals(monster.getCurrentHealth(), monster.getMaxHealth());
    gameState.pushEvent(new LoseHealthEvent(monster, damage));
    assertEquals(gameState.monsters.size(), 1);
    assertEquals(monster.getCurrentHealth(), monster.getMaxHealth() - damage);
  }
}
