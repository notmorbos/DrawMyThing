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
	Client client;
	Server server;
	GamePanel game;
	Lobby lobby;
	
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
		client = new Client("localhost", 9901, "Raijen");
		
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
		lobby.newPlayerConnected(IP);
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
		System.out.println(msg);
	}
	
	public void setTurn(boolean myturn)
	{
		game.setTurn(myturn);
	}
}
