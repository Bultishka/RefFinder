package com.example.reffinder;

public class Game {
    String Name;
    String Release;
    String Type;
    String Link;
    String image;

    public Game(){

    }

    public Game(String name, String release, String type, String link, String image) {
        Name = name;
        Release = release;
        this.Type = type;
        Link = link;
        this.image = image;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRelease() {
        return Release;
    }

    public void setRelease(String release) {
        Release = release;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        this.Link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
