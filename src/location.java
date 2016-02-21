/* 
 * Author: Blaks Zeng 
 * Purpose: create a location with x and y
 * Date: 10/13/2014 
 */

public class location
{
	int x, y;

	/* get x and y coordinates */
	public location(int a, int b)
	{
		x = a;
		y = b;
	}

	/* return x cord */
	public int getX()
	{
		return x;
	}

	/* return y cord */
	public int getY()
	{
		return y;
	}

	/* check whther two locations are equal */
	@Override
	public boolean equals(Object b)
	{
		if (b instanceof location)
		{
			/* if equal, its x and y are the same */
			return x == ((location) b).getX() && y == ((location) b).getY();
		} else
			/* return not the same */
			return false;
	}

}
