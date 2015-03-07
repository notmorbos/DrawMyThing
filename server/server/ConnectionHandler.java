package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
public class ConnectionHandler extends Thread
{
	GameStateHandler gg;
	private BufferedReader out;
	private Socket Client;
	String IPg = "Could not detect IP";
	String name = "Newb";
	int points = 0;
	PostMaster p;
	boolean myTurn = false;
	boolean dc = false;
	public ConnectionHandler(Socket s, GameStateHandler gg)
	{
		this.gg = gg;
		Client = s;
	}
	public void run()
	{
		gg.addToList(this);
		try 
		{
			out = new BufferedReader(new InputStreamReader(Client.getInputStream()));
			String string;
			InetAddress IP = Client.getInetAddress();
        	String sIP = IP.toString();
        	sIP = sIP.substring(1);
        	IPg = sIP;
        	System.out.println("NEW CONNECTION: " + IPg);
        	gg.toLog("NEW CONNECTION: " + IPg);
        	

    		p = new PostMaster(IPg, 9902, gg);
    		p.start();
    		String welcomeMessage = "Connection Established. Welcome <3";
    		p.writeMessage("heyu" + welcomeMessage);
	        while((string = out.readLine()) != null)
	        {	
	        	System.out.println("RECEIVING MESSAGE: " + string);
	        	gg.toLog("RECV: " + string);
	        		 if(string.substring(0, 4).equals("draw"))
	        	{
	        		boolean pulling = false;
	 	        	if(string.substring(4, 5).equals("1"))
	 	       		{
	 	       			pulling = true;
	 	       		}
        			int x = Integer.valueOf(string.substring(5, string.indexOf(",")));
        			int y = Integer.valueOf(string.substring(string.indexOf(",") + 1));
        			gg.handlePoint(this, x, y, pulling);
	        	}
	        	else if(string.substring(0, 4).equals("size"))
	 	        {
	        		int size = Integer.valueOf(string.substring(4));
	        		gg.handleSizeChange(this, size);
	 	        }
	        	else if(string.substring(0, 4).equals("colr"))
	 	        {
	        		int rgb = Integer.valueOf(string.substring(4));
	        		gg.handleColorChange(this, rgb);
	 	        }
	        	else if(string.substring(0, 4).equals("text"))
	        	{
	        		String msg = string.substring(4);
	        		gg.handleText(this, msg);
	        	}
	        	else if(string.substring(0, 4).equals("name"))
	        	{
	        		String newName = string.substring(4);
	        		gg.handleConnect(this, newName + " (" + IPg + ") has connected.");
	        		name = newName;
	        		gg.newPlayerConnected(name, IPg);
	        	}
	        	else if(string.substring(0, 4).equals("word"))
	        	{
	        		String msg = string.substring(4);
	        		gg.handleWord(this, msg);
	        	}
	        }
		} 
		catch (IOException e) 
		{
			System.out.println("IOE");
			gg.toLog("IOE");
			dc = true;
    		gg.playerDisconnected(name, IPg);
		}
	}
}
