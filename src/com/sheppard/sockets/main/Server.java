package com.sheppard.sockets.main;

import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable
{    
    public Server()
    {
        started = false;
        serverSocket = null;
    }

    public void start()
    {
        if(!started)
        {
            started = true;

            try
            {
                serverSocket = new ServerSocket(PORT);
                running = true;

                serverThread = new Thread(this);
                serverThread.start();

                System.out.println("Server started!\n");
            }catch(Exception e)
            {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    public void stop()
    {
        running = false;
        started = false;

        if(serverThread != null)
            serverThread.interrupt();
        serverThread = null;
    }

    public void run()
    {
        try
        {
            while(running)
            {
                try
                {
                    Socket client = serverSocket.accept();
                    System.out.println("Client connected.");

                    ClientHandler handler = new ClientHandler(client);

                    handler.sendMessage("Connected.");
                }catch(Exception e){e.printStackTrace();}
            }
        }catch(Exception e){e.printStackTrace();}
    }

    private boolean started;
    private boolean running;
    private ServerSocket serverSocket;
    private Thread serverThread;

    private static final int PORT = 25565;

    public static void main(String args[])
    {
        Server server = new Server();
        server.start();
    }

}