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

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

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