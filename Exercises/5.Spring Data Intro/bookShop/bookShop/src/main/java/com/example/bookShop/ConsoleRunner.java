package com.example.bookShop;

import com.example.bookShop.services.author.AuthorService;
import com.example.bookShop.services.book.BookService;
import com.example.bookShop.services.seed.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private final LocalDate BOOK_YEAR_AFTER = LocalDate.of(2000, 1, 1);
    private final LocalDate BOOK_YEAR_BEFORE = LocalDate.of(1990, 1, 1);

    private final SeedService seedService;
    private final BookService bookService;
    private final AuthorService authorService;


    @Autowired
    public ConsoleRunner(SeedService seedService, BookService bookService, AuthorService authorService) {
        this.seedService = seedService;
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.seedService.seedAllData();
        this.getAllBooksAfterGivenYear();
        this.getAllAuthorsWithBooksReleaseDateBefore();
    }

    private void getAllBooksAfterGivenYear() {
        this.bookService
                .findAllByReleaseDateAfter(BOOK_YEAR_AFTER)
                .forEach(book -> System.out.println(book.getTitle()));
    }

    private void getAllAuthorsWithBooksReleaseDateBefore() {
        this.authorService
                .findDistinctByBooksReleaseDateBefore(BOOK_YEAR_BEFORE)
                .forEach(author -> System.out.println(author.getFirstName() + " " + author.getLastName()));
    }


}
