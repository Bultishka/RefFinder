package com.example.reffinder;

public class Game {
    String Description;
    String Name;
    Integer Release;
    String Type;
    String Link;
    int image;

    public Game(){

    }

    public Game(String description, String name, Integer release, String type, String link, int image) {
        Description = description;
        Name = name;
        Release = release;
        this.Type = type;
        Link = link;
        this.image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getRelease() {
        return Release;
    }

    public void setRelease(Integer release) {
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
