package src.events;

import java.util.ArrayList;

public abstract class Event {
  protected ArrayList<Event> queue;

  // Events should be able to add other events to the queue
  public Event(ArrayList<Event> queue) {
    this.queue = queue;
  }

  public abstract void execute();
}
