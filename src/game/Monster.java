package src.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
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

  // search all available cards and return the effect value of an effect
  // TODO: This should be a part of the card class
  public int cardEffect(String effectName) {
    //for (int i = 0; i < cards.size(); i++) {
    //  try {
    //    //Find variable by "name"
    //    if (Effect.class.getField(effectName).getInt(cards.get(i).effect) > 0) {
    //      return Effect.class.getField(effectName).getInt(cards.get(i).effect);
    //    }
    //  } catch (Exception e) {}
    //}
    return 0;
  }

  public String cardsToString() {
    String returnString = "";
    if (cards.size() == 0) return "[NO CARDS]:";
    for (int i = 0; i < cards.size(); i++) {
      returnString += "\t[" + i + "] " + cards.get(i) + ":";
    }
    return returnString;
  }

  public void attackMonster(Monster attacked, int damage, GameState gameState) {
    gameState.eventQueue.add(
      new AttackEvent(gameState.eventQueue, this, attacked, damage),
      gameState
    );
    gameState
      .eventQueue.get(gameState.eventQueue.size() - 1)
      .execute(gameState);
  }

  public boolean hasCard(String cardName) {
    for (Card card : this.cards) {
      if (card.name == cardName) {
        return true;
      }
    }
    return false;
  }

  public void giveCard(Card card) {
    this.cards.add(card);
  }

  //TODO: This should maybe return a bool instead
  // (if the monster does not have the card)
  public void removeCard(Card card) {
    for (int i = 0; i < this.cards.size(); i++) {
      if (this.cards.get(i) == card) {
        this.cards.remove(i);
      }
    }
  }

  public boolean buyCard(StoreCard card, GameState gameState) {
    Event buyRequestEvent = new BuyRequestEvent(
      gameState.eventQueue,
      this,
      card
    );
    gameState.eventQueue.add(buyRequestEvent, gameState);
    buyRequestEvent.execute(gameState);
    return (
      gameState.eventQueue.get(
        gameState.eventQueue.size() - 1
      ) instanceof BuyEvent
    );
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

  public void setCurrentHealth(int amount) {
    this.currentHealth = amount > this.maxHealth ? this.maxHealth : amount;
  }
}
