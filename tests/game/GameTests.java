package tests.game;

import static org.junit.Assert.*;

import java.net.Socket;
import java.util.ArrayList;
import org.junit.Test;
import src.game.Game;
import src.game.Monster;
import src.network.Stream;

public class GameTests {

  @Test
  public void gameTestsAreRunning() {
    assertTrue(true);
  }

  @Test
  public void allPlayersAreAssignedAMonster() throws Exception {
    ArrayList<Stream> streams = new ArrayList<Stream>();
    streams.add(null);
    streams.add(null);
    Game game = new Game(streams);
    Monster monster1 = game.getState().monsters.get(0);
    Monster monster2 = game.getState().monsters.get(1);

    assertEquals(game.getState().monsters.size(), 2);
    assertTrue(
      monster1 != null && monster1.getName() != null && monster1.getName() != ""
    );
    assertTrue(
      monster2 != null && monster2.getName() != null && monster2.getName() != ""
    );
  }
}
