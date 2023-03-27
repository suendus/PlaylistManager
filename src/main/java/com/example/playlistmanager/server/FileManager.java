package com.example.playlistmanager.server;

import java.io.*;
import java.util.*;

public class FileManager {
    public static String readFile(String filePath) {
        BufferedReader reader = null;
        try {
            // Read file contents
            String line;
            StringBuilder contentBuilder = new StringBuilder();
            reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line);
            }
            return contentBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close reader
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static List<Song> getSongList() throws IOException {
        List<Song> songList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\suendus\\IdeaProjects\\PlaylistManagerServer\\src\\main\\java\\com\\example\\playlistmanager\\songlist.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length == 5) {
                String title = fields[0].trim();
                String artist = fields[1].trim();
                String album = fields[2].trim();
                int duration = Integer.parseInt(fields[3].trim());
                int popularity = Integer.parseInt(fields[4].trim());
                Song song = new Song(title, artist, album, duration, popularity);
                songList.add(song);
            }
        }
        reader.close();
        return songList;
    }

    public static void printSongList() throws IOException {
        List<Song> songList = getSongList();
        for (Song song : songList) {
            System.out.println(song);
        }
    }


    public static List<Artist> getArtistList(List<Song> songList) {
        Set<String> artistNames = new HashSet<>();
        for (Song song : songList) {
            artistNames.add(song.getArtist());
        }
        List<Artist> artists = new ArrayList<>();
        for (String artistName : artistNames) {
            Artist artist = new Artist(artistName);
            List<Album> discography = artist.getDiscography(songList);
            artist.setDiscography(discography);
            artists.add(artist);
        }
        Collections.sort(artists);
        return artists;
    }


}