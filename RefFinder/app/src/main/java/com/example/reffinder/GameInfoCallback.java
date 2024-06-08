package com.example.reffinder;

public interface GameInfoCallback {
    void onGameFound(String title, String releaseDate, String logoUrl, int genre, String url);
    void onGameNotFound(String error);
}