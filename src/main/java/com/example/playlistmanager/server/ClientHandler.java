package com.example.playlistmanager.server;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import com.example.playlistmanager.common.Request;
import com.example.playlistmanager.common.Response;
import com.example.playlistmanager.common.ResponseType;

public class ClientHandler implements Runnable {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private StringBuilder messageBuffer;
    private Server server;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageBuffer = new StringBuilder();
    }

    public void run() {
        try {
            // send menu of actions when client first connects
            System.out.println("Sending menu to client...");
            sendMenu();


            ObjectInputStream ois = new ObjectInputStream(inputStream);
            while (true) {
                Request request = (Request) ois.readObject();
                System.out.println("Received request from client: " + request.getType());
                server.listen(this, request); // pass the request to the server
                System.out.println("Going to server listen: ");
                sendMenu(); // send menu of actions after a request has been fulfilled
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to parse request: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading request: " + e.getMessage());
        }
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Socket getSocket() {
        return socket;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    // send a menu of actions to the client
    private void sendMenu() throws IOException {
        List<String> menu = Arrays.asList(
                "View Song List",
                "Search Song in List",
                "View All Playlists",
                "Search Playlist",
                "Sort Playlists",
                "Create Playlist",
                "Remove Playlist",
                "Add Song to Playlist",
                "Remove Song from Playlist",
                "Search Song in Playlist",
                "Sort Playlist by Title",
                "Sort Playlist by Artist",
                "Sort Playlist by Album",
                "Sort Playlist by Duration",
                "Sort Playlist by Popularity",
                "View Artist",
                "View Album",
                "Please enter the number of the action you want to perform: "
        );
        Response response = new Response(ResponseType.MENU, "Menu", menu);
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(response);
    }
}
