package server;
import java.awt.EventQueue;
import java.util.Vector;

import ui.ServerUI;


public class GameStateHandler 
{
	private ServerUI ui;
	private Vector <ConnectionHandler> IDList;
	private boolean isActive = false;
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
			ui.setGameStartable(IDList.size() > 1);
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
	
	public void handleText(ConnectionHandler c, String msg)
	{
		for(int i = 0; i < IDList.size(); i++)
		{
			IDList.elementAt(i).p.writeMessage("text" + c.name + ";" + msg);
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
	
	public void handleNameChange(ConnectionHandler c, String newName)
	{
		for(int i = 0; i < IDList.size(); i++)
		{
			if(IDList.elementAt(i) != c)
			{
				IDList.elementAt(i).p.writeMessage("name" + newName);
			}
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
}
