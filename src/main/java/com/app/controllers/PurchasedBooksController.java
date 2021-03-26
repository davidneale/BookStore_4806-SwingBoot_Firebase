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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
public class PurchasedBooksController {

    @Autowired
    FirebaseInitializer db;

    @RequestMapping("/purchasedBooks")
    public Object newPurchaseBooks(@ModelAttribute("user") User user, Model model) throws ExecutionException, InterruptedException {
        ArrayList<String> books = user.getShoppingCart();
        ArrayList<String> pBooks = user.getPurchasedBooks();
        for(String s : books){
            pBooks.add(s);
        }
        user.setPurchasedBooks(pBooks);
        books.clear();
        user.setShoppingCart(books);
        List<Book> bookList = getAllBooks(pBooks);
        model.addAttribute("user", user);
        model.addAttribute("purchasedBooks", bookList);
        return new ModelAndView("purchasedBooks");
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
