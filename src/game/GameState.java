package src.game;

import java.util.ArrayList;
import java.util.Observer;
import src.game.Deck;
import src.game.EventQueue;
import src.game.events.Event;
import src.game.Monster;

public class GameState {
  private EventQueue eventQueue;
  public Deck deck;
  public ArrayList<Monster> monsters;

  public GameState(ArrayList<Monster> monsters) {
    this.monsters = monsters;
    this.deck = new Deck(monsters);
    this.eventQueue = new EventQueue();
  }

  /*
   * Add an event to the end of the queue and execute it.
   *
   * @param event The event to add and execute
   */
  public void pushEvent(Event event) {
    this.eventQueue.add(event, this);
    this.eventQueue.get(this.eventQueue.size() - 1).execute(this);
  }

  /*
   * Add an observer to the event queue.
   *
   * @param observer The observer to add.
   */
  public void addEventObserver(Observer observer) {
    this.eventQueue.addObserver(observer);
  }

  /*
   * Return the newest (last) item in the event queue.
   */
  public Event peekEventQueue() {
    return this.eventQueue.get(this.eventQueue.size() - 1);
  }
}
