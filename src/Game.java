import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class Game {
  private Deck deck;

  Game() {
    this.deck = new Deck();
  }

  public void loop(ArrayList<Monsters> monsters) {
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
      for (int i = 0; i < monsters.size(); i++) {
        Monsters currentMonster = monsters.get(i);
        if (currentMonster.currentHealth <= 0) {
          currentMonster.inTokyo = false;
          continue;
        }

        // pre: Award a monster in Tokyo 1 star
        if (currentMonster.inTokyo) {
          currentMonster.stars += 1;
        }
        String statusUpdate =
          "You are " +
            currentMonster.name +
            " and it is your turn. Here are the stats";
        for (int count = 0; count < 3; count++) {
          statusUpdate +=
            ":" +
              monsters.get(count).name +
              (
                monsters.get(count).inTokyo ? " is in Tokyo "
                  : " is not in Tokyo "
              );
          statusUpdate +=
            "with " +
              monsters.get(count).currentHealth +
              " health, " +
              monsters.get(count).stars +
              " stars, ";
          statusUpdate +=
            monsters.get(count).energy +
              " energy, and owns the following cards:";
          statusUpdate += monsters.get(count).cardsToString();
        }
        KingTokyoPowerUpServer.sendMessage(
          monsters.get(i),
          statusUpdate + "\n"
        );

        // 1. Roll 6 dice
        ArrayList<Dice> dice = new ArrayList<Dice>();
        dice = Dice.diceRoll(6);

        // 2. Decide which dice to keep
        String rolledDice = "ROLLED:You rolled:\t[1]\t[2]\t[3]\t[4]\t[5]\t[6]:";
        for (int allDice = 0; allDice < dice.size(); allDice++) {
          rolledDice += "\t[" + dice.get(allDice) + "]";
        }
        rolledDice +=
          ":Choose which dice to reroll, separate with comma and in decending order (e.g. 5,4,1   0 to skip)\n";
        String[] reroll = KingTokyoPowerUpServer
          .sendMessage(monsters.get(i), rolledDice)
          .split(",");
        if (Integer.parseInt(reroll[0]) != 0) for (int j = 0; j <
          reroll.length; j++) {
          dice.remove(Integer.parseInt(reroll[j]) - 1);
        }

        // 3. Reroll remaining dice
        dice.addAll(Dice.diceRoll(6 - dice.size()));

        // 4. Decide which dice to keep
        rolledDice = "ROLLED:You rolled:\t[1]\t[2]\t[3]\t[4]\t[5]\t[6]:";
        for (int allDice = 0; allDice < dice.size(); allDice++) {
          rolledDice += "\t[" + dice.get(allDice) + "]";
        }
        rolledDice +=
          ":Choose which dice to reroll, separate with comma and in decending order (e.g. 5,4,1   0 to skip)\n";
        reroll =
          KingTokyoPowerUpServer
            .sendMessage(monsters.get(i), rolledDice)
            .split(",");
        if (Integer.parseInt(reroll[0]) != 0) for (int j = 0; j <
          reroll.length; j++) {
          dice.remove(Integer.parseInt(reroll[j]) - 1);
        }

        // 5. Reroll remaining dice
        dice.addAll(Dice.diceRoll(6 - dice.size()));

        // 6. Sum up totals
        Collections.sort(dice);
        HashMap<Dice, Integer> result = new HashMap<Dice, Integer>();
        for (Dice unique : new HashSet<Dice>(dice)) {
          result.put(unique, Collections.frequency(dice, unique));
        }
        String ok = KingTokyoPowerUpServer.sendMessage(
          monsters.get(i),
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
            if (currentMonster.name.equals("Kong")) {
              //Todo: Add support for more cards.
              //Current support is only for the Red Dawn card
              //Add support for keeping it secret until played
              String power = KingTokyoPowerUpServer.sendMessage(
                monsters.get(i),
                "POWERUP:Deal 2 damage to all others\n"
              );
              for (int mon = 0; mon < monsters.size(); mon++) {
                if (mon != i) {
                  monsters.get(mon).currentHealth += -2;
                }
              }
            }
            if (currentMonster.name.equals("Gigazaur")) {
              //Todo: Add support for more cards.
              //Current support is only for the Radioactive Waste
              //Add support for keeping it secret until played
              String power = KingTokyoPowerUpServer.sendMessage(
                monsters.get(i),
                "POWERUP:Receive 2 energy and 1 health\n"
              );
              currentMonster.energy += 2;
              if (
                currentMonster.currentHealth + 1 >= currentMonster.maxHealth
              ) {
                currentMonster.currentHealth = currentMonster.maxHealth;
              } else {
                currentMonster.currentHealth += 1;
              }
            }
            if (currentMonster.name.equals("Alienoid")) {
              //Todo: Add support for more cards.
              //Current support is only for the Alien Scourge
              //Add support for keeping it secret until played
              String power = KingTokyoPowerUpServer.sendMessage(
                monsters.get(i),
                "POWERUP:Receive 2 stars\n"
              );
              currentMonster.stars += 2;
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
          currentMonster.stars +=
            currentMonster.cardEffect("starsWhenAttacking"); //Alpha Monster
          if (currentMonster.inTokyo) {
            for (int mon = 0; mon < monsters.size(); mon++) {
              int moreDamage = currentMonster.cardEffect("moreDamage"); //Acid Attack
              int totalDamage = result.get(aClaw).intValue() + moreDamage;
              if (
                mon != i && totalDamage > monsters.get(mon).cardEffect("armor")
              ) { //Armor Plating
                monsters.get(mon).currentHealth += -totalDamage;
              }
            }
          } else {
            boolean monsterInTokyo = false;
            for (int mon = 0; mon < monsters.size(); mon++) {
              if (monsters.get(mon).inTokyo) {
                monsterInTokyo = true;
                int moreDamage = currentMonster.cardEffect("moreDamage"); //Acid Attack
                int totalDamage = result.get(aClaw).intValue() + moreDamage;
                if (
                  totalDamage > monsters.get(mon).cardEffect("armor")
                ) monsters.get(mon).currentHealth += -totalDamage; //Armor Plating

                // 6e. If you were outside, then the monster inside tokyo may decide to leave Tokyo
                String answer = KingTokyoPowerUpServer.sendMessage(
                  monsters.get(mon),
                  "ATTACKED:You have " +
                    monsters.get(mon).currentHealth +
                    " health left. Do you wish to leave Tokyo? [YES/NO]\n"
                );
                if (answer.equalsIgnoreCase("YES")) {
                  monsters.get(mon).inTokyo = false;
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
        String answer = KingTokyoPowerUpServer.sendMessage(
          monsters.get(i),
          msg
        );
        int buy = Integer.parseInt(answer);
        if (
          buy > 0 &&
          (
            currentMonster.energy >=
            (deck.store[buy].cost - currentMonster.cardEffect("cardsCostLess"))
          )
        ) { //Alien Metabolism
          if (deck.store[buy].discard) {
            //7a. Play "DISCARD" cards immediately
            currentMonster.stars += deck.store[buy].effect.stars;
          } else currentMonster.cards.add(deck.store[buy]);

          //Deduct the cost of the card from energy
          currentMonster.energy += -(
              deck.store[buy].cost - currentMonster.cardEffect("cardsCostLess")
            ); //Alient Metabolism

          //Draw a new card from the deck to replace the card that was bought
          deck.store[buy] = deck.deck.remove(0);
        }

        //8. Check victory conditions
        // String winner = checkVictoryConditionsStar(monsters);
        // if (winner != ""){
        // for(int player=0; player<monsters.size(); player++) {
        // String victoryByStars = sendMessage(player, "Victory: " + winner + " has won by stars\n");
        // }
        //     System.exit(0);
        // }
        // winner = checkVictoryConditionsAlive(monsters);
        // if (winner != ""){
        // for(int player=0; player<monsters.size(); player++) {
        // String victoryByStars = sendMessage(player, "Victory: " + winner + " has won by stars\n");
        // }
        //     System.exit(0);
        // }
        int alive = 0;
        String aliveMonster = "";
        for (int mon = 0; mon < monsters.size(); mon++) {
          if (monsters.get(mon).stars >= 20) {
            for (int victory = 0; victory < monsters.size(); victory++) {
              String victoryByStars = KingTokyoPowerUpServer.sendMessage(
                monsters.get(victory),
                "Victory: " + monsters.get(mon).name + " has won by stars\n"
              );
            }
            System.exit(0);
          }
          if (monsters.get(mon).currentHealth > 0) {
            alive++;
            aliveMonster = monsters.get(mon).name;
          }
        }
        if (alive == 1) {
          for (int victory = 0; victory < monsters.size(); victory++) {
            String victoryByKills = KingTokyoPowerUpServer.sendMessage(
              monsters.get(victory),
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
}
