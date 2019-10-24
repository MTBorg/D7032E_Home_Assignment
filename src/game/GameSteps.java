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
import src.game.GameState;
import src.game.Monster;
import src.server.Server;

public class GameSteps {

  public static HashMap<Dice, Integer> diceRoll(
    GameState gameState,
    Monster monster
  ) {
    HashMap<Dice, Integer> result = new HashMap<Dice, Integer>();

    // 1. Roll 6 dices
    ArrayList<Dice> dices = new ArrayList<Dice>();
    dices = Dice.diceRoll(6);
    gameState.pushEvent(new DiceRollEvent(monster, dices));

    // 2. Decide which dice to keep
    keepDices(dices, monster);

    // 3. Reroll remaining dice
    dices.addAll(Dice.diceRoll(6 - dices.size()));
    gameState.pushEvent(new DiceRollEvent(monster, dices));

    // 4. Decide which dice to keep
    keepDices(dices, monster);

    // 5. Reroll remaining dice
    dices.addAll(Dice.diceRoll(6 - dices.size()));
    gameState.pushEvent(new DiceRollEvent(monster, dices));

    // 6. Sum up totals
    Collections.sort(dices);
    for (Dice unique : new HashSet<Dice>(dices)) {
      result.put(unique, Collections.frequency(dices, unique));
    }
    String ok = Server.sendMessage(
      monster.stream,
      "ROLLED:You rolled " + result + " Press [ENTER]\n"
    );

    return result;
  }

  static private void keepDices(ArrayList<Dice> dices, Monster monster) {
    String rolledDice = "ROLLED:You rolled:\t[1]\t[2]\t[3]\t[4]\t[5]\t[6]:";
    for (int allDice = 0; allDice < dices.size(); allDice++) {
      rolledDice += "\t[" + dices.get(allDice) + "]";
    }
    String choices =
      ":Choose which dices to reroll, separate with comma and in decending order (e.g. 5,4,1   0 to skip)\n";
    String[] reroll = Server
      .sendMessage(monster.stream, rolledDice + choices)
      .split(",");
    while (true) {
      try {
        if (Integer.parseInt(reroll[0]) != 0) for (int j = 0; j <
          reroll.length; j++) {
          dices.remove(Integer.parseInt(reroll[j]) - 1);
        }
        break;
      } catch (NumberFormatException e) {
        reroll =
          Server
            .sendMessage(
              monster.stream,
              "Please enter a valid number! \n" + choices
            )
            .split(",");
      }
    }
  }

  public static void countHearts(
    Monster monster,
    HashMap<Dice, Integer> result,
    GameState gameState
  ) {
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
          gameState.addEventObserver(powerUpCard);
          monster.giveCard(powerUpCard);
        }

        Server.sendOneWayMessage(
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
            String answer = Server.sendMessage(
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
      String answer = Server.sendMessage(monster.stream, msg);
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
          Server.sendOneWayMessage(
            monster.stream,
            "You cannot afford that item\n"
          );
          validInput = false;
        }
      } else if (buy > 2) {
        Server.sendOneWayMessage(
          monster.stream,
          "Please enter a valid input\n"
        );
      } else if (buy <= -1) {
        validInput = true;
      }
    }
  }
}
