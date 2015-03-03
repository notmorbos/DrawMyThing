import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
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
        			//ADD DRAW HERE
	        		//gui.updateDrawWindow(pic)
	        	}
	        	else if(string.substring(0, 4).equals("text"))
	        	{
	        		gui.processToChatWindow(string);
	        	}
	        	else if(string.substring(0, 4).equals("list"))
	        	{
	        		String list = "";
	        		gui.incrementPoints(list);
	        	}
	        	else if(string.substring(0, 4).equals("name"))
	        	{
	        		String newName = "";
	        		String oldName = "";
	        		gui.clientNameChange(IPg, oldName, newName);
	        	}
	        	else if(string.substring(0, 4).equals("turn"))
	        	{
	        		gui.initiateTurn();
	        	}
	        	else if(string.substring(0, 4).equals("newc"))
	        	{
	        		String newIP = "";
	        		gui.clientConnected(newIP);
	        	}
	        	else if(string.substring(0, 4).equals("heyu"))
	        	{
	        		String msg = "";
	        		gui.serverSaysHey(msg);
	        	}
	        }
		} 
		catch (IOException e) 
		{
			System.out.println("DC");
		}
	}
}
