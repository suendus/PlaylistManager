package com.example.playlistmanager.client;

import com.example.playlistmanager.common.Request;
import com.example.playlistmanager.common.RequestType;
import com.example.playlistmanager.common.Response;
import com.example.playlistmanager.common.ResponseType;
import com.example.playlistmanager.server.Playlist;
import com.example.playlistmanager.server.Song;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {
    private final String hostname;
    private final int port;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final Scanner scanner;
    private static final int VIEW_SONG_LIST = 1;
    private static final int SEARCH_SONG_IN_LIST = 2;
    private static final int VIEW_ALL_PLAYLISTS = 3;
    private static final int SEARCH_PLAYLIST = 4;
    private static final int SORT_PLAYLISTS = 5;
    private static final int CREATE_PLAYLIST = 6;
    private static final int REMOVE_PLAYLIST = 7;
    private static final int ADD_SONG_TO_PLAYLIST = 8;
    private static final int REMOVE_SONG_FROM_PLAYLIST = 9;
    private static final int SEARCH_SONG_IN_PLAYLIST = 10;
    private static final int SORT_PLAYLIST_BY_TITLE = 11;
    private static final int SORT_PLAYLIST_BY_ARTIST = 12;
    private static final int SORT_PLAYLIST_BY_ALBUM = 13;
    private static final int SORT_PLAYLIST_BY_DURATION = 14;
    private static final int SORT_PLAYLIST_BY_POPULARITY = 15;
    private static final int VIEW_ARTIST = 16;
    private static final int VIEW_ALBUM = 17;
    private static final int EXIT = 18;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        scanner = new Scanner(System.in);
    }

    public boolean connect() {
        try {
            socket = new Socket(hostname, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Failed to disconnect from server: " + e.getMessage());
        }
    }

    public void run() {
        boolean running = true;
        while (running) {
            try {
                Response response = (Response) inputStream.readObject();
                System.out.println("Received response of type: " + response.getType());
                if (response.getType() == ResponseType.ERROR) {
                    System.err.println("Server error: " + response.getMessage());
                    continue;
                }
                Object data = response.getData();
                System.out.println("Received data of type: " + data.getClass());

                List<String> menu = (List<String>) response.getData();
                System.out.println("Menu:");
                for (int i = 0; i < menu.size(); i++) {
                    System.out.println((i + 1) + ". " + menu.get(i));
                }
                System.out.print("Enter a number: ");
                int choice = Integer.parseInt(getUserInput());

                Request request;
                switch (choice) {
                    case VIEW_SONG_LIST:
                        System.out.println("Requesting song list...");
                        request = new Request(RequestType.VIEW_SONG_LIST);
                        System.out.println("Song list requested!");
                        break;
                    case SEARCH_SONG_IN_LIST:
                        System.out.print("Enter the search query: ");
                        String query = getUserInput();
                        request = new Request(RequestType.SEARCH_SONG_IN_LIST, query);
                        break;
                    case VIEW_ALL_PLAYLISTS:
                        request = new Request(RequestType.VIEW_ALL_PLAYLISTS);
                        break;
                    case SEARCH_PLAYLIST:
                        System.out.print("Enter the search query: ");
                        query = getUserInput();
                        request = new Request(RequestType.SEARCH_PLAYLIST, query);
                        break;
                    case SORT_PLAYLISTS:
                        request = new Request(RequestType.SORT_PLAYLISTS);
                        break;
                    case CREATE_PLAYLIST:
                        System.out.print("Enter the playlist name: ");
                        String name = getUserInput();
                        request = new Request(RequestType.CREATE_PLAYLIST, name);
                        break;
                    case REMOVE_PLAYLIST:
                        System.out.print("Enter the playlist name: ");
                        name = getUserInput();
                        request = new Request(RequestType.REMOVE_PLAYLIST, name);
                        break;
                    case ADD_SONG_TO_PLAYLIST:
                        System.out.print("Enter the playlist name: ");
                        name = getUserInput();
                        System.out.print("Enter the song title: ");
                        String title = getUserInput();
                        System.out.print("Enter the song artist: ");
                        String artist = getUserInput();
                        System.out.print("Enter the song album: ");
                        String album = getUserInput();
                        System.out.print("Enter the song duration (in seconds): ");
                        int duration = Integer.parseInt(getUserInput());
                        System.out.print("Enter the song plays: ");
                        int popularity = Integer.parseInt(getUserInput());
                        Song song = new Song(title, artist, album, duration, popularity);
                        request = new Request(RequestType.ADD_SONG_TO_PLAYLIST, name, song);
                        break;

                    case REMOVE_SONG_FROM_PLAYLIST:
                        System.out.print("Enter the playlist name: ");
                        name = getUserInput();
                        System.out.print("Enter the song title: ");
                        title = getUserInput();
                        System.out.print("Enter the song artist: ");
                        artist = getUserInput();
                        request = new Request(RequestType.REMOVE_SONG_FROM_PLAYLIST, name, new Song(title, artist, null, 0, 0));
                        break;
                    case SEARCH_SONG_IN_PLAYLIST:
                        System.out.print("Enter the playlist name: ");
                        name = getUserInput();
                        System.out.print("Enter the search query: ");
                        query = getUserInput();
                        request = new Request(RequestType.SEARCH_SONG_IN_PLAYLIST, name, query);
                        break;
                    case SORT_PLAYLIST_BY_TITLE:
                        System.out.print("Enter the playlist name: ");
                        name = getUserInput();
                        request = new Request(RequestType.SORT_PLAYLIST_BY_TITLE, name);
                        break;
                    case SORT_PLAYLIST_BY_ARTIST:
                        System.out.print("Enter the playlist name: ");
                        name = getUserInput();
                        request = new Request(RequestType.SORT_PLAYLIST_BY_ARTIST, name);
                        break;
                    case SORT_PLAYLIST_BY_DURATION:
                        System.out.print("Enter the playlist name: ");
                        name = getUserInput();
                        request = new Request(RequestType.SORT_PLAYLIST_BY_DURATION, name);
                        break;
                    case SORT_PLAYLIST_BY_POPULARITY:
                        System.out.print("Enter the playlist name: ");
                        name = getUserInput();
                        request = new Request(RequestType.SORT_PLAYLIST_BY_POPULARITY, name);
                        break;
                    case VIEW_ARTIST:
                        System.out.print("Enter the artist name: ");
                        String artistName = getUserInput();
                        request = new Request(RequestType.VIEW_ARTIST, artistName);
                        break;
                    case VIEW_ALBUM:
                        System.out.print("Enter the album name: ");
                        String albumName = getUserInput();
                        request = new Request(RequestType.VIEW_ALBUM, albumName);
                        break;
                    case EXIT:
                        System.out.println("Goodbye!");
                        running = false;
                        request = new Request(RequestType.EXIT);
                        break;
                    default:
                        System.out.println("Invalid input, please try again.");
                        continue;
                }
                outputStream.writeObject(request);
                outputStream.flush();

                System.out.println("Waiting for Response...");

                response = (Response) inputStream.readObject();
                if (response.getType() == ResponseType.ERROR) {
                    System.err.println("Server error: " + response.getMessage());
                    continue;
                }
                data = response.getData();
                System.out.println("Received data of type: " + data.getClass());


                switch (response.getType()) {
                    case VIEW_SONG_LIST:
                        List<Song> songList = (List<Song>) response.getData();
                        System.out.println("Song List:");
                        for (Song s : songList) {
                            System.out.println(s);
                        }
                        break;
                    case SEARCH_SONG_IN_LIST:
                        songList = (List<Song>) response.getData();
                        System.out.println("Search results:");
                        for (Song s : songList) {
                            System.out.println(s);
                        }
                        break;
                    case VIEW_ALL_PLAYLISTS:
                        List<String> playlists = (List<String>) response.getData();
                        System.out.println("Playlists:");
                        for (String p : playlists) {
                            System.out.println(p);
                        }
                        break;
                    case SEARCH_PLAYLIST:
                        playlists = (List<String>) response.getData();
                        System.out.println("Search results:");
                        for (String p : playlists) {
                            System.out.println(p);
                        }
                        break;
                    case SORT_PLAYLISTS:
                        List<Playlist> sortedPlaylists = (List<Playlist>) response.getData();
                        System.out.println("Sorted Playlists:");
                        for (Playlist p : sortedPlaylists) {
                            System.out.println(p);
                        }
                        break;
                    case CREATE_PLAYLIST:
                        System.out.println("Playlist created successfully.");
                        break;
                    case REMOVE_PLAYLIST:
                        System.out.println("Playlist removed successfully.");
                        break;
                    case ADD_SONG_TO_PLAYLIST:
                        System.out.println("Song added to playlist successfully.");
                        break;
                    case REMOVE_SONG_FROM_PLAYLIST:
                        System.out.println("Song removed from playlist successfully.");
                        break;
                    case SEARCH_SONG_IN_PLAYLIST:
                        songList = (List<Song>) response.getData();
                        System.out.println("Search results:");
                        for (Song s : songList) {
                            System.out.println(s);
                        }
                        break;
                    default:
                        System.err.println("Unknown response type received.");
                        break;

                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Communication error with server: " + e.getMessage());
                running = false;
            } catch (NumberFormatException e) {
                System.err.println("Invalid input: please enter a number.");
            }
        }
    }

    private String getUserInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        return input;
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 8080);
        if (client.connect()) {
            System.out.println("Connected to server");
            client.run();
            //client.disconnect();
        }
    }


}
