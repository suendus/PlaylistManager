package com.example.playlistmanager.server;

import com.example.playlistmanager.common.Request;
import com.example.playlistmanager.common.Response;
import com.example.playlistmanager.common.ResponseType;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;
    private boolean running;
    private List<ClientHandler> clients;

    public Server(int port, int connections) throws IOException {
        System.out.println("Starting server on port " + port + "...");
        serverSocket = new ServerSocket(port, connections);
        clients = new ArrayList<>();
        System.out.println("Server started successfully!");
    }

    public void start() {
        running = true;
        System.out.println("Waiting for clients to connect...");
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected from " + socket.getInetAddress().getHostName() + ":" + socket.getPort());
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start(); // start the new thread for this client
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
        System.out.println("Stopping server...");
        try {
            serverSocket.close();
            for (ClientHandler client : clients) {
                Socket socket = client.getSocket();
                socket.close();
                System.out.println("Connection to client at " + socket.getInetAddress().getHostName() + ":" + socket.getPort() + " closed.");
            }
            System.out.println("Server stopped successfully!");
        } catch (IOException e) {
            System.err.println("Error closing server or client sockets: " + e.getMessage());
        }
    }

    public void listen(ClientHandler client, Request request) throws IOException {

        System.out.println("getting response type");
        switch (request.getType()) {
            case VIEW_SONG_LIST:
                System.out.println("Creating songlist:");
                List<Song> songList = FileManager.getSongList();
                System.out.println("creating response: ");
                Response response = new Response(ResponseType.VIEW_SONG_LIST, "Song List", songList);
                try {
                    ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
                    outputStream.writeObject(response);
                    outputStream.flush(); // flush the output stream to make sure the data is sent immediately
                    System.out.println("Sent response: " + response);

                    ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
                    Object receivedObject = inputStream.readObject(); // read the response from the client
                    // do something with the received object

                } catch (IOException e) {
                    System.err.println("Error sending or receiving response to/from client: " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.err.println("Received object of unknown class: " + e.getMessage());
                }
                break;
            case SEARCH_SONG_IN_LIST:
                // Handle search song request
                break;
            case VIEW_ALL_PLAYLISTS:
                // Handle view all playlists request
                break;
            case SEARCH_PLAYLIST:
                // Handle search playlist request
                break;
            case SORT_PLAYLISTS:
                // Handle sort playlists request
                break;
            case CREATE_PLAYLIST:
                // Handle create playlist request
                break;
            case REMOVE_PLAYLIST:
                // Handle remove playlist request
                break;
            case ADD_SONG_TO_PLAYLIST:
                // Handle add song to playlist request
                break;
            case REMOVE_SONG_FROM_PLAYLIST:
                // Handle remove song from playlist request
                break;
            case SEARCH_SONG_IN_PLAYLIST:
                // Handle search song in playlist request
                break;
            case SORT_PLAYLIST_BY_TITLE:
                // Handle sort playlist by title request
                break;
            case SORT_PLAYLIST_BY_ARTIST:
                // Handle sort playlist by artist request
                break;
            case SORT_PLAYLIST_BY_ALBUM:
                // Handle sort playlist by album request
                break;
            case SORT_PLAYLIST_BY_DURATION:
                // Handle sort playlist by duration request
                break;
            case SORT_PLAYLIST_BY_POPULARITY:
                // Handle sort playlist by popularity request
                break;
            case VIEW_ARTIST:
                // Handle view artist request
                break;
            case VIEW_ALBUM:
                // Handle view album request
                break;
            default:
                System.out.println("Unknown request type received: " + request.getType());
                break;
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 8080;
        Server server = new Server(port, 5);

        FileManager.getSongList();
        FileManager.printSongList();
        server.start();
    }

}

