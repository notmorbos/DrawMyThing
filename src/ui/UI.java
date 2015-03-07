package ui;

import ui.GamePanel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.net.ConnectException;
import java.util.Vector;

import network.Client;
import network.Server;


public class UI {
	public Client client;
	public Server server;
	public GamePanel game;
	public Lobby lobby;
	
	public static void main(String[] args) 
    {
        EventQueue.invokeLater(new Runnable() {
            public void run() 
            {
                try 
                {
                	UI DrawMyThing = new UI();
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        });
    }
	
	public UI()
	{
		server = new Server(9902, this);
		server.start();
		client = new Client("localhost", 9901, "Raijen", this);
		
    	game = new GamePanel(this);
    	
    	lobby = new Lobby(this);
    	lobby.initWindow();
	}
	
	public void processToChatWindow(String msg)
	{
		game.receiveMessage(msg);
	}
	
	public void sendMessage(String msg) {
		client.sendMessage(msg);
	}
	
	public void updateDrawWindow(int x, int y, boolean isDragging)
	{
		game.updateFromServer(new Point(x, y), isDragging);
	}
	
	public void sendPoint(int x, int y, boolean isDragging) {
		client.sendPoint(x, y, isDragging);
	}
	
	public void drawWidthChanged(int width) {
		game.drawWidthChanged(width);
	}
	
	public void sendDrawWidth(int width) {
		client.sendDrawWidth(width);
	}
	
	public void drawColorChanged(int rgb) {
		game.drawColorChanged(new Color(rgb));
	}
	
	public void sendDrawColor(int rgb) {
		client.sendDrawColor(rgb);
	}
	
	public void clientConnected(String IP)
	{
		lobby.newPlayerConnected(IP);
	}
	
	public void receiveChoiceOfWords(String choice1, String choice2, String choice3) {
		game.chooseWord(choice1, choice2, choice3);
	}
	
	public void sendChosenWord(String word) {
		client.sendChosenWord(word);
	}
	
	public void incrementPoints(String list)
	{
		//CHANGE STATS
	}
	
	public void serverSaysHey(String msg)
	{
		System.out.println(msg);
	}
	
	public void setTurn(String player)
	{
		game.setTurn(player);
	}
}
