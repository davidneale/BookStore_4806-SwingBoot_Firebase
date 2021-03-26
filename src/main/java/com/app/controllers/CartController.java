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
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
public class CartController {
    @Autowired
    FirebaseInitializer db;

    @RequestMapping("/shopping")
    public Object shoppingCart(@ModelAttribute("user") User user, Model model) throws ExecutionException, InterruptedException {
        List<Book> bookList = getAllBooks(user.getShoppingCart());
        model.addAttribute("cart", user.getShoppingCart());
        model.addAttribute("bookList", (List<Book>) bookList);
        return new ModelAndView("shoppingCart");
    }

    @RequestMapping("/newShopping")
    public Object newShoppingCart(@ModelAttribute("user") User user, @RequestParam(name = "book") String book, Model model) throws ExecutionException, InterruptedException {
        ArrayList<String> books = user.getShoppingCart();
        books.add(book);
        user.setShoppingCart(books);
        db.getFirebase().collection("Users").document(user.id).set(user);
        List<Book> bookList = getAllBooks(books);
        model.addAttribute("user", user);
        model.addAttribute("cart", user.getShoppingCart());
        model.addAttribute("bookList", (List<Book>) bookList);
        return new ModelAndView("shoppingCart");
    }

    public List<Book> getAllBooks(ArrayList<String> bookIds) throws InterruptedException, ExecutionException {
        List<Book> list = new ArrayList<Book>();
        CollectionReference books = db.getFirebase().collection("Books");
        ApiFuture<QuerySnapshot> querySnapshot= books.get();
        for(DocumentSnapshot doc:querySnapshot.get().getDocuments()) {
            Book emp = Objects.requireNonNull(doc.toObject(Book.class)).withId(doc.getId());
            if(emp.id != null && bookIds.contains(emp.id)){
                list.add(emp);
            }
        }
        return list;
    }
}
