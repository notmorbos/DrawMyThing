package server;
import java.awt.EventQueue;
import java.util.Vector;


public class GameStateHandler 
{
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
		IDList = new Vector<ConnectionHandler>();
		//GAME ON HOLD, PLAYERS CONNECTING NOW
		while(isActive == false)
		{
			if(IDList.size() > 1)
			{
				//ENABLE UI GAME START BUTTON
			}
		}
	}
	
	public void handleText(ConnectionHandler c, String msg)
	{
		for(int i = 0; i < IDList.size(); i++)
		{
			if(IDList.elementAt(i) != c)
			{
				IDList.elementAt(i).p.writeMessage("text" + IDList.elementAt(i).name + ";" + msg);
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
	
	public void addToList(ConnectionHandler c)
	{
		IDList.add(c);
	}
}
