package src.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Monsters {
  public int maxHealth = 10;
  public int currentHealth = 10;
  public String name;
  public int energy = 0;
  public int stars = 0;
  public boolean inTokyo = false;
  ArrayList<Card> cards = new ArrayList<Card>();
  public Socket connection = null;
  public BufferedReader inFromClient = null;
  public DataOutputStream outToClient = null;

  public Monsters(String name) {
    this.name = name;
  }

  // search all available cards and return the effect value of an effect
  public int cardEffect(String effectName) {
    for (int i = 0; i < cards.size(); i++) {
      try {
        //Find variable by "name"
        if (Effect.class.getField(effectName).getInt(cards.get(i).effect) > 0) {
          return Effect.class.getField(effectName).getInt(cards.get(i).effect);
        }
      } catch (Exception e) {}
    }
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
}
