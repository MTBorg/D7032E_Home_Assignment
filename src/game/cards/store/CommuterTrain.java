package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.game.cards.store.StoreCard;
import src.game.EventQueue;
import src.game.events.Event;
import src.game.Monster;
import src.server.KingTokyoPowerUpServer;

public class CommuterTrain extends DiscardCard implements Observer {

  public CommuterTrain() {
    super("Commuter Train", 4);
  }

  @Override
  public void update(Observable queue, Object event) {}

  @Override
  protected void effect(Monster monster) {
    KingTokyoPowerUpServer.sendOneWayMessage(
      monster.stream,
      "Message: You used the card " +
        this.name +
        " and were rewarded two stars!\n"
    ); //TODO: Remove this eventually
    monster.stars += 2;
  }
}
