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
import java.util.stream.Collectors;

@RestController
public class LoginController {
    @Autowired
    FirebaseInitializer db;

    @RequestMapping("/login")
    public Object login(String loginPassword, String loginEmail, Model model) throws InterruptedException, ExecutionException {
        List<User> userList = getUsers();
        List<User> loggedIn = userList.stream()
                .filter(u -> loginEmail.equals(u.getEmail()) && loginPassword.equals(u.getPassword()))
                .collect(Collectors.toList());

        if(loggedIn.size() == 1){
            model.addAttribute("user", loggedIn.get(0));
            ModelAndView mav;
            if(loggedIn.get(0).isOwner()){
                mav = new ModelAndView("adminLoginSuccess");
            } else{
                mav = new ModelAndView("loginSuccess");
            }
            return mav;
        } else {
            ModelAndView mav = new ModelAndView("loginFailure");
            return mav;
        }
    }

    @RequestMapping("/signup")
    public Object signUp(String signUpPassword, String signUpEmail, String name, boolean owner) throws ExecutionException, InterruptedException {
        List<User> userList = getUsers();

        List<User> loggedIn = userList.stream()
                .filter(u -> signUpEmail.equals(u.getEmail()))
                .collect(Collectors.toList());

        if(loggedIn.size() == 0){
            User newUser = new User();
            newUser.setEmail(signUpEmail);
            newUser.setPassword(signUpPassword);
            newUser.setName(name);
            newUser.setPurchasedBooks(new ArrayList<>());
            newUser.setShoppingCart(new ArrayList<>());
            newUser.setOwner(owner);
            CollectionReference userCR = db.getFirebase().collection("Users");
            userCR.add(newUser);
            return new ModelAndView("signupSuccess");
        } else {
            return new ModelAndView("signupFailure");
        }
    }

    private List<User> getUsers() throws InterruptedException, ExecutionException {
        List<User> userList = new ArrayList<User>();
        CollectionReference books = db.getFirebase().collection("Users");
        ApiFuture<QuerySnapshot> querySnapshot = books.get();
        for(DocumentSnapshot doc:querySnapshot.get().getDocuments()) {
            User emp = Objects.requireNonNull(doc.toObject(User.class)).withId(doc.getId());
            userList.add(emp);
        }
        return userList;
    }

    @RequestMapping("/")
    public ModelAndView mainPage() {
        return new ModelAndView("index");
    }
}
