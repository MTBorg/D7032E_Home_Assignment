package tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestsAreRunning.class, tests.game.AllTests.class })
public final class AllTests {}
