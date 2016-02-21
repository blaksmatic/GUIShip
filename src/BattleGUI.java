/* 
 * Author: Blaks Zeng 
 * Purpose: This class is for the BattleshipGUI. It controls everything about frame and button except for the painting . 
 * Date: 10/13/2014 
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

/* this is controls each one of the grid. make a panel for each one */

public class BattleGUI extends JFrame
{
	/*
	 * BattleMap is the buttons for players to click, PlayerMap is for Computers
	 * to click, OKButton is for Players to click
	 */
	JButton BattleMap[][], PlayerMap[][], OKButton;

	/* AIpanel is for AI to display its ship */
	/* Playerpanel is for Player to see AI hit the ships */
	/* Info Panel is to display information */
	JPanel AIpanel, PlayerPanel, InfoPanel;

	/* this is the painting map to show AI's hit */
	Painting AIMaps = new Painting();

	/* PlayAgain windows */
	JFrame PlayAgain;

	/* four images to display the ships' status */
	ImageIcon notHitShip, hitShip, computerShotMiss, water;

	/* the char[][] to represent the ships */
	char AIships[][], PLships[][];

	/* the labels for info panels */
	/* more information about the difference is below */
	JLabel Info1, Info2, Info3;

	/* visits stores the location that visited */
	ArrayList<location> visits;

	/* AI to hit the ships */
	AIset AI;

	public BattleGUI()
	{
		/* the battle before. player can press it */
		BattleMap = new JButton[10][10];
		PlayerMap = new JButton[10][10];
		OKButton = new JButton();

		/* this is the main panel that include two ships in */
		AIpanel = new JPanel();
		PlayerPanel = new JPanel();

		/* the information given to the player */
		InfoPanel = new JPanel();
		Info1 = new JLabel();
		Info2 = new JLabel();
		Info3 = new JLabel();
		InfoPanel.add(Info1);
		InfoPanel.add(Info2);
		InfoPanel.add(Info3);

		/* info1 is going to give main directions like whose turn it is */
		Info1.setPreferredSize(new Dimension(340, 50));

		/* info2 is going to give you your result */
		Info2.setPreferredSize(new Dimension(340, 30));

		/* info3 is going to give you the computer's result */
		Info3.setPreferredSize(new Dimension(340, 30));

		/* PlayAgain panel to ask whether to play again */
		PlayAgain = new JFrame();

		/* initiate AIships and PLships */
		AIships = new char[10][10];
		PLships = new char[10][10];

		/* set the icons */
		notHitShip = new ImageIcon("not_hit_ship.png");
		hitShip = new ImageIcon("hit_ship.png");
		computerShotMiss = new ImageIcon("computer_shot_miss.png");
		water = new ImageIcon("water.png");

		/* ArrayList for visits */
		visits = new ArrayList<location>();

		/* set two panels as flowLayout */
		AIpanel.setVisible(true);
		PlayerPanel.setVisible(true);

		AIpanel.setLayout(new FlowLayout(1, 5, 5));
		AIpanel.setPreferredSize(new Dimension(380, 380));

		PlayerPanel.setLayout(new FlowLayout(1, 5, 5));
		PlayerPanel.setPreferredSize(new Dimension(380, 380));

		/* Initialize the Buttons for the OKButton */
		OKButton.setPreferredSize(new Dimension(200, 20));
		OKButton.setMargin(new Insets(1, 1, 1, 1));
		OKButton.setText("OK");
		OKButton.addActionListener(new nextAction());
		OKButton.setVisible(true);
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
			{
				/* initialize the buttons for battles and players */
				BattleMap[i][j] = new JButton();
				PlayerMap[i][j] = new JButton();

				BattleMap[i][j].setPreferredSize(new Dimension(30, 30));
				BattleMap[i][j].setMargin(new Insets(0, 0, 0, 0));

				PlayerMap[i][j].setPreferredSize(new Dimension(30, 30));
				PlayerMap[i][j].setMargin(new Insets(0, 0, 0, 0));

				AIpanel.add(BattleMap[i][j]);
				PlayerPanel.add(PlayerMap[i][j]);
			}

		/* set Layout */
		this.setVisible(true);
		this.setTitle("BattleShip");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		/* set size and layout for panels */
		this.setPreferredSize(new Dimension(1400, 430));
		this.setLayout(new FlowLayout());

		/* make a temp panel here to add info and OK in */
		JPanel temp = new JPanel();
		temp.setPreferredSize(new Dimension(500, 400));
		temp.setLayout(new FlowLayout(1, 0, 0));
		temp.add(InfoPanel);
		temp.add(OKButton);
		this.add(AIpanel);
		this.add(temp);

		/* add the info panel in */
		InfoPanel.setPreferredSize(new Dimension(390, 120));
		// this.add(OKButton);
		this.add(PlayerPanel);

		/* pack everything up */
		this.pack();

	}

