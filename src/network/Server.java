package network;

import java.net.*;
import java.io.*;

import ui.UI;
public class Server extends Thread
{
    private ServerSocket server;
    private int port;
    UI gui;
    public Server(int port, UI gui)
    {
    	this.gui = gui;
        this.port = port;
    }
    
    public void run()
    {
        try 
        {
            server = new ServerSocket(port);
            Socket client;
            client = server.accept();
            ConnectionHandler c = new ConnectionHandler(client, gui);
            c.start();
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
