package src.game.events;

import src.game.GameState;

public interface Event {
  /**
   * This defines how the event will affect the game state.
   */
  public abstract void execute(GameState gameState);
}
