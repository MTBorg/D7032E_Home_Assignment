package src.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import src.game.cards.Card;
import src.game.cards.store.AlphaMonster;
import src.game.cards.store.StoreCard;
import src.game.EventQueue;
import src.game.events.AttackEvent;
import src.game.events.BuyEvent;
import src.game.events.BuyRequestEvent;
import src.game.events.Event;
import src.network.Stream;

public class Monster {
  private final int maxHealth = 10;
  private int currentHealth = 10;
  private final String name;
  public int energy = 0;
  public int stars = 0;
  public boolean inTokyo = false;
  ArrayList<Card> cards = new ArrayList<Card>();
  public Stream stream;

  public Monster(String name) {
    this.name = name;
  }

  /**
   * Prints all the monster's cards.
   */
  public String cardsToString() {
    String returnString = "";
    if (cards.size() == 0) return "[NO CARDS]:";
    for (int i = 0; i < cards.size(); i++) {
      returnString += "\t[" + i + "] " + cards.get(i) + ":";
    }
    return returnString;
  }

  /**
   * Attack another monster using this monster.
   *
   * This adds an AttackEvent to the event queue.
   *
   * @param attacked The monster to attack.
   * @param damage The amount of damage the attack does.
   * @param gameState The state of the game.
   */
  public void attackMonster(Monster attacked, int damage, GameState gameState) {
    gameState.pushEvent(new AttackEvent(this, attacked, damage));
  }

  /**
   * Returns whether or not the monster owns a card.
   *
   * @param cardName The card to search for.
   * @return Whether or not the monster has the card.
   */
  public boolean hasCard(String cardName) {
    for (Card card : this.cards) {
      if (card.getName() == cardName) {
        return true;
      }
    }
    return false;
  }

  /**
   * Give the monster a card.
   *
   * @param card The card to give.
   */
  public void giveCard(Card card) {
    this.cards.add(card);
  }

  /**
   * Removes a card from the monster.
   *
   * Does nothing if the monster does not own the card.
   *
   * @param monster The card to remove.
   */
  public void removeCard(Card card) {
    for (int i = 0; i < this.cards.size(); i++) {
      if (this.cards.get(i) == card) {
        this.cards.remove(i);
      }
    }
  }

  /**
   * Buys a card for the monster.
   *
   * This adds a BuyRequestEvent to the event queue.
   *
   * @param card The card to buy.
   * @param gameState The state of the game
   */
  public boolean buyCard(StoreCard card, GameState gameState) {
    gameState.pushEvent(new BuyRequestEvent(this, card));
    return (gameState.peekEventQueue() instanceof BuyEvent);
  }

  public String getName() {
    return this.name;
  }

  public int getMaxHealth() {
    return this.maxHealth;
  }

  public int getCurrentHealth() {
    return this.currentHealth;
  }

  /**
   * Set the current health of the monster.
   *
   * If the amount exceeds the maximum health of the monster it sets the current
   * health to the max healt.
   */
  public void setCurrentHealth(int amount) {
    this.currentHealth = amount > this.maxHealth ? this.maxHealth : amount;
  }
}
