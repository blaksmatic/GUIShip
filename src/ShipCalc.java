/* 
 * Author: Blaks Zeng 
 * Purpose: This class provide two algreithms to calculate ships
 * 			the first is to check whether ships are placed in a right way. second is to generate a ship
 * Date: 10/13/2014 
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ShipCalc
{
	/* make a hash map of a ship to store the ship number */
	static HashMap<Character, Integer> ships;

	public static boolean CheckPossible(char[][] ship)
	{

		/* add dictionary to hashmap */
		ships = new HashMap<Character, Integer>(5);
		ships.put('A', 5);
		ships.put('B', 4);
		ships.put('C', 3);
		ships.put('D', 2);
		ships.put('S', 3);

		/* make a checklist to store all ships */
		ArrayList<Character> check = new ArrayList<Character>();

		/* check whther all the ships have the right length */
		if (!shipcount(ship))
			return false;

		/* while ships are not all checked */
		while (check.size() < 5)
		{
			for (int i = 0; i < 10; i++)
				for (int j = 0; j < 10; j++)
				{
					/* choose a spot which is not a blank */
					char temp = ship[i][j];
					if (temp != 0)
					{
						/* check whether this ship has been checked */
						if (!check.contains(temp))
						{
							/* add this to check */
							check.add(temp);

							/* check if this is placed vertically */
							if (j + 1 < 10 && ship[i][j + 1] == temp)
							{
								/* check whether it is placed vertically */
								int count = 0;
								for (int n = 0; n < 6; n++)
								{
									if (n + j >= 10)
										/* break when it meets the barrier */
										break;

									/* break when it is not this ship any longer */
									if (ship[i][j + n] != temp)
										break;
									count++;
								}
								/* check if the ship has this right length */
								if (count != ships.get(temp))
									return false;

							} else
							{
								/* the ship is placed horizontally */
								int count = 0;
								for (int n = 0; n < 6; n++)
								{
									/* check if it out of border */
									if (n + i >= 10)
										break;

									/* check next is the same ship */
									if (ship[i + n][j] != temp)
										break;

									count++;
								}
								/* check whether the ship is the right length */
								if (count != ships.get(temp))
									return false;
							}
						}

					}
				}
		}
		// System.out.println("This is good!");
		return true;
	}

	public static boolean shipcount(char[][] ship)
	{// This talks about whether you cheat by adding a ship longer than rule

		ships = new HashMap<Character, Integer>(5);
		ships.put('A', 5);
		ships.put('B', 4);
		ships.put('C', 3);
		ships.put('D', 2);
		ships.put('S', 3);

		int[] shipn = new int[5];
		for (int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				char temp = ship[i][j];
				if (temp - 'A' >= 0 && temp - 'A' <= 3)
					shipn[temp - 'A']++;// calculating the types of ships
				else if (temp - 'A' == 18)
					shipn[4]++;// ship S
			}
		}

		for (int i = 0; i < 4; i++)
		{
			char temp = (char) (i + 'A');
			int temp2 = ships.get((Character) temp);
			if (shipn[i] != temp2)// ship ABCD
				/* return the ship count is wrong */
				return false;
		}
		if (shipn[4] != 3)
			return false;// SHip S
		// check finished
		/* reutrn the ship count is right */
		return true;

	}

	public static char[][] generateShip()
	{
		ships = new HashMap<Character, Integer>(5);
		ships.put('A', 5);
		ships.put('B', 4);
		ships.put('C', 3);
		ships.put('D', 2);
		ships.put('S', 3);

		char[][] newship = new char[10][10];
		// add every ship to the list
		ArrayList<Character> check = new ArrayList<Character>();
		check.add('A');
		check.add('B');
		check.add('C');
		check.add('D');
		check.add('S');

		Random rand = new Random();

		while (check.size() > 0)
		{
			// not all ships are on
			char temp = check.remove(0);
			int x = rand.nextInt(9);
			int y = rand.nextInt(9);

			// dir:direction: 0 means vertical. 1 means horizontal
			int dir = rand.nextInt(1);
			boolean rightplace = true;
			if (dir == 1)
			{// horizontal
				if (ships.get((Character) temp) + x > 9)
				{
					check.add(temp);
					rightplace = false;
					// it surpasses, add it back
				} else
				{
					for (int n = 0; n < ships.get(temp); n++)
					{ // check out of border
						// the null of char is '\u0000'
						if (newship[x + n][y] != 0)
						{// not null, add the ship back
							check.add(temp);
							rightplace = false;
							break;// break and redo the placement
						}
					}
				}
				if (rightplace)
				{// if it is a right place, place the ship on
					for (int n = 0; n < ships.get(temp); n++)
					{
						newship[x + n][y] = temp;
					}
				}

			}
			if (dir == 0)// the same as hprizontal. it is vertical
			{
				if (ships.get((Character) temp) + y > 9)
				{
					check.add(temp);
					rightplace = false;
					// it surpasses, add it back
				} else
				{
					for (int n = 0; n < ships.get((Character) temp); n++)
					{ // check out of border
						// the null of char is 0
						if (newship[x][y + n] != 0)
						{// not null, add the ship back
							check.add(temp);
							rightplace = false;
							break;// break and redo the placement
						}
					}
				}
				/* if it is the right place, place the ships on */
				if (rightplace)
				{
					for (int n = 0; n < ships.get(temp); n++)
					{
						newship[x][n + y] = temp;
					}
				}

			}
		}

		/* return the ship built */
		return newship;
	}
}
