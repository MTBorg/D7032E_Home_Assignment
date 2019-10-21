package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.game.cards.store.StoreCard;
import src.game.EventQueue;
import src.game.events.Event;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

public class ApartmentBuilding extends DiscardCard implements Observer {

  public ApartmentBuilding() {
    super("Apartment Building", 5);
  }

  @Override
  public void update(Observable queue, Object event) {}

  @Override
  protected void effect(Monster monster, GameState gameState) {
    Server.sendOneWayMessage(
      monster.stream,
      "Message: You used the card " +
        this.name +
        " and were rewarded three stars!\n"
    ); //TODO: Remove this eventually
    monster.stars += 3;
  }
}
