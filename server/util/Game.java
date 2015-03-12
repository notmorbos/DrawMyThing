package util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Timer;

import server.ConnectionHandler;
import server.GameStateHandler;
import ui.ServerUI;

public class Game {

	private GameStateHandler gg;
	private ServerUI ui;
	public Vector <ConnectionHandler> IDList;
	private WordDatabase wdb;
	
	private Timer timer;
	private int time;
	
	private String currentword;
	private ConnectionHandler currentplayer;
	private int turnnumber = 0;
	private int totalturns;
	private int playersthatguessedright = 0;
	
	public Game(GameStateHandler gg, ServerUI ui, Vector<ConnectionHandler> idlist, WordDatabase wdb) {
		this.gg = gg;
		this.ui = ui;
		this.IDList = idlist;
		totalturns = 2 * IDList.size();
		this.wdb = wdb;
		
		ActionListener clocksetter = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(time > 1) {
					time--;
				}
				else {
					timer.stop();
					startTurn();
					String scoreBoard = "";
					for(int x = 0; x < IDList.size(); x++)
					{
						scoreBoard += IDList.elementAt(x).name + ";" + IDList.elementAt(x).points + ";";
					}
					sendScore(scoreBoard);
				}
			}
		};
		timer = new Timer(1000, clocksetter);
		timer.setInitialDelay(1000);
	}
	
	public void sendScore(String scoreBoard)
	{
		gg.submitScore(scoreBoard);
	}
	
	//Wird zu Beginn jedes Zuges aufgerufen
	public void startTurn() {
		timer.stop();
		playersthatguessedright = 0;
		currentplayer = IDList.get(turnnumber % IDList.size());
		turnnumber++;
		ui.toLog("GAME: Turn " + turnnumber + " (" + currentplayer.getName() + ")");
		
		String choice1 = "Kappa";
		String choice2 = "FrankerZ";
		String choice3 = "BibleThump";

		//gg.submitScore();
		gg.handleTurn(currentplayer, true);
		gg.sendNewWords(currentplayer, choice1, choice2, choice3);
		ui.toLog("GAME: Words to choose send to " + currentplayer.getName());
	}
	
	//Wird extern durch das Empfangen des gewählten Wortes aufgerufen
	public void startDrawing(String chosenword) {
		currentword = chosenword;
		ui.toLog("GAME: Chosen word received");
		
		time = 90;
		gg.handleTurn(currentplayer, false);
		timer.restart();
		ui.toLog("GAME: Drawing phase initiated.");
	}
	
	//Wird extern durch jede Chateingabe aufgerufen, beendet die Runde falls das Wort richtig war
	public void wordGuessed(ConnectionHandler c) {
		gg.sendGameOver(c);
		ui.toLog("GAME: " + c.getName() + " guessed the word.");
		if(playersthatguessedright == 0) {
			time = 10;
			timer.restart();
			currentplayer.points += IDList.size();
		}
		c.points += IDList.size() - playersthatguessedright;
		playersthatguessedright++;
	}
}
