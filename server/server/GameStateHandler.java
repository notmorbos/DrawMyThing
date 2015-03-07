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
	private boolean gameActive = false;
	private String s = "";
	private int anzahlRichtig = 0;
	
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
		//GAME ON HOLD, PLAYERS CONNECTING NOW
		while(isActive == false)
		{
			ui.setGameStartable(IDList.size() > 0);
		}
	}
	

	public void startGame()
	{
		//ID des Spielers am Zug
		int amZug = 0;
		//Anzahl Runden
		int numberOfRounds = IDList.size() * 2;
		int gameCounter = 0;
		String zuZeichnen = "";
		gameActive = true;
		while(gameActive){
			s = w.getRandomWord();
			IDList.elementAt(amZug).myTurn = true;
			zuZeichnen = "Du bist am Zug. Dein Wort ist " + s + ".";
			sendPrivateMessage(IDList.elementAt(amZug), zuZeichnen); // SendPrivateMessage ist hier definitiv nicht richtig, sendNewWords eher
			while(!(anzahlRichtig >= IDList.size())){
				//ja, was tut man hier?  - warten bis das wort erraten wurde, oder der timer abgelaufen ist.
				//hints geben? - das soll es geben?
				//verdammt, ich brauch n timer. irgendwas was seperat l�uft. - http://docs.oracle.com/javase/7/docs/api/java/util/Timer.html
				//der Text sollte ja gehandlet werden. also parallel. in handleText wird auch anzahlRichtig erh�ht.
			}
			//n�chster Spieler ist dran
			if(amZug == IDList.size()){
				amZug = 0;
			}else{
				amZug++;
			}
			//Spielende
			gameCounter++;
			if(gameCounter > numberOfRounds){
				gameActive = false;
			}
			//Gott, ich bin m�de.
		}
	}
	
	public void sendGameStart()
	{
		for(int i = 0; i < IDList.size(); i++)
		{
			IDList.elementAt(i).p.writeMessage("strt");
		}
	}
	
	public void sendGameOver()
	{
		for(int i = 0; i < IDList.size(); i++)
		{
			IDList.elementAt(i).p.writeMessage("over");
		}
	}
	
	private void sendPrivateMessage(ConnectionHandler c, String msg) {
		c.p.writeMessage("text" + msg);
	}
	
	private void sendNewWords(ConnectionHandler c, String word1, String word2, String word3) {
		c.p.writeMessage("word" + word1 + "," + word2 + ";" + word3);
	}
	
	private void sendWinMessage(ConnectionHandler c){
		if(anzahlRichtig == 0){
			c.p.writeMessage("text" + "Gl�ckwunsch! Du hast es als erstes erraten! (3 Punkte)");
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
				if(gameActive && IDList.elementAt(i) == c && w.isGuessCorrect(msg, s)){
					sendWinMessage(c);
					anzahlRichtig++;
				}else if(IDList.elementAt(i) != c){
					IDList.elementAt(i).p.writeMessage("text" + IDList.elementAt(i).name + ": " + msg);
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
	
	public void handleTurn(int playerID) {
		
		//NUR ZUM TESTEN, kann gerne �berarbeitet werden.
		
		String nextplayer;
		if(playerID < IDList.size()) {
			nextplayer = IDList.elementAt(playerID).name;
		}
		else {
			nextplayer = "Niemand";
		}
		for(int i = 0; i < IDList.size(); i++)
		{
			IDList.elementAt(i).p.writeMessage("turn" + nextplayer);
		}
	}
	
	public void addToList(ConnectionHandler c)
	{
		IDList.add(c);
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
