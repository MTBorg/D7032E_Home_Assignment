package tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.game.GameTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestsAreRunning.class, GameTests.class })
public final class AllTests {}
