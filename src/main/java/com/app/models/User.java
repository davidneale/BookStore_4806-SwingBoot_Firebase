package com.app.models;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class User extends Model {
    private String name;
    private String email;
    private String password;
    private ArrayList<String> shoppingCart;
    private ArrayList<String> purchasedBooks;
    private boolean isOwner;

    public boolean isOwner() { return isOwner; }

    public void setOwner(boolean owner) { isOwner = owner; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public ArrayList<String> getShoppingCart() { return shoppingCart; }

    public ArrayList<String> getPurchasedBooks() { return purchasedBooks; }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPurchasedBooks(ArrayList<String> purchasedBooks) {
        this.purchasedBooks = purchasedBooks;
    }

    public void setShoppingCart(ArrayList<String> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
}