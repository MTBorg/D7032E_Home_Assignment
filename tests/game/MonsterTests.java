package tests.game;

import static org.junit.Assert.*;

import org.junit.Test;
import src.game.Monster;

public class MonsterTests {

  @Test
  public void cantSetHealthHigherThanMax() {
    Monster monster = new Monster("Test Monster");

    monster.setCurrentHealth(monster.getMaxHealth() * 10);

    assertEquals(monster.getCurrentHealth(), monster.getMaxHealth());
  }
}
