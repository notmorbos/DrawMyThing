package ui;

import ui.Interface;

import java.awt.EventQueue;
import java.util.Vector;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import network.Client;
import network.Server;


public class UI {
	Client client;
	Server server;
	private boolean isDrawing = false;
	
	public static void main(String[] args) 
    {
        EventQueue.invokeLater(new Runnable() {
            public void run() 
            {
                try 
                {
                	Interface window = new Interface();
                	window.initWindow();
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        });
    }
	
	public UI()
	{
		
	}
	
	public void processToChatWindow(String msg)
	{
		//ADD CHAT TEXT
	}
	
	public void updateDrawWindow(int[][] pic)
	{
		
	}
	
	
	public void clientConnected(String IP)
	{
		//GIVE NEW CLIENT MSG
	}
	
	public void clientNameChange(String IP, String oldName, String newName)
	{
		//GIVE CLIENT CHANGED NAME MSG
	}
	
	public void setName(String name)
	{
		client.name = name;
		// SEND CLIENT NAME CHANGE MSG
	}
	
	public void incrementPoints(String list)
	{
		//CHANGE STATS
	}
	
	public void serverSaysHey(String msg)
	{
		//GIVE MSG
	}
	
	public void initiateTurn()
	{
		isDrawing = true;
	}
}
