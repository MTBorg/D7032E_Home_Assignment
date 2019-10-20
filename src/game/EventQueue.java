package src.game;

import java.util.ArrayList;
import java.util.Observable;
import src.events.Event;

public class EventQueue extends Observable {
  private ArrayList<Event> queue;

  public EventQueue() {
    this.queue = new ArrayList<Event>();
  }

  public void add(Event e) {
    this.queue.add(e);
    setChanged();
    notifyObservers(e);
  }

  public Event get(int index) {
    return this.queue.get(index);
  }

  public int size() {
    return this.queue.size();
  }
}
