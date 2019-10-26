package tests.game;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.game.Game;
import tests.game.GameStepsTests;
import tests.game.MonsterTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({ Game.class, MonsterTests.class, GameStepsTests.class })
public final class GameTests {}
