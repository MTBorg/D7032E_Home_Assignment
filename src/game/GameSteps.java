package src.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import src.game.cards.evolution.EvolutionCard;
import src.game.cards.evolution.PermanentEvolutionCard;
import src.game.cards.evolution.TemporaryEvolutionCard;
import src.game.cards.store.DiscardCard;
import src.game.Dice;
import src.game.events.DiceRollEvent;
import src.game.events.GainHealthEvent;
import src.game.Game;
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

public class GameSteps {

  public static ArrayList<Dice> rollDices(
    GameState gameState,
    Monster monster
  ) {
    ArrayList<Dice> dices = Dice.diceRoll(6);
    gameState.pushEvent(new DiceRollEvent(monster, dices));
    return dices;
  }

  /**
   * Rerolls the missing dice of the dice set (a set contains six dices).
   *
   * @param dices the dice set to reroll
   * @param the state of the game
   * @param monster The monster to reroll the dice
   */
  public static void rerollDices(
    ArrayList<Dice> dices,
    GameState gameState,
    Monster monster
  ) {
    dices.addAll(Dice.diceRoll(6 - dices.size()));
    gameState.pushEvent(new DiceRollEvent(monster, dices));
  }

  /**
   * Prompt the player for which dice to reroll
   *
   * @param monster The player/monster to prompt
   * @param dices The array of dices to reroll from.
   */
  static public String promptReroll(Monster monster, ArrayList<Dice> dices) {
    String rolledDice = "ROLLED:You rolled:\t[1]\t[2]\t[3]\t[4]\t[5]\t[6]:";
    for (int allDice = 0; allDice < dices.size(); allDice++) {
      rolledDice += "\t[" + dices.get(allDice) + "]";
    }
    String choices =
      ":Choose which dices to reroll, separate with comma and in decending order (e.g. 5,4,1   0 to skip)\n";
    String response = Server.sendQuestion(monster.stream, rolledDice + choices);
    return response;
  }

  /**
   * Keep the dice specified according to input.
   *
   * @param input String containing the input from the user
   * @param dices The array of dice from which to keep
   * @param monster The monster which will keep the dice
   */
  static public boolean keepDices(
    String input,
    ArrayList<Dice> dices,
    Monster monster
  ) {
    String[] reroll = input.split(",");
    while (true) {
      try {
        if (Integer.parseInt(reroll[0]) != 0) for (int j = 0; j <
          reroll.length; j++) {
          dices.remove(Integer.parseInt(reroll[j]) - 1);
        }
        return true;
      } catch (NumberFormatException e) {
        Server.sendMessage(monster.stream, "Please enter a valid number! \n");
        return false;
      }
    }
  }

  /**
   * Count hearts and assign health and power up cards.
   *
   * @param monster The monster who rolled the hearts.
   * @param result The map between dice and the amount of said dice
   * @param gameState The state of the game
   */
  public static void countHearts(
    Monster monster,
    HashMap<Dice, Integer> result,
    GameState gameState
  ) {
    if (monster.inTokyo) {
      return;
    }

    // 6a. Hearts = health (max 10 unless a cord increases it)
    Dice aHeart = new Dice(Dice.HEART);
    if (result.containsKey(aHeart)) { //+1 currentHealth per heart, up to maxHealth
      // TODO: Maybe a gain health event shouldn't be added if already at max health
      gameState.pushEvent(
        new GainHealthEvent(monster, result.get(aHeart).intValue())
      );

      // 6b. 3 hearts = power-up
      if (result.get(aHeart).intValue() >= 3) {
        // Deal a power-up card to the currentMonster
        // TODO: Add support for more cards.
        // TODO: Add support for keeping it secret until played
        // Draw from player's evolution deck
        EvolutionCard powerUpCard = gameState
          .deck.evolutionCards.get(monster.getName())
          .remove(0);

        if (powerUpCard instanceof PermanentEvolutionCard) {
          gameState.addEventObserver((PermanentEvolutionCard) powerUpCard);
          monster.giveCard(powerUpCard);
        }

        Server.sendMessage(
          monster.stream,
          "You rolled three hearts and received the card " +
            powerUpCard.getName() +
            "\n"
        );

        // Get a card from the factory and execute it's effect
        if (powerUpCard instanceof TemporaryEvolutionCard) {
          ((TemporaryEvolutionCard) powerUpCard).execute(monster, gameState);
        }
      }
    }
  }

