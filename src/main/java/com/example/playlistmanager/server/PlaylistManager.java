package com.example.playlistmanager.server;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import com.example.playlistmanager.server.Artist;


public class PlaylistManager {
    private List<Playlist> playlists;
    private List<Song> songSearch;
    private List<Song> songList;

    public PlaylistManager() {
        playlists = new ArrayList<>();
        songSearch = new ArrayList<>();
        songList = new ArrayList<>();
    }

    public List<Song> getAllSongs() {
        List<Song> allSongs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            try {
                allSongs.addAll(playlist.getSongs());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return allSongs;
    }
    public static List<Song> searchSongsByCriteria(List<Song> songs, String title, String artist) {
        List<Song> matchingSongs = new ArrayList<>();
        for (Song song : songs) {
            if ((title == null || song.getTitle().equalsIgnoreCase(title)) &&
                    (artist == null || song.getArtist().equalsIgnoreCase(artist))) {
                matchingSongs.add(song);
            }
        }
        return matchingSongs;
    }
    public static List<Song> searchSongInList(String title, String artist) throws Exception {
        List<Song> songList = FileManager.getSongList();
        List<Song> matchingSongs = searchSongsByCriteria(songList, title, artist);
        if (matchingSongs.isEmpty()) {
            throw new Exception("Song not found.");
        }
        return matchingSongs;
    }


    public List<Playlist> getAllPlaylists() {
        return playlists;
    }

    public List<Song> getPlaylist(Playlist playlist) throws Exception {
        return playlist.getSongs();
    }

    public List<Playlist> searchPlaylistByName(String keyword) {
        return playlists.stream()
                .filter(playlist -> playlist.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void sortPlaylistByName(Playlist playlist) throws Exception {
        Collections.sort(playlist.getSongs(), (s1, s2) -> s1.getTitle().compareTo(s2.getTitle()));
    }

    public void addPlaylist(String name) {
        playlists.add(new Playlist(name));
    }

    public void removePlaylist(Playlist playlist) {
        playlists.remove(playlist);
    }

    public void addSongToPlaylist(Playlist playlist, Song song) throws IOException {
        playlist.addSong(song);
    }

    public void removeSongFromPlaylist(Playlist playlist, Song song) {
        playlist.removeSong(song);
    }
    public static List<Song> searchSongsInPlaylist(Playlist playlist, String title, String artist) throws Exception {
        List<Song> songs = playlist.getSongs();
        List<Song> matchingSongs = searchSongsByCriteria(songs, title, artist);
        if (matchingSongs.isEmpty()) {
            throw new Exception("Song not found.");
        }
        return matchingSongs;
    }
    public void sortPlaylistByTitle(Playlist playlist) throws Exception {
        Collections.sort(playlist.getSongs(), (s1, s2) -> s1.getTitle().compareTo(s2.getTitle()));
    }
    public void sortPlaylistByArtist(Playlist playlist) throws Exception {
        if (playlist == null) {
            throw new Exception("Playlist is null.");
        }
        Collections.sort(playlist.getSongs(), (s1, s2) -> s1.getArtist().compareTo(s2.getArtist()));
    }

    public void sortPlaylistByAlbum(Playlist playlist) throws Exception {
        if (playlist == null) {
            throw new Exception("Playlist has no songs.");
        }
        Collections.sort(playlist.getSongs(), (s1, s2) -> s1.getAlbum().compareTo(s2.getAlbum()));
    }

    public void sortPlaylistByDuration(Playlist playlist) throws Exception {
        if (playlist == null) {
            throw new Exception("Playlist has no songs.");
        }
        Collections.sort(playlist.getSongs(), (s1, s2) -> Integer.compare(s1.getDuration(), s2.getDuration()));
    }

    public void sortPlaylistByPopularity(Playlist playlist) throws Exception {
        if (playlist == null) {
            throw new Exception("Playlist has no songs.");
        }
        Collections.sort(playlist.getSongs(), (s1, s2) -> Integer.compare(s1.getPopularity(), s2.getPopularity()));
    }


    public void shufflePlaylist(Playlist playlist) throws Exception {
        Collections.shuffle(playlist.getSongs());
    }
    public static Artist getArtist(String artistName, List<Song> songList) {
        for (Artist artist : FileManager.getArtistList(songList)) {
            if (artist.getName().equalsIgnoreCase(artistName)) {
                return artist;
            }
        }
        return null;
    }

    public static String viewArtistDiscography(String artistName, List<Song> songList) {
        StringBuilder sb = new StringBuilder();
        sb.append("Discography for ").append(artistName).append(":\n\n");

        Artist artist = new Artist(artistName);
        List<Album> discography = artist.getDiscography(songList);

        for (Album album : discography) {
            sb.append(album.toString()).append("\n");
        }

        return sb.toString();
    }

    public static Album viewAlbum(String albumName, String artistName, List<Song> songList) {
        for (Album album : getArtist(artistName, songList).getDiscography(songList)) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }
        return null;
    }

}
