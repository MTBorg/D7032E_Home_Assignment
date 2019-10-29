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

  /**
   * Add an event to the queue.
   *
   * This will notify all observers of the queue.
   *
   * @param event The event to add.
   * @param gameState The state of the game.
   */
  public void add(Event event, GameState gameState) {
    this.queue.add(event);
    setChanged();
    notifyObservers(new EventNotification(event, gameState));
  }

  /**
   * Return the event at the specified index.
   *
   * @param index The index of the event to get.
   * @return The event at the index.
   */
  public Event get(int index) {
    return this.queue.get(index);
  }

  /**
   * Return the size (amount of elements in the event queue) of the queue.
   *
   * @return The size of the queue.
   */
  public int size() {
    return this.queue.size();
  }
}
