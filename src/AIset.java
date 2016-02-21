import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/* v1: 9/?.2014
 * v2:Date: 10/13/2014 
 * AI's strategy: shoot until both sides are 'O' so that AI wont be cheated by the player.
 * Ai will make sure all ships work like this:
 *  OXXXXO, OXXO and this part is stopped
 */

public class AIset
{
	/* the list of ship */
	ArrayList<Character> ShipList;

	/* target history */
	ArrayList<int[]> targets;

	/* the hash map from ship character to ship length */
	static HashMap<Integer, Character> dict;

	/*
	 * work with targets to determine whether it is hits. they have the same
	 * index
	 */
	ArrayList<int[]> targethit;

	/* How many hits are there */
	int thitcount;

	int combo;
	/* the length of the smallest ship */
	int minimumlength;
	/*
	 * This tells the current direction the target is going to. 0 is to up, 1 is
	 * to right, 2 is to down, 3 is to left.
	 */
	int direction;

	/*
	 * this stores the possible spots for the next stop
	 */
	ArrayList<int[]> possiblespot;

	/*
	 * this is for next step function
	 */
	int thinksteps;;

	public AIset()
	{
		/* constructors */
		ShipList = new ArrayList<Character>();
		targets = new ArrayList<int[]>();
		targethit = new ArrayList<int[]>();
		possiblespot = new ArrayList<int[]>();

		thinksteps = 0;

		/* combo is 0 at the beginning */
		combo = 0;

		/* direction is 0 at the biginning */
		direction = 0;
		dict = new HashMap<Integer, Character>(5);
		thitcount = 0;

		/* min length is 2 for all kinds of ships */
		minimumlength = 2;

		/* PUt the shipsize into dictionary */
		dict.put(5, 'A');
		dict.put(4, 'B');
		dict.put(3, 'C');
		dict.put(2, 'D');
		dict.put(3, 'S');

		/* put the ships into lists */
		ShipList.add('A');
		ShipList.add('B');
		ShipList.add('C');
		ShipList.add('D');
		ShipList.add('S');
	}

	// It is a hit
	public void addhit()
	{
		// add the last hit to it
		targethit.add(targets.get(targets.size() - 1).clone());

		/* totalhit +1 */
		thitcount++;
		/* combo +1 */
		combo++;
	}

	// this function is called when a ship is sank
	public void shipsank()
	{
		if (dict.containsValue(combo))
		// if the ship is in the list
		{
			char ship = dict.get(combo);
			if (ShipList.contains((Character) ship))
			{// if the ship is in the list
				ShipList.remove((Character) ship);
				dict.remove(combo);// remove the ship
				combo = 0;// reset it to 0
				updateminimum();
			}
		} else
		{/*
		 * there must be some problems cheats that program cannot figure out,
		 * step into the very careful mood. the figure will update later
		 */
			minimumlength = 2;
		}
	}

	// check whether this position is a hit
	public boolean isHit(int[] a)
	{
		for (int i = 0; i < targethit.size(); i++)
		{
			/* check all hits and judge if there is one same spot */
			int[] temp = targethit.get(i);
			if (temp[0] == a[0] && temp[1] == a[1])
				/* it is a hit */
				return true;
		}
		/* return it is not a hit */
		return false;
	}

	// exam whether this last time is a hit
	public boolean LastIsHit()
	{
		/* if there is no record for hits, return no */
		if (targethit.size() == 0)
			return false;
		int lastx = targets.get(targets.size() - 1)[0];
		int lasty = targets.get(targets.size() - 1)[1];
		int lasthitx = targethit.get(targethit.size() - 1)[0];
		int lasthity = targethit.get(targethit.size() - 1)[1];
		/* if the last hit is the same spot as the last time's hit, reutrn true */
		return (lastx == lasthitx && lasty == lasthity);
	}

	// check whther a is out of border
	public boolean OutOfBorder(int[] a)
	{
		int x = a[0];
		int y = a[1];
		if (x > 9 || y > 9 || x < 0 || y < 0)
			/* is out of border */
			return true;
		else
			/* not out of border */
			return false;
	}

	/*
	 * This is the main function of AI. this will return the coordinate that
	 * this time where the hit is going to be.
	 */
	public void turnDirection()
	{// turn one
		direction = (direction + 1) % 4;
	}

	public void turnaround()
	{// turn two directions
		direction = (direction + 2) % 4;
	}

	// this function detects whether this place is visited
	public boolean visited(int[] a)
	{
		for (int i = 0; i < targets.size(); i++)
		{
			int[] temp = targets.get(i);
			if (temp[0] == a[0] && temp[1] == a[1])
				/* this place is visited */
				return true;
		}
		/* this place is not visited */
		return false;
	}

