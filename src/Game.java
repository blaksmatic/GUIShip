/* 
 * Author: Blaks Zeng 
 * Purpose: the main class where the game begins
 * Date: 10/13/2014 
 */

public class Game
{

	public static void main(String[] args)
	{
		BattleGUI battle = new BattleGUI();
		battle.clearTable();
		battle.PrepareGame();
	}
}
