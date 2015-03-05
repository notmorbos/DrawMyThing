package ui;

import ui.GamePanel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.util.Vector;

import network.Client;
import network.Server;


public class UI {
	Client client;
	Server server;
	private GamePanel game;
	
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
		//client = new Client("localhost", 9901, "Markus");
		//client.start();
		
    	game = new GamePanel(this);
    	game.initWindow();
	}
	
	public void processToChatWindow(String msg)
	{
		game.receiveMessage(msg);
	}
	
	public void sendMessage(String msg) {
		client.sendMessage(msg);
	}
	
	public void updateDrawWindow(int x, int y)
	{
		game.updateFromServer(new Point(x, y));
	}
	
	public void sendPoint(int x, int y) {
		client.sendPoint(x, y);
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
		game.receiveMessage(IP + " connected to the game.");
	}
	
	public void clientNameChange(String IP, String oldName, String newName)
	{
		//GIVE CLIENT CHANGED NAME MSG
	}
	
	public void setName(String name)
	{
		client.name = name;
		// SEND CLIENT NAME CHANGE MSG
	}
	
	public void incrementPoints(String list)
	{
		//CHANGE STATS
	}
	
	public void serverSaysHey(String msg)
	{
		//GIVE MSG
	}
	
	public void setTurn(boolean myturn)
	{
		game.setTurn(myturn);
	}
}
