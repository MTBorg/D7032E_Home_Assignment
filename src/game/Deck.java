package src.game;

import java.util.ArrayList;
import java.util.Collections;
import src.game.cards.store.StoreCard;
import src.game.factories.StoreCardFactory;

public class Deck {
  public ArrayList<StoreCard> deck = new ArrayList<StoreCard>();
  public StoreCard[] store = new StoreCard[3];
  private static final int FACEUP_CARDS = 3; //TODO: Reset back to three once cards have been implemented

  public Deck() {
    // Effect moreDamage = new Effect();
    // moreDamage.moreDamage = 1;
    // Effect cardsCostLess = new Effect();
    // cardsCostLess.cardsCostLess = 1;
    // Effect starsWhenAttacking = new Effect();
    // starsWhenAttacking.starsWhenAttacking = 1;
    // Effect stars3 = new Effect();
    // stars3.stars = 3;
    // Effect armor = new Effect();
    // armor.armor = 1;
    // Effect stars2 = new Effect();
    // stars2.stars = 2;
    // Effect stars1 = new Effect();
    // stars1.stars = 1;
    // deck.add(
    //   new Card(
    //     "Acid Attack",
    //     6,
    //     false,
    //     moreDamage,
    //     "Deal 1 extra damage each turn"
    //   )
    // );
    // deck.add(
    //   new Card(
    //     "Alien Metabolism",
    //     3,
    //     false,
    //     cardsCostLess,
    //     "Buying cards costs you 1 less"
    //   )
    // );
    // deck.add(
    //   new Card(
    //     "Alpha Monster",
    //     5,
    //     false,
    //     starsWhenAttacking,
    //     "Gain 1 star when you attack"
    //   )
    // );
    // deck.add(new Card("Apartment Building", 5, true, stars3, "+3 stars"));
    // deck.add(new Card("Armor Plating", 4, false, armor, "Ignore damage of 1"));
    // deck.add(new Card("Commuter Train", 4, true, stars2, "+2 stars"));
    // deck.add(new Card("Corner Stone", 3, true, stars1, "+1 stars"));
    //Todo: Add more cards
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
