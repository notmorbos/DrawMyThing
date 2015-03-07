package server;
import java.awt.EventQueue;
import java.util.Vector;

import ui.ServerUI;
import util.WordDatabase;


public class GameStateHandler 
{
	private ServerUI ui;
	public Vector <ConnectionHandler> IDList;
	private boolean isActive = false;
	private WordDatabase w = new WordDatabase();
	public boolean gameActive = false;
	private String chosenWord = "";
	private int anzahlRichtig = 0;
	private boolean waitingForWord = true;
	
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
		while(isActive == false)
		{
			ui.setGameStartable(IDList.size() > 0);
		}
	}
	

	public void startGame()
	{
		//Anzahl Runden
		int numberOfRounds = IDList.size() * 2;
		int gameCounter = 0;
		String zuZeichnen = "";
		gameActive = true;
		for (int x = 0; x < numberOfRounds; x++)
		{
			int playerID = x % IDList.size();
			String word1 = w.getRandomWord();
			String word2 = w.getRandomWord();
			String word3 = w.getRandomWord();
			IDList.elementAt(playerID).myTurn = true;
			handleTurn(x, false);
			sendNewWords(IDList.elementAt(playerID), word1, word2, word3);
			while(chosenWord.equals(""))
			{
				// WAIT
			}
			System.out.println(chosenWord);
			waitingForWord = false;
			handleTurn(x, true);
			
			//Gott: ich bin müde. 6 Tage Arbeit reichen, dann bekommt Stefan Böhling halt kein Gehirn mehr.
		}
	}
	
	public void sendGameStart()
	{
		for(int i = 0; i < IDList.size(); i++)
		{
			IDList.elementAt(i).p.writeMessage("strt");
		}
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
	
	public void sendWinMessage(ConnectionHandler c){
		if(anzahlRichtig == 0){
			c.p.writeMessage("text" + "Glückwunsch! Du hast es als erstes erraten! (3 Punkte)");
			c.points = c.points + 3;
		}else if(anzahlRichtig >= 1){
			c.p.writeMessage("text" + "Das Wort wurde bereits erraten, du belegst den " + anzahlRichtig+1 + ". Platz. (1 Punkt)");
			c.points = c.points + 1;
		}
	}

	public void handleText(ConnectionHandler c, String msg)
	{
		{
			for(int i = 0; i < IDList.size(); i++)
			{
				if(gameActive && IDList.elementAt(i) == c && w.isGuessCorrect(msg, chosenWord)){
					sendWinMessage(c);
					anzahlRichtig++;
				}
				else
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
	// WORD HAS BEEN CHOSEN
	}
	
	public void handleTurn(int playerID, boolean choosing) 
	{
		// neuer boolean choosing, true um ein Wort wählen zu lassen, false um
		// das Zeichnen zu beginnen (Form: turn0Markus / turn1Markus)
		// Den alten Code habe ich gelöscht, sollte jetzt überflüssig sein
		
		if(playerID < IDList.size()) 
		{
			for(int i = 0; i < IDList.size(); i++)
			{
				if(choosing) {
					IDList.elementAt(i).p.writeMessage("turn" + "1" + IDList.elementAt(playerID).name);
				}
				else {
					IDList.elementAt(i).p.writeMessage("turn" + "0" + IDList.elementAt(playerID).name);
				}
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
