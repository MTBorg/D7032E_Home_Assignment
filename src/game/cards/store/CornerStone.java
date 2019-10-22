package src.game.cards.store;

import java.util.Observable;
import java.util.Observer;
import src.game.cards.store.StoreCard;
import src.game.EventQueue;
import src.game.Monster;
import src.server.KingTokyoPowerUpServer;

public class CornerStone extends DiscardCard implements Observer {

  public CornerStone() {
    super("Corner Stone", 4);
  }

  @Override
  public void update(Observable queue, Object event) {}

  @Override
  protected void effect(Monster monster) {
    KingTokyoPowerUpServer.sendOneWayMessage(
      monster.stream,
      "Message: You used the card " +
        this.name +
        " and were rewarded one star!\n"
    ); //TODO: Remove this eventually
    monster.stars += 1;
  }
}
