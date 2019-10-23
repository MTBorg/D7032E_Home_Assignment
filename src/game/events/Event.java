package src.game.events;

import src.game.EventQueue;
import src.game.GameState;

public abstract class Event {
  protected EventQueue queue;

  // Events should be able to add other events to the queue
  public Event(EventQueue queue) {
    this.queue = queue;
  }

  public abstract void execute(GameState gameState);
}
