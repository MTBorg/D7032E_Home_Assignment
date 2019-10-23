package src.game;

import java.util.ArrayList;
import java.util.Observable;
import src.game.events.Event;
import src.game.GameState;

public class EventQueue extends Observable {
  private ArrayList<Event> queue;

  public class EventNotification {
    public Event event;
    public GameState gameState;

    private EventNotification(Event event, GameState gameState) {
      this.event = event;
      this.gameState = gameState;
    }
  }

  public EventQueue() {
    this.queue = new ArrayList<Event>();
  }

  public void add(Event event, GameState gameState) {
    this.queue.add(event);
    setChanged();
    notifyObservers(new EventNotification(event, gameState));
  }

  public Event get(int index) {
    return this.queue.get(index);
  }

  public int size() {
    return this.queue.size();
  }
}
