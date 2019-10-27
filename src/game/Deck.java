package src.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import src.game.cards.evolution.EvolutionCard;
import src.game.cards.store.StoreCard;
import src.game.Deck;
import src.game.factories.EvolutionCardFactory;
import src.game.factories.StoreCardFactory;
import src.server.Server;

public class Deck {
  public ArrayList<StoreCard> deck = new ArrayList<StoreCard>();
  public StoreCard[] store = new StoreCard[3];
  public HashMap<String, ArrayList<EvolutionCard>> evolutionCards;
  private static final int FACEUP_CARDS = 3;
  public static final int RESET_COST = 2;

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

  public void resetStore(Monster monster) {
    Server.broadCastMessage(monster.getName() + " reset the store\n", monster);
    Server.sendOneWayMessage(monster.stream, "You reset the store \n");
    System.out.println(monster.getName() + " reset the store");

    // Place all store cards back in the deck
    for (int i = 0; i < FACEUP_CARDS; i++) {
      this.deck.add(this.store[i]);
    }

    Collections.shuffle(this.deck);

    for (int i = 0; i < FACEUP_CARDS; i++) {
      this.store[i] = this.deck.remove(0);
    }

    monster.energy -= Deck.RESET_COST;
  }
}
