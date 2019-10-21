package src.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import src.events.AttackEvent;
import src.events.Event;
import src.factories.EvolutionCardFactory;
import src.game.cards.*;
import src.game.Deck;
import src.game.EventQueue;
import src.game.Monster;
import src.server.KingTokyoPowerUpServer;

public class Game {
  private EventQueue eventQueue;
  private Deck deck;
  private ArrayList<Monster> monsters;
  private final int VICTORY_STARS = 20;

  public Game(ArrayList<Monster> monsters) {
    this.monsters = monsters;
    this.deck = new Deck();
    this.eventQueue = new EventQueue();

    for (Monster monster : monsters) {
      for (Card card : monster.cards) {
        eventQueue.addObserver(card);
      }
    }
  }

  public void loop() {
    /*
            Game loop:
            pre: Award a monster in Tokyo 1 star
            1. Roll 6 dice
            2. Decide which dice to keep
            3. Reroll remaining dice
            4. Decide which dice to keep 
            5. Reroll remaining dice
            6. Sum up totals
              6a. Hearts = health (max 10 unless a cord increases it)
              6b. 3 hearts = power-up
              6c. 3 of a number = victory points
              6d. claws = attack (if in Tokyo attack everyone, else attack monster in Tokyo)
              6e. If you were outside, then the monster inside tokyo may decide to leave Tokyo
              6f. energy = energy tokens
            7. Decide to buy things for energy
              7a. Play "DISCARD" cards immediately
            8. Check victory conditions
        */
    while (true) {
      for (int i = 0; i < this.monsters.size(); i++) {
        Monster currentMonster = this.monsters.get(i);
        if (currentMonster.currentHealth <= 0) {
          currentMonster.inTokyo = false;
          continue;
        }

        // pre: Award a monster in Tokyo 1 star
        if (currentMonster.inTokyo) {
          currentMonster.stars += 1;
        }

        // 1. Roll 6 dices
        ArrayList<Dice> dices = new ArrayList<Dice>();
        dices = Dice.diceRoll(6);

        // 2. Decide which dice to keep
        keepDices(dices, currentMonster);

        // 3. Reroll remaining dice
        dices.addAll(Dice.diceRoll(6 - dices.size()));

        // 4. Decide which dice to keep
        keepDices(dices, currentMonster);

        // 5. Reroll remaining dice
        dices.addAll(Dice.diceRoll(6 - dices.size()));

        // 6. Sum up totals
        Collections.sort(dices);
        HashMap<Dice, Integer> result = new HashMap<Dice, Integer>();
        for (Dice unique : new HashSet<Dice>(dices)) {
          result.put(unique, Collections.frequency(dices, unique));
        }
        String ok = KingTokyoPowerUpServer.sendMessage(
          this.monsters.get(i).stream,
          "ROLLED:You rolled " + result + " Press [ENTER]\n"
        );

        // 6a. Hearts = health (max 10 unless a cord increases it)
        Dice aHeart = new Dice(Dice.HEART);
        if (result.containsKey(aHeart)) { //+1 currentHealth per heart, up to maxHealth
          if (
            currentMonster.currentHealth +
              result.get(aHeart).intValue() >=
              currentMonster.maxHealth
          ) {
            currentMonster.currentHealth = currentMonster.maxHealth;
          } else {
            currentMonster.currentHealth += result.get(aHeart).intValue();
          }

          // 6b. 3 hearts = power-up
          if (result.get(aHeart).intValue() >= 3) {
            // Deal a power-up card to the currentMonster
            //Todo: Add support for more cards.
            //Current support is only for the Red Dawn card
            //Add support for keeping it secret until played
            EvolutionCardFactory factory = new EvolutionCardFactory();
            String card = "";
            switch (currentMonster.name) {
              case "Kong":
                card = "Red Dawn";
                break;
              case "Gigazaur":
                card = "Radioactive Waste";
                break;
              case "Alienoid":
                card = "Alien Scourge";
                break;
            }

            // Get a card from the factory and execute it's effect
            EvolutionCard powerUpCard = null;
            try {
              powerUpCard = factory.getCard(card);
            } catch (Exception e) {
              System.out.println(e);
              System.exit(0);
            }
            if (
              powerUpCard.getDuration() == EvolutionCard.CardDuration.Temporary
            ) {
              powerUpCard.executeEffect(this.monsters);
            }
          }
        }

        // 6c. 3 of a number = victory points
        for (int num = 1; num < 4; num++) {
          if (result.containsKey(new Dice(num))) if (
            result.get(new Dice(num)).intValue() >= 3
          ) currentMonster.stars +=
            num + (result.get(new Dice(num)).intValue() - 3);
        }

        // 6d. claws = attack (if in Tokyo attack everyone, else attack monster in Tokyo)
        Dice aClaw = new Dice(Dice.CLAWS);
        if (result.containsKey(aClaw)) {
          // TODO: Remove this comment once card mechanism has been fixed
          // currentMonster.stars +=
          //   currentMonster.cardEffect("starsWhenAttacking"); //Alpha Monster
          if (currentMonster.inTokyo) {
            for (int mon = 0; mon < this.monsters.size(); mon++) {
              // int moreDamage = currentMonster.cardEffect("moreDamage"); //Acid Attack
              int moreDamage = 0;
              int totalDamage = result.get(aClaw).intValue() + moreDamage;
              if (
                mon != i &&
                totalDamage > this.monsters.get(mon).cardEffect("armor")
              ) { //Armor Plating
                currentMonster.attackMonster(
                  this.monsters.get(mon),
                  totalDamage,
                  this.eventQueue
                );
              }
            }
          } else {
            boolean monsterInTokyo = false;
            for (int mon = 0; mon < this.monsters.size(); mon++) {
              if (this.monsters.get(mon).inTokyo) {
                monsterInTokyo = true;

                // int moreDamage = currentMonster.cardEffect("moreDamage"); //Acid Attack
                int moreDamage = 0;
                int totalDamage = result.get(aClaw).intValue() + moreDamage;
                if (totalDamage > this.monsters.get(mon).cardEffect("armor")) {
                  currentMonster.attackMonster(
                    this.monsters.get(mon),
                    totalDamage,
                    this.eventQueue
                  );
                  this.eventQueue.get(this.eventQueue.size() - 1).execute();
                // this.monsters.get(mon).currentHealth += -totalDamage; //Armor Plating
                }

                // 6e. If you were outside, then the monster inside tokyo may decide to leave Tokyo
                String answer = KingTokyoPowerUpServer.sendMessage(
                  this.monsters.get(mon).stream,
                  "ATTACKED:You have " +
                    this.monsters.get(mon).currentHealth +
                    " health left. Do you wish to leave Tokyo? [YES/NO]\n"
                );
                if (answer.equalsIgnoreCase("YES")) {
                  this.monsters.get(mon).inTokyo = false;
                  monsterInTokyo = false;
                }
              }
            }
            if (!monsterInTokyo) {
              currentMonster.inTokyo = true;
              currentMonster.stars += 1;
            }
          }
        }

        // 6f. energy = energy tokens
        Dice anEnergy = new Dice(Dice.ENERGY);
        if (result.containsKey(anEnergy)) currentMonster.energy +=
          result.get(anEnergy).intValue();

        // 7. Decide to buy things for energy
        String msg =
          "PURCHASE:Do you want to buy any of the cards from the store? (you have " +
            currentMonster.energy +
            " energy) [#/-1]:" +
            this.deck +
            "\n";
        boolean validInput = false;
        while (!validInput) {
          String answer = KingTokyoPowerUpServer.sendMessage(
            this.monsters.get(i).stream,
            msg
          );
          int buy = Integer.parseInt(answer);
          if (buy >= 0 && buy <= 2) {
            if (
              (
                currentMonster.energy >=
                (
                  deck.store[buy].cost -
                  currentMonster.cardEffect("cardsCostLess")
                )
              )
            ) {
              System.out.println(
                "Alien Metabolism " + currentMonster.cardEffect("cardsCostLess")
              );

              //Alien Metabolism
              if (deck.store[buy].discard) {
                //7a. Play "DISCARD" cards immediately
              // currentMonster.stars += deck.store[buy].effect.stars;
              } else {
                Card card = deck.store[buy];
                currentMonster.cards.add(card);
                eventQueue.addObserver(card);
              }

              //Deduct the cost of the card from energy
              currentMonster.energy += -(
                  deck.store[buy].cost -
                  currentMonster.cardEffect("cardsCostLess")
                ); //Alient Metabolism

              //Draw a new card from the deck to replace the card that was bought
              if (deck.deck.size() != 0) {
                deck.store[buy] = deck.deck.remove(0);
              } else {
                //TODO: This should not happen
                System.out.println("Out of cards");
              // System.exit(0);
              }

              validInput = true;
            } else {
              KingTokyoPowerUpServer.sendOneWayMessage(
                currentMonster.stream,
                "Message:You cannot afford that item \n"
              );
            }
          } else if (buy > 2) {
            KingTokyoPowerUpServer.sendOneWayMessage(
              currentMonster.stream,
              "Message:Please enter a valid input\n"
            );
          } else if (buy <= -1) {
            validInput = true;
          }
        }

        //8. Check victory conditions
        // String winner = checkVictoryConditionsStar(this.monsters);
        // if (winner != ""){
        // for(int player=0; player<this.monsters.size(); player++) {
        // String victoryByStars = sendMessage(player, "Victory: " + winner + " has won by stars\n");
        // }
        //     System.exit(0);
        // }
        // winner = checkVictoryConditionsAlive(this.monsters);
        // if (winner != ""){
        // for(int player=0; player<this.monsters.size(); player++) {
        // String victoryByStars = sendMessage(player, "Victory: " + winner + " has won by stars\n");
        // }
        //     System.exit(0);
        // }
        int alive = 0;
        String aliveMonster = "";
        for (int mon = 0; mon < this.monsters.size(); mon++) {
          if (this.monsters.get(mon).stars >= 20) {
            for (int victory = 0; victory < this.monsters.size(); victory++) {
              String victoryByStars = KingTokyoPowerUpServer.sendMessage(
                this.monsters.get(victory).stream,
                "Victory: " +
                  this.monsters.get(mon).name +
                  " has won by stars\n"
              );
            }
            System.exit(0);
          }
          if (this.monsters.get(mon).currentHealth > 0) {
            alive++;
            aliveMonster = this.monsters.get(mon).name;
          }
        }
        if (alive == 1) {
          for (int victory = 0; victory < this.monsters.size(); victory++) {
            String victoryByKills = KingTokyoPowerUpServer.sendMessage(
              this.monsters.get(victory).stream,
              "Victory: " +
                aliveMonster +
                " has won by being the only one alive\n"
            );
          }
          System.exit(0);
        }
      }
    }
  }

