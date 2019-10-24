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
import src.game.GameSteps;
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
          this.state.addEventObserver((KeepCard) card);
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

        HashMap<Dice, Integer> result = GameSteps.diceRoll(
          this.state,
          currentMonster
        );

        // 6a. Hearts = health (max 10 unless a cord increases it)
        GameSteps.countHearts(currentMonster, result, this.state);

        // 6c. 3 of a number = victory points
        GameSteps.countVictoryPoints(currentMonster, result);

        // 6d. claws = attack (if in Tokyo attack everyone, else attack monster in Tokyo)
        GameSteps.countClaws(currentMonster, result, this.state);

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
              currentMonster.buyCard(this.state.deck.store[buy], this.state)
            ) {
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