	public void PrepareGame()
	{
		/* if there is AIMaps, this will remove that. else this will do nothing */
		this.getContentPane().remove(AIMaps);
		PlayerPanel.setVisible(true);
		PlayerPanel.setEnabled(true);
		this.revalidate();
		this.repaint();
		pack();

		/* lock Player so that they will not do click jobs */
		lockPlayer();

		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
			{
				/* add listener to each PlayerButton to input ships */
				PlayerMap[i][j].addActionListener(new InputShips(i, j));
			}

		/* remove the former actionlistener "hitShip" listener */
		for (int i = 0; i < 10; i++)
			for (JButton currentbutton : BattleMap[i])
			{
				for (ActionListener al : currentbutton.getActionListeners())
					currentbutton.removeActionListener(al);
			}

		/* Html format for settext */
		OKButton.setVisible(true);

		/* clear the visits */
		visits.clear();

		/* set message */
		Info1.setText("<html>Please input your input at right by pressing the buttons. <br/> Press multiple times will change the letter of the ship.<br/> Press 'OK' when finished.</html>");
	}

	public void StartGame()
	{
		/* add the painting panel and delete the input panel */
		AIMaps = new Painting();
		PlayerPanel.setVisible(false);
		PlayerPanel.setEnabled(false);

		/* remove the player panel so that you will not see it */
		this.remove(PlayerPanel);
		this.add(AIMaps);
		this.revalidate();
		pack();

		/* Players can now shoot the Ships */
		unlockPlayer();

		/* Initialize new AI */
		AI = new AIset();

		/* generate new ships */
		AIships = ShipCalc.generateShip();

		/* draw your ships for the computer part */
		drawShips();
		Info1.setText("Now Choose the Ship You Want To Hit");

		/* remove the former actionlistener "input" listener */
		for (int i = 0; i < 10; i++)
			for (JButton currentbutton : PlayerMap[i])
			{
				for (ActionListener al : currentbutton.getActionListeners())
					currentbutton.removeActionListener(al);
			}

		/* add new actionlistener */
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
			{
				/* add actionlisterner to every buttons. */
				BattleMap[i][j].addActionListener(new HitShip(i, j));
			}
	}

	/* this function lock player's action */
	public void lockPlayer()
	{
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
			{
				BattleMap[i][j].setEnabled(false);
			}
	}

	/* this function unlocks the Player's action */
	public void unlockPlayer()
	{
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
			{
				BattleMap[i][j].setEnabled(true);
			}
	}

	/* check whether player win the game */
	public boolean GameOverPlayerWins()
	{
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
			{
				/* if any of the ship has left */
				if ((AIships[i][j] - 'A' >= 0 && AIships[i][j] - 'A' <= 3) || AIships[i][j] - 'A' == 18)
				{
					return false;
				}
			}
		return true;
	}

	/* check whther computer wins the game */
	public boolean GameOverAIWins()
	{
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
			{
				/* if any of the ship has left */
				if ((PLships[i][j] - 'A' >= 0 && PLships[i][j] - 'A' <= 3) || PLships[i][j] - 'A' == 18)
				{
					return false;
				}
			}
		return true;
	}

	/* clear everything on the table. use for reset the table */
	public void clearTable()
	{
		this.remove(AIMaps);
		this.add(PlayerPanel);
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
			{
				/* clear all ships and labels */
				BattleMap[i][j].setText(null);
				BattleMap[i][j].setIcon(null);
				PlayerMap[i][j].setText(null);
				AIships[i][j] = 0;
				PLships[i][j] = 0;
			}
		/* set text to null */
		Info1.setText(null);
		Info2.setText(null);
		Info3.setText(null);

	}

	/* this draws your ships for the AI to hit */
	public void drawShips()
	{
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
			{
				/* set up Player's ships */
				char temp = PLships[i][j];
				if (temp != 0)
				{
					/* the ship is not hit */
					AIMaps.addShip(i, j);
				}

			}

	}

	/* check whther the player wants to play again */
	public void checkForPlayAgain()
	{
		/* a new JFrame */
		PlayAgain = new JFrame();
		PlayAgain.setPreferredSize(new Dimension(400, 200));
		PlayAgain.setLayout(new FlowLayout());
		PlayAgain.setTitle("Play Again?");

		/* the message */
		JLabel inforPlayAgain = new JLabel("Do you want to play again?");
		PlayAgain.add(inforPlayAgain);
		JButton yes = new JButton("Yes");
		JButton no = new JButton("No");
		PlayAgain.add(yes);
		PlayAgain.add(no);

		/* attach actionlistener to buttons */
		yes.addActionListener(new PlayAgain());
		no.addActionListener(new Exit());
		PlayAgain.setDefaultCloseOperation(EXIT_ON_CLOSE);
		PlayAgain.setVisible(true);

		/* pack it up */
		PlayAgain.pack();
	}