  /**
   * Count ones, twos, and threes and assign stars.
   *
   * @param monster The monster to assign stars
   * @param result The map between dice and the amount of said dice
   */
  public static void countVictoryPoints(
    Monster monster,
    HashMap<Dice, Integer> result
  ) {
    // 6c. 3 of a number = victory points
    for (int num = 1; num < 4; num++) {
      if (result.containsKey(new Dice(num))) if (
        result.get(new Dice(num)).intValue() >= 3
      ) monster.stars += num + (result.get(new Dice(num)).intValue() - 3);
    }
  }

  /** Count claws and attack
   * @param monster The monster who rolled.
   * @param result The map between dice and the amount of said dice.
   * @param gameState The state of the game.
   */
  public static void countClaws(
    Monster monster,
    HashMap<Dice, Integer> result,
    GameState gameState
  ) {
    // 6d. claws = attack (if in Tokyo attack everyone, else attack monster in Tokyo)
    Dice aClaw = new Dice(Dice.CLAWS);
    if (result.containsKey(aClaw)) {
      if (monster.inTokyo) {
        for (int mon = 0; mon < gameState.monsters.size(); mon++) {
          // int moreDamage = monster.cardEffect("moreDamage"); //Acid Attack
          int moreDamage = 0;
          int totalDamage = result.get(aClaw).intValue() + moreDamage;
          if (
            gameState.monsters.get(mon) != monster &&
            totalDamage > gameState.monsters.get(mon).cardEffect("armor")
          ) { //Armor Plating
            monster.attackMonster(
              gameState.monsters.get(mon),
              totalDamage,
              gameState
            );
          }
        }
      } else {
        boolean monsterInTokyo = false;
        for (int mon = 0; mon < gameState.monsters.size(); mon++) {
          if (gameState.monsters.get(mon).inTokyo) {
            monsterInTokyo = true;

            // int moreDamage = monster.cardEffect("moreDamage"); //Acid Attack
            int moreDamage = 0;
            int totalDamage = result.get(aClaw).intValue() + moreDamage;
            if (totalDamage > gameState.monsters.get(mon).cardEffect("armor")) {
              monster.attackMonster(
                gameState.monsters.get(mon),
                totalDamage,
                gameState
              );
            // gameState.monsters.get(mon).getCurrentHealth() += -totalDamage; //Armor Plating
            }

            // 6e. If you were outside, then the monster inside tokyo may decide to leave Tokyo
            String answer = Server.sendQuestion(
              gameState.monsters.get(mon).stream,
              "ATTACKED:You have " +
                gameState.monsters.get(mon).getCurrentHealth() +
                " health left. Do you wish to leave Tokyo? [YES/NO]\n"
            );
            if (answer.equalsIgnoreCase("YES")) {
              gameState.monsters.get(mon).inTokyo = false;
              monsterInTokyo = false;
            }
          }
        }
        if (!monsterInTokyo) {
          monster.inTokyo = true;
          monster.stars += 1;
        }
      }
    }
  }

  //TODO: Not used?
  public static void shop(Monster monster, GameState gameState) {
    // 7. Decide to buy things for energy
    String msg =
      "PURCHASE:Do you want to buy any of the cards from the store? (you have " +
        monster.energy +
        " energy) [#/-1]:" +
        gameState.deck +
        "\n";
    boolean validInput = false;
    while (!validInput) {
      String answer = Server.sendQuestion(monster.stream, msg);
      int buy = Integer.parseInt(answer);
      if (buy >= 0 && buy <= 2) {
        //If card was bought Successfully
        if (monster.buyCard(gameState.deck.store[buy], gameState)) {
          System.out.println("Successfully bought card");

          if (gameState.deck.store[buy].isDiscardCard()) {
            //7a. Play "DISCARD" cards immediately
            ((DiscardCard) gameState.deck.store[buy]).execute(monster, gameState);
          }

          //Draw a new card from the deck to replace the card that was bought
          gameState.deck.store[buy] = null;
          if (gameState.deck.deck.size() != 0) {
            gameState.deck.store[buy] = gameState.deck.deck.remove(0);
          } else {
            //TODO: This should not happen
            System.out.println("Out of cards");
          }
          validInput = true;
        } else {
          Server.sendMessage(monster.stream, "You cannot afford that item\n");
          validInput = false;
        }
      } else if (buy > 2) {
        Server.sendMessage(monster.stream, "Please enter a valid input\n");
      } else if (buy <= -1) {
        validInput = true;
      }
    }
  }

