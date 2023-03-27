package com.example.playlistmanager.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String playlistName;
    private List<Song> songs;

    public Playlist(String playlistName) {
        this.playlistName = playlistName;
        this.songs = new ArrayList<>();
    }

    public String getName() {
        return playlistName;
    }

    public void addSong(Song song) throws IOException {
        // check if song already exists in the list
        boolean songExists = false;
        List<Song> songList = FileManager.getSongList();
        for (Song s : songList) {
            if (s.getTitle().equals(song.getTitle()) && s.getArtist().equals(song.getArtist()) && s.getAlbum().equals(song.getAlbum())) {
                songExists = true;
                break;
            }
        }

        // add song to the list
        if (songExists) {
            songs.add(song);
        } else {
            Song newSong = new Song(song.getTitle(), song.getArtist(), song.getAlbum(), song.getDuration(), song.getPopularity());
            songs.add(newSong);
        }
    }

    public void removeSong(Song song) {
        if (songs.contains(song)) {
            songs.remove(song);
        } else {
            throw new IllegalArgumentException("Song not found in playlist");
        }
    }


    public List<Song> getSongs() throws Exception {
        if (songs.isEmpty()) {
            throw new Exception("There are no songs in this playlist.");
        }
        return songs;
    }
}
