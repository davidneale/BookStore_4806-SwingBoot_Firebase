package com.app.models;

import org.springframework.stereotype.Component;

@Component
public class Book extends Model {
    private String name;
    private String author;
    private String img;
    private String genre;
    private String isbn;
    private int cost;

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getImg() { return img; }

    public String getGenre() { return genre; }

    public int getCost() { return cost;}

    public String getIsbn() { return isbn;}


}