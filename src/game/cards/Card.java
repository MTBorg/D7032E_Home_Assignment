package src.game.cards;

import java.util.Observable;
import java.util.Observer;
import src.game.Monster;
import src.server.Server;

public abstract class Card {
  protected final String name;
  protected final String description;

  public Card(String name, String description) {
    this.name = name;
    this.description = description;
  }

  abstract public String toString();

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  /**
   * Announce that the card was played to all players and server.
   *
   * @param executor The monster who executed the card
   */
  protected void announcePlayed(Monster executor) {
    Server.broadCastMessage(
      executor.getName() + " played card " + this.name + "\n",
      executor
    );
    Server.sendMessage(executor.stream, "You played card " + this.name + "\n");
    System.out.println(executor.getName() + " played card " + this.name + "\n");
  }
}
