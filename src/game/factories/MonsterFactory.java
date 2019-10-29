package src.game.factories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import src.game.Monster;

public class MonsterFactory {
  /**
   * Add the monster names here in order to add them to the game
   */
  public static final String[] monsterNames = {
    "Kong",
    "Gigazaur",
    "Alienoid"
  };

  /**
   * Returns a random subset of all possible monsters defined in the array above.
   *
   * @param amount The amount of monsters to generate
   */
  public static ArrayList<String> getMonsters(int amount) {
    ArrayList<String> monsters = new ArrayList<String>();
    Random rand = new Random();
    List<String> tempNames = new ArrayList<String>(Arrays.asList(monsterNames));

    //Shuffle which monsters will played (if not all)
    Collections.shuffle(tempNames);

    if (amount > monsterNames.length) {
      System.out.println("There are too many players in the game");
      System.exit(0);
    }
    for (int i = 0; i < amount; i++) {
      int index = rand.nextInt(tempNames.size());
      monsters.add(tempNames.get(index));
      tempNames.remove(index);
    }

    return monsters;
  }
}