  private void keepDices(ArrayList<Dice> dices, Monster monster) {
    String rolledDice = "ROLLED:You rolled:\t[1]\t[2]\t[3]\t[4]\t[5]\t[6]:";
    for (int allDice = 0; allDice < dices.size(); allDice++) {
      rolledDice += "\t[" + dices.get(allDice) + "]";
    }
    String choices =
      ":Choose which dices to reroll, separate with comma and in decending order (e.g. 5,4,1   0 to skip)\n";
    String[] reroll = KingTokyoPowerUpServer
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
          KingTokyoPowerUpServer
            .sendMessage(
              monster.stream,
              "Please enter a valid number! \n" + choices
            )
            .split(",");
      }
    }
  }

  private String checkVictoryConditionsStar(ArrayList<Monster> monsters) {
    //8. Check victory conditions
    for (int mon = 0; mon < monsters.size(); mon++) {
      if (monsters.get(mon).stars >= VICTORY_STARS) {
        return monsters.get(mon).name;
      }
    }
    return "";
  }

  private void sendStatusMessage(
    Monster recipient,
    ArrayList<Monster> monsters
  ) {
    String statusUpdate =
      "You are " + recipient.name + " and it is your turn. Here are the stats";
    for (int count = 0; count < 3; count++) {
      statusUpdate +=
        ":" +
          this.monsters.get(count).name +
          (
            this.monsters.get(count).inTokyo ? " is in Tokyo "
              : " is not in Tokyo "
          );
      statusUpdate +=
        "with " +
          this.monsters.get(count).currentHealth +
          " health, " +
          this.monsters.get(count).stars +
          " stars, ";
      statusUpdate +=
        this.monsters.get(count).energy +
          " energy, and owns the following cards:";
      statusUpdate += this.monsters.get(count).cardsToString();
    }
    KingTokyoPowerUpServer.sendMessage(recipient.stream, statusUpdate + "\n");
  }

  private String checkVictoryConditionsAlive(ArrayList<Monster> monsters) {
    int alive = 0;
    String aliveMonster = "";
    for (int mon = 0; mon < monsters.size(); mon++) {
      if (monsters.get(mon).currentHealth > 0) {
        alive++;
        aliveMonster = monsters.get(mon).name;
      }
    }
    return alive == 1 ? aliveMonster : "";
  }
}
