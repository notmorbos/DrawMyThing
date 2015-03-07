package network;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import ui.UI;
public class ConnectionHandler extends Thread
{
	private BufferedReader out;
	private Socket Client;
	String IPg = "Could not detect IP";
	UI gui;
	
	public ConnectionHandler(Socket s, UI gui)
	{
		Client = s;
		this.gui = gui;
	}
	public void run()
	{
		try 
		{
			out = new BufferedReader(new InputStreamReader(Client.getInputStream()));
			String string;
			InetAddress IP = Client.getInetAddress();
        	String sIP = IP.toString();
        	sIP = sIP.substring(1);
        	IPg = sIP;
			gui.clientConnected(IPg);
	        while((string = out.readLine()) != null)
	        {	
	        	if(string.substring(0, 4).equals("draw"))
	        	{
	        		boolean pulling = false;
	        		if(string.substring(4, 5).equals("1"))
	        		{
	        			pulling = true;
	        		}
        			int x = Integer.valueOf(string.substring(5, string.indexOf(",")));
        			int y = Integer.valueOf(string.substring(string.indexOf(",")+1));
	        		gui.updateDrawWindow(x, y, pulling);
	        	}
	        	else if(string.substring(0, 4).equals("size"))
	 	        {
	        		int x = Integer.valueOf(string.substring(4));
	 	        	gui.drawWidthChanged(x);
	 	        }
	 	        else if(string.substring(0, 4).equals("colr"))
	 	        {     	
	 	        	int rgb = Integer.valueOf(string.substring(4));
	 	        	gui.drawColorChanged(rgb);
	 	        }
	        	else if(string.substring(0, 4).equals("text"))
	        	{
	        		gui.processToChatWindow(string.substring(4));
	        	}
	        	else if(string.substring(0, 4).equals("list"))
	        	{
	        		String list = "";
	        		gui.incrementPoints(list);
	        	}
	        	else if(string.substring(0, 4).equals("strt"))
	        	{
	        		gui.lobby.gameStarted();
	        	}
	        	else if(string.substring(0, 4).equals("word"))
	        	{
	        		String word1 = string.substring(4, string.indexOf(","));
	        		String word2 = string.substring(string.indexOf(",") + 1, string.indexOf(";"));
	        		String word3 = string.substring(string.indexOf(";") + 1);
	        	}
	        	else if(string.substring(0, 4).equals("over"))
	        	{
	        		// Wort wurde erraten
	        	}
	        	else if(string.substring(0, 4).equals("time"))
	        	{
	        		// Wort wurde erraten, noch 10 sek
	        	}
	        	else if(string.substring(0, 4).equals("turn"))
	        	{
	        		gui.setTurn(string.substring(4));
	        	}
	        	else if(string.substring(0, 4).equals("newc"))
	        	{
	        		String name = string.substring(4);
	        		gui.clientConnected(name);
	        	}
	        	else if(string.substring(0, 4).equals("heyu"))
	        	{
	        		System.out.println("WUT");
	        		gui.processToChatWindow(string.substring(4));
	        	}
	        }
		} 
		catch (IOException e) 
		{
			System.out.println("DC");
			gui.lobby.showDisconnected();
		}
	}
}
