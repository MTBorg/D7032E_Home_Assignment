package src.game;

import java.util.ArrayList;
import java.util.Collections;
import src.game.cards.store.StoreCard;
import src.game.factories.StoreCardFactory;

public class Deck {
  public ArrayList<StoreCard> deck = new ArrayList<StoreCard>();
  public StoreCard[] store = new StoreCard[3];
  private static final int FACEUP_CARDS = 3;

  public Deck() {
    this.deck = new StoreCardFactory().getAllCards();
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
