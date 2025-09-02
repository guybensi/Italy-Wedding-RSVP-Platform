package com.wedding.music;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

public class Song {
    private String title;
    private String artist;
    private URI spotifyUrl; // אופציונלי
    private URI youtubeUrl; // אופציונלי

    public Song() {
        this.title = null;
        this.artist = null;
        this.spotifyUrl = null;
        this.youtubeUrl = null;
    }

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public Song(String title, String artist, URI spotifyUrl, URI youtubeUrl) {
        this.title = title;
        this.artist = artist;
        this.spotifyUrl = spotifyUrl;
        this.youtubeUrl = youtubeUrl;
    }

    // Getters / Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public URI getSpotifyUrl() { return spotifyUrl; }
    public void setSpotifyUrl(URI spotifyUrl) { this.spotifyUrl = spotifyUrl; }

    public URI getYoutubeUrl() { return youtubeUrl; }
    public void setYoutubeUrl(URI youtubeUrl) { this.youtubeUrl = youtubeUrl; }

    // עזרים נחמדים
    public boolean hasAnyLink() { return spotifyUrl != null || youtubeUrl != null; }

    /** קישור מועדף (עדיפות ל-Spotify, אחרת YouTube) */
    public Optional<URI> preferredLink() {
        if (spotifyUrl != null) return Optional.of(spotifyUrl);
        if (youtubeUrl != null) return Optional.of(youtubeUrl);
        return Optional.empty();
    }

    // שוויון לפי כותרת+אומן (Case-insensitive)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song s)) return false;
        return Objects.equals(normalize(title), normalize(s.title)) &&
               Objects.equals(normalize(artist), normalize(s.artist));
    }

    @Override
    public int hashCode() {
        return Objects.hash(normalize(title), normalize(artist));
    }

    private static String normalize(String s) {
        return s == null ? null : s.trim().toLowerCase();
    }

    @Override
    public String toString() {
        return (artist == null || artist.isBlank()) ? String.valueOf(title) : title + " — " + artist;
    }
}
