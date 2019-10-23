package src.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import src.game.cards.evolution.EvolutionCard;
import src.game.cards.store.StoreCard;
import src.game.factories.EvolutionCardFactory;
import src.game.factories.StoreCardFactory;

public class Deck {
  public ArrayList<StoreCard> deck = new ArrayList<StoreCard>();
  public StoreCard[] store = new StoreCard[3];
  public HashMap<String, ArrayList<EvolutionCard>> evolutionCards;
  private static final int FACEUP_CARDS = 3;

  public Deck(ArrayList<Monster> monsters) {
    this.deck = new StoreCardFactory().getAllCards();
    EvolutionCardFactory evolutionCardFactory = new EvolutionCardFactory();
    evolutionCards = new HashMap<String, ArrayList<EvolutionCard>>();

    for (Monster monster : monsters) {
      try {
        ArrayList<EvolutionCard> cards = evolutionCardFactory.getMonsterCards(
          monster.getName()
        );
        Collections.shuffle(cards);
        evolutionCards.put(monster.getName(), cards);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    Collections.shuffle(deck);

    // Start the game with 3 cards face up in the store
    for (int i = 0; i < FACEUP_CARDS; i++) {
      store[i] = deck.remove(0);
    }
  }

  // Print the store
  public String toString() {
    String returnString = "";
    for (int i = 0; i < FACEUP_CARDS; i++) {
      returnString += "\t[" + i + "] " + store[i] + ":";
    }
    return returnString;
  }
}
