package tests.game;

import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import src.game.Game;
import src.game.Monster;
import tests.game.GameStepsTests;
import tests.game.GameTests;
import tests.game.MonsterTests;

@RunWith(Suite.class)
@Suite.SuiteClasses(
  {
    GameTests.class,
    MonsterTests.class,
    GameStepsTests.class,
    tests.game.events.AllTests.class
  }
)
public final class AllTests {}
