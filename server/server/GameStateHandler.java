package server;
import java.awt.EventQueue;
import java.util.Vector;

import ui.ServerUI;
import util.Game;
import util.WordDatabase;


public class GameStateHandler 
{
	private ServerUI ui;
	public Vector <ConnectionHandler> IDList;
	private Game game;
	WordDatabase wdb;
	
	private boolean isActive = false;
	public boolean gameActive = false;
	public String chosenWord;
	
	public static void main(String[] args)
	{
		GameStateHandler gg = new GameStateHandler();
	}
	
	public GameStateHandler()
	{
		Server s = new Server(9901, this);
		s.start();
		ui = new ServerUI(this);
		ui.initWindow();
		IDList = new Vector<ConnectionHandler>();
		wdb = new WordDatabase();
		while(isActive == false)
		{
			ui.setGameStartable(IDList.size() > 0);
		}
	}
	
	public void sendGameStart(int numberrounds)
	{
		game = new Game(this, ui, IDList, wdb, numberrounds);
		for(int i = 0; i < IDList.size(); i++)
		{
			IDList.elementAt(i).p.writeMessage("strt");
		}
		game.startTurn();
	}
	
	public void sendGameOver(ConnectionHandler c)
	{
		for(int i = 0; i < IDList.size(); i++)
		{
			IDList.elementAt(i).p.writeMessage("over" + c.name);
		}
	}
	
	public void sendNewWords(ConnectionHandler c, String word1, String word2, String word3) {
		c.p.writeMessage("word" + word1 + "," + word2 + ";" + word3);
	}

	public void handleText(ConnectionHandler c, String msg)
	{
		{
			if(wdb.isGuessCorrect(msg, chosenWord)){
				game.wordGuessed(c);
			}
			else
			{
				for(int i = 0; i < IDList.size(); i++)
				{
						IDList.elementAt(i).p.writeMessage("text" + c.name + ": " + msg);
				}
			}
		}
	}
	
	public void handleNameChange(ConnectionHandler c, String newName, String oldName)
	{
		for(int i = 0; i < IDList.size(); i++)
		{
			IDList.elementAt(i).p.writeMessage("name" + newName + ";" + oldName);
		}
	}
	
	public void submitScore(String scoreBoard)
	{
		for(int i = 0; i < IDList.size(); i++)
		{
			IDList.elementAt(i).p.writeMessage("list" + scoreBoard);
		}
	}
	
	public void handleColorChange(ConnectionHandler c, int rgb)
	{
		for(int i = 0; i < IDList.size(); i++)
		{
			if(IDList.elementAt(i) != c)
			{
				IDList.elementAt(i).p.writeMessage("colr" + rgb);
			}
		}
	}
	
	public void handleSizeChange(ConnectionHandler c, int size)
	{
		for(int i = 0; i < IDList.size(); i++)
		{
			if(IDList.elementAt(i) != c)
			{
				IDList.elementAt(i).p.writeMessage("size" + size);
			}
		}
	}
	
	public void handleWord(ConnectionHandler c, String word)
	{
		chosenWord = word;
		game.startDrawing(word);
	}
	
	public void handleTurn(ConnectionHandler currentplayer, boolean choosing)
	{
	// neuer boolean choosing, true um ein Wort wählen zu lassen, false um
	// das Zeichnen zu beginnen (Form: turn0Markus / turn1Markus)
	// Den alten Code habe ich gelöscht, sollte jetzt überflüssig sein
		for(int i = 0; i < IDList.size(); i++)
		{
			if(choosing) {
				IDList.elementAt(i).p.writeMessage("turn" + "1" + currentplayer.name);
			}
			else {
				IDList.elementAt(i).p.writeMessage("turn" + "0" + currentplayer.name);
			}
		}
	}
	
	public void handleConnect(ConnectionHandler c, String newName)
	{
		for(int i = 0; i < IDList.size(); i++)
		{
			if(IDList.elementAt(i) != c)
			{
				IDList.elementAt(i).p.writeMessage("newc" + newName);
			}
		}
	}
	
	public void handlePoint(ConnectionHandler c, int x, int y, boolean pulling)
	{
		for(int i = 0; i < IDList.size(); i++)
		{
			if(IDList.elementAt(i) != c)
			{
				if(pulling)
				{
					IDList.elementAt(i).p.writeMessage("draw" + "1" + x + "," + y);
				}
				else
				{
					IDList.elementAt(i).p.writeMessage("draw" + "0" + x + "," + y);
				}
			}
		}
	}
	
	public boolean reconnect(ConnectionHandler c)
	{
		boolean success = false;
		for(int i = 0; i < IDList.size(); i++)
		{
			if(IDList.elementAt(i).dc == true)
			{
				if(c.IPg.equals(IDList.elementAt(i).IPg) && c.name.equals(IDList.elementAt(i).name))
				{
					IDList.set(i, c);
					success = true;
					i = IDList.size();
				}
			}
		}
		return success;
	}
	
	public void addToList(ConnectionHandler c)
	{
		if(gameActive == false)
		{
			IDList.add(c);
		}
		else
		{
			if(reconnect(c))
			{
				//SEND U ARE RECONNECTED MSG
			}
		}
	}
	
	public void removeFromlist(ConnectionHandler c)
	{
		if(gameActive == false) 
		{
			IDList.remove(c);
		}
	}
	
	public void toLog(String input) {
		ui.toLog(input);
	}
	
	public void newPlayerConnected(String name, String ip) {
		ui.newPlayerConnected(name, ip);
	}
	
	public void playerDisconnected(String name, String ip) {
		ui.playerDisconnected(name, ip);
	}
}
