package com.example.bookshopV2;

import com.example.bookshopV2.domain.entities.Author;
import com.example.bookshopV2.domain.entities.Book;
import com.example.bookshopV2.repositories.AuthorRepository;
import com.example.bookshopV2.repositories.BookRepository;
import com.example.bookshopV2.services.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final SeedService seedService;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private Scanner scanner;

    @Autowired
    public ConsoleRunner(SeedService seedService, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.seedService = seedService;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        this.seedService.seedAuthors();
//        this.seedService.seedCategories();
//        this.seedService.seedAll();
//        this.getBooksAfter2000();
//        this.getAllAuthorsWIthBookBefore1990();
//        this.getAllAuthorsOrderedByBookCount();
        Scanner scanner = new Scanner(System.in);
        String authorName = scanner.nextLine();
        this.getAllBooksByGivenAuthor(authorName);

    }

    private void getBooksAfter2000() {
        LocalDate year2000 = LocalDate.of(2000, 1, 1);
        List<Book> books = bookRepository.findByReleaseDateAfter(year2000);

        books.forEach(b -> System.out.println(b.getReleaseDate() + " " + b.getTitle()));
    }

    private void getAllAuthorsWIthBookBefore1990() {
        LocalDate year1990 = LocalDate.of(1990, 1, 1);
        List<Author> authors = this.authorRepository.findDistinctByBooksReleaseDateBefore(year1990);

        authors.forEach(a -> System.out.println(a.getFirstName() + " " + a.getLastName()));
    }

    private void getAllAuthorsOrderedByBookCount() {
        List<Author> authors = this.authorRepository.findAll();

        authors.stream()
                .sorted((l, r) -> r.getBooks().size() - l.getBooks().size())
                .forEach(a -> System.out.printf("%s %s -> %d%n",
                        a.getFirstName(), a.getLastName(), a.getBooks().size()));
    }

    private void getAllBooksByGivenAuthor(String name) {
        String[] nameData = name.split("\\s+");
        String firstName = nameData[0];
        String lastName = nameData[1];
        Author author = authorRepository.findByFirstNameAndLastName(firstName, lastName);
        int authorId = author.getId();

        List<Book> books = this.bookRepository.findAllBooksByAuthorIdOrderByReleaseDateDesc(authorId);

        books.forEach(b -> System.out.printf("%s %s %d%n",
                b.getTitle(), b.getReleaseDate(), b.getCopies()));
    }

}
