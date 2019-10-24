package src.game.events;

import src.game.GameState;

public interface Event {
  public abstract void execute(GameState gameState);
}
