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
	private boolean gameactive;
	private Vector<ConnectionHandler> ineligibleplayers;
	
	public Game(GameStateHandler gg, ServerUI ui, Vector<ConnectionHandler> idlist, WordDatabase wdb, int numberrounds) {
		this.gg = gg;
		this.ui = ui;
		this.IDList = idlist;
		totalturns = numberrounds * IDList.size();
		this.wdb = wdb;
		ineligibleplayers = new Vector<ConnectionHandler>();
		
		ActionListener clocksetter = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(time > 1) {
					time--;
				}
				else {
					timer.stop();
					startTurn();
					gameactive = false;
					Vector<ConnectionHandler> temp = new Vector<ConnectionHandler>();
					temp.addAll(IDList);
					String scoreBoard = "";
					while(!temp.isEmpty()) {
						int max = -1;
						ConnectionHandler maxc = temp.firstElement();
						for(ConnectionHandler tempc : temp) {
							if(tempc.points > max) {
								max = tempc.points;
								maxc = tempc;
							}
						}
						scoreBoard += maxc.name + ";" + maxc.points + ";";
						temp.remove(maxc);
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
		if(turnnumber < totalturns) {
			playersthatguessedright = 0;
			ineligibleplayers.clear();
			//ineligibleplayers.add(currentplayer);
			currentplayer = IDList.get(turnnumber % IDList.size());
			turnnumber++;
			ui.toLog("GAME: Turn " + turnnumber + " (" + currentplayer.getName() + ")");
			
			String choice1 = "Kappa";
			String choice2 = "FrankerZ";
			String choice3 = "BibleThump";
	
			gg.handleTurn(currentplayer, true);
			gg.sendNewWords(currentplayer, choice1, choice2, choice3);
			ui.toLog("GAME: Words to choose send to " + currentplayer.getName());
		}
		else {
			ui.toLog("GAME: Over.");
		}
	}
	
	//Wird extern durch das Empfangen des gewählten Wortes aufgerufen
	public void startDrawing(String chosenword) {
		currentword = chosenword;
		gameactive = true;
		ui.toLog("GAME: Chosen word received");
		
		time = 90;
		gg.handleTurn(currentplayer, false);
		timer.restart();
		ui.toLog("GAME: Drawing phase initiated.");
	}
	
	//Wird extern durch jede Chateingabe aufgerufen, beendet die Runde falls das Wort richtig war
	public void wordGuessed(ConnectionHandler c) {
		if(!ineligibleplayers.contains(c) && gameactive) {
			gg.sendGameOver(c);
			ui.toLog("GAME: " + c.getName() + " guessed the word.");
			if(playersthatguessedright == 0) {
				time = 10;
				timer.restart();
				currentplayer.points += IDList.size();
			}
			c.points += IDList.size() - playersthatguessedright;
			playersthatguessedright++;
			ineligibleplayers.add(c);
		}
	}
}
