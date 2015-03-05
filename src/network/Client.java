package network;
import java.io.*;
import java.net.*;
public class Client extends Thread
{
    private Socket socket;
    public String ip;
    private int port;
    public String name;
    private BufferedWriter out;

    
    public void run()
    {
    	connect(ip, port, name);   	
    }

    public Client(String IP, int port, String name)
    {
        ip = IP;
        this.port = port;
        this.name = name;
    }
    public void connect(String p_IP, int p_Port, String Name)
    {
        boolean IOE = false;
        while(IOE == false)
        {
            try
            {
                socket = new Socket(p_IP, p_Port);
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                out.flush();
                IOE = true;
            	System.out.println("Connection Established");
            }
            catch (UnknownHostException e)
            {
                IOE = false;
                System.out.println("Connection Failed");
            }
            catch (IOException e)
            {
                IOE = false;
                System.out.println("IOE");
                e.printStackTrace();
            }
        }
    }
    
    public void sendPoint(int x, int y)
    {
        try
        {
        	if(out != null)
        	{
        		String msg = ""
            	System.out.println("Sending message: " + msg);
                out.write(msg + "\n");
                out.flush();
        	}
        }
        catch (IOException e)
        {
        	
        }
    }
    
    public void writeMessage(String msg)
    {
        try
        {
        	if(out != null)
        	{
            	System.out.println("Sending message: " + msg);
                out.write(msg + "\n");
                out.flush();
        	}
        }
        catch (IOException e)
        {
        	
        }
    }
}