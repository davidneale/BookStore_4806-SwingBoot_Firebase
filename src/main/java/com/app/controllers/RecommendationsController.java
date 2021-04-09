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

import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
public class RecommendationsController {
    @Autowired
    FirebaseInitializer db;

    @RequestMapping("/recommendations")
    public Object shoppingCart(@ModelAttribute("user") User user, Model model) throws ExecutionException, InterruptedException {
        User updatedUser = (User) db.getFirebase().collection("Users").document(user.id).get().get().toObject(User.class);
        User recommendUser = getRecommendUser(updatedUser);
        if(recommendUser.getPurchasedBooks() == null || updatedUser.getPurchasedBooks()== null){
            model.addAttribute("bookList", (List<Book>) new ArrayList<Book>());
        }
        else {
            List<Book> bookList = getAllBooks(updatedUser.getPurchasedBooks(), recommendUser.getPurchasedBooks());
            model.addAttribute("bookList", (List<Book>) bookList);
        }
        return new ModelAndView("recommendedCart");
    }

    public List<Book> getAllBooks(ArrayList<String> bookIds, ArrayList<String> recommendBookIds) throws InterruptedException, ExecutionException {

        List<Book> list = new ArrayList<Book>();
        CollectionReference books = db.getFirebase().collection("Books");
        ApiFuture<QuerySnapshot> querySnapshot= books.get();
        for(DocumentSnapshot doc:querySnapshot.get().getDocuments()) {
            Book emp = Objects.requireNonNull(doc.toObject(Book.class)).withId(doc.getId());
            if(!(bookIds.contains(emp.id)) && recommendBookIds.contains(emp.id)){
                list.add(emp);
            }
        }
        return list;
    }

    private User getRecommendUser(User user) throws InterruptedException, ExecutionException {
        User recommendedUser = new User();
        double prevJD = 0;
        CollectionReference books = db.getFirebase().collection("Users");
        ApiFuture<QuerySnapshot> querySnapshot = books.get();
        for(DocumentSnapshot doc:querySnapshot.get().getDocuments()) {
            User currUser = Objects.requireNonNull(doc.toObject(User.class)).withId(doc.getId());
            if(!currUser.getEmail().equals(user.getEmail())){
                Set<String> currUserList = new HashSet<String>(currUser.getPurchasedBooks());
                Set<String> userList = new HashSet<String>(user.getPurchasedBooks());
                int currUserListSize = currUserList.size();
                int userListLengthSize = currUserList.size();
                userList.retainAll(currUserList);
                double jd = (1d / (currUserListSize + userListLengthSize - userList.size()) * userList.size());
                if(jd > prevJD){
                    prevJD = jd;
                    recommendedUser = currUser;
                }
            }
        }
        return recommendedUser;
    }
}
