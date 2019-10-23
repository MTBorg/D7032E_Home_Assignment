package src.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import src.game.cards.*;
import src.game.cards.evolution.EvolutionCard;
import src.game.cards.evolution.PermanentEvolutionCard;
import src.game.cards.evolution.TemporaryEvolutionCard;
import src.game.cards.store.DiscardCard;
import src.game.cards.store.KeepCard;
import src.game.Deck;
import src.game.EventQueue;
import src.game.events.AttackEvent;
import src.game.events.DiceRollEvent;
import src.game.events.Event;
import src.game.events.GainHealthEvent;
import src.game.factories.EvolutionCardFactory;
import src.game.Monster;
import src.server.Server;

public class Game {
  private GameState state;
  private final int VICTORY_STARS = 20;

  public Game(ArrayList<Monster> monsters) {
    this.state = new GameState(monsters);
    for (Monster monster : this.state.monsters) {
      for (Card card : monster.cards) {
        if (card instanceof KeepCard) {
          this.state.eventQueue.addObserver((KeepCard) card);
        }
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
    System.out.println("Game started!");
    while (true) {
      for (int i = 0; i < this.state.monsters.size(); i++) {
        Monster currentMonster = this.state.monsters.get(i);
        System.out.println("It is " + currentMonster.getName() + "'s turn");
        if (currentMonster.getCurrentHealth() <= 0) {
          currentMonster.inTokyo = false;
          continue;
        }

        // pre: Award a monster in Tokyo 1 star
        if (currentMonster.inTokyo) {
          currentMonster.stars += 1;
        }
        sendStatusMessage(currentMonster, this.state.monsters);

        // 1. Roll 6 dices
        ArrayList<Dice> dices = new ArrayList<Dice>();
        dices = Dice.diceRoll(6);
        this.state.eventQueue.add(
            new DiceRollEvent(this.state.eventQueue, currentMonster, dices),
            this.state
          );
        this.state.eventQueue.get(this.state.eventQueue.size() - 1)
          .execute(this.state);

        // 2. Decide which dice to keep
        keepDices(dices, currentMonster);

        // 3. Reroll remaining dice
        dices.addAll(Dice.diceRoll(6 - dices.size()));
        this.state.eventQueue.add(
            new DiceRollEvent(this.state.eventQueue, currentMonster, dices),
            this.state
          );
        this.state.eventQueue.get(this.state.eventQueue.size() - 1)
          .execute(this.state);

        // 4. Decide which dice to keep
        keepDices(dices, currentMonster);

        // 5. Reroll remaining dice
        dices.addAll(Dice.diceRoll(6 - dices.size()));
        this.state.eventQueue.add(
            new DiceRollEvent(this.state.eventQueue, currentMonster, dices),
            this.state
          );
        this.state.eventQueue.get(this.state.eventQueue.size() - 1)
          .execute(this.state);

        // 6. Sum up totals
        Collections.sort(dices);
        HashMap<Dice, Integer> result = new HashMap<Dice, Integer>();
        for (Dice unique : new HashSet<Dice>(dices)) {
          result.put(unique, Collections.frequency(dices, unique));
        }
        String ok = Server.sendMessage(
          this.state.monsters.get(i).stream,
          "ROLLED:You rolled " + result + " Press [ENTER]\n"
        );

        // 6a. Hearts = health (max 10 unless a cord increases it)
        countHearts(currentMonster, result);

        // 6c. 3 of a number = victory points
        this.countVictoryPoints(currentMonster, result);

        // 6d. claws = attack (if in Tokyo attack everyone, else attack monster in Tokyo)
        Dice aClaw = new Dice(Dice.CLAWS);
        if (result.containsKey(aClaw)) {
          if (currentMonster.inTokyo) {
            for (int mon = 0; mon < this.state.monsters.size(); mon++) {
              // int moreDamage = currentMonster.cardEffect("moreDamage"); //Acid Attack
              int moreDamage = 0;
              int totalDamage = result.get(aClaw).intValue() + moreDamage;
              if (
                mon != i &&
                totalDamage > this.state.monsters.get(mon).cardEffect("armor")
              ) { //Armor Plating
                currentMonster.attackMonster(
                  this.state.monsters.get(mon),
                  totalDamage,
                  this.state.eventQueue,
                  this.state
                );
              }
            }
          } else {
            boolean monsterInTokyo = false;
            for (int mon = 0; mon < this.state.monsters.size(); mon++) {
              if (this.state.monsters.get(mon).inTokyo) {
                monsterInTokyo = true;

                // int moreDamage = currentMonster.cardEffect("moreDamage"); //Acid Attack
                int moreDamage = 0;
                int totalDamage = result.get(aClaw).intValue() + moreDamage;
                if (
                  totalDamage > this.state.monsters.get(mon).cardEffect("armor")
                ) {
                  currentMonster.attackMonster(
                    this.state.monsters.get(mon),
                    totalDamage,
                    this.state.eventQueue,
                    this.state
                  );
                  this.state.eventQueue.get(this.state.eventQueue.size() - 1)
                    .execute(this.state);
                // this.state.monsters.get(mon).getCurrentHealth() += -totalDamage; //Armor Plating
                }

                // 6e. If you were outside, then the monster inside tokyo may decide to leave Tokyo
                String answer = Server.sendMessage(
                  this.state.monsters.get(mon).stream,
                  "ATTACKED:You have " +
                    this.state.monsters.get(mon).getCurrentHealth() +
                    " health left. Do you wish to leave Tokyo? [YES/NO]\n"
                );
                if (answer.equalsIgnoreCase("YES")) {
                  this.state.monsters.get(mon).inTokyo = false;
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
            this.state.deck +
            "\n";
        boolean validInput = false;
        while (!validInput) {
          String answer = Server.sendMessage(
            this.state.monsters.get(i).stream,
            msg
          );
          int buy = Integer.parseInt(answer);
          if (buy >= 0 && buy <= 2) {
            //If card was bought Successfully
            if (
              currentMonster.buyCard(
                this.state.eventQueue,
                this.state.deck.store[buy],
                this.state
              )
            ) {
              System.out.println("Successfully bought card");

              if (this.state.deck.store[buy].isDiscardCard()) {
                //7a. Play "DISCARD" cards immediately
                ((DiscardCard) this.state.deck.store[buy]).execute(currentMonster, this.state);
              }

              //Draw a new card from the deck to replace the card that was bought
              this.state.deck.store[buy] =
                null;
              if (this.state.deck.deck.size() != 0) {
                this.state.deck.store[buy] = this.state.deck.deck.remove(0);
              } else {
                //TODO: This should not happen
                System.out.println("Out of cards");
              }
              validInput = true;
            } else {
              Server.sendOneWayMessage(
                currentMonster.stream,
                "You cannot afford that item\n"
              );
              validInput = false;
            }
          } else if (buy > 2) {
            Server.sendOneWayMessage(
              currentMonster.stream,
              "Please enter a valid input\n"
            );
          } else if (buy <= -1) {
            validInput = true;
          }
        }

        //8. Check victory conditions
        // String winner = checkVictoryConditionsStar(this.state.monsters);
        // if (winner != ""){
        // for(int player=0; player<this.state.monsters.size(); player++) {
        // String victoryByStars = sendMessage(player, "Victory: " + winner + " has won by stars\n");
        // }
        //     System.exit(0);
        // }
        // winner = checkVictoryConditionsAlive(this.state.monsters);
        // if (winner != ""){
        // for(int player=0; player<this.state.monsters.size(); player++) {
        // String victoryByStars = sendMessage(player, "Victory: " + winner + " has won by stars\n");
        // }
        //     System.exit(0);
        // }
        int alive = 0;
        String aliveMonster = "";
        for (int mon = 0; mon < this.state.monsters.size(); mon++) {
          if (this.state.monsters.get(mon).stars >= 20) {
            for (int victory = 0; victory <
              this.state.monsters.size(); victory++) {
              String victoryByStars = Server.sendMessage(
                this.state.monsters.get(victory).stream,
                "Victory: " +
                  this.state.monsters.get(mon).getName() +
                  " has won by stars\n"
              );
            }
            System.exit(0);
          }
          if (this.state.monsters.get(mon).getCurrentHealth() > 0) {
            alive++;
            aliveMonster = this.state.monsters.get(mon).getName();
          }
        }
        if (alive == 1) {
          for (int victory = 0; victory <
            this.state.monsters.size(); victory++) {
            String victoryByKills = Server.sendMessage(
              this.state.monsters.get(victory).stream,
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

  private void countVictoryPoints(
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

  private void countHearts(Monster monster, HashMap<Dice, Integer> result) {
    // 6a. Hearts = health (max 10 unless a cord increases it)
    Dice aHeart = new Dice(Dice.HEART);
    if (result.containsKey(aHeart)) { //+1 currentHealth per heart, up to maxHealth
      // TODO: Maybe a gain health event shouldn't be added if already at max health
      this.state.eventQueue.add(
          new GainHealthEvent(
            this.state.eventQueue,
            monster,
            result.get(aHeart).intValue()
          ),
          this.state
        );
      this.state.eventQueue.get(this.state.eventQueue.size() - 1)
        .execute(this.state);

      // 6b. 3 hearts = power-up
      if (result.get(aHeart).intValue() >= 3) {
        // Deal a power-up card to the currentMonster
        // TODO: Add support for more cards.
        // TODO: Add support for keeping it secret until played
        // Draw from player's evolution deck
        EvolutionCard powerUpCard =
          this.state.deck.evolutionCards.get(monster.getName()).remove(0);

        if (powerUpCard instanceof PermanentEvolutionCard) {
          this.state.eventQueue.addObserver(
              (PermanentEvolutionCard) powerUpCard
            );
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
          ((TemporaryEvolutionCard) powerUpCard).execute(monster, this.state);
        }
      }
    }
  }

  private String checkVictoryConditionsStar(ArrayList<Monster> monsters) {
    //8. Check victory conditions
    for (int mon = 0; mon < monsters.size(); mon++) {
      if (monsters.get(mon).stars >= VICTORY_STARS) {
        return monsters.get(mon).getName();
      }
    }
    return "";
  }

  private void sendStatusMessage(
    Monster recipient,
    ArrayList<Monster> monsters
  ) {
    String statusUpdate =
      "You are " +
        recipient.getName() +
        " and it is your turn. Here are the stats";
    for (int count = 0; count < monsters.size(); count++) {
      statusUpdate +=
        ":" +
          this.state.monsters.get(count).getName() +
          (
            this.state.monsters.get(count).inTokyo ? " is in Tokyo "
              : " is not in Tokyo "
          );
      statusUpdate +=
        "with " +
          this.state.monsters.get(count).getCurrentHealth() +
          " health, " +
          this.state.monsters.get(count).stars +
          " stars, ";
      statusUpdate +=
        this.state.monsters.get(count).energy +
          " energy, and owns the following cards:";
      statusUpdate += this.state.monsters.get(count).cardsToString();
    }
    Server.sendMessage(recipient.stream, statusUpdate + "\n");
  }

  private String checkVictoryConditionsAlive(ArrayList<Monster> monsters) {
    int alive = 0;
    String aliveMonster = "";
    for (int mon = 0; mon < monsters.size(); mon++) {
      if (monsters.get(mon).getCurrentHealth() > 0) {
        alive++;
        aliveMonster = monsters.get(mon).getName();
      }
    }
    return alive == 1 ? aliveMonster : "";
  }
}