  /**
   * Count and assign energy
   *
   * @param monster The monster who rolled the dices.
   * @param result The map between dice and the amount of said dice.
   */
  static public void countEnergy(
    Monster monster,
    HashMap<Dice, Integer> result
  ) {
    Dice anEnergy = new Dice(Dice.ENERGY);
    if (result.containsKey(anEnergy)) monster.energy +=
      result.get(anEnergy).intValue();
  }

  /**
   * Prompt  a player to shop.
   *
   * @param monster The monster/player to prompt.
   * @param gameState The state of the game.
   */
  static public int shopPrompt(Monster monster, GameState gameState) {
    String msg =
      "PURCHASE:Do you want to buy any of the cards from the store (enter -1 to skip and -2 to reset the store (Cost " +
        Deck.RESET_COST +
        "))? (you have " +
        monster.energy +
        " energy) [#/-1,-2]:" +
        gameState.deck +
        "\n";
    while (true) {
      String answer = Server.sendQuestion(monster.stream, msg);
      int buy = Integer.parseInt(answer);
      if (buy >= 0 && buy <= 2) {
        return buy;
      } else if (buy > 2) {
        Server.sendMessage(monster.stream, "Please enter a valid input\n");
      } else if (buy == -1) {
        return -1;
      } else if (buy <= -2) {
        return -2;
      }
    }
  }

  /**
   * Buy an item for a monster.
   *
   * @param monster The monster who buys the item.
   * @param gameState The state of the game.
   * @param item The store index of the item the monster wishes to buy.
   */
  static public boolean buy(Monster monster, GameState gameState, int item) {
    // 7. Decide to buy things for energy
    if (item >= 0 && item <= 2) {
      //If card was bought Successfully
      if (monster.buyCard(gameState.deck.store[item], gameState)) {
        if (gameState.deck.store[item].isDiscardCard()) {
          //7a. Play "DISCARD" cards immediately
          ((DiscardCard) gameState.deck.store[item]).execute(monster, gameState);
        }

        //Draw a new card from the deck to replace the card that was bought
        gameState.deck.store[item] = null;
        if (gameState.deck.deck.size() != 0) {
          gameState.deck.store[item] = gameState.deck.deck.remove(0);
        } else {
          //TODO: This should not happen
          System.out.println("Out of cards");
        }

        return true;
      } else {
        return false;
      }
    } else if (item == -2) {
      gameState.deck.resetStore(monster);
      return true;
    } else {
      // TODO: Throw exception
      return false;
    }
  }

  public static boolean checkVictoryConditions(GameState gameState) {
    int alive = 0;
    String aliveMonster = "";
    for (int mon = 0; mon < gameState.monsters.size(); mon++) {
      if (gameState.monsters.get(mon).stars >= 20) {
        for (int victory = 0; victory < gameState.monsters.size(); victory++) {
          String victoryByStars = Server.sendQuestion(
            gameState.monsters.get(victory).stream,
            "Victory: " +
              gameState.monsters.get(mon).getName() +
              " has won by stars\n"
          );
        }
        return true;
      }
      if (gameState.monsters.get(mon).getCurrentHealth() > 0) {
        alive++;
        aliveMonster = gameState.monsters.get(mon).getName();
      }
    }
    if (alive == 1) {
      for (int victory = 0; victory < gameState.monsters.size(); victory++) {
        String victoryByKills = Server.sendQuestion(
          gameState.monsters.get(victory).stream,
          "Victory: " + aliveMonster + " has won by being the only one alive\n"
        );
      }
      return true;
    }
    return false;
  }

  public static void checkIfInTokyo(Monster monster) {
    // pre: Award a monster in Tokyo 1 star
    if (monster.inTokyo) {
      monster.stars += 1;
    }
  }
}