	/* if the player wants to play again */
	public class PlayAgain implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent exitgame)
		{
			/* close the windows */
			PlayAgain.setVisible(false);

			/* clear the table and prepare the game */
			clearTable();
			PrepareGame();
		}

	}

	/* exit the game */
	public class Exit implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent exitgame)
		{
			/* exit the game */
			System.exit(0);
		}

	}

	/* the game begins and player click yes */
	public class nextAction implements ActionListener
	{
		public void actionPerformed(ActionEvent ok)
		{
			if (ShipCalc.CheckPossible(PLships))
			{

				OKButton.setVisible(false);
				Info2.setText(null);
				StartGame();
			} else
			{
				/* the text are not going to change until game begins */
				Info2.setText("Invalid Ships! Check it and press OK!");
			}
		}
	}

	public class HitShip implements ActionListener
	{
		/* x, y for coordinates */
		int x, y;

		public HitShip(int horizon, int vertical)
		{
			x = horizon;
			y = vertical;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			repaint();
			/* do not allowed clicked the same spot */
			if (isVisited(new location(x, y)))
			{
				Info2.setText("You have Clicked this before! Invalid!");
			} else
			{
				/* add to visits */
				visits.add(new location(x, y));
				char temp = AIships[x][y];
				int temp2 = AIships[x][y] - 'A';

				/* determine whether this is a ship */
				if ((temp2 >= 0 && temp2 <= 3) || temp2 == 18)
				{
					AIships[x][y] = 'X';

					/* initialize sank */
					boolean sank = true;

					/* check if it is a sank */
					/* check if it the last one of its type */
					label: for (int i = 0; i < 10; i++)
						for (int j = 0; j < 10; j++)
						{
							/* not the last one */
							if (AIships[i][j] == temp)
							{
								sank = false;
								/* break the whole loop */
								break label;
							}
						}

					/* if it is sank */
					if (sank)
					{
						Info2.setText("You Have Hit and Sank a Ship!");
					} else
						Info2.setText("You Hit a Ship!");

					BattleMap[x][y].setIcon(hitShip);
				} else
				{
					/* not Hit */
					Info2.setText("You missed!");
					BattleMap[x][y].setIcon(water);
				}
				if (GameOverPlayerWins())
				{
					Info1.setText("You Win!");
					checkForPlayAgain();
				} else
				{
					lockPlayer();

					/* set an AI */
					int[] a = AI.nexthit();
					int m = a[0];
					int n = a[1];

					/* AI hit a point */
					temp = PLships[m][n];
					temp2 = PLships[m][n] - 'A';

					/* if this is a ship */
					if ((temp2 >= 0 && temp2 <= 3) || temp2 == 18)
					{
						AI.addhit();
						AIMaps.hitShip(m, n);
						PLships[m][n] = 'X';
						boolean sank = true;
						/* check if it is a sank */
						label: for (int i = 0; i < 10; i++)
							for (int j = 0; j < 10; j++)
							{
								if (AIships[i][j] == temp)
								{
									sank = false;
									break label;
								}
							}

						/* if it is sank */
						if (sank)
						{
							/* give information to AI */
							AI.shipsank();
							/* give information to user */
							Info3.setText("AI Has Hit and Sank a Ship at " + (n + 1) + " " + (m + 1));
						} else
							/* not sank , just hit a ship */
							Info3.setText("AI Hit a Ship at " + (n + 1) + " " + (m + 1));

						/* give information to drawing pictures */
						AIMaps.hitShip(m, n);
					} else
					{
						/* not Hit */
						PLships[m][n] = 'O';
						Info3.setText("AI missed!");

						/* pass information to drawing */
						AIMaps.MissShips(m, n);
					}
					/* check whether AI wins */
					if (GameOverAIWins())

					{
						Info1.setText("AI Win!");
						checkForPlayAgain();
					}
					unlockPlayer();
				}
			}
		}
	}

	/* this function checks whther a button is pressed by player */
	public boolean isVisited(location a)
	{
		for (location la : visits)
			if (la.equals(a))
				return true;
		return false;
	}

	/* this actionlistener inputs the ships */
	public class InputShips implements ActionListener
	{
		int x, y;

		/* constructor */
		public InputShips(int horizon, int vertical)
		{
			x = horizon;
			y = vertical;
		}

		/* it is a loop from A to null, so that players can change their letter */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			/* if it is 0, change to A */
			if (PLships[x][y] == 0)
			{
				PlayerMap[x][y].setText("A");
				PLships[x][y] = 'A';
			} else
			{
				/* if it is A, change to B */
				char temp = PLships[x][y];
				if (temp == 'A')
				{
					PlayerMap[x][y].setText("B");
					PLships[x][y] = 'B';
				}

				/* if it is B, change to C */
				else if (temp == 'B')
				{
					PlayerMap[x][y].setText("C");
					PLships[x][y] = 'C';
				}

				/* if it is C, change to D */
				else if (temp == 'C')
				{
					PlayerMap[x][y].setText("D");
					PLships[x][y] = 'D';
				}

				/* if it is D, change to S */
				else if (temp == 'D')
				{
					PlayerMap[x][y].setText("S");
					PLships[x][y] = 'S';
				}

				/* if it is S, change to 0 */
				else if (temp == 'S')
				{
					PlayerMap[x][y].setText(null);
					PLships[x][y] = 0;
				}

			}

		}
	}
}
