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

    		p = new PostMaster(IPg, 9902);
    		p.start();
    		String welcomeMessage = "Connection Established. Choose a name now, or you will be a newb 4evar!!1!";
    		p.writeMessage("heyu" + welcomeMessage);
	        while((string = out.readLine()) != null)
	        {	
	        	System.out.println("RECIEVING MESSAGE: " + string);
	        		 if(string.substring(0, 4).equals("draw"))
	        	{
        			int x = Integer.valueOf(string.substring(4, string.indexOf(",")));
        			int y = Integer.valueOf(string.substring(string.indexOf(",")));
        			System.out.println(x + "" + y);
	        	}
	        	else if(string.substring(0, 4).equals("size"))
	 	        {
	        		int size = Integer.valueOf(string.substring(4));
	 	        }
	        	else if(string.substring(0, 4).equals("colr"))
	 	        {
	        		int rgb = Integer.valueOf(string.substring(4));
	 	        }
	        	else if(string.substring(0, 4).equals("text"))
	        	{
	        		String msg = string.substring(4);
	        		System.out.println(msg);
	        	}
	        	else if(string.substring(0, 4).equals("name"))
	        	{
	        		String newName = string.substring(4);
	        		gg.handleNameChange(this, string.substring(4), name);
	        		name = newName;
	        	}
	        }
		} 
		catch (IOException e) 
		{
			System.out.println("IOE");
		}
	}
}