	// this is a recursive function to check the right spot to hit
	public int[] nextstep()
	{
		/* create a new location */
		int[] lasthit = targethit.get(targethit.size() - 1);
		int[] nexthit = lasthit.clone();
		// if already thinks too much, return a random position.
		if (thinksteps > 15)
			return randomhit();
		for (int i = 1; i < 5; i++)// because the maxlength is 5
		{
			if (direction == 0)// up
			{
				nexthit[1]++;
			} else if (direction == 1)// right
			{
				nexthit[0]++;
			} else if (direction == 2)// down
			{
				nexthit[1]--;
			} else if (direction == 3)// left
			{
				nexthit[0]--;
			}
			if (isHit(nexthit))// this position is hit. bypass it
				continue;
			else if (OutOfBorder(nexthit))
			{// out of border. stop this direction
				break;
				// it is a not hit position.
			} else if ((visited(nexthit) && !isHit(nexthit)))
				break;
			// if not visited, it is the right point
			else if (!(visited(nexthit)))
			{
				/* if not visited, reset thinkstep and return this point */
				thinksteps = 0;
				return nexthit;
			}
		}
		/* front points are all impossible, test backwards points */
		turnaround();

		nexthit = lasthit.clone();
		for (int i = 1; i < 5; i++)// the maxlength is 5
		{
			if (direction == 0)// up
			{
				nexthit[1]++;
			} else if (direction == 1)// right
			{
				nexthit[0]++;
			} else if (direction == 2)// down
			{
				nexthit[1]--;
			} else if (direction == 3)// left
			{
				nexthit[0]--;
			}

			/* if nexthit is a hit, ignore and continue hit the next one */
			if (isHit(nexthit))
				continue;
			/* if it is out of border, no need to search anymore */
			if (OutOfBorder(nexthit))
				break;
			/* if it is visited and is not hit, no need to search anymore */
			else if ((visited(nexthit) && !isHit(nexthit)))
				break;
			/* if it is nore visited, return this point */
			else if (!(visited(nexthit)))
			{
				thinksteps = 0;
				return nexthit;
			}
		}

		/*
		 * if both sides are of no use, test the following: turn a direction and
		 * hit. two ships are staying together.
		 */

		/* if this line is hit, turn a direction and continue this function */
		turnDirection();

		/* count how many turns has been made */
		thinksteps++;

		/* seek the point on the next line */
		nexthit = nextstep();

		/* reset thinkstep to 0, and return nexthit point */
		thinksteps = 0;
		return nexthit;

	}

	public int[] nexthit()
	{
		int[] nexthit = new int[2];
		/* no part of the ship is hit, random hit */
		if (!LastIsHit() && combo == 0)
		{
			nexthit = randomhit();
		}
		/* turn andh hit until hit the next part */
		else if (combo == 1)
		{
			turnDirection();
			nexthit = nextstep();
		}

		/* if at least two parts are hit, continue in this direction */
		else if (LastIsHit() && combo > 1)
		{
			nexthit = nextstep();
		}

		/* if last is not a hit and combo>1, turn back and hit */
		else if (!LastIsHit() && combo > 1)
		{
			turnaround();
			nexthit = nextstep();
		}

		/* target add this nexthit and return this point */
		targets.add(nexthit.clone());
		return nexthit.clone();

	}

	/* update the minimum length of the ship left */
	public void updateminimum()
	{
		int min = 5;
		for (int i = 0; i < ShipList.size(); i++)
		{
			/* check all ships left for possible length */
			if (min > ShipList.get(i))
				min = ShipList.get(i);
		}
		minimumlength = min;
	}

	/* this function return a random possible position */
	public int[] randomhit()
	{
		Random rand = new Random();

		/* update the possible positions */
		updatepossible();

		int size = possiblespot.size();

		/* if no possible size, hit margin because something is wrong */
		if (size == 0)
			return hitmargin();

		/* else, hit one of the position in random */
		int r = rand.nextInt(possiblespot.size() - 1);
		int[] nexthit = possiblespot.get(r).clone();

		/* return this random point */
		return nexthit.clone();
	}

	/*
	 * If the computer is cheated that actually there is one ship that has one
	 * part left, this function will hit that corner.
	 */
	public int[] hitmargin()
	{
		int[] posi = new int[2];
		for (int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				posi[0] = i;
				posi[1] = j;
				/* visit all spots, only hit point will have corner */
				if (isHit(posi))
				{
					posi[0]++;// right side
					if (!visited(posi))
						/* return this point */
						return posi;

					posi[0]--;// reset to origin
					posi[0]--;// left side
					if (!visited(posi))
						/* return this point */
						return posi;

					posi[0]++;// return back
					posi[1]++;// up
					if (!visited(posi))
						/* return this point */
						return posi;

					posi[1]--;// return back
					posi[1]--;// down
					if (!visited(posi))
						/* return this point */
						return posi;
				}
			}
		}
		/* if not place is found, randomhit a place */
		/* it is not possible for the function to get there */
		minimumlength = 1;
		return randomhit();
	}

	/* this function put all possible spots into an arraylist */
	public void updatepossible()
	{
		/* clear all possible points stored */
		possiblespot.clear();

		int[] temp = new int[2];
		for (int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				temp[0] = i;
				temp[1] = j;
				/* if the point is not visited, check its four sides */
				if (!visited(temp))
				{
					if (possiblespot(temp, 0) || possiblespot(temp, 1) || possiblespot(temp, 2) || possiblespot(temp, 3))
						possiblespot.add(temp.clone());
				}
			}
		}
	}

	/* this function check whether a location and a side is possible */
	private boolean possiblespot(int[] a, int direction)
	{
		for (int k = 1; k < minimumlength; k++)
		{
			/* test a particular direction */
			int[] test = a.clone();
			if (direction == 0)
				test[1]++;
			if (direction == 1)
				test[0]++;
			if (direction == 2)
				test[1]--;
			if (direction == 3)
				test[0]--;
			if (OutOfBorder(test) || visited(test))
			{
				/* return it is not possible */
				return false;
			}
		}
		/* return it is possible */
		return true;
	}

}
