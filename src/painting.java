/* 
 * Author: Blaks Zeng 
 * Purpose: create a painting class that controls the painting work.
 * Date: 10/13/2014 
 */

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

class Painting extends JPanel
{
	/* set up four icons */
	ImageIcon ComputerNotHit = new ImageIcon("computer_shot_miss.png");
	ImageIcon ComputerHit = new ImageIcon("hit_ship.png");
	ImageIcon ship = new ImageIcon("Images/not_hit_ship.png");
	ImageIcon water = new ImageIcon("Images/water.png");

	/* make arrays to store the positions for these variables */
	ArrayList<location> ships;
	ArrayList<location> missHit;
	ArrayList<location> waters;
	ArrayList<location> shipsHit;

	public Painting()
	{
		/* constructor */
		ships = new ArrayList<location>();
		missHit = new ArrayList<location>();
		waters = new ArrayList<location>();
		shipsHit = new ArrayList<location>();

		/* make this panel the same as buttons panel in GUI */
		this.setPreferredSize(new Dimension(380, 380));
		this.setBackground(Color.BLACK);

		/* set up water. water is everywhere */
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				waters.add(new location(i, j));
	}

	/* override painting class */
	@Override
	protected void paintComponent(Graphics g)
	{
		/* set color to draw the small point */
		g.setColor(Color.RED);

		/* set opaque to true */
		this.setOpaque(true);
		super.paintComponents(g);

		/* the lower code will overlap the upper code. */
		/* water first */
		for (location lc : waters)
			g.drawImage(water.getImage(), 15 + 35 * lc.getY(), 15 + 35 * lc.getX(), null);

		/* missHit is the next */
		for (location lc : missHit)
			g.drawImage(ComputerNotHit.getImage(), 15 + 35 * lc.getY(), 15 + 35 * lc.getX(), null);

		/* ship is on water */
		for (location lc : ships)
			g.drawImage(ship.getImage(), 15 + 35 * lc.getY(), 15 + 35 * lc.getX(), null);

		/* shiphit is on ship */
		for (location lc : shipsHit)
			g.fillOval(22 + 35 * lc.getY(), 22 + 35 * lc.getX(), 15, 15);

	}

	/* this function at a ship to arraylist ship */
	public void addShip(int a, int b)
	{
		ships.add(new location(a, b));
		/* repaint the picture */
		repaint();
	}

	/* this function at a hitship to arraylist hitship */
	public void hitShip(int a, int b)
	{
		shipsHit.add(new location(a, b));
		repaint();
	}

	/* this function add a miss hit to the arraylist */
	public void MissShips(int a, int b)
	{
		missHit.add(new location(a, b));
		/* repaint the picture */
		repaint();
	}

}
