package server;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class PostMaster extends Thread
{
	
	private GameStateHandler gg;
	private BufferedWriter out;
	private String ip;
	private int port;
	Socket socket;
	
	public void run()
    {
    	connect(ip, port);   
    	System.out.println("INITIALIZED");
    }

    public PostMaster(String IP, int port, GameStateHandler gg)
    {
        this.ip = IP;
        this.port = port;
        this.gg = gg;
    }
    public void connect(String p_IP, int p_Port)
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
            	System.out.println("P - Connection Established");
            	gg.toLog("CONNECTION ESTABLISHED");
            }
            catch (UnknownHostException e)
            {
                IOE = false;
                System.out.println("Connection Failed");
                gg.toLog("CONNECTION FAILED");
            }
            catch (IOException e)
            {
                IOE = false;
                System.out.println("IOE");
                gg.toLog("I/O EXCEPTION");
                e.printStackTrace();
            }
        }
    }
	public void writeMessage(String msg)
    {
        try
        {
        	if(out != null)
        	{
            	//System.out.println("Sending message: " + msg);
            	//gg.toLog("SEND: " + msg);
                out.write(msg + "\n");
                out.flush();
        	}
        }
        catch (IOException e)
        {
        	
        }
    }
}
