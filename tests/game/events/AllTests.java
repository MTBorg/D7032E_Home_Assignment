package tests.game.events;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.game.events.LoseHealthEventTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({ LoseHealthEventTests.class })
public final class AllTests {}
