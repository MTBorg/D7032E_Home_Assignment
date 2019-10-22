package src.game;

import java.util.ArrayList;
import src.game.Deck;
import src.game.EventQueue;
import src.game.Monster;

public class GameState {
  public EventQueue eventQueue;
  public Deck deck;
  public ArrayList<Monster> monsters;

  public GameState(ArrayList<Monster> monsters) {
    this.monsters = monsters;
    this.deck = new Deck();
    this.eventQueue = new EventQueue();
  }
}
