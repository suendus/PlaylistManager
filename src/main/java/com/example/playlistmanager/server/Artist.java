package com.example.playlistmanager.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Artist implements Comparable<Artist> {
    private String name;

    private List<Album> discography;

    public Artist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Album> getDiscography(List<Song> songList) {
        Set<String> albumNames = new HashSet<>();
        for (Song song : songList) {
            if (song.getArtist().equals(name)) {
                albumNames.add(song.getAlbum());
            }
        }
        List<Album> discography = new ArrayList<>();
        for (String albumName : albumNames) {
            List<Song> albumSongs = new ArrayList<>();
            for (Song song : songList) {
                if (song.getArtist().equals(name) && song.getAlbum().equals(albumName)) {
                    albumSongs.add(song);
                }
            }
            Album album = new Album(albumName, name, albumSongs);
            discography.add(album);
        }
        Collections.sort(discography);
        return discography;
    }
    public void setDiscography(List<Album> discography) {
        this.discography = discography;
    }

    public String viewArtist(List<Song> songList) {
        StringBuilder sb = new StringBuilder();
        sb.append("Artist: ").append(name).append("\n\n");

        List<Album> discography = getDiscography(songList);

        for (Album album : discography) {
            sb.append(album.toString());
        }

        return sb.toString();
    }
    public int compareTo(Artist other) {
        return this.name.compareTo(other.getName());
    }
}