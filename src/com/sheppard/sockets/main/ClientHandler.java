package com.sheppard.sockets.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable
    {
        public ClientHandler(Socket socket)
        {
            this.socket = socket;

            try
            {
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
        
        public void response(String response)
        {
        	try
        	{
        		System.out.println("Responding...");
        		sendMessage(response);

        		
        	}catch(Exception e) {e.printStackTrace(); disconnect();}
        }
        
        public void execute(String command) 
        {
        	
        	System.out.println("Executing '" + command + "'");
        	//Insert execution here
        	String response = command;
        	response(response);
        	
        }

        public void run()
        {
        	try
            {
                String command = "";
                while((command = reader.readLine()) != null && running)
                {
                    execute(command);
                    command = "";
                }
            }catch(Exception e){e.printStackTrace(); disconnect();}
        	
            }
        
        

        private Socket socket;
        private PrintWriter writer;
        private BufferedReader reader;

        private Thread runningThread;
        private boolean running;
    }