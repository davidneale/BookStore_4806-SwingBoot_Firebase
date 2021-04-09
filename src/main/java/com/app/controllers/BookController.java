package com.app.controllers;
import com.app.models.Book;
import com.app.models.User;
import com.app.services.FirebaseInitializer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
public class BookController {
    @Autowired
    FirebaseInitializer db;


    public List<Book> getAllBooks() throws InterruptedException, ExecutionException {
        List<Book> empList = new ArrayList<Book>();
        CollectionReference books = db.getFirebase().collection("Books");
        ApiFuture<QuerySnapshot> querySnapshot= books.get();
        for(DocumentSnapshot doc:querySnapshot.get().getDocuments()) {
            Book emp = Objects.requireNonNull(doc.toObject(Book.class)).withId(doc.getId());
            empList.add(emp);
        }
        return empList;
    }

    @RequestMapping("/bookstore")
    public Object listAllBooks(@ModelAttribute("user") User user, Model model) throws ExecutionException, InterruptedException {
        model.addAttribute("user", user);
        List<Book> bookList = getAllBooks();
        model.addAttribute("bookList", (List<Book>) bookList);
        ModelAndView mav = new ModelAndView("bookstore");
        return mav;
    }

    @RequestMapping("/ownerBookstore")
    public Object listAllBooksOwner(@ModelAttribute("user") User user, Model model) throws ExecutionException, InterruptedException {
        model.addAttribute("user", user);
        List<Book> bookList = getAllBooks();
        model.addAttribute("bookList", (List<Book>) bookList);
        ModelAndView mav = new ModelAndView("ownerBookstore");
        return mav;

    }

    @RequestMapping("/bookEditor")
    public Object editBook(@ModelAttribute("user") User user, @RequestParam(name = "bookId") String book, Model model) throws ExecutionException, InterruptedException {
        Book oldBook = db.getFirebase().collection("Books").document(book).get().get().toObject(Book.class);
        model.addAttribute("book", oldBook);
        model.addAttribute("bookId", book);
        return new ModelAndView("editBook");
    }

    @RequestMapping ("/editBookSuccess")
    public Object editBookSuccess(@ModelAttribute("book") Book book, @ModelAttribute("bookId") String bookId, Model model){


        db.getFirebase().collection("Books").document(bookId).set(book);
        model.addAttribute("book", book);
        return new ModelAndView("/editBookSuccess");
    }
}

