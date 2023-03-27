package com.example.playlistmanager.server;

import java.util.List;

public class Album implements Comparable<Album> {
    private String name;
    private String artistName;
    private List<Song> songs;

    public Album(String name, String artistName, List<Song> songs) {
        this.name = name;
        this.artistName = artistName;
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public String getArtistName() {
        return artistName;
    }

    public List<Song> getSongs() {
        return songs;
    }

    @Override
    public int compareTo(Album otherAlbum) {
        return this.name.compareTo(otherAlbum.getName());
    }
}
