package src.game.events;

import src.game.EventQueue;
import src.game.GameState;

public abstract class Event {
  private EventQueue queue;

  // TODO: This should no longer need to receive the event queue
  // as the execute method takes the entire state.
  // Events should be able to add other events to the queue
  public Event(EventQueue queue) {
    this.queue = queue;
  }

  public abstract void execute(GameState gameState);
}
