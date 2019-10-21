package src.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import src.events.AttackEvent;
import src.game.cards.store.AlphaMonster;
import src.game.EventQueue;
import src.network.Stream;

public class Monster {
  public int maxHealth = 10;
  public int currentHealth = 10;
  public String name;
  public int energy = 0;
  public int stars = 0;
  public boolean inTokyo = false;
  ArrayList<Card> cards = new ArrayList<Card>();
  public Stream stream;

  // public Socket connection = null;
  // public BufferedReader inFromClient = null;
  // public DataOutputStream outToClient = null;
  public Monster(String name) {
    this.name = name;

    // Always give the player an alphamonster card
    // TODO: Remove this once card buying functionality has been implemented
    cards.add(new AlphaMonster());
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

  public void attackMonster(
    Monster attacked,
    int damage,
    EventQueue eventQueue
  ) {
    eventQueue.add(new AttackEvent(eventQueue, this, attacked, damage));
    eventQueue.get(eventQueue.size() - 1).execute();
  }

  public boolean hasCard(String cardName) {
    for (Card card : this.cards) {
      if (card.name == cardName) {
        return true;
      }
    }
    return false;
  }
}
