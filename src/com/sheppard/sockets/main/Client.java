package com.sheppard.sockets.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable
{
    public Client()
    {
        try
        {
            socket = new Socket(IP, PORT);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            running = true;

            runningThread = new Thread(this);
            runningThread.start();
        }catch(Exception e){e.printStackTrace(); disconnect();}
    }

    public void disconnect()
    {
        running = false;
        if(runningThread != null)
            runningThread.interrupt();
        runningThread = null;

        try
        {
            reader.close();
        }catch(Exception e){}
        reader = null;

        try
        {
            writer.close();
        }catch(Exception e){}
        writer = null;
        try
        {
            socket.close();
        }catch(Exception e){}
        socket = null;
    }

    public void sendMessage(String message)
    {
        if(running)
        {
            writer.println(message);
            writer.flush();
        }
    }
    
    public void response()
    {
    	try
    	{
    		
    		String response = "";
    		while((response = reader.readLine()) != null && running)
    		{
    			System.out.println("Recieved Response '" + response + "'");
    			disconnect();
    		}
    	}catch(Exception e) {disconnect();} finally {disconnect();}
    }

    public void run()
    {
        try
        {
            Scanner scan = new Scanner(System.in);
        	String message = "";
            while((message = reader.readLine()) != null && running)
            {
                System.out.println(message);

                System.out.println("Enter Command: ");
                String command = scan.nextLine();
                
                sendMessage(command);
                command = "";
                scan.close();
                response();
            }
        }catch(Exception e){disconnect();}
        
    	}

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private Thread runningThread;
    private boolean running;

    private static final int PORT = 25565;
    private static final String IP = "70.69.217.16";

    public static void main(String args[])
    {
        new Client();
    }
}