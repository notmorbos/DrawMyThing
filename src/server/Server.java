package server;

import java.net.*;
import java.io.*;
public class Server extends Thread
{
    private ServerSocket server;
    private int port;
    GameStateHandler gg;
    public Server(int port, GameStateHandler gg)
    {
    	this.gg = gg;
        this.port = port;
    }
    
    public void run()
    {
        try 
        {
            server = new ServerSocket(port);
            Socket client;
            client = server.accept();
            ConnectionHandler c = new ConnectionHandler(client, gg);
            
        } 
        catch (UnknownHostException e) 
        {
            System.out.println("UNKNOWN HOST");
        } 
        catch (IOException e) 
        {
        	System.out.println("IOE");
        }
        
    }
}
