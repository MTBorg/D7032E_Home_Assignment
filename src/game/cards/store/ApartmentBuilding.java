package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.events.Event;
import src.game.cards.store.StoreCard;
import src.game.EventQueue;
import src.game.Monster;
import src.server.KingTokyoPowerUpServer;

public class ApartmentBuilding extends StoreCard implements Observer {

  public ApartmentBuilding() {
    super("Apartment Building", 5, true);
  }

  @Override
  public void update(Observable queue, Object event) {}

  @Override
  public void execute(Monster monster) {
    KingTokyoPowerUpServer.sendOneWayMessage(
      monster.stream,
      "Message: You used the card " +
        this.name +
        " and were rewarded three stars!\n"
    ); //TODO: Remove this eventually
    monster.stars += 3;

    monster.removeCard(this);
  }
}
