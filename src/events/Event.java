package src.events;

import src.game.EventQueue;

public abstract class Event {
  protected EventQueue queue;

  // Events should be able to add other events to the queue
  public Event(EventQueue queue) {
    this.queue = queue;
  }

  public abstract void execute();
}
