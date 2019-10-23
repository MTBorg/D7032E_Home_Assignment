package src.game.factories;

import java.lang.Exception;
import java.util.ArrayList;
import src.game.cards.evolution.EvolutionCard;
import src.game.Monster;

public class EvolutionCardFactory {
  static private final String CARD_PATH = "src.game.cards.evolution.";

  // Select what cards to be available in the game by entering their name in
  // this array. Make sure that the names (case sensitive and stripped of whitespaces)
  // match a card name in the CARD_PATH folder.
  // E.g. to include the card Red Dawn in the file CARD_PATH/RedDawn.java place the
  // String "Red Dawn" in the array below.
  static private final String[] cards = {
    "Red Dawn",
    "Radioactive Waste",
    "Alien Scourge"
  };

  public EvolutionCard getCard(String name) throws Exception {
    for (String card : EvolutionCardFactory.cards) {
      if (name.equalsIgnoreCase(card)) {
        try {
          //Append the name of the card (stripped of whitespaces) to the CARD_PATH
          //of the and look for that java file.
          return (EvolutionCard) Class
            .forName(CARD_PATH + card.replaceAll("\\s", ""))
            .newInstance();
        } catch (Exception e) {
          throw e;
        }
      }
    }
    throw new Exception("Unimplemented card");
  }

  public ArrayList<EvolutionCard> getMonsterCards(String monsterName)
    throws Exception {
    ArrayList<EvolutionCard> cards = new ArrayList<EvolutionCard>();
    for (String card : EvolutionCardFactory.cards) {
      try {
        //Append the name of the card (stripped of whitespaces) to the CARD_PATH
        //of the and look for that java file.
        EvolutionCard evolutionCard = (EvolutionCard) Class
          .forName(CARD_PATH + card.replaceAll("\\s", ""))
          .newInstance();
        if (evolutionCard.getMonster() == monsterName) {
          cards.add(evolutionCard);
        }
      } catch (Exception e) {
        throw e;
      }
    }
    return cards;
  }
}
