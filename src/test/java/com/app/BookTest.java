package com.app;

import com.app.models.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookTest {




    @Test
    public void testSetName(){
        Book book = new Book();
        book.setName("The Da Vinci Code");
        assertEquals(book.getName(), "The Da Vinci Code");
    }
    @Test
    public void testAuthor(){
        Book book = new Book();
        book.setAuthor("Dan Brown");
        assertEquals(book.getAuthor(), "Dan Brown");
    }
    @Test
    public void testSetGenre(){
        Book book = new Book();
        book.setGenre("thriller");
        assertEquals(book.getGenre(), "thriller");
    }
    @Test
    public void testSetISBN(){
        Book book = new Book();
        book.setIsbn("9780307277671");
        assertEquals(book.getIsbn(), "9780307277671");
    }

    @Test
    public void testSetImg(){
        Book book = new Book();
        book.setImg("https://images-na.ssl-images-amazon.com/images/I/91Q5dCjc2KL.jpg");
        assertEquals(book.getImg(), "https://images-na.ssl-images-amazon.com/images/I/91Q5dCjc2KL.jpg");
    }

    @Test
    public void testSetId(){
        Book book = new Book();
        book.setId("1234");
        assertEquals(book.getId(), "1234");
    }
}
