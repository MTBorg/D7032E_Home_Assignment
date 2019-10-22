package src.game.factories;

import java.lang.Exception;
import java.util.ArrayList;
import src.game.cards.store.StoreCard;

public class StoreCardFactory {
  static private final String CARD_PATH = "src.game.cards.store.";

  // Select what cards to be available in the game by entering their name in
  // this array. Make sure that the names (case sensitive and stripped of whitespaces)
  // match a card name in the CARD_PATH folder.
  // E.g. to include the card Alpha Monster in the file CARD_PATH/RedDawn.java place the
  // String "Alpha Monster" in the array below.
  static private final String[] cards = {
    "Alpha Monster",
    "Alien Metabolism",
    "Armor Plating",
    "Apartment Building",
    "Commuter Train",
    "Corner Stone",
    "Complete Destruction"
  };

  public StoreCard getCard(String name) throws Exception {
    for (String card : StoreCardFactory.cards) {
      if (name.equalsIgnoreCase(card)) {
        try {
          //Append the name of the card (stripped of whitespaces) to the CARD_PATH
          //of the and look for that java file.
          return (StoreCard) Class
            .forName(CARD_PATH + card.replaceAll("\\s", ""))
            .newInstance();
        } catch (Exception e) {
          throw e;
        }
      }
    }
    throw new Exception("Unimplemented card");
  }

  public ArrayList<StoreCard> getAllCards() {
    ArrayList<StoreCard> result = new ArrayList<StoreCard>();
    for (String card : StoreCardFactory.cards) {
      try {
        result.add(this.getCard(card));
      } catch (Exception e) {
        System.out.println("Store card factory error: " + e);
        System.exit(0);
      }
    }
    return result;
  }
}
