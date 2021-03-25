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
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.ModelAttribute;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RestController;
        import org.springframework.web.servlet.ModelAndView;

        import javax.annotation.PostConstruct;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Objects;
        import java.util.concurrent.ExecutionException;
        import java.util.stream.Collectors;

@RestController
public class DashboardController {
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

    @RequestMapping("/addBookSuccess")
    public Object addBook(String name, String author, String img, String genre, String isbn, Integer cost, Model model) throws ExecutionException, InterruptedException {
        List<Book> bookList = getAllBooks();

        List<Book> filteredBooks = bookList.stream()
                .filter(b -> isbn.equals(b.getIsbn()))
                .collect(Collectors.toList());

        if(filteredBooks.size() == 0){
            Book newBook = new Book();
            newBook.setName(name);
            newBook.setAuthor(author);
            newBook.setImg(img);
            newBook.setGenre(genre);
            newBook.setIsbn(isbn);
            newBook.setCost(cost);
            CollectionReference bookCR = db.getFirebase().collection("Books");
            bookCR.add(newBook);
            return new ModelAndView("/addBookSuccess");
        } else {
            return new ModelAndView("addBookFailure");
        }
    }
}

