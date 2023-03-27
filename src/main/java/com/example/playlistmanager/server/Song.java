package com.example.playlistmanager.server;

public class Song {
    private String title;
    private String artist;
    private String album;
    private int duration;
    private int popularity;

    public Song(String title, String artist, String album, int duration, int popularity) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public int getDuration() {
        return duration;
    }

    public int getPopularity() {
        return popularity;
    }

    public String toString() {
        return "Title: " + this.title + "\nArtist: " + this.artist + "\nAlbum: " + this.album + "\nDuration: " + this.duration + "\nPopularity: " + this.popularity;
    }

}